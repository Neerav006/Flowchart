package com.demo.flowchart.navigation;

import androidx.fragment.app.Fragment;

public interface Navigator {
    void navigateTo(Fragment destinationFragment, boolean popBackStack);
}
