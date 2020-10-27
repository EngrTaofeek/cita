package com.taofeek.citaa.customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taofeek.citaa.EventList;
import com.taofeek.citaa.FacilityListDisplay;
import com.taofeek.citaa.R;


public class UserGuidelineFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public String category;
    public static final String CATEGORY = "com.taofeek.cita.CATEGORY";

    private CardView mEvents,mGyms,mSport,mSchools,mHealth,mOffice;

    public UserGuidelineFragment() {
        // Required empty public constructor
    }

    public static UserGuidelineFragment newInstance(String param1, String param2) {
        UserGuidelineFragment fragment = new UserGuidelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_guideline, container, false);
        mEvents = v.findViewById(R.id.events_card);
        mEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EventList.class);
                startActivity(intent);
            }
        });
        /*item>Gyms</item>
        <item>Offices</item>
        <item>Health</item>
        <item>School</item>
        <item>Sport facility and gyms</item>*/
        mGyms = v.findViewById(R.id.gyms_card);
        mSport = v.findViewById(R.id.sports_card);
        mHealth = v.findViewById(R.id.health_card);
        mOffice = v.findViewById(R.id.business_card);
        mSchools = v.findViewById(R.id.schools_card);
        mGyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCategory("Gyms");
            }
        });
        mOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCategory("Offices");
            }
        });
        mHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCategory("Health");
            }
        });
        mSchools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCategory("School");
            }
        });
        mSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCategory("Sport facility and gyms");
            }
        });
        return v;
    }

    public void launchCategory(String category){
        Intent intent = new Intent(getContext(), FacilityListDisplay.class);
        intent.putExtra(CATEGORY, category);
        startActivity(intent);

    }
}