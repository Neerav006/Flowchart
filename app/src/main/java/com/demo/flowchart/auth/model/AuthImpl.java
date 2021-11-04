package com.demo.flowchart.auth.model;


import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthImpl implements Auth {

    @Override
    public boolean isEmailEmpty(String email) {
        return email.trim().isEmpty();
    }

    @Override
    public boolean isPasswordEmpty(String password) {
        return password.trim().isEmpty();
    }

    @Override
    public boolean arePasswordsEqual(String firstPassword, String secondPassword) {
        return firstPassword.trim().equals(secondPassword.trim());
    }

    @Override
    public boolean signIn(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.signInWithEmailAndPassword(email.trim(), password.trim()).isSuccessful();
    }

    @Override
    public boolean signUp(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.createUserWithEmailAndPassword(email.trim(), password.trim()).isSuccessful();
    }

    @Override
    public boolean isValidEmail(String email) {
        if (email != null) {
            Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher m = p.matcher(email);
            return m.find();
        }
        return false;
    }

    @Override
    public boolean isValidPassword(String password) {
        return password.trim().length() >= 6;
    }
}
