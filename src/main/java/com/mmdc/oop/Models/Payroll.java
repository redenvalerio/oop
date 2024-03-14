package com.mmdc.oop.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "payroll")
public class Payroll {
  @DatabaseField(generatedId = true)
  private int id;
  
  @DatabaseField(canBeNull = false)
  private String payBeginDate;

  @DatabaseField(canBeNull = false)
  private String payEndDate;

  @DatabaseField(canBeNull = false)
  private Double totalHours;

  @DatabaseField(canBeNull = false)
  private Double regularPay;

  @DatabaseField(canBeNull = false)
  private Double overtimePay;

  @DatabaseField(canBeNull = false)
  private Double specialHoliday;

  @DatabaseField(canBeNull = false)
  private Double regularHoliday;

  @DatabaseField(canBeNull = false)
  private Double allowance;

  @DatabaseField(canBeNull = false)
  private Double sss;

  @DatabaseField(canBeNull = false)
  private Double philhealth;

  @DatabaseField(canBeNull = false)
  private Double pagibig;

  @DatabaseField(canBeNull = false)
  private Double tax;

  @DatabaseField(canBeNull = false)
  private Double cashAdvance;

  @DatabaseField(canBeNull = false)
  private Double loan;

  @DatabaseField(canBeNull = false)
  private Double otherDeductions;

  @DatabaseField(canBeNull = false)
  private Double totalPay;

  @DatabaseField(canBeNull = false)
  private Double totalDeductions;

  @DatabaseField(canBeNull = false)
  private Double netPay;

  @DatabaseField(foreign = true, columnName = "user_id")
  private User user;

  public Payroll() {
  }


  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPayBeginDate() {
    return this.payBeginDate;
  }

  public void setPayBeginDate(String payBeginDate) {
    this.payBeginDate = payBeginDate;
  }

  public String getPayEndDate() {
    return this.payEndDate;
  }

  public void setPayEndDate(String payEndDate) {
    this.payEndDate = payEndDate;
  }

  public Double getTotalHours() {
    return this.totalHours;
  }

  public void setTotalHours(Double totalHours) {
    this.totalHours = totalHours;
  }

  public Double getRegularPay() {
    return this.regularPay;
  }

  public void setRegularPay(Double regularPay) {
    this.regularPay = regularPay;
  }

  public Double getOvertimePay() {
    return this.overtimePay;
  }

  public void setOvertimePay(Double overtimePay) {
    this.overtimePay = overtimePay;
  }

  public Double getSpecialHoliday() {
    return this.specialHoliday;
  }

  public void setSpecialHoliday(Double specialHoliday) {
    this.specialHoliday = specialHoliday;
  }

  public Double getRegularHoliday() {
    return this.regularHoliday;
  }

  public void setRegularHoliday(Double regularHoliday) {
    this.regularHoliday = regularHoliday;
  }

  public Double getAllowance() {
    return this.allowance;
  }

  public void setAllowance(Double allowance) {
    this.allowance = allowance;
  }

  public Double getSss() {
    return this.sss;
  }

  public void setSss(Double sss) {
    this.sss = sss;
  }

  public Double getPhilhealth() {
    return this.philhealth;
  }

  public void setPhilhealth(Double philhealth) {
    this.philhealth = philhealth;
  }

  public Double getPagibig() {
    return this.pagibig;
  }

  public void setPagibig(Double pagibig) {
    this.pagibig = pagibig;
  }

  public Double getTax() {
    return this.tax;
  }

  public void setTax(Double tax) {
    this.tax = tax;
  }

  public Double getCashAdvance() {
    return this.cashAdvance;
  }

  public void setCashAdvance(Double cashAdvance) {
    this.cashAdvance = cashAdvance;
  }

  public Double getLoan() {
    return this.loan;
  }

  public void setLoan(Double loan) {
    this.loan = loan;
  }

  public Double getOtherDeductions() {
    return this.otherDeductions;
  }

  public void setOtherDeductions(Double otherDeductions) {
    this.otherDeductions = otherDeductions;
  }

  public Double getTotalPay() {
    return this.totalPay;
  }

  public void setTotalPay(Double totalPay) {
    this.totalPay = totalPay;
  }

  public Double getTotalDeductions() {
    return this.totalDeductions;
  }

  public void setTotalDeductions(Double totalDeductions) {
    this.totalDeductions = totalDeductions;
  }

  public Double getNetPay() {
    return this.netPay;
  }

  public void setNetPay(Double netPay) {
    this.netPay = netPay;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
