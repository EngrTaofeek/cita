package com.taofeek.cita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import static android.text.TextUtils.isEmpty;

public class RecoverPassword extends AppCompatActivity {
    EditText mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        mEmail = findViewById(R.id.email_input);
        Button recover = findViewById(R.id.button);
        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty(mEmail.getText().toString())){
                    forgotPassword();
                }
                else {
                    Snackbar.make(findViewById(R.id.recover_layout),"Enter a registered email address and try again",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
    private void forgotPassword(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = mEmail.getText().toString().trim();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Snackbar.make(findViewById(R.id.recover_layout),"Check your email to create a new password",
                                    Snackbar.LENGTH_LONG).show();
                        }
                        else {
                            Snackbar.make(findViewById(R.id.recover_layout),"Enter a registered email address and try again",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }
}