package com.example.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class PatientAppointmentPage extends AppCompatActivity {
    Appointment appointment = new Appointment();
    RecyclerView recyclerView;
    DatabaseReference database, ref;
    PAP myAdapter;
    ArrayList<Appointment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_page);
        setTitle("View My Appointments");

        recyclerView = findViewById(R.id.patientappointmentpage);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        database = FirebaseDatabase.getInstance().getReference().child("Appointment");
//        Query queryRef = database.orderByChild("doc_name").equalTo("Maghesan");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new PAP(this,list);
        recyclerView.setAdapter(myAdapter);


        ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                Query queryRef = database.orderByChild("bookedby").equalTo(name);
                queryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Appointment appointment3 = dataSnapshot.getValue(Appointment.class);

                                list.add(appointment3);

                        }

                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        queryRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
//                    list.add(appointment);
//                }
//                myAdapter.notifyDataSetChanged();
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.back:
                Intent intent2back = new Intent(PatientAppointmentPage.this,PatientHomePage.class);
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