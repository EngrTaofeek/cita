package com.taofeek.cita;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FacilityListDisplay extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference detailsRef = db.collection("facility_details").
            document("details").collection("profile");
    private FacilityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_list_display);

        setUpRecyclerView();
    }
    private void setUpRecyclerView() {
        Query query = detailsRef.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<FacilityDataModel> options = new FirestoreRecyclerOptions.Builder<FacilityDataModel>()
                .setQuery(query, FacilityDataModel.class)
                .build();

        adapter = new FacilityAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}