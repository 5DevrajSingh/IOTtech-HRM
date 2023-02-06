package com.androidproject.iottechhrm.model;

public class Loan_Instalment {
    public String emiId,NoOfInstallment,PaidInstallment,loanAmount,interestRate,emi,startMonthOfEmi,totalPayableAmount;

    public Loan_Instalment(String emiId, String noOfInstallment, String paidInstallment, String loanAmount, String interestRate, String emi, String startMonthOfEmi, String totalPayableAmount) {
        this.emiId = emiId;
        NoOfInstallment = noOfInstallment;
        PaidInstallment = paidInstallment;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.emi = emi;
        this.startMonthOfEmi = startMonthOfEmi;
        this.totalPayableAmount = totalPayableAmount;
    }

    public String getEmiId() {
        return emiId;
    }

    public String getNoOfInstallment() {
        return NoOfInstallment;
    }

    public String getPaidInstallment() {
        return PaidInstallment;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public String getInterestRate() {
        return interestRate;
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
}
