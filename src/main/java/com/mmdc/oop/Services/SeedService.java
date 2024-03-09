package com.mmdc.oop.Services;

import java.util.List;

import com.mmdc.oop.Models.Role;
import com.mmdc.oop.Models.User;
import com.mmdc.oop.Models.UserRole;
import com.mmdc.oop.Repositories.RoleRepository;
import com.mmdc.oop.Repositories.UserRepository;
import com.mmdc.oop.Repositories.UserRoleRepository;
import com.mmdc.oop.Utils.Authentication;

public class SeedService {
  private UserRepository userRepository;
  private RoleRepository roleRepository;
  private UserRoleRepository userRoleRepository;

  public SeedService(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository) { 
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.userRoleRepository = userRoleRepository;
  }

  public void seed() {
    List<User> users = userRepository.findAll();
    List<Role> roles = roleRepository.findAll();
    List<UserRole> userRoles = userRoleRepository.findAll();

    users.stream().filter(user -> user.getUsername().equals("admin")).findFirst().ifPresentOrElse(
      user -> System.out.println("Admin user already exists"),
      () -> {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(Authentication.hashPassword("admin123"));
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

    // create 10 dummy user
    for (int i = 1; i <= 10; i++) {
      User user = new User();
      user.setUsername("user" + i);
      user.setPassword(Authentication.hashPassword("user" + i + "123"));
      userRepository.save(user);
    }

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
