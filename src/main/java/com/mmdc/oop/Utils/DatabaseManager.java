package com.mmdc.oop.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mmdc.oop.Models.Role;
import com.mmdc.oop.Models.User;
import com.mmdc.oop.Models.UserRole;

public class DatabaseManager {
    private final String DATABASE_URL = "jdbc:sqlite:./src/main/resources/data/myapp.db";
    private Authentication authentication;
    private Dao<User, Integer> userDao;
    private Dao<Role, Integer> roleDao;
    private Dao<UserRole, Integer> userRoleDao;

    public DatabaseManager() throws Exception {
        ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL);
        userDao = DaoManager.createDao(connectionSource, User.class);
        roleDao = DaoManager.createDao(connectionSource, Role.class);
        userRoleDao = DaoManager.createDao(connectionSource, UserRole.class);

        TableUtils.createTableIfNotExists(connectionSource, User.class);

        System.out.println("Database and table creation successful.");

        authentication = new Authentication();
        seedAdminUser();
    }

    public boolean createUser(String username, String password) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(authentication.hashPassword(password));
            userDao.create(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User authenticateUser(String username, String password) {
        try {
            for (User user : userDao.queryForAll()) {
                if (user.getUsername().equals(username) && user.getPassword().equals(authentication.hashPassword(password))) {
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void seedAdminUser() throws Exception {
        if (userDao.queryForEq("username", "admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(authentication.hashPassword("admin123"));
            userDao.create(adminUser);

            if (roleDao.queryForEq("name", "admin").isEmpty()) {
                Role adminRole = new Role("admin");
                roleDao.create(adminRole);
            }

            Role adminRole = roleDao.queryForEq("name", "admin").get(0);

            if(userRoleDao.queryForEq("user_id", adminUser.getId()).isEmpty()) {
                assignRoleToUser(adminUser, adminRole);
            }
        }

    }

    public void assignRoleToUser(User user, Role role) throws SQLException {
        UserRole userRole = new UserRole(user, role);
        userRoleDao.create(userRole);
    }

    public List<Role> getUserRoles(User user) throws SQLException {
        QueryBuilder<UserRole, Integer> userRoleQB = userRoleDao.queryBuilder();
        userRoleQB.where().eq("user_id", user.getId());
        List<UserRole> userRoles = userRoleDao.query(userRoleQB.prepare());
        List<Role> roles = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            roles.add(userRole.getRole());
        }
        return roles;
    }

}
