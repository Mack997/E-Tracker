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

    @Query("SELECT * FROM attendance_details WHERE Code = :code")
    List<AttendanceDetails> employeeSearched(String code);

    @Query("SELECT * , MAX(createdAt)  FROM attendance_details WHERE Code = :code")
    List<AttendanceDetails> employeeNew(String code);

    @Delete
    void delete(AttendanceDetails attendanceDetails);

    @Query("DELETE from attendance_details where Code = :code")
    void deleteALl(String code);

    @Query("select A.Code as code, A.Daily_Checkin as checkin_Time, B.Daily_Checkout as checkout_Time " +
            "from (select *,  min(createdAt) as Daily_Checkin from attendance_details where Code = :code and Status = 'Checked In' group by DateTime(createdAt)) A " +
            "INNER JOIN (select *,  max(createdAt) as Daily_Checkout from attendance_details where Code = :code and Status = 'Checked Out' group by DateTime(createdAt)) B " +
            "on A.Code = B.Code")
    Cursor cursor(String code);
}
