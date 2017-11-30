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
import com.goer.view.EventDetailActivity;
import com.goer.helper.GlobalHelper;
import com.goer.model.Invitation;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import android.content.Intent;

import java.util.ArrayList;

public class InvitationRVAdapter extends RecyclerView.Adapter<InvitationRVAdapter.InvitationViewHolder> {

    public static class InvitationViewHolder extends RecyclerView.ViewHolder {

        TextView invitation_usrname, invitation_event;
        ImageView invitation_avatar;
        Button btnAttend;

        InvitationViewHolder(View itemView) {
            super(itemView);
            invitation_usrname = (TextView)itemView.findViewById(R.id.invitation_usrname);
            invitation_event = (TextView)itemView.findViewById(R.id.invitation_event);
            invitation_avatar = (ImageView) itemView.findViewById(R.id.invitation_avatar);
            btnAttend = (Button) itemView.findViewById(R.id.btnAttend);
        }
    }

    ArrayList<Invitation> invitations;

    public InvitationRVAdapter(ArrayList<Invitation> invitations){
        updateDataSet(invitations);
    }
    public void updateDataSet(ArrayList<Invitation> invitations){
        if(invitations != null)
            this.invitations = invitations;
        else
            this.invitations = new ArrayList<Invitation>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public InvitationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invitation, viewGroup, false);
        InvitationViewHolder ivh = new InvitationViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(final InvitationViewHolder ivh, final int i) {
        ivh.invitation_usrname.setText(GlobalHelper.fromHtml(invitations.get(i).getInvitationMsg()));
        ivh.invitation_event.setText(invitations.get(i).getEventName());
        UrlImageViewHelper.setUrlDrawable(ivh.invitation_avatar, invitations.get(i).getSenderAvatarURL());

        ivh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(ivh.itemView.getContext(), EventDetailActivity.class);
                    intent.putExtra("EventModel", EventController.getEventByEventID(invitations.get(i).getEventID()));
                    ivh.itemView.getContext().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        ivh.btnAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(EventController.joinEvent(invitations.get(i).getRecieverID(), invitations.get(i).getEventID())){
                        invitations.remove(i);
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
        return invitations.size();
    }

}