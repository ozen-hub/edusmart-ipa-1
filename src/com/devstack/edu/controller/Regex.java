package com.devstack.edu.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//https://www.geeksforgeeks.org/regular-expressions-in-java/
public class Regex {
    public static void main(String[] args) {



        String regex="\\s"; //[0-9 a-z A-Z _]

        Pattern pattern = Pattern.compile(regex);

        String input="Hello World";
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()){
            System.out.println(true);
        }else{
            System.out.println(false);
        }

    }
}
