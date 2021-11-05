package com.demo.flowchart.auth;


import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthValidation {

    public static boolean isEmailEmpty(String email) {
        return email.isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (email != null) {
            Pattern pattern = Pattern.compile(
                    "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE
            );
            Matcher matcher = pattern.matcher(email);
            return matcher.find();
        }
        return false;
    }

    public static boolean isPasswordEmpty(String password) {
        return password.isEmpty();
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    public static boolean arePasswordsEqual(String firstPassword, String secondPassword) {
        return firstPassword.equals(secondPassword);
    }
}
