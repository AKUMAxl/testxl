package com.huawei.ivi.hmi.netbuslib;

import static com.huawei.ivi.hmi.netbuslib.DeviceConstant.DEVICE_NAME.HW_HOST;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DeviceConstant {

    //    public static final String P2P_GROUP_OWNER_NAME = "HUAWEI Mate 40 Pro";
//    public static final String P2P_GROUP_OWNER_NAME = "HUAWEI Mate 30E Pro 5G";
//    public static final String P2P_GROUP_OWNER_NAME = "HUAWEI P40 Pro";
//    public static final String P2P_GROUP_OWNER_NAME = "ICSA";
//    public static final String P2P_GROUP_OWNER_NAME = "HW_HOST";
    public static final String P2P_GROUP_OWNER_NAME = "LANTU_QR";
    //    public static final String P2P_GROUP_OWNER_NAME = "LANTU_XL";
    public static final String P2P_GROUP_OWNER_NAME_1 = "岚图FREE";
//        public static final String P2P_GROUP_OWNER_NAME = "岚图FREE";
//    public static final String P2P_GROUP_OWNER_NAME = "Android_55b2";
//    public static final String IVI_NAME = DEVICE_NAME.LANTU;
//    public static final String PIVI_NAME = DEVICE_NAME.HW_HOST;

    //        public static final String P2P_GROUP_MEMBER_NAME = "岚图FREE";
    public static final String P2P_GROUP_MEMBER_NAME1 = "HW_HOST";
    public static final String P2P_GROUP_MEMBER_NAME2 = "HW_LEFT_PAD";
    public static final String P2P_GROUP_MEMBER_NAME3 = "HW_RIGHT_PAD";

//    public static final String IVI_NAME = DEVICE_NAME.HW_HOST;
//    public static final String PIVI_NAME = DEVICE_NAME.LANTU;
//    public static final String REAR_LEFT = DEVICE_NAME.REAR_LEFT;
//    public static final String REAR_RIGHT = DEVICE_NAME.REAR_RIGHT;

    public static final String IVI_NAME = DEVICE_NAME.LANTU;
    public static final String PIVI_NAME = DEVICE_NAME.HW_HOST;
    public static final String REAR_LEFT = DEVICE_NAME.REAR_LEFT;
    public static final String REAR_RIGHT = DEVICE_NAME.REAR_RIGHT;


    public static final String KEY_P2P_OWNER_DEVICE_NAME = "ownerDeviceName";

    public static final String KEY_LOCATION = "location";

    @IntDef({LOCATION.UNKNOWN, LOCATION.HW_HOST, LOCATION.LANTU, LOCATION.REAR_LEFT, LOCATION.REAR_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PARAM_LOCATION_TYPE {
    }

    public static class LOCATION {
        public static final int UNKNOWN = 0;
        public static final int HW_HOST = 1;
        public static final int LANTU = 2;
        public static final int REAR_LEFT = 3;
        public static final int REAR_RIGHT = 4;
    }

    @StringDef({DEVICE_NAME.HW_HOST, DEVICE_NAME.LANTU, DEVICE_NAME.REAR_LEFT, DEVICE_NAME.REAR_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PARAM_DEVICE_NAME {
    }

    public static class DEVICE_NAME {
        public static final String HW_HOST = "hw_host";
        public static final String LANTU = "lantu";
        public static final String REAR_LEFT = "rear_left";
        public static final String REAR_RIGHT = "rear_right";
    }

    public static class RetryType {
        public static final int CREATE_GROUP = 1;
        public static final int DISCOVER_PEERS = 2;
        public static final int CONNECT_GROUP = 3;
        public static final int REQ_GROUP_INFO = 4;
        public static final int REQ_CONNECT_INFO = 5;
    }

    @IntDef({RetryType.CREATE_GROUP, RetryType.DISCOVER_PEERS, RetryType.CONNECT_GROUP, RetryType.REQ_GROUP_INFO, RetryType.REQ_CONNECT_INFO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RETRY_TYPE {
    }

}
