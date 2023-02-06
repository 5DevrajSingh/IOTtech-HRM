package com.androidproject.iottechhrm.model;

public class PF_Detail {
    public String provident_fund,payment_month,gross_salary,basic_salary,net_salary,bonus;

    public PF_Detail(String provident_fund, String payment_month, String gross_salary, String basic_salary, String net_salary, String bonus) {
        this.provident_fund = provident_fund;
        this.payment_month = payment_month;
        this.gross_salary = gross_salary;
        this.basic_salary = basic_salary;
        this.net_salary = net_salary;
        this.bonus = bonus;
    }

    public String getProvident_fund() {
        return provident_fund;
    }

    public String getPayment_month() {
        return payment_month;
    }

    public String getGross_salary() {
        return gross_salary;
    }

    public String getBasic_salary() {
        return basic_salary;
    }

    public String getNet_salary() {
        return net_salary;
    }

    public String getBonus() {
        return bonus;
    }
}
