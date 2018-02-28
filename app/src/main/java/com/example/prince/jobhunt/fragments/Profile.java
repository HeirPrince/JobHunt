package com.example.prince.jobhunt.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.activities.edit_Profile;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.User;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    private TextView profile_name, profile_email, profile_desc;
    private CircleImageView profile_photo;
    private AppBarLayout appbar;
    private View edit_p;

    private FirebaseAgent agent;
    private FirebaseAuth auth;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_profile, container, false);
        agent = new FirebaseAgent(getContext());
        auth = FirebaseAuth.getInstance();


        profile_name = v.findViewById(R.id.profile_name);
        profile_photo = v.findViewById(R.id.profile_photo);
        profile_desc = v.findViewById(R.id.profile_desc);
        profile_email = v.findViewById(R.id.profile_email);
        edit_p = v.findViewById(R.id.edit_profile);
        appbar = v.findViewById(R.id.appbar);

        edit_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), edit_Profile.class));
            }
        });

        //methods
        popAppBar();

        return v;
    }

    public void popAppBar(){
        //profile info
        int size = Constants.getStatusBarHeight(getContext());
        appbar.setPadding(0, size, 0 ,0);
        agent.getUserByUID(auth.getCurrentUser().getUid(), new FirebaseAgent.getUser() {
            @Override
            public void gottenUser(User user) {
                profile_name.setText(user.getUsername());
                profile_desc.setText(user.getAbout());
                profile_email.setText(user.getEmail());
                String path = Constants.USER_PROFILE_IMAGE_PATH;
                agent.downloadImage(auth.getCurrentUser().getUid(), path, new FirebaseAgent.OnImageDownload() {
                    @Override
                    public void isDownloaded(Boolean status, String url) {
                        if (status && url != null) {
                            Glide.with(getActivity())
                                    .load(url)
                                    .into(profile_photo);
                        }
                    }

                    @Override
                    public void isFailed(Boolean status) {
                        Toast.makeText(getContext(), "image can't be downloaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
