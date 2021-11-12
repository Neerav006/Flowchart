package com.demo.flowchart.editor.view;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.flowchart.R;
import com.demo.flowchart.editor.model.DrawingBlock;
import com.demo.flowchart.navigation.Navigator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class EditorFragment extends Fragment implements View.OnDragListener {

    public static final String FLOWCHART_ID_KEY = "flowchartIdKey";

    private EditorViewModel viewModel;

    private Navigator navigator;
    private RecyclerView blocksRecycler;
    private BlockAdapter blockAdapter;
    private LinearLayoutManager layoutManager;
    private WorkspaceView workspaceView;
    private FloatingActionButton buttonSave;

    private long flowchartId;

    public EditorFragment() {
    }

//    public static EditorFragment newInstance() {
//        return new EditorFragment();
//    }

    public static EditorFragment newInstance(long flowchartId) {
        EditorFragment fragment = new EditorFragment();
        Bundle args = new Bundle();
        args.putLong(FLOWCHART_ID_KEY, flowchartId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (Navigator) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(EditorViewModel.class);

        if (getArguments() != null) {
            flowchartId = getArguments().getLong(FLOWCHART_ID_KEY);
            viewModel.loadFlowchart(flowchartId);
        }
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
        workspaceView = view.findViewById(R.id.view_workspace);
        buttonSave = view.findViewById(R.id.fab_save_project);

        setUpRecycler();

        workspaceView.setDrawingBlocks(viewModel.getDrawingBlocks());

        buttonSave.setOnClickListener(v -> {
            viewModel.setDrawingBlocks(workspaceView.getDrawingBlocks());
            viewModel.saveFlowchart();
        });
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        if (event.getAction() == DragEvent.ACTION_DROP) {
            Toast.makeText(this.getContext(), "DROP", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static boolean flag = false;

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
        if (flag) {
            DrawingBlock.counter = viewModel.getBlocksCount() - 1;
        }else{
            flag = true;
        }
    }
}