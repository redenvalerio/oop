package com.mmdc.oop.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "attendance")
public class Attendance {
  @DatabaseField(generatedId = true)
  private int id;

  @DatabaseField(canBeNull = false)
  private String dateIn;

  @DatabaseField(canBeNull = true)
  private String timeIn;

  @DatabaseField(canBeNull = true)
  private String dateOut;

  @DatabaseField(canBeNull = true)
  private String timeOut;

  @DatabaseField(foreign = true, columnName = "user_id")
  private User user;

  public Attendance() {
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDateIn() {
    return this.dateIn;
  }

  public void setDateIn(String dateIn) {
    this.dateIn = dateIn;
  }

  public String getTimeIn() {
    return this.timeIn;
  }

  public void setTimeIn(String timeIn) {
    this.timeIn = timeIn;
  }

  public String getDateOut() {
    return this.dateOut;
  }

  public void setDateOut(String dateOut) {
    this.dateOut = dateOut;
  }

  public String getTimeOut() {
    return this.timeOut;
  }

  public void setTimeOut(String timeOut) {
    this.timeOut = timeOut;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}