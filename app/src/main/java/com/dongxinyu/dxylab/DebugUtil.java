package com.dongxinyu.dxylab;

import android.os.SystemClock;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DebugUtil {
    public static void printCallStack(int depth) {
        printCallStack(depth, "");
    }

    public static void printCallStack(int depth, String info) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StringBuffer callers = new StringBuffer();
        for (int i = 3; i < stackTraceElements.length && i < 3 + depth; i++) {
            StackTraceElement stackTraceElement = stackTraceElements[i];
            callers.append(stackTraceElement.getClassName()
                                   + "." + stackTraceElement.getMethodName()
                                   + "->" + stackTraceElement.getLineNumber());
            callers.append('\n');

        }
        Log.d("tmp", info + " on thread:" + Thread.currentThread().getName() + " callers:\n" + callers.toString());
    }

    public static void printThread(String title) {
        Log.d("tmp", "thread:" + Thread.currentThread().getName() + " title:" + title);
    }

    private volatile static ThreadLocal<Long> sTimeStamp = new ThreadLocal<>();

    public static void printStep(String stepName) {
        Long last = sTimeStamp.get();
        if (last == null) {
            last = Long.valueOf(0);
        }
        long current = SystemClock.elapsedRealtime();
        long diff = current - last;
        sTimeStamp.set(current);
        Log.d("DebugStep", stepName + " cost " + diff + " on " + Thread.currentThread().getName());
    }

    private static Map<String, Long> sTasks = new ConcurrentHashMap<>();

    public static void printBegin(String taskName) {
        sTasks.put(taskName, SystemClock.elapsedRealtime());
        Log.d("DebugTime", taskName + " begin ");
    }

    public static void printEnd(String taskName) {
        Long begin = sTasks.remove(taskName);
        if (begin == null) {
            begin = Long.valueOf(0);
        }
        long current = SystemClock.elapsedRealtime();
        long diff = current - begin;
        Log.d("DebugTime", taskName + " cost " + diff + " on " + Thread.currentThread().getName());
    }
}
