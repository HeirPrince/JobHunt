package com.example.prince.jobhunt.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    public static final int GALLERY_CODE = 123;
    @BindView(R.id.username)EditText username;
    @BindView(R.id.email)EditText email;
    @BindView(R.id.skill)MaterialSpinner skill;
    @BindView(R.id.profile)
    CircleImageView profile_pic;
    @BindView(R.id.add_profile)View addImage;

    private Uri file;
    private ArrayList<String> skills;

    //Utils
    private FirebaseAgent agent;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        agent = new FirebaseAgent(this);
        authManager = new AuthManager(this);

        //checking if a user is registered
        if (authManager.checkAuth()){
            setCats();
        }

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });
    }

    //selecting image from gallery
    public void getImage(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Select image"), GALLERY_CODE);
    }

    public void setCats(){
        skills = new ArrayList<>();
        skills.add("Engineering");
        skills.add("Android Programming");
        skills.add("Software Engineer");
        skills.add("Mechanical Engineer");
        skills.add("Designer");

        skill.setItems(skills);
    }

    //button click to insert a new user
    public void insertNew(View view) {


        String u = username.getText().toString();
        String e = email.getText().toString();
        String s = skills.get(skill.getSelectedIndex());


        if (file != null){
            agent.addUser(authManager.getCurrentUser(),u, authManager.getCurrentUID(), e, s, "Available", file);
        }else {
            Toast.makeText(this, "no image loaded", Toast.LENGTH_SHORT).show();
        }
    }

    //getting activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            file = data.getData();
            //starting crop image activity
            CropImage.activity(file).start(this);
        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri cropped = result.getUri();//return cropped image
                file = cropped;
                Bitmap bmap;
                try {
                    bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cropped);
                    profile_pic.setImageBitmap(bmap);
                } catch (IOException e) {
                    Toast.makeText(this, "Image cant't be loaded", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
