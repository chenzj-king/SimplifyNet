package com.dreamliner.simplifyokhttp.cookie.store;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * @author chenzj
 * @Title: CookieStore
 * @Description: 类的描述 -
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public interface CookieStore {

    void add(HttpUrl uri, List<Cookie> cookie);

    List<Cookie> get(HttpUrl uri);

    List<Cookie> getCookies();

    boolean remove(HttpUrl uri, Cookie cookie);

    boolean removeAll();

}
