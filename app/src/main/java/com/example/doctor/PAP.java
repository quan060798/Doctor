package com.example.doctor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class PAP extends RecyclerView.Adapter<PAP.MyViewHolder> {

    Context context;

    ArrayList<Appointment> list;

    public PAP(Context context, ArrayList<Appointment> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.patientappointmentlist,parent,false);
        return new MyViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Appointment appointment = list.get(position);
        holder.pname.setText(appointment.getCust_name());
        holder.cdate.setText(appointment.getDate());
        holder.ctime.setText(appointment.getTime());
        holder.cnum.setText(appointment.getCust_phonenum());
        holder.dname.setText(appointment.getDoc_name());
        holder.status.setText(appointment.getDoc_approval());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pname,cdate,ctime,cnum,dname,status;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            pname = itemView.findViewById(R.id.name1);
            cdate = itemView.findViewById(R.id.date1);
            ctime = itemView.findViewById(R.id.time1);
            cnum = itemView.findViewById(R.id.phonenum1);
            dname = itemView.findViewById(R.id.docname);
            status = itemView.findViewById(R.id.status);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent a = new Intent(itemView.getContext(),AppointmentDetails.class);
                    a.putExtra("custname", pname.getText().toString());
                    a.putExtra("custdate", cdate.getText().toString());
                    a.putExtra("custtime", ctime.getText().toString());
                    a.putExtra("custphonenum", cnum.getText().toString());
                    a.putExtra("docname",dname.getText().toString());
                    a.putExtra("status",status.getText().toString());
                    itemView.getContext().startActivity(a);




                }
            });

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    Intent a = new Intent(itemView.getContext(),appointmentdetails.class);
//                    a.putExtra("pname", pname.getText().toString());
//                    a.putExtra("date", cdate.getText().toString());
//                    a.putExtra("time", ctime.getText().toString());
//                    a.putExtra("phonenum", cnum.getText().toString());
//                    a.putExtra("docname", dname.getText().toString());
//                    itemView.getContext().startActivity(a);
//
//
//
//
//                }
//            });
        }

    }

}
