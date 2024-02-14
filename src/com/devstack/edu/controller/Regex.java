package com.devstack.edu.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//https://www.geeksforgeeks.org/regular-expressions-in-java/
public class Regex {
    public static void main(String[] args) {
        if (Pattern.matches("[abch]","b")){
            System.out.println("YES");
        }else{
            System.out.println("NO");
        }
    }
}
