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
import com.androidproject.iottechhrm.model.Loan_Instalment;

import java.util.ArrayList;

public class LoanInstalmentAdapter extends RecyclerView.Adapter<LoanInstalmentAdapter.MyViewHolder> {

    Context context;
    ArrayList<Loan_Instalment> loan_instalments;
    OnRecyler onRecyler;

    public LoanInstalmentAdapter(Context context, ArrayList<Loan_Instalment> loan_instalments, OnRecyler onRecyler) {
        this.context=context;
        this.loan_instalments=loan_instalments;
        this.onRecyler=onRecyler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_instalment,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Loan_Instalment loan_detail=loan_instalments.get(position);

        holder.tv_paid_emi.setText(loan_detail.getPaidInstallment());
        holder.tv_loan_amount.setText("₹ "+loan_detail.getLoanAmount());
        holder.tv_interest.setText(loan_detail.getInterestRate()+" %");
        holder.tv_payable.setText("₹ "+loan_detail.getTotalPayableAmount());
        holder.tv_instalment.setText(loan_detail.getNoOfInstallment());
        holder.tv_emi.setText(loan_detail.getEmi());
        holder.tv_startMonthOfEmi.setText("Start Month of Emi "+loan_detail.getStartMonthOfEmi());

    }


    @Override
    public int getItemCount() {
        return loan_instalments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_instalment,tv_paid_emi,tv_payable,tv_loan_amount,tv_interest,tv_emi,tv_startMonthOfEmi;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_instalment=itemView.findViewById(R.id.tv_instalment);
            tv_paid_emi=itemView.findViewById(R.id.tv_paid_emi);
            tv_payable=itemView.findViewById(R.id.tv_payable);
            tv_loan_amount=itemView.findViewById(R.id.tv_loan_amount);
            tv_interest=itemView.findViewById(R.id.tv_interest);
            tv_emi=itemView.findViewById(R.id.tv_emi);
            tv_startMonthOfEmi=itemView.findViewById(R.id.tv_startMonthOfEmi);
        }
    }
}
