package cn.trico.doorgod.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    public static int DIGITAL_TIME = 0;
    public static int CHINESE_TIME = 1;

    public static String utc2Local(String utcTime, int type) {
        SimpleDateFormat localFormater;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date UtcDate = null;
        try {
            UtcDate = sdf.parse(utcTime);
        } catch (Exception e) {
            return null;
        }
        switch (type) {
            case 0:
                localFormater = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                break;
            case 1:
                localFormater = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
                break;
            default:
                return null;
        }
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(UtcDate.getTime());
        return localTime;
    }
}
