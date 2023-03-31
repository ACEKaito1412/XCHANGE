package com.project.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.project.xchange.model.Balance;
import com.project.xchange.model.users_information;

public class sign_up extends AppCompatActivity implements View.OnClickListener{
    
    myClass sck = new myClass();

    TextView x_privacy;
    TextView x_terms;
    TextView x_login_txt;

    EditText xchange_fname;
    EditText xchange_lname;
    EditText xchange_email;
    EditText xchange_password;

    CheckBox terms_and_privacy;

    Button xchange_sign_btn;

    ProgressBar pg_bar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//      CheckBox
        terms_and_privacy = (CheckBox) findViewById(R.id.checkbox_terms);

//      onclicks SETUPs
        x_login_txt = (TextView) findViewById(R.id.xchange_login_txt);
        x_login_txt.setOnClickListener(this);

        x_terms = (TextView) findViewById(R.id.xchange_terms);
        x_terms.setOnClickListener(this);

        x_privacy = (TextView) findViewById(R.id.xchange_privacy);
        x_privacy.setOnClickListener(this);

        xchange_sign_btn = (Button) findViewById(R.id.xchange_sign_btn);
        xchange_sign_btn.setOnClickListener(this);

//      INFOS VARIABLES
        xchange_fname = (EditText) findViewById(R.id.xchange_fname);
        xchange_lname = (EditText) findViewById(R.id.xchange_lname);
        xchange_email = (EditText) findViewById(R.id.xchange_email);
        xchange_password = (EditText) findViewById(R.id.xchange_password);

//      progress Base
        pg_bar = (ProgressBar) findViewById(R.id.progress_bar);

//      firebase Auth
        mAuth = FirebaseAuth.getInstance();


        
//      check if user is sign-in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.xchange_privacy:
                startActivity(new Intent(this, privacy.class));
                break;
            case R.id.xchange_login_txt:
                startActivity(new Intent(this, login.class));
                break;
            case R.id.xchange_terms:
                startActivity(new Intent(this, terms.class));
                break;
            case R.id.xchange_sign_btn:
                if(sck.netIsAvailable()){
                    registerUser();
                }else{
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();   
                }
                break;
            default:
                Toast.makeText(this, "Error While on Switch", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser() {
        String fName = xchange_fname.getText().toString().trim();
        String lName = xchange_lname.getText().toString().trim();
        String email = xchange_email.getText().toString().trim();
        String password = xchange_password.getText().toString().trim();
        Boolean terms = terms_and_privacy.isChecked();

//      Check for exceptions

        if(fName.isEmpty()){
            xchange_fname.setError("Enter your first name.");
            xchange_fname.requestFocus();
            return;
        }

        if(lName.isEmpty()){
            xchange_lname.setError("Enter your last name.");
            xchange_lname.requestFocus();
            return;
        }
        if(email.isEmpty()){
            xchange_email.setError("Enter your Email Address.");
            xchange_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            xchange_email.setError("Enter valid Email Address");
            xchange_email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            xchange_password.setError("Enter a password.");
            xchange_password.requestFocus();
            return;
        }
        if(password.length() < 6){
            xchange_password.setError("Password must be greater than 6 Characters.");
            xchange_password.requestFocus();
            return;
        }
        if(!terms){
            terms_and_privacy.setError("Please Agree to Our Terms And Privacy Policy");
            terms_and_privacy.requestFocus();
            return;
        }

        pg_bar.setVisibility(View.VISIBLE);
        xchange_sign_btn.setVisibility(View.GONE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            users_information users = new users_information(email, fName, lName, "User");
                            FirebaseDatabase.getInstance().getReference("users_information")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        createWallet();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if(user.isEmailVerified()){
                                            pg_bar.setVisibility(View.GONE);
                                            xchange_sign_btn.setVisibility(View.VISIBLE);
                                            startActivity(new Intent(sign_up.this, MainActivity.class));
                                        }else{
                                            pg_bar.setVisibility(View.GONE);
                                            xchange_sign_btn.setVisibility(View.VISIBLE);
                                            user.sendEmailVerification();
                                            Toast.makeText(sign_up.this, "Please Please Check and Verify Your Email Verify Your Email", Toast.LENGTH_SHORT).show();
                                        }
                                        
                                    }else{
                                        Toast.makeText(sign_up.this, "Error While Trying TO Save To DB", Toast.LENGTH_SHORT).show();
                                        pg_bar.setVisibility(View.GONE);
                                        xchange_sign_btn.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(sign_up.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pg_bar.setVisibility(View.GONE);
                            xchange_sign_btn.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    private void createWallet() {
        Balance balance = new Balance();
        FirebaseDatabase.getInstance().getReference("Balance")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(balance).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(sign_up.this, "Created new Wallet", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(sign_up.this, "Error While Trying TO Save To Create Wallet", Toast.LENGTH_SHORT).show();
                    pg_bar.setVisibility(View.GONE);
                    xchange_sign_btn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}