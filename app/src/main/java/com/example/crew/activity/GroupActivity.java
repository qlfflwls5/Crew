package com.example.crew.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.crew.R;
import com.example.crew.fragment.fragNotice;
import com.example.crew.fragment.fragCalender;
import com.example.crew.fragment.fragMembers;
import com.example.crew.fragment.fragChat;
import com.example.crew.fragment.fragArchive;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class GroupActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String group_name;
    private String group_info;

    private BottomNavigationView bottomNavigationView;

    private Bundle bundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        if(intent!=null){
            group_name = intent.getStringExtra("group_name");
            startToast(group_name + "에 접속하셨습니다.");
        }
        bundle.putString("group_name", group_name);
        Fragment fragment = null;
        fragment = new fragNotice();
        fragment.setArguments(bundle);
        loadFragment(fragment);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.action_notice:
                bundle.putString("group_name", group_name);
                fragment = new fragNotice();
                fragment.setArguments(bundle);
                break;

            case R.id.action_calender:
                bundle.putString("group_name", group_name);
                fragment = new fragCalender();
                fragment.setArguments(bundle);
                break;

            case R.id.action_group:
                bundle.putString("group_name", group_name);
                fragment = new fragMembers();
                fragment.setArguments(bundle);
                break;

            case R.id.action_chat:
                bundle.putString("group_name", group_name);
                fragment = new fragChat();
                fragment.setArguments(bundle);
                break;

            case R.id.action_achieve:
                bundle.putString("group_name", group_name);
                fragment = new fragArchive();
                fragment.setArguments(bundle);
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
