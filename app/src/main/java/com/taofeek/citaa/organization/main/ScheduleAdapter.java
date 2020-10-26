package com.taofeek.citaa.organization.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.taofeek.citaa.R;
import com.taofeek.citaa.organization.ScheduleDataModel;

public class ScheduleAdapter extends FirestoreRecyclerAdapter<ScheduleDataModel, ScheduleAdapter.ScheduleHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ScheduleAdapter(@NonNull FirestoreRecyclerOptions<ScheduleDataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ScheduleHolder holder, int position, @NonNull ScheduleDataModel model) {
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.email.setText(model.getEmail());
    }

    @NonNull
    @Override
    public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_item,
                parent, false);
        return new ScheduleAdapter.ScheduleHolder(v);

    }

    class ScheduleHolder extends RecyclerView.ViewHolder{
        TextView  date, time, position, email;


        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.schedule_email);
            date = itemView.findViewById(R.id.schedule_date);
            time = itemView.findViewById(R.id.schedule_time);

        }
    }
}
