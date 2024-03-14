package com.mmdc.oop.Repositories;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mmdc.oop.Interfaces.IRepository;
import com.mmdc.oop.Models.Payroll;

public class PayrollRepository implements IRepository<Payroll, Integer> {

  private Dao<Payroll, Integer> dao;

  public PayrollRepository(ConnectionSource connectionSource) {
    try {
      TableUtils.createTableIfNotExists(connectionSource, Payroll.class);
      dao = DaoManager.createDao(connectionSource, Payroll.class);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Payroll findById(Integer id) {
    try {
      return dao.queryForId(id);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public List<Payroll> findAll() {
    try {
      return dao.queryForAll();
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Payroll save(Payroll entity) {
    try {
      dao.create(entity);
      return entity;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Payroll update(Payroll entity) {
    try {
      dao.update(entity);
      return entity;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void delete(Payroll entity) {
    try { 
      dao.delete(entity);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
}
