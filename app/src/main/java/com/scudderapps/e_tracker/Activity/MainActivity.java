package com.scudderapps.e_tracker.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.scudderapps.e_tracker.Database.EmployeeDatabase;
import com.scudderapps.e_tracker.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity {

    Button register, attendance, edit, export;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAndRequestPermissions();

        register = findViewById(R.id.register);
        attendance = findViewById(R.id.attendance);
        edit = findViewById(R.id.edit);
        export = findViewById(R.id.export);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerpage = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerpage);
                finish();
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attendancePage = new Intent(MainActivity.this, AttendanceActivity.class);
                startActivity(attendancePage);
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editPage = new Intent(MainActivity.this, UpdateActivity.class);
                startActivity(editPage);
                finish();
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exportPage = new Intent(MainActivity.this, ExportActivity.class);
                startActivity(exportPage);
                finish();
            }
        });
    }
    private  boolean checkAndRequestPermissions() {
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        } else {
        }
        return true;
    }
}
