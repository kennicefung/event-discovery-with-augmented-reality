package com.goer.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goer.view.adapter.FriendNewsRVAdapter;
import com.goer.R;
import com.goer.controller.UserController;
import com.goer.model.UserAction;

import java.util.ArrayList;

public class FriendNewsFragment extends GoerFragment {

    private RecyclerView rv;
    private SwipeRefreshLayout srl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_friends_content, container, false);
        rv = (RecyclerView)v.findViewById(R.id.rv_friends);
        srl = (SwipeRefreshLayout)v.findViewById(R.id.friends_refresh_layout);
        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(llm);
        refreshItems();
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        return v;
    }


    @Override
    public Boolean isAddEventButtonVisible() {
        return false;
    }

    public void refreshItems(){
        try {
            ArrayList<UserAction> news = UserController.getFriendsNews(getContext());
            if(rv.getAdapter() == null) {
                FriendNewsRVAdapter adapter = new FriendNewsRVAdapter(news);
                rv.setAdapter(adapter);
            } else {
                FriendNewsRVAdapter adapter = (FriendNewsRVAdapter)rv.getAdapter();
                adapter.updateDataSet(news);
                adapter.notifyDataSetChanged();
                srl.setRefreshing(false);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
