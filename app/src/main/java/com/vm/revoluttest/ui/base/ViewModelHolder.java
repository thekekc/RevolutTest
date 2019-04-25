package com.vm.revoluttest.ui.base;

import android.app.Fragment;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ViewModelHolder<VM> extends Fragment {

    private VM viewModel;

    public ViewModelHolder() {
    }

    public static <M> ViewModelHolder createContainer(@NonNull M viewModel) {
        ViewModelHolder<M> viewModelContainer = new ViewModelHolder<>();
        viewModelContainer.setViewModel(viewModel);
        return viewModelContainer;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    public VM getViewmodel() {
        return viewModel;
    }

    public void setViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
    }
}
