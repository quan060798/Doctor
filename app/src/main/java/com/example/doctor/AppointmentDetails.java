package com.example.doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppointmentDetails extends AppCompatActivity {
    DatabaseReference database;
    TextView pname,num,date,time,dname,status;
    Button cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        database = FirebaseDatabase.getInstance().getReference().child("Appointment");
        pname = findViewById(R.id.myname);
        num = findViewById(R.id.phonenum);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        dname = findViewById(R.id.docname);
        status = findViewById(R.id.status);
        cancel = findViewById(R.id.cancel);

        pname.setText(getIntent().getStringExtra("custname"));
        num.setText(getIntent().getStringExtra("custphonenum"));
        date.setText(getIntent().getStringExtra("custdate"));
        time.setText(getIntent().getStringExtra("custtime"));
        dname.setText(getIntent().getStringExtra("docname"));
        status.setText(getIntent().getStringExtra("status"));

        if(status.getText().toString().equals("Approved")){
            Toast.makeText(AppointmentDetails.this, "Hooray ! Your Appointment Has Been Approved !", Toast.LENGTH_SHORT).show();
        }
        if(status.getText().toString().equals("Completed")){
            cancel.setVisibility(View.INVISIBLE);
        }
        else{

        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child(pname.getText().toString()).setValue(null);
                Toast.makeText(AppointmentDetails.this, "Appointment Cancelled", Toast.LENGTH_SHORT).show();
                Intent b = new Intent(AppointmentDetails.this,PatientHomePage.class);
                startActivity(b);
            }
        });


//        al.setText(getIntent().getStringExtra("custname"));
//        bookingdate.setText(getIntent().getStringExtra("custdate"));
//        bookingtime.setText(getIntent().getStringExtra("custtime"));
//        patient_phonenum.setText(getIntent().getStringExtra("custphonenum"));setText("lol");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.back:
                Intent intent2back = new Intent(AppointmentDetails.this,PatientAppointmentPage.class);
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