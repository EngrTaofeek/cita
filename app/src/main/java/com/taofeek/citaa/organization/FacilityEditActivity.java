package com.taofeek.citaa.organization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.taofeek.citaa.R;
import com.taofeek.citaa.customer.UserEditActivity;

import java.util.HashMap;
import java.util.Map;

public class FacilityEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public ImageView mProfileImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private StorageReference mStorageRef;
    private FirebaseFirestore mDb;
    private String mEmail;
    private Spinner mSpinner;
    private String mLabel;
    private TextInputLayout mInputName;
    private TextInputLayout mInputEmail;
    private TextInputLayout mInputAddress;
    private TextInputLayout mInputPhone;
    private TextInputLayout mInputOverview;
    private TextInputLayout mInputCapacity;
    private TextInputLayout mInputOthers;
    private String mCategoryText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_edit);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FacilityEditActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("profile_state", "old");
        editor.apply();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        populateSpinner();
        mSpinner.setOnItemSelectedListener(this);

        mInputName = findViewById(R.id.text_input_facility_name);
        mInputEmail = findViewById(R.id.text_input_email_address);
        mInputAddress = findViewById(R.id.text_input_address);
        mInputPhone = findViewById(R.id.text_input_phone);
        mInputOverview = findViewById(R.id.text_input_overview);
        mInputCapacity = findViewById(R.id.text_input_capacity);
        mInputOthers = findViewById(R.id.text_input_others);


        mProfileImage = findViewById(R.id.facility_profile_image);

        final String data = prefs.getString("email_id", "default_email");


        mEmail = data;
        mDb = FirebaseFirestore.getInstance();
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
                startActivity(new Intent(FacilityEditActivity.this,FacilityHomeActivity.class));
                finish();
            }
        });
        retrieveProfilePhoto();

        ImageView backImage = findViewById(R.id.imageView);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FacilityEditActivity.this, FacilityHomeActivity.class));
                finish();
            }
        });



    }

    public String getEditText(TextInputLayout editText) {
        String text = editText.getEditText().getText().toString().trim();
        return text;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveEditText("name", mInputName);
        retrieveEditText("email",mInputEmail);
        retrieveEditText("address" , mInputAddress);
        retrieveEditText("capacity", mInputCapacity);
        retrieveEditText("phone", mInputPhone);
        retrieveEditText("others", mInputOthers);
        retrieveEditText("overview", mInputOverview);
        retrieveSpinner();
    }

    private void retrieveSpinner() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("facility_details").document("details").collection("profile").document(mEmail)

                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if ( document.exists()) {
                        String field = document.getString("spinner_position");
                        if ((field != null) && !(field.isEmpty()) ) {
                            int position = Integer.parseInt(field);
                            mSpinner.setSelection(position);
                        }


                    }
                }
            }
        });
    }

    public void addTextDocuments() {
        String name = getEditText(mInputName);
        mEmail = getEditText(mInputEmail);
        String address = getEditText(mInputAddress);
        String phone = getEditText(mInputPhone);
        String overview = getEditText(mInputOverview);
        String capacity = getEditText(mInputCapacity);
        String others = getEditText(mInputOthers);
        int capacity_int = Integer.parseInt(capacity);
        int permissible_capacity_int = (int) (capacity_int * 0.5);
        int spinnerPosition = mSpinner.getSelectedItemPosition();
        String spinner_position = String.valueOf(spinnerPosition);
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
//        Map<String, Object> user_int = new HashMap<>();
//        user_int.put("capacity", capacity);
//        user_int.put("permissible_capacity", permissible_capacity_int);
        user.put("capacity", capacity_int);
        user.put("permissible_capacity", permissible_capacity_int);
        user.put("spinner_position",spinner_position);
        user.put("category",mCategoryText);


        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FacilityEditActivity.this);
        final String data = prefs.getString("email_id", "default_email");*/


// Add a new document with a generated ID
        db.collection("facility_details").document("details").collection("profile").document(mEmail)
                .set(user,SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
      /* db.collection("facility_details").document("details").collection("profile").document(mEmail)
                .set(user_int)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).placeholder(R.drawable.image_loading) // during loading this image will be set imageview
                    .networkPolicy(NetworkPolicy.OFFLINE) //stores images for offline view
                    .fit().centerCrop().into(mProfileImage);
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
                            Toast.makeText(FacilityEditActivity.this, "Upload successful", Toast.LENGTH_LONG).show();


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri image_uri = uri;
//                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FacilityEditActivity.this);
//                                    final String data = prefs.getString("email_id", "default_email");
                                    // Create a new user with a first and last name

                                    Map<String, Object> user = new HashMap<>();
                                    String image_url = image_uri.toString();
                                    user.put("image_url", image_url);
                                    mDb.collection("facility_details").document("details").collection("profile")
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
                            Toast.makeText(FacilityEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        } else {
            return;
        }
    }
    private void retrieveEditText(final String key, final TextInputLayout textInputLayout) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FacilityEditActivity.this);
        final String data = prefs.getString("email_id", "default_email");
        EditText editText = textInputLayout.getEditText();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("facility_details").document("details").collection("profile").document(mEmail)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if ( document.exists()) {
                        if (key == "capacity"){
                            Double field = document.getDouble(key);
                            int capacity = field.intValue();
                            String text = String.valueOf(capacity);
                            editText.setText(text);
                            return;
                        }
                        String field = document.getString(key);
                        editText.setText(field);

                    }
                }
            }
        });



    }

    private void retrieveProfilePhoto() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FacilityEditActivity.this);
        final String data = prefs.getString("email_id", "default_email");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("facility_details").document("details").collection("profile").document(mEmail)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if ( document.exists()) {
                        String field = document.getString("image_url");
                        Picasso.get().load(field) // during loading this image will be set imageview
                               .fit().centerCrop()   // apply scaling OR
                                .into(mProfileImage);
                    }
                    if (!(document.exists())){
                        Picasso.get().load(R.drawable.facilitator_image)
                                .fit().centerCrop().into(mProfileImage);
                    }
                }
            }
        });



        // Picasso.get().load(mImageUri).into(mImageView);

    }

    private void populateSpinner() {
        mSpinner = (Spinner) findViewById(R.id.spinner_category);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
    }


    public String getSpinnerItem(String text){
        String spinnerText = text;
        return text;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mLabel = parent.getItemAtPosition(position).toString();
        mCategoryText = parent.getSelectedItem().toString();


        getSpinnerItem(mLabel);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}