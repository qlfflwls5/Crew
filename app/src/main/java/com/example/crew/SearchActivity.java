package com.example.crew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference searchgroupsrRef = db.collection("groups");

    private RecyclerView rv_search;
    private EditText et_search;

    private SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        rv_search = findViewById(R.id.rv_search);
        rv_search.setHasFixedSize(true);
        rv_search.setLayoutManager(new LinearLayoutManager(this));

        //쿼리
        Query query = searchgroupsrRef.orderBy("name").startAt("").endAt("" + "\uf8ff");

        //리사이클러 옵션
        FirestoreRecyclerOptions<SearchGroupsModel> options = new FirestoreRecyclerOptions.Builder<SearchGroupsModel>()
                .setQuery(query, SearchGroupsModel.class)
                .build();

        adapter = new SearchAdapter(options);

        rv_search.setAdapter(adapter);

        et_search = findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //쿼리
                Query query = searchgroupsrRef
                        .orderBy("name").startAt(s.toString()).endAt(s.toString() + "\uf8ff");
                //리사이클러 옵션

                FirestoreRecyclerOptions<SearchGroupsModel> options = new FirestoreRecyclerOptions.Builder<SearchGroupsModel>()
                        .setQuery(query, SearchGroupsModel.class)
                        .build();

                adapter.updateOptions(options);
            }
        });

        //리사이클러뷰 클릭 이벤트를 어댑터내 메서드와 연동
        adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                myStartActivity(GroupActivity.class);
            }
        }) ;
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        finish();
    }

    private void myStartActivity(Class c){
        Intent intent=new Intent(this, c);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //finish();
    }
}
