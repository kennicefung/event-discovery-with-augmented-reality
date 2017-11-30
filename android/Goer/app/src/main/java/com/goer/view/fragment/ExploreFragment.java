package com.goer.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import com.goer.R;
import com.goer.view.EventDetailActivity;
import com.goer.view.adapter.ExploreEventRVAdapter;
import com.goer.controller.EventController;
import com.goer.model.Event;


import java.util.ArrayList;
import android.view.MenuInflater;
public class ExploreFragment extends GoerFragment {

    private RecyclerView rv;
    private SwipeRefreshLayout srl;
    private SliderLayout recommend_event;
    private String uid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        uid = getArguments().getString("user_id");
        rv = (RecyclerView)v.findViewById(R.id.rv_explore);
        srl = (SwipeRefreshLayout)v.findViewById(R.id.explore_refresh_layout);
        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(llm);
        refreshItems();
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        recommend_event = (SliderLayout)v.findViewById(R.id.recommend_event);
        ArrayList<Event> re = EventController.getRecommendation(uid);
        if(re != null ) {
            v.findViewById(R.id.recommend_content).setVisibility(View.VISIBLE);
            for (Event e : re) {
                TextSliderView textSliderView = new TextSliderView(getActivity().getApplicationContext());
                textSliderView
                        .description(e.title)
                        .image(e.img)
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                                intent.putExtra("EventModel", slider.getBundle().getSerializable("EventModel"));
                                startActivity(intent);

                            }
                        });

                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putSerializable("EventModel", e);

                recommend_event.addSlider(textSliderView);
            }
            recommend_event.setDuration(5000);
            recommend_event.getPagerIndicator().setVisibility(View.INVISIBLE);
        }
        else
        {
            v.findViewById(R.id.recommend_content).setVisibility(View.GONE);
        }

        return v;
    }


    @Override
    public Boolean isAddEventButtonVisible() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_explore, menu);
        super.onCreateOptionsMenu(menu,inflater);

        MenuItem action_search = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) action_search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    ((ExploreEventRVAdapter)rv.getAdapter()).filter("");
                } else {
                    ((ExploreEventRVAdapter)rv.getAdapter()).filter(newText);
                }
                return true;
            }
        });
    }

    public void refreshItems(){
        try {
            ArrayList<Event> events = EventController.getAllEvents(uid);

            if(rv.getAdapter() == null) {
                ExploreEventRVAdapter adapter = new ExploreEventRVAdapter(events);
                rv.setAdapter(adapter);
            } else {
                ExploreEventRVAdapter adapter = (ExploreEventRVAdapter)rv.getAdapter();
                adapter.updateDataSet(events);
                adapter.notifyDataSetChanged();
                srl.setRefreshing(false);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
