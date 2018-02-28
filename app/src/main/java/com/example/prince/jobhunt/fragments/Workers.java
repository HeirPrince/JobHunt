package com.example.prince.jobhunt.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prince.jobhunt.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Workers extends Fragment {


    public Workers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_workers, container, false);

        return v;
    }

}
