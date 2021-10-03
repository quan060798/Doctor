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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;

    ArrayList<Doctor> list;

    public MyAdapter(Context context, ArrayList<Doctor> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Doctor doctor = list.get(position);
        holder.name.setText(doctor.getName());
        holder.address.setText(doctor.getLocation());
        holder.experience.setText(doctor.getExperience()+" years");
        holder.specialist.setText(doctor.getSpecialist());
        holder.fees.setText("RM "+doctor.getFees());
        String link = doctor.getImage();

       Picasso.get().load(link).resize(400 ,400).into(holder.imageView);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,address,experience,specialist,fees;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            experience = itemView.findViewById(R.id.experience);
            specialist = itemView.findViewById(R.id.specialist);
            fees = itemView.findViewById(R.id.fees);
            imageView = itemView.findViewById(R.id.docphoto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent a = new Intent(itemView.getContext(),Bookpage.class);
                    a.putExtra("docname", name.getText().toString());
                    a.putExtra("docaddress", address.getText().toString());
                    a.putExtra("docexperience", experience.getText().toString());
                    a.putExtra("docspecialist", specialist.getText().toString());
                    a.putExtra("docfees", fees.getText().toString());
//                    a.putExtra("docphoto",imageView.toString());
                    itemView.getContext().startActivity(a);




                }
            });
        }

    }

}
