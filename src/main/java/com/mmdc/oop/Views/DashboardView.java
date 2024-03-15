package com.mmdc.oop.Views;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.CheckBox;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.mmdc.oop.DTO.RepositoriesDto;
import com.mmdc.oop.Interfaces.IView;
import com.mmdc.oop.Models.Attendance;
import com.mmdc.oop.Models.Employee;
import com.mmdc.oop.Models.Leave;
import com.mmdc.oop.Models.Overtime;
import com.mmdc.oop.Models.Role;
import com.mmdc.oop.Models.User;
import com.mmdc.oop.Models.UserRole;
import com.mmdc.oop.Repositories.AttendanceRepository;
import com.mmdc.oop.Repositories.EmployeeRepository;
import com.mmdc.oop.Repositories.RoleRepository;
import com.mmdc.oop.Repositories.UserRepository;
import com.mmdc.oop.Repositories.UserRoleRepository;
import com.mmdc.oop.Utils.AppState;

public class DashboardView implements IView {
  private Panel panel;
  private BasicWindow window;
  private UserRepository userRepository;
  private RoleRepository roleRepository;
  private AttendanceRepository attendanceRepository;
  private UserRoleRepository userRoleRepository;
  private EmployeeRepository employeeRepository;
  private RepositoriesDto re;
  private MultiWindowTextGUI gui;

  private List<User> users;

  public DashboardView(MultiWindowTextGUI gui, RepositoriesDto repositoriesDto) {
    this.userRepository = repositoriesDto.getUserRepository();
    this.roleRepository = repositoriesDto.getRoleRepository();
    this.userRoleRepository = repositoriesDto.getUserRoleRepository();
    this.employeeRepository = repositoriesDto.getEmployeeRepository();
    this.attendanceRepository = repositoriesDto.getAttendanceRepository();
    this.re = repositoriesDto;

    this.gui = gui;
    this.panel = new Panel();

    panel.setLayoutManager(new GridLayout(1));

    Panel infoPanel = new Panel();
    infoPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
    User user = AppState.currentUser;
    Role role = AppState.currentRole;

    if (AppState.currentUser != null)
      infoPanel.addComponent(new Label("User: " + user.getUsername() + " Role: " + role.getRoleName()));
    else
      infoPanel.addComponent(new Label("User: Guest Role: Guest"));

    Panel footerPanel = new Panel();
    footerPanel.setLayoutManager(new GridLayout(2));
    Button logoutButton = new Button("Logout");
    logoutButton.addListener(e -> {
      gui.removeWindow(gui.getActiveWindow());
      LoginView loginView = new LoginView(gui, re);
      gui.addWindowAndWait(loginView.getWindow());
    });
    footerPanel.addComponent(logoutButton);

    // Dashboard screen checkboxes
    Panel dashboardPanel = new Panel();
    dashboardPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
    List<CheckBox> checkBoxList = new ArrayList<>();
    String currentRole = AppState.currentRole.getRoleName();
    List<String> labels = new ArrayList<>();
    if(currentRole.equals("admin")) {
      labels.add("Users");
      labels.add("Profile");
      labels.add("Attendance");
      labels.add("Payroll");
      labels.add("Payroll Reports");
      labels.add("Leave Requests");
      labels.add("Overtime Requests");
    } else if(currentRole.equals("employee")) {
      labels.add("Profile");
      labels.add("Attendance");
    } else if(currentRole.equals("payroll")) {
      labels.add("Profile");
      labels.add("Attendance");
      labels.add("Payroll");
      labels.add("Payroll Reports");
      labels.add("Overtime Requests");
    } else if(currentRole == "hr") {
      labels.add("Users");
      labels.add("Profile");
      labels.add("Attendance");
      labels.add("Leave Requests");
    } else {
      labels.add("Profile");
    }

    // change forloop
    for (int i = 0; i < labels.size(); i++) {
      CheckBox checkBox = new CheckBox(labels.get(i));
      checkBoxList.add(checkBox);
      dashboardPanel.addComponent(checkBox);
    }

    Panel userManagementPanel = getUserManagement();
    Panel profilePanel = getProfilePanel();

    checkBoxList.forEach(checkBox -> checkBox.addListener(e -> {
      if (e) {
        AppState.currentView = checkBox.getLabel();
        panel.removeAllComponents();
        panel.addComponent(infoPanel);
        panel.addComponent(dashboardPanel);
        checkBoxList.forEach(cb -> {
          if (cb != checkBox) {
            cb.setChecked(false);
          }
        });
        if (checkBox.getLabel().equals("Users")) {
          panel.addComponent(userManagementPanel);
        }
        if (checkBox.getLabel().equals("Profile")) {
          panel.addComponent(profilePanel);
        }
        if (checkBox.getLabel().equals("Attendance")) {
          panel.addComponent(getAttendancePanel());
        }
        if (checkBox.getLabel().equals("Payroll")) {
          panel.addComponent(getPayrollPanel());
        }
        if(checkBox.getLabel().equals("Payroll Reports")) {
          panel.addComponent(getPayrollReportsPanel());
        }
        if(checkBox.getLabel().equals("Leave Requests")) {
          panel.addComponent(getLeaveRequestsPanel());
        }
        if(checkBox.getLabel().equals("Overtime Requests")) {
          panel.addComponent(getOvertimeRequestsPanel());
        }
        panel.addComponent(footerPanel);
      }
    }));

    String currentView = AppState.currentView;
    checkBoxList.forEach(checkBox -> {
      if (checkBox.getLabel().equals(currentView)) {
        checkBox.setChecked(true);
      }
    });

    panel.addComponent(infoPanel);
    panel.addComponent(dashboardPanel);

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
    userManagementPanel.addComponent(new Label("Role"));

    this.users = userRepository.findAll();

    List<CheckBox> checkBoxList = new ArrayList<>();

    List<UserRole> userRoles = userRoleRepository.findAll();
    for (User user : users) {
      checkBoxList.add(new CheckBox(user.getId() + ""));
      userManagementPanel.addComponent(checkBoxList.get(users.indexOf(user)));
      userManagementPanel.addComponent(new Label(user.getUsername()));
      try {
        UserRole userRole = userRoles.stream().filter(ur -> ur.getUser().getId() == user.getId()).findAny().get();
        userManagementPanel.addComponent(new Label(userRole.getRole().getRoleName()));
      } catch (Exception e) {
        userManagementPanel.addComponent(new Label("Guest"));
      }
    }

    checkBoxList.forEach(checkBox -> checkBox.addListener(e -> {
      if (e) {
        checkBoxList.forEach(cb -> {
          if (cb != checkBox) {
            cb.setChecked(false);
          }
        });
      }
    }));
    parent.addComponent(userManagementPanel);

    Panel actionPanel = new Panel();
    actionPanel.setLayoutManager(new GridLayout(4));

    Button addUserButton = new Button("Add");
    addUserButton.addListener(e -> {
      gui.addWindowAndWait(new CreateUserView(gui, this.re).getWindow());
    });

    Button editUserButton = new Button("Edit");
    editUserButton.addListener(e -> {
      if(checkBoxList.stream().noneMatch(CheckBox::isChecked)) {
        MessageDialog.showMessageDialog(gui, "Error", "Please select a user to edit");
        return;
      }

      if (checkBoxList.stream().anyMatch(CheckBox::isChecked)) {
        Integer id = Integer
            .parseInt(checkBoxList.stream().filter(CheckBox::isChecked).findFirst().get().getLabel().toString());
        User user = userRepository.findById(id);
        if (user == null) {
          MessageDialog.showMessageDialog(gui, "Error", "User not found");
          return;
        }

        gui.addWindowAndWait(new EditUserView(gui, id, this.re).getWindow());
      }
    });

    Button deleteButton = new Button("Delete");
    deleteButton.addListener(e -> {
      if (checkBoxList.stream().anyMatch(CheckBox::isChecked)) {
        Integer id = Integer
            .parseInt(checkBoxList.stream().filter(CheckBox::isChecked).findFirst().get().getLabel().toString());
        User user = userRepository.findById(id);
        if (user == null) {
          MessageDialog.showMessageDialog(gui, "Error", "User not found");
          return;
        }

        if (user.getId() == AppState.currentUser.getId()) {
          MessageDialog.showMessageDialog(gui, "Error", "You cannot delete yourself");
          return;
        }
        gui.addWindowAndWait(new DeleteUserView(gui, id, this.re).getWindow());
      }
    });
    actionPanel.addComponent(addUserButton);
    actionPanel.addComponent(editUserButton);
    actionPanel.addComponent(deleteButton);

    parent.addComponent(actionPanel);
    return parent;
  }

  private Panel getProfilePanel() {
    Panel parent = new Panel();
    parent.setLayoutManager(new GridLayout(2));

    parent.addComponent(new Label(""));
    parent.addComponent(new Label(""));
    parent.addComponent(new Label("Your Profile"));
    parent.addComponent(new Label(""));

    Button viewButton = new Button("View Payslips");
    viewButton.addListener((button) -> {
      gui.addWindowAndWait(new ShowPayrollReport(gui, AppState.currentUser.getId(), re).getWindow());
    });

    Button applyForLeave = new Button("Apply for Leave");
    applyForLeave.addListener((button) -> {
      gui.addWindowAndWait(new ApplyForLeaveView(gui, AppState.currentUser.getId(), re).getWindow());
    });

    Button viewLeaveRequests = new Button("View Leave Requests");
    viewLeaveRequests.addListener((button) -> {
      gui.addWindowAndWait(new ShowLeaveRequestsView(gui, AppState.currentUser.getId(), re).getWindow());
    });

    Button submitOvertime = new Button("Submit Overtime");
    submitOvertime.addListener((button) -> {
      gui.addWindowAndWait(new ApplyForOvertimeView(gui, AppState.currentUser.getId(), re).getWindow());
    });

    Button viewOvertimeRequests = new Button("View Overtime Requests");
    viewOvertimeRequests.addListener((button) -> {
      gui.addWindowAndWait(new ShowOvertimeRequestsView(gui, AppState.currentUser.getId(), re).getWindow());
    });

    parent.addComponent(viewButton);
    parent.addComponent(new Label(""));
    parent.addComponent(applyForLeave);
    parent.addComponent(viewLeaveRequests);
    parent.addComponent(submitOvertime);
    parent.addComponent(viewOvertimeRequests);
    parent.addComponent(new Label(""));
    parent.addComponent(new Label(""));

    Panel firstColumn = new Panel();
    firstColumn.setLayoutManager(new GridLayout(2));
    Panel secondColumn = new Panel();
    secondColumn.setLayoutManager(new GridLayout(2));

    firstColumn.addComponent(new Label("First Name*:"));
    TextBox firstNameTextBox = new TextBox();
    firstColumn.addComponent(firstNameTextBox);

    // middle name
    firstColumn.addComponent(new Label("Middle Name:"));
    TextBox middleNameTextBox = new TextBox();
    firstColumn.addComponent(middleNameTextBox);

    firstColumn.addComponent(new Label("Last Name*:"));
    TextBox lastNameTextBox = new TextBox();
    firstColumn.addComponent(lastNameTextBox);

    firstColumn.addComponent(new Label("Date of Birth*:"));
    TextBox dateOfBirthTextBox = new TextBox();
    firstColumn.addComponent(dateOfBirthTextBox);

    firstColumn.addComponent(new Label("Address*:"));
    TextBox addressTextBox = new TextBox();
    firstColumn.addComponent(addressTextBox);

    firstColumn.addComponent(new Label("City"));
    TextBox cityTextBox = new TextBox();
    firstColumn.addComponent(cityTextBox);

    firstColumn.addComponent(new Label("Province"));
    TextBox provinceTextBox = new TextBox();
    firstColumn.addComponent(provinceTextBox);

    firstColumn.addComponent(new Label("Phone*:"));
    TextBox phoneTextBox = new TextBox();
    firstColumn.addComponent(phoneTextBox);

    firstColumn.addComponent(new Label("Email*:"));
    TextBox emailTextBox = new TextBox();
    firstColumn.addComponent(emailTextBox);

    // SSS, TIN, PhilHealth, PAGIBIG
    firstColumn.addComponent(new Label("SSS*:"));
    TextBox sssTextBox = new TextBox();
    firstColumn.addComponent(sssTextBox);

    firstColumn.addComponent(new Label("TIN*:"));
    TextBox tinTextBox = new TextBox();
    firstColumn.addComponent(tinTextBox);

    firstColumn.addComponent(new Label("PhilHealth*:"));
    TextBox philHealthTextBox = new TextBox();
    firstColumn.addComponent(philHealthTextBox);

    firstColumn.addComponent(new Label("PAGIBIG*:"));
    TextBox pagibigTextBox = new TextBox();
    firstColumn.addComponent(pagibigTextBox);

    secondColumn.addComponent(new Label("Emergency C. Name*:"));
    TextBox emergencyContactNameTextBox = new TextBox();
    secondColumn.addComponent(emergencyContactNameTextBox);

    secondColumn.addComponent(new Label("Emergency C. Phone*:"));
    TextBox emergencyContactPhoneTextBox = new TextBox();
    secondColumn.addComponent(emergencyContactPhoneTextBox);

    secondColumn.addComponent(new Label("Emergency C. Address*:"));
    TextBox emergencyContactAddressTextBox = new TextBox();
    secondColumn.addComponent(emergencyContactAddressTextBox);

    secondColumn.addComponent(new Label("Emergency C. Relationship*:"));
    TextBox emergencyContactRelationshipTextBox = new TextBox();
    secondColumn.addComponent(emergencyContactRelationshipTextBox);

    secondColumn.addComponent(new Label("Department (readonly):"));
    TextBox departmentTextBox = new TextBox();
    departmentTextBox.setReadOnly(true);
    secondColumn.addComponent(departmentTextBox);

    secondColumn.addComponent(new Label("Position (readonly):"));
    TextBox positionTextBox = new TextBox();
    positionTextBox.setReadOnly(true);
    secondColumn.addComponent(positionTextBox);

    secondColumn.addComponent(new Label("Hire Date (readonly):"));
    TextBox hireDateTextBox = new TextBox();
    hireDateTextBox.setReadOnly(true);
    secondColumn.addComponent(hireDateTextBox);

    secondColumn.addComponent(new Label("Salary (readonly):"));
    TextBox salaryTextBox = new TextBox();
    salaryTextBox.setReadOnly(true);
    secondColumn.addComponent(salaryTextBox);

    secondColumn.addComponent(new Label("Hourly Rate (readonly):"));
    TextBox hourlyRateTextBox = new TextBox();
    hourlyRateTextBox.setReadOnly(true);
    secondColumn.addComponent(hourlyRateTextBox);

    // employee type, employee status
    secondColumn.addComponent(new Label("Employee Type (readonly):"));
    TextBox employeeTypeTextBox = new TextBox();
    employeeTypeTextBox.setReadOnly(true);
    secondColumn.addComponent(employeeTypeTextBox);

    secondColumn.addComponent(new Label("Employee Status (readonly):"));
    TextBox employeeStatusTextBox = new TextBox();
    employeeStatusTextBox.setReadOnly(true);
    secondColumn.addComponent(employeeStatusTextBox);

    Employee currentEmployee = AppState.currentUser.getEmployee();

    // Work Days
    Panel workDaysContainer = new Panel();
    workDaysContainer.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
    List<CheckBox> workDaysCheckBoxList = new ArrayList<>();
    // SUN,MON,TUE,WED,THU,FRI,SAT
    String[] workDays = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
    String employeeWorkDays = currentEmployee.getWorkDays();

    for (String workDay : workDays) {
      CheckBox checkBox = new CheckBox(workDay);
      checkBox.setEnabled(false);
      checkBox.setChecked(employeeWorkDays.contains(workDay));
      workDaysCheckBoxList.add(checkBox);
      workDaysContainer.addComponent(checkBox);
    }

    secondColumn.addComponent(new Label(""));
    secondColumn.addComponent(new Label(""));
    secondColumn.addComponent(new Label("Work Days (readonly):"));
    secondColumn.addComponent(new Label(""));
    secondColumn.addComponent(workDaysContainer);
    secondColumn.addComponent(new Label(""));
    secondColumn.addComponent(new Label(""));
    secondColumn.addComponent(new Label(""));
    secondColumn.addComponent(new Label("Shift Hours (readonly):"));

    Panel shiftHoursContainer = new Panel();
    shiftHoursContainer.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
    // shiftStartTime, shiftEndTime
    TextBox shiftStartTimeTextBox = new TextBox();
    shiftStartTimeTextBox.setReadOnly(true);
    shiftStartTimeTextBox.setText(currentEmployee.getShiftStartTime());
    shiftHoursContainer.addComponent(shiftStartTimeTextBox);
    shiftHoursContainer.addComponent(new Label("to"));
    TextBox shiftEndTimeTextBox = new TextBox();
    shiftEndTimeTextBox.setReadOnly(true);
    shiftEndTimeTextBox.setText(currentEmployee.getShiftEndTime());
    shiftHoursContainer.addComponent(shiftEndTimeTextBox);
    secondColumn.addComponent(new Label(""));
    secondColumn.addComponent(shiftHoursContainer);
    secondColumn.addComponent(new Label(""));
    secondColumn.addComponent(new Label(""));

    try {
      Employee employee = employeeRepository.findById(AppState.currentUser.getId());
      if (employee != null) {
        firstNameTextBox.setText(employee.getFirstName() != null ? employee.getFirstName() : "");
        middleNameTextBox.setText(employee.getMiddleName() != null ? employee.getMiddleName() : "");
        lastNameTextBox.setText(employee.getLastName() != null ? employee.getLastName() : "");
        dateOfBirthTextBox.setText(employee.getDateOfBirth() != null ? employee.getDateOfBirth() : "");
        addressTextBox.setText(employee.getAddress() != null ? employee.getAddress() : "");
        cityTextBox.setText(employee.getCity() != null ? employee.getCity() : "");
        provinceTextBox.setText(employee.getProvince() != null ? employee.getProvince() : "");
        phoneTextBox.setText(employee.getPhone() != null ? employee.getPhone() : "");
        emailTextBox.setText(employee.getEmail() != null ? employee.getEmail() : "");
        emergencyContactNameTextBox
            .setText(employee.getEmergencyContactName() != null ? employee.getEmergencyContactName() : "");
        emergencyContactPhoneTextBox
            .setText(employee.getEmergencyContactPhone() != null ? employee.getEmergencyContactPhone() : "");
        emergencyContactAddressTextBox
            .setText(employee.getEmergencyContactAddress() != null ? employee.getEmergencyContactAddress() : "");
        emergencyContactRelationshipTextBox.setText(
            employee.getEmergencyContactRelationship() != null ? employee.getEmergencyContactRelationship() : "");
        sssTextBox.setText(employee.getSSS() != null ? employee.getSSS() : "");
        tinTextBox.setText(employee.getTIN() != null ? employee.getTIN() : "");
        philHealthTextBox.setText(employee.getPhilHealth() != null ? employee.getPhilHealth() : "");
        pagibigTextBox.setText(employee.getPAGIBIG() != null ? employee.getPAGIBIG() : "");
        departmentTextBox.setText(employee.getDepartment() != null ? employee.getDepartment() : "");
        positionTextBox.setText(employee.getPosition() != null ? employee.getPosition() : "");
        hireDateTextBox.setText(employee.getHireDate() != null ? employee.getHireDate() : "");
        salaryTextBox.setText(employee.getSalary() != null ? employee.getSalary() + "" : "");
        hourlyRateTextBox.setText(employee.getHourlyRate() != null ? employee.getHourlyRate() + "" : "");
        employeeTypeTextBox.setText(employee.getEmployeeType() != null ? employee.getEmployeeType() : "");
        employeeStatusTextBox.setText(employee.getEmployeeStatus() != null ? employee.getEmployeeStatus() : "");
      }
    } catch (Exception e) {
      e.printStackTrace();
      MessageDialog.showMessageDialog(gui, "Error", e.getMessage());
    }

    parent.addComponent(firstColumn);
    parent.addComponent(secondColumn);

    Button saveButton = new Button("Update Profile Information");
    saveButton.addListener((button) -> {
      try {
        Employee employee = employeeRepository.findById(AppState.currentUser.getId());

        if (firstNameTextBox.getText().isEmpty() || lastNameTextBox.getText().isEmpty()
            || dateOfBirthTextBox.getText().isEmpty() || addressTextBox.getText().isEmpty()
            || phoneTextBox.getText().isEmpty() || emailTextBox.getText().isEmpty() || sssTextBox.getText().isEmpty()
            || tinTextBox.getText().isEmpty() || philHealthTextBox.getText().isEmpty()
            || pagibigTextBox.getText().isEmpty()) {
          MessageDialog.showMessageDialog(gui, "Error", "All required fields must be filled");
          return;
        }

        if (employee != null) {
          employee.setFirstName(firstNameTextBox.getText());
          employee.setMiddleName(middleNameTextBox.getText());
          employee.setLastName(lastNameTextBox.getText());
          employee.setDateOfBirth(dateOfBirthTextBox.getText());
          employee.setAddress(addressTextBox.getText());
          employee.setCity(cityTextBox.getText());
          employee.setProvince(provinceTextBox.getText());
          employee.setPhone(phoneTextBox.getText());
          employee.setEmail(emailTextBox.getText());
          employee.setEmergencyContactName(emergencyContactNameTextBox.getText());
          employee.setEmergencyContactPhone(emergencyContactPhoneTextBox.getText());
          employee.setEmergencyContactAddress(emergencyContactAddressTextBox.getText());
          employee.setEmergencyContactRelationship(emergencyContactRelationshipTextBox.getText());
          employee.setSSS(sssTextBox.getText());
          employee.setTIN(tinTextBox.getText());
          employee.setPhilHealth(philHealthTextBox.getText());
          employee.setPAGIBIG(pagibigTextBox.getText());
          employee.setDepartment(departmentTextBox.getText());
          employee.setPosition(positionTextBox.getText());
          employee.setHireDate(hireDateTextBox.getText());
          employee.setSalary(Double.parseDouble(salaryTextBox.getText()));
          employee.setHourlyRate(Double.parseDouble(hourlyRateTextBox.getText()));
          employee.setEmployeeType(employeeTypeTextBox.getText());
          employee.setEmployeeStatus(employeeStatusTextBox.getText());

          employeeRepository.update(employee);
          MessageDialog.showMessageDialog(gui, "Success", "Profile updated");
        }
      } catch (Exception e) {
        e.printStackTrace();
        MessageDialog.showMessageDialog(gui, "Error", e.getMessage());
      }
    });
    parent.addComponent(saveButton);


    return parent;
  }

  private Panel getAttendancePanel() {
    Panel parent = new Panel();
    parent.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    List<Attendance> refAttendances = attendanceRepository.findAll();
    User user = AppState.currentUser;

    List<Attendance> userAttendances = new ArrayList<>();
    refAttendances.forEach(attendance -> {
      if (attendance.getUser().getId() == user.getId()) {
        userAttendances.add(attendance);
      }
    });

    // Scheduled workdays
    String workDays = user.getEmployee().getWorkDays();
    String shiftStart = user.getEmployee().getShiftStartTime();
    String shiftEnd = user.getEmployee().getShiftEndTime();

    LocalDate today = LocalDate.now();
    DayOfWeek dayOfWeek = today.getDayOfWeek();
    String todayName = dayOfWeek.name().substring(0, 3);

    if (!workDays.contains(todayName)) {
      parent.addComponent(new Label("Today is not a workday"));
      return parent;
    }

    Attendance currentAttendance = userAttendances.stream()
        .filter(attendance -> attendance.getDateIn().equals(LocalDate.now().toString())).findAny().orElse(null);
    if (currentAttendance == null) {
      currentAttendance = new Attendance();
      currentAttendance.setUser(user);
      currentAttendance.setDateIn(LocalDate.now().toString());
      attendanceRepository.save(currentAttendance);
    }

    parent.addComponent(new Label(""));
    parent.addComponent(new Label("Your Attendance"));
    parent.addComponent(new Label("Schedule: " + workDays));
    parent.addComponent(new Label("Shift: " + shiftStart + " - " + shiftEnd));
    parent.addComponent(new Label(""));

    Panel schedule = new Panel();
    schedule.setLayoutManager(new GridLayout(2));

    schedule.addComponent(new Label("Date (YYYY-MM-DD):"));
    TextBox dateInput = new TextBox(new TerminalSize(12, 1))
        .setValidationPattern(Pattern.compile("(\\d{0,4}-?\\d{0,2}-?\\d{0,2})?"));
    if (currentAttendance != null) {
      dateInput.setText(currentAttendance.getDateIn());
    }
    dateInput.setReadOnly(true);
    schedule.addComponent(dateInput);

    // Time In/ Time Out info TextBox
    schedule.addComponent(new Label("Time In (HH:mm):"));
    TextBox timeInTextBox = new TextBox();
    if (currentAttendance != null) {
      if (currentAttendance.getTimeIn() != null) {
        timeInTextBox.setText(currentAttendance.getTimeIn());
        schedule.removeComponent(new Button("Time In"));
      }
    }
    timeInTextBox.setReadOnly(true);
    schedule.addComponent(timeInTextBox);

    schedule.addComponent(new Label("Time Out (HH:mm):"));
    TextBox timeOutTextBox = new TextBox();
    if (currentAttendance != null) {
      if (currentAttendance.getTimeOut() != null) {
        timeOutTextBox.setText(currentAttendance.getTimeOut());
        schedule.removeComponent(new Button("Time Out"));
      }
    }
    timeOutTextBox.setReadOnly(true);
    schedule.addComponent(timeOutTextBox);

    Label actionLabel = new Label("Action");
    schedule.addComponent(actionLabel);

    Button timeInButton = new Button("Time In");
    Button timeOutButton = new Button("Time Out");

    timeInButton.addListener((button) -> {
      User cUser = AppState.currentUser;
      List<Attendance> cAttendances = attendanceRepository.findAll();
      Attendance cAttendance = cAttendances.stream()
          .filter(attendance -> attendance.getDateIn().equals(LocalDate.now().toString())
              && attendance.getUser().getId() == cUser.getId())
          .findAny().orElse(null);
      if (cAttendance != null) {
        if (cAttendance.getTimeIn() != null) {
          MessageDialog.showMessageDialog(gui, "Error", "You have already timed in");
          return;
        }
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = now.format(formatter);
        cAttendance.setTimeIn(formattedTime);
        attendanceRepository.update(cAttendance);
        timeInTextBox.setText(cAttendance.getTimeIn());
        schedule.removeComponent(timeInButton);
        schedule.addComponent(timeOutButton);
        MessageDialog.showMessageDialog(gui, "Success", "Time in recorded");
      } else {
        cAttendance = new Attendance();
        cAttendance.setUser(cUser);
        cAttendance.setDateIn(LocalDate.now().toString());
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = now.format(formatter);
        cAttendance.setTimeIn(formattedTime);
        attendanceRepository.save(cAttendance);
        timeInTextBox.setText(cAttendance.getTimeIn());
        schedule.removeComponent(timeInButton);
        schedule.addComponent(timeOutButton);
        MessageDialog.showMessageDialog(gui, "Success", "Time in recorded");
      }
    });

    timeOutButton.addListener((button) -> {
      User cUser = AppState.currentUser;
      List<Attendance> cAttendances = attendanceRepository.findAll();
      Attendance cAttendance = cAttendances.stream()
          .filter(attendance -> attendance.getDateIn().equals(LocalDate.now().toString())
              && attendance.getUser().getId() == cUser.getId())
          .findAny().orElse(null);
      if (cAttendance != null) {
        if (cAttendance.getTimeOut() != null) {
          MessageDialog.showMessageDialog(gui, "Error", "You have already timed out");
          return;
        }
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = now.format(formatter);
        cAttendance.setDateOut(LocalDate.now().toString());
        cAttendance.setTimeOut(formattedTime);
        attendanceRepository.update(cAttendance);
        timeOutTextBox.setText(cAttendance.getTimeOut());
        schedule.removeComponent(timeOutButton);
        schedule.removeComponent(actionLabel);
        schedule.addComponent(new Label("You have timed in and out for today"));
        MessageDialog.showMessageDialog(gui, "Success", "Time out recorded");
      } else {
        cAttendance = new Attendance();
        cAttendance.setUser(cUser);
        cAttendance.setDateIn(LocalDate.now().toString());
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = now.format(formatter);
        cAttendance.setTimeIn(formattedTime);
        cAttendance.setDateOut(LocalDate.now().toString());
        cAttendance.setTimeOut(formattedTime);
        attendanceRepository.save(cAttendance);
        timeOutTextBox.setText(cAttendance.getTimeOut());
        schedule.removeComponent(timeOutButton);
        schedule.removeComponent(actionLabel);
        schedule.addComponent(new Label("You have timed in and out for today"));
        MessageDialog.showMessageDialog(gui, "Success", "Time out recorded");
      }
    });

    if (currentAttendance.getTimeIn() == null) {
      schedule.addComponent(timeInButton);
    } else if (currentAttendance.getTimeOut() == null) {
      schedule.addComponent(timeOutButton);
    }

    if (currentAttendance.getTimeIn() != null && currentAttendance.getTimeOut() != null) {
      schedule.removeComponent(timeInButton);
      schedule.removeComponent(timeOutButton);
      schedule.removeComponent(actionLabel);
      schedule.addComponent(new Label("You have timed in and out for today"));
    }

    parent.addComponent(schedule);

    return parent;
  }

  private Panel getPayrollPanel() {
    Panel parent = new Panel();
    parent.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    parent.addComponent(new Label(""));
    parent.addComponent(new Label("Payroll Management"));

    Panel panel = new Panel();
    panel.setLayoutManager(new GridLayout(7)); // 7 columns for days of the week

    String[] dayNames = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
    for (String dayName : dayNames) {
      panel.addComponent(new Label(dayName));
    }

    Button backButton = new Button("Back");
    Button nextButton = new Button("Next");
    backButton.addListener(e -> {
      AppState.currentCalendarDate = AppState.currentCalendarDate.minusMonths(1);
      gui.removeWindow(gui.getActiveWindow());
      DashboardView dashboardView = new DashboardView(gui, re);
      gui.addWindowAndWait(dashboardView.getWindow());
    });

    nextButton.addListener(e -> {
      AppState.currentCalendarDate = AppState.currentCalendarDate.plusMonths(1);
      gui.removeWindow(gui.getActiveWindow());
      DashboardView dashboardView = new DashboardView(gui, re);
      gui.addWindowAndWait(dashboardView.getWindow());
    });

    Panel headerPanel = new Panel();
    headerPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    Panel actionPanel = new Panel();
    actionPanel.setLayoutManager(new GridLayout(3));
    actionPanel.addComponent(backButton);
    actionPanel.addComponent(new Label(AppState.currentCalendarDate.getMonth().toString() + " "
        + AppState.currentCalendarDate.getYear()));
    actionPanel.addComponent(nextButton);

    Panel infoPanel = new Panel();
    infoPanel.setLayoutManager(new GridLayout(2));
    // Paybegindate and Payenddate interval using checkbox
    infoPanel.addComponent(new Label("Selected Pay Begin Date:"));
    if (AppState.selectedPayBeginDate == null) {
      infoPanel.addComponent(new Label("None selected"));
    } else {
      infoPanel.addComponent(new Label(AppState.selectedPayBeginDate.toString()));
    }
    infoPanel.addComponent(new Label("Selected Pay End Date:"));
    if (AppState.selectedPayEndDate == null) {
      infoPanel.addComponent(new Label("None selected"));
    } else {
      infoPanel.addComponent(new Label(AppState.selectedPayEndDate.toString()));
    }

    headerPanel.addComponent(actionPanel);
    headerPanel.addComponent(infoPanel);
    headerPanel.addComponent(new Label(""));

    LocalDate currentMonth = AppState.currentCalendarDate;
    LocalDate firstOfMonth = currentMonth.withDayOfMonth(1);
    LocalDate lastOfMonth = currentMonth.withDayOfMonth(currentMonth.lengthOfMonth());

    // Find out what day of the week the first of the month falls on
    DayOfWeek startDay = firstOfMonth.getDayOfWeek();
    int value = startDay.getValue(); // 1 = Monday, 7 = Sunday
    value = value % 7; // Adjust to start from Sunday

    for (int i = 0; i < value; i++) {
      panel.addComponent(new EmptySpace());
    }

    for (int day = 1; day <= lastOfMonth.getDayOfMonth(); day++) {
      CheckBox dayCheckBox = new CheckBox(String.valueOf(day));
      // set the dayCheckbox to true if within the selectedPayBeginDate and selectedPayEndDate
      if(AppState.selectedPayBeginDate != null && AppState.selectedPayEndDate != null) {
        if (LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day).isAfter(AppState.selectedPayBeginDate)
            && (LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day).isBefore(AppState.selectedPayEndDate) || 
            LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day).isEqual(AppState.selectedPayEndDate)
            )) { 
          dayCheckBox.setChecked(true);
        }
      }

      if(AppState.selectedPayBeginDate != null) {
        if (LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day).isEqual(AppState.selectedPayBeginDate)) {
          dayCheckBox.setChecked(true);
        }
      }

      dayCheckBox.addListener((e) -> {
        if (e) {
          final int selectedDay = Integer.parseInt(dayCheckBox.getLabel());
          if (AppState.selectedPayBeginDate != null && AppState.selectedPayEndDate != null) {
            AppState.selectedPayBeginDate = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), selectedDay);
            AppState.selectedPayEndDate = null;
            gui.removeWindow(gui.getActiveWindow());
            DashboardView dashboardView = new DashboardView(gui, re);
            gui.addWindowAndWait(dashboardView.getWindow());
            return;
          }

          if (AppState.selectedPayBeginDate == null) {
            AppState.selectedPayBeginDate = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), selectedDay);
            gui.removeWindow(gui.getActiveWindow());
            DashboardView dashboardView = new DashboardView(gui, re);
            gui.addWindowAndWait(dashboardView.getWindow());
            return;
          } else if (AppState.selectedPayEndDate == null) {
            AppState.selectedPayEndDate = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), selectedDay);
            gui.removeWindow(gui.getActiveWindow());
            DashboardView dashboardView = new DashboardView(gui, re);
            gui.addWindowAndWait(dashboardView.getWindow());
            return;
          }
        }
      });
      panel.addComponent(dayCheckBox);
    }

    parent.addComponent(headerPanel);

    parent.addComponent(panel);

    Panel footerPanel = new Panel();
    footerPanel.setLayoutManager(new GridLayout(2));
    Button generatePayrollButton = new Button("Generate Payroll");
    generatePayrollButton.addListener((button) -> {
      if (AppState.selectedPayBeginDate == null || AppState.selectedPayEndDate == null) {
        MessageDialog.showMessageDialog(gui, "Error", "Please select a pay period");
        return;
      }
      gui.addWindowAndWait(new GeneratePayrollView(gui, re).getWindow());
    });

    footerPanel.addComponent(generatePayrollButton);
    parent.addComponent(footerPanel);
    return parent;
  }

  private Panel getPayrollReportsPanel() {
    Panel parent = new Panel();
    parent.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    Panel headerPanel = new Panel();
    headerPanel.setLayoutManager(new GridLayout(2));
    headerPanel.addComponent(new Label(""));
    headerPanel.addComponent(new Label(""));
    headerPanel.addComponent(new Label("Payroll Reports"));
    headerPanel.addComponent(new Label(""));

    parent.addComponent(headerPanel);

    Panel userPanel = new Panel();
    userPanel.setLayoutManager(new GridLayout(3));

    List<User> users = userRepository.findAll();

    for (User user : users) {
      Button viewButton = new Button("View");
      viewButton.addListener(e -> {
        gui.addWindowAndWait(new ShowPayrollReport(gui, user.getId(), re).getWindow());
      });
      userPanel.addComponent(viewButton);
      userPanel.addComponent(new Label(user.getEmployee().getFirstName()));
      userPanel.addComponent(new Label(user.getEmployee().getLastName()));
    }

    parent.addComponent(userPanel);
    return parent;
  }

  private Panel getLeaveRequestsPanel() {
    Panel parent = new Panel();
    parent.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    Panel headerPanel = new Panel();
    headerPanel.setLayoutManager(new GridLayout(2));
    headerPanel.addComponent(new Label(""));
    headerPanel.addComponent(new Label(""));
    headerPanel.addComponent(new Label("Leave Requests"));
    headerPanel.addComponent(new Label(""));

    parent.addComponent(headerPanel);

    Panel leavePanel = new Panel();
    leavePanel.setLayoutManager(new GridLayout(6));

    List<Leave> leaves = re.getLeaveRepository().findAll();

    for (Leave leave: leaves) {
      Button approveButton = new Button("Approve");
      User user = re.getUserRepository().findById(leave.getUser().getId());
      Employee employee = user.getEmployee();
      approveButton.addListener(e -> {
        BasicWindow window = new BasicWindow("Approve Leave");
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));
        panel.addComponent(new Label(""));
        panel.addComponent(new Label(""));
        panel.addComponent(new Label("Approve Leave for " + employee.getFirstName() + " " + employee.getLastName() + "?"));
        panel.addComponent(new Label(""));
        Button yes = new Button("Yes");
        yes.addListener(e1 -> {
          leave.setStatus("Approved");
          re.getLeaveRepository().update(leave);
          window.close();
          gui.removeWindow(gui.getActiveWindow());
          DashboardView dashboardView = new DashboardView(gui, re);
          gui.addWindowAndWait(dashboardView.getWindow());
          AppState.currentView = "Leave Requests";
        });
        Button no = new Button("No");
        no.addListener(e1 -> {
          leave.setStatus("Declined");
          re.getLeaveRepository().update(leave);
          window.close();
          gui.removeWindow(gui.getActiveWindow());
          DashboardView dashboardView = new DashboardView(gui, re);
          gui.addWindowAndWait(dashboardView.getWindow());
          AppState.currentView = "Leave Requests";
        });

        panel.addComponent(yes);
        panel.addComponent(no);
        window.setComponent(panel);
        gui.addWindowAndWait(window);
      });
      leavePanel.addComponent(approveButton);
      if(leave.getStatus().equals("Approved")) {
        approveButton.setEnabled(false);
      } else if(leave.getStatus().equals("Declined")) {
        approveButton.setEnabled(false);
      } else {
        approveButton.setEnabled(true);
      }
      leavePanel.addComponent(new Label(employee.getFirstName()));
      leavePanel.addComponent(new Label(leave.getDateStart()));
      leavePanel.addComponent(new Label(leave.getDateEnd()));
      leavePanel.addComponent(new Label(leave.getReason()));
      leavePanel.addComponent(new Label(leave.getStatus()));
    }

    parent.addComponent(leavePanel);

    return parent;
  }

  private Panel getOvertimeRequestsPanel() {
    Panel parent = new Panel();
    parent.setLayoutManager(new LinearLayout(Direction.VERTICAL));

    Panel headerPanel = new Panel();
    headerPanel.setLayoutManager(new GridLayout(2));
    headerPanel.addComponent(new Label(""));
    headerPanel.addComponent(new Label(""));
    headerPanel.addComponent(new Label("Overtime Requests"));
    headerPanel.addComponent(new Label(""));

    parent.addComponent(headerPanel);

    Panel otPanel = new Panel();
    otPanel.setLayoutManager(new GridLayout(5));

    List<Overtime> overtimes = re.getOvertimeRepository().findAll();

    for (Overtime ot: overtimes) {
      User user = re.getUserRepository().findById(ot.getUser().getId());
      Employee employee = user.getEmployee();
      Button approve = new Button("Approve");
      approve.addListener(e -> {
        BasicWindow window = new BasicWindow("Approve Overtime");
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));
        panel.addComponent(new Label(""));
        panel.addComponent(new Label(""));
        panel.addComponent(new Label("Approve Overtime for " + employee.getFirstName() + " " + employee.getLastName() + "?"));
        panel.addComponent(new Label(""));
        Button yes = new Button("Yes");
        yes.addListener(e1 -> {
          ot.setStatus("Approved");
          re.getOvertimeRepository().update(ot);
          window.close();
          gui.removeWindow(gui.getActiveWindow());
          DashboardView dashboardView = new DashboardView(gui, re);
          gui.addWindowAndWait(dashboardView.getWindow());
          AppState.currentView = "Overtime Requests";
        });
        Button no = new Button("No");
        no.addListener(e1 -> {
          ot.setStatus("Declined");
          re.getOvertimeRepository().update(ot);
          window.close();
          gui.removeWindow(gui.getActiveWindow());
          DashboardView dashboardView = new DashboardView(gui, re);
          gui.addWindowAndWait(dashboardView.getWindow());
          AppState.currentView = "Overtime Requests";
        });

        panel.addComponent(yes);
        panel.addComponent(no);
        window.setComponent(panel);
        gui.addWindowAndWait(window);
      });

      otPanel.addComponent(approve);
      if(ot.getStatus().equals("Approved")) {
        approve.setEnabled(false);
      } else if(ot.getStatus().equals("Declined")) {
        approve.setEnabled(false);
      } else {
        approve.setEnabled(true);
      }

      //calculate total hours based on timeStart and timeEnd of ot
      double totalHours = 0;
      try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime start = LocalTime.parse(ot.getTimeStart(), formatter);
        LocalTime end = LocalTime.parse(ot.getTimeEnd(), formatter);
        totalHours = Duration.between(start, end).toMinutes() / 60.0;
      } catch (Exception e) {
        e.printStackTrace();
      }

      // limit to 2 decimal
      otPanel.addComponent(new Label(String.format("%.2fh", totalHours)));
      otPanel.addComponent(new Label(ot.getDate()));
      otPanel.addComponent(new Label(employee.getFirstName()));
      otPanel.addComponent(new Label(ot.getStatus()));
    }

    parent.addComponent(otPanel);

    return parent;
  }
}
