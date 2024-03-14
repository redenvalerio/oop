package com.mmdc.oop.Repositories;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mmdc.oop.Interfaces.IRepository;
import com.mmdc.oop.Models.Attendance;

public class AttendanceRepository implements IRepository<Attendance, Integer> {

  private Dao<Attendance, Integer> dao;

  public AttendanceRepository(ConnectionSource connectionSource) {
    try {
      TableUtils.createTableIfNotExists(connectionSource, Attendance.class);
      this.dao = DaoManager.createDao(connectionSource, Attendance.class);
    } catch (SQLException e) {
      System.err.println("Error creating table: " + e.getMessage());
    }
  }

  @Override
  public Attendance findById(Integer id) {
    try {
      return this.dao.queryForId(id);
    } catch (SQLException e) {
      System.err.println("Error finding Attendance by id: " + e.getMessage());
      return null;
    }
  }

  @Override
  public List<Attendance> findAll() {
    try {
      return this.dao.queryForAll();
    } catch (SQLException e) {
      System.err.println("Error finding all Attendances: " + e.getMessage());
      return null;
    }
  }

  @Override
  public Attendance save(Attendance entity) {
    try {
      this.dao.create(entity);
      return entity;
    } catch (SQLException e) {
      System.err.println("Error saving Attendance: " + e.getMessage());
      return null;
    }
  }

  @Override
  public Attendance update(Attendance entity) {
    try {
      this.dao.update(entity);
      return entity;
    } catch (SQLException e) {
      System.err.println("Error updating Attendance: " + e.getMessage());
      return null;
    }
  }

  @Override
  public void delete(Attendance entity) {
    try {
      this.dao.delete(entity);
    } catch (SQLException e) {
      System.err.println("Error deleting Attendance: " + e.getMessage());
    }
  }
  
}
