package com.demo.flowchart.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.demo.flowchart.R;
import com.demo.flowchart.auth.AuthRepository;
import com.demo.flowchart.auth.view.LoginFragment;
import com.demo.flowchart.editor.view.EditorFragment;
import com.demo.flowchart.home.HomeFragment;
import com.demo.flowchart.navigation.Navigator;


public class ProfileFragment extends Fragment {

    private Navigator navigator;
    private Button logOut;
    private Button edtior;

    private LiveData<Boolean> isLoggedOutLiveData;

    public ProfileFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (Navigator) context;
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        AuthRepository repo = new AuthRepository();
        isLoggedOutLiveData = repo.getLoggedOutLiveData();

        navigator.setUpNavBar(true);
        logOut = view.findViewById(R.id.btn_profile_logout);
        edtior = view.findViewById(R.id.btn_profile_editor);

        logOut.setOnClickListener(v -> {
            repo.signOut();
        });

        edtior.setOnClickListener(v -> {
            navigator.navigateTo(EditorFragment.newInstance());
        });

        isLoggedOutLiveData.observe(this.getViewLifecycleOwner(), b -> {
            if (b) {
                navigator.navigateTo(LoginFragment.newInstance());
            }
        });
    }
}