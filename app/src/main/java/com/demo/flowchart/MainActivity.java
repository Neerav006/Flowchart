package com.demo.flowchart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.demo.flowchart.auth.view.LoginFragment;

import com.demo.flowchart.auth.view.RegistrationFragment;
import com.demo.flowchart.editor.view.EditorFragment;
import com.demo.flowchart.home.HomeFragment;
import com.demo.flowchart.navigation.Navigator;
import com.demo.flowchart.profile.ProfileFragment;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements Navigator {

    private BottomAppBar bottomAppBar;
    private NavigationBarView bottomNavigationView;
    private FloatingActionButton fabCreateProject;
    private int lastSelectedItemId = 0;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {
            navigateTo(HomeFragment.newInstance());
        }

        bottomAppBar = findViewById(R.id.bottom_app_bar);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_home:
                    navigateTo(HomeFragment.newInstance());
                    return true;
                case R.id.item_profile:
                    if (auth.getCurrentUser() != null) {
                        navigateTo(ProfileFragment.newInstance());
                    } else {
                        navigateTo(LoginFragment.newInstance());
                    }
                    return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    @Override
    public void navigateTo(Fragment destinationFragment) {
        final int MAIN_CONTAINER_ID = R.id.main_container;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (destinationFragment instanceof RegistrationFragment
                || destinationFragment instanceof EditorFragment) {
            transaction.addToBackStack(destinationFragment.getClass().getSimpleName());
        } else {
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();
            }
        }

        transaction.replace(MAIN_CONTAINER_ID, destinationFragment);
        transaction.commit();
    }

    @Override
    public void setUpNavBar(boolean visibility) {
        CoordinatorLayout cl = findViewById(R.id.bottom_navigation);
        if (visibility) {
//            bottomAppBar.setVisibility(View.VISIBLE);
//            bottomNavigationView.setVisibility(View.VISIBLE);
//            fabCreateProject.setVisibility(View.VISIBLE);
            cl.setVisibility(View.VISIBLE);
        } else {
//            bottomAppBar.setVisibility(View.GONE);
//            bottomNavigationView.setVisibility(View.GONE);
//            fabCreateProject.setVisibility(View.GONE);
            cl.setVisibility(View.GONE);
        }
    }
}