package pro.quicksense.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class TimezoneUtil {

    /**
     * convert timestamp(UTC) to ICT
     */
    public static String convertUTC2ICT(String utcTimeString) throws ParseException {
        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate = utcFormat.parse(utcTimeString);
        SimpleDateFormat ictFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ictFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok")); // 使用 "Asia/Bangkok" 时区
        return ictFormat.format(utcDate);
    }

    /**
     * For comparison purpose, convert a time string(ICT) to a java Date object
     */
    public static Date convertTimeStringToDateObject(String ictTimeString) throws ParseException {
        SimpleDateFormat ictFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ictFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
        return ictFormat.parse(ictTimeString);
    }

    /**
     * Return a string with format of yyyy-MM-dd
     */
    public static String getCurrentDateByYYYYMMDD() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }
}
