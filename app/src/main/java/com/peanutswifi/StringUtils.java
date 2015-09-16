package com.peanutswifi;

/**
 * Created by Jac on 2015/4/8.
 */
import android.text.TextUtils;

class StringUtils {
    public static String convertToQuotedString(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }

        final int lastPos = string.length() - 1;
        if (lastPos < 0 || (string.charAt(0) == '"' && string.charAt(lastPos) == '"')) {
            return string;
        }

        return "\"" + string + "\"";
    }
}