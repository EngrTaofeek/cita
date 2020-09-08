package com.taofeek.cita.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.taofeek.cita.HomeActivity;
import com.taofeek.cita.R;

public class BookingActivity extends AppCompatActivity {
    public static final String emailItem = "facility_email";
    public String mEmail;
    public ImageView profile ;
    public TextView title, address, email, phone, overview, capacity, others;
    public Button bookNow, buttonDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        //Intent intent = getIntent();
        //mEmail = intent.getParcelableExtra(emailItem);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(BookingActivity.this);
        final String data = prefs.getString("facility_item_id", "teslim@yahoo.com");
        mEmail = data;
        profile= findViewById(R.id.book_image);
        title = findViewById(R.id.book_title);
        address = findViewById(R.id.address_value);
        email = findViewById(R.id.email_value);
        phone = findViewById(R.id.phone_value);
        overview = findViewById(R.id.overview_value);
        capacity = findViewById(R.id.capacity_value);
        others = findViewById(R.id.others_value);
        populateProfilePhoto();
        populateTextView("name",title);
        populateTextView("address",address);
        populateTextView("email",email);
        populateTextView("phone",phone);
        populateTextView("overview",overview);
        populateTextView("capacity",capacity);
        populateTextView("others",others);



    }
    private void populateProfilePhoto() {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("facility_details").document("details").collection("profile")
                .document(mEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if ( document.exists()) {
                        String field = document.getString("image_url");
                        Picasso.get().load(field).into(profile);
                    }
                }
            }
        });



        // Picasso.get().load(mImageUri).into(mImageView);

    }
    private void populateTextView(final String key, final TextView textView) {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("facility_details").document("details").collection("profile")
                .document(mEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if ( document.exists()) {
                        String field = document.getString(key);
                        textView.setText(field);

                    }
                }
            }
        });



    }

}