package com.androidproject.iottechhrm.model;

public class Salary_Detail {
    public String payment_amount,payment_month,gross_salary,total_deduction,net_salary,bonus,present,absence;

    public Salary_Detail(String payment_amount, String payment_month, String gross_salary, String total_deduction, String net_salary, String bonus, String present, String absence) {
        this.payment_amount = payment_amount;
        this.payment_month = payment_month;
        this.gross_salary = gross_salary;
        this.total_deduction = total_deduction;
        this.net_salary = net_salary;
        this.bonus = bonus;
        this.present = present;
        this.absence = absence;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public String getPayment_month() {
        return payment_month;
    }

    public String getGross_salary() {
        return gross_salary;
    }

    public String getTotal_deduction() {
        return total_deduction;
    }

    public String getNet_salary() {
        return net_salary;
    }

    public String getBonus() {
        return bonus;
    }

    public String getPresent() {
        return present;
    }

    public String getAbsence() {
        return absence;
    }
}
