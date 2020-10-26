package com.taofeek.citaa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class EventList extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference detailsRef = db.collection("facility_details")
            .document("details").collection("event");
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        setUpRecyclerView();
    }
    private void setUpRecyclerView() {
        Query query = detailsRef.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<EventDataModel> options = new FirestoreRecyclerOptions.Builder<EventDataModel>()
                .setQuery(query, EventDataModel.class)
                .build();

        adapter = new EventAdapter(options);
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