package com.demo.flowchart.auth;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.demo.flowchart.auth.result.AuthResult;
import com.demo.flowchart.auth.result.AuthError;
import com.demo.flowchart.auth.result.AuthSuccess;
import com.demo.flowchart.auth.result.LoggedOut;
import com.demo.flowchart.database.FlowchartEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.microedition.khronos.opengles.GL;

public class FirebaseRepository {

    private final FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private final MutableLiveData<AuthResult> resultLiveData;
    private final MutableLiveData<Boolean> loggedOutLiveData;

    private final MutableLiveData<List<FlowchartEntity>> flowchartsLiveData;

    private final String GLOBAL_CHILD = "user";

    public FirebaseRepository() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        resultLiveData = new MutableLiveData<>();
        loggedOutLiveData = new MutableLiveData<>();
        flowchartsLiveData = new MutableLiveData<>();
        database = FirebaseDatabase.getInstance();

        if (auth.getCurrentUser() != null) {
            reference = database.getReference();
            resultLiveData.postValue(new AuthSuccess());
            loggedOutLiveData.postValue(false);
        } else {
            resultLiveData.postValue(new LoggedOut());
            loggedOutLiveData.postValue(true);
        }
    }

    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && auth.getCurrentUser() != null) {
                resultLiveData.postValue(new AuthSuccess());
                loggedOutLiveData.postValue(false);
                reference = database.getReference();
                user = auth.getCurrentUser();
                getFlowchartsFromFirebase();
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
        resultLiveData.postValue(new LoggedOut());
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

    public void getFlowchartsFromFirebase() {
        List<FlowchartEntity> flowcharts = new ArrayList<>();

        if (user != null) {
            reference.child(GIGA_CHAD).child(user.getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            reference.child(GLOBAL_CHILD).child(user.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DataSnapshot data : task.getResult().getChildren()) {
                        String uid = Objects.requireNonNull(data.child("uid").getValue()).toString();
                        String name = Objects.requireNonNull(data.child("name").getValue()).toString();
                        String json = Objects.requireNonNull(data.child("json").getValue()).toString();
                        flowcharts.add(new FlowchartEntity(Long.parseLong(uid), name, json));
                    }
                }
                flowchartsLiveData.postValue(flowcharts);
            });
        }
    }

    public MutableLiveData<AuthResult> getResultLiveData() {
        return resultLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

    public MutableLiveData<List<FlowchartEntity>> getFlowchartsLiveData() {
        return flowchartsLiveData;
    }


}
