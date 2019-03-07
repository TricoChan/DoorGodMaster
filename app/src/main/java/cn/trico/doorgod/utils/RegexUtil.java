package cn.trico.doorgod.utils;

import java.util.regex.Pattern;

/**
 * 正则工具类
 *
 * @author Trico
 * @since 2018/3/26
 */

public class RegexUtil {
    public static String MOBILE_REGEX = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";

    public static boolean isMobileNum(String mobile) {
        return Pattern.matches(MOBILE_REGEX, mobile);
    }
}
