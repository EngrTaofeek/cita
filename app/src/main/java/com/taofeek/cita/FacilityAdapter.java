package com.taofeek.cita;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

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

    }

    @NonNull
    @Override
    public FacilityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    class FacilityHolder extends RecyclerView.ViewHolder{
        public FacilityHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
