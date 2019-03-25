package com.dongxinyu.dxylab;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.everphoto.sdkcv.EverphotoClient;
import cn.everphoto.sdkcv.people.EpPeople;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ConsoleActivity extends AppCompatActivity {
    private TextView mConsoleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);
        mConsoleText = findViewById(R.id.console_text);
        mConsoleText.setText("Hello!");

//        printStaticThread();
//        testFindMin();
//        testep();

        testSynchronized();
    }

    private void printStaticThread() {
        TestCaseStaticThread.testThread();
    }


    private void testFindMin() {
        int[] test = new int[]{5, 6, 7, 1, 2, 3, 4};
        findMin(test);
        test = new int[]{5, 6, 7, 8, 2, 3, 4};
        findMin(test);
        test = new int[]{5, 6, 7, 8, 1, 2, 3, 4};
        findMin(test);
        test = new int[]{5, 6, 7, 8, 9, 0, 1, 2, 3, 4};
        findMin(test);
        test = new int[]{5, 6, 7, 8, 9, 0, 1, 2};
        findMin(test);
        test = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        findMin(test);
        test = new int[]{9, 1, 2, 3, 4, 5, 6, 7, 8};
        findMin(test);
        test = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 0};
        findMin(test);
    }

    private void findMin(int[] a) {
        int start = 0;
        int end = a.length - 1;
        int mid;
        int min = 0;

        while (start <= end) {
            mid = (start + end) / 2;
            if (a[mid] < a[min]) {
                min = mid;
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }

        Log.d("xxx", "min of " + Arrays.toString(a) + " is " + a[min]);
    }

    private void testep() {
        EverphotoClient everphotoClient = EverphotoClient.create(this);
        everphotoClient.peopleApi().getPeoples().observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<EpPeople>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<EpPeople> epPeople) {
                Log.d("xxx", "onNext" + epPeople);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Set set = Collections.synchronizedSet(linkedHashSet);
        set.add(1);
        set.add(3);
        set.add(2);
    }

    private void testSynchronized() {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        DebugUtil.printBegin("hashMap.put");
        for (Integer i = 0; i < 100000; i++) {
            hashMap.put(i, i);
        }
        DebugUtil.printEnd("hashMap.put");

        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        DebugUtil.printBegin("concurrentHashMap.put");
        for (Integer i = 0; i < 100000; i++) {
            concurrentHashMap.put(i, i);
        }
        DebugUtil.printEnd("concurrentHashMap.put");

        Map<Integer, Integer> synchronizedMap = Collections.synchronizedMap(new HashMap<Integer, Integer>());
        DebugUtil.printBegin("synchronizedMap.put");
        for (Integer i = 0; i < 100000; i++) {
            synchronizedMap.put(i, i);
        }
        DebugUtil.printEnd("synchronizedMap.put");
    }
}
