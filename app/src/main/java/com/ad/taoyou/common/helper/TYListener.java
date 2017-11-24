package com.ad.taoyou.common.helper;

import com.ad.taoyou.swk.login.UserInfo;

/**
 * Created by sunweike on 2017/10/26.
 */

public interface TYListener {

    void onSuccess(UserInfo var0);

    void onFaild(String var0);
}
