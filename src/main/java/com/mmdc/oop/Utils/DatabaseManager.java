package com.mmdc.oop.Utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mmdc.oop.Models.User;

public class DatabaseManager {
    private final String DATABASE_URL = "jdbc:sqlite:./src/main/resources/data/myapp.db";
    private Dao<User, Integer> userDao;

    public DatabaseManager() throws Exception {
        ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL);
        userDao = DaoManager.createDao(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, User.class);

        System.out.println("Database and table creation successful.");

        seedAdminUser();
    }

    public boolean createUser(String username, String password) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
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
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
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
            adminUser.setPassword("admin123");
            userDao.create(adminUser);
        }
    }
}
