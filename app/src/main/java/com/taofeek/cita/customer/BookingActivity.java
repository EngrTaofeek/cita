package com.taofeek.cita.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.taofeek.cita.HomeActivity;
import com.taofeek.cita.R;
import com.taofeek.cita.time.DatePickerFragment;

import com.taofeek.cita.time.TimePickerFragment;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    public static final String emailItem = "facility_email";
    public String mEmail;
    public ImageView profile ;
    public TextView title, address, email, phone, overview, capacity, others;
    public Button bookNow, buttonDirection;
    final Calendar calendar=Calendar.getInstance();

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
        LinearLayout book_layout = findViewById(R.id.booking_layout);
        book_layout.setVisibility(View.GONE);
        bookNow = findViewById(R.id.button_book);
        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(book_layout.getVisibility() == View.GONE){
                    book_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        CardView date_item = findViewById(R.id.card_view_date_picker);
        date_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView textView = findViewById(R.id.date_text);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd");
        textView.setText(simpleDateFormat.format(calendar.getTime()));


    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = (TextView) findViewById(R.id.time_text);
        String am_pm = "";

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";
        textView.setText(strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm );
    }
}