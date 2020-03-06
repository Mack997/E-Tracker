package com.scudderapps.e_tracker.Database;

import android.content.Context;

import com.scudderapps.e_tracker.DAO.AttendanceDAO;
import com.scudderapps.e_tracker.DATA.AttendanceDetails;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {AttendanceDetails.class}, version = 1)

public abstract class AttendanceDatabase extends RoomDatabase {

    public abstract AttendanceDAO attendanceDAO();

    private static AttendanceDatabase instance;

    public static synchronized AttendanceDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AttendanceDatabase.class, "AttendanceData")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

}
