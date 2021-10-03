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

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.MyViewHolder> {

    Context context;

    ArrayList<Appointment> list;

    public CustomListAdapter(Context context, ArrayList<Appointment> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.booking_info,parent,false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Appointment appointment = list.get(position);
        holder.cname.setText(appointment.getCust_name());
        holder.cdate.setText(appointment.getDate());
        holder.ctime.setText(appointment.getTime());
        holder.cnum.setText(appointment.getCust_phonenum());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView cname,cdate,ctime,cnum;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cname = itemView.findViewById(R.id.name1);
            cdate = itemView.findViewById(R.id.date1);
            ctime = itemView.findViewById(R.id.time1);
            cnum = itemView.findViewById(R.id.phonenum1);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent a = new Intent(itemView.getContext(),DoctorApproval.class);
                    a.putExtra("custname", cname.getText().toString());
                    a.putExtra("custdate", cdate.getText().toString());
                    a.putExtra("custtime", ctime.getText().toString());
                    a.putExtra("custphonenum", cnum.getText().toString());
                    itemView.getContext().startActivity(a);




                }
            });
        }

    }

}
