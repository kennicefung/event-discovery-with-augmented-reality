package com.goer.view.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.goer.R;
import com.goer.model.Event;
import com.goer.view.EventDetailActivity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;
import java.util.Locale;

public class ExploreEventRVAdapter extends RecyclerView.Adapter<ExploreEventRVAdapter.EventViewHolder> {

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView event_title, event_date, event_host;
        ImageView event_image;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            event_title = (TextView)itemView.findViewById(R.id.event_name);
            event_date = (TextView) itemView.findViewById(R.id.event_date);
            event_host = (TextView) itemView.findViewById(R.id.event_host);
            event_image = (ImageView) itemView.findViewById(R.id.event_image);
        }
    }

    ArrayList<Event> searched_events;
    ArrayList<Event> all_events;

    public ExploreEventRVAdapter(ArrayList<Event> events){
        updateDataSet(events);
    }

    public void updateDataSet(ArrayList<Event> events){
        if(events != null) {
            this.all_events = (ArrayList<Event>) events.clone();
            this.searched_events = events;
        } else {
            this.all_events = new ArrayList<Event>();
            this.searched_events = new ArrayList<Event>();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event_card, viewGroup, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(final EventViewHolder eventViewHolder, final int i) {
        eventViewHolder.event_title.setText(searched_events.get(i).title);
        eventViewHolder.event_date.setText(searched_events.get(i).getFormattedStartTime("dd MMM"));
        eventViewHolder.event_host.setText("By " + searched_events.get(i).user_name);
        UrlImageViewHelper.setUrlDrawable(eventViewHolder.event_image, searched_events.get(i).img);

        eventViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(eventViewHolder.itemView.getContext(), EventDetailActivity.class);
                intent.putExtra("EventModel", searched_events.get(i));
                eventViewHolder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searched_events.size();
    }

    // Filter method
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        searched_events.clear();
        if (charText.length() == 0) {
            searched_events.addAll(all_events);
        } else {
            for (Event e : all_events) {
                if (e.title.toLowerCase(Locale.getDefault()).contains(charText)) {
                    searched_events.add(e);
                }
            }
        }
        notifyDataSetChanged();
    }
}