package com.scudderapps.e_tracker.DAO;

import android.database.Cursor;

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

    @Update
    void update(EmployeeData employeeData);

    @Query("SELECT * FROM employee_data where code = :eCode and password = :ePass")
    List<EmployeeData> searchEmployee(String eCode, String ePass);

    @Query("SELECT * FROM employee_data")
    List<EmployeeData> allEmployee();

    @Query("SELECT * FROM employee_data")
    Cursor allEmployeeData();

    @Query("SELECT * FROM employee_data where code = :code")
    Cursor currentEmployeeData(String code);

    @Query("SELECT * FROM employee_data Where code = :code")
    List<EmployeeData> getSelectedEmployee(String code);

    @Query("delete from employee_data where code = :code")
    void delete(String code);
}
