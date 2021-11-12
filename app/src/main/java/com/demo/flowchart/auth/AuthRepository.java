package com.demo.flowchart.auth;

import androidx.lifecycle.MutableLiveData;

import com.demo.flowchart.auth.result.AuthResult;
import com.demo.flowchart.auth.result.AuthError;
import com.demo.flowchart.auth.result.AuthSuccess;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthRepository {

    private final FirebaseAuth auth;
    private final FirebaseUser user;
    private final MutableLiveData<AuthResult> resultLiveData;
    private final MutableLiveData<Boolean> loggedOutLiveData;
    FirebaseDatabase database;
    DatabaseReference reference;

    public AuthRepository() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        resultLiveData = new MutableLiveData<>();
        loggedOutLiveData = new MutableLiveData<>();
        database = FirebaseDatabase.getInstance();

        if (auth.getCurrentUser() != null) {
            //TODO исправить
//            reference = database.getReference();
//            assert user != null;
//            reference.child("users").child(user.getUid()).setValue(user.getEmail());
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

    public void uploadToFirebase(String projectName, String json) {
        if (user != null) {
            reference = database.getReference();
            reference.child("projects").child(user.getUid()).child(projectName).child("data").setValue(json);
        }
    }

    public MutableLiveData<AuthResult> getResultLiveData() {
        return resultLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
