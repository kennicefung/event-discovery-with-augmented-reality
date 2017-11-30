package com.goer.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.goer.R;
import com.goer.controller.UserController;
import com.goer.model.User;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

public class FriendRequestRVAdapter extends RecyclerView.Adapter<FriendRequestRVAdapter.FriendRequestViewHolder> {

    public static class FriendRequestViewHolder extends RecyclerView.ViewHolder {

        TextView friend_request_usrname;
        ImageView friend_request_avatar;
        Button btnAccept, btnCancel;

        FriendRequestViewHolder(View itemView) {
            super(itemView);
            friend_request_usrname = (TextView)itemView.findViewById(R.id.friend_request_usrname);
            friend_request_avatar = (ImageView) itemView.findViewById(R.id.friend_request_avatar);
            btnAccept = (Button) itemView.findViewById(R.id.btnAccept);
            btnCancel = (Button) itemView.findViewById(R.id.btnCancel);
        }
    }

    ArrayList<User> requests;
    String uid;

    public FriendRequestRVAdapter(String uid, ArrayList<User> requests){
        this.uid = uid;
        updateDataSet(requests);
    }
    public void updateDataSet(ArrayList<User> requests){
        if(requests != null)
            this.requests = requests;
        else
            this.requests = new ArrayList<User>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public FriendRequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_friend_request, viewGroup, false);
        FriendRequestViewHolder fvh = new FriendRequestViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(final FriendRequestViewHolder fvh, final int i) {
        fvh.friend_request_usrname.setText(requests.get(i).getUsername());
        UrlImageViewHelper.setUrlDrawable(fvh.friend_request_avatar, requests.get(i).getAvatarURL());

        fvh.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(UserController.sendFriendRequest(uid, requests.get(i).getUserID())){
                        requests.remove(i);
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        fvh.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(UserController.unfriend(uid, requests.get(i).getUserID())){
                        requests.remove(i);
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

}