package com.taofeek.cita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.taofeek.cita.organization.FacilityHomeActivity;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;


    // widgets
    private EditText mEmail, mPassword;
    private ProgressBar mProgressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private CheckBox mCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mEmail = (EditText) findViewById(R.id.email_input);
        mPassword = (EditText) findViewById(R.id.password_input);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        mCheckBox = findViewById(R.id.login_check_box);

        Button signIn = (Button) findViewById(R.id.button);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                //check if the fields are filled out
                if (!isEmpty(mEmail.getText().toString())
                        && !isEmpty(mPassword.getText().toString())) {
                    Log.d(TAG, "onClick: attempting to authenticate.");
                    hideSoftKeyboard();

                    showDialog();
                    signInMethod(email, password);
                }
                else{
                    hideDialog();
                    Toast.makeText(LoginActivity.this, "Fill both email and password fields",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView register = (TextView) findViewById(R.id.link_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        /*TextView resendVerification = findViewById(R.id.resend_verification_email);
        resendVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationEmail();

            }
        });*/

        TextView testing = findViewById(R.id.forgotPassword);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        preGoogleSignInSetUp();


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
       // updateUI(account);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String email = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("email_id", email); //InputString: from the EditText
                editor.apply();
            }


            Intent user_intent = new Intent(LoginActivity.this, HomeActivity.class);
            Intent facility_intent = new Intent(LoginActivity.this, FacilityHomeActivity.class);
            if (mCheckBox.isChecked()){
                hideDialog();
                startActivity(facility_intent);
                finish();
            }
            else {
                hideDialog();
                startActivity(user_intent);
                finish();

            }


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }
    public void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Sent Verification Email", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "couldn't send email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    public void signInMethod (final String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("email_id", email); //InputString: from the EditText
                            editor.apply();
                            hideDialog();
                            Intent user_intent = new Intent(LoginActivity.this, HomeActivity.class);
                            Intent facility_intent = new Intent(LoginActivity.this, FacilityHomeActivity.class);
                            if (mCheckBox.isChecked()){
                                startActivity(facility_intent);
                                finish();
                            }
                            else {
                                startActivity(user_intent);
                                finish();

                            }
                                 }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private boolean isEmpty(String string){
        return string.equals("");
    }


    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }
    public void preGoogleSignInSetUp(){
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Set the dimensions of the sign-in button.

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
