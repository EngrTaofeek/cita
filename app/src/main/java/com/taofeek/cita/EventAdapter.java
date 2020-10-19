package com.taofeek.cita;

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
import com.taofeek.cita.customer.BookingActivity;

public class EventAdapter extends FirestoreRecyclerAdapter<EventDataModel, EventAdapter.EventHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context mContext;
    public EventAdapter(@NonNull FirestoreRecyclerOptions<EventDataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventAdapter.EventHolder holder, int position, @NonNull EventDataModel model) {
        holder.title.setText(model.getTitle());
        holder.email.setText(model.getEmail());
        holder.address.setText(model.getAddress());
        holder.email_text = model.getEmail();
        holder.facility_name = model.getTitle();
        if(model.getImage_url() !=null){
            Picasso.get().load(model.getImage_url()).placeholder(R.drawable.image_loading) // during loading this image will be set imageview
                    //if image is failed to load - this image is set to imageview
                    .networkPolicy(NetworkPolicy.OFFLINE) //stores images for offline view
                    .fit().centerCrop().into(holder.profile);
        }




    }

    @NonNull
    @Override
    public EventAdapter.EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item,
                parent, false);
        return new EventAdapter.EventHolder(v);
    }

    public class EventHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView email;
        TextView address;
        ImageView profile;
        Button book_button;
        public String email_text,facility_name;
        public EventHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.event_title);
            email = itemView.findViewById(R.id.event_mail);
            address = itemView.findViewById(R.id.event_address);
            profile = itemView.findViewById(R.id.event_image);
            book_button = itemView.findViewById(R.id.event_view_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext = view.getContext();
                    Intent intent = new Intent(mContext, BookingActivity.class);
                    //intent.putExtra(BookingActivity.emailItem, email_text);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("facility_item_id", email_text);
                    editor.putString("facility_item_name", facility_name);//InputString: from the EditText
                    editor.apply();
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
