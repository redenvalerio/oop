package com.mmdc.oop.Views;

import java.util.List;

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
import com.mmdc.oop.Models.Leave;

public class ShowLeaveRequestsView implements IView {

  private MultiWindowTextGUI gui;
  private BasicWindow window;

  public ShowLeaveRequestsView(MultiWindowTextGUI gui, Integer userId, RepositoriesDto repositoriesDto) {
    this.gui = gui;
    this.window = new BasicWindow("Leave Requests");

    Panel parent = new Panel();
    parent.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    List<Leave> leaves = repositoriesDto.getLeaveRepository().findAll().stream().filter(l -> l.getUser().getId() == userId).toList();
    parent.addComponent(new Label(""));

    Panel headerPanel = new Panel();
    headerPanel.setLayoutManager(new GridLayout(4));
    headerPanel.addComponent(new Label("Type"));
    headerPanel.addComponent(new Label("Date Start"));
    headerPanel.addComponent(new Label("Date End"));
    headerPanel.addComponent(new Label("Status"));
    parent.addComponent(headerPanel);
    for (Leave leave : leaves) {
      Panel leavePanel = new Panel();
      leavePanel.setLayoutManager(new GridLayout(4));
      leavePanel.addComponent(new Label(leave.getReason().toString()));
      leavePanel.addComponent(new Label(leave.getDateStart().toString()));
      leavePanel.addComponent(new Label(leave.getDateEnd().toString()));
      leavePanel.addComponent(new Label(leave.getStatus()));
      parent.addComponent(leavePanel);
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
