package com.taofeek.citaa.customer;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.taofeek.citaa.LoginActivity;
import com.taofeek.citaa.R;

import java.util.HashMap;
import java.util.Map;


public class UserEditActivity extends AppCompatActivity  {

    EditText mName,mEmail,mAddress,mPhone;
    Spinner mFacilitySpinner;
    private ImageView mImageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private StorageReference   mStorageRef;
    private ProgressBar mProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UserEditActivity.this);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("profile_state", "old");
        editor.apply();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mName = findViewById(R.id.user_edit_name);
        mProgressBar = findViewById(R.id.progress_bar);
        Button saveButton = findViewById(R.id.user_save_button);
        ImageView image_icon = findViewById(R.id.imageView7);
        image_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        mImageView = findViewById(R.id.user_profile_pic);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });
        ImageView backImage = findViewById(R.id.imageView);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserEditActivity.this, HomeActivity.class));
                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextDocuments();
                uploadFile();
                startActivity(new Intent(UserEditActivity.this,HomeActivity.class));
                finish();
            }
        });
        retrieveProfilePhoto();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });
        EditText etname = findViewById(R.id.user_edit_name);
        EditText etemail = findViewById(R.id.user_edit_email);
        EditText etaddress = findViewById(R.id.user_edit_address);
        EditText etphone = findViewById(R.id.user_edit_phone);

        retrieveEditText("name",etname);
        retrieveEditText("email",etemail);
        retrieveEditText("address",etaddress);
        retrieveEditText("phone",etphone);


    }

    private void retrieveProfilePhoto() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UserEditActivity.this);
        final String data = prefs.getString("email_id", "default_email");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document("details").collection("profile")
                .document(data).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if ( document.exists()) {
                        String field = document.getString("image_url");
                        Picasso.get().load(field)
                                 .fit()//stores images for offline view
                                .centerCrop().into(mImageView);
                    }
                }
            }
        });



       // Picasso.get().load(mImageUri).into(mImageView);

    }

    private void retrieveEditText(final String key, final EditText editText) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UserEditActivity.this);
        final String data = prefs.getString("email_id", "default_email");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document("details").collection("profile")
                .document(data).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if ( document.exists()) {
                        String field = document.getString(key);
                        editText.setText(field);

                    }
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent( UserEditActivity.this,HomeActivity.class));
        finish();
    }

    public void addTextDocuments(){
        EditText etname = findViewById(R.id.user_edit_name);
        EditText etemail = findViewById(R.id.user_edit_email);
        EditText etaddress = findViewById(R.id.user_edit_address);
        EditText etphone = findViewById(R.id.user_edit_phone);
        String name = getEditText(etname);
        String email = getEditText(etemail);
        String phone = getEditText(etphone);
        String address = getEditText(etaddress);
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("address", address);
        user.put("phone", phone);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UserEditActivity.this);
        final String data = prefs.getString("email_id", "default_email");

// Add a new document with a generated ID
        db.collection("users").document("details").collection("profile").document(data)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });


    }

    public String getEditText (EditText editText){
        String text = editText.getText().toString().trim();
        return text;
    }

    
    private void openFileChooser() {
        Intent intent = new Intent( Intent.ACTION_PICK,
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
                    .fit().centerCrop().into(mImageView);
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
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(UserEditActivity.this, "Upload successful", Toast.LENGTH_LONG).show();


                             fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                 @Override
                                 public void onSuccess(Uri uri) {
                                     Uri image_url = uri;
                                     SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UserEditActivity.this);
                                     final String data = prefs.getString("email_id", "default_email");
                                     FirebaseFirestore db = FirebaseFirestore.getInstance();
                                     // Create a new user with a first and last name
                                     Map<String, Object> user = new HashMap<>();
                                     user.put("image_url", image_url.toString());
                                     db.collection("users").document("details").collection("profile")
                                             .document(data).set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                         @Override
                                         public void onSuccess(Void aVoid) {
                                         }
                                     }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                         @Override
                                         public void onSuccess(Void aVoid) {
                                             retrieveProfilePhoto();
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
                            Toast.makeText(UserEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            return;
        }
    }


}