package com.xl.testlocaltion.location;

import android.os.IBinder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LocationServiceHook implements InvocationHandler {

    private IBinder mBase;
    private Class<?> mStub;
    private Class<?> mInterface;
    private InvocationHandler mInvocationHandler;

    public LocationServiceHook(IBinder mBase,String iInterface,boolean isStub,InvocationHandler invocationHandler){
        this.mBase = mBase;
        this.mInvocationHandler = invocationHandler;
        try {
            this.mInterface = Class.forName(iInterface);
            this.mStub = Class.forName(String.format("%s%s",iInterface,isStub?"$Stub":""));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("callLocationChangedLocked".equals(method.getName())){
//            return Proxy.newProxyInstance(proxy.getClass().getClassLoader(),new Class[]{mInterface},new H)
        }
        return null;
    }

}
