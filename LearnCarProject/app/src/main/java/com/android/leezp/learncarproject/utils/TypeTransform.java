package com.android.leezp.learncarproject.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeTransform {

    public static boolean canStrToInt(String str) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

}
