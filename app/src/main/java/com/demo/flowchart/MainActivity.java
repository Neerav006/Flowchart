package com.demo.flowchart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements Navigator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(MAIN_CONTAINER_ID, EditorFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void navigateTo(Fragment destinationFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .replace(MAIN_CONTAINER_ID, destinationFragment)
                .addToBackStack(destinationFragment.getClass().getSimpleName())
                .commit();

        // TODO: Добавить логику сохранения стека фрагментов
    }

    private final int MAIN_CONTAINER_ID = R.id.main_container;
}