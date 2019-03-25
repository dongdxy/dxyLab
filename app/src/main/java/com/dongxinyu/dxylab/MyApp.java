package com.dongxinyu.dxylab;

import android.app.Application;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                                                            .showThreadInfo(true)  //是否选择显示线程信息，默认为true
                                                            .methodCount(2)         //方法数显示多少行，默认2行
                                                            .methodOffset(7)        //隐藏方法内部调用到偏移量，默认5
                                                            .tag("logger")   //自定义TAG全部标签，默认PRETTY_LOGGER
                                                            .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        Log.d("xxx", "appOncreate");
    }
}
