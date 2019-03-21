package my.app;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZoneId;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import my.app.config.ApplicationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);

	private final Environment env;

	public App(Environment env) {
		this.env = env;
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@PostConstruct
	public void initApplication() {
		// deal with a key store for https
		String protocol = "http";
		if (env.getProperty("server.ssl.key-store") != null) {
				protocol = "https";
		}

		String appName = env.getProperty("spring.application.name", "<unamed>");

		String serverPort = env.getProperty("server.port", "8080");
		String serverHost = resolveServerHost();

		String contextPath = env.getProperty("server.servlet.contextPath");
		if (StringUtils.isBlank(contextPath)) {
			contextPath = "/";
		}

		logAppInfo(appName, protocol, serverHost, serverPort, contextPath);
	}

	private String resolveServerHost() {
		String host = "localhost";
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.warn("The host name could not be determined, use localhost");
		}
		return host;
	}

	private void logAppInfo(String appName, String protocol, String hostName, String serverPort, String contextPath) {
		log.info(
			"\n======================================================\n" +
			"Application '{}' is running\n" +
			"- local access: \t{}://localhost:{}{}\n" +
			"- external access: \t{}://{}:{}{}\n" +
			"- locale: \t\t{}\n" +
			"- timezone: \t\t{}\n" +
			"- profiles: \t\t{}\n" +
			"======================================================",
			appName,
			protocol,
			serverPort,
			contextPath,
			protocol,
			hostName,
			serverPort,
			contextPath,
			Locale.getDefault(),
			ZoneId.systemDefault(),
			env.getActiveProfiles());
	}
}