package com.beckytech.mathsgrade8amharic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AppRate {
    private static final String PREF_NAME = "apprater";
    private static final String DONTSHOWAGAIN = "dontshowagain";
    private static final String LAUNCH_COUNT = "launch_count";
    private static final String DATE_FIRSTLAUNCH = "date_firstlaunch";

    private final static int DAYS_UNTIL_PROMPT = 3;
    private final static int LAUNCHES_UNTIL_PROMPT = 3;

    public static void app_launched(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (prefs.getBoolean(DONTSHOWAGAIN, false)) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        long launch_count = prefs.getLong(LAUNCH_COUNT, 0) + 1;
        editor.putLong(LAUNCH_COUNT, launch_count);

        long date_firstLaunch = prefs.getLong(DATE_FIRSTLAUNCH, 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong(DATE_FIRSTLAUNCH, date_firstLaunch);
        }

        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(activity, editor);
            }
        }

        editor.apply();
    }

    private static void showRateDialog(final Activity activity, final SharedPreferences.Editor editor) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        builder.setTitle("Enjoying the app?")
                .setMessage("If you enjoy using " + activity.getString(R.string.app_name) + ", please take a moment to rate it. Thanks for your support!")
                .setPositiveButton("Rate Now", (dialog, which) -> {
                    AppUpdateReviewHelper.showReviewDialog(activity);
                    if (editor != null) {
                        editor.putBoolean(DONTSHOWAGAIN, true);
                        editor.commit();
                    }
                })
                .setNeutralButton("Remind me later", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("No, thanks", (dialog, which) -> {
                    if (editor != null) {
                        editor.putBoolean(DONTSHOWAGAIN, true);
                        editor.commit();
                    }
                    dialog.dismiss();
                })
                .show();
    }
}
