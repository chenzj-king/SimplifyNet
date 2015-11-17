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

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * @author chenzj
 * @Title: BaseBuilder
 * @Description: 构建request工具类.包括根据map拼装url&添加header等操作
 * @date 2015/11/7
 * @email admin@chenzhongjin.cn
 */
public class BaseBuilder {
    public BaseBuilder() {
    }

    public static Request.Builder addCommonCookie(Request.Builder builder) {
        //add apistore apikey
        builder.header("apikey", "15f0d14ed33720b6b73ec8a3f7bb4d46");
        return builder;
    }

    public static Request.Builder urlGet(String url) throws DreamLinerException {
        return urlGet(url, (Map) null);
    }

    public static Request.Builder urlGet(String url, Map<String, String> specificParams) throws DreamLinerException {
        if (specificParams != null) {
            url = url + "?" + Util.ConvertMap2HttpParams(Util.encoderName(specificParams));
        }

        return addCommonCookie((new Request.Builder()).url(url));
    }

    public static Request.Builder urlGet(String url, Map<String, String> specificParams, String appsecret) throws DreamLinerException {
        //if you need sign.you can do in here
       /*
        String sig = null;
        sig = SignatureUtil.generateSignature(appsecret, specificParams);
        if (TextUtils.isEmpty(sig)) {
            throw new CrazyboaException(601, "exception occurs when caculate signature");
        } else {
            specificParams.put("sig", sig);
            return urlGet(url, specificParams);
        }*/

        return urlGet(url, specificParams);
    }

    public static Request.Builder urlPost(String url, Map<String, String> specificParams, String appsecret) throws
            DreamLinerException {
        //if you need sign.you can do in here
       /*
        String sig = null;
        sig = SignatureUtil.generateSignature(appsecret, specificParams);
        if (TextUtils.isEmpty(sig)) {
            throw new CrazyboaException(601, "exception occurs when caculate signature");
        } else {
            specificParams.put("sig", sig);
            return urlPost(url, specificParams);
        }*/

        return urlPost(url, specificParams);
    }

    public static Request.Builder urlPost(String url, Map<String, String> specificParams) throws DreamLinerException {
        FormEncodingBuilder formEncodingBuilder = null;
        if (specificParams != null && specificParams.size() > 0) {
            formEncodingBuilder = new FormEncodingBuilder();
            Iterator entryIterator = specificParams.entrySet().iterator();

            while (entryIterator.hasNext()) {
                Map.Entry entry = (Map.Entry) entryIterator.next();
                formEncodingBuilder.add((String) entry.getKey(), (String) entry.getValue());
            }

            return addCommonCookie((new Request.Builder()).url(url).post(formEncodingBuilder.build()));
        } else {
            throw new DreamLinerException(602, "Form encoded body must have at least one part");
        }
    }

    public static Request.Builder urlPost(String url, File file) throws DreamLinerException {
        return addCommonCookie((new Request.Builder()).url(url).post(RequestBody.create((MediaType) null, file)));
    }

    public static Request.Builder urlPost(String url, byte[] bytes) throws DreamLinerException {
        return addCommonCookie((new Request.Builder()).url(url).post(RequestBody.create((MediaType) null, bytes)));
    }

    public static Request.Builder urlPost(String url, String str) throws DreamLinerException {
        return addCommonCookie((new Request.Builder()).url(url).post(RequestBody.create((MediaType) null, str)));
    }
}

