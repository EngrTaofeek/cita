package com.taofeek.citaa.organization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.taofeek.citaa.EventAdapter;
import com.taofeek.citaa.EventDataModel;
import com.taofeek.citaa.R;
import com.taofeek.citaa.customer.EventBooking;

public class EventHistoryAdapter extends FirestoreRecyclerAdapter<EventHistoryDataModel, EventHistoryAdapter.EventHistoryHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context mContext;
    public EventHistoryAdapter(@NonNull FirestoreRecyclerOptions<EventHistoryDataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventHistoryAdapter.EventHistoryHolder holder, int position, @NonNull EventHistoryDataModel model) {
        holder.title.setText(model.getTitle());
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());





    }

    @NonNull
    @Override
    public EventHistoryAdapter.EventHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_history_item,
                parent, false);
        return new EventHistoryAdapter.EventHistoryHolder(v);
    }

    public class EventHistoryHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView date;
        TextView time;
        public EventHistoryHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.event_title);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);

        }
    }
}
