package com.demo.flowchart.auth.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.flowchart.auth.model.Auth;
import com.demo.flowchart.auth.model.AuthImpl;
import com.demo.flowchart.auth.util.AuthResult;

public class LoginViewModel extends ViewModel {
    Auth auth = new AuthImpl();

    public MutableLiveData<AuthResult> result = new MutableLiveData<>();

    public void signIn(String email, String password) {
        if (auth.isEmailEmpty(email)) {
            result.postValue(new AuthResult.EmailError());
            return;
        }
        if (auth.isPasswordEmpty(password)) {
            result.postValue(new AuthResult.PasswordError());
            return;
        }
        if (auth.signIn(email, password)) {
            result.postValue(new AuthResult.AuthSuccess());
        } else {
            result.postValue(new AuthResult.AuthError());
        }
    }

}
