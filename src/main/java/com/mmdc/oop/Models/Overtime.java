package com.mmdc.oop.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "overtime")
public class Overtime {
  //id, attendance_id, date, time_start, time_end, status

  @DatabaseField(generatedId = true)
  private int id;

  @DatabaseField(canBeNull = false)
  private String date;

  @DatabaseField(canBeNull = false)
  private String timeStart;

  @DatabaseField(canBeNull = false)
  private String timeEnd;

  @DatabaseField(canBeNull = false, defaultValue = "Pending")
  private String status;

  @DatabaseField(foreign = true, columnName = "attendance_id")
  private Attendance attendance;

  @DatabaseField(foreign = true, columnName = "user_id")
  private User user;

  public Overtime() {
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Attendance getAttendance() {
    return this.attendance;
  }

  public void setAttendance(Attendance attendance) {
    this.attendance = attendance;
  }

  public String getDate() {
    return this.date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getTimeStart() {
    return this.timeStart;
  }

  public void setTimeStart(String timeStart) {
    this.timeStart = timeStart;
  }

  public String getTimeEnd() {
    return this.timeEnd;
  }

  public void setTimeEnd(String timeEnd) {
    this.timeEnd = timeEnd;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
