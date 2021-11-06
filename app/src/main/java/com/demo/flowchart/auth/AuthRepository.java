package com.demo.flowchart.auth;

import androidx.lifecycle.MutableLiveData;

import com.demo.flowchart.auth.result.AuthResult;
import com.demo.flowchart.auth.result.AuthError;
import com.demo.flowchart.auth.result.AuthSuccess;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {

    private final FirebaseAuth auth;
    private final FirebaseUser user;
    private final MutableLiveData<AuthResult> resultLiveData;
    private final MutableLiveData<Boolean> loggedOutLiveData;

    public AuthRepository() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        resultLiveData = new MutableLiveData<>();
        loggedOutLiveData = new MutableLiveData<>();

        if (auth.getCurrentUser() != null) {
            resultLiveData.postValue(new AuthSuccess());
            loggedOutLiveData.postValue(false);
        }
    }

    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && auth.getCurrentUser() != null) {
                resultLiveData.postValue(new AuthSuccess());
            } else {
                resultLiveData.postValue(new AuthError("Вход не выполнен"));
            }
        });
    }

    public void register(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && auth.getCurrentUser() != null) {
                resultLiveData.postValue(new AuthSuccess());
            } else {
                resultLiveData.postValue(new AuthError("Регистрация не выполнена"));
            }
        });
    }

    public void signOut() {
        auth.signOut();
        loggedOutLiveData.postValue(true);
    }

    public String getUserEmail() {
        assert user != null;
        return user.getEmail();
    }

    public MutableLiveData<AuthResult> getResultLiveData() {
        return resultLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
