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
import com.androidproject.iottechhrm.model.Leave_Detail;

import java.util.ArrayList;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.MyViewHolder> {

    Context context;
    ArrayList<Leave_Detail> leave_details;
    OnRecyler onRecyler;

    public LeaveAdapter(Context context, ArrayList<Leave_Detail> leave_details, OnRecyler onRecyler) {
        this.context=context;
        this.leave_details=leave_details;
        this.onRecyler=onRecyler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_detail,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Leave_Detail leave_detail=leave_details.get(position);

        holder.cat.setText(leave_detail.getCategory());
        holder.leave_date.setText(leave_detail.getStartDate()+" - "+leave_detail.getEndDate());
        if (leave_detail.getStatus().equalsIgnoreCase("Not Accepted")) {
            holder.leave_day.setTextColor(context.getResources().getColor(R.color.teal_200));
            holder.leave_day.setText(leave_detail.getStatus());
        }
        if (leave_detail.getStatus().equalsIgnoreCase("Pending")) {
            holder.leave_day.setTextColor(context.getResources().getColor(R.color.pending));
            holder.leave_day.setText(leave_detail.getStatus());
        }
        if (leave_detail.getStatus().equalsIgnoreCase("Accepted")) {
            holder.leave_day.setTextColor(context.getResources().getColor(R.color.accepted));
            holder.leave_day.setText(leave_detail.getStatus());
        }
        holder.apply_at.setText(leave_detail.getApplied_at());

    }


    @Override
    public int getItemCount() {
        return leave_details.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cat,leave_date,leave_day,apply_at;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cat=itemView.findViewById(R.id.cat);
            leave_date=itemView.findViewById(R.id.leave_date);
            leave_day=itemView.findViewById(R.id.leave_day);
            apply_at=itemView.findViewById(R.id.apply_at);
        }
    }
}
