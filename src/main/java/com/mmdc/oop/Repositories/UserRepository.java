package com.mmdc.oop.Repositories;

import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mmdc.oop.Interfaces.IRepository;
import com.mmdc.oop.Models.User;

public class UserRepository implements IRepository<User, Integer> {

  private Dao<User, Integer> userDao;

  public UserRepository(ConnectionSource connectionSource) {
    try {
      TableUtils.createTableIfNotExists(connectionSource, User.class);
      userDao = DaoManager.createDao(connectionSource, User.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public User findById(Integer id) {
    try {
      return userDao.queryForId(id);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public List<User> findAll() {
    try {
      return userDao.queryForAll();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public User save(User entity) {
    try {
      userDao.create(entity);
      return entity;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public User update(User entity) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public void delete(User entity) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }
}
