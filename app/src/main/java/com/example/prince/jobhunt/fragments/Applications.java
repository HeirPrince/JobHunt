package com.example.prince.jobhunt.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Application;
import com.example.prince.jobhunt.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Applications extends Fragment {

    private RecyclerView application_list;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;
    private FirebaseAgent agent;
    private FirestoreRecyclerAdapter adapter;


    public Applications() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_workers, container, false);
        application_list = v.findViewById(R.id.app_list);
        progressBar =v.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        agent = new FirebaseAgent(getContext());

        firestore = FirebaseFirestore.getInstance();

        adapter = feedApplications();

        return v;
    }

    @Override
    public boolean getUserVisibleHint() {
        if (isVisible()){
            setupViews();
        }
        return super.getUserVisibleHint();
    }

    public void setupViews(){
        adapter.startListening();
        application_list.setLayoutManager(new LinearLayoutManager(getContext()));
        application_list.setHasFixedSize(true);
        application_list.setAdapter(adapter);
    }
    public FirestoreRecyclerAdapter feedApplications(){
        Query query = firestore.collection("application");

        FirestoreRecyclerOptions<Application> result = new FirestoreRecyclerOptions.Builder<Application>()
                .setQuery(query, Application.class)
                .build();

        return new FirestoreRecyclerAdapter<Application, ApplicationHolder>(result) {
            @Override
            public ApplicationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_application_item, parent, false);
                return new ApplicationHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ApplicationHolder holder, int position, @NonNull final Application model) {
                Toast.makeText(getContext(), model.getSalary(), Toast.LENGTH_SHORT).show();
                application_list.setVisibility(View.GONE);
                agent.getUserByUID(model.getUid(), new FirebaseAgent.getUser() {
                    @Override
                    public void gottenUser(User user) {
                        holder.user_name.setText(user.getUsername());
                        holder.user_career.setText(user.getCareer());
                        holder.app_desc.setText(model.getDesc());
                        agent.downloadImage(model.getUid(), Constants.USER_PROFILE_IMAGE_PATH, new FirebaseAgent.OnImageDownload() {
                            @Override
                            public void isDownloaded(Boolean status, String url) {
                                if (url != null && status){
                                    Glide.with(getContext())
                                            .load(url)
                                            .into(holder.user_profile);
                                }
                            }

                            @Override
                            public void isFailed(Boolean status) {

                            }
                        });
                    }
                });
            }
        };


    }

    public class ApplicationHolder extends RecyclerView.ViewHolder {

        public TextView user_name, user_career, app_desc;
        public CircleImageView user_profile;

        public ApplicationHolder(View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            user_career = itemView.findViewById(R.id.user_career);
            app_desc = itemView.findViewById(R.id.desc);
            user_profile = itemView.findViewById(R.id.user_photo);
        }
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
