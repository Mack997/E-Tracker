package com.scudderapps.e_tracker.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.scudderapps.e_tracker.DAO.EmployeeDAO;
import com.scudderapps.e_tracker.DATA.EmployeeData;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {EmployeeData.class}, version = 1)

public abstract class EmployeeDatabase extends RoomDatabase {

    public abstract EmployeeDAO employeeDAO();
    private static EmployeeDatabase instance;

    public static synchronized EmployeeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    EmployeeDatabase.class, "EmployeeData")
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
