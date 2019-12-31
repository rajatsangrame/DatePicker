package com.example.rajat.meeting.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.rajat.meeting.R;

/**
 * Created by Rajat Sangrame on 30/12/19.
 * http://github.com/rajatsangrame
 */
public class Helper {

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }


    public static void showAlert(Context context, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.alert))
                .setMessage(message)
                .setNegativeButton("Close", null)
                .show();

    }

    public static void showMessage(Context context, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setNegativeButton("Close", null)
                .show();

    }

    private Helper() {
    }
}
