package my.app.util;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class MiscUtil {
  public static <K, V> Map<K, V> toMap(Object... items) {
    if (items == null) {
      return null;
    }
    if (items.length % 2 != 0) {
      throw new IllegalArgumentException("Odd number of elements");
    }
    Map<K, V> map = new HashMap<>(items.length);
    for (int i = 0; i < items.length; i += 2) {
      map.put((K) items[i], (V) items[i + 1]);
    }
    return map;
  }

  private MiscUtil() {
  }
}
