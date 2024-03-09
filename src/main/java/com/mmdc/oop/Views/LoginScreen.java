package com.mmdc.oop.Views;

import com.googlecode.lanterna.gui2.*;

public class LoginScreen {

    private Panel panel;
    private BasicWindow window;
    private TextBox usernameTextBox;
    private TextBox passwordTextBox;
    private Button loginButton;
    private Button exitButton;

    public LoginScreen(MultiWindowTextGUI gui) {
        this.panel = new Panel(new GridLayout(2));
        
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
