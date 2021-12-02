package com.demo.flowchart.auth;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.demo.flowchart.auth.result.AuthResult;
import com.demo.flowchart.auth.result.AuthError;
import com.demo.flowchart.auth.result.AuthSuccess;
import com.demo.flowchart.database.FlowchartEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseRepository {

    private final FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private final MutableLiveData<AuthResult> resultLiveData;
    private final MutableLiveData<Boolean> loggedOutLiveData;

    private final String GLOBAL_CHILD = "user";

    public FirebaseRepository() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        resultLiveData = new MutableLiveData<>();
        loggedOutLiveData = new MutableLiveData<>();
        database = FirebaseDatabase.getInstance();

        if (auth.getCurrentUser() != null) {
            reference = database.getReference();
            resultLiveData.postValue(new AuthSuccess());
            loggedOutLiveData.postValue(false);

        }
    }

    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && auth.getCurrentUser() != null) {
                resultLiveData.postValue(new AuthSuccess());
                user = auth.getCurrentUser();
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
        user = auth.getCurrentUser();
        loggedOutLiveData.postValue(true);
    }

    public String getUserEmail() {
        if (user != null)
            return user.getEmail();
        return null;
    }

    public void uploadFlowchartToFirebase(FlowchartEntity flowchartEntity) {
        if (user != null) {
            reference.child(GLOBAL_CHILD).child(user.getUid()).child(flowchartEntity.getName()).setValue(flowchartEntity);
        }
    }

    public void removeFlowchartFromFirebase(FlowchartEntity flowchartEntity) {
        if (user != null) {
            reference.child(GLOBAL_CHILD).child(user.getUid()).child(flowchartEntity.getName()).removeValue();
        }
    }

    //TODO изменить
    List<FlowchartEntity> flowcharts = new ArrayList<>();

    public void getFlowchartsFromFirebase() {
        if(user != null)
        reference.child(GLOBAL_CHILD).child(user.getUid()).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                Log.d("firebase", String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
                for (DataSnapshot data : task.getResult().getChildren()) {
                    String uid = Objects.requireNonNull(data.child("uid").getValue()).toString();
                    String name = Objects.requireNonNull(data.child("name").getValue()).toString();
                    String json = Objects.requireNonNull(data.child("json").getValue()).toString();
                    flowcharts.add(new FlowchartEntity(Long.parseLong(uid), name, json));
                }
            }
        });
    }

    public int getCountFlowchartsFromFirebase(){
        return flowcharts.size();
    }

    public MutableLiveData<AuthResult> getResultLiveData() {
        return resultLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
