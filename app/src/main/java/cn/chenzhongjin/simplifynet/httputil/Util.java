/*
 *
 * Copyright (c) 2015 [admin@chenzhongjin | chenzhongjin@vip.qq.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.chenzhongjin.simplifynet.httputil;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author chenzj
 * @Title: Util
 * @Description: 把map转换为url的工具类
 * @date 2015/11/7
 * @email admin@chenzhongjin.cn
 */
public class Util {
    public Util() {
    }

    public static JSONObject HttpParameters2Json(Map<String, Object> map) {
        return new JSONObject(map);
    }

    public static String ConvertMap2HttpParams(Map<String, String> map) {
        StringBuffer buffer = new StringBuffer();
        Iterator var3 = map.entrySet().iterator();

        while (var3.hasNext()) {
            Map.Entry entry = (Map.Entry) var3.next();
            buffer.append((String) entry.getKey()).append("=").append((String) entry.getValue()).append("&");
        }

        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    public static Map<String, String> encoderName(Map<String, String> map) {
        Iterator var2 = map.entrySet().iterator();

        while (var2.hasNext()) {
            Map.Entry entry = (Map.Entry) var2.next();
            String key = (String) entry.getKey();
            if (key.equals("q") || key.equals("tag_name")) {
                String value = null;

                try {
                    value = URLEncoder.encode((String) entry.getValue(), "UTF-8");
                } catch (UnsupportedEncodingException var6) {
                    var6.printStackTrace();
                }

                map.put(key, value);
                break;
            }
        }

        return map;
    }

    public static String readInputStream(InputStream inStream) throws Exception {
        BufferedReader bufferedReader = null;
        bufferedReader = new BufferedReader(new InputStreamReader(inStream));
        StringBuffer buffer = new StringBuffer();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            buffer.append(line.trim());
        }

        return buffer.toString();
    }

    public static boolean isEmpty(String str) {
        return !TextUtils.isEmpty(str) && !"null".equals(str);
    }

    public static Map<String, String> cType(Map<String, Object> map) {
        HashMap param = new HashMap();
        Iterator var3 = map.entrySet().iterator();

        while (var3.hasNext()) {
            Map.Entry entry = (Map.Entry) var3.next();
            param.put((String) entry.getKey(), String.valueOf(entry.getValue()));
        }

        return param;
    }
}

