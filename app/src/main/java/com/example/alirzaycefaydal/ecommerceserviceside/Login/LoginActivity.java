package com.example.alirzaycefaydal.ecommerceserviceside.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alirzaycefaydal.ecommerceserviceside.HomeActivity;
import com.example.alirzaycefaydal.ecommerceserviceside.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //wids
    private Button login_button, register_button;
    private EditText mail_field, pass_field;
    private android.support.v7.widget.Toolbar mToolbar;
    private ProgressBar mProgress;


    //vars
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        mToolbar = findViewById(R.id.login_tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");
        mProgress=findViewById(R.id.login_progress);

        login_button = findViewById(R.id.login_button);
        register_button = findViewById(R.id.register_btn);
        mail_field = findViewById(R.id.login_mail);
        pass_field = findViewById(R.id.login_pass);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ////////////------- check if the user is stuff, then let them to write------------////////////////////////

                mProgress.setVisibility(View.VISIBLE);
                String email = mail_field.getText().toString().trim();
                String password = pass_field.getText().toString().trim();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {


                                mProgress.setVisibility(View.INVISIBLE);
                                Intent logIntent=new Intent(LoginActivity.this,HomeActivity.class);
                                logIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(logIntent);
                                finish();


                            }else{
                                mProgress.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, "Something went wrong! \n"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser current_user = mAuth.getCurrentUser();

        if (current_user != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }
}