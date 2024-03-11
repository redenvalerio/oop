package com.mmdc.oop.Views;

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
import com.googlecode.lanterna.gui2.TextBox;
import com.mmdc.oop.Interfaces.IView;
import com.mmdc.oop.Models.User;
import com.mmdc.oop.Repositories.UserRepository;

public class DashboardView implements IView {
  private Panel panel;
  private BasicWindow window;
  private UserRepository userRepository;
  private MultiWindowTextGUI gui;
  public DashboardView(MultiWindowTextGUI gui, UserRepository userRepository) {
    this.gui = gui;
    this.panel = new Panel();
    this.userRepository = userRepository;

    panel.setLayoutManager(new GridLayout(1));
    panel.addComponent(getUserManagement());

    this.window = new BasicWindow("Dashboard");

    window.setComponent(panel);
  }

  public BasicWindow getWindow() {
    return window;
  }

  public Panel getPanel() {
    return panel;
  }

  public Panel getUserManagement() {
    Panel parent = new Panel();
    parent.setLayoutManager(new LinearLayout(Direction.VERTICAL));
    Label spacer = new Label("");
    Label label = new Label("User Management");
    parent.addComponent(spacer);
    parent.addComponent(label);

    Panel userManagementPanel = new Panel();
    userManagementPanel.setLayoutManager(new GridLayout(3));
    userManagementPanel.addComponent(new Label("ID"));
    userManagementPanel.addComponent(new Label("Username"));
    userManagementPanel.addComponent(new Label("Password"));

    List<User> users = userRepository.findAll();

    List<CheckBox> checkBoxList = new ArrayList<>();

    for (User user : users) {
      checkBoxList.add(new CheckBox(user.getId() + ""));
      userManagementPanel.addComponent(checkBoxList.get(users.indexOf(user)));
      userManagementPanel.addComponent(new Label(user.getUsername()));
      userManagementPanel.addComponent(new Label(user.getPassword()));
    }

    checkBoxList.forEach(checkBox -> checkBox.addListener(e -> {
      if(e) {
        checkBoxList.forEach(cb -> {
          if(cb != checkBox) {
            cb.setChecked(false);
          }
        });
      }
    }));
    parent.addComponent(userManagementPanel);

    BasicWindow addUserWindow = new BasicWindow("Add User");
    Panel addUserPanel = new Panel();
    addUserPanel.setLayoutManager(new GridLayout(2));
    addUserPanel.addComponent(new Label("Username:"));
    addUserPanel.addComponent(new TextBox());
    addUserPanel.addComponent(new Label("Password:"));
    addUserPanel.addComponent(new TextBox().setMask('*'));
    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");
    cancelButton.addListener(e -> {
      gui.removeWindow(addUserWindow);
    });
    addUserPanel.addComponent(saveButton);
    addUserPanel.addComponent(cancelButton);
    addUserWindow.setComponent(addUserPanel);

    BasicWindow editUserWindow = new BasicWindow("Edit User");
    Panel editUserPanel = new Panel();
    editUserPanel.setLayoutManager(new GridLayout(2));
    editUserPanel.addComponent(new Label("Username:"));
    editUserPanel.addComponent(new TextBox());
    editUserPanel.addComponent(new Label("Password:"));
    editUserPanel.addComponent(new TextBox().setMask('*'));
    Button updateButton = new Button("Update");
    Button cancelEditButton = new Button("Cancel");
    cancelEditButton.addListener(e -> {
      gui.removeWindow(editUserWindow);
    });
    editUserPanel.addComponent(updateButton);
    editUserPanel.addComponent(cancelEditButton);
    editUserWindow.setComponent(editUserPanel);

    Panel actionPanel = new Panel();
    actionPanel.setLayoutManager(new GridLayout(4));

    Button addUserButton = new Button("Add");
    addUserButton.addListener(e -> {
      gui.addWindow(addUserWindow);
    });

    Button editButton = new Button("Edit");
    editButton.addListener(e -> {
      if(checkBoxList.stream().anyMatch(CheckBox::isChecked)) {
        gui.addWindow(editUserWindow);
      }
    });

    Button deleteButton = new Button("Delete");
    Button logoutButton = new Button("Logout");
    logoutButton.addListener(e -> {
      System.exit(0);
    });
    actionPanel.addComponent(addUserButton);
    actionPanel.addComponent(editButton);
    actionPanel.addComponent(new Button("Delete"));
    actionPanel.addComponent(logoutButton);

    parent.addComponent(actionPanel);
    return parent;
  }
}
