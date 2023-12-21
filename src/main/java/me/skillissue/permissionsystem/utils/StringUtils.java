package me.skillissue.permissionsystem.utils;

import java.util.HashMap;

public class StringUtils {
  public static String formatMessage(String message) {
    HashMap<String, String> messages = Config.getInstance().messages;
    for (String key : messages.keySet()) {
      if (!message.contains("%" + key)) {
        continue;
      }
      message = message.replace("%" + key, formatMessage(messages.get(key)));
    }
    return message;
  }

  public static String formatTime(long time, int accuracy) {
    if (time == 0) {
      return "Lifetime";
    }
    StringBuilder result = new StringBuilder();
    long[] units = {31104000000L, 2592000000L, 86400000, 3600000, 60000, 1000};
    String[] unitNames = {"y", "M", "d", "h", "m", "s"};
    for (int i = 0; i < units.length; i++) {
      if (time >= units[i]) {
        result.append(time / units[i]).append(unitNames[i]);
        time %= units[i];
        accuracy--;
      }
      if (accuracy == 0) {
        break;
      }
    }
    return result.toString();
  }

  public static long parseTime(String time) {
    if (time.isEmpty()) {
      return 0;
    }
    long result = 0;
    String[] split = time.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
    for (int i = 0; i < split.length; i += 2) {
      result += Integer.parseInt(split[i]) * parseUnit(split[i + 1]);
    }
    return result;
  }

  private static long parseUnit(String unit) {
   return switch (unit) {
    case "s" -> 1000;
    case "m" -> 60000;
    case "h" -> 3600000;
    case "d" -> 86400000;
    case "M" -> 2592000000L;
    case "y" -> 31104000000L;
    default -> 0;
   };
  }
}
