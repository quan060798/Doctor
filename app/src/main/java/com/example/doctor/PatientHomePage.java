package com.example.doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class PatientHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home_page);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.bookdoc:
                Intent intent2page = new Intent(PatientHomePage.this, Selectdoctor.class);
                startActivity(intent2page);
                break;


            case R.id.myappointment:
                Intent intent2viewappointment = new Intent(PatientHomePage.this,PatientAppointmentPage.class);
                intent2viewappointment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2viewappointment);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent2logout = new Intent(PatientHomePage.this,MainActivity.class);
                intent2logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2logout);
                break;



        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        menu.removeItem(R.id.pending_appointment);
        menu.removeItem(R.id.back);
        return super.onCreateOptionsMenu(menu);
//        return true;

    }
}