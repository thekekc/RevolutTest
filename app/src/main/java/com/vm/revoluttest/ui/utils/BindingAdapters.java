package com.vm.revoluttest.ui.utils;

import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.databinding.BindingAdapter;

public class BindingAdapters {
    @BindingAdapter("app:src")
    public static void setSrcVector(ImageView view, @DrawableRes int drawable) {
        view.setImageResource(drawable);
    }
}
