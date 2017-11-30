package com.goer.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.goer.R;
import com.goer.helper.GlobalHelper;
import com.goer.model.UserAction;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

public class FriendNewsRVAdapter extends RecyclerView.Adapter<FriendNewsRVAdapter.FriendNewsViewHolder> {

    public static class FriendNewsViewHolder extends RecyclerView.ViewHolder {

        TextView txt_friend_news, friend_news_dt;
        ImageView friend_avatar;

        FriendNewsViewHolder(View itemView) {
            super(itemView);
            txt_friend_news = (TextView)itemView.findViewById(R.id.txt_friend_news);
            friend_news_dt = (TextView)itemView.findViewById(R.id.friend_news_dt);
            friend_avatar = (ImageView) itemView.findViewById(R.id.friend_news_avatar);
        }
    }

    ArrayList<UserAction> news;

    public FriendNewsRVAdapter(ArrayList<UserAction> news){
        updateDataSet(news);
    }

    public void updateDataSet(ArrayList<UserAction> news){
        if(news != null)
            this.news = news;
        else
            this.news = new ArrayList<UserAction>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public FriendNewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_friend_news, viewGroup, false);
        FriendNewsViewHolder fvh = new FriendNewsViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(final FriendNewsViewHolder fvh, final int i) {
        fvh.txt_friend_news.setText(GlobalHelper.fromHtml(news.get(i).getNewsMsg()));
        fvh.friend_news_dt.setText(news.get(i).getActionDt());
        UrlImageViewHelper.setUrlDrawable(fvh.friend_avatar, news.get(i).getAvatarURL());

        fvh.itemView.setOnClickListener(new View.OnClickListener() {
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
        return news.size();
    }

}