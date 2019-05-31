package com.zillasun.number;

import org.junit.Test;

public class IntegerTest {

    @Test
    public void test0() {
        System.out.println("test0");
        System.out.println("Integer.MAX_VALUE: ");
        System.out.println(Integer.MAX_VALUE);
    }

    @Test
    public void test1() {
        System.out.println("test1");
        System.out.println("2147483647 == Integer.MAX_VALUE: ");
        System.out.println(2147483647 == Integer.MAX_VALUE);
    }

    @Test
    public void test2() {
        System.out.println("test2");
        System.out.println("Integer.MAX_VALUE == new Integer(2147483647): ");
        System.out.println(Integer.MAX_VALUE == new Integer(2147483647));
    }

    @Test
    public void test3() {
        System.out.println("test3");
        System.out.println("new Integer(2147483647) == new Integer(2147483647): ");
        System.out.println((new Integer(2147483647) == new Integer(2147483647)));
    }

    @Test
    public void test4() {
        System.out.println("test4");
        System.out.println("2147483647 == new Integer(2147483647): ");
        System.out.println((2147483647 == new Integer(2147483647)));
    }
}
