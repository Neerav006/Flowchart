package com.demo.flowchart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.WindowManager;

import com.demo.flowchart.auth.view.LoginFragment;

import com.demo.flowchart.auth.view.RegistrationFragment;
import com.demo.flowchart.editor.view.EditorFragment;
import com.demo.flowchart.home.HomeFragment;
import com.demo.flowchart.navigation.Navigator;
import com.demo.flowchart.profile.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements Navigator, View.OnDragListener {

    private CoordinatorLayout bottomNavBarContainer;
    private NavigationBarView bottomNavigationView;
    private FloatingActionButton fabCreateProject;

    @Override
    public boolean onDrag(View v, DragEvent event) {

        Log.d("WorkspaceView", "DRAG");
        return false;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fabCreateProject = findViewById(R.id.fab_create);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setBackground(null);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {
            navigateTo(HomeFragment.newInstance());
        }

        fabCreateProject.setOnClickListener(v -> navigateTo(EditorFragment.newInstance()));

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
        bottomNavBarContainer = findViewById(R.id.bottom_navigation);
        if (visibility) {
            bottomNavBarContainer.setVisibility(View.VISIBLE);
        } else {
            bottomNavBarContainer.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bottomNavigationView = null;
        fabCreateProject = null;
        bottomNavBarContainer = null;
    }
}