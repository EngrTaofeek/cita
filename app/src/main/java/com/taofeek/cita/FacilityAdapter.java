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
import com.taofeek.cita.customer.EventBooking;

import static java.security.AccessController.getContext;

public class FacilityAdapter extends FirestoreRecyclerAdapter<FacilityDataModel, FacilityAdapter.FacilityHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context mContext;
    public FacilityAdapter(@NonNull FirestoreRecyclerOptions<FacilityDataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FacilityHolder holder, int position, @NonNull FacilityDataModel model) {
        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        holder.address.setText(model.getAddress());
        holder.email_text = model.getEmail();
        holder.facility_name = model.getName();
        if(model.getImage_url() !=null){
            Picasso.get().load(model.getImage_url()).placeholder(R.drawable.image_loading) // during loading this image will be set imageview
                     //if image is failed to load - this image is set to imageview
                    .networkPolicy(NetworkPolicy.OFFLINE) //stores images for offline view
                    .fit().centerCrop().into(holder.profile);
        }




        }

    @NonNull
    @Override
    public FacilityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.facility_list_item,
                parent, false);
        return new FacilityHolder(v);
    }

    public class FacilityHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView email;
        TextView address;
        ImageView profile;
        Button book_button;
        public String email_text,facility_name;
        public FacilityHolder(@NonNull final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_name);
            email = itemView.findViewById(R.id.list_email);
            address = itemView.findViewById(R.id.list_address);
            profile = itemView.findViewById(R.id.list_picture);
            book_button = itemView.findViewById(R.id.list_button_book_now);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext = view.getContext();
                    Intent intent = new Intent(mContext, EventBooking.class);
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
