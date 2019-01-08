package com.example.alirzaycefaydal.ecommerceserviceside.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alirzaycefaydal.ecommerceserviceside.HomeActivity;
import com.example.alirzaycefaydal.ecommerceserviceside.Models.User;
import com.example.alirzaycefaydal.ecommerceserviceside.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegisterActivity extends AppCompatActivity {

    //wids
    private EditText name_field, mail_field, pass_field,phone_field;
    private Button register_button, login_button;
    private Toolbar mToolbar;
    private ProgressBar mProgress;

    //vars
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Users");

        mToolbar=findViewById(R.id.reg_tool_bar);
//        setSupportActionBar(mToolbar);
  //      getSupportActionBar().setTitle("Register");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgress=findViewById(R.id.reg_progress);


        name_field=findViewById(R.id.reg_name);
        mail_field=findViewById(R.id.reg_mail);
        phone_field=findViewById(R.id.reg_phone);
        pass_field=findViewById(R.id.reg_pass);
        register_button=findViewById(R.id.reg_button);
        login_button=findViewById(R.id.reg_login);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setVisibility(View.VISIBLE);
                final String name=name_field.getText().toString();
                String mail=mail_field.getText().toString().trim();
                String pass=pass_field.getText().toString().trim();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass)){

                    mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                mProgress.setVisibility(View.INVISIBLE);

                                String user_id=mAuth.getCurrentUser().getUid();

                                String device_token= FirebaseInstanceId.getInstance().getToken();

                                User user=new User();
                                user.setName(name);
                                user.setUser_id(user_id);
                                user.setImage("default");
                                user.setIsStuff("true");
                                user.setToken_id(device_token);
                                user.setPhone(phone_field.getText().toString().trim());

                                mDatabase.child(user_id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){

                                            Intent regIntent=new Intent(RegisterActivity.this,LoginActivity.class);
                                            regIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(regIntent);
                                            finish();
                                        }
                                    }
                                });


                            }else{
                                mProgress.setVisibility(View.INVISIBLE);
                                Toast.makeText(RegisterActivity.this, "Something went wrong!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        if (firebaseUser != null){
            startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
            finish();
        }
    }
}
