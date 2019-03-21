package my.app.util;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface RestUtil {
  public static <T> ResponseEntity<T> toResponseEntity(Optional<T> response) {
    return toResponseEntity(response, null);
  }

  public static <T> ResponseEntity<T> toResponseEntity(Optional<T> maybeResponse, HttpHeaders headers) {
    return maybeResponse.map(response -> ResponseEntity.ok().headers(headers).body(response))
            .orElse(ResponseEntity.notFound().build());
  }
}
