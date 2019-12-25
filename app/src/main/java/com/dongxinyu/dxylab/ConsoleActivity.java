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
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import cn.everphoto.sdkcv.EverphotoClient;
import cn.everphoto.sdkcv.EverphotoClientConfig;
import cn.everphoto.sdkcv.moment.EpMoment;
import io.reactivex.BackpressureStrategy;
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

//        testep();
//        testTreeSet();
//        testZigzag();
//        testMerge();
//        testFlowable();
        testMerge();
    }

    private void testFlowable() {
        List<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<String> list = new ArrayList<>();
            list.add("test" + i + "-1");
            list.add("test" + i + "-2");
            list.add("test" + i + "-3");
            lists.add(list);
        }

        Observable.just(0)
                .throttleLatest(3, TimeUnit.SECONDS, Schedulers.io())
                .toFlowable(BackpressureStrategy.LATEST)
                .map(integer -> lists)
                .concatMapIterable(lists1 -> lists1)
                .doOnNext(strings -> Log.d("xxx", "doonNext:" + strings))
                .subscribeOn(Schedulers.io())
                .subscribe();

    }

    private void testZigzag() {
        int[] test = new int[]{5, 6, 7, 1, 2, 3, 4};
        Log.d("xxx", "orig : " + Arrays.toString(test));
        int pre = test[0];
        int lenth = test.length;
        boolean bigger = true;
        for (int i = 1; i < lenth; i++) {
            if (test[i] - pre < 0 == bigger) {
                swap(test, i - 1, i);
            }
            bigger = !bigger;
            pre = test[i];
        }
        Log.d("xxx", "zig zag : " + Arrays.toString(test));
    }

    private void swap(int[] test, int i, int j) {
        int tmp = test[i];
        test[i] = test[j];
        test[j] = tmp;
    }

    private void testTreeSet() {
        TreeSet<TestData> set = new TreeSet<>();

        TestData tx;

        TestData t1 = new TestData(1, "aaaaa");
        TestData t2 = new TestData(1, "bbbb");
        TestData t3 = new TestData(1, "cccc");

        tx = t3;

        set.add(t1);
        set.add(t2);
        set.add(t3);

        TestData tN = new TestData(1, "dddd");
        set.add(tN);
        tN = new TestData(1, "eeeee");
        set.add(tN);
        tN = new TestData(1, "ffffff");
        set.add(tN);
        tN = new TestData(2, "ggggg");
        set.add(tN);
        tN = new TestData(2, "hhhhhh");
        set.add(tN);
        tN = new TestData(2, "iiiii");
        set.add(tN);
        tN = new TestData(2, "jjjjjjj");
        set.add(tN);

        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 100000; i++) {
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < 100; j++) {
                int number = random.nextInt(62);
                sb.append(str.charAt(number));
            }
            tN = new TestData(1, sb.toString());
            set.add(tN);
            if (i == 500) {
                tx = tN;
            }
        }


        Log.d("xxx", "treeSet.size:" + set.size());
        Log.d("xxx", "contains:" + set.contains(tx));

        Log.d("xxx", "tx.hash:" + tx.hashCode());
        Log.d("xxx", "tx.hash.max-:" + (Integer.MAX_VALUE - tx.hashCode()));

        for (TestData testData : set) {
            if (!set.contains(testData)) {
                Log.d("xxx", "not contains:" + testData);
                Log.d("xxx", "not contains.hash:" + testData.hashCode());
                Log.d("xxx", "not contains.hash.max-:" + (Integer.MAX_VALUE - testData.hashCode()));
            }
        }
    }

    private static class TestData implements Comparable<TestData> {
        String str;
        long value;

        public TestData(long value, String str) {
            this.str = str;
            this.value = value;
        }


        @Override
        public int compareTo(TestData o) {
            if (o == null) {
                return -1;
            }
            long otherTime = o.value;
            long thisTime = this.value;
            if (thisTime > otherTime) {
                return -1;
            } else if (thisTime < otherTime) {
                return 1;
            }

            return this.hashCode() - o.hashCode();

        }

        @Override
        public int hashCode() {
            return this.str.hashCode();
        }
    }

    private void printStaticThread() {
        TestCaseStaticThread.testThread();
    }

    private void testMerge() {
        int[][] input = {
                {1, 3, 5, 7, 9, 10},
                {1, 2, 3, 5, 6, 10},
                {2, 4, 5, 6, 8, 10, 12, 14}
        };

        mergeList(input);
    }

    private void mergeList(int[][] input) {
        int[] pick = input[0];
        int[] p = new int[input.length];

        for (int i = 0; i < pick.length; i++) {
            int c = pick[i];
            boolean noFound = false;
            for (int j = 1; j < input.length; j++) {
                int[] line = input[j];

                while (p[j] < line.length) {
                    if (line[p[j]] < c) {
                        p[j]++;
                    } else if (line[p[j]] == c) {
                        break;
                    } else {
                        noFound = true;
                        break;
                    }
                }
            }

            if (!noFound) {
                Log.d("xxx", "find common " + c);
            }
        }

    }

    private int findNext(int curent, int i, int[][] input, int[] p) {
        int v[] = input[i];
        while (p[i] < v.length) {
            if (v[p[i]] == curent) {
                return curent;
            } else if (v[p[i]] > curent) {
                return v[p[i]];
            }
            p[i]++;
        }
        return -1;
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
        EverphotoClient.init(this, config);
        EverphotoClient everphotoClient = EverphotoClient.getInstance();
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

    private void test1(){
        final int n=3;

    }
}
