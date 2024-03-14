package com.mmdc.oop.Views;

import java.util.Collections;
import java.util.List;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.mmdc.oop.DTO.RepositoriesDto;
import com.mmdc.oop.Interfaces.IView;
import com.mmdc.oop.Models.Payroll;
import com.mmdc.oop.Models.User;

public class ShowPayrollReport implements IView {
  private MultiWindowTextGUI gui;
  private BasicWindow window;
  private RepositoriesDto repositoriesDto;
  private Panel panel;

  public ShowPayrollReport(MultiWindowTextGUI gui, Integer userId, RepositoriesDto repositoriesDto) {
    this.gui = gui;
    this.window = new BasicWindow("Payroll Report");
    this.repositoriesDto = repositoriesDto;
    this.panel = new Panel();
    this.panel.setLayoutManager(new GridLayout(1));

    Panel headerPanel = new Panel();
    headerPanel.setLayoutManager(new GridLayout(2));

    Panel listPanel = new Panel();
    listPanel.setLayoutManager(new GridLayout(2));

    // sort by id
    List<Payroll> payrolls = repositoriesDto.getPayrollRepository().findAll().stream().filter(p -> p.getUser().getId() == userId).toList();

    for (Payroll payroll : payrolls) {
      Button button = new Button("View");
      button.addListener((button1) -> {
        try {
          gui.addWindowAndWait(new ShowPayrollDetails(gui, payroll.getUser().getId(), payroll.getId(), repositoriesDto).getWindow());
        } catch (Exception e) {
          e.printStackTrace();
          MessageDialog.showMessageDialog(gui, "Error", e.getMessage());
        }
      });
      listPanel.addComponent(button);
      listPanel.addComponent(new Label("<"+payroll.getPayBeginDate() + "> - <" + payroll.getPayEndDate() + ">"));
    }


    Panel footerPanel = new Panel();
    footerPanel.setLayoutManager(new GridLayout(1));

    Button closeButton = new Button("Close");
    closeButton.addListener((button) -> {
      this.window.close();
    });

    footerPanel.addComponent(closeButton);

    this.panel.addComponent(headerPanel);
    this.panel.addComponent(listPanel);
    this.panel.addComponent(footerPanel);

    this.window.setComponent(this.panel);
  }

  @Override
  public BasicWindow getWindow() {
    return window;
  }
  
}
