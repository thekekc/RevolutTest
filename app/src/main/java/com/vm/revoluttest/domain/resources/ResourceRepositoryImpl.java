package com.vm.revoluttest.domain.resources;

import android.content.Context;

public class ResourceRepositoryImpl implements ResourceRepository {
    private Context context;

    public ResourceRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public int getDrawableIdByName(String name) {
        return context.getResources().getIdentifier(
                "flag_" + name,
                "drawable", context.getPackageName());
    }

    @Override
    public String getStringByName(String name) {
        return context.getString(
                context.getResources().getIdentifier(name,
                        "string", context.getPackageName()));
    }
}
