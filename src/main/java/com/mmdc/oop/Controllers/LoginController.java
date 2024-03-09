package com.mmdc.oop.Controllers;

import java.io.IOException;
import java.util.List;

import com.mmdc.oop.App;
import com.mmdc.oop.Models.User;
import com.mmdc.oop.Repositories.UserRepository;
import com.mmdc.oop.Utils.Authentication;
import com.mmdc.oop.Views.LoginScreen;

public class LoginController {
  public LoginController(App app, LoginScreen loginScreen, UserRepository userRepository) {
    List<User> users = userRepository.findAll();

    loginScreen.getLoginButton().addListener(e -> {
      String username = loginScreen.getUsername();
      String password = loginScreen.getPassword();
      users.stream().filter(user -> user.getUsername().equals(username) && user.getPassword().equals(Authentication.hashPassword(password))).findFirst().ifPresentOrElse(
        user -> {
              app.showMessage("Login successful", "Success");
              try {
                app.showDashboardScreen(userRepository);
              } catch (IOException e1) {
                e1.printStackTrace();
              }
        },
        () -> {
              app.showMessage("Invalid username or password", "Error");
        });
    });

    loginScreen.getExitButton().addListener(e -> {
      System.exit(0);
    });
  } 

}
