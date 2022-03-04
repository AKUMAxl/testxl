package com.huawei.ivi.hmi.test_opengl;

public class GLNative {

    static {
        System.loadLibrary("test_gl");
    }

    public static native void test();
}
