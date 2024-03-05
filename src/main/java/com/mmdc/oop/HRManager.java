package com.mmdc.oop; 

class HRManager extends Employee {
  private int hiringQuota;

  public HRManager(int employeeId, String name, Role role, int salary, int hiringQuota) {
    super(employeeId, name, role, salary);
    this.hiringQuota = hiringQuota;
  }

  public int getHiringQuota() {
    return hiringQuota;
  }

  public void evaluatePolicies() {
    System.out.println("Evaluating HR policies.");
    return;
  }
}
