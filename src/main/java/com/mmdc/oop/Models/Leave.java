package com.mmdc.oop.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "leave")
public class Leave {
  @DatabaseField(generatedId = true)
  private int id;

  @DatabaseField(canBeNull = false)
  private String dateStart;

  @DatabaseField(canBeNull = false)
  private String dateEnd;

  @DatabaseField(canBeNull = false)
  private String reason;

  @DatabaseField(canBeNull = false, defaultValue = "Pending")
  private String status;

  @DatabaseField(foreign = true, columnName = "user_id")
  private User user;

  public Leave() {
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public User getEmployee() {
    return this.user;
  }

  public void setEmployee(User user) {
    this.user = user;
  }

  public String getDateStart() {
    return this.dateStart;
  }

  public void setDateStart(String dateStart) {
    this.dateStart = dateStart;
  }

  public String getDateEnd() {
    return this.dateEnd;
  }

  public void setDateEnd(String dateEnd) {
    this.dateEnd = dateEnd;
  }

  public String getReason() {
    return this.reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
