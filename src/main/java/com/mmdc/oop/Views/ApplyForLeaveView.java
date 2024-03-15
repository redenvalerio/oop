package com.mmdc.oop.Views;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.CheckBox;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.mmdc.oop.App;
import com.mmdc.oop.DTO.RepositoriesDto;
import com.mmdc.oop.Interfaces.IView;
import com.mmdc.oop.Models.Leave;
import com.mmdc.oop.Models.User;
import com.mmdc.oop.Utils.AppState;

public class ApplyForLeaveView implements IView {

  private BasicWindow window;
  private MultiWindowTextGUI gui;
  private Panel panel;

  public ApplyForLeaveView(MultiWindowTextGUI gui, Integer userId, RepositoriesDto repositoriesDto) {
    this.gui = gui;
    this.window = new BasicWindow("Apply for Leave");
    this.panel = new Panel();

    this.panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    Panel headerPanel = new Panel();
    headerPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    this.panel.addComponent(headerPanel);

    Panel mainPanel = new Panel();
    mainPanel.setLayoutManager(new GridLayout(2));

    mainPanel.addComponent(new Label("Leave Type:"));

    Panel leaveTypePanel = new Panel();
    leaveTypePanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

    List<CheckBox> leaveTypeCheckBoxList = List.of(new CheckBox("Sick Leave"), new CheckBox("Vacation Leave"), new CheckBox("Maternity Leave"), new CheckBox("RTO"), new CheckBox("PTO"));

    for (CheckBox checkBox : leaveTypeCheckBoxList) {
      checkBox.addListener((e) -> {
        if(e) {
          leaveTypeCheckBoxList.stream().filter(c -> c != checkBox).forEach(c -> c.setChecked(false));
        }
      });
      leaveTypePanel.addComponent(checkBox);
    }

    this.panel.addComponent(mainPanel);
    this.panel.addComponent(leaveTypePanel);

    Panel parentPanel = new Panel();
    parentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    Panel calendarPanel = new Panel();
    calendarPanel.setLayoutManager(new GridLayout(7)); // 7 columns for days of the week

    String[] dayNames = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
    for (String dayName : dayNames) {
      calendarPanel.addComponent(new Label(dayName));
    }


    Button backButton = new Button("Back");
    Button nextButton = new Button("Next");
    backButton.addListener(e -> {
      AppState.currentLeaveCalendarDate = AppState.currentLeaveCalendarDate.minusMonths(1);
            gui.removeWindow(gui.getActiveWindow());
            ApplyForLeaveView applyForLeaveView = new ApplyForLeaveView(gui, userId, repositoriesDto);
            gui.addWindowAndWait(applyForLeaveView.getWindow());
    });

    nextButton.addListener(e -> {
      AppState.currentLeaveCalendarDate = AppState.currentLeaveCalendarDate.plusMonths(1);
            gui.removeWindow(gui.getActiveWindow());
            ApplyForLeaveView applyForLeaveView = new ApplyForLeaveView(gui, userId, repositoriesDto);
            gui.addWindowAndWait(applyForLeaveView.getWindow());
    });

    Panel actionPanel = new Panel();
    actionPanel.setLayoutManager(new GridLayout(3));
    actionPanel.addComponent(backButton);
    actionPanel.addComponent(new Label(AppState.currentLeaveCalendarDate.getMonth().toString() + " "
        + AppState.currentLeaveCalendarDate.getYear()));
    actionPanel.addComponent(nextButton);

    parentPanel.addComponent(actionPanel);

    LocalDate currentMonth = AppState.currentLeaveCalendarDate;
    LocalDate firstOfMonth = currentMonth.withDayOfMonth(1);
    LocalDate lastOfMonth = currentMonth.withDayOfMonth(currentMonth.lengthOfMonth());

    // Find out what day of the week the first of the month falls on
    DayOfWeek startDay = firstOfMonth.getDayOfWeek();
    int value = startDay.getValue(); // 1 = Monday, 7 = Sunday
    value = value % 7; // Adjust to start from Sunday

    for (int i = 0; i < value; i++) {
      calendarPanel.addComponent(new EmptySpace());
    }

    // if day of the month is before the current day, disable the checkbox
    for (int day = 1; day <= lastOfMonth.getDayOfMonth(); day++) {
      CheckBox dayCheckBox = new CheckBox(String.valueOf(day));
      // set the dayCheckbox to true if within the selectedPayBeginDate and selectedPayEndDate
      if(AppState.selectedLeaveBeginDate != null && AppState.selectedLeaveEndDate != null) {
        if (LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day).isAfter(AppState.selectedLeaveBeginDate)
            && (LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day).isBefore(AppState.selectedLeaveEndDate) || 
            LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day).isEqual(AppState.selectedLeaveEndDate)
            )) { 
          dayCheckBox.setChecked(true);
        }
      }

      if(AppState.selectedLeaveBeginDate != null) {
        if (LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day).isEqual(AppState.selectedLeaveBeginDate)) {
          dayCheckBox.setChecked(true);
        }
      }

      dayCheckBox.addListener((e) -> {
        if (e) {
          final int selectedDay = Integer.parseInt(dayCheckBox.getLabel());
          if (AppState.selectedLeaveBeginDate != null && AppState.selectedLeaveEndDate != null) {
            AppState.selectedLeaveBeginDate = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), selectedDay);
            AppState.selectedLeaveEndDate = null;
            gui.removeWindow(gui.getActiveWindow());
            ApplyForLeaveView applyForLeaveView = new ApplyForLeaveView(gui, userId, repositoriesDto);
            gui.addWindowAndWait(applyForLeaveView.getWindow());
            return;
          }

          if (AppState.selectedLeaveBeginDate == null) {
            AppState.selectedLeaveBeginDate = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), selectedDay);
            gui.removeWindow(gui.getActiveWindow());
            ApplyForLeaveView applyForLeaveView = new ApplyForLeaveView(gui, userId, repositoriesDto);
            gui.addWindowAndWait(applyForLeaveView.getWindow());
            return;
          } else if (AppState.selectedLeaveEndDate == null) {
            AppState.selectedLeaveEndDate = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), selectedDay);
            gui.removeWindow(gui.getActiveWindow());
            ApplyForLeaveView applyForLeaveView = new ApplyForLeaveView(gui, userId, repositoriesDto);
            gui.addWindowAndWait(applyForLeaveView.getWindow());
            return;
          }
        }
      });

      if(LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day).isBefore(LocalDate.now())) {
        dayCheckBox.setEnabled(false);
      }

      calendarPanel.addComponent(dayCheckBox);
    }

    parentPanel.addComponent(calendarPanel);
    this.panel.addComponent(parentPanel);

    Button closeButton = new Button("Close");
    closeButton.addListener((button) -> {
      this.window.close();
    });

    Panel footerPanel = new Panel();
    footerPanel.setLayoutManager(new GridLayout(2));
    // total days is number of selected checkbox
    footerPanel.addComponent(new Label("Total Day/s: " + (AppState.selectedLeaveEndDate != null ? AppState.selectedLeaveEndDate.getDayOfMonth() - AppState.selectedLeaveBeginDate.getDayOfMonth() + 1 : 0)));
    footerPanel.addComponent(new Label(""));

    Button submitButton = new Button("Submit");
    submitButton.addListener((button) -> {
      if(AppState.selectedLeaveBeginDate == null || AppState.selectedLeaveEndDate == null) {
        MessageDialog.showMessageDialog(gui, "Error", "Please select a date range");
        return;
      }

      // check if leave type is selected
      if(leaveTypeCheckBoxList.stream().noneMatch(checkBox -> checkBox.isChecked())) {
        MessageDialog.showMessageDialog(gui, "Error", "Please select a leave type");
        return;
      }

      String leaveType = "";
      for (CheckBox checkBox : leaveTypeCheckBoxList) {
        if (checkBox.isChecked()) {
          leaveType = checkBox.getLabel();
          break;
        }
      }

      User user = repositoriesDto.getUserRepository().findById(userId);

      // save leave
      Leave leave = new Leave();
      leave.setReason(leaveType);
      leave.setDateStart(AppState.selectedLeaveBeginDate.toString());
      leave.setDateEnd(AppState.selectedLeaveEndDate.toString());
      leave.setStatus("Pending");
      leave.setUser(user);
      repositoriesDto.getLeaveRepository().save(leave);

      MessageDialog.showMessageDialog(gui, "Success", "Leave application submitted");
      AppState.selectedLeaveBeginDate = null;
      AppState.selectedLeaveEndDate = null;
      AppState.currentLeaveCalendarDate = LocalDate.now();
      gui.removeWindow(gui.getActiveWindow());
    });

    footerPanel.addComponent(submitButton);
    this.panel.addComponent(footerPanel);
    this.panel.addComponent(closeButton);

    this.window.setComponent(this.panel);
  }

  @Override
  public BasicWindow getWindow() {
    return window;
  }
  
}
