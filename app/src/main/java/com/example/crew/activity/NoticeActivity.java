package com.example.crew.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.crew.R;
import com.example.crew.customClass.GroupApplicantsInfo;
import com.example.crew.customClass.Notice;
import com.example.crew.fragment.fragNotice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoticeActivity extends AppCompatActivity {

    private String group_name;

    final private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        Intent intent = getIntent();
        group_name = intent.getStringExtra("group_name");

        ImageButton ib_goNotice = findViewById(R.id.ib_goNotice);
        ib_goNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final EditText ed_subject = findViewById(R.id.ed_subject);
        final EditText ed_content = findViewById(R.id.ed_content);

        findViewById(R.id.bt_noticeSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(NoticeActivity.this);
                builder.setTitle("공지 등록");
                builder.setMessage("새 공지를 등록하시겠습니까?");
                builder.setCancelable(false);
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        long now = System.currentTimeMillis();
                        Date mdate = new Date(now);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
                        String getTime = simpleDateFormat.format(mdate);

                        Notice notice = new Notice(ed_subject.getText().toString(), ed_content.getText().toString(), getTime, 0);
                        db.collection("groups")
                                .document(group_name)
                                .collection("notice")
                                .document(getTime)
                                .set(notice);

                        dialog.cancel();
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
        });
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
