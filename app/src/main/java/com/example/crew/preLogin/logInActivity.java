package com.example.crew.preLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crew.activity.MainActivity;
import com.example.crew.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class logInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.bt_login).setOnClickListener(onClickListener);
        findViewById(R.id.bt_reset).setOnClickListener(onClickListener);
        findViewById(R.id.bt_signUp).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Check if user is signed in (non-null) and update UI accordingly.
        finish();
    }


    private void updateUI(FirebaseUser currentUser) {
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.bt_login:
                    logIn();
                    break;
                case R.id.bt_signUp:
                    myStartActivity(signUpActivity.class);
                    break;
                case R.id.bt_reset:
                    myStartActivity(pwdResetActivity.class);
                    break;

            }
        }
    };

    private void logIn() {
        String email = ((EditText) findViewById(R.id.ed_email)).getText().toString();
        final String password = ((EditText) findViewById(R.id.ed_pwd)).getText().toString();

        if (email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                startToast("로그인에 성공하였습니다.");
                                myStartActivity(MainActivity.class);
                                finish();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                startToast("아이디 및 비밀번호가 일치하지 않습니다.");
                                updateUI(null);
                                // ...
                            }

                            // ...
                        }
                    });
        }else{
            startToast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c){
        Intent intent=new Intent(this, c);
        startActivity(intent);
    }
}

