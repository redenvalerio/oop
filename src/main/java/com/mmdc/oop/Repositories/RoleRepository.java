package com.mmdc.oop.Repositories;

import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mmdc.oop.Interfaces.IRepository;
import com.mmdc.oop.Models.Role;

public class RoleRepository implements IRepository<Role, Integer> {

  private Dao<Role, Integer> roleDao;

  public RoleRepository(ConnectionSource connectionSource) {
    try {
      TableUtils.createTableIfNotExists(connectionSource, Role.class);
      roleDao = DaoManager.createDao(connectionSource, Role.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Role findById(Integer id) {
    try {
      return roleDao.queryForId(id);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public List<Role> findAll() {
    try {
      return roleDao.queryForAll();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Role save(Role entity) {
    try {
      roleDao.create(entity);
      return entity;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Role update(Role entity) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public void delete(Role entity) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }
  
}
