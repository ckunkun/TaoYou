package com.ad.taoyou.common.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunweike on 2017/8/29.
 */

public class FragManager {
    public static final String TAG = "FragManager";
    public static final int TAB_HOME = 0, TAB_CIRCLE = 1, TAB_GIFT = 2, TAB_ACTIVITY = 3;
    public static List<String> homeTags, circleTags, giftTags, activityTags;
    private static FragManager instance;
    public static int curItem = 0;

    private FragManager() {

    }

    /**
     * 单例模式
     */
    public static FragManager getFragManager() {
        if (instance == null) {
            synchronized (FragManager.class) {
                if (instance == null) {
                    instance = new FragManager();
                }
            }
        }
        return instance;
    }

    public static void addFragment(String TAG) {
        switch (curItem) {
            case TAB_HOME:
                if (homeTags == null) {
                    homeTags = new ArrayList<>();
                }
                homeTags.add(TAG);
//                Log.e(TAG, "addFragment" + homeTags.toString());
                break;
            case TAB_CIRCLE:
                if (circleTags == null) {
                    circleTags = new ArrayList<>();
                }
                circleTags.add(TAG);
                break;
            case TAB_GIFT:
                if (giftTags == null) {
                    giftTags = new ArrayList<>();
                }
                giftTags.add(TAG);
//                Log.e(TAG, "addFragment" + giftTags.toString());
                break;
            case TAB_ACTIVITY:
                if (activityTags == null) {
                    activityTags = new ArrayList<>();
                }
                activityTags.add(TAG);
//                Log.e(TAG, "addFragment" + activityTags.toString());
                break;
        }

    }

    public static void finishFragment() {
        switch (curItem) {
            case TAB_HOME:
                homeTags.remove(homeTags.size() - 1);
//                Log.e(TAG, "finishFragment" + homeTags.toString());
                break;
            case TAB_CIRCLE:
                circleTags.remove(circleTags.size() - 1);
                break;
            case TAB_GIFT:
                giftTags.remove(giftTags.size() - 1);
//                Log.e(TAG, "finishFragment" + giftTags.toString());
                break;
            case TAB_ACTIVITY:
                activityTags.remove(activityTags.size() - 1);
//                Log.e(TAG, "finishFragment" + activityTags.toString());
                break;
        }

    }

    public static String getCurFragment() {
        switch (curItem) {
            case TAB_HOME:
                if (homeTags == null || homeTags.size() == 0) {
                    return "";
                }
//                Log.e(TAG, "getCurFragment" + homeTags.toString());
                return homeTags.get(homeTags.size() - 1);
            case TAB_CIRCLE:
                if (circleTags == null || circleTags.size() == 0) {
                    return "";
                }
                return circleTags.get(circleTags.size() - 1);
            case TAB_GIFT:
                if (giftTags == null || giftTags.size() == 0) {
                    return "";
                }
//                Log.e(TAG, "getCurFragment" + giftTags.toString());
                return giftTags.get(giftTags.size() - 1);
            case TAB_ACTIVITY:
                if (activityTags == null || activityTags.size() == 0) {
                    return "";
                }
//                Log.e(TAG, "getCurFragment" + activityTags.toString());
                return activityTags.get(activityTags.size() - 1);
        }
        return "";
    }

    public static String getCurSecendFrag() {
        switch (curItem) {
            case TAB_HOME:
                if (homeTags.size() < 2)
                    return "";
//                Log.e(TAG, "getCurSecendFrag" + homeTags.toString());
                return homeTags.get(homeTags.size() - 2);
            case TAB_CIRCLE:
                if (circleTags.size() < 2)
                    return "";
                return circleTags.get(circleTags.size() - 2);
            case TAB_GIFT:
                if (giftTags.size() < 2)
                    return "";
//                Log.e(TAG, "getCurSecendFrag" + giftTags.toString());
                return giftTags.get(giftTags.size() - 2);
            case TAB_ACTIVITY:
                if (activityTags.size() < 2)
                    return "";
//                Log.e(TAG, "getCurSecendFrag" + activityTags.toString());
                return activityTags.get(activityTags.size() - 2);
        }
        return "";
    }
}
