package com.example;

import org.test.dep1.Person;

public class TestDeny {
    public static void main(String[] args) {
        System.out.println(new Person("John", 26));
    }
}