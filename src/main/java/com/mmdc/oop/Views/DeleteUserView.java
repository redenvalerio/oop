package com.mmdc.oop.Views;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.mmdc.oop.DTO.RepositoriesDto;
import com.mmdc.oop.Interfaces.IView;
import com.mmdc.oop.Models.User;
import com.mmdc.oop.Models.UserRole;
import com.mmdc.oop.Repositories.EmployeeRepository;
import com.mmdc.oop.Repositories.UserRepository;

public class DeleteUserView implements IView {

  private MultiWindowTextGUI gui;
  private BasicWindow window;
  private Panel panel;

  public DeleteUserView(MultiWindowTextGUI gui, Integer id, RepositoriesDto repositoriesDto) {
    UserRepository userRepository = repositoriesDto.getUserRepository();
    EmployeeRepository employeeRepository = repositoriesDto.getEmployeeRepository();

    this.gui = gui;
    this.panel = new Panel();
    this.panel.setLayoutManager(new GridLayout(1));

    this.panel.addComponent(new Label("Are you sure you want to delete this user?"));

    this.window = new BasicWindow("Delete User");

    Panel actionPanel = new Panel();
    actionPanel.setLayoutManager(new GridLayout(2));
    Button deleteUserButton = new Button("Confirm");
    deleteUserButton.addListener((button) -> {
      User user = userRepository.findById(id);
      if(user == null){
        MessageDialog.showMessageDialog(gui, "Error", "User not found");
        return;
      }
      UserRole userRole = repositoriesDto.getUserRoleRepository().findAll().stream().filter(ur -> ur.getUser().getId() == user.getId()).findFirst().get();
      repositoriesDto.getUserRoleRepository().delete(userRole);
      employeeRepository.delete(user.getEmployee());
      userRepository.delete(user);
      MessageDialog.showMessageDialog(gui, "Success", "User deleted");
      gui.removeWindow(window);
      gui.removeWindow(gui.getActiveWindow());
      gui.addWindowAndWait(new DashboardView(gui,repositoriesDto).getWindow());
    });
    actionPanel.addComponent(deleteUserButton);

    Button cancelButton = new Button("Cancel");
    cancelButton.addListener((button) -> {
      gui.removeWindow(window);
      gui.removeWindow(gui.getActiveWindow());
      gui.addWindowAndWait(new DashboardView(gui, repositoriesDto).getWindow());
    });
    actionPanel.addComponent(cancelButton);
    this.panel.addComponent(actionPanel);

    this.window.setComponent(panel);
  }

  @Override
  public BasicWindow getWindow() {
    return window;
  }
  
}
