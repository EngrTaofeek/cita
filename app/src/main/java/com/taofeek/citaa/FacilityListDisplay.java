package com.taofeek.citaa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.taofeek.citaa.customer.HomeActivity;
import com.taofeek.citaa.customer.UserEditActivity;
import com.taofeek.citaa.customer.UserGuidelineFragment;

public class FacilityListDisplay extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference detailsRef = db.collection("facility_details").
            document("details").collection("profile");
    private FacilityAdapter adapter;
    public String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_list_display);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent intent = getIntent();
        category = intent.getStringExtra(UserGuidelineFragment.CATEGORY);
        TextView title = findViewById(R.id.book_title);
        title.setText(category);

        setUpRecyclerView();
        ImageView backImage = findViewById(R.id.imageView);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FacilityListDisplay.this, HomeActivity.class));
                finish();
            }
        });
    }
    private void setUpRecyclerView() {
        Query query = detailsRef.orderBy("name", Query.Direction.ASCENDING).whereEqualTo("category",category);

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