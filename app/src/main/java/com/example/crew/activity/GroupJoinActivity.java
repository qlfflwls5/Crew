package com.example.crew.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crew.R;
import com.example.crew.customClass.GroupApplicantsInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GroupJoinActivity extends AppCompatActivity {
    private String TAG = "GroupJoinActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //문서들 필드값 담은 변수
    private String group_name;
    private String group_info;
    private String applicantname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_join);

        final TextView tv_name = findViewById(R.id.tv_name);
        final TextView tv_info = findViewById(R.id.tv_info);
        findViewById(R.id.bt_join).setOnClickListener(onClickListener);


        Intent intent = getIntent();
        if(intent!=null){
            group_name = intent.getStringExtra("group_name");
        }

        DocumentReference docRef = db.collection("groups").document(group_name);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    group_info = documentSnapshot.getString("info");

                    tv_name.setText(group_name);
                    tv_info.setText(group_info);
                }
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.bt_join:

                    //가입 신청 중복 확인
                    db.collection("groups").document(group_name)
                            .collection("applicants").document(user.getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    startToast("이미 가입 신청한 그룹입니다.");
                                } else {
                                    joinGroup();
                                }
                            }
                        }
                    });
                    break;
            }
        }
    };
    private void joinGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("가입 신청");
        builder.setMessage(group_name + "에 가입을 신청하시겠습니까?");
        builder.setCancelable(false);
        builder.setPositiveButton("신청", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                //현재 사용자 계정의 name 필드 값을 읽고, 그룹의 applicants 에 추가하기
                DocumentReference userRef = db.collection("users").document(user.getUid());
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                applicantname = document.getString("name");
                                GroupApplicantsInfo groupApplicantsInfo = new GroupApplicantsInfo(applicantname);
                                db.collection("groups").document(group_name)
                                        .collection("applicants").document(user.getUid())
                                        .set(groupApplicantsInfo);
                                startToast("가입 신청을 보냈습니다.");
                            }
                        }
                    }
                });
                dialog.cancel();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
