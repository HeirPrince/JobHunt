package com.example.prince.jobhunt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.prince.jobhunt.ApplicationHelper;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.engine.LocationServer;
import com.example.prince.jobhunt.engine.NonSwipableViewPager;
import com.example.prince.jobhunt.engine.onSearchQueryListener;
import com.example.prince.jobhunt.fragments.AllJobs;
import com.example.prince.jobhunt.fragments.Notifications;
import com.example.prince.jobhunt.fragments.Profile;
import com.example.prince.jobhunt.fragments.Search;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity {

    @BindView(R.id.fab_menu)FloatingActionButton fab;
    @BindView(R.id.toolbar_home)Toolbar toolbar;
    @BindView(R.id.bnve)BottomNavigationViewEx bnv;
    @BindView(R.id.pager)NonSwipableViewPager pager;
    private SlideAdapter adapter;
    private TabLayout mTabLayout;

    private FirebaseAuth auth;
    private FirebaseUser active_user;
    private FirebaseAgent agent;
    onSearchQueryListener callback;
    private LocationServer locationServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        agent = ApplicationHelper.getFirebaseAgent();

        auth = FirebaseAuth.getInstance();
        active_user = auth.getCurrentUser();
        locationServer = new LocationServer(this);
        adapter = new SlideAdapter(getSupportFragmentManager());

        //check authentication
        if (active_user != null){
            //authenticated
            setBottomNavigation();
            uiStuff();
            locationServer.updateUserLocation();
        }else {
            //registration
            finish();
            startActivity(new Intent(Home.this, MainActivity.class));
        }

    }

    public void uiStuff() {
        setSupportActionBar(toolbar);
        TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Post.class));
            }
        });
    }

    public void moveTo(int pos){
        pager.setCurrentItem(pos);
    }

    public void setupToolbar(){
//        setSupportActionBar(toolbar);
//        //set padding
//
////        // create our manager instance after the content view is set
////        SystemBarTintManager tintManager = new SystemBarTintManager(this);
////        // enable status bar tint
////        tintManager.setStatusBarTintEnabled(true);
////        // enable navigation bar tint
////        tintManager.setNavigationBarTintEnabled(true);
//
//        //Onclick
//        toolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//	            pager.setCurrentItem(3);
//            }
//        });
    }

    public void setBottomNavigation(){
        //animations
        bnv.enableAnimation(true);
        bnv.enableShiftingMode(false);
        bnv.enableItemShiftingMode(false);

        SlideAdapter adapter = new SlideAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllJobs());
        adapter.addFragment(new Notifications());
        adapter.addFragment(new Search());
        adapter.addFragment(new Profile());

        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);

        bnv.setupWithViewPager(pager);
        setupToolbar();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.home_menu, menu);
//        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
//        android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.search).getActionView();
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconified(true);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return false;
//            }
//        });
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id){
//            case R.id.out:
//                AuthUI.getInstance()
//                        .signOut(Home.this).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        startActivity(new Intent(Home.this, MainActivity.class));
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(Home.this, "no internet connection", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                break;
//            case R.id.search:
//
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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

    //interfaces

    public void onSearch(onSearchQueryListener listener){
        callback = listener;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
