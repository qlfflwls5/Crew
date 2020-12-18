package com.example.crew.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.crew.R;
import com.example.crew.adapter.ApplicantsAdapter;
import com.example.crew.adapter.SearchAdapter;
import com.example.crew.adapter.SearchGroupsModel;
import com.example.crew.customClass.GroupApplicantsInfo;
import com.example.crew.customClass.GroupMembersInfo;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ApplicantsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference groupRef = db.collection("groups");

    private String group_name;
    private RecyclerView rv_applicants;
    private ApplicantsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appicants);

        Intent intent = getIntent();
        group_name = intent.getStringExtra("group_name");

        findViewById(R.id.ib_goMembers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv_applicants = findViewById(R.id.rv_applicants);
        rv_applicants.setHasFixedSize(true);
        rv_applicants.setLayoutManager(new LinearLayoutManager(this));

        //쿼리
        Query query = groupRef.document(group_name).collection("applicants");

        //리사이클러 옵션
        FirestoreRecyclerOptions<GroupApplicantsInfo> options = new FirestoreRecyclerOptions.Builder<GroupApplicantsInfo>()
                .setQuery(query, GroupApplicantsInfo.class)
                .build();

        adapter = new ApplicantsAdapter(options);

        rv_applicants.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
