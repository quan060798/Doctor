package com.example.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DoctorDecline extends AppCompatActivity {

    private EditText declinereason;
    private Button save;
    DatabaseReference reff;
    String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_decline);
        declinereason = findViewById(R.id.reason);
        save = findViewById(R.id.declinebtn);

        setTitle("Decline Page");
        data = getIntent().getStringExtra("declinename");

        reff = FirebaseDatabase.getInstance().getReference().child("Appointment");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String approval = "Declined";
                String name2 = data;
                Query queryRef = reff.orderByChild("cust_name").equalTo(name2);
                queryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("doc_approval", approval);
                        hashMap.put("decline_reason", declinereason.getText().toString());
                        reff.child(name2).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(DoctorDecline.this, "Appointment rejected", Toast.LENGTH_SHORT).show();
                                Intent intentapproved = new Intent(DoctorDecline.this,AppointmentList.class);
                                startActivity(intentapproved);
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });






    }
}