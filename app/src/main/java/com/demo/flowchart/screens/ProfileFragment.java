package com.demo.flowchart.screens;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.flowchart.R;
import com.demo.flowchart.auth.AuthRepository;
import com.demo.flowchart.navigation.Navigator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ProfileFragment extends Fragment {

    private Navigator navigator;

    private FloatingActionButton fabLogout;
    private TextView email;
    private TextView uploadsCount;

    private LiveData<Boolean> isLoggedOutLiveData;
    private AuthRepository repo;

    public ProfileFragment() {
    }

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
        fabLogout = view.findViewById(R.id.fab_logout);
        email = view.findViewById(R.id.profile_email);
        uploadsCount = view.findViewById(R.id.profile_projects_upload_count);
        repo = new AuthRepository();
        isLoggedOutLiveData = repo.getLoggedOutLiveData();

        navigator.setUpNavBar(true);

        email.setText(repo.getUserEmail());

        fabLogout.setOnClickListener(v-> repo.signOut());
        isLoggedOutLiveData.observe(this.getViewLifecycleOwner(), b -> {
            if (b) {
                navigator.navigateTo(LoginFragment.newInstance());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        repo = null;
        fabLogout = null;
        email = null;
        uploadsCount = null;
    }
}