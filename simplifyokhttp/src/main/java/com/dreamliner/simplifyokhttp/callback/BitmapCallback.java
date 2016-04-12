package com.dreamliner.simplifyokhttp.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author chenzj
 * @Title: BitmapCallback
 * @Description: 类的描述 - 图片文件回调
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public abstract class BitmapCallback extends HttpCallBack<Bitmap> {

    @Override
    public Bitmap parseNetworkResponse(BaseResponse baseResponse) throws Exception {
        return BitmapFactory.decodeStream(baseResponse.getResponse().body().byteStream());
    }

}
