package com.mmdc.oop.DTO;

import com.mmdc.oop.Repositories.AttendanceRepository;
import com.mmdc.oop.Repositories.EmployeeRepository;
import com.mmdc.oop.Repositories.LeaveRepository;
import com.mmdc.oop.Repositories.OvertimeRepository;
import com.mmdc.oop.Repositories.RoleRepository;
import com.mmdc.oop.Repositories.UserRepository;
import com.mmdc.oop.Repositories.UserRoleRepository;

public class RepositoriesDto {
   private UserRepository userRepository;
   private RoleRepository roleRepository;
   private UserRoleRepository userRoleRepository;
   private EmployeeRepository employeeRepository;
   private AttendanceRepository attendanceRepository;
   private OvertimeRepository overtimeRepository;
   private LeaveRepository leaveRepository;

   public RepositoriesDto(UserRepository userRepository, RoleRepository roleRepository,
      UserRoleRepository userRoleRepository, EmployeeRepository employeeRepository,
      AttendanceRepository attendanceRepository, OvertimeRepository overtimeRepository,
      LeaveRepository leaveRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.userRoleRepository = userRoleRepository;
    this.employeeRepository = employeeRepository;
    this.attendanceRepository = attendanceRepository;
    this.overtimeRepository = overtimeRepository;
    this.leaveRepository = leaveRepository;
  }


  public AttendanceRepository getAttendanceRepository() {
    return this.attendanceRepository;
  }

  public void setAttendanceRepository(AttendanceRepository attendanceRepository) {
    this.attendanceRepository = attendanceRepository;
  }

  public OvertimeRepository getOvertimeRepository() {
    return this.overtimeRepository;
  }

  public void setOvertimeRepository(OvertimeRepository overtimeRepository) {
    this.overtimeRepository = overtimeRepository;
  }

  public LeaveRepository getLeaveRepository() {
    return this.leaveRepository;
  }

  public void setLeaveRepository(LeaveRepository leaveRepository) {
    this.leaveRepository = leaveRepository;
  }

  public UserRepository getUserRepository() {
    return this.userRepository;
  }

  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public RoleRepository getRoleRepository() {
    return this.roleRepository;
  }

  public void setRoleRepository(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  public UserRoleRepository getUserRoleRepository() {
    return this.userRoleRepository;
  }

  public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
    this.userRoleRepository = userRoleRepository;
  }

  public EmployeeRepository getEmployeeRepository() {
    return this.employeeRepository;
  }

  public void setEmployeeRepository(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }
}