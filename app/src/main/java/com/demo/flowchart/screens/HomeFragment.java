package com.demo.flowchart.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.flowchart.App;
import com.demo.flowchart.R;
import com.demo.flowchart.adapters.FlowchartAdapter;
import com.demo.flowchart.database.FlowchartEntity;
import com.demo.flowchart.navigation.Navigator;
import com.demo.flowchart.viewmodels.EditorViewModel;
import com.demo.flowchart.viewmodels.HomeViewModel;

public class HomeFragment extends Fragment implements FlowchartAdapter.FlowchartListener {

    private Navigator navigator;

    private RecyclerView recycler;
    private FlowchartAdapter adapter;
    //HomeViewModel homeViewModel;


    public HomeFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (Navigator) context;
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        navigator.setUpNavBar(true);

        recycler = view.findViewById(R.id.rv_flowcharts);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        adapter = new FlowchartAdapter(this);
        recycler.setAdapter(adapter);
        // TESt
        adapter.setFlowcharts(App.getInstance().getDatabase().flowchartDao().getAll());
    }

    @Override
    public void onFlowchartClick(long flowchartId) {
        navigator.navigateTo(EditorFragment.newInstance(flowchartId));
    }

//    @Override
//    public void onCloudUploadClick(FlowchartEntity flowchartEntity) {
//
//    }
//
//    @Override
//    public void onDeleteProjectClick(FlowchartEntity flowchartEntity) {
//        homeViewModel.deleteProject(flowchartEntity);
//    }
}
