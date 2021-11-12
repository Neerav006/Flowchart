package com.demo.flowchart.viewmodels;

import androidx.lifecycle.ViewModel;

import com.demo.flowchart.App;
import com.demo.flowchart.database.FlowchartDao;

public class HomeViewModel extends ViewModel {

    private final FlowchartDao flowchartDao;

    public HomeViewModel() {
        this.flowchartDao = App.getInstance().getDatabase().flowchartDao();
    }


}
