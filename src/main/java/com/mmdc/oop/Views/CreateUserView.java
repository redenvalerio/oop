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
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.mmdc.oop.DTO.RepositoriesDto;
import com.mmdc.oop.Interfaces.IView;
import com.mmdc.oop.Models.Employee;
import com.mmdc.oop.Models.Role;
import com.mmdc.oop.Models.User;
import com.mmdc.oop.Models.UserRole;
import com.mmdc.oop.Repositories.EmployeeRepository;
import com.mmdc.oop.Repositories.RoleRepository;
import com.mmdc.oop.Repositories.UserRepository;
import com.mmdc.oop.Repositories.UserRoleRepository;
import com.mmdc.oop.Utils.Authentication;

public class CreateUserView implements IView {

  private MultiWindowTextGUI gui;
  private BasicWindow window;
  private EmployeeRepository employeeRepository;

  private Panel panel;

  public CreateUserView(MultiWindowTextGUI gui, RepositoriesDto repositoriesDto) {
    UserRepository userRepository = repositoriesDto.getUserRepository();
    RoleRepository roleRepository = repositoriesDto.getRoleRepository();
    UserRoleRepository userRoleRepository = repositoriesDto.getUserRoleRepository();

    this.employeeRepository = repositoriesDto.getEmployeeRepository();

    this.gui = gui;
    this.window = new BasicWindow("Create User");

    this.panel = new Panel();
    this.panel.setLayoutManager(new GridLayout(2));

    this.panel.addComponent(new Label("Login Information"));
    this.panel.addComponent(new Label(""));

    //username
    TextBox usernameTextBox = new TextBox();
    this.panel.addComponent(new Label("Username:"));
    this.panel.addComponent(usernameTextBox);

    //password
    TextBox passwordTextBox = new TextBox().setMask('*');
    this.panel.addComponent(new Label("Password:"));
    this.panel.addComponent(passwordTextBox);

    //confirm password
    TextBox confirmPasswordTextBox = new TextBox().setMask('*');
    this.panel.addComponent(new Label("Confirm Password:"));
    this.panel.addComponent(confirmPasswordTextBox);

    Panel parentPanel = new Panel();
    parentPanel.setLayoutManager(new GridLayout(2));

    parentPanel.addComponent(this.panel);

    Panel employeeInfoPanel = new Panel();
    employeeInfoPanel.addComponent(new Label("Employee Information"));
    employeeInfoPanel.addComponent(new Label(""));
    employeeInfoPanel.setLayoutManager(new GridLayout(2));
    employeeInfoPanel.addComponent(new Label("First Name*:"));
    TextBox firstNameTextBox = new TextBox();
    employeeInfoPanel.addComponent(firstNameTextBox);

    employeeInfoPanel.addComponent(new Label("Last Name*:"));
    TextBox lastNameTextBox = new TextBox();
    employeeInfoPanel.addComponent(lastNameTextBox);

    employeeInfoPanel.addComponent(new Label("Date of Birth*:"));
    TextBox dateOfBirthTextBox = new TextBox();
    employeeInfoPanel.addComponent(dateOfBirthTextBox);

    employeeInfoPanel.addComponent(new Label("Address*:"));
    TextBox addressTextBox = new TextBox();
    employeeInfoPanel.addComponent(addressTextBox);

    employeeInfoPanel.addComponent(new Label("Phone*:"));
    TextBox phoneTextBox = new TextBox();
    employeeInfoPanel.addComponent(phoneTextBox);

    employeeInfoPanel.addComponent(new Label("Email*:"));
    TextBox emailTextBox = new TextBox();
    employeeInfoPanel.addComponent(emailTextBox);

    employeeInfoPanel.addComponent(new Label("Department*:"));
    TextBox departmentTextBox = new TextBox();
    employeeInfoPanel.addComponent(departmentTextBox);

    employeeInfoPanel.addComponent(new Label("Position*:"));
    TextBox positionTextBox = new TextBox();
    employeeInfoPanel.addComponent(positionTextBox);

    employeeInfoPanel.addComponent(new Label("Hire Date*:"));
    TextBox hireDateTextBox = new TextBox();
    employeeInfoPanel.addComponent(hireDateTextBox);

    employeeInfoPanel.addComponent(new Label("Salary*:"));
    TextBox salaryTextBox = new TextBox();
    employeeInfoPanel.addComponent(salaryTextBox);

    employeeInfoPanel.addComponent(new Label("Hourly Rate*:"));
    TextBox hourlyRateTextBox = new TextBox();
    employeeInfoPanel.addComponent(hourlyRateTextBox);

    parentPanel.addComponent(employeeInfoPanel);
    parentPanel.addComponent(new Label(""));
    parentPanel.addComponent(new Label(""));

    //Role 
    Panel roleContainer = new Panel();
    roleContainer.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
    List<Role> roles = roleRepository.findAll();

    List<CheckBox> checkBoxList = new ArrayList<>();
    for (Role role : roles) {
      checkBoxList.add(new CheckBox(role.getId() + ""));
      roleContainer.addComponent(checkBoxList.get(roles.indexOf(role)));
      roleContainer.addComponent(new Label(role.getRoleName()));
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

    //Work Days
    Panel workDaysContainer = new Panel();
    workDaysContainer.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
    List<CheckBox> workDaysCheckBoxList = new ArrayList<>();
    // SUN,MON,TUE,WED,THU,FRI,SAT
    String[] workDays = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    String employeeWorkDays = "SUN,MON,TUE,WED,THU,FRI,SAT"; 

    for (String workDay : workDays) {
      CheckBox checkBox = new CheckBox(workDay);
      checkBox.setChecked(employeeWorkDays.contains(workDay));
      workDaysCheckBoxList.add(checkBox);
      workDaysContainer.addComponent(checkBox);
    }


    parentPanel.addComponent(new Label(""));
    parentPanel.addComponent(new Label("Select User Role*:"));
    parentPanel.addComponent(new Label(""));
    parentPanel.addComponent(roleContainer);
    parentPanel.addComponent(new Label(""));
    parentPanel.addComponent(new Label(""));
    parentPanel.addComponent(new Label(""));
    parentPanel.addComponent(new Label("Select Work Days*:"));
    parentPanel.addComponent(new Label(""));
    parentPanel.addComponent(workDaysContainer);
    parentPanel.addComponent(new Label(""));
    parentPanel.addComponent(new Label(""));
    parentPanel.addComponent(new Label(""));
    parentPanel.addComponent(new Label("Select Shift Hours*:"));

    Panel shiftHoursContainer = new Panel();
    shiftHoursContainer.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
    // shiftStartTime, shiftEndTime
    TextBox shiftStartTimeTextBox = new TextBox();
    shiftStartTimeTextBox.setText("08:00");
    shiftHoursContainer.addComponent(shiftStartTimeTextBox);
    shiftHoursContainer.addComponent(new Label("to"));
    TextBox shiftEndTimeTextBox = new TextBox();
    shiftEndTimeTextBox.setText("17:00");
    shiftHoursContainer.addComponent(shiftEndTimeTextBox);
    parentPanel.addComponent(new Label(""));
    parentPanel.addComponent(shiftHoursContainer);
    parentPanel.addComponent(new Label(""));
    parentPanel.addComponent(new Label(""));

    //create user button
    Button createUserButton = new Button("Confirm Create");
    createUserButton.addListener((button) -> {
      //require all fields
      if(usernameTextBox.getText().isEmpty() || passwordTextBox.getText().isEmpty() || confirmPasswordTextBox.getText().isEmpty() || firstNameTextBox.getText().isEmpty() || lastNameTextBox.getText().isEmpty() || dateOfBirthTextBox.getText().isEmpty() || addressTextBox.getText().isEmpty() || phoneTextBox.getText().isEmpty() || emailTextBox.getText().isEmpty() || departmentTextBox.getText().isEmpty() || positionTextBox.getText().isEmpty() || hireDateTextBox.getText().isEmpty() || salaryTextBox.getText().isEmpty() || hourlyRateTextBox.getText().isEmpty()){
        MessageDialog.showMessageDialog(gui, "Error", "All fields are required");
        return;
      }

      if(checkBoxList.stream().noneMatch(CheckBox::isChecked)){
        MessageDialog.showMessageDialog(gui, "Error", "Select a role");
        return;
      }

      // check if salary and hourly rate are numbers
      try {
        Double.parseDouble(salaryTextBox.getText());
        Double.parseDouble(hourlyRateTextBox.getText());
      } catch (NumberFormatException e) {
        MessageDialog.showMessageDialog(gui, "Error", "Salary and hourly rate must be numbers");
        return;
      }

      User user = new User();
      Role role = roleRepository.findById(Integer.parseInt(checkBoxList.stream().filter(CheckBox::isChecked).findFirst().get().getLabel().toString()));
      user.setUsername(usernameTextBox.getText());
      if(!passwordTextBox.getText().equals(confirmPasswordTextBox.getText())){
        MessageDialog.showMessageDialog(gui, "Error", "Passwords do not match");
        return;
      }

      if(userRepository.findAll().stream().anyMatch(u -> u.getUsername().equals(usernameTextBox.getText()))){
        MessageDialog.showMessageDialog(gui, "Error", "Username already exists");
        return;
      }

      if(shiftStartTimeTextBox.getText().isEmpty() || shiftEndTimeTextBox.getText().isEmpty()) {
        MessageDialog.showMessageDialog(gui, "Error", "Shift start and end time are required");
        return;
      }

      //check if shiftStartTime and shiftEndTime are valid HH:mm
      if(!shiftStartTimeTextBox.getText().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$") || !shiftEndTimeTextBox.getText().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
        MessageDialog.showMessageDialog(gui, "Error", "Shift start and end time must be in HH:mm format");
        return;
      }

      //workdays check
      if(workDaysCheckBoxList.stream().noneMatch(CheckBox::isChecked)){
        MessageDialog.showMessageDialog(gui, "Error", "Select work days");
        return;
      }

      Employee employee = new Employee();
      employee.setFirstName(firstNameTextBox.getText());
      employee.setLastName(lastNameTextBox.getText());
      employee.setDateOfBirth(dateOfBirthTextBox.getText());
      employee.setAddress(addressTextBox.getText());
      employee.setPhone(phoneTextBox.getText());
      employee.setEmail(emailTextBox.getText());
      employee.setDepartment(departmentTextBox.getText());
      employee.setPosition(positionTextBox.getText());
      employee.setHireDate(hireDateTextBox.getText());
      employee.setSalary(Double.parseDouble(salaryTextBox.getText()));
      employee.setHourlyRate(Double.parseDouble(hourlyRateTextBox.getText()));

      String newWorkDays = workDaysCheckBoxList.stream().filter(CheckBox::isChecked).map(cb -> cb.getLabel().toString()).reduce((a, b) -> a + "," + b).get().toUpperCase();
      if(newWorkDays.isEmpty()) {
        MessageDialog.showMessageDialog(gui, "Error", "Select at least one work day");
        return;
      }
      employee.setWorkDays(newWorkDays);

      employee.setShiftStartTime(shiftStartTimeTextBox.getText());
      employee.setShiftEndTime(shiftEndTimeTextBox.getText());

      employeeRepository.save(employee);

      user.setPassword(Authentication.hashPassword(passwordTextBox.getText()));
      user.setEmployee(employee);

      userRepository.save(user);

      UserRole userRole = new UserRole(user, role);
      userRoleRepository.save(userRole);

      MessageDialog.showMessageDialog(gui, "Success", "User created");
      gui.removeWindow(window);
      gui.removeWindow(gui.getActiveWindow());
      gui.addWindowAndWait(new DashboardView(gui, repositoriesDto).getWindow());
    });
    parentPanel.addComponent(createUserButton);

    //Cancel button
    Button cancelButton = new Button("Cancel");
    cancelButton.addListener((button) -> {
      gui.removeWindow(window);
      gui.removeWindow(gui.getActiveWindow());
      gui.addWindowAndWait(new DashboardView(gui, repositoriesDto).getWindow());
    });
    parentPanel.addComponent(cancelButton);

    this.window.setComponent(parentPanel);
  }

  @Override
  public BasicWindow getWindow() {
    return window;
  }
}
