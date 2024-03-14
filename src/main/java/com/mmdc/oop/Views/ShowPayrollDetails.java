package com.mmdc.oop.Views;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.mmdc.oop.DTO.RepositoriesDto;
import com.mmdc.oop.Interfaces.IView;
import com.mmdc.oop.Models.Employee;
import com.mmdc.oop.Models.Payroll;
import com.mmdc.oop.Models.User;

public class ShowPayrollDetails implements IView {

  private MultiWindowTextGUI gui;
  private BasicWindow window;
  private RepositoriesDto repositoriesDto;
  private Panel panel;

  public ShowPayrollDetails(MultiWindowTextGUI gui, Integer userId, Integer payrollId, RepositoriesDto repositoriesDto) {
    this.gui = gui;
    this.window = new BasicWindow("Payroll Details");
    this.repositoriesDto = repositoriesDto;
    this.panel = new Panel();
    this.panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    this.panel.addComponent(new Label(""));
    this.panel.addComponent(new Label(""));
    this.panel.addComponent(new Label("Payroll Details"));

    Panel payrollPanel = new Panel();
    payrollPanel.setLayoutManager(new GridLayout(2));
    Payroll payroll = repositoriesDto.getPayrollRepository().findById(payrollId);
    
    User user = repositoriesDto.getUserRepository().findById(userId);
    Employee employee = user.getEmployee();

    payrollPanel.addComponent(new Label("User:"));
    payrollPanel.addComponent(new Label(employee.getFirstName() + " " + employee.getLastName()));

    payrollPanel.addComponent(new Label("Pay Begin Date:"));
    payrollPanel.addComponent(new Label(payroll.getPayBeginDate().toString()));

    payrollPanel.addComponent(new Label("Pay End Date:"));
    payrollPanel.addComponent(new Label(payroll.getPayEndDate().toString()));

    payrollPanel.addComponent(new Label("Total Hours:"));
    payrollPanel.addComponent(new Label(payroll.getTotalHours().toString()));

    payrollPanel.addComponent(new Label("Basic Salary:"));
    payrollPanel.addComponent(new Label(payroll.getRegularPay().toString()));

    payrollPanel.addComponent(new Label("Overtime Pay:"));
    payrollPanel.addComponent(new Label(payroll.getOvertimePay().toString()));

    payrollPanel.addComponent(new Label("Special Holiday:"));
    payrollPanel.addComponent(new Label(payroll.getSpecialHoliday().toString()));

    payrollPanel.addComponent(new Label("Regular Holiday:"));
    payrollPanel.addComponent(new Label(payroll.getRegularHoliday().toString()));

    payrollPanel.addComponent(new Label("Allowance:"));
    payrollPanel.addComponent(new Label(payroll.getAllowance().toString()));

    payrollPanel.addComponent(new Label("SSS:"));
    payrollPanel.addComponent(new Label(payroll.getSss().toString()));

    payrollPanel.addComponent(new Label("Philhealth:"));
    payrollPanel.addComponent(new Label(payroll.getPhilhealth().toString()));

    payrollPanel.addComponent(new Label("Pagibig:"));
    payrollPanel.addComponent(new Label(payroll.getPagibig().toString()));

    payrollPanel.addComponent(new Label("Tax:"));
    payrollPanel.addComponent(new Label(payroll.getTax().toString()));

    payrollPanel.addComponent(new Label("Cash Advance:"));
    payrollPanel.addComponent(new Label(payroll.getCashAdvance().toString()));

    payrollPanel.addComponent(new Label("Loan:"));
    payrollPanel.addComponent(new Label(payroll.getLoan().toString()));

    payrollPanel.addComponent(new Label("Other Deductions:"));
    payrollPanel.addComponent(new Label(payroll.getOtherDeductions().toString()));

    payrollPanel.addComponent(new Label("Total Pay:"));
    payrollPanel.addComponent(new Label(payroll.getTotalPay().toString()));

    payrollPanel.addComponent(new Label("Total Deductions:"));
    payrollPanel.addComponent(new Label(payroll.getTotalDeductions().toString()));

    payrollPanel.addComponent(new Label("Net Pay:"));
    payrollPanel.addComponent(new Label(payroll.getNetPay().toString()));

    this.panel.addComponent(payrollPanel);

    this.panel.addComponent(new Label(""));
    Button closeButton = new Button("Close");
    closeButton.addListener((button) -> {
      this.window.close();
    });

    this.panel.addComponent(closeButton);

    this.window.setComponent(this.panel);
  }

  @Override
  public BasicWindow getWindow() {
    return window;
  }
  
}


/*
 * 
  @DatabaseField(generatedId = true)
  private int id;
  
  @DatabaseField(canBeNull = false)
  private String payBeginDate;

  @DatabaseField(canBeNull = false)
  private String payEndDate;

  @DatabaseField(canBeNull = false)
  private Double totalHours;

  @DatabaseField(canBeNull = false)
  private Double regularPay;

  @DatabaseField(canBeNull = false)
  private Double overtimePay;

  @DatabaseField(canBeNull = false)
  private Double specialHoliday;

  @DatabaseField(canBeNull = false)
  private Double regularHoliday;

  @DatabaseField(canBeNull = false)
  private Double allowance;

  @DatabaseField(canBeNull = false)
  private Double sss;

  @DatabaseField(canBeNull = false)
  private Double philhealth;

  @DatabaseField(canBeNull = false)
  private Double pagibig;

  @DatabaseField(canBeNull = false)
  private Double tax;

  @DatabaseField(canBeNull = false)
  private Double cashAdvance;

  @DatabaseField(canBeNull = false)
  private Double loan;

  @DatabaseField(canBeNull = false)
  private Double otherDeductions;

  @DatabaseField(canBeNull = false)
  private Double totalPay;

  @DatabaseField(canBeNull = false)
  private Double totalDeductions;

  @DatabaseField(canBeNull = false)
  private Double netPay;

  @DatabaseField(foreign = true, columnName = "user_id")
  private User user;
 */