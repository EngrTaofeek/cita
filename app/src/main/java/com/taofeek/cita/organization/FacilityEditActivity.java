package com.taofeek.cita.organization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.taofeek.cita.R;
import com.taofeek.cita.customer.UserEditActivity;

import java.util.HashMap;
import java.util.Map;

public class FacilityEditActivity extends AppCompatActivity {
    private String TAG = "testing";
    public ImageView mProfileImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private StorageReference   mStorageRef;
    private ProgressBar mProgressBar;
    private FirebaseFirestore mDb;
    private String mEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_edit);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        mProfileImage = findViewById(R.id.facility_profile_image);
        mProgressBar = findViewById(R.id.progress_bar);
      mProfileImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              openFileChooser();
          }
      });
        Button submit_button = findViewById(R.id.facility_submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextDocuments();
                uploadFile();
            }
        });

        TextInputLayout inputEmail = findViewById(R.id.text_input_email_address);
        mEmail = getEditText(inputEmail);
        mDb = FirebaseFirestore.getInstance();

    }
    public String getEditText (TextInputLayout editText){
        String text = editText.getEditText().getText().toString().trim();
        return text;
    }
    private void openFileChooser() {
        Intent intent = new Intent( Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextInputLayout inputEmail = findViewById(R.id.text_input_email_address);
        mEmail = getEditText(inputEmail);
    }

    public void addTextDocuments(){
        TextInputLayout inputName = findViewById(R.id.text_input_facility_name);
        TextInputLayout inputEmail = findViewById(R.id.text_input_email_address);
        TextInputLayout inputAddress = findViewById(R.id.text_input_address);
        TextInputLayout inputPhone = findViewById(R.id.text_input_phone);
        TextInputLayout inputOverview = findViewById(R.id.text_input_overview);
        TextInputLayout inputCapacity = findViewById(R.id.text_input_capacity);
        TextInputLayout inputOthers = findViewById(R.id.text_input_others);
        String name = getEditText(inputName);
        mEmail = getEditText(inputEmail);
        String address = getEditText(inputAddress);
        String phone = getEditText(inputPhone);
        String overview = getEditText(inputOverview);
        String capacity = getEditText(inputCapacity);
        String others = getEditText(inputOthers);
        int capacity_int = Integer.parseInt(capacity);
        int permissible_capacity = (int) (capacity_int * 0.5);
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", mEmail);
        user.put("address", address);
        user.put("phone", phone);
        user.put("others", others);
        user.put("overview", overview);
        user.put("capacity", capacity);
        user.put("permissible_capacity", permissible_capacity);
        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FacilityEditActivity.this);
        final String data = prefs.getString("email_id", "default_email");*/


// Add a new document with a generated ID
        db.collection("facility_details").document("details").collection("profile").document(mEmail)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mProfileImage);
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile() {
        if (mProfileImage != null) {
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
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(FacilityEditActivity.this, "Upload successful", Toast.LENGTH_LONG).show();


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri image_url = uri;
//                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FacilityEditActivity.this);
//                                    final String data = prefs.getString("email_id", "default_email");
                                    // Create a new user with a first and last name

                                    Map<String, Object> user = new HashMap<>();
                                    user.put("image_url", image_url.toString());
                                    mDb.collection("facility_details").document("details").collection("profile")
                                            .document(mEmail).set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + mEmail);
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });

                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FacilityEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}