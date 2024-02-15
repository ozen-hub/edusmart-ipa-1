package com.devstack.edu.validators;

public class SimpleTextValidator {
    public static boolean validateName(String name){
        return name.matches("^[a-zA-Z]{3,45}$");
    }
    public static boolean validateDob(String dob){
        System.out.println(dob);
        return dob.matches("\\d{4}-\\d{2}-\\d{2}");// 2024-02-20
    }
    public static boolean validateEmail(String email){
        return email.matches("\\d{1,2}/\\d{1,2}/\\d{4}");//2/13/2024
    }
    public static boolean validateAddress(String address){
        System.out.println(address);
        return address.matches("\\w+\\s+\\w+");// kalutara Panadura
    }
}
