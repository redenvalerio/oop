package com.mmdc.oop;

class Employee {

  private Role role;

  private int employeeId;
  private String department;
  private int salary;
  private String name;

  public Employee(int employeeId, String name, Role role, int salary) {
    this.employeeId = employeeId;
    this.name = name;
    this.salary = salary;
    this.role = role;
  }

  public int getEmployeeId() {
    return employeeId;
  }

  public String getDepartment() {
    return department;
  }

  public int getSalary() {
    return salary;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public String toString() {
    return "Employee{id="+employeeId+",name="+name+",role="+role+",salary="+salary+"}";
  }
  
}
