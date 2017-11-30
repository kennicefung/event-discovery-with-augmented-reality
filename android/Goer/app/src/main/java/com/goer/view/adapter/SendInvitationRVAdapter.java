package com.goer.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.goer.R;
import com.goer.controller.EventController;
import com.goer.model.User;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

public class SendInvitationRVAdapter extends RecyclerView.Adapter<SendInvitationRVAdapter.SendInvitationViewHolder> {

    String event_id, sender_id;
    public static class SendInvitationViewHolder extends RecyclerView.ViewHolder {

        TextView select_friend_usrname;
        ImageView select_friend_avatar;
        Button btnInvite;

        SendInvitationViewHolder(View itemView) {
            super(itemView);
            select_friend_usrname = (TextView)itemView.findViewById(R.id.select_friend_usrname);
            select_friend_avatar = (ImageView) itemView.findViewById(R.id.select_friend_avatar);
            btnInvite = (Button) itemView.findViewById(R.id.btnInvite);
        }
    }

    ArrayList<User> friends;

    public SendInvitationRVAdapter(String event_id, String sender_id, ArrayList<User> friends){
        this.event_id = event_id;
        this.sender_id = sender_id;
        updateDataSet(friends);
    }
    public void updateDataSet(ArrayList<User> friends){
        if(friends != null)
            this.friends = friends;
        else
            this.friends = new ArrayList<User>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public SendInvitationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_select_friend, viewGroup, false);
        SendInvitationViewHolder svh = new SendInvitationViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(final SendInvitationViewHolder svh, final int i) {
        svh.select_friend_usrname.setText(friends.get(i).getUsername());
        UrlImageViewHelper.setUrlDrawable(svh.select_friend_avatar, friends.get(i).getAvatarURL());

        svh.btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(EventController.sendInvitation(sender_id, event_id, friends.get(i).getUserID())){
                        v.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

}