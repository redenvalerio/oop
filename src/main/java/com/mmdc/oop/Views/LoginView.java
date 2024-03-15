package com.mmdc.oop.Views;

import java.util.List;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.mmdc.oop.DTO.RepositoriesDto;
import com.mmdc.oop.Interfaces.IView;
import com.mmdc.oop.Models.User;
import com.mmdc.oop.Models.UserRole;
import com.mmdc.oop.Repositories.UserRepository;
import com.mmdc.oop.Repositories.UserRoleRepository;
import com.mmdc.oop.Utils.AppState;
import com.mmdc.oop.Utils.Authentication;

public class LoginView implements IView {

    private Panel panel;
    private BasicWindow window;
    private TextBox usernameTextBox;
    private TextBox passwordTextBox;
    private Button loginButton;
    private Button exitButton;
    private MultiWindowTextGUI gui;

    public MultiWindowTextGUI getGui() {
        return gui;
    }

    public LoginView(MultiWindowTextGUI gui, RepositoriesDto repositoriesDto) {
        this.panel = new Panel(new GridLayout(2));
        this.gui = gui;
        
        panel.addComponent(new Label("Username:"));
        usernameTextBox = new TextBox();
        panel.addComponent(usernameTextBox);

        panel.addComponent(new Label("Password:"));
        passwordTextBox = new TextBox().setMask('*'); // Mask input for password
        panel.addComponent(passwordTextBox);

        loginButton = new Button("Login");
        panel.addComponent(loginButton);

        exitButton = new Button("Exit");
        panel.addComponent(exitButton);

    UserRepository userRepository = repositoriesDto.getUserRepository();
    UserRoleRepository userRoleRepository = repositoriesDto.getUserRoleRepository();

    List<User> users = userRepository.findAll();

    loginButton.addListener(e -> {
      String username = usernameTextBox.getText();
      String password = passwordTextBox.getText();
      users.stream().filter(user -> user.getUsername().equals(username) && user.getPassword().equals(Authentication.hashPassword(password))).findFirst().ifPresentOrElse(
        user -> {
              UserRole userRole = userRoleRepository.findAll().stream().filter(ur -> ur.getUser().getId() == user.getId()).findFirst().get();
              AppState.currentUser = user;
              AppState.currentEmployee = user.getEmployee();
              AppState.currentRole = userRole.getRole();
              MessageDialog.showMessageDialog(gui, "Success", "Login successful");
              gui.removeWindow(gui.getActiveWindow());
              gui.addWindowAndWait(new DashboardView(gui, repositoriesDto).getWindow());
        },
        () -> {
              MessageDialog.showMessageDialog(gui, "Error", "Invalid username or password");
        });
    });

    exitButton.addListener(e -> {
      System.exit(0);
    });

        this.window = new BasicWindow("Login");
        window.setComponent(panel);
    }

    public Button getLoginButton() {
        return loginButton;
    }
    
    public Button getExitButton() {
        return exitButton;
    }

    public String getUsername() {
        return usernameTextBox.getText();
    }

    public String getPassword() {
        return passwordTextBox.getText();
    }

    public BasicWindow getWindow() {
        return window;
    }
}
