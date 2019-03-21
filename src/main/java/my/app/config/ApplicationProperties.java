package my.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// every key that starts with "myapp" in the application properties and matches the attributes of this class will be injected automatically
@ConfigurationProperties(prefix = "myapp", ignoreUnknownFields = true)
public class ApplicationProperties {
}
