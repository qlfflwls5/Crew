package com.example.crew.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.crew.customClass.GroupInfo;
import com.example.crew.R;
import com.example.crew.customClass.GroupMembersInfo;
import com.example.crew.customClass.Notice;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static String TAG = "CreateActivity";
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();
    private ImageView iv_initGroupProfile;
    private Uri downloadUrl;
    private Uri imageUri;
    final private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mAuth = FirebaseAuth.getInstance();

        iv_initGroupProfile = findViewById(R.id.iv_initGroupProfile);
        iv_initGroupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

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

            final GroupInfo groupInfo = new GroupInfo(name, attr, info, downloadUrl.toString());
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
                                                            String profileUrl = (String) document.get("profileImageUrl");
                                                            GroupMembersInfo groupMembersInfo = new GroupMembersInfo(creatorName, "마스터", 1, profileUrl);
                                                            db.collection("groups")
                                                                    .document(name)
                                                                    .collection("members")
                                                                    .document(user.getUid())
                                                                    .set(groupMembersInfo);
                                                            long now = System.currentTimeMillis();
                                                            Date mdate = new Date(now);
                                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
                                                            String getTime = simpleDateFormat.format(mdate);
                                                            Notice notice = new Notice("환영합니다", "이 곳을 통해 Bubble을 관리하고 일정을 정리해 보세요.", getTime, 0);
                                                            db.collection("groups")
                                                                    .document(name)
                                                                    .collection("notice")
                                                                    .document(getTime)
                                                                    .set(notice);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            iv_initGroupProfile.setImageURI(data.getData()); // 가운데 뷰를 바꿈
            imageUri = data.getData(); // 이미지 경로 원본
            uploadPicture();
        }
    }

    private void uploadPicture() {

        final StorageReference Ref = storageReference.child("groupProfiles/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        UploadTask uploadTask = Ref.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return Ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUrl = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
}
