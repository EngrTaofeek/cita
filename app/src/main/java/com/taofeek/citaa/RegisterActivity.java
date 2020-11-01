package com.taofeek.citaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taofeek.citaa.customer.HomeActivity;
import com.taofeek.citaa.organization.FacilityHomeActivity;


public class RegisterActivity extends AppCompatActivity {

    //widgets
    private EditText mEmail, mPassword, mConfirmPassword, validateEmail;
    private Button mRegister;
    private LottieAnimationView mProgressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private CheckBox mCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mEmail = (EditText) findViewById(R.id.email_input);
        mPassword = (EditText) findViewById(R.id.password_input);
        mConfirmPassword = (EditText) findViewById(R.id.confirm_password_input);
        mRegister = (Button) findViewById(R.id.button);
        mProgressBar = (LottieAnimationView) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        mCheckBox = findViewById(R.id.checkbox);
        hideDialog();

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Password = mPassword.getText().toString().trim();

                // Password Authentication
                if (TextUtils.isEmpty(Password)){
                    mPassword.setError("Password is required!");
                    return;
                }
                if (Password.length() <6){
                    mPassword.setError("Passwords must be greater than 6 characters!");
                    return;
                }


                //check for null valued EditText fields
                if(!isEmpty(mEmail.getText().toString())
                        && !isEmpty(mPassword.getText().toString())
                        && !isEmpty(mConfirmPassword.getText().toString())){


                        //check if passwords match
                        if(doStringsMatch(mPassword.getText().toString(), mConfirmPassword.getText().toString())){

                            //Initiate registration task
                            registerNewEmail(mEmail.getText().toString(), mPassword.getText().toString());
                        }else{
                            Toast.makeText(RegisterActivity.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                        }
                    }

                else{
                    Toast.makeText(RegisterActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hideSoftKeyboard();
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        preGoogleSignInSetUp();

    }

    public void registerNewEmail(final String email, String password){

        showDialog();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            //send email verificaiton
                            sendVerificationEmail();

                            FirebaseAuth.getInstance().signOut();

                            //redirect the user to the login screen
                            redirectLoginScreen();
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Unable to Register",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideDialog();

                        // ...
                    }
                });
    }

    /**
     * sends an email verification link to the user
     */
    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Sent Verification Email", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Couldn't Verification Send Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }


    /**
     * Redirects the user to the login screen
     */
    private void redirectLoginScreen(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(RegisterActivity.this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String email = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                FirebaseUser user = mAuth.getCurrentUser();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("email_id", email); //InputString: from the EditText
                editor.apply();

                Intent user_intent = new Intent(RegisterActivity.this, HomeActivity.class);
                Intent facility_intent = new Intent(RegisterActivity.this, FacilityHomeActivity.class);
                if (mCheckBox.isChecked()){
                    hideDialog();
                    editor.putString("login_user_state","facilitator");
                    editor.apply();
                    startActivity(facility_intent);
                    finish();
                }
                else {
                    editor.putString("login_user_state","consumer");
                    editor.apply();
                    hideDialog();
                    startActivity(user_intent);
                    finish();

                }
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private boolean doStringsMatch(String s1, String s2){
        return s1.equals(s2);
    }

    private boolean isEmpty(String string){
        return string.equals("");
    }


    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    // Hide and show Password

    public void showHiddenPassword(View view){
        if (view.getId()==R.id.password_icon){

            if (mPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.hide_password);
                // show Password
                mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else {
                ((ImageView)(view)).setImageResource(R.drawable.ic_show_password);
                // Hide Password
                mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    public void showPassword(View view){
        if (view.getId()==R.id.password_image){

            if (mConfirmPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.hide_password);
                // show Password
                mConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else {
                ((ImageView)(view)).setImageResource(R.drawable.ic_show_password);
                // Hide Password
                mConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }



}


