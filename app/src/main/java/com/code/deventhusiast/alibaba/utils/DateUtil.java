package com.code.deventhusiast.alibaba.utils;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Created by Ethiel on 26/10/2017.
 */

public class DateUtil {

    public static String dateToElapsed(String time) {

        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime created_at = df.parseDateTime(time);

//        DateTime created_at = new DateTime(millis);
        Period period = new Period(created_at, DateTime.now());

        PeriodFormatterBuilder periodFormatter = new PeriodFormatterBuilder();
        if (period.getYears() != 0) {
            periodFormatter.appendYears().appendSuffix(" ans ");
        } else if (period.getMonths() != 0) {
            periodFormatter.appendMonths().appendSuffix(" mois ");
        } else if (period.getWeeks() != 0) {
            periodFormatter.appendWeeks().appendSuffix(" semaines ");
        } else if (period.getDays() != 0) {
            periodFormatter.appendDays().appendSuffix(" jours ");
        } else if (period.getHours() != 0) {
            periodFormatter.appendHours().appendSuffix(" heures ");
        } else if (period.getMinutes() != 0) {
            periodFormatter.appendMinutes().appendSuffix(" minutes ");
        } else if (period.getSeconds() != 0) {
            periodFormatter.appendSeconds().appendSuffix(" secondes ");
        }
        PeriodFormatter formatter = periodFormatter.printZeroNever().toFormatter();

        return formatter.print(period).trim();
    }
}
