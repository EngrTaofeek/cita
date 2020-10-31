package com.taofeek.citaa.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.taofeek.citaa.R;

import java.util.HashMap;
import java.util.Map;

public class EventBooking extends AppCompatActivity {
    private String mTime;
    public String mEmail, mFacilityTitle ,mStrHrsToShow,mCurrentDateString, mUserMail,mConsumerName;
    public ImageView profile ;
    public TextView title, address, email, description,phone, time, date, capacity;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NestedScrollView mBook_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_booking);
        SharedPreferences prefs_user = PreferenceManager.getDefaultSharedPreferences(EventBooking.this);
        mUserMail = prefs_user.getString("email_id", "default_email");
        mConsumerName = prefs_user.getString("consumer_name","USER");
        mFacilityTitle = prefs_user.getString("event_item_name", "Facility");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EventBooking.this);
        final String data = prefs.getString("event_item_id", "teslim@yahoo.com");
        mEmail = data;
        mBook_layout = findViewById(R.id.scrollViewBook);
        profile = findViewById(R.id.book_image);
        title = findViewById(R.id.book_title);
        address = findViewById(R.id.address_value);
        email = findViewById(R.id.email_value);
        phone = findViewById(R.id.phone_value);
        description = findViewById(R.id.overview_value);
        capacity = findViewById(R.id.capacity_value);
        time = findViewById(R.id.others_value);
        date = findViewById(R.id.date_value);
        populateProfilePhoto();
        populateTextView("name",title);
        populateTextView("address",address);
        populateTextView("email",email);
        populateTextView("phone",phone);
        populateTextView("description",description);
        populateTextView("permissible_capacity",capacity);
        populateTextView("time",time);
        populateTextView("date", date);
        ImageView backImage = findViewById(R.id.imageView);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventBooking.this, HomeActivity.class));
                finish();
            }
        });
        Button bookButton = findViewById(R.id.button_book);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAppointmentToFacilitator();
                saveUserSchedule();
            }
        });
    }

    private void sendAppointmentToFacilitator() {
        Map<String, Object> user = new HashMap<>();
        user.put("email", mUserMail);
        user.put("name", mConsumerName);
        user.put("event_title", mFacilityTitle);
        user.put("status", "Pending");
        db.collection("facility_details").document("details").collection("appointment").
                document("facilitator").collection(mEmail).document(mUserMail).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(mBook_layout,"You have successfully booked an appointment.", Snackbar.LENGTH_LONG).show();

            }
        });



    }
    private void saveUserSchedule(){
        Map<String, Object> user = new HashMap<>();
        mTime = time.getText().toString();
        mCurrentDateString = date.getText().toString();
        user.put("time", mTime );
        user.put("email", mUserMail);
        user.put("name", mFacilityTitle);
        user.put("date", mCurrentDateString);
        user.put("status", "Pending");
        db.collection("users").document("details").collection("appointment").
                document(mUserMail).collection("facility").document(mEmail).set(user);

    }

    private void populateProfilePhoto() {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("facility_details").document("details").collection("event")
                .document(mEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if ( document.exists()) {
                        String field = document.getString("image_url");
                        Picasso.get().load(field)
                                .fit().centerCrop()   // apply scaling OR
                                .into(profile);
                    }
                }
            }
        });




    }
    private void populateTextView(final String key, final TextView textView) {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("facility_details").document("details").collection("event")
                .document(mEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if ( document.exists()) {
                        if ( key == "permissible_capacity"){
                            Long capacity = document.getLong(key);
                            String book_capacity = capacity.toString().trim();

                            textView.setText(book_capacity);

                        }else {
                            String field = document.getString(key);

                            textView.setText(field);
                        }


                    }
                }
            }
        });



    }
}