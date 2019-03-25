package my.app.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import my.app.auth.JwtTokenProvider;
import my.app.util.AuthUtil;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  private final UserDetailsService userDetailsService;

  private final JwtTokenProvider tokenProvider;

  private final SecurityProblemSupport problemSupport;

  private final CorsFilter corsFilter;

  public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder, UserDetailsService userDetailsService, JwtTokenProvider tokenProvider, SecurityProblemSupport problemSupport, CorsFilter corsFilter) {
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.userDetailsService = userDetailsService;
    this.tokenProvider = tokenProvider;
    this.problemSupport = problemSupport;
    this.corsFilter = corsFilter;
}

  @PostConstruct
  public void init() {
    try {
      authenticationManagerBuilder
          .userDetailsService(userDetailsService)
          .passwordEncoder(passwordEncoder());
      // example of a static super-user additional authentication mechanism
      // in production, make the login/password configurable from properties
      // authenticationManagerBuilder
      //     .inMemoryAuthentication()
      //     .withUser("superadmin")
      //     .password(passwordEncoder().encode("password"))
      //     .authorities(AuthUtil.ADMIN);
    } catch (Exception e) {
      throw new BeanInitializationException("Security configuration failed", e);
    }
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
      .antMatchers(HttpMethod.OPTIONS, "/**") // no restrictions for OPTIONS requests
      .antMatchers("/static/**"); // no restriction on static resources
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable() // disable CSRF protection (the auth token is explicitly sent by the client in the Authorization header)
      .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class) // adds the CORS filter
      .exceptionHandling()
      .authenticationEntryPoint(problemSupport) // deal with RFC7807 when authentication exception are thrown
      .accessDeniedHandler(problemSupport) // deal with RFC7807 when authentication exception are thrown
    .and()
      .headers()
      .frameOptions().disable() // disable X-Frame header(s) https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Frame-Options
    .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // do not create an HttpSession
    .and()
      .authorizeRequests()
      .antMatchers("/api/register").permitAll()
      .antMatchers("/api/activate").permitAll()
      .antMatchers("/api/authenticate").permitAll()
      .antMatchers("/api/time").permitAll()
      .antMatchers("/api/ping").permitAll()
      .antMatchers("/api/echo").permitAll()
      .antMatchers("/api/public/**").permitAll()
      // protect the metrics and health endpoints
      .antMatchers("/actuator/prometheus").hasIpAddress("127.0.0.1")
      .antMatchers("/actuator/**").hasAuthority(AuthUtil.ADMIN)
    .and()
      .apply(new JwtConfiguration(tokenProvider));
  }
}
