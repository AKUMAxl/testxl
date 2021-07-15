package com.xl.testui.vehicle.c100;

public class C100VehicleTest {

    private C100VehicleTest() {
    }

    private static class SingletonInstance {
        private static final C100VehicleTest INSTANCE = new C100VehicleTest();
    }

    public static C100VehicleTest getInstance() {
        return SingletonInstance.INSTANCE;
    }
}