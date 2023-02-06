package com.androidproject.iottechhrm.model;

public class Leave_Detail {
    public String emp_id,category,period,startDate,endDate,lastDate,lastPeriod,lastCategory,address,bottomSection,status,applied_at;

    public Leave_Detail(String emp_id, String category, String period, String startDate, String endDate, String lastDate, String lastPeriod, String lastCategory, String address, String bottomSection, String status, String applied_at) {
        this.emp_id = emp_id;
        this.category = category;
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lastDate = lastDate;
        this.lastPeriod = lastPeriod;
        this.lastCategory = lastCategory;
        this.address = address;
        this.bottomSection = bottomSection;
        this.status = status;
        this.applied_at = applied_at;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public String getCategory() {
        return category;
    }

    public String getPeriod() {
        return period;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public String getLastPeriod() {
        return lastPeriod;
    }

    public String getLastCategory() {
        return lastCategory;
    }

    public String getAddress() {
        return address;
    }

    public String getBottomSection() {
        return bottomSection;
    }

    public String getStatus() {
        return status;
    }

    public String getApplied_at() {
        return applied_at;
    }
}
