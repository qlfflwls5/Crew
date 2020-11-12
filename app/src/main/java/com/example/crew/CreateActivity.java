package com.example.crew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CreateActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static String TAG = "CreateActivity";
    final private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.bt_groupCreate).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        finish();
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.bt_groupCreate:
                    groupCreate();
                    break;
            }
        }
    };

    private void groupCreate() {
        final String name = ((EditText) findViewById(R.id.ed_groupName)).getText().toString();
        String attr = ((EditText) findViewById(R.id.ed_groupAttr)).getText().toString();
        String info = ((EditText) findViewById(R.id.ed_groupInfo)).getText().toString();


        if (name.length() > 0 && attr.length() > 0 && info.length() > 0) {

            final GroupInfo groupInfo = new GroupInfo(name, attr, info);
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            //그룹이 이미 존재하는지 확인
            DocumentReference existCheckRef = db.collection("groups").document(name);
            existCheckRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            startToast("해당 그룹 이름이 이미 존재합니다.");
                        } else {

                            //그룹 추가
                            db.collection("groups").document(name).set(groupInfo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            //그룹을 생성하는 사람이 그룹의 마스터로 설정 됨
                                            DocumentReference docRef = db.collection("users").document(user.getUid());
                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            String creatorName = (String) document.get("name");
                                                            GroupMembersInfo groupMembersInfo = new GroupMembersInfo(creatorName, "Master");
                                                            db.collection("groups")
                                                                    .document(name)
                                                                    .collection("members")
                                                                    .document(user.getUid())
                                                                    .set(groupMembersInfo);
                                                            db.collection("users")
                                                                    .document(user.getUid())
                                                                    .collection("myGroups")
                                                                    .document(name)
                                                                    .set(groupInfo);
                                                            startToast("그룹 생성을 완료했습니다.");
                                                        } else {
                                                            Log.d(TAG, "No such document");
                                                        }
                                                    } else {
                                                        Log.d(TAG, "get failed with ", task.getException());
                                                    }
                                                }
                                            });

                                            myStartActivity(MainActivity.class);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            startToast("그룹 생성을 실패하였습니다.");
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                }
            });

        }else{
            startToast("모든 정보를 입력한 후 생성할 수 있습니다.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void myStartActivity(Class c){
        Intent intent=new Intent(this, c);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        finish();
    }
}
