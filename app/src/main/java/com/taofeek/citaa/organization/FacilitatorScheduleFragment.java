package com.taofeek.citaa.organization;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;

import com.taofeek.citaa.R;


public class FacilitatorScheduleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ScheduleAdapter adapter;


    public FacilitatorScheduleFragment() {
        // Required empty public constructor
    }


    public static FacilitatorScheduleFragment newInstance(String param1, String param2) {
        FacilitatorScheduleFragment fragment = new FacilitatorScheduleFragment();
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String data = prefs.getString("email_id", "default_email");
        String mEmail = data;

        CollectionReference detailsRef = db.collection("facility_details").document("details").collection("appointment").
                document("facilitator").collection(mEmail);


        View v = inflater.inflate(R.layout.fragment_facilitator_schedule, container, false);
        Query query = detailsRef.orderBy("email", Query.Direction.ASCENDING);


        FirestoreRecyclerOptions<ScheduleDataModel> options = new FirestoreRecyclerOptions.Builder<ScheduleDataModel>()
                .setQuery(query, ScheduleDataModel.class)
                .build();

        adapter = new ScheduleAdapter(options);
        RecyclerView recyclerView = v.findViewById(R.id.recycler_schedule);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(adapter);
        /*if (adapter.getItemCount() >= 1){
            ConstraintLayout empty = v.findViewById(R.id.empty_schedule);
            empty.setVisibility(View.GONE);


        }*/

        return v;

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}