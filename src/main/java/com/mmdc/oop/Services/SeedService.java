package com.mmdc.oop.Services;

import java.util.Date;
import java.util.List;

import com.mmdc.oop.DTO.RepositoriesDto;
import com.mmdc.oop.Models.Employee;
import com.mmdc.oop.Models.Role;
import com.mmdc.oop.Models.User;
import com.mmdc.oop.Models.UserRole;
import com.mmdc.oop.Repositories.EmployeeRepository;
import com.mmdc.oop.Repositories.RoleRepository;
import com.mmdc.oop.Repositories.UserRepository;
import com.mmdc.oop.Repositories.UserRoleRepository;
import com.mmdc.oop.Utils.Authentication;

public class SeedService {
  private UserRepository userRepository;
  private RoleRepository roleRepository;
  private UserRoleRepository userRoleRepository;
  private EmployeeRepository employeeRepository;

  public SeedService(RepositoriesDto repositoriesDto) { 
    this.userRepository = repositoriesDto.getUserRepository();
    this.roleRepository = repositoriesDto.getRoleRepository();
    this.userRoleRepository = repositoriesDto.getUserRoleRepository();
    this.employeeRepository = repositoriesDto.getEmployeeRepository();
  }

  public void seed() {
    List<User> users = userRepository.findAll();
    List<Role> roles = roleRepository.findAll();
    List<UserRole> userRoles = userRoleRepository.findAll();

    users.stream().filter(user -> user.getUsername().equals("admin")).findFirst().ifPresentOrElse(
      user -> System.out.println("Admin user already exists"),
      () -> {
        Employee employee = new Employee();
        employee.setFirstName("Admin");
        employee.setLastName("Admin");
        employee.setDateOfBirth("01/01/2000");

        employee.setAddress("Placeholder Address");

        employee.setPhone("1234567890");
        employee.setEmail("admin@test.com");

        employee.setDepartment("Systems");
        employee.setPosition("Admin");

        employee.setHireDate(new Date().toString());

        employee.setEmployeeType("Full Time");;
        employee.setEmployeeStatus("Active");
        employeeRepository.save(employee);

        User user = new User();
        user.setUsername("admin");
        user.setPassword(Authentication.hashPassword("admin123"));
        user.setEmployee(employee);
        userRepository.save(user);
        System.out.println("Admin user created");
      });

    roles.stream().filter(role -> role.getRoleName().equals("admin")).findFirst().ifPresentOrElse(
      role -> System.out.println("Admin role already exists"),
      () -> {
        Role role = new Role();
        role.setRoleName("admin");
        roleRepository.save(role);
        System.out.println("Admin role created");
      });


    String[] roleNames = {"payroll", "hr", "employee"};
    for (String roleName : roleNames) {
      roles.stream().filter(role -> role.getRoleName().equals(roleName)).findFirst().ifPresentOrElse(
        role -> System.out.println(roleName + " role already exists"),
        () -> {
          Role role = new Role();
          role.setRoleName(roleName);
          roleRepository.save(role);
          System.out.println(roleName + " role created");
        });
    }

    userRoles.stream().filter(userRole -> userRole.getUser().getUsername().equals("admin")).findFirst().ifPresentOrElse(
      userRole -> System.out.println("Admin user role already exists"),
      () -> {
        List<User> usersList = userRepository.findAll();
        List<Role> rolesList = roleRepository.findAll();
        User user = usersList.stream().filter(u -> u.getUsername().equals("admin")).findFirst().get();
        Role role = rolesList.stream().filter(r -> r.getRoleName().equals("admin")).findFirst().get();
        UserRole userRole = new UserRole(user, role);
        userRoleRepository.save(userRole);
        System.out.println("Admin user role created");
      });

    System.out.println("Seeding completed");
  }

  public void clear() {
    List<User> users = userRepository.findAll();
    List<Role> roles = roleRepository.findAll();

    users.forEach(user -> userRepository.delete(user));
    roles.forEach(role -> roleRepository.delete(role));

    System.out.println("Database cleared");
  }

  public void createUser(String username, String password) {
    User user = new User();
    user.setUsername(username);
    user.setPassword(Authentication.hashPassword(password));
    userRepository.save(user);
  }
}
