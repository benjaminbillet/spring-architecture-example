package my.app.util;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JsonUtil {

  public static final ObjectMapper mapper = createObjectMapper();

  public static ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

  public static byte[] toJsonBytes(Object object) throws IOException {
    return mapper.writeValueAsBytes(object);
  }

  private JsonUtil() {
  }
}
