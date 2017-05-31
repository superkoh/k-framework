package me.superkoh.kframework.core.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * 时间格式处理工具
 * Created by zhangyh on 2017/5/23.
 */
public class DateTimeHelper {
    public static Long timestampOfDateTimeStringAtChina(String dateTimeStr, String dateTimePattern) {
        return timestampOfDateTimeString(dateTimeStr, dateTimePattern, ZoneId.of("Asia/Shanghai"));
    }

    public static Long timestampOfDateTimeString(String dateTimeStr, String dateTimePattern, ZoneId timezone) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(dateTimePattern))
                .atZone(timezone).toEpochSecond();
    }

    public static Long timestampOfDateStringAtChina(String dateStr, String datePattern) {
        return timestampOfDateString(dateStr, datePattern, ZoneId.of("Asia/Shanghai"));
    }

    public static Long timestampOfDateString(String dateStr, String datePattern, ZoneId timezone) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(datePattern)).atTime(0, 0)
                .atZone(timezone).toEpochSecond();
    }

    public static String stringOfCurrentTimeAtChina(String pattern) {
        return stringOfTimestampAtChina(Instant.now().getEpochSecond(), pattern);
    }

    public static String stringOfTimestampAtChina(Long timestamp, String pattern) {
        return stringOfTimestamp(timestamp, pattern, ZoneId.of("Asia/Shanghai"));
    }

    public static String stringOfTimestamp(Long timestamp, String pattern, ZoneId timezone) {
        return Instant.ofEpochSecond(timestamp).atZone(timezone).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static int daysBetweenTime(Long srcTimestamp, ZoneId srcTimezone, Long destTimestamp, ZoneId destTimezone) {
        LocalDate fromDate = Instant.ofEpochSecond(srcTimestamp).atZone(srcTimezone).toLocalDate();
        LocalDate destDate = Instant.ofEpochSecond(destTimestamp).atZone(destTimezone).toLocalDate();
        return Period.between(fromDate, destDate).getDays();
    }
}
