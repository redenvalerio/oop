package com.mmdc.oop.Repositories;

import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mmdc.oop.Interfaces.IRepository;
import com.mmdc.oop.Models.UserRole;

public class UserRoleRepository implements IRepository<UserRole, Integer> {

  private Dao<UserRole, Integer> userRoleDao;

  public UserRoleRepository(ConnectionSource connectionSource) {
    try {
      TableUtils.createTableIfNotExists(connectionSource, UserRole.class);
      userRoleDao = DaoManager.createDao(connectionSource, UserRole.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public UserRole findById(Integer id) {
    try {
      return userRoleDao.queryForId(id);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public List<UserRole> findAll() {
    try {
      return userRoleDao.queryForAll();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public UserRole save(UserRole entity) {
    try {
      userRoleDao.create(entity);
      return entity;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public UserRole update(UserRole entity) {
    try {
      userRoleDao.update(entity);
      return entity;
    } catch (Exception e) {
      e.printStackTrace();
      return null;

    }
  }

  @Override
  public void delete(UserRole entity) {
    try {
      userRoleDao.delete(entity);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
}
