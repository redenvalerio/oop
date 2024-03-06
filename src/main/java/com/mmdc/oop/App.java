package com.mmdc.oop;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.logger.LoggerFactory;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.LogManager;

import com.mmdc.oop.Controllers.LoginController;
import com.mmdc.oop.Views.LoginScreen;

import java.io.IOException;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.TerminalScreen;

/**
 * Hello world!
 *
 */
public class App 
{
    private final Screen screen;
    private final MultiWindowTextGUI gui;

    public App() throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        this.screen = new TerminalScreen(terminal);
        this.gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK));
        screen.startScreen();
    }

    public void showLoginScreen() throws Exception {
        LoginScreen loginScreen = new LoginScreen(gui);
        new LoginController(loginScreen, this);
        gui.addWindowAndWait(loginScreen.getWindow());
    }

    public void showDashboardScreen() {
        // DashboardScreen dashboardScreen = new DashboardScreen(gui);
        // gui.addWindowAndWait(dashboardScreen.getWindow());
    }

    public static void main( String[] args ) throws Exception
    {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
        App app = new App();
        app.showLoginScreen();
    }
}
