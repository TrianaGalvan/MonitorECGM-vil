package com.example.trianaandaluciaprietogalvan.helloworldsupport.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by trianaandaluciaprietogalvan on 25/04/16.
 */
public class HourUtils {

    public static String getDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
    }

    public static String getDateHour(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(c.getTime());
    }

    public static String getDateHourSinPuntos(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmm");
        return sdf.format(c.getTime());
    }
}
