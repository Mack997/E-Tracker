package com.scudderapps.e_tracker.Database;

import com.scudderapps.e_tracker.DAO.AttendanceDAO;
import com.scudderapps.e_tracker.DATA.AttendanceDetails;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {AttendanceDetails.class}, version = 1)

public abstract class AttendanceDatabase extends RoomDatabase {

    public abstract AttendanceDAO attendanceDAO();

}
