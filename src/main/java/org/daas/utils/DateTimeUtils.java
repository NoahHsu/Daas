package org.daas.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
  private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public static LocalDateTime toLocalDateTime(String datetimeStr) {
    return LocalDateTime.parse(datetimeStr, FORMATTER);
  }

  public static boolean isAfterOrEquals(LocalDateTime a, LocalDateTime b) {
    return a.isAfter(b) || a.isEqual(b);
  }

  public static boolean isBeforeOrEquals(LocalDateTime a, LocalDateTime b) {
    return a.isBefore(b) || a.isEqual(b);
  }

  public static int compareSafely(LocalDateTime datetime1, LocalDateTime datetime2) {
    LocalDateTime safeDatetime1 = datetime1 == null ? LocalDateTime.MIN : datetime1;
    LocalDateTime safeDatetime2 = datetime1 == null ? LocalDateTime.MIN : datetime2;
    return safeDatetime1.compareTo(safeDatetime2);
  }

}
