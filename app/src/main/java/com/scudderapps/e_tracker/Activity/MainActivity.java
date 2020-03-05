package com.scudderapps.e_tracker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.scudderapps.e_tracker.Database.EmployeeDatabase;
import com.scudderapps.e_tracker.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity {

    public static EmployeeDatabase employeeDatabase;
    Button register, attendance, edit, export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        employeeDatabase = Room.databaseBuilder(getApplicationContext(),
                EmployeeDatabase.class, "EmployeeData")
                .allowMainThreadQueries()
                .build();

        register = findViewById(R.id.register);
        attendance = findViewById(R.id.attendance);
        edit = findViewById(R.id.edit);
        export = findViewById(R.id.export);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerpage = new Intent(MainActivity.this, RegisterEmployee.class);
                startActivity(registerpage);
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attendancePage = new Intent(MainActivity.this, CheckInPage.class);
                startActivity(attendancePage);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editPage = new Intent(MainActivity.this, UpdateData.class);
                startActivity(editPage);
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exportPage = new Intent(MainActivity.this, AttendanceData.class);
                startActivity(exportPage);
            }
        });

    }
}
