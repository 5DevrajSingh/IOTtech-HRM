package com.androidproject.iottechhrm.model;

public class Loan_Detail {
    public String emiId,loanAmount,interestRate,NoOfInstallment,emi,startMonthOfEmi,totalPayableAmount,created_at;

    public Loan_Detail(String emiId, String loanAmount, String interestRate, String noOfInstallment, String emi, String startMonthOfEmi, String totalPayableAmount, String created_at) {
        this.emiId = emiId;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        NoOfInstallment = noOfInstallment;
        this.emi = emi;
        this.startMonthOfEmi = startMonthOfEmi;
        this.totalPayableAmount = totalPayableAmount;
        this.created_at = created_at;
    }

    public String getEmiId() {
        return emiId;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public String getNoOfInstallment() {
        return NoOfInstallment;
    }

    public String getEmi() {
        return emi;
    }

    public String getStartMonthOfEmi() {
        return startMonthOfEmi;
    }

    public String getTotalPayableAmount() {
        return totalPayableAmount;
    }

    public String getCreated_at() {
        return created_at;
    }
}
