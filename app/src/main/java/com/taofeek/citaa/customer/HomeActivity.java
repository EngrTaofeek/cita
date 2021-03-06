package com.taofeek.citaa.customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.taofeek.citaa.FacilityListDisplay;
import com.taofeek.citaa.LoginActivity;
import com.taofeek.citaa.R;
import com.taofeek.citaa.main.SectionsPagerAdapter;
import com.taofeek.citaa.organization.FacilityHomeActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public ImageView mImageView;
    public TextView mNameTextView;
    public TextView mEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        View hView =  navigationView.getHeaderView(0);
        mImageView = hView.findViewById(R.id.nav_image_view);
        mNameTextView = hView.findViewById(R.id.nav_header_title);
        mEmailTextView = hView.findViewById(R.id.nav_email);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_schedule:
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_edit_profile:
                        Intent editIntent = new Intent(HomeActivity.this, UserEditActivity.class);
                        startActivity(editIntent);

                        break;
                    case R.id.nav_log_out:
                        FirebaseAuth.getInstance().signOut();
                        Intent signOutIntent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(signOutIntent);
                        break;
                    case R.id.nav_gym:
                        Intent user_activity = new Intent(HomeActivity.this, FacilityListDisplay.class);
                        startActivity(user_activity);
                        break;
                    case R.id.nav_switch_to_facility:
                        Intent facility_switch = new Intent(HomeActivity.this, FacilityHomeActivity.class);
                        startActivity(facility_switch);
                        break;





                    default:
                        return true;
                }
                return true;
            }
        });


                }

    @Override
    protected void onStart() {
        retrieveNavHeader();

        super.onStart();
    }

    private void retrieveNavHeader() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        final String data = prefs.getString("email_id", "default_email");

        mEmailTextView.setText(data);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document("details").collection("profile")
                .document(data).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if ( document.exists()) {
                        String field = document.getString("image_url");
                        Picasso.get().load(field).fit().centerCrop().into(mImageView);
                    }
                    if (!(document.exists())){
                        Picasso.get().load(R.drawable.single_image)
                                .fit().centerCrop().into(mImageView);
                    }
                }
            }
        });
        db.collection("users").document("details").collection("profile")
                .document(data).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if ( document.exists()) {
                        String field = document.getString("name");
                        mNameTextView.setText(field);
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("consumer_name", field); //InputString: from the EditText
                        editor.apply();
                    }
                }
            }
        });



        // Picasso.get().load(mImageUri).into(mImageView);

    }
    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to EXIT?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


}