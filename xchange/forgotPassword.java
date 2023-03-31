package com.project.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {

    EditText xchange_email;

    Button xchange_reset_btn;

    FirebaseAuth mAuth;

    ProgressBar pg_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        xchange_email = (EditText) findViewById(R.id.xchange_email);

        mAuth = FirebaseAuth.getInstance();

        pg_bar = (ProgressBar) findViewById(R.id.progress_bar);

        xchange_reset_btn = (Button) findViewById(R.id.xchange_reset_btn);
        xchange_reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPass();
            }
        });
    }

    public void resetPass(){
        String email = xchange_email.getText().toString().trim();

        if(email.isEmpty()){
            xchange_email.setError("Enter your Email");
            xchange_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            xchange_email.setError("Please provide a valid Email Address");
            xchange_email.requestFocus();
            return;
        }

        pg_bar.setVisibility(View.VISIBLE);
        xchange_reset_btn.setVisibility(View.GONE);

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    pg_bar.setVisibility(View.GONE);
                    xchange_reset_btn.setVisibility(View.VISIBLE);
                    Toast.makeText(forgotPassword.this, "Check Your Email to Reset you Password", Toast.LENGTH_SHORT).show();
                }else{
                    pg_bar.setVisibility(View.GONE);
                    xchange_reset_btn.setVisibility(View.VISIBLE);
                    Toast.makeText(forgotPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}