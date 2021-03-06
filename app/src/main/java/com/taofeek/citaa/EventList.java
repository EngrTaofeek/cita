package com.taofeek.citaa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.taofeek.citaa.customer.HomeActivity;
import com.taofeek.citaa.customer.UserEditActivity;

public class EventList extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference detailsRef = db.collection("facility_details")
            .document("details").collection("event");
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setUpRecyclerView();
        ImageView backImage = findViewById(R.id.imageView);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventList.this, HomeActivity.class));
                finish();
            }
        });
    }
    private void setUpRecyclerView() {
        Query query = detailsRef.orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<EventDataModel> options = new FirestoreRecyclerOptions.Builder<EventDataModel>()
                .setQuery(query, EventDataModel.class)
                .build();

        adapter = new EventAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.event_list_recyclerview);


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