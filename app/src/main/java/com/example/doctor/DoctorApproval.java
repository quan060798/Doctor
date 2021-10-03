package com.example.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class DoctorApproval extends AppCompatActivity {

    TextView patient_name, patient_phonenum, bookingdate, bookingtime, custhistory;
    DatabaseReference database, reff;
    private Button btn_accept, btn_decline, btn_history;
    private ImageView pic;
    DatabaseReference ref;
    Appointment appointment;
    History history;
    long maxid=0;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_approval);



        database = FirebaseDatabase.getInstance().getReference().child("Appointment");
//        Query queryRef = database.orderByChild("doc_name").equalTo("Maghesan");

        patient_name = (TextView)findViewById(R.id.name);
        patient_phonenum = (TextView)findViewById(R.id.phonenum);
        bookingdate = (TextView)findViewById(R.id.date);
        bookingtime = (TextView)findViewById(R.id.time);
        btn_accept = findViewById(R.id.accept);
        btn_decline = findViewById(R.id.decline);
        custhistory = findViewById(R.id.commenthistory);
//        name = getIntent().getExtras().get("name").toString();

        setTitle("Appointment Details");

        patient_name.setText(getIntent().getStringExtra("custname"));
        bookingdate.setText(getIntent().getStringExtra("custdate"));
        bookingtime.setText(getIntent().getStringExtra("custtime"));
        patient_phonenum.setText(getIntent().getStringExtra("custphonenum"));


//        reff = FirebaseDatabase.getInstance().getReference().child("Appointment");
//        DatabaseReference appointment = FirebaseDatabase.getInstance().getReference("Appointment");
//
//        DatabaseReference newRef = reff.push();

//        patient_name.setText(name);

//        reff.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Query queryRef = appointment.orderByChild("name").equalTo(name);
//
//                queryRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                            Appointment appointment2 = dataSnapshot.getValue(Appointment.class);
//                            String a = appointment2.getCust_name();
////                            String b = appointment2.getCust_phonenum();
////                            String c = appointment2.getDate();
////                            String d = appointment2.getTime();
//                            patient_name.setText(name);
////                            patient_phonenum.setText(b);
////                            bookingdate.setText(c);
////                            bookingtime.setText(d);
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    maxid=(dataSnapshot.getChildrenCount());
//                }
//                String cust_name=dataSnapshot.child("cust_name").getValue().toString();
//                String cust_phonenum=dataSnapshot.child("cust_phonenum").getValue().toString();
//                String date=dataSnapshot.child("date").getValue().toString();
//                String time=dataSnapshot.child("time").getValue().toString();
//                patient_name.setText(cust_name);
//                patient_phonenum.setText(cust_phonenum);
//                bookingdate.setText(date);
//                bookingtime.setText(time);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


//        btn_accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ref.child("1").child("cust_name").setValue("Approved");
//
//            }
//        });


        reff = FirebaseDatabase.getInstance().getReference().child("Appointment");
        ref = FirebaseDatabase.getInstance().getReference().child("History");

        String name2=patient_name.getText().toString();
        Query queryReff = ref.orderByChild("cust_name").equalTo(name2);
        queryReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    History history1 = dataSnapshot.getValue(History.class);
                    custhistory.setText(history1.getComment());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String approval = "Accepted";
                String name1 = patient_name.getText().toString();
                Query queryRef = reff.orderByChild("cust_name").equalTo(name1);
                queryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("doc_approval", approval);
                        reff.child(name1).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(DoctorApproval.this, "Appointment confirmed", Toast.LENGTH_SHORT).show();
                                Intent intentapproved = new Intent(DoctorApproval.this,AppointmentList.class);
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

        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2decline = new Intent(DoctorApproval.this, DoctorDecline.class);
                String data = patient_name.getText().toString();
                intent2decline.putExtra("declinename", data);
                startActivity(intent2decline);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.back:
                Intent intent2back = new Intent(DoctorApproval.this,AppointmentList.class);
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