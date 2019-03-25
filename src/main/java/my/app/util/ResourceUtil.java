package my.app.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public final class ResourceUtil {
  public static String resolvePathBuildPrefix(Class<?> clazz) {
    String pathToClass;
    try {
      pathToClass = URLDecoder.decode(clazz.getResource("").getPath(), StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      pathToClass = clazz.getResource("").getPath();
    }
    String rootPath = Paths.get(".").toUri().normalize().getPath();
    String extractedPath = pathToClass.replace(rootPath, "");
    int idx = extractedPath.indexOf("build/");
    if (idx <= 0) {
      return "";
    }
    return extractedPath.substring(0, idx);
  }

  private ResourceUtil() {
  }
}
