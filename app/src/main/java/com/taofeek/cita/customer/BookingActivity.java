package com.taofeek.cita.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.taofeek.cita.HomeActivity;
import com.taofeek.cita.R;

import com.taofeek.cita.time.DatePickerFragment;
import com.taofeek.cita.time.TimePickerFragment;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity implements  TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    public static final String emailItem = "facility_email";
    public String mEmail, mFacilityTitle ,mStrHrsToShow,mCurrentDateString, mUserMail,mConsumerName;
    public ImageView profile ;
    public TextView title, address, email, phone, overview, capacity, others, mTextViewTime, mTextViewDate;
    public Button bookNow, buttonDirection;
    final Calendar calendar=Calendar.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LinearLayout mBook_layout;
    private String mCompleteTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        SharedPreferences prefs_user = PreferenceManager.getDefaultSharedPreferences(BookingActivity.this);
        mUserMail = prefs_user.getString("email_id", "default_email");
        mConsumerName = prefs_user.getString("consumer_name","USER");
        mFacilityTitle = prefs_user.getString("facility_item_name", "Facility");
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
        mBook_layout = findViewById(R.id.booking_layout);
        mBook_layout.setVisibility(View.GONE);
        bookNow = findViewById(R.id.button_book);
        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBook_layout.getVisibility() == View.GONE){
                    mBook_layout.setVisibility(View.VISIBLE);
                }
                if(bookNow.getVisibility() == View.VISIBLE){
                    bookNow.setVisibility(View.GONE);
                    final NestedScrollView scrollview = ((NestedScrollView) findViewById(R.id.scrollViewBook));
                    scrollview.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }

            }
        });
        CardView date_item = findViewById(R.id.card_view_date_picker);
        date_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");

            }
        });
        CardView time_item = findViewById(R.id.card_view_time_picker);
        time_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        Button buttonConfirmation = findViewById(R.id.button_book_confirmation);
        buttonConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mTextViewDate.equals(getString(R.string.booking_date_hint))) && !(mTextViewTime.equals(getString(R.string.booking_time_hint))) ) {
                    saveUserSchedule();
                    sendAppointmentToFacilitator();

                }
                else {

                    Snackbar.make(mBook_layout,"Unable to book appointment, try selecting another date/time", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        }

    private void sendAppointmentToFacilitator() {
        Map<String, Object> user = new HashMap<>();
        user.put("time", mCompleteTime );
        user.put("email", mUserMail);
        user.put("name", mConsumerName);
        user.put("date", mCurrentDateString);
        user.put("status", "Pending");
        db.collection("facility_details").document("details").collection("appointment").
                document("facilitator").collection(mEmail).add(user)
               .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                   @Override
                   public void onSuccess(DocumentReference documentReference) {
                       Snackbar.make(mBook_layout,"You have successfully booked an appointment.", Snackbar.LENGTH_LONG).show();

                   }
               });


    }
    private void saveUserSchedule(){
        Map<String, Object> user = new HashMap<>();
        user.put("time", mCompleteTime );
        user.put("email", mUserMail);
        user.put("name", mFacilityTitle);
        user.put("date", mCurrentDateString);
        user.put("status", "Pending");
        db.collection("users").document("details").collection("appointment").
                document("user").collection(mEmail).add(user);

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





    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mTextViewTime = (TextView) findViewById(R.id.time_text);
        String am_pm = "";

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        mStrHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";
        mCompleteTime = mStrHrsToShow +":"+datetime.get(Calendar.MINUTE)+" "+am_pm;
        mTextViewTime.setText(mCompleteTime);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mCurrentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        mTextViewDate = (TextView) findViewById(R.id.date_text);
        mTextViewDate.setText(mCurrentDateString);
    }
}