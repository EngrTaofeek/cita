package com.taofeek.cita.organization;


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
import com.squareup.picasso.Picasso;
import com.taofeek.cita.FacilityDataModel;
import com.taofeek.cita.R;
import com.taofeek.cita.customer.BookingActivity;

import static java.security.AccessController.getContext;

class ScheduleAdapter extends FirestoreRecyclerAdapter<ScheduleDataModel, com.taofeek.cita.organization.ScheduleAdapter.ScheduleHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context mContext;
    public ScheduleAdapter(@NonNull FirestoreRecyclerOptions<ScheduleDataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull com.taofeek.cita.organization.ScheduleAdapter.ScheduleHolder holder, int position, @NonNull ScheduleDataModel model) {
        int num = position + 1;
        String pos = String.valueOf(num);
        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        holder.time.setText(model.getTime());
        holder.date.setText(model.getDate());
        holder.number.setText(pos);






    }

    @NonNull
    @Override
    public ScheduleAdapter.ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_item,
                parent, false);
        return new com.taofeek.cita.organization.ScheduleAdapter.ScheduleHolder(v);
    }

    class ScheduleHolder extends RecyclerView.ViewHolder{
        TextView name,email,time,date, number;

        Button book_button;
        public String email_text;
        public ScheduleHolder(@NonNull final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.schedule_name);
            email = itemView.findViewById(R.id.schedule_email);
            time = itemView.findViewById(R.id.schedule_time);
            date = itemView.findViewById(R.id.schedule_date);
            number = itemView.findViewById(R.id.schedule_number);

        }
    }
}

