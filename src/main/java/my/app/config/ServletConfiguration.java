package my.app.config;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import my.app.util.ResourceUtil;

// configuration for servlets
@Configuration
public class ServletConfiguration implements ServletContextInitializer, WebServerFactoryCustomizer<WebServerFactory> {

  private final Logger log = LoggerFactory.getLogger(ServletConfiguration.class);

  private final ApplicationProperties config;

  public ServletConfiguration(ApplicationProperties config) {
    this.config = config;
  }

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    if (config.getHttp().isCacheHeadersEnabled()) {
      log.debug("Registering HTTP Cache headers filter");
      FilterRegistration.Dynamic httpCacheHeadersFilter = servletContext.addFilter("httpCacheHeadersFilter",
          new HttpCacheHeadersFilter(config));
      httpCacheHeadersFilter.setAsyncSupported(true);

      EnumSet<DispatcherType> types = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
      httpCacheHeadersFilter.addMappingForUrlPatterns(types, true, "/static/*");
    }
  }

  @Override
  public void customize(WebServerFactory server) {
    if (server instanceof ConfigurableServletWebServerFactory) {
      ConfigurableServletWebServerFactory servletWebServer = (ConfigurableServletWebServerFactory) server;
      String prefixPath = ResourceUtil.resolvePathBuildPrefix(this.getClass());
      File root = new File(prefixPath + "build/resources/main/static");
      if (root.exists() && root.isDirectory()) {
        servletWebServer.setDocumentRoot(root);
      }
    }
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration cors = config.getHttp().getCors();
    if (cors.getAllowedOrigins() != null && !cors.getAllowedOrigins().isEmpty()) {
      log.debug("Registering CORS filter");
      source.registerCorsConfiguration("/api/**", cors);
    }
    return new CorsFilter(source);
  }

  // a simple CORS filter implementation
  public static class HttpCacheHeadersFilter implements Filter {

    private long cacheTimeToLive;

    private ApplicationProperties config;

    public HttpCacheHeadersFilter(ApplicationProperties config) {
      this.config = config;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
      cacheTimeToLive = TimeUnit.DAYS.toMillis(config.getHttp().getTimeToLiveInDays());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

      HttpServletResponse httpResponse = (HttpServletResponse) response;

      httpResponse.setHeader("Cache-Control", "max-age=" + cacheTimeToLive + ", public");
      httpResponse.setHeader("Pragma", "cache");
      httpResponse.setDateHeader("Expires", System.currentTimeMillis() + cacheTimeToLive);

      chain.doFilter(request, response);
    }
  }
}
