package com.example.dep1interop;

import org.test.dep1.DeprecatedThing;

public class TestWarn {
    public static void main(String[] args) {
        System.out.println(DeprecatedThing.DEPRECATED.name());
    }
}
