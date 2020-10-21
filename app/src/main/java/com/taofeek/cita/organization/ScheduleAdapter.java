package com.taofeek.cita.organization;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.taofeek.cita.FacilityDataModel;
import com.taofeek.cita.R;
import com.taofeek.cita.customer.BookingActivity;

import java.util.HashMap;
import java.util.Map;

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
        holder.status.setText(model.getStatus());
        holder.number.setText(pos);


    }

    @NonNull
    @Override
    public ScheduleAdapter.ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_item,
                parent, false);
        return new com.taofeek.cita.organization.ScheduleAdapter.ScheduleHolder(v);
    }


    class ScheduleHolder extends RecyclerView.ViewHolder {
        TextView name, email, time, date, number, status;

        Button book_button;
        public String email_text;

        public ScheduleHolder(@NonNull final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.schedule_name);
            email = itemView.findViewById(R.id.schedule_email);
            time = itemView.findViewById(R.id.schedule_time);
            date = itemView.findViewById(R.id.schedule_date);
            number = itemView.findViewById(R.id.schedule_number);
            status = itemView.findViewById(R.id.schedule_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.facilitator_schedule_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            email_text = email.getText().toString().trim();
                            switch (item.getItemId()) {
                                case R.id.item_menu_appoint:
                                    status.setText("APPOINTED");
                                    updateStatus("APPROVED", email_text);
                                    return true;
                                case R.id.item_menu_decline:
                                    status.setText("DECLINED");
                                    updateStatus("DECLINED", email_text);
                                    return true;
                                case R.id.item_menu_pending:
                                    status.setText("PENDING");
                                    updateStatus("PENDING", email_text);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    //displaying the popup
                    popup.show();

                }
            });

        }
        public void updateStatus(String status, String userEmail){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(name.getContext());
            final String data = prefs.getString("email_id", "oduola.taofeekkola@gmail.com");
            String mEmail = data;
            Map<String, Object> user = new HashMap<>();
            user.put("status", status );
            db.collection("facility_details").document("details").collection("appointment").
                    document("facilitator").collection("schedule").document(userEmail).update(user);
            db.collection("users").document("details").collection("appointment").
                    document(mEmail).update(user);
        }


    }
}

