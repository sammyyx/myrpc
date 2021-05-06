package com.sammyyx.service;

/**
 * User: sammy
 * Date: 2021/5/5
 * Time: 12:01
 */
public class HelloWorldImpl implements HelloWorld {

    @Override
    public String helloWorld(String arg) {
        return arg;
    }

    @Override
    public String helloWorld(String arg1, String arg2) {
        return arg1 + arg2;
    }
}
