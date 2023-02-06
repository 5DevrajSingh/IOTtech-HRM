package com.androidproject.iottechhrm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.inter.OnRecyler;
import com.androidproject.iottechhrm.model.Salary_Detail;

import java.util.ArrayList;

public class SalaryDetailAdapter extends RecyclerView.Adapter<SalaryDetailAdapter.MyViewHolder> {
    Context context;
    ArrayList<Salary_Detail> salary_details;
    OnRecyler onRecyler;
    public SalaryDetailAdapter(Context context, ArrayList<Salary_Detail> salary_details, OnRecyler onRecyler) {
        this.context=context;
        this.salary_details=salary_details;
        this.onRecyler=onRecyler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.salary_detail,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Salary_Detail salary_detail=salary_details.get(position);

        holder.tv_paid_amount.setText("₹ "+salary_detail.getPayment_amount());
        holder.tv_month_amount.setText("Pay For "+salary_detail.getPayment_month());
        holder.tv_attendance_amount.setText(salary_detail.getPresent());
        holder.tv_bonus_amount.setText("₹ "+salary_detail.getBonus());
        holder.tv_gross_amount.setText("₹ "+salary_detail.getGross_salary());
        holder.tv_deduct_amount.setText("₹ "+salary_detail.getTotal_deduction());
        holder.tv_net_salary_amount.setText("₹ "+salary_detail.getNet_salary());

//        holder.cv.setBackgroundResource(R.drawable.card_back);


    }


    @Override
    public int getItemCount() {
        return salary_details.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_paid_amount,tv_month_amount,tv_attendance_amount,tv_bonus_amount,tv_gross_amount,tv_deduct_amount,tv_net_salary_amount;
        CardView cv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_paid_amount=itemView.findViewById(R.id.tv_paid_amount);
            tv_month_amount=itemView.findViewById(R.id.tv_month_amount);
            tv_attendance_amount=itemView.findViewById(R.id.tv_attendance_amount);
            tv_bonus_amount=itemView.findViewById(R.id.tv_bonus_amount);
            tv_gross_amount=itemView.findViewById(R.id.tv_gross_amount);
            tv_deduct_amount=itemView.findViewById(R.id.tv_deduct_amount);
            tv_net_salary_amount=itemView.findViewById(R.id.tv_net_salary_amount);
            cv=itemView.findViewById(R.id.cv);
        }
    }
}
