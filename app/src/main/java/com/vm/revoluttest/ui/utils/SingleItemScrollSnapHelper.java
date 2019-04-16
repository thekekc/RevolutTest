package com.vm.revoluttest.ui.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;


public class SingleItemScrollSnapHelper extends LinearSnapHelper {
    private int lastTargetPosition = 0;
    private OrientationHelper orientationHelper;
    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        View centerView = findSnapView(layoutManager);
        if (centerView == null) {
            return RecyclerView.NO_POSITION;
        }

        int position = layoutManager.getPosition(centerView);
        int targetPosition = -1;
        if (layoutManager.canScrollHorizontally()) {
            if (velocityX < 0) {
                targetPosition = position - 1;
            } else {
                targetPosition = position + 1;
            }
        }

        if (layoutManager.canScrollVertically()) {
            if (velocityY < 0) {
                targetPosition = position - 1;
            } else {
                targetPosition = position + 1;
            }
        }
        if(Math.abs(lastTargetPosition - targetPosition)> 1){
            targetPosition = position;
        }
        final int firstItem = 0;
        final int lastItem = layoutManager.getItemCount() - 1;
        targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
        lastTargetPosition = targetPosition;
        return targetPosition;
    }

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
            int[] out = new int[2];
            if (layoutManager.canScrollHorizontally()) {
                out[0] = distanceToLeftEdge(layoutManager, targetView,
                        getOrientationHelper(layoutManager));
            } else {
                out[0] = 0;
            }

            if (layoutManager.canScrollVertically()) {
                //noinspection ConstantConditions
                out[1] = super.calculateDistanceToFinalSnap(layoutManager, targetView)[1];
            } else {
                out[1] = 0;
            }
            return out;
    }

    private int distanceToLeftEdge(@NonNull RecyclerView.LayoutManager layoutManager,
                                 @NonNull View targetView, OrientationHelper helper) {
        final int childLeftEdge = helper.getDecoratedStart(targetView);
        final int containerLeftEdge;
        if (layoutManager.getClipToPadding()) {
            containerLeftEdge = helper.getStartAfterPadding();
        } else {
            containerLeftEdge = 0;
        }
        return childLeftEdge - containerLeftEdge;
    }
    @NonNull
    private OrientationHelper getOrientationHelper(
            @NonNull RecyclerView.LayoutManager layoutManager) {
        if (orientationHelper == null) {
            orientationHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return orientationHelper;
    }
}
