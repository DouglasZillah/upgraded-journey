package com.zillasun.date;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTest {

    @Test
    public void test0() {
        System.out.println("date.getTime(): " + new Date().getTime());
        System.out.println("System.currentTimeMillis(): " + System.currentTimeMillis());
        System.out.println("localDateTime.toEpochMilli(): " + LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
}
