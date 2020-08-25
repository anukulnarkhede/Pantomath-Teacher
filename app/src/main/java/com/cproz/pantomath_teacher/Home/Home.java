package com.cproz.pantomath_teacher.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cproz.pantomath_teacher.Notifications.NotificationsFragment;
import com.cproz.pantomath_teacher.R;
import com.cproz.pantomath_teacher.TeacherProfile.TeacherProfileFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Home extends AppCompatActivity {

    FrameLayout fragmentContainer;
    BottomNavigationView bottomNav;


    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new NotificationsFragment();
    final Fragment fragment3 = new TeacherProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        initialisation();




        bottomNav.setOnNavigationItemSelectedListener(navListener);



        fm.beginTransaction().add(R.id.fragment_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container,fragment1, "1").commit();





        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){

                case R.id.home:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    break;
                case R.id.Notification:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    break;
                case R.id.profile:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    break;


            }



            return true;
        }
    };

    public void initialisation(){
        fragmentContainer = findViewById(R.id.fragment_container);
        bottomNav = findViewById(R.id.bottomNavStdAppStart);
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavStdAppStart);
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.home != seletedItemId) {
            setHomeItem(Home.this);
        } else if (active == fragment1) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            super.onBackPressed();
        }
    }

        public static void setHomeItem(Activity activity) {
            BottomNavigationView bottomNavigationView = (BottomNavigationView)
                    activity.findViewById(R.id.bottomNavStdAppStart);
            bottomNavigationView.setSelectedItemId(R.id.home);
        }
}