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
    void insert(AttendanceDetails attendanceDetails);

    @Delete
    void delete(AttendanceDetails attendanceDetails);

    @Query("DELETE from attendance_details where code = :code")
    void deleteALl(String code);

    @Query("SELECT * FROM attendance_details")
    Cursor allAttendanceData();

    @Query("SELECT * FROM attendance_details WHERE code = :code")
    List<AttendanceDetails> employeeSearched(String code);

    @Query("SELECT * , MAX(createdAt)  FROM attendance_details WHERE code = :code")
    List<AttendanceDetails> latestEntry(String code);

    @Query("select A.code as code, A.Daily_Checkin as checkin_Time, B.Daily_Checkout as checkout_Time " +
            "from (select *,  min(createdAt) as Daily_Checkin from attendance_details where code = :code and status = 'Checked In' group by DateTime(createdAt)) A " +
            "INNER JOIN (select *,  max(createdAt) as Daily_Checkout from attendance_details where code = :code and status = 'Checked Out' group by DateTime(createdAt)) B " +
            "on A.code = B.code")
    Cursor totalTimeCursor(String code);
}
