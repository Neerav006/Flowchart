package com.demo.flowchart.auth.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.demo.flowchart.auth.model.Auth;
import com.demo.flowchart.auth.model.AuthImpl;
import com.demo.flowchart.auth.util.AuthResult;

public class RegistrationViewModel {
    Auth auth = new AuthImpl();

    public MutableLiveData<AuthResult> result = new MutableLiveData<>();

    public void signUp(String email, String firstPassword, String secondPassword) {
        if (auth.isEmailEmpty(email)) {
            result.postValue(new AuthResult.EmailError());
            return;
        } else if (!auth.isValidEmail(email)) {
            result.postValue(new AuthResult.EmailNotValid());
            return;
        }
        if (auth.isPasswordEmpty(firstPassword)) {
            result.postValue(new AuthResult.PasswordError());
            return;
        } else if (!auth.isValidPassword(firstPassword)) {
            result.postValue(new AuthResult.PasswordIsShorterThanSixCharacters());
            return;
        }
        if (auth.isPasswordEmpty(secondPassword)) {
            result.postValue(new AuthResult.PasswordError());
            return;
        } else if (!auth.isValidPassword(secondPassword)) {
            result.postValue(new AuthResult.PasswordIsShorterThanSixCharacters());
            return;
        }
        if (!auth.arePasswordsEqual(firstPassword, secondPassword)) {
            result.postValue(new AuthResult.PasswordsAreNotEqual());
            return;
        }
        if (auth.signUp(email, firstPassword)) {
            result.postValue(new AuthResult.AuthSuccess());
        } else {
            result.postValue(new AuthResult.AuthError());
        }
    }
}

