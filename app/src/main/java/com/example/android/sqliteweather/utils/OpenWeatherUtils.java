package com.example.android.sqliteweather.utils;

import android.content.Context;
import android.text.TextUtils;

import com.example.android.sqliteweather.R;

import java.util.Calendar;
import java.util.TimeZone;

public class OpenWeatherUtils {
    private final static String FIVE_DAY_FORECAST_DEFAULT_TIMEZONE = "UTC";
    private final static String TIMEZONE_OFFSET_FORMAT_STR = "GMT%0+3d:%02d";

    /**
     * Converts a Unix epoch time (e.g. `dt` from the OpenWeather API) plus a timezone offset
     * to a calendar with the correct timezone for the specified offset.
     *
     * @param epoch A Unix epoch timestamp, in seconds.
     * @param tzOffsetSeconds A timezone offset, in seconds.
     *
     * @return Returns a Calendar object representing the time specified by `epoch`, with the
     * timezone correctly set to the one specified by `tzOffsetSeconds`.
     */
    public static Calendar dateFromEpochAndTZOffset(int epoch, int tzOffsetSeconds) {
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone(FIVE_DAY_FORECAST_DEFAULT_TIMEZONE));
        date.setTimeInMillis(epoch * 1000L);
        int tzOffsetHours = tzOffsetSeconds / 3600;
        int tzOffsetMin = (Math.abs(tzOffsetSeconds) % 3600) / 60;
        String localTimezoneId = String.format(TIMEZONE_OFFSET_FORMAT_STR, tzOffsetHours, tzOffsetMin);
        date.setTimeZone(TimeZone.getTimeZone(localTimezoneId));
        return date;
    }

    /**
     * Returns the correct temperature unit string to display for a given setting of the units
     * preference, e.g. "F" for imperial units.
     *
     * @param units The current value of the pref_units preference ("standard", "metric", or
     *              "imperial").
     * @param ctx A context.
     * @return Returns the correct temperature unit display string for the given value of `units`,
     *   e.g. "F" for imperial.
     */
    public static String getTemperatureDisplayForUnitsPref(String units, Context ctx) {
        if (TextUtils.equals(units, ctx.getString(R.string.pref_units_standard_value))) {
            return ctx.getString(R.string.pref_units_standard_temp_display);
        } else if (TextUtils.equals(units, ctx.getString(R.string.pref_units_metric_value))) {
            return ctx.getString(R.string.pref_units_metric_temp_display);
        } else {
            return ctx.getString(R.string.pref_units_imperial_temp_display);
        }
    }

    /**
     * Returns the correct wind speed unit string to display for a given setting of the units
     * preference, e.g. "mph" for imperial units.
     *
     * @param units The current value of the pref_units preference ("standard", "metric", or
     *              "imperial").
     * @param ctx A context.
     * @return Returns the correct wind speed unit display string for the given value of `units`,
     *   e.g. "mph" for imperial.
     */
    public static String getWindSpeedDisplayForUnitsPref(String units, Context ctx) {
        if (TextUtils.equals(units, ctx.getString(R.string.pref_units_standard_value))) {
            return ctx.getString(R.string.pref_units_standard_wind_display);
        } else if (TextUtils.equals(units, ctx.getString(R.string.pref_units_metric_value))) {
            return ctx.getString(R.string.pref_units_metric_wind_display);
        } else {
            return ctx.getString(R.string.pref_units_imperial_wind_display);
        }
    }
}
