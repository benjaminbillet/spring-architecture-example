package my.app.api;

import java.time.ZonedDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping("/api")
public class UtilsEndpoint {

  // this method will be invoked when the http endpoint /api/echo will be called
  // the "echo" query parameter (?echo=truc) will be passed to the function
  @GetMapping("/echo")
  @Timed
  public ResponseEntity<String> getEcho(String echo) {
    return ResponseEntity.ok(echo);
  }

  // this method will be invoked when the http endpoint /api/time will be called
  // the date will be automatically serialized to json
  @GetMapping("/time")
  @Timed
  public ResponseEntity<ZonedDateTime> getTime() {
    return ResponseEntity.ok(ZonedDateTime.now());
  }

  // this method will be invoked when the http endpoint /api/ping will be called
  @GetMapping("/ping")
  @Timed
  public ResponseEntity<String> getPing() {
    return ResponseEntity.ok("pong");
  }
}
