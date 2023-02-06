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
import com.androidproject.iottechhrm.model.PF_Detail;

import java.util.ArrayList;

public class PFAdapter extends RecyclerView.Adapter<PFAdapter.MyViewHolder> {

    Context context;
    ArrayList<PF_Detail> pf_details;
    OnRecyler onRecyler;

    public PFAdapter(Context context, ArrayList<PF_Detail> pf_details, OnRecyler onRecyler) {
        this.context=context;
        this.onRecyler=onRecyler;
        this.pf_details=pf_details;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pf_detail,parent,false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PF_Detail pf_detail=pf_details.get(position);

        holder.tv_pf.setText("₹ "+pf_detail.getProvident_fund());
        holder.tv_bonus_amount.setText("₹ "+pf_detail.getBonus());
        holder.tv_gross_amount.setText("₹ "+pf_detail.getGross_salary());
        holder.tv_month_amount.setText("Pay For "+pf_detail.getPayment_month());
        holder.tv_basic.setText("₹ "+pf_detail.getBasic_salary());
        holder.tv_net_salary_amount.setText("₹ "+pf_detail.getNet_salary());

    }


    @Override
    public int getItemCount() {
        return pf_details.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_pf,tv_gross_amount,tv_basic,tv_net_salary_amount,tv_bonus_amount,tv_month_amount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_pf=itemView.findViewById(R.id.tv_pf);
            tv_gross_amount=itemView.findViewById(R.id.tv_gross_amount);
            tv_basic=itemView.findViewById(R.id.tv_basic);
            tv_net_salary_amount=itemView.findViewById(R.id.tv_net_salary_amount);
            tv_bonus_amount=itemView.findViewById(R.id.tv_bonus_amount);
            tv_month_amount=itemView.findViewById(R.id.tv_month_amount);
        }
    }
}
