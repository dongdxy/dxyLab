package com.dongxinyu.dxylab;

import com.orhanobut.logger.Logger;

public class TestClassStatic {
    public static TestClassStatic sInstance;

    static {
        sInstance = new TestClassStatic();
    }

    public static TestClassStatic getsInstance() {
        Logger.d("getInstance");
        return sInstance;
    }

    public TestClassStatic() {
        Logger.d("constructor");
    }
}
