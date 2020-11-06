package com.taofeek.citaa.organization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.taofeek.citaa.R;
import com.taofeek.citaa.customer.HomeActivity;
import com.taofeek.citaa.customer.UserEditActivity;
import com.taofeek.citaa.time.DatePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, PopupMenu.OnMenuItemClickListener {
    EditText mTitle,mAddress,mCapacity,mPhone,mDescription;
    public ImageView mImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private StorageReference mStorageRef;
    private FirebaseFirestore mDb;
    private String mEmail;
    private String mCurrentDateString;
    private String mTime;
    private TextView mTextViewDate,mTextViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mTextViewTime = findViewById(R.id.textView7);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDb = FirebaseFirestore.getInstance();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(NewEvent.this);
        final String data = prefs.getString("email_id", "default_email");
        mEmail = data;

        mTitle = findViewById(R.id.editTextTitle);
        mAddress = findViewById(R.id.editTextAddress);
        mCapacity = findViewById(R.id.editTextCapacity);
        mDescription = findViewById(R.id.editTextTextDescription);
        mPhone = findViewById(R.id.editTextPhone);
        Button createEvent = findViewById(R.id.buttonCreate);
        CardView cardTime = findViewById(R.id.card_time);
        CardView cardDate = findViewById(R.id.card_date);
        mImage = findViewById(R.id.imageView3);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        cardDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        ImageView backImage = findViewById(R.id.imageView);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewEvent.this, FacilityHomeActivity.class));
                finish();
            }
        });


        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if date and time was set
                if (!(isNullOrEmpty(mTime)) && !(isNullOrEmpty(mCurrentDateString))){
                    addTextDocuments();
                    uploadFile();
                    Snackbar.make(findViewById(R.id.new_event_layout)," Successfully created a new event",
                            Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(findViewById(R.id.new_event_layout)," Kindly fill all the fields and select date and time",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addTextDocuments() {

        String address = getEditText(mAddress);
        String title = getEditText(mTitle);
        String description = getEditText(mDescription);
        String capacity = getEditText(mCapacity);
        String phone = getEditText(mPhone);
        int capacity_int = Integer.parseInt(capacity);
        int permissible_capacity_int = (int) (capacity_int * 0.5);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("title", title);
        user.put("email", mEmail);
        user.put("address", address);
        user.put("description", description);
        user.put("category","event");
        user.put("time",mTime);
        user.put("date",mCurrentDateString);
        user.put("capacity", capacity_int);
        user.put("phone", phone);
        user.put("permissible_capacity", permissible_capacity_int);




        db.collection("facility_details").document("details").collection("event")
                .document("facilitator").collection(mEmail).document(mEmail).set(user,SetOptions.merge());


        db.collection("facility_details").document("details").collection("event")
                .document(mEmail).set(user,SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                    }
                });



    }

    public String getEditText(EditText editText) {
        String text = editText.getText().toString().trim();
        return text;
    }
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri)
                    .fit().centerCrop().into(mImage);
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 500);
                            Toast.makeText(NewEvent.this, "Upload successful", Toast.LENGTH_LONG).show();


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri image_uri = uri;

                                    Map<String, Object> user = new HashMap<>();
                                    String image_url = image_uri.toString();
                                    user.put("image_url", image_url);
                                    mDb.collection("facility_details").document("details").collection("event")
                                            .document(mEmail).set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                           }
                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                           }
                                    });

                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewEvent.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            return;
        }
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mCurrentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        mTextViewDate = (TextView) findViewById(R.id.textViewDate);
        mTextViewDate.setText(mCurrentDateString);
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
}