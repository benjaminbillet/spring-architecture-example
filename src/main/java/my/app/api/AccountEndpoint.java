package my.app.api;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Timed;
import my.app.api.errors.BadRequestException;
import my.app.api.errors.InternalServerErrorException;
import my.app.api.errors.InvalidPasswordException;
import my.app.config.ApplicationProperties;
import my.app.domain.User;
import my.app.dto.UserDto;
import my.app.service.MailService;
import my.app.service.UserService;
import my.app.util.AuthUtil;
import my.app.vdo.UserVdo;

@RestController
@RequestMapping("/api/auth")
public class AccountEndpoint {
  private final UserService userService;

  private final ApplicationProperties config;

  private final MailService mailService;

  public AccountEndpoint(ApplicationProperties config, UserService userService, MailService mailService) {
    this.config = config;
    this.userService = userService;
    this.mailService = mailService;
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  @Timed
  public void registerAccount(@Valid @RequestBody UserVdo vdo) {
    if (vdo.getId() != null) {
      throw new BadRequestException(config, "A new resource cannot already have an ID", UserDto.ENTITY_NAME, "id-provided");
    }
    if (!AuthUtil.checkPasswordLength(vdo.getPassword())) {
      throw new InvalidPasswordException(config);
    }
    User user = userService.registerUser(vdo, vdo.getPassword());
    if (config.isMailEnabled()) {
      mailService.sendActivationEmail(user);
    }
  }

  @GetMapping("/activate")
  @Timed
  public void activateAccount(@RequestParam(value = "key") String key) {
    if (!userService.activateRegistration(key)) {
      throw new InternalServerErrorException(config, "No user was found for this activation key");
    }
  }

  @GetMapping("/account")
  @Timed
  public UserDto getAccount() {
    return userService.getUserWithAuthorities()
        .orElseThrow(() -> new InternalServerErrorException(config, "User could not be found"));
  }
}
