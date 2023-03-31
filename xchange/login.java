package com.project.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity implements View.OnClickListener{

    myClass sck;

    TextView x_signup_txt;

    EditText xchange_email;
    EditText xchange_password;

    Button x_login_btn;

    ProgressBar pg_bar;

    FirebaseAuth mAuth;

    myClass mFunction = new myClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sck = new myClass();

//      onClick Setup
        x_signup_txt = (TextView) findViewById(R.id.xchange_sign_txt);
        x_signup_txt.setOnClickListener(this);
        x_login_btn  = (Button) findViewById(R.id.xchange_login_btn);
        x_login_btn.setOnClickListener(this);

//      Log Info Variable
        xchange_email = (EditText) findViewById(R.id.xchange_email);
        xchange_password = (EditText) findViewById(R.id.xchange_password);

//      progress Base
        pg_bar = (ProgressBar) findViewById(R.id.progress_bar);

//      Firebase
        mAuth = FirebaseAuth.getInstance();

        loginUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.xchange_sign_txt:
                startActivity(new Intent(getApplicationContext(), sign_up.class));
                break;
            case R.id.xchange_login_btn:
                if(sck.netIsAvailable()){
                    loginUser();
                }else{
                    Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(this, "Error Has Occur", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser() {
        String email = xchange_email.getText().toString().trim();
        String pass = xchange_password.getText().toString().trim();

        if(email.isEmpty()){
            xchange_email.setError("Please enter your Email");
            xchange_email.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            xchange_password.setError("Enter Your Password");
            xchange_password.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            xchange_email.setError("Please Enter a valid Email");
            xchange_email.requestFocus();
            return;
        }

        if(pass.length() < 6){
            xchange_password.setError("Password must be greater than Six.");
            xchange_password.requestFocus();
            return;
        }


        pg_bar.setVisibility(View.VISIBLE);
        x_login_btn.setVisibility(View.GONE);
        if(mFunction.netIsAvailable()){
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user.isEmailVerified()){
                                    pg_bar.setVisibility(View.GONE);
                                    x_login_btn.setVisibility(View.VISIBLE);
                                    finish();
                                    startActivity(new Intent(login.this, MainActivity.class));
                                }else{
                                    pg_bar.setVisibility(View.GONE);
                                    x_login_btn.setVisibility(View.VISIBLE);
                                    user.sendEmailVerification();
                                    Toast.makeText(login.this, "Please Check and Verify Your Email", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                pg_bar.setVisibility(View.GONE);
                                x_login_btn.setVisibility(View.VISIBLE);
                                Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            pg_bar.setVisibility(View.GONE);
            x_login_btn.setVisibility(View.VISIBLE);
        }
    }

    public void forgotPass(View view) {
        startActivity(new Intent(this, forgotPassword.class));
    }
}