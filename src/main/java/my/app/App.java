package my.app;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import my.app.config.ApplicationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);

	private final Environment env;

	// the environment and properties parameter will be injected by spring
	public App(Environment env, ApplicationProperties properties) {
		this.env = env;
		log.info("The application properties: {}", properties);
	}
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@PostConstruct
	public void initApplication() {
		log.info("The application '{}' is running!", env.getProperty("spring.application.name"));
		log.info("Application active profiles: {}", Arrays.asList(env.getActiveProfiles()));
		log.info("Is application in dev: {}", env.acceptsProfiles(Profiles.of("dev", "development")));
	}
}