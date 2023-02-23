package com.qiren.geoapp.common.user;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private UserInfo userInfo;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final Map<Object, UserInfoChangeListener> userInfoChangeListeners = new HashMap<>();

    private boolean isLogin() {
        return null != userInfo && !TextUtils.isEmpty(userInfo.getUserId());
    }

    public UserInfo getUserInfo() {
        // we may get chance to get wrong userInfo here because of concurrent problem
        // but currently we ignore it
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mainHandler.post(() -> {
            // we may try to check md5 here, also we may not
            // this listener may only be called on the main thread
            userInfoChangeListeners.forEach((k, v) -> {
                v.onChanged(this.userInfo, userInfo);
            });
            this.userInfo = userInfo;
        });
    }

    public void addUserInfoChangeListener(Object source, UserInfoChangeListener listener) {
        userInfoChangeListeners.put(source, listener);
    }

    public void removeUserInfoChangeListener(Object source) {
        userInfoChangeListeners.remove(source);
    }

    public static UserManager getInstance() {
        return Inner.instance;
    }

    private UserManager() {

    }

    public interface UserInfoChangeListener {
        void onChanged(UserInfo oldInfo, UserInfo newInfo);
    }

    public static class Inner {
        private static final UserManager instance = new UserManager();
    }
}
