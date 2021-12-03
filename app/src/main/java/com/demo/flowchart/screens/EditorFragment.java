package com.demo.flowchart.screens;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.flowchart.R;
import com.demo.flowchart.adapters.BlockAdapter;
import com.demo.flowchart.drawing.WorkspaceView;
import com.demo.flowchart.navigation.Navigator;
import com.demo.flowchart.viewmodels.EditorViewModel;
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
    private FloatingActionButton buttonViewToPdf;

    private Group editGroup;
    private FloatingActionButton buttonEditBlock;
    private FloatingActionButton buttonDeleteBlock;

    private long flowchartId;

    public EditorFragment() {}

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
            viewModel.loadWorkspace(flowchartId);
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
        buttonViewToPdf = view.findViewById(R.id.fab_view_to_pdf);

        setUpRecycler();

        workspaceView.setWorkspace(viewModel.loadWorkspace(flowchartId));

        buttonSave.setOnClickListener(v -> {
            viewModel.saveWorkspace(workspaceView.getWorkspace());
        });

        buttonViewToPdf.setOnClickListener(v -> {
           if( viewModel.viewToPdf(workspaceView.getWorkspace())){
               Toast.makeText(getContext(), "File created: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Toast.LENGTH_LONG).show();
           }else{
               Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
           }
        });

        editGroup = view.findViewById(R.id.edit_group);
        buttonEditBlock = view.findViewById(R.id.fab_edit_block);
        buttonDeleteBlock = view.findViewById(R.id.fab_delete_block);

        workspaceView.setSelectedBlockListener( isSelected -> {
            if (isSelected) {
                editGroup.setVisibility(View.VISIBLE);
            } else {
                editGroup.setVisibility(View.GONE);
            }
        });

        buttonEditBlock.setOnClickListener( v -> {
            String oldText = workspaceView.getBlockText();
            EditText editText = new EditText(requireContext());
            editText.setText(oldText);

            new AlertDialog.Builder(requireContext())
                    .setTitle("Edit block text")
                    .setView(editText)
                    .setCancelable(true)
                    .setPositiveButton("Save", (dialog, which) -> {
                        String newText = editText.getText().toString();
                        workspaceView.editBlockText(newText);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
        buttonDeleteBlock.setOnClickListener( v -> { workspaceView.deleteBlock(); });
    }

    @Override
    public void onDestroyView() {
        clearViews();
        super.onDestroyView();
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

    private void clearViews() {
        // TODO assign all views to NULL
    }
}