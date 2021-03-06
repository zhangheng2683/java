package com.zh;

import com.zh.concurrent.Exercise3Fibonacci;
import com.zh.concurrent.LiftOff;
import org.junit.Test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by zh on 2016/12/18.
 */
public class TestExecutor {

    @Test
    public void testCachedThreadPool() throws IOException {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0; i < 10; i++) {
            exec.execute(new LiftOff());
        }
        exec.shutdown();
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSingleThreadPool() throws IOException {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        for(int i = 0; i < 10; i++) {
            exec.execute(new LiftOff());
        }
        exec.shutdown();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() throws IOException {
        DecimalFormat format = new DecimalFormat("#");
        Random random = new Random(47);
        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<Double>> arrayList = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            arrayList.add(exec.submit(new Exercise3Fibonacci(random.nextInt(1000))));
        }
        for(Future<Double> future : arrayList) {
            try {
                System.out.println(format.format(future.get())+ "-");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        exec.shutdown();
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4() throws IOException {
        List<String[]> list  = new ArrayList<>();
        list.add(new String[]{"1", "2"});
        list.add(new String[]{"3", "4", "5"});
    }

    @Test
    public void test5() throws IOException {
        Map<String, String> map = new ConcurrentHashMap<>();
    }
}
