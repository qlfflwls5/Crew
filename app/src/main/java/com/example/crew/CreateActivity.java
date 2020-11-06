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

            GroupInfo groupInfo = new GroupInfo(name, attr, info);

            db.collection("groups").document(name).set(groupInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startToast("그룹 생성을 완료했습니다.");

                            //현재 회원의 그룹에 포함하는 과정이 필요.

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

        }else{
            startToast("모든 정보를 입력해야 그룹을 생성할 수 있습니다.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void myStartActivity(Class c){
        Intent intent=new Intent(this, c);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
