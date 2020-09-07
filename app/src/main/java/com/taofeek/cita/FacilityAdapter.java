package com.taofeek.cita;

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

import static java.security.AccessController.getContext;

class FacilityAdapter extends FirestoreRecyclerAdapter<FacilityDataModel, FacilityAdapter.FacilityHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FacilityAdapter(@NonNull FirestoreRecyclerOptions<FacilityDataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FacilityHolder holder, int position, @NonNull FacilityDataModel model) {
        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        holder.address.setText(model.getAddress());
        if(model.getImage_url() !=null){
            Picasso.get().load(model.getImage_url()).into(holder.profile);
        }



        }

    @NonNull
    @Override
    public FacilityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.facility_list_item,
                parent, false);
        return new FacilityHolder(v);
    }

    class FacilityHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView email;
        TextView address;
        ImageView profile;
        Button book_button;
        public FacilityHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_name);
            email = itemView.findViewById(R.id.list_email);
            address = itemView.findViewById(R.id.list_address);
            profile = itemView.findViewById(R.id.list_picture);
            book_button = itemView.findViewById(R.id.list_button_book_now);
             }
    }
}
