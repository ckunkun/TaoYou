package com.ad.taoyou.common.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by sunweike on 2017/9/1.
 */

public class SignUtils {

    public static String parseAscii(Map<String, String> allParam) {
        final StringBuffer content = new StringBuffer();
        final List<String> keys = new ArrayList<>(allParam.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            final String key = keys.get(i);
            final String value = allParam.get(key);
            if ("sign".equals(key)) {
                continue;
            }
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }

    public static String getSign(final Map<String, String> allParam, final String secretKey) {
        final String signConent = parseAscii(allParam);
//        Log.i("parseAscii", signConent);
        return Md5Utils.md5(signConent + "&key=" + secretKey).toUpperCase();
    }
}
