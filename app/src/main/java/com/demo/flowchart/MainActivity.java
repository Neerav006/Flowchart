package com.demo.flowchart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.demo.flowchart.auth.FirebaseRepository;
import com.demo.flowchart.screens.LoginFragment;

import com.demo.flowchart.screens.RegistrationFragment;
import com.demo.flowchart.database.FlowchartDao;
import com.demo.flowchart.database.FlowchartEntity;
import com.demo.flowchart.screens.EditorFragment;
import com.demo.flowchart.screens.HomeFragment;
import com.demo.flowchart.util.JsonService;
import com.demo.flowchart.model.Workspace;
import com.demo.flowchart.navigation.Navigator;
import com.demo.flowchart.screens.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements Navigator {

    private CoordinatorLayout bottomNavBarContainer;
    private NavigationBarView bottomNavigationView;
    private FloatingActionButton fabCreateProject;
    private AlertDialog.Builder alert;
    private FirebaseRepository authRepo;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fabCreateProject = findViewById(R.id.fab_create);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setBackground(null);

        authRepo = App.getInstance().getFirebase();
        authRepo.getFlowchartsFromFirebase();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {
            navigateTo(HomeFragment.newInstance());
        }

        fabCreateProject.setOnClickListener(v -> showCreateProjectDialog());

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
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
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

    private void showCreateProjectDialog() {
        alert = new AlertDialog.Builder(this);
        EditText etProjectName = new EditText(this);
        alert.setTitle("Project creation");
        alert.setView(etProjectName);

        alert.setPositiveButton("Create", (dialog, whichButton) -> {
            String name = etProjectName.getText().toString();
            String json = new JsonService().flowchartToJson(new Workspace());
            FlowchartDao flowchartDao = App.getInstance().getDatabase().flowchartDao();
            FlowchartEntity flowchartEntity = new FlowchartEntity(name, json);
            long flowchartId = flowchartDao.insert(flowchartEntity);
            flowchartEntity.setUid(flowchartId);
            authRepo.uploadFlowchartToFirebase(flowchartEntity);
            navigateTo(EditorFragment.newInstance(flowchartId));
            alert = null;
        });

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> alert = null);

        alert.show();
    }
}