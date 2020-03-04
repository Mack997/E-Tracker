package com.scudderapps.e_tracker.DAO;

import com.scudderapps.e_tracker.DATA.EmployeeData;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface EmployeeDAO {

    @Insert
    void addEmployee(EmployeeData employeeData);

    @Query("SELECT * FROM employee_data where emp_code = :eCode and password = :ePass")
    List<EmployeeData> getEmployee(String eCode, String ePass);

    @Query("SELECT * FROM employee_data")
    List<EmployeeData> allEmployee();

    @Query("SELECT * FROM employee_data Where emp_code = :code")
    List<EmployeeData> Employee(String code);

    @Update
    void update(EmployeeData employeeData);

    @Query("delete from employee_data where emp_code = :code")
    void delete(String code);
}
