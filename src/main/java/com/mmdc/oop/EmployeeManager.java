package com.mmdc.oop;

class EmployeeManager extends Employee {
  private int teamSize;
  private String project;

  public EmployeeManager(int employeeId, String name, Role role, int salary, int teamSize, String project) {
    super(employeeId, name, role, salary);
    this.teamSize = teamSize;
    this.project = project;
  }

  public int getTeamSize() {
    return teamSize;
  }

  public String getProject() {
    return project;
  }

  public void assignTask() {
    System.out.println("Assigning tasks to employees.");
    return;
  }

  public void evaluatePerformance() {
    System.out.println("Evaluating performance of employees.");
    return;
  }
}
