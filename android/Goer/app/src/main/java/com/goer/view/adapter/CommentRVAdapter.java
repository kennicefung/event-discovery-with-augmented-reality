package com.goer.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.goer.model.Comment;

import com.goer.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

public class CommentRVAdapter extends RecyclerView.Adapter<CommentRVAdapter.CommentViewHolder> {

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView comment_usrname, comment_content, comment_cdate;
        ImageView comment_avatar;

        CommentViewHolder(View itemView) {
            super(itemView);
            comment_usrname = (TextView)itemView.findViewById(R.id.comment_usrname);
            comment_content = (TextView)itemView.findViewById(R.id.comment_content);
            comment_cdate = (TextView)itemView.findViewById(R.id.comment_cdate);
            comment_avatar = (ImageView) itemView.findViewById(R.id.comment_avatar);
        }
    }

    ArrayList<Comment> comments;

    public CommentRVAdapter(ArrayList<Comment> requests){
        updateDataSet(requests);
    }
    public void updateDataSet(ArrayList<Comment> requests){
        if(requests != null)
            this.comments = requests;
        else
            this.comments = new ArrayList<Comment>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);
        CommentViewHolder cvh = new CommentViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder cvh, final int i) {
        cvh.comment_usrname.setText(comments.get(i).getUsername());
        cvh.comment_content.setText(comments.get(i).getContent());
        cvh.comment_cdate.setText(comments.get(i).getCDate());
        UrlImageViewHelper.setUrlDrawable(cvh.comment_avatar, comments.get(i).getAvatarURL());

        cvh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(fvh.itemView.getContext(), EventDetailActivity.class);
                intent.putExtra("EventModel", searched_events.get(i));
                eventViewHolder.itemView.getContext().startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

}