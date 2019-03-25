package my.app.service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import my.app.domain.User;

@Service
public class MailService {

  private final Logger log = LoggerFactory.getLogger(MailService.class);

  private final JavaMailSender javaMailSender;

  private final MessageSource messageSource;

  private final SpringTemplateEngine templateEngine;

  public MailService(JavaMailSender javaMailSender, MessageSource messageSource, SpringTemplateEngine templateEngine) {
    this.javaMailSender = javaMailSender;
    this.messageSource = messageSource;
    this.templateEngine = templateEngine;
  }

  @Async
  public void sendRawEmail(String to, String subject, String content) {
    try {

      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
      message.setTo(to);
      message.setFrom("hello@benjaminbillet.fr"); // TODO make it configurable
      message.setSubject(subject);
      message.setText(content, true);
      javaMailSender.send(mimeMessage);

      log.debug("Sent mail to {}: {}, {}", to, subject, content);
    } catch (Exception e) {
      log.warn("Sent mail to {} failed", to, e);
    }
  }

  @Async
  public void sendTemplateEmail(User user, String templateName, String titleKey) {
    Locale locale = Locale.forLanguageTag("fr"); // TODO add language to the User entity and use it here

    Context context = new Context(locale);
    context.setVariable("user", user);
    context.setVariable("baseUrl", "localhost:8080/api"); // TODO make it configurable

    String content = templateEngine.process(templateName, context);
    String subject = messageSource.getMessage(titleKey, null, locale);
    sendRawEmail(user.getEmail(), subject, content);
  }

  @Async
  public void sendActivationEmail(User user) {
    sendTemplateEmail(user, "activation-mail", "mail.activation.title");
  }
}
