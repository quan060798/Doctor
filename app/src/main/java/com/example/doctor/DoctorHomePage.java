package com.example.doctor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class DoctorHomePage extends AppCompatActivity {

    private Button viewschedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home_page);
        viewschedule = findViewById(R.id.schedule);

        viewschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2view = new Intent(DoctorHomePage.this, DoctorSchedule.class);
                startActivity(intent2view);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pending_appointment:
                Intent intent2page = new Intent(DoctorHomePage.this, AppointmentList.class);
                startActivity(intent2page);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent2logout = new Intent(DoctorHomePage.this,MainActivity.class);
                intent2logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2logout);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        menu.removeItem(R.id.bookdoc);
        menu.removeItem(R.id.myappointment);
        menu.removeItem(R.id.back);
        return super.onCreateOptionsMenu(menu);

    }

}