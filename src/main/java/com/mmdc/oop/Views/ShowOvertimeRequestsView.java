package com.mmdc.oop.Views;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.googlecode.lanterna.TerminalSize;
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
import com.mmdc.oop.Models.Overtime;

public class ShowOvertimeRequestsView implements IView {

  private BasicWindow window;

  public ShowOvertimeRequestsView(MultiWindowTextGUI gui, Integer userId, RepositoriesDto repositoriesDto) {
    this.window = new BasicWindow("Overtime Requests");

    List<Overtime> overtimes = repositoriesDto.getOvertimeRepository().findAll().stream()
        .filter(o -> o.getUser().getId() == userId).toList();

    Panel parent = new Panel();
    parent.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    for (Overtime overtime : overtimes) {
      Panel overtimePanel = new Panel();
      overtimePanel.setLayoutManager(new GridLayout(5));
      // double totalHours = 0;
      // String timeIn = attendance.getTimeIn();
      // String timeOut = attendance.getTimeOut();
      // String[] timeInArr = timeIn.split(":");
      // String[] timeOutArr = timeOut.split(":");
      // int timeInHour = Integer.parseInt(timeInArr[0]);
      // int timeInMinute = Integer.parseInt(timeInArr[1]);
      // int timeOutHour = Integer.parseInt(timeOutArr[0]);
      // int timeOutMinute = Integer.parseInt(timeOutArr[1]);
      // int hours = timeOutHour - timeInHour;
      // int minutes = timeOutMinute - timeInMinute;
      // totalHours += hours + (minutes / 60);

      double totalHours = 0;
      String timeStart = overtime.getTimeStart();
      String timeEnd = overtime.getTimeEnd();
      try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime start = LocalTime.parse(timeStart, formatter);
        LocalTime end = LocalTime.parse(timeEnd, formatter);
        totalHours = Duration.between(start, end).toMinutes() / 60.0;
      } catch (Exception e) {
        e.printStackTrace();
      }

      overtimePanel.addComponent(new Label(overtime.getDate()));
      overtimePanel.addComponent(new Label(String.format("%.2fh", totalHours)));
      overtimePanel.addComponent(new Label(overtime.getTimeStart()));
      overtimePanel.addComponent(new Label(overtime.getTimeEnd()));
      overtimePanel.addComponent(new Label(overtime.getStatus()));
      parent.addComponent(overtimePanel);
    }

    Button closeButton = new Button("Close");
    closeButton.addListener((button) -> {
      this.window.close();
    });
    parent.addComponent(closeButton);

    this.window.setComponent(parent);
  }

  @Override
  public BasicWindow getWindow() {
    return window;
  }

}
