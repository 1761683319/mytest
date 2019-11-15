package com.itheima.jobs.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wangxin
 * @version 1.0
 */
public class JobTest {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("applicationContext-jobs.xml");
    }
}
