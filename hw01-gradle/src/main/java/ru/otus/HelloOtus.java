package ru.otus;

import com.google.common.base.Joiner;

public class HelloOtus {
    @SuppressWarnings("squid:S106")
    public static void main(String[] args) {
        var helloResult = Joiner.on(", ").join("Hello", "World!");
        System.out.println(helloResult);
    }
}
