package com.goer.view.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;


public abstract class GoerFragment extends Fragment{
    protected FrameLayout fragmentContainer;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public void refresh() {
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    public void display() {
        /*if (fragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            fragmentContainer.startAnimation(fadeIn);
        }*/
    }

    public void hide() {
        /*if (fragmentContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            fragmentContainer.startAnimation(fadeOut);
        }*/
    }

    public abstract Boolean isAddEventButtonVisible();

}
