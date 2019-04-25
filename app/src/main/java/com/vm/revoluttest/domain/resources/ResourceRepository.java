package com.vm.revoluttest.domain.resources;

import androidx.annotation.IdRes;

public interface ResourceRepository {
    @IdRes
    int getDrawableIdByName(String name);

    String getStringByName(String name);
}
