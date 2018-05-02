package app.utils;

import java.time.ZonedDateTime;

public class DateUtil {
    public static String GetDateTimeNow() {
        return ZonedDateTime.now().toString();
    }
}
