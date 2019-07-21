package com.uber.rib.core.navigation;

import androidx.annotation.Nullable;
import java.util.Map;

public interface Node {
    void onNavigation(@Nullable String child, @Nullable Object data,
                      @Nullable Map<String, String> queryParameters);

    @Nullable Object onEnterBackStack(@Nullable Object data);
    boolean handleBackPress();
}