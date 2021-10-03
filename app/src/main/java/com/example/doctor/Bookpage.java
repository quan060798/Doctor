package com.example.doctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import dmax.dialog.SpotsDialog;

import static java.security.AccessController.getContext;

public class Bookpage extends AppCompatActivity {
    ListView spinner;
    TextView name,specialist,address,fullname,phonenumber,chosendate,chosentime;
    Button someone,me,confirm;
    DatabaseReference reff;
    ImageView docphoto;
    Appointment appointment = new Appointment();
    long maxid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookpage);
        docphoto = findViewById(R.id.docphoto);
        chosentime = findViewById(R.id.chosentime);
        spinner = findViewById(R.id.timeslot);
        name = findViewById(R.id.docname);
        specialist = findViewById(R.id.specialist);
        address = findViewById(R.id.address);
        someone = findViewById(R.id.someone);
        me = findViewById(R.id.myself);
        fullname = findViewById(R.id.fullname);
        phonenumber = findViewById(R.id.phonenumber);
        confirm = findViewById(R.id.confirm);
        chosendate = findViewById(R.id.chosendate);
        reff = FirebaseDatabase.getInstance().getReference().child("Appointment");
        confirm.setVisibility(View.INVISIBLE);

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    maxid= (int) (snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        name.setText(getIntent().getStringExtra("docname"));
        specialist.setText(getIntent().getStringExtra("docspecialist"));
        address.setText(getIntent().getStringExtra("docaddress"));
//        String a = getIntent().getStringExtra("docphoto");
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/doctor-50945.appspot.com/o/profile_images%2FHx9THt4tjSf6e5qmEbV9ttzhtEp2?alt=media&token=105891c9-a651-4f1e-929f-223c9d193766").resize(400 ,400).into(docphoto);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");



        ArrayAdapter<String>myAdapter = new ArrayAdapter<>(Bookpage.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.timeslot));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);
        spinner.setBackgroundColor(Color.CYAN);

/** end after 1 month from now */ Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        /** start before 1 month from now */ Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);


        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendar)


                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .build();
        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String output = outputFormatter.format(startDate.getTime()); // Output : 01/20/2012
        chosendate.setText("Chosen date: "+output);

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {


                                                   @Override
        public void onDateSelected(Date date, int position) {

           DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
           String output = outputFormatter.format(date); // Output : 01/20/2012
           chosendate.setText("Chosen date: "+output);
           chosentime.setText("");
           confirm.setVisibility(View.INVISIBLE);
           }
          }
        );



        someone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname.setText("");
                phonenumber.setText("");
                fullname.setEnabled(true);
                phonenumber.setEnabled(true);
            }
        });

        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        phonenumber.setText(phone);
                        phonenumber.setEnabled(false);
                        fullname.setEnabled(false);
                        fullname.setText(name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                String d = String.valueOf(chosendate.getText());
//                String substr = d.substring(13, 23);
//
//
//
//
//                reff.orderByChild("date").equalTo(substr).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()){
//                            Toast.makeText(Bookpage.this, "Appointment made, waiting for Doctor approval.", Toast.LENGTH_LONG).show();
//
//                        }
//                        else {
//                            Toast.makeText(Bookpage.this, "FAIL.", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

            }
        });

       spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String selectedFromList = (String) (spinner.getItemAtPosition(position));
               chosentime.setText("Chosen time: "+selectedFromList);
               Object item = parent.getItemAtPosition(position);
               reff.orderByChild("time").equalTo(String.valueOf(item)).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.exists()){
                           String d = String.valueOf(chosendate.getText());
                           String substr = d.substring(13, 23);




                           reff.orderByChild("date").equalTo(substr).addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   if(snapshot.exists()){
                                       Toast.makeText(Bookpage.this, "Time Slot Taken, Please Try Different Time", Toast.LENGTH_SHORT).show();
                                        confirm.setVisibility(View.INVISIBLE);
//                                        chosentime.setText("");
                                   }
                                   else {
                                       Toast.makeText(Bookpage.this, "Slot Available !", Toast.LENGTH_SHORT).show();
                                       confirm.setVisibility(View.VISIBLE);
                                   }
                               }



                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {

                               }
                           });

                       }
                       else {
                           Toast.makeText(Bookpage.this, "Slot Available !", Toast.LENGTH_SHORT).show();
                           confirm.setVisibility(View.VISIBLE);
                       }
                   }



                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
           }
       });


//       spinner.setOnTouchListener(new View.OnTouchListener() {
//           @Override
//           public boolean onTouch(View v, MotionEvent event) {
//               String d = String.valueOf(chosendate.getText());
//               String substr = d.substring(13, 23);
//
//
//
//
//               reff.orderByChild("date").equalTo(substr).addListenerForSingleValueEvent(new ValueEventListener() {
//                   @Override
//                   public void onDataChange(@NonNull DataSnapshot snapshot) {
//                       if(snapshot.exists()){
//                           Toast.makeText(Bookpage.this, "Appointment made, waiting for Doctor approval.", Toast.LENGTH_SHORT).show();
//                           confirm.setVisibility(View.INVISIBLE);
//
//
//                       }
//                       else {
//                           Toast.makeText(Bookpage.this, "FAIL.", Toast.LENGTH_SHORT).show();
//                           confirm.setVisibility(View.VISIBLE);
//                       }
//                   }
//
//
//
//                   @Override
//                   public void onCancelled(@NonNull DatabaseError error) {
//
//                   }
//               });
//               return false;
//
//           }
//
//       });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (fullname.length()!=0 && phonenumber.length()!=0) {
                            appointment.setDoc_name(name.getText().toString().trim());
                            appointment.setCust_name(fullname.getText().toString().trim());
                            appointment.setCust_phonenum(phonenumber.getText().toString().trim());
                            String d = String.valueOf(chosendate.getText());
                            String substr = d.substring(13, 23);
                            String t = String.valueOf(chosentime.getText());
                            String subtime = t.substring(13,22);
                            appointment.setDate(substr);
                            appointment.setTime(subtime);
                            appointment.setDoc_approval("");
                            appointment.setDecline_reason("");
                            String bb = snapshot.child("name").getValue().toString();
                            appointment.setBookedby(bb);
                            reff.child(String.valueOf(appointment.getCust_name())).setValue(appointment);
//                    reff.push().setValue(appointment);


                            Intent intentdone = new Intent(Bookpage.this,PatientHomePage.class);
                            Toast.makeText(Bookpage.this, "Appointment made, waiting for Doctor approval.", Toast.LENGTH_LONG).show();
                            startActivity(intentdone);

                        }else{
                            Toast.makeText(Bookpage.this, "Info Missing", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });





    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.back:
                Intent intent2back = new Intent(Bookpage.this,Selectdoctor.class);
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
