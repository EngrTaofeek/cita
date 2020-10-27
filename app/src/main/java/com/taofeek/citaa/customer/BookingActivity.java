package com.taofeek.citaa.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import com.taofeek.citaa.time.DatePickerFragment;
import com.taofeek.citaa.time.TimePickerFragment;


import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity implements   DatePickerDialog.OnDateSetListener, PopupMenu.OnMenuItemClickListener {
    public static final String emailItem = "facility_email";
    private String mTime;
    public String mEmail, mFacilityTitle ,mStrHrsToShow,mCurrentDateString, mUserMail,mConsumerName;
    public ImageView profile ;
    public TextView title, address, email, phone, overview, capacity, others, mTextViewTime, mTextViewDate;
    public Button bookNow, buttonDirection;
    final Calendar calendar=Calendar.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LinearLayout mBook_layout;



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
        mTextViewTime = findViewById(R.id.time_text);
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
        populateTextView("permissible_capacity",capacity);
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
        user.put("time", mTime );
        user.put("email", mUserMail);
        user.put("name", mConsumerName);
        user.put("date", mCurrentDateString);
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
        db.collection("facility_details").document("details").collection("profile")
                .document(mEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if ( document.exists()) {
                        String field = document.getString("image_url");
                        Picasso.get().load(field).placeholder(R.drawable.image_loading) // during loading this image will be set imageview
                                .error(R.drawable.ic_baseline_error_24) //if image is failed to load - this image is set to imageview
                                .networkPolicy(NetworkPolicy.OFFLINE) //stores images for offline view
                                .fit().centerCrop()   // apply scaling OR
                                .into(profile);
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






    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.time_menu);
        popup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_8am:
                mTime = "8AM";
                mTextViewTime.setText("8AM - 10AM");
                return true;
            case R.id.item_10am:
                mTime = "10AM";
                mTextViewTime.setText("10AM - 12PM");
                return true;
            case R.id.item_12pm:
                mTime = "12PM";
                mTextViewTime.setText("12PM - 2PM");

                return true;
            case R.id.item_2pm:
                mTime = "2PM";
                mTextViewTime.setText("2PM - 4PM");
                return true;
            case R.id.item_4pm:
                mTime = "4PM";
                mTextViewTime.setText("4PM - 6PM");
                return true;
            case R.id.item_6pm:
                mTime = "6PM";
                mTextViewTime.setText("6PM - 8PM");
                return true;
            case R.id.item_8pm:
                mTime = "8PM";
                mTextViewTime.setText("8PM - 10PM");
                return true;
            case R.id.item_10pm:
                mTime = "10PM";
                mTextViewTime.setText("10PM - 12AM");
                return true;
            default:
                return false;
        }
    }
    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
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