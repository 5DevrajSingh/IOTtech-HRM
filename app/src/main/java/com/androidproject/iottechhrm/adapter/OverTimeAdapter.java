package com.androidproject.iottechhrm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.inter.OnRecyler;
import com.androidproject.iottechhrm.model.Overtime_Detail;

import java.util.ArrayList;

public class OverTimeAdapter extends RecyclerView.Adapter<OverTimeAdapter.MyViewHolder> {
    Context context;
    ArrayList<Overtime_Detail> overtime_details;
    OnRecyler onRecyler;
    public OverTimeAdapter(Context context, ArrayList<Overtime_Detail> overtime_details, OnRecyler onRecyler) {
        this.context=context;
        this.onRecyler=onRecyler;
        this.overtime_details=overtime_details;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.over_time,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Overtime_Detail overtime_detail=overtime_details.get(position);

        holder.in_out.setText(overtime_detail.getOvertimeIn()+" - "+overtime_detail.getOvertimeOut());
        holder.tv_date.setText(overtime_detail.getDate());
        holder.over_work_hours.setText(overtime_detail.getOvertimeHour());

    }


    @Override
    public int getItemCount() {
        return overtime_details.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date,over_work_hours,in_out;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date=itemView.findViewById(R.id.tv_date);
            over_work_hours=itemView.findViewById(R.id.over_work_hours);
            in_out=itemView.findViewById(R.id.in_out);
        }
    }
}
