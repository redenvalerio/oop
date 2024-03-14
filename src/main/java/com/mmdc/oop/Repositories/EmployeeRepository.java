package com.mmdc.oop.Repositories;

import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mmdc.oop.Interfaces.IRepository;
import com.mmdc.oop.Models.Employee;

public class EmployeeRepository implements IRepository<Employee, Integer> {

  private Dao<Employee, Integer> employeeDao;

  public EmployeeRepository(ConnectionSource connectionSource) {
    try {
      TableUtils.createTableIfNotExists(connectionSource, Employee.class);
      this.employeeDao = DaoManager.createDao(connectionSource, Employee.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Employee findById(Integer id) {
    try {
      return employeeDao.queryForId(id);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public List<Employee> findAll() {
    try {
      return employeeDao.queryForAll();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Employee save(Employee entity) {
    try {
      employeeDao.create(entity);
      return entity;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Employee update(Employee entity) {
    try {
      employeeDao.update(entity);
      return entity;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void delete(Employee entity) {
    try {
      employeeDao.delete(entity);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
}
