package com.scudderapps.e_tracker.Database;

import com.scudderapps.e_tracker.DAO.EmployeeDAO;
import com.scudderapps.e_tracker.DATA.EmployeeData;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {EmployeeData.class}, version = 1)

public abstract class EmployeeDatabase extends RoomDatabase {

    public abstract EmployeeDAO employeeDAO();

}
