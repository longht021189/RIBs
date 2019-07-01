package com.uber.rib.core.navigation;

import androidx.annotation.Nullable;

public interface Node {
    void onNavigation(@Nullable String child);
}