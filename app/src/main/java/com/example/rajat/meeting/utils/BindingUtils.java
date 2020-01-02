package com.example.rajat.meeting.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rajat Sangrame on 31/12/19.
 * http://github.com/rajatsangrame
 */
public class BindingUtils {


    public static String getEndTime(String endTime) {

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.getDefault());
            Date dateObj = sdf.parse(endTime);
            assert dateObj != null;
            return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(dateObj);

        } catch (final ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getStartTime(String startTime) {

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.getDefault());
            Date dateObj = sdf.parse(startTime);
            assert dateObj != null;
            return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(dateObj);

        } catch (final ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getUsers(List<String> list) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));

            if (i != list.size() - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();

    }
}
