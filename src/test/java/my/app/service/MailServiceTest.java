package my.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.spring5.SpringTemplateEngine;

import my.app.App;
import my.app.domain.User;
import my.app.util.UserTestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class MailServiceTest {

  @Autowired
  private MessageSource messageSource;

  @Autowired
  private SpringTemplateEngine templateEngine;

  @Spy
  private JavaMailSenderImpl javaMailSender;

  @Captor
  private ArgumentCaptor<MimeMessage> messageCaptor;

  @Autowired
  private MailService mailService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    doNothing().when(javaMailSender).send(any(MimeMessage.class));
    mailService = new MailService(javaMailSender, messageSource, templateEngine);
  }

  @Test
  public void testSendActivationEmail() throws Exception {
    User user = UserTestUtil.createJohnDoe(null);
    mailService.sendActivationEmail(user);
    verify(javaMailSender).send(messageCaptor.capture());

    MimeMessage message = messageCaptor.getValue();
    assertThat(message.getAllRecipients()[0].toString()).isEqualTo(user.getEmail());
    assertThat(message.getFrom()[0].toString()).isEqualTo("hello@benjaminbillet.fr");
    assertThat(message.getContent().toString()).isNotEmpty();
    assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
  }
}
