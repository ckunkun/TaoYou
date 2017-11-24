package com.ad.taoyou.common.helper;

import com.ad.taoyou.swk.login.UserInfo;
import com.snowfish.cn.ganga.helper.SFOnlineUser;

/**
 * Created by 华硕 on 2017/7/11.
 */

public interface SFLoginListener {

    void onLogout(Object o);

    void onLoginSuccess(SFOnlineUser sfOnlineUser, Object o, UserInfo userInfo);

    void onLoginFailed(String s, Object o);
}
