package com.mmdc.oop.Repositories;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mmdc.oop.Interfaces.IRepository;
import com.mmdc.oop.Models.Overtime;

public class OvertimeRepository implements IRepository<Overtime, Integer> {

  private Dao<Overtime, Integer> dao;

  public OvertimeRepository(ConnectionSource connectionSource) {
    try {
      TableUtils.createTableIfNotExists(connectionSource, Overtime.class);
      this.dao = DaoManager.createDao(connectionSource, Overtime.class);
    } catch (SQLException e) {
      System.err.println("Error creating table: " + e.getMessage());
    }
  }

  @Override
  public Overtime findById(Integer id) {
    try {
      return this.dao.queryForId(id);
    } catch (SQLException e) {
      System.err.println("Error finding Overtime by id: " + e.getMessage());
      return null;
    }
  }

  @Override
  public List<Overtime> findAll() {
    try{
      return this.dao.queryForAll();
    } catch (SQLException e) {
      System.err.println("Error finding all Overtimes: " + e.getMessage());
      return null;
    }
  }

  @Override
  public Overtime save(Overtime entity) {
    try {
      this.dao.create(entity);
      return entity;
    } catch (SQLException e) {
      System.err.println("Error saving Overtime: " + e.getMessage());
      return null;
    }
  }

  @Override
  public Overtime update(Overtime entity) {
    try {
      this.dao.update(entity);
      return entity;
    } catch (SQLException e) {
      System.err.println("Error updating Overtime: " + e.getMessage());
      return null;
    }
  }

  @Override
  public void delete(Overtime entity) {
    try {
      this.dao.delete(entity);
    } catch (SQLException e) {
      System.err.println("Error deleting Overtime: " + e.getMessage());
    }
  }
  
}
