package com.example.restaurant;
public class SafeLogger {
    public static synchronized void log(String s){
        System.out.println("[" + Thread.currentThread().getName() + "] " + s);
    }
}

