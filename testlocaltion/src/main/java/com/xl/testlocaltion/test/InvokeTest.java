package com.xl.testlocaltion.test;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvokeTest implements InvocationHandler {

    public static final String TAG = InvokeTest.class.getSimpleName();

    private Object object;

    public InvokeTest(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("doThins".equals(method.getName())){
            Log.d(TAG,"befor invoke");
            method.invoke(object,args);
            Log.d(TAG,"after invoke");
            return true;
        }
        return null;
    }

}
