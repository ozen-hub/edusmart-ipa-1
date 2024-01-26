package com.devstack.edu.model;

public class User {
    private String firstName;
    private String lastname;
    private String rootEmail;
    private String password;

    public User() {
    }

    public User(String firstName, String lastname, String rootEmail, String password) {
        this.firstName = firstName;
        this.lastname = lastname;
        this.rootEmail = rootEmail;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRootEmail() {
        return rootEmail;
    }

    public void setRootEmail(String rootEmail) {
        this.rootEmail = rootEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastname='" + lastname + '\'' +
                ", rootEmail='" + rootEmail + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
