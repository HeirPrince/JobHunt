package com.example.prince.jobhunt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.engine.NonSwipableViewPager;
import com.example.prince.jobhunt.fragments.Jobs;
import com.example.prince.jobhunt.fragments.Workers;
import com.example.prince.jobhunt.fragments.Profile;
import com.example.prince.jobhunt.fragments.Search;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity {

    @BindView(R.id.toolbar_home)Toolbar toolbar;
    @BindView(R.id.fab_menu)FloatingActionButton fab;
    @BindView(R.id.bnve)BottomNavigationViewEx bnv;
    @BindView(R.id.pager)NonSwipableViewPager pager;

    private FirebaseAuth auth;
    private FirebaseUser active_user;
    private FirebaseAgent agent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        active_user = auth.getCurrentUser();
        agent = new FirebaseAgent(this);

        //check authentication
        if (active_user != null){
            //authenticated
            setBottomNavigation();
            uiStuff();
        }else {
            //registration
            finish();
            startActivity(new Intent(Home.this, MainActivity.class));
        }

    }

    public void uiStuff(){
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageDrawable(getDrawable(R.drawable.job));
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Home.this, Post.class));
                            }
                        });
                        getSupportActionBar().show();
                        break;
                    case 1:
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageDrawable(getDrawable(R.drawable.vector_locate_white));
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Home.this, addWork.class));
                            }
                        });
                        getSupportActionBar().show();
                        break;
                    case 2:
                        fab.setVisibility(View.GONE);
                        getSupportActionBar().show();
                        break;
                    case 3:
                        fab.setVisibility(View.GONE);
                        getSupportActionBar().hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        //set padding
        toolbar.setPadding(0, Constants.getStatusBarHeight(this), 0, 0);

        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        //Onclick
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
	            pager.setCurrentItem(3);
            }
        });
    }

    public void setBottomNavigation(){
        //animations
        bnv.enableAnimation(true);
        bnv.enableShiftingMode(false);
        bnv.enableItemShiftingMode(true);

        SlideAdapter adapter = new SlideAdapter(getSupportFragmentManager());
        adapter.addFragment(new Jobs());
        adapter.addFragment(new Workers());
        adapter.addFragment(new Search());
        adapter.addFragment(new Profile());

        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);

        bnv.setupWithViewPager(pager);
        setupToolbar();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.out:
                AuthUI.getInstance()
                        .signOut(Home.this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(Home.this, MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Home.this, "no internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SlideAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();


        public SlideAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment frag){
            fragments.add(frag);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    public void showFloatingActionButton() {
        fab.show();
    };

    public void hideFloatingActionButton() {
        fab.hide();
    };

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }


}
