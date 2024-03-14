package com.mmdc.oop.Views;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.CheckBox;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.mmdc.oop.DTO.RepositoriesDto;
import com.mmdc.oop.Interfaces.IView;
import com.mmdc.oop.Models.Attendance;
import com.mmdc.oop.Models.Employee;
import com.mmdc.oop.Models.Payroll;
import com.mmdc.oop.Models.User;
import com.mmdc.oop.Utils.AppState;
import com.mmdc.oop.Utils.PayrollUtils;

public class GeneratePayrollView implements IView {

  private MultiWindowTextGUI gui;
  private BasicWindow window;
  private RepositoriesDto repositoriesDto;
  private Panel panel;

  public GeneratePayrollView(MultiWindowTextGUI gui, RepositoriesDto repositoriesDto) {
    this.gui = gui;
    this.window = new BasicWindow("Generate Payroll");
    this.repositoriesDto = repositoriesDto;
    this.panel = new Panel();

    this.panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    this.panel.addComponent(new Label("Select Users"));

    Panel userPanel = new Panel();
    userPanel.setLayoutManager(new GridLayout(3));

    // checkboxlist for users to select
    List<CheckBox> checkBoxList = new ArrayList<>();
    List<User> users = this.repositoriesDto.getUserRepository().findAll();

    for (User user : users) {
      CheckBox checkBox = new CheckBox(user.getId() + "");
      checkBoxList.add(checkBox);
      userPanel.addComponent(checkBox);
      userPanel.addComponent(new Label(user.getEmployee().getFirstName()));
      userPanel.addComponent(new Label(user.getEmployee().getLastName()));
    }

    Panel footerPanel = new Panel();
    footerPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
    Button generateButton = new Button("Generate");
    generateButton.addListener((button) -> {

      if (checkBoxList.stream().noneMatch(checkBox -> checkBox.isChecked())) {
        MessageDialog.showMessageDialog(gui, "Error", "Please select at least one user");
        return;
      }
      // get selected users
      List<User> selectedUsers = new ArrayList<>();
      for (CheckBox checkBox : checkBoxList) {
        if (checkBox.isChecked()) {
          int userId = Integer.parseInt(checkBox.getLabel().toString());
          User user = this.repositoriesDto.getUserRepository().findById(userId);
          selectedUsers.add(user);
        }
      }

      // generate payroll
      // for each user, get attendance, calculate total hours, and save payroll
      LocalDate selectedPayBeginDate = AppState.selectedPayBeginDate;
      LocalDate selectedPayEndDate = AppState.selectedPayEndDate;

      if (selectedPayBeginDate == null || selectedPayEndDate == null) {
        MessageDialog.showMessageDialog(gui, "Error", "Please select a date range");
        return;
      }

      // filter to get attendance within the selected date range of attendance
      // getDateIn() and getDateOut()
      for (User user : selectedUsers) {
        List<Attendance> attendances = this.repositoriesDto.getAttendanceRepository().findAll().stream()
            .filter(attendance -> attendance.getUser().getId() == user.getId()).toList().stream().filter(attendance -> {
              LocalDate attendanceDate = LocalDate.parse(attendance.getDateIn());
              return attendanceDate.isAfter(selectedPayBeginDate)
                  && (attendanceDate.isBefore(selectedPayEndDate) || attendanceDate.isEqual(selectedPayEndDate));
            }).toList();

        double totalHours = 0;

        for (Attendance attendance : attendances) {
          String timeIn = attendance.getTimeIn();
          String timeOut = attendance.getTimeOut();
          String[] timeInArr = timeIn.split(":");
          String[] timeOutArr = timeOut.split(":");
          int timeInHour = Integer.parseInt(timeInArr[0]);
          int timeInMinute = Integer.parseInt(timeInArr[1]);
          int timeOutHour = Integer.parseInt(timeOutArr[0]);
          int timeOutMinute = Integer.parseInt(timeOutArr[1]);
          int hours = timeOutHour - timeInHour;
          int minutes = timeOutMinute - timeInMinute;
          totalHours += hours + (minutes / 60);
        }

        Payroll payroll = new Payroll();

        // setPayBeginDate and setPayEndDate as string
        payroll.setPayBeginDate(selectedPayBeginDate.toString());
        payroll.setPayEndDate(selectedPayEndDate.toString());
        payroll.setTotalHours(totalHours);

        Employee emp = user.getEmployee();
        double regularPay = totalHours * emp.getHourlyRate();
        payroll.setRegularPay(regularPay);

        // get set overtime
        double overtimePay = 0;
        if (totalHours > 8) {
          overtimePay = PayrollUtils.computeOvertimePay(totalHours, emp.getHourlyRate());
        }
        payroll.setOvertimePay(overtimePay);

        double sss = 0;
        if (emp.getSSS() != null) {
          sss = PayrollUtils.computeSSS(regularPay);
        }
        payroll.setSss(sss);

        double philHealth = 0;
        if (emp.getPhilHealth() != null) {
          philHealth = PayrollUtils.computePhilhealth(regularPay);
        }
        payroll.setPhilhealth(philHealth);

        double PAGIBIG = 0;
        if (emp.getPAGIBIG() != null) {
          PAGIBIG = PayrollUtils.computePAGIBIG(regularPay);
        }
        payroll.setPagibig(PAGIBIG);

        double tax = 0;
        tax = PayrollUtils.computeTax(regularPay, sss, philHealth, PAGIBIG);
        payroll.setTax(tax);

        double totalDeductions = sss + philHealth + PAGIBIG + tax;
        payroll.setTotalDeductions(totalDeductions);

        double netPay = regularPay - totalDeductions;
        payroll.setTotalPay(regularPay);
        payroll.setNetPay(netPay);

        payroll.setUser(user);

        payroll.setAllowance(0.00);
        payroll.setCashAdvance(0.00);

        payroll.setSpecialHoliday(0.00);
        payroll.setRegularHoliday(0.00);
        payroll.setLoan(0.00);
        payroll.setOtherDeductions(0.00);
        repositoriesDto.getPayrollRepository().save(payroll);
      }

      MessageDialog.showMessageDialog(gui, "Success", "Payroll generated successfully");
      this.window.close();
    });
    Button cancelButton = new Button("Cancel");
    cancelButton.addListener((button) -> {
      this.window.close();
    });

    footerPanel.addComponent(generateButton);
    footerPanel.addComponent(cancelButton);

    this.panel.addComponent(userPanel);
    this.panel.addComponent(footerPanel);

    this.window.setComponent(this.panel);
  }

  @Override
  public BasicWindow getWindow() {
    return this.window;
  }

}
