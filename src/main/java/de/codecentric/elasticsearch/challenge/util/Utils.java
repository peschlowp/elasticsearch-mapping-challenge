package de.codecentric.elasticsearch.challenge.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

public final class Utils {

  public static Date fromISO8601(String date) {
    DateTime dateTime = dateTimefromISO8601(date);
    if (dateTime != null) {
      return dateTime.toDate();
    }
    return null;
  }

  public static DateTime dateTimefromISO8601(String date) {
    if (StringUtils.isNotBlank(date)) {
      try {
        return ISODateTimeFormat.dateOptionalTimeParser().withZone(DateTimeZone.UTC).parseDateTime(date);
      } catch (IllegalArgumentException e) {
        // Do nothing.
      }
    }
    return null;
  }

  private Utils() {
    super();
  }
}
