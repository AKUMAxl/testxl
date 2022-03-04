//
// Created by XL on 2022/3/4.
//

#include "test_gl.h"
#include <jni.h>
#include <android/log.h>
#include <string>
#include <iostream>
using namespace std;

const char * LOG_TGA = "NATIVE_TEST_GL";


extern "C"
JNIEXPORT void JNICALL
Java_com_huawei_ivi_hmi_test_1opengl_GLNative_test(JNIEnv *env, jclass thiz) {
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TGA, "test gl");
}