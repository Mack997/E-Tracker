package com.scudderapps.e_tracker.DAO;

import com.scudderapps.e_tracker.DATA.AttendanceDetails;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface AttendanceDAO {

    @Insert
    void addAttendance(AttendanceDetails attendanceDetails);

//    @Query("SELECT *,FROM attendance_details WHERE Code = :code")
//    List<AttendanceDetails> statusDetail(String code);

    @Query("SELECT * FROM attendance_details WHERE Code = :code")
    List<AttendanceDetails> empCode(String code);

    @Query("SELECT * , MAX(createdAt)  FROM attendance_details WHERE Code = :code")
    List<AttendanceDetails> x(String code);

    @Delete
    void delete(AttendanceDetails attendanceDetails);
}
