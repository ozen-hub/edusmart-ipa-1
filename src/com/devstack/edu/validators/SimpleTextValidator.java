package com.devstack.edu.validators;

public class SimpleTextValidator {
    public static boolean validateName(String name){
        return name.matches("^[a-zA-Z]{3,45}$");
    }
}
