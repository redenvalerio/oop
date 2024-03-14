package com.mmdc.oop.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "employees")
public class Employee {
  @DatabaseField(generatedId = true)
  private int id; 

  @DatabaseField(canBeNull = false)
  private String firstName;

  @DatabaseField(canBeNull = true)
  private String middleName;

  @DatabaseField(canBeNull = false)
  private String lastName;

  @DatabaseField(canBeNull = false, defaultValue = "1900-01-01")
  private String dateOfBirth;

  @DatabaseField(canBeNull = false)
  private String email;

  @DatabaseField(canBeNull = true)
  private String phone;

  @DatabaseField(canBeNull = true)
  private String address;

  @DatabaseField(canBeNull = true)
  private String city;

  @DatabaseField(canBeNull = true)
  private String province;

  @DatabaseField(canBeNull = false)
  private String hireDate;

  @DatabaseField(canBeNull = false)
  private String position;

  @DatabaseField(canBeNull = false)
  private String department;

  @DatabaseField(canBeNull = false, defaultValue = "Full Time")
  private String employeeType;

  @DatabaseField(canBeNull = true, defaultValue = "Active")
  private String employeeStatus;

  @DatabaseField(canBeNull = false, defaultValue = "0.0")
  private Double salary;

  @DatabaseField(canBeNull = false, defaultValue = "0.0")
  private Double hourlyRate;

  @DatabaseField(canBeNull = true)
  private String SSS;

  @DatabaseField(canBeNull = true)
  private String TIN;

  @DatabaseField(canBeNull = true)
  private String PhilHealth;

  @DatabaseField(canBeNull = true)
  private String PAGIBIG;

  @DatabaseField(canBeNull = true)
  private String emergencyContactName;

  @DatabaseField(canBeNull = true)
  private String emergencyContactPhone;

  @DatabaseField(canBeNull = true)
  private String emergencyContactAddress;

  @DatabaseField(canBeNull = true)
  private String emergencyContactRelationship;

  @DatabaseField(canBeNull = false, defaultValue = "SUN,MON,TUE,WED,THU,FRI,SAT")
  private String workDays;

  // standard work times
  @DatabaseField(canBeNull = false, defaultValue = "08:00")
  private String shiftStartTime;

  @DatabaseField(canBeNull = false, defaultValue = "17:00")
  private String shiftEndTime;

  public Employee() {}

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return this.middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDateOfBirth() {
    return this.dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return this.phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return this.address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return this.city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getProvince() {
    return this.province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getHireDate() {
    return this.hireDate;
  }

  public void setHireDate(String hireDate) {
    this.hireDate = hireDate;
  }

  public String getPosition() {
    return this.position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getDepartment() {
    return this.department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getEmployeeType() {
    return this.employeeType;
  }

  public void setEmployeeType(String employeeType) {
    this.employeeType = employeeType;
  }

  public String getEmployeeStatus() {
    return this.employeeStatus;
  }

  public void setEmployeeStatus(String employeeStatus) {
    this.employeeStatus = employeeStatus;
  }

  public Double getSalary() {
    return this.salary;
  }

  public void setSalary(Double salary) {
    this.salary = salary;
  }

  public Double getHourlyRate() {
    return this.hourlyRate;
  }

  public void setHourlyRate(Double hourlyRate) {
    this.hourlyRate = hourlyRate;
  }

  public String getSSS() {
    return this.SSS;
  }

  public void setSSS(String SSS) {
    this.SSS = SSS;
  }

  public String getTIN() {
    return this.TIN;
  }

  public void setTIN(String TIN) {
    this.TIN = TIN;
  }

  public String getPhilHealth() {
    return this.PhilHealth;
  }

  public void setPhilHealth(String PhilHealth) {
    this.PhilHealth = PhilHealth;
  }

  public String getPAGIBIG() {
    return this.PAGIBIG;
  }

  public void setPAGIBIG(String PAGIBIG) {
    this.PAGIBIG = PAGIBIG;
  }

  public String getEmergencyContactName() {
    return this.emergencyContactName;
  }

  public void setEmergencyContactName(String emergencyContactName) {
    this.emergencyContactName = emergencyContactName;
  }

  public String getEmergencyContactPhone() {
    return this.emergencyContactPhone;
  }

  public void setEmergencyContactPhone(String emergencyContactPhone) {
    this.emergencyContactPhone = emergencyContactPhone;
  }

  public String getEmergencyContactAddress() {
    return this.emergencyContactAddress;
  }

  public void setEmergencyContactAddress(String emergencyContactAddress) {
    this.emergencyContactAddress = emergencyContactAddress;
  }

  public String getEmergencyContactRelationship() {
    return this.emergencyContactRelationship;
  }

  public void setEmergencyContactRelationship(String emergencyContactRelationship) {
    this.emergencyContactRelationship = emergencyContactRelationship;
  }

  public String getWorkDays() {
    return this.workDays;
  }

  public void setWorkDays(String workDays) {
    this.workDays = workDays;
  }

  public String getShiftStartTime() {
    return this.shiftStartTime;
  }

  public void setShiftStartTime(String shiftStartTime) {
    this.shiftStartTime = shiftStartTime;
  }

  public String getShiftEndTime() {
    return this.shiftEndTime;
  }

  public void setShiftEndTime(String shiftEndTime) {
    this.shiftEndTime = shiftEndTime;
  }
}