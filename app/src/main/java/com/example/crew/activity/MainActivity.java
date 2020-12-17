package com.example.crew.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.crew.R;
import com.example.crew.adapter.SearchGroupsModel;
import com.example.crew.adapter.MainAdapter;
import com.example.crew.preLogin.logInActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity>";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference myGroupsrRef = db.collection("users");

    private RecyclerView rv_main;
    private boolean switchOn = false;

    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            myStartActivity(logInActivity.class);
            finish();
        }else{
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document != null){
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                                myStartActivity(memberInitActivity.class);
                                finish();
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        if(user == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{

            // 로그인과 비로그인 구분, 회원정보 등록과 미등록 구분까지의 프로세스

            findViewById(R.id.ibt_search).setOnClickListener(onClickListener);
            findViewById(R.id.bt_create).setOnClickListener(onClickListener);
            findViewById(R.id.ibt_user).setOnClickListener(onClickListener);

            rv_main = findViewById(R.id.rv_main);
            rv_main.setHasFixedSize(true);
            rv_main.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            //쿼리
            Query query = myGroupsrRef.document(user.getUid()).collection("myGroups");

            //리사이클러 옵션
            FirestoreRecyclerOptions<SearchGroupsModel> options = new FirestoreRecyclerOptions.Builder<SearchGroupsModel>()
                    .setQuery(query, SearchGroupsModel.class)
                    .build();

            adapter = new MainAdapter(options);

            rv_main.setAdapter(adapter);

            //리사이클러뷰 클릭 이벤트를 어댑터내 메서드와 연동
            adapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position, String name) {
                    Intent intent = new Intent(getApplicationContext(), GroupActivity.class);

                    intent.putExtra("group_name", name);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }) ;
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ibt_search:
                    myStartActivity(SearchActivity.class);
                    break;
                case R.id.bt_create:
                    myStartActivity(CreateActivity.class);
                    break;
                case R.id.ibt_user:
                    myStartActivity(MyPageActivity.class);
                    break;
            }
        }
    };


    private void myStartActivity(Class c){
        Intent intent=new Intent(this, c);
        startActivity(intent);
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
