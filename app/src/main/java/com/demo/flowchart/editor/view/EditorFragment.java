package com.demo.flowchart.editor.view;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.flowchart.R;
import com.demo.flowchart.navigation.Navigator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class EditorFragment extends Fragment implements View.OnDragListener {

    private Navigator navigator;
    private RecyclerView blocksRecycler;
    private BlockAdapter blockAdapter;
    private LinearLayoutManager layoutManager;
    private FloatingActionButton buttonSave;

    public EditorFragment() {}

    public static EditorFragment newInstance() {
        EditorFragment fragment = new EditorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (Navigator) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigator.setUpNavBar(false);

        blocksRecycler = view.findViewById(R.id.rv_blocks);
        buttonSave = view.findViewById(R.id.fab_save_project);
        setUpRecycler();
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        if (event.getAction() == DragEvent.ACTION_DROP) {
            Toast.makeText(this.getContext(), "DROP", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void setUpRecycler() {
        int orientation = Configuration.ORIENTATION_PORTRAIT;
        if (getContext() != null) {
            orientation = this.getContext().getResources().getConfiguration().orientation;
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        }
        blocksRecycler.setLayoutManager(layoutManager);
        blockAdapter = new BlockAdapter();
        blockAdapter.setHasStableIds(true);
        blocksRecycler.setAdapter(blockAdapter);
    }
}