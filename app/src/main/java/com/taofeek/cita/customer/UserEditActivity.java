package com.taofeek.cita.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taofeek.cita.ChangePhotoDialog;
import com.taofeek.cita.R;

import java.util.HashMap;
import java.util.Map;


public class UserEditActivity extends AppCompatActivity implements
        ChangePhotoDialog.OnPhotoReceivedListener {

    private String TAG = "testing";
    EditText mName,mEmail,mAddress,mPhone;
    Spinner mFacilitySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Button saveButton = findViewById(R.id.user_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextDocuments();
            }
        });


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

// Add a new document with a generated ID
        db.collection("users").document("details").collection("profile")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }
    public String getEditText (EditText editText){
        String text = editText.getText().toString().trim();
        return text;
    }

    @Override
    public void getImagePath(Uri imagePath) {

    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {

    }
}