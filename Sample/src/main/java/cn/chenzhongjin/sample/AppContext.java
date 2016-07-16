package cn.chenzhongjin.sample;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * @author chenzj
 * @Title: AppContext
 * @Description: 类的描述 -
 * @date 2016/4/10 16:05
 * @email admin@chenzhongjin.cn
 */
public class AppContext extends Application {

    private static AppContext mInstance;

    public static AppContext getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        mInstance = this;

        Logger.init("Dreamliner").methodCount(1).methodOffset(0).logLevel(LogLevel.FULL).hideThreadInfo();
    }
}
