package com.mmdc.oop.Views;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
import com.mmdc.oop.DTO.RepositoriesDto;
import com.mmdc.oop.Interfaces.IView;
import com.mmdc.oop.Models.Attendance;
import com.mmdc.oop.Models.Overtime;
import com.mmdc.oop.Models.User;
import com.mmdc.oop.Utils.AppState;

public class ApplyForOvertimeView implements IView {

  private BasicWindow window;
  private MultiWindowTextGUI gui;

  public ApplyForOvertimeView(MultiWindowTextGUI gui, Integer userId, RepositoriesDto repositoriesDto) {
    this.window = new BasicWindow("Apply for Overtime");
    this.gui = gui;

    Panel panel = new Panel();

    Panel calendarPanel = new Panel();
    calendarPanel.setLayoutManager(new GridLayout(7)); // 7 columns for days of the week

    String[] dayNames = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
    for (String dayName : dayNames) {
      calendarPanel.addComponent(new Label(dayName));
    }


    Button backButton = new Button("Back");
    Button nextButton = new Button("Next");
    backButton.addListener(e -> {
      AppState.currentOvertimeCalendarDate = AppState.currentOvertimeCalendarDate.minusMonths(1);
            gui.removeWindow(gui.getActiveWindow());
            ApplyForOvertimeView applyForOvertimeView = new ApplyForOvertimeView(gui, userId, repositoriesDto);
            gui.addWindowAndWait(applyForOvertimeView.getWindow());
    });

    nextButton.addListener(e -> {
      AppState.currentOvertimeCalendarDate = AppState.currentOvertimeCalendarDate.plusMonths(1);
            gui.removeWindow(gui.getActiveWindow());
            ApplyForOvertimeView applyForOvertimeView = new ApplyForOvertimeView(gui, userId, repositoriesDto);
            gui.addWindowAndWait(applyForOvertimeView.getWindow());
    });

    Panel actionPanel = new Panel();
    actionPanel.setLayoutManager(new GridLayout(3));
    actionPanel.addComponent(backButton);
    actionPanel.addComponent(new Label(AppState.currentOvertimeCalendarDate.getMonth().toString() + " "
        + AppState.currentOvertimeCalendarDate.getYear()));
    actionPanel.addComponent(nextButton);

    panel.addComponent(actionPanel);

    LocalDate currentMonth = AppState.currentOvertimeCalendarDate;
    LocalDate firstOfMonth = currentMonth.withDayOfMonth(1);
    LocalDate lastOfMonth = currentMonth.withDayOfMonth(currentMonth.lengthOfMonth());

    // Find out what day of the week the first of the month falls on
    DayOfWeek startDay = firstOfMonth.getDayOfWeek();
    int value = startDay.getValue(); // 1 = Monday, 7 = Sunday
    value = value % 7; // Adjust to start from Sunday

    for (int i = 0; i < value; i++) {
      calendarPanel.addComponent(new EmptySpace());
    }

    List<Attendance> attendances = repositoriesDto.getAttendanceRepository().findAll().stream().filter(a -> a.getUser().getId() == userId).toList();

    List<CheckBox> checkBoxList = new ArrayList<>();

    // if day of the month is before the current day, disable the checkbox
    // check if there's attendance for the day, if not, disable the checkbox
    for (int day = 1; day <= lastOfMonth.getDayOfMonth(); day++) {
      CheckBox dayCheckBox = new CheckBox(String.valueOf(day));

      final int dayFinal = day;
      // take into account also the month of the attendance when setting the checkbox to disable or enabled
      if(attendances.stream().noneMatch(a -> LocalDate.parse(a.getDateIn()).getDayOfMonth() == dayFinal && LocalDate.parse(a.getDateIn()).getMonthValue() == currentMonth.getMonthValue())) {
        dayCheckBox.setEnabled(false);
      }

      if(AppState.selectedOvertimeDate != null) {
        if (LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day).isEqual(AppState.selectedOvertimeDate)) {
          dayCheckBox.setChecked(true);
        } else {
          dayCheckBox.setChecked(false);
        }
      }
      checkBoxList.add(dayCheckBox);
      calendarPanel.addComponent(dayCheckBox);
    }
    
    // loop thorugh all checkbox add listener and when checked, uncheck other
    for (CheckBox checkBox : checkBoxList) {
      checkBox.addListener(e -> {
        if(checkBox.isChecked()) {
          for (CheckBox cb : checkBoxList) {
            if(cb != checkBox) {
              cb.setChecked(false);
            }
          }
          AppState.selectedOvertimeDate = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), Integer.parseInt(checkBox.getLabel()));
        }
      });
    }

    panel.addComponent(calendarPanel);

    Panel footer = new Panel();
    footer.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
    Button submit = new Button("Submit");
    submit.addListener(e -> {
      if(AppState.selectedOvertimeDate == null) {
        MessageDialog.showMessageDialog(gui, "Error", "Please select a date");
        return;
      }

      if(repositoriesDto.getOvertimeRepository().findAll().stream().anyMatch(o -> o.getUser().getId() == userId && o.getDate().equals(AppState.selectedOvertimeDate.toString()))) {
        MessageDialog.showMessageDialog(gui, "Error", "Overtime already applied for the selected date");
        return;
      }

      User user = repositoriesDto.getUserRepository().findById(userId);
      Overtime overtime = new Overtime();
      Attendance attendance = repositoriesDto.getAttendanceRepository().findAll().stream().filter(a -> a.getUser().getId() == userId).filter(a -> LocalDate.parse(a.getDateIn()).isEqual(AppState.selectedOvertimeDate)).findFirst().get();

      if(attendance == null) {
        MessageDialog.showMessageDialog(gui, "Error", "No attendance found for the selected date");
        return;
      }
      overtime.setAttendance(attendance);
      
      overtime.setStatus("Pending"); 
      overtime.setDate(AppState.selectedOvertimeDate.toString());

      // get user shift, and calculate the overtime based on the shift end time and attendance
      // check if shiftEndTime is less than attendance time in, if so, set ot time start to attendance time in
      // format is HH:mm
      // convert first to time
      String shiftEndTime = user.getEmployee().getShiftEndTime();
      String attendanceTimeIn = attendance.getTimeIn();


      LocalTime shiftEndTimeLocalTime = LocalTime.parse(shiftEndTime);
      LocalTime attendanceTimeInLocalTime = LocalTime.parse(attendanceTimeIn);

      if(shiftEndTimeLocalTime.isAfter(attendanceTimeInLocalTime)) {
        overtime.setTimeStart(shiftEndTime);
      } else {
        overtime.setTimeStart(attendanceTimeIn);
      }

      //double totalHours = 0;
      //String timeIn = attendance.getTimeIn();
      //String timeOut = attendance.getTimeOut();
      //String[] timeInArr = timeIn.split(":");
      //String[] timeOutArr = timeOut.split(":");
      //int timeInHour = Integer.parseInt(timeInArr[0]);
      //int timeInMinute = Integer.parseInt(timeInArr[1]);
      //int timeOutHour = Integer.parseInt(timeOutArr[0]);
      //int timeOutMinute = Integer.parseInt(timeOutArr[1]);
      //int hours = timeOutHour - timeInHour;
      //int minutes = timeOutMinute - timeInMinute;
      //totalHours += hours + (minutes / 60);

      LocalTime timeOut = LocalTime.parse(attendance.getTimeOut());
      
      // check if timeout is greated that shiftendtime and also check if its greater than the timein
      // get the overtime

      if(timeOut.isAfter(shiftEndTimeLocalTime) && timeOut.isAfter(attendanceTimeInLocalTime)) {
        LocalTime otTimeEnd = timeOut;
        overtime.setTimeEnd(otTimeEnd.toString());
      } else {
        MessageDialog.showMessageDialog(gui, "Error", "No overtime found for the selected date");
        return;
      }

      overtime.setUser(user);
      repositoriesDto.getOvertimeRepository().save(overtime);

      MessageDialog.showMessageDialog(gui, "Success", "Overtime application submitted");
    });

    Button close = new Button("Close");
    close.addListener(e -> {
      this.window.close();
    });

    footer.addComponent(submit);
    footer.addComponent(close);

    panel.addComponent(footer);

    this.window.setComponent(panel);
  }

  @Override
  public BasicWindow getWindow() {
    return window;
  }
  
}
