package com.mmdc.oop.Controllers;

import com.mmdc.oop.App;
import com.mmdc.oop.Models.User;
import com.mmdc.oop.Utils.DatabaseManager;
import com.mmdc.oop.Views.LoginScreen;

public class LoginController {
    private LoginScreen loginScreen;
    private App app;

    public LoginController(LoginScreen loginScreen, App app) throws Exception {
        this.loginScreen = loginScreen;
        this.app = app;

        setupActions();
    }

    private void setupActions() throws Exception {
        final DatabaseManager dbManager = new DatabaseManager();

        loginScreen.getLoginButton().addListener(button -> {
            String username = loginScreen.getUsername();
            String password = loginScreen.getPassword();

            User user = dbManager.authenticateUser(username, password);
            if (user != null) {
                loginScreen.showMessage("Login Successful!", "Success");
            } else {
                loginScreen.showMessage("Invalid username or password", "Error");
            }
        });

        loginScreen.getExitButton().addListener(button -> {
            System.exit(0);
        });
    }
  }
