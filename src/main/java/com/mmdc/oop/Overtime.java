package com.mmdc.oop;

public class Overtime {
    private int overtimeID;
    private Employee employee;
    private double hours;

    public Overtime(int overtimeID, Employee employee, double hours) {
        this.overtimeID = overtimeID;
        this.employee = employee;
        this.hours = hours;
    }

    public int getOvertimeID() {
        return overtimeID;
    }

    public void setOvertimeID(int overtimeID) {
        this.overtimeID = overtimeID;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }
}
