package cn.chenzhongjin.sample.net.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * @author chenzj
 * @Title: NetworkType
 * @Description: 类的描述 - 检测网络类型
 * @date 2016/4/10 16:10
 * @email admin@chenzhongjin.cn
 */
public class NetworkType {
    public NetworkType() {
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                return false;
            case 3:
                return true;
            case 4:
                return false;
            case 5:
                return true;
            case 6:
                return true;
            case 7:
                return false;
            case 8:
                return true;
            case 9:
                return true;
            case 10:
                return true;
            case 11:
                return false;
            case 12:
                return true;
            case 13:
                return true;
            case 14:
                return true;
            case 15:
                return true;
            default:
                return false;
        }
    }

    public static NetworkType.NetWorkType getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        NetworkType.NetWorkType mNetWorkType = NetworkType.NetWorkType.NETWORKTYPE_INVALID;
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NetworkType.NetWorkType.NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = Proxy.getDefaultHost();
                mNetWorkType = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NetworkType.NetWorkType.NETWORKTYPE_3G
                        : NetworkType.NetWorkType.NETWORKTYPE_2G) : NetworkType.NetWorkType.NETWORKTYPE_WAP;
            }
        } else {
            mNetWorkType = NetworkType.NetWorkType.NETWORKTYPE_INVALID;
        }

        return mNetWorkType;
    }

    public static enum NetWorkType {
        NETWORKTYPE_INVALID("no_network", 1),
        NETWORKTYPE_WAP("wap", 2),
        NETWORKTYPE_2G("2g", 3),
        NETWORKTYPE_3G("3g", 4),
        NETWORKTYPE_WIFI("wifi", 5);

        private String name;
        private int index;

        private NetWorkType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            NetworkType.NetWorkType[] netWorkTypes;
            int length = (netWorkTypes = values()).length;

            for (int i = 0; i < length; ++i) {
                NetworkType.NetWorkType c = netWorkTypes[i];
                if (c.getIndex() == index) {
                    return c.name;
                }
            }

            return null;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return this.index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
