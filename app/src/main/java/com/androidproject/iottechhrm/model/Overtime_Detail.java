package com.androidproject.iottechhrm.model;

public class Overtime_Detail {
    public String id,attendance,date,overtimeIn,overtimeOut,overtimeHour;

    public Overtime_Detail(String id, String attendance, String date, String overtimeIn, String overtimeOut, String overtimeHour) {
        this.id = id;
        this.attendance = attendance;
        this.date = date;
        this.overtimeIn = overtimeIn;
        this.overtimeOut = overtimeOut;
        this.overtimeHour = overtimeHour;
    }

    public String getId() {
        return id;
    }

    public String getAttendance() {
        return attendance;
    }

    public String getDate() {
        return date;
    }

    public String getOvertimeIn() {
        return overtimeIn;
    }

    public String getOvertimeOut() {
        return overtimeOut;
    }

    public String getOvertimeHour() {
        return overtimeHour;
    }
}
