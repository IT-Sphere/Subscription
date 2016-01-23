package ru.itsphere.subscription.common.format;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class contains date formats for the app
 */
public class AppDateFormat {

    public static String formatDateWithHoursAndMinutes(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }
}
