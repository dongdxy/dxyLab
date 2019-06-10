package com.dongxinyu.dxylab;

import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import cn.everphoto.sdkcv.EverphotoClient;
import cn.everphoto.sdkcv.EverphotoClientConfig;
import cn.everphoto.sdkcv.moment.EpMoment;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ConsoleActivity extends AppCompatActivity {
    public static final int TESTTIMES = 1000000;
    private TextView mConsoleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);
        mConsoleText = findViewById(R.id.console_text);
        mConsoleText.setText("Hello!");

//        printStaticThread();
//        testFindMin();


//        testSynchronized();
//        EverphotoClientConfig config = new EverphotoClientConfig.Builder().build();
//        EverphotoClient everphotoClient = EverphotoClient.create(this, config);

//        String[] permissions = everphotoClient.needPermission();
//        final RxPermissions rxPermissions = new RxPermissions(this);
//        rxPermissions.requestEachCombined(permissions)
//                     .subscribe(permission -> { // will emit 1 Permission object
//                         if (permission.granted) {
//                             testep();
//                         } else if (permission.shouldShowRequestPermissionRationale) {
//                             Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
//                         } else {
//                             // At least one denied permission with ask never again
//                             // Need to go to the settings
//                             Toast.makeText(this, "denied permission", Toast.LENGTH_SHORT).show();
//                         }
//                     });

        testep();
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
        EverphotoClientConfig config = new EverphotoClientConfig.Builder().build();
        EverphotoClient everphotoClient = EverphotoClient.create(this, config);
        everphotoClient.startRecognition();
        everphotoClient.momentApi().refreshMoments();
        everphotoClient.momentApi().getMoments().subscribe(new Observer<List<EpMoment>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<EpMoment> epMoments) {
                Log.d("xxx", "getMoments" + epMoments);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
//        everphotoClient.peopleApi().getPeoples().observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<EpPeople>>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(List<EpPeople> epPeople) {
//                Log.d("xxx", "onNext" + epPeople);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
        Observable.just(0)
                  .delay(10, TimeUnit.SECONDS)
                  .doOnSubscribe(disposable -> everphotoClient.assetStoreApi().preLoad())
                  .subscribeOn(Schedulers.io())
                  .subscribe(entry -> Log.d("xxx", "assetEntry get: " +
                          everphotoClient.assetStoreApi().getAssetEntry("/storage/emulated/0/DCIM/Camera/IMG_20190203_152228.jpg")));

    }

    private void testSynchronized() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        DebugUtil.printBegin("arrayList.put");
        for (int i = 0; i < TESTTIMES; i++) {
            arrayList.add(i);
        }
        DebugUtil.printEnd("arrayList.put");

        DebugUtil.printBegin("arrayList.get");
        for (int i = 0; i < TESTTIMES; i++) {
            arrayList.get(i);
        }
        DebugUtil.printEnd("arrayList.get");

        SparseArray<Integer> sparseArray = new SparseArray<>();
        DebugUtil.printBegin("sparseArray.put");
        for (int i = 0; i < TESTTIMES; i++) {
            sparseArray.put(i, i);
        }
        DebugUtil.printEnd("sparseArray.put");

        DebugUtil.printBegin("sparseArray.get");
        for (int i = 0; i < TESTTIMES; i++) {
            int integer = sparseArray.get(i);
        }
        DebugUtil.printEnd("sparseArray.get");

        ArrayMap<Integer, Integer> arrayMap = new ArrayMap<>();
        DebugUtil.printBegin("arrayMap.put");
        for (int i = 0; i < TESTTIMES; i++) {
            arrayMap.put(i, i);
        }
        DebugUtil.printEnd("arrayMap.put");

        DebugUtil.printBegin("arrayMap.get");
        for (int i = 0; i < TESTTIMES; i++) {
            int integer = arrayMap.get(i);
        }
        DebugUtil.printEnd("arrayMap.get");

        HashMap<Integer, Integer> hashMap = new HashMap<>();
        DebugUtil.printBegin("hashMap.put");
        for (int i = 0; i < TESTTIMES; i++) {
            hashMap.put(i, i);
        }
        DebugUtil.printEnd("hashMap.put");

        DebugUtil.printBegin("hashMap.get");
        for (int i = 0; i < TESTTIMES; i++) {
            int integer = hashMap.get(i);
        }
        DebugUtil.printEnd("hashMap.get");

        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        DebugUtil.printBegin("concurrentHashMap.put");
        for (int i = 0; i < TESTTIMES; i++) {
            concurrentHashMap.put(i, i);
        }
        DebugUtil.printEnd("concurrentHashMap.put");

        DebugUtil.printBegin("concurrentHashMap.get");
        for (int i = 0; i < TESTTIMES; i++) {
            int integer = concurrentHashMap.get(i);
        }
        DebugUtil.printEnd("concurrentHashMap.get");

        Map<Integer, Integer> synchronizedMap = Collections.synchronizedMap(new HashMap<Integer, Integer>());
        DebugUtil.printBegin("synchronizedMap.put");
        for (int i = 0; i < TESTTIMES; i++) {
            synchronizedMap.put(i, i);
        }
        DebugUtil.printEnd("synchronizedMap.put");

        DebugUtil.printBegin("synchronizedMap.get");
        for (int i = 0; i < TESTTIMES; i++) {
            int integer = synchronizedMap.get(i);
        }
        DebugUtil.printEnd("synchronizedMap.get");
    }
}
