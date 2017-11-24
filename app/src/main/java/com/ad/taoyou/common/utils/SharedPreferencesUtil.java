package com.ad.taoyou.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ad.taoyou.MyApplication;
import com.google.gson.Gson;

import org.apache.commons.codec.binary.Base64;

public class SharedPreferencesUtil {

    /*********************************
     * 本地缓存
     ***********************************/
    private final static String SP_NAME = "config";
    private static SharedPreferences sp;

    public static void saveBoolean(String key, boolean value) {
        if (sp == null)
            sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(SP_NAME, 0);
        sp.edit().putBoolean(key, value).commit();
    }

    public static void saveString(String key, String value) {
        if (sp == null)
            sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(SP_NAME, 0);
        sp.edit().putString(key, value).commit();

    }

    public static void clear() {
        if (sp == null)
            sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(SP_NAME, 0);
        sp.edit().clear().commit();
    }

    public static void saveLong(String key, long value) {
        if (sp == null)
            sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(SP_NAME, 0);
        sp.edit().putLong(key, value).commit();
    }

    public static void saveInt(String key, int value) {
        if (sp == null)
            sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(SP_NAME, 0);
        sp.edit().putInt(key, value).commit();
    }

    public static void saveFloat(String key, float value) {
        if (sp == null)
            sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(SP_NAME, 0);
        sp.edit().putFloat(key, value).commit();
    }

    /**
     * 删除缓存
     */
    public static void remove(String key) {
        if (sp == null)
            sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(SP_NAME, 0);
        sp.edit().remove(key).commit();
    }

    public static String getString(String key, String defValue) {
        if (sp == null)
            sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(SP_NAME, 0);
        return sp.getString(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        if (sp == null)
            sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(SP_NAME, 0);
        return sp.getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        if (sp == null)
            sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(SP_NAME, 0);
        return sp.getLong(key, defValue);
    }

    public static float getFloat(String key, float defValue) {
        if (sp == null)
            sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(SP_NAME, 0);
        return sp.getFloat(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        if (sp == null)
            sp = MyApplication.getInstance().getApplicationContext().getSharedPreferences(SP_NAME, 0);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 将对象进行base64编码后保存到SharePref中
     *
     * @param context
     * @param key
     * @param object
     */
    public static void saveObj(Context context, String key, Object object) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        String json = new Gson().toJson(object);
        // 将对象的转为base64码
        String objBase64 = new String(Base64.encodeBase64(json.getBytes()));
        sp.edit()
                .putString(key, objBase64).commit();
    }

    /**
     * 将SharePref中经过base64编码的对象读取出来
     *
     * @param context
     * @param key
     * @return
     */
    public static Object getObj(Context context, String key, Class clazz) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        String objBase64 = sp.getString(key, null);
        if (TextUtils.isEmpty(objBase64))
            return null;

        // 对Base64格式的字符串进行解码
        byte[] base64Bytes = Base64.decodeBase64(objBase64.getBytes());

        String json = new String(base64Bytes);

        return new Gson().fromJson(json, clazz);
    }

}
