package com.dongxinyu.dxylab;

public class TestCaseStaticThread {
    public static void testMainThread() {
        TestClassStatic.getsInstance();
    }

    public static void testThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TestClassStatic.getsInstance();
            }
        }).start();
    }
}
