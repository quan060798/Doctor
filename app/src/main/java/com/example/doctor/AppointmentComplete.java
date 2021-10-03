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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AppointmentComplete extends AppCompatActivity {
    private EditText patientstatus;
    TextView patient_name, patient_phonenum, bookingdate, bookingtime;
    private Button complete;
    DatabaseReference database, reff;
    DatabaseReference ref;
    Appointment appointment;
    History history = new History();
    private String name;
    long maxid=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_complete);

        database = FirebaseDatabase.getInstance().getReference().child("Appointment");

        patientstatus = (EditText) findViewById(R.id.status);
        complete = findViewById(R.id.btncomplete);
        patient_name = (TextView)findViewById(R.id.name);
        patient_phonenum = (TextView)findViewById(R.id.phonenum);
        bookingdate = (TextView)findViewById(R.id.date);
        bookingtime = (TextView)findViewById(R.id.time);


        patient_name.setText(getIntent().getStringExtra("custname"));
        bookingdate.setText(getIntent().getStringExtra("custdate"));
        bookingtime.setText(getIntent().getStringExtra("custtime"));
        patient_phonenum.setText(getIntent().getStringExtra("custphonenum"));

        reff = FirebaseDatabase.getInstance().getReference().child("Appointment");
        ref = FirebaseDatabase.getInstance().getReference().child("History");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    maxid= (int) (snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String name2=patient_name.getText().toString();
        Query queryReff = ref.orderByChild("cust_name").equalTo(name2);
        queryReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    History history1 = dataSnapshot.getValue(History.class);
                    patientstatus.setText(history1.getComment());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                history.setCust_name(patient_name.getText().toString());
                history.setComment(patientstatus.getText().toString());
                ref.child(String.valueOf(history.getCust_name())).setValue(history);
                String approval = "Completed";
                String name1 = patient_name.getText().toString();
                Query queryRef = reff.orderByChild("cust_name").equalTo(name1);
                Query queryReff = ref.orderByChild("cust_name").equalTo(name1);
                queryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("doc_approval", approval);
//                        hashMap.put("comment", patientstatus.getText().toString());
                        reff.child(name1).updateChildren(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                queryReff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("comment", patientstatus.getText().toString());
                        ref.child(name1).updateChildren(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(AppointmentComplete.this, "Appointment Completed", Toast.LENGTH_SHORT).show();
                Intent intentnew = new Intent(AppointmentComplete.this,DoctorHomePage.class);
                startActivity(intentnew);
            }

        });



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.back:
                Intent intent2back = new Intent(AppointmentComplete.this,DoctorSchedule.class);
                intent2back.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2back);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        menu.removeItem(R.id.bookdoc);
        menu.removeItem(R.id.myappointment);
        menu.removeItem(R.id.pending_appointment);
        menu.removeItem(R.id.logout);
        return super.onCreateOptionsMenu(menu);

    }
}