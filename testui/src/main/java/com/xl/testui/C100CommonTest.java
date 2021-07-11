package com.xl.testui;

public class C100CommonTest {

    private C100CommonTest() {
    }

    private static class SingletonInstance {
        private static final C100CommonTest INSTANCE = new C100CommonTest();
    }

    public static C100CommonTest getInstance() {
        return SingletonInstance.INSTANCE;
    }
}