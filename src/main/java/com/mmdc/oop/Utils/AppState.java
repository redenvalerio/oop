package com.mmdc.oop.Utils;

import java.time.LocalDate;

import com.mmdc.oop.Models.Employee;
import com.mmdc.oop.Models.Role;
import com.mmdc.oop.Models.User;

public class AppState {
  public static User currentUser = null; 
  public static Employee currentEmployee = null;
  public static Role currentRole = null;

  // Users, Profile, Attendance, Payroll
  public static String currentView = "Users";
  public static LocalDate currentCalendarDate = LocalDate.now();
  public static LocalDate selectedPayBeginDate = null;
  public static LocalDate selectedPayEndDate = null;
}
