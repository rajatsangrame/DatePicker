package com.example.rajat.meeting.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rajat Sangrame on 31/12/19.
 * http://github.com/rajatsangrame
 */
public class BindingUtils {


    public static String getDuration(String startTime, String endTime) {

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.getDefault());
            Date dateObj = sdf.parse(startTime);
            assert dateObj != null;
            startTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(dateObj);

            dateObj = sdf.parse(endTime);
            assert dateObj != null;
            endTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(dateObj);

            return String.format("%s - %s", startTime, endTime);

        } catch (final ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
