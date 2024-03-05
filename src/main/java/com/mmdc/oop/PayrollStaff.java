package com.mmdc.oop;

class PayrollStaff extends Employee {
  private int managedPayrollCount;

  public PayrollStaff(int employeeId, String name, Role role, int salary, int managedPayrollCount) {
    super(employeeId, name, role, salary);
    this.managedPayrollCount = managedPayrollCount;
  }

  public int getManagedPayrollCount() {
    return managedPayrollCount;
  }

  public void processPayroll() {
    System.out.println("Processing payroll for employees.");
    return;
  }

  public void updatePayroll() {
    System.out.println("Updating payroll for employees.");
    return;
  }
}
