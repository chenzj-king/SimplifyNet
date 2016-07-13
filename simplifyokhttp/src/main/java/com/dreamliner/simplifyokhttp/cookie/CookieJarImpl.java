package com.dreamliner.simplifyokhttp.cookie;

import com.dreamliner.simplifyokhttp.cookie.store.CookieStore;
import com.dreamliner.simplifyokhttp.cookie.store.HasCookieStore;
import com.dreamliner.simplifyokhttp.utils.Exceptions;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @author chenzj
 * @Title: CookieJarImpl
 * @Description: 类的描述 -
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class CookieJarImpl implements CookieJar, HasCookieStore {
    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        if (cookieStore == null) Exceptions.illegalArgument("cookieStore can not be null.");
        this.cookieStore = cookieStore;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.add(url, cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.get(url);
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }
}
