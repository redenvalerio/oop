package com.mmdc.oop;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.support.ConnectionSource;
import com.mmdc.oop.Controllers.LoginController;
import com.mmdc.oop.Repositories.RoleRepository;
import com.mmdc.oop.Repositories.UserRepository;
import com.mmdc.oop.Repositories.UserRoleRepository;
import com.mmdc.oop.Services.SeedService;
import com.mmdc.oop.Views.DashboardView;
import com.mmdc.oop.Views.LoginView;

import java.io.IOException;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.TerminalScreen;

public class App 
{
    private final String DATABASE_URL = "jdbc:sqlite:./src/main/resources/data/myapp.db";
    private final ConnectionSource connectionSource;
    private final Screen screen;
    private final MultiWindowTextGUI gui;

    public App() throws Exception {
        connectionSource = new JdbcConnectionSource(DATABASE_URL);
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        this.screen = new TerminalScreen(terminal);
        this.gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK));
        screen.startScreen();
    }

    public void showLoginScreen(UserRepository userRepository) throws Exception {
        gui.removeWindow(gui.getActiveWindow());
        LoginView loginScreen = new LoginView(gui);
        new LoginController(this, loginScreen, userRepository);
        gui.addWindowAndWait(loginScreen.getWindow());
    }

    public void showDashboardScreen(UserRepository userRepository) throws IOException {
        gui.removeWindow(gui.getActiveWindow());
        DashboardView dashboardScreen = new DashboardView(gui, userRepository);
        gui.addWindowAndWait(dashboardScreen.getWindow());
    }

    public void replace(BasicWindow newWindow) {
        gui.removeWindow(gui.getActiveWindow());
        gui.addWindowAndWait(newWindow);
    }

    public void showOnTop(BasicWindow newWindow) {
        gui.addWindowAndWait(newWindow);
    }

    public static void main( String[] args ) throws Exception
    {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
        App app = new App();

        // Repository
        UserRepository userRepository = new UserRepository(app.connectionSource);
        RoleRepository roleRepository = new RoleRepository(app.connectionSource);
        UserRoleRepository userRoleRepository = new UserRoleRepository(app.connectionSource);

        // Services
        SeedService seedService = new SeedService(userRepository, roleRepository, userRoleRepository);
        seedService.seed();

        // Views

        app.showLoginScreen(userRepository);
    }

    public void showMessage(String message, String title) {
        MessageDialog.showMessageDialog(gui, title, message);
    }
}
