package com.mmdc.oop;

import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    Authentication authentication = new Authentication();

    HashMap <String, String> users = new HashMap<>();
    users.put("dvbondoy", authentication.hashPassword("letmein123"));
    users.put("reden", authentication.hashPassword("qwerty321"));

    HashMap<String, Role> roles = new HashMap<>();
    roles.put("dvbondoy", Role.MANAGER);
    roles.put("reden", Role.EMPLOYEE);

    // lets login
    String username = "dvbondoy";
    String password = "letmein123";

    if(users.containsKey(username) && authentication.authenticate(username, password, users.get(username))) {
      System.out.println("Login successful for " + username);

      if(authentication.authorize(username, roles.get(username))) {
        System.out.println(username + " is authorized as a " + roles.get(username));
      } else {
        System.out.println(username + " is not authorized");
      }
    } else {
      System.out.println("Login failed for " + username);
    }

    Employee employee = new Employee(1, "Dioscoro", Role.MANAGER, 50000);
    System.out.println(employee);

    }
}
