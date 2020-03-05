package com.scudderapps.e_tracker.DAO;

import android.database.Cursor;

import com.scudderapps.e_tracker.DATA.AttendanceDetails;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface AttendanceDAO {

    @Insert
    void addAttendance(AttendanceDetails attendanceDetails);

    @Delete
    void delete(AttendanceDetails attendanceDetails);

    @Query("DELETE from attendance_details where code = :code")
    void deleteSelected(String code);

    @Query("SELECT * FROM attendance_details WHERE code = :code")
    List<AttendanceDetails> employeeSearched(String code);

    @Query("SELECT * FROM attendance_details WHERE code = :code")
    List<AttendanceDetails> empCode(String code);

    @Query("SELECT * , MAX(createdAt)  FROM attendance_details WHERE code = :code")
    List<AttendanceDetails> latestEntry(String code);

    @Query("SELECT * FROM attendance_details where code = :code")
    Cursor currentAttendanceData(String code);

    @Query("SELECT * FROM attendance_details")
    Cursor allAttendanceData();
}
