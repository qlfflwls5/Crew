package com.example.crew.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crew.R;
import com.example.crew.customClass.MemberInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class memberInitActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "memberInitActivity";
    private ImageView iv_initProfile;
    private Uri imageUri;
    private int PICK_FROM_ALBUM = 10;
    private MemberInfo memberInfo;
    private String sex = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        iv_initProfile = findViewById(R.id.iv_initProfile);
        iv_initProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        findViewById(R.id.bt_enroll).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // Check if user is signed in (non-null) and update UI accordingly.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("등록 종료");
        builder.setMessage("회원정보 미등록 시 서비스 이용이 불가능합니다. 앱을 종료하시겠습니까?");
        builder.setCancelable(false);
        builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                startToast("앱을 종료합니다.");
                finish();
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


    private void updateUI(FirebaseUser currentUser) {
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.bt_enroll:
                    porfileUpdate();
                    break;
            }
        }
    };

    private void porfileUpdate() {
        final String name = ((EditText) findViewById(R.id.ed_name)).getText().toString();
        final String birthDay = ((EditText) findViewById(R.id.ed_birth)).getText().toString();
        final String phoneNumber = ((EditText) findViewById(R.id.ed_phoneNumber)).getText().toString();
        int rb = ((RadioGroup) findViewById(R.id.rg_sex)).getCheckedRadioButtonId();
        switch(rb){
            case R.id.rb_male:
                sex = "남";
                break;
            case R.id.rb_female:
                sex = "여";
                break;
        }

        if (name.length() > 0 && phoneNumber.length() > 9 && (sex != null) && birthDay.length() > 7 ) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // Access a Cloud Firestore instance from your Activity
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            if(imageUri != null) {
                FirebaseStorage.getInstance().getReference().child("userProfiles").child(user.getUid()).putFile(imageUri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                                memberInfo = new MemberInfo(name, sex, phoneNumber, birthDay, imageUrl.getResult().toString());
                            }
                        });
            }else{
                memberInfo = new MemberInfo(name, sex, phoneNumber, birthDay, Uri.parse("android.resource://"+getPackageName()+
                        "/drawable/ic_account_box_black_24dp").toString());
            }



            if(user != null) {
                db.collection("users").document(user.getUid()).set(memberInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("정보 등록을 완료했습니다.");
                                myStartActivity(MainActivity.class);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("등록을 실패하였습니다.");
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }



        }else{
            startToast("회원정보를 입력해주세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM && requestCode == RESULT_OK) {
            iv_initProfile.setImageURI(data.getData()); // 가운데 뷰를 바꿈
            imageUri = data.getData(); // 이미지 경로 원본
        }
    }
}

