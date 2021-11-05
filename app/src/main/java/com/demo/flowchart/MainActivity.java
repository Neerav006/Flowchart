package com.demo.flowchart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.demo.flowchart.auth.view.LoginFragment;

import com.demo.flowchart.editor.view.EditorFragment;
import com.demo.flowchart.home.view.HomeFragment;
import com.demo.flowchart.navigation.Navigator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements Navigator {

    private BottomNavigationView bottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {
            navigateTo(HomeFragment.newInstance(), false);
        }
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_home:
                    navigateTo(HomeFragment.newInstance(), false);
                    return true;
                case R.id.item_add:
                    navigateTo(EditorFragment.newInstance(), false);
                    return true;
                case R.id.item_profile:
                    navigateTo(LoginFragment.newInstance(), false);
                    return true;
            }
            return false;
        });
    }

    @Override
    public void navigateTo(Fragment destinationFragment, boolean popBackStack) {
        final int MAIN_CONTAINER_ID = R.id.main_container;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (popBackStack) {
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            transaction.addToBackStack(destinationFragment.getClass().getSimpleName());
        }

        transaction.replace(MAIN_CONTAINER_ID, destinationFragment);
        transaction.commit();
    }
}