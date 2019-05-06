package com.zillasun.exception;

import org.junit.Test;


public class ExceptionTest {

    /**
     * 先打印 try 再打印 finally
     */
    @Test
    public void test0() {
        try {
            System.out.println("test0 try");
        } finally {
            System.out.println("test0 finally");
        }
    }

    /**
     * 只打印 finally<br/>
     * 原因是 finally 中返回的值会代替 try 返回的值
     */
    @Test
    public void test1() {
        System.out.println(testFinally());
    }

    private String testFinally() {
        try {
            return "test1 try";
        } finally {
            return "test1 finally";
        }
    }

    /**
     * 先打印 catch 再打印 finally
     */
    @Test
    public void test2() {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            System.out.println("test2 catch");
        } finally {
            System.out.println("test2 finally");
        }
    }

    /**
     * 在 IDE 和 junit 框架里运行以下代码会发现,finally 代码块会先于 RuntimeException 执行
     * <p>但实际上程序的顺序是先执行 catch, 再执行 finally, 然后打印异常信息, 最后结束</p>
     * <p>但为什么是先 finally 再抛异常呢?</p>
     * <p>因为这个 RuntimeException 没有被 catch, 也没有执行任何打印,</p>
     * <p>所以不会立即打印异常, 导致程序看上去是在执行完 finally 后才抛异常</p>
     * <p>由此推导: 程序在出现 exception 异常时并不会马上结束运行, 而是需要处理一些收尾,</p>
     * <p>如内存回收释放, 清理缓存等, 然后打印异常信息, 最后退出</p>
     * <p>所以正确的顺序是：try -> catch -> finally -> junit and others -> exit</p>
     */
    @Test
    public void test3() {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            System.out.println("test3 finally");
        }
    }

    /**
     * 先执行内部 try finally, 再执行外层 finally
     * <p>print:</p>
     * <p>test4 try</p>
     * <p>test4 inner finally</p>
     * <p>test4 outer finally</p>
     */
    @Test
    public void test4() {
        try {
            try {
                System.out.println("test4 try");
            } finally {
                System.out.println("test4 inner finally");
            }
        } finally {
            System.out.println("test4 outer finally");
        }
    }

    /**
     * 先执行 try try finally 再执行 finally try finally
     * <p>print:</p>
     * <p>test5 try try</p>
     * <p>test5 try finally</p>
     * <p>test5 finally try</p>
     * <p>test5 finally finally</p>
     */
    @Test
    public void test5() {
        try {
            try {
                System.out.println("test5 try try");
            } finally {
                System.out.println("test5 try finally");
            }
        } finally {
            try {
                System.out.println("test5 finally try");
            } finally {
                System.out.println("test5 finally finally");
            }
        }
    }
}
