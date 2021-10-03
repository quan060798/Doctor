package com.example.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private TextView forgot, regPatient, regDoctor;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        String userType = ""+ds.child("userType").getValue();
                        //if user is doctor
                        if (userType.equals("Doctor")){
                            progressDialog.dismiss();
                            Intent intentToDoctor = new Intent(MainActivity.this,DoctorHomePage.class);
                            startActivity(intentToDoctor);
                            finish();
                        }
                        //if user is patient
                        else {
                            Intent intentToPatient = new Intent(MainActivity.this,PatientHomePage.class);
                            startActivity(intentToPatient);
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");

        email = findViewById(R.id.ed_email);
        password = findViewById(R.id.ed_password);
        login = findViewById(R.id.btn_login);
        forgot = findViewById(R.id.tv_forgot);
        regPatient = findViewById(R.id.tv_registerPt);
        regDoctor = findViewById(R.id.tv_registerDr);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging In...");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String logEmail = email.getText().toString();
                String logPass = password.getText().toString();

                if(email.getText().toString().matches("")){
                    email.setError("Email is required!");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(logEmail).matches()){
                    email.setError("Please provide a valid email!");
                    email.requestFocus();
                    return;
                }
                if(password.getText().toString().matches("")){
                    password.setError("Password is required!");
                    password.requestFocus();
                    return;
                }
                if(password.length()<6){
                    password.setError("Password cannot less than 6 character!");
                    password.requestFocus();
                    return;
                }

                progressDialog.show();

                mAuth.signInWithEmailAndPassword(logEmail,logPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user.isEmailVerified()){
                                //get user email and is from auth
                                checkUserType();
                            }
                            else{
                                user.sendEmailVerification();
                                Toast.makeText(getApplicationContext(),"Please check your email to verify your account!",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Login failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),""+ e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToForgot = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(intentToForgot);
            }
        });

        regPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToRegisterPt = new Intent(MainActivity.this, RegisterPatient.class);
                startActivity(intentToRegisterPt);
            }
        });

        regDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToRegisterDr = new Intent(MainActivity.this, RegisterDoctor.class);
                startActivity(intentToRegisterDr);
            }
        });
    }

    private void checkUserType(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String userType = ""+ds.child("userType").getValue();
                    //if user is doctor
                    if (userType.equals("Doctor")){
                        progressDialog.dismiss();
                        Intent intentToDoctor = new Intent(MainActivity.this,DoctorHomePage.class);
                        startActivity(intentToDoctor);
                        finish();
                    }
                    //if user is patient
                    else {
                        Intent intentToPatient = new Intent(MainActivity.this,PatientHomePage.class);
                        startActivity(intentToPatient);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}