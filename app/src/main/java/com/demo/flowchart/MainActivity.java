package com.demo.flowchart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.demo.flowchart.auth.view.LoginFragment;

import com.demo.flowchart.home.view.HomeFragment;
import com.demo.flowchart.navigation.Navigator;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements Navigator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {
            if (auth.getCurrentUser() != null) {
                navigateTo(HomeFragment.newInstance(), false);
            } else {
                navigateTo(LoginFragment.newInstance(), false);
            }
        }
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