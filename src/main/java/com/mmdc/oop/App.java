package com.mmdc.oop;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLogBackend;
import com.j256.ormlite.support.ConnectionSource;
import com.mmdc.oop.DTO.RepositoriesDto;
import com.mmdc.oop.Repositories.AttendanceRepository;
import com.mmdc.oop.Repositories.EmployeeRepository;
import com.mmdc.oop.Repositories.LeaveRepository;
import com.mmdc.oop.Repositories.OvertimeRepository;
import com.mmdc.oop.Repositories.PayrollRepository;
import com.mmdc.oop.Repositories.RoleRepository;
import com.mmdc.oop.Repositories.UserRepository;
import com.mmdc.oop.Repositories.UserRoleRepository;
import com.mmdc.oop.Services.SeedService;
import com.mmdc.oop.Utils.DatabaseUtil;
import com.mmdc.oop.Views.LoginView;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.TerminalScreen;

public class App 
{
    private final String DATABASE_URL = DatabaseUtil.connectToDatabase();
    private final ConnectionSource connectionSource;
    private final Screen screen;
    private final MultiWindowTextGUI gui;

    public MultiWindowTextGUI getGui() {
        return gui;
    }

    public App() throws Exception {
        connectionSource = new JdbcConnectionSource(DATABASE_URL);
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        this.screen = new TerminalScreen(terminal);
        this.gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK));
        screen.startScreen();
    }

    public static void main( String[] args ) throws Exception
    {
        System.setProperty(LocalLogBackend.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
        App app = new App();

        // Repository
        UserRepository userRepository = new UserRepository(app.connectionSource);
        RoleRepository roleRepository = new RoleRepository(app.connectionSource);
        UserRoleRepository userRoleRepository = new UserRoleRepository(app.connectionSource);
        EmployeeRepository employeeRepository = new EmployeeRepository(app.connectionSource);
        AttendanceRepository attendanceRepository = new AttendanceRepository(app.connectionSource);
        OvertimeRepository overtimeRepository = new OvertimeRepository(app.connectionSource);
        LeaveRepository leaveRepository = new LeaveRepository(app.connectionSource);
        PayrollRepository payrollRepository = new PayrollRepository(app.connectionSource);

        RepositoriesDto repositoriesDto = new RepositoriesDto(userRepository, roleRepository, userRoleRepository,
            employeeRepository, attendanceRepository, overtimeRepository, leaveRepository, payrollRepository);

        // Services
        SeedService seedService = new SeedService(repositoriesDto);
        seedService.seed();

        LoginView loginView = new LoginView(app.gui, repositoriesDto);

        app.gui.addWindowAndWait(loginView.getWindow());
    }
}
