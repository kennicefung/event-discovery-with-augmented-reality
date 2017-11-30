package com.goer.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goer.view.adapter.FriendRequestRVAdapter;
import com.goer.R;
import com.goer.controller.UserController;
import com.goer.model.User;

import java.util.ArrayList;

public class FriendRequestFragment extends GoerFragment {

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
            ArrayList<User> requests = UserController.getFriendsRequest(getContext());
            if(rv.getAdapter() == null) {
                FriendRequestRVAdapter adapter = new FriendRequestRVAdapter(UserController.getUserModel(getContext()).getUserID(), requests);
                rv.setAdapter(adapter);
            } else {
                FriendRequestRVAdapter adapter = (FriendRequestRVAdapter)rv.getAdapter();
                adapter.updateDataSet(requests);
                adapter.notifyDataSetChanged();
                srl.setRefreshing(false);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
