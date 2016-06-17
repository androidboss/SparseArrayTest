package com.example.dingran.sparsearraytest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.example.dingran.sparsearraytest.logger.Log;
import com.example.dingran.sparsearraytest.logger.LogFragment;
import com.example.dingran.sparsearraytest.logger.LogWrapper;
import com.example.dingran.sparsearraytest.logger.MessageOnlyLogFilter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "SparseArray test";

    // Reference to the fragment showing events, so we can clear it with a button
    // as necessary.
    private LogFragment mLogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize the logging framework.
        initializeLogging();
        // 得在初始化之后才能打印出来，否则截取不到，无法打印出来
        Log.i(TAG, "onCreate()");
        initWork();
    }

    private void initWork() {
        // Android提供的SparseArray是用于替代HashMap的，内部基于二分查找，更快速方便。
        // HashMap的用法
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(0, "000");
        hashMap.put(1, "111");
        hashMap.put(2, "222");
        hashMap.put(3, "333");
        hashMap.put(4, "444");
        for (int i = 0; i < hashMap.size(); i++) {
            Log.i(TAG, "HashMap遍历得到位置"+ i +"的值为:" + hashMap.get(i));
        }

        hashMap.remove(Integer.valueOf(0));
        Log.d(TAG, "删除操作后hashMap大小 size=" + hashMap.size());

        // 在执行删除后hashMap的size减小了
        // 为了遍历完，应该将循环条件改为i < hashMap.size() + 1
        for (int i = 0; i < hashMap.size() + 1; i++) {
            Log.i(TAG, "HashMap遍历得到位置"+ i +"的值为:" + hashMap.get(i));
        }
        // 但是这样做意义不大，因为我们一般都是用keySet来遍历的：
        Set<Integer> keySet = hashMap.keySet();
        for (Iterator<Integer> iter = keySet.iterator(); iter.hasNext();) {
            Integer keyTemp = iter.next();
            String valueTemp = hashMap.get(keyTemp);
            Log.d(TAG, "用keySet遍历得到位置"+ keyTemp +"的值为:" + valueTemp);
        }

        Log.i(TAG, "/////////////////这是华丽的分割线//////////////////////");

        // SparseArray的用法
        SparseArray sparseArray = new SparseArray();

        // 增加数据有两种方法
        sparseArray.append(0, "This is 0");
        sparseArray.append(1, "This is 1");
        sparseArray.append(2, "This is 2");

        sparseArray.put(3, "This is 3");
        sparseArray.put(4, "This is 4");

        for (int i = 0; i < sparseArray.size(); i++) {
            Log.i(TAG, "sparseArray遍历得到位置"+ i +"的值为:" + sparseArray.get(i));
        }

        // 查找某个位置的键
        int key = sparseArray.keyAt(1);
        Log.i(TAG, "查找位置1的键 key=" + key);

        // 查找某个位置的值
        String value = (String) sparseArray.valueAt(1);
        Log.i(TAG, "查找位置1的值 value=" + value);

        // 修改数值有两种方式：
        sparseArray.put(0, "This is new 0");
        sparseArray.put(1, "This is new 1");

        sparseArray.setValueAt(2, "This is new 2");
        sparseArray.setValueAt(3, "This is new 3");

        for (int i = 0; i < sparseArray.size(); i++) {
            Log.i(TAG, "sparseArray遍历得到位置"+ i +"的值为:" + sparseArray.get(i));
        }

        // 删除
        sparseArray.delete(0);
        Log.d(TAG, "删除操作后sparseArray大小 size=" + sparseArray.size());

        for (int i = 0; i < sparseArray.size() + 1; i++) {
            Log.i(TAG, "删除后，sparseArray遍历得到位置"+ i +"的值为:" + sparseArray.get(i));
        }

    }

    /** Create a chain of targets that will receive log data */
    public void initializeLogging() {

        // Using Log, front-end to the logging chain, emulates
        // android.util.log method signatures.

        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        // A filter that strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        mLogFragment =
                (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(mLogFragment.getLogView());
    }
}
