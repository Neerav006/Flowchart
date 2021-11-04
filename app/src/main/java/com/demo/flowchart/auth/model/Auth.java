package com.demo.flowchart.auth.model;

public interface Auth {

    boolean isEmailEmpty(String email);

    boolean isPasswordEmpty(String password);

    boolean isValidEmail(String email);

    boolean isValidPassword(String password);

    boolean arePasswordsEqual(String firstPassword, String secondPassword);

    boolean signIn(String email, String password);

    boolean signUp(String email, String firstPassword);

}
