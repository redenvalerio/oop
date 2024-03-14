package com.mmdc.oop.Repositories;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mmdc.oop.Interfaces.IRepository;
import com.mmdc.oop.Models.Leave;

public class LeaveRepository implements IRepository<Leave, Integer>{
  private Dao<Leave, Integer> dao;

  public LeaveRepository(ConnectionSource connectionSource) {
    try {
      TableUtils.createTableIfNotExists(connectionSource, Leave.class);
      this.dao = DaoManager.createDao(connectionSource, Leave.class);
    } catch (SQLException e) {
      System.err.println("Error creating table: " + e.getMessage());
    }
  }

  @Override
  public Leave findById(Integer id) {
    try {
      return this.dao.queryForId(id);
    } catch (SQLException e) {
      System.err.println("Error finding Leave by id: " + e.getMessage());
      return null;
    }
  }

  @Override
  public List<Leave> findAll() {
    try {
      return this.dao.queryForAll();
    } catch (SQLException e) {
      System.err.println("Error finding all Leaves: " + e.getMessage());
      return null;
    }
  }

  @Override
  public Leave save(Leave entity) {
    try {
      this.dao.create(entity);
      return entity;
    } catch (SQLException e) {
      System.err.println("Error saving Leave: " + e.getMessage());
      return null;
    }
  }

  @Override
  public Leave update(Leave entity) {
    try {
      this.dao.update(entity);
      return entity;
    } catch (SQLException e) {
      System.err.println("Error updating Leave: " + e.getMessage());
      return null;
    }
  }

  @Override
  public void delete(Leave entity) {
    try {
      this.dao.delete(entity);
    } catch (SQLException e) {
      System.err.println("Error deleting Leave: " + e.getMessage());
    }
  }
  
}
