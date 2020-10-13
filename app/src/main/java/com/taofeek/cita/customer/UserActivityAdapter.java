package com.taofeek.cita.customer;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taofeek.cita.R;

import java.util.HashMap;
import java.util.Map;

public class UserActivityAdapter extends FirestoreRecyclerAdapter<ActivityDataModel, UserActivityAdapter.UserActivityHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UserActivityAdapter(@NonNull FirestoreRecyclerOptions<ActivityDataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserActivityHolder holder, int position, @NonNull ActivityDataModel model) {

        holder.name.setText(model.getName());
        holder.time.setText(model.getTime());
        holder.date.setText(model.getDate());
        holder.status.setText(model.getStatus());
    }

    @NonNull
    @Override
    public UserActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_schedule_item,
                parent, false);
        return new UserActivityHolder(v);
    }

    class UserActivityHolder extends RecyclerView.ViewHolder {
        TextView name, time, date, number, status;

        Button book_button;
        public String email_text;

        public UserActivityHolder(@NonNull final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.schedule_name_facility);
            time = itemView.findViewById(R.id.schedule_time);
            date = itemView.findViewById(R.id.schedule_date);
            status = itemView.findViewById(R.id.user_schedule_status);




        }


    }
}
