package com.scudderapps.e_tracker.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scudderapps.e_tracker.DATA.AttendanceDetails;
import com.scudderapps.e_tracker.DATA.EmployeeData;
import com.scudderapps.e_tracker.Database.AttendanceDatabase;
import com.scudderapps.e_tracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class CheckInPage extends AppCompatActivity {

    String ECode, EPass;
    EditText eCode, ePass;
    Button CheckIn, CheckOut, searchEmployee;
    TextView nameView, codeView, dobView;

    AttendanceDetails attendanceDetails;
    AttendanceDatabase attendanceDatabase;
    String code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_activity);
        CheckIn = findViewById(R.id.checkIn);
        CheckOut = findViewById(R.id.checkOut);
        searchEmployee = findViewById(R.id.search);
        eCode = findViewById(R.id.ECode);
        ePass = findViewById(R.id.EPassword);
        nameView = findViewById(R.id.searchedName);
        codeView = findViewById(R.id.searchedCode);
        dobView = findViewById(R.id.searchedDob);

        CheckOut.setEnabled(false);
        CheckIn.setEnabled(false);

        attendanceDetails = new AttendanceDetails();
        attendanceDatabase = Room.databaseBuilder(getApplicationContext(),
                AttendanceDatabase.class, "AttendanceData")
                .allowMainThreadQueries()
                .build();

        final String check_in = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault()).format(new Date());

        searchEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ECode = eCode.getText().toString();
                EPass = ePass.getText().toString();

                List<EmployeeData> allEmployeeList = MainActivity.employeeDatabase.employeeDAO().allEmployee();

                for (EmployeeData allData : allEmployeeList) {
                    final String allCode = allData.getCode();
                    String allPass = allData.getPassword();

                    if (ECode.equals(allCode) && EPass.equals(allPass)) {

                        List<EmployeeData> employeeDataList = MainActivity.employeeDatabase.employeeDAO().getEmployee(ECode, EPass);

                        for (EmployeeData data : employeeDataList) {

                            String name = data.getName();
                            code = data.getCode();
                            String dob = data.getDate();

                            nameView.setText(name);
                            codeView.setText(code);
                            dobView.setText(dob);

                            List<AttendanceDetails> statusDetails = attendanceDatabase.attendanceDAO().employeeNew(ECode);

                            List<AttendanceDetails> empCode = attendanceDatabase.attendanceDAO().employeeSearched(ECode);

                            String l = String.valueOf(empCode.size());
                            Log.v("length", l);

                            if (empCode.size() == 0) {
                                CheckIn.setEnabled(true);
                                CheckOut.setEnabled(false);
                                CheckIn.setBackgroundColor(Color.GREEN);
                                CheckOut.setBackgroundColor(Color.GRAY);
                            } else {
                                for (AttendanceDetails statusString : statusDetails) {
                                    String status = statusString.getStatus();
                                    String size = String.valueOf(statusDetails.size());
                                    Log.v("max", size);
                                    if (status.equals("Checked In")) {
                                        CheckIn.setEnabled(false);
                                        CheckOut.setEnabled(true);
                                        CheckOut.setBackgroundColor(Color.GREEN);
                                        CheckIn.setBackgroundColor(Color.GRAY);
                                    } else {
                                        CheckIn.setEnabled(true);
                                        CheckOut.setEnabled(false);
                                        CheckIn.setBackgroundColor(Color.GREEN);
                                        CheckOut.setBackgroundColor(Color.GRAY);
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(CheckInPage.this, "Please enter the correct details", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        CheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "Checked In";
                attendanceDetails.setCode(ECode);
                attendanceDetails.setCreatedAt(check_in);
                attendanceDetails.setStatus(status);

                attendanceDatabase.attendanceDAO().addAttendance(attendanceDetails);
                Toast.makeText(CheckInPage.this, "Checked In", Toast.LENGTH_SHORT).show();
                back();
            }
        });

        CheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "Checked Out";
                attendanceDetails.setCode(ECode);
                attendanceDetails.setCreatedAt(check_in);
                attendanceDetails.setStatus(status);

                attendanceDatabase.attendanceDAO().addAttendance(attendanceDetails);
                Toast.makeText(CheckInPage.this, "Checked Out", Toast.LENGTH_SHORT).show();
                back();
            }
        });
    }

    public void back() {
        Intent home = new Intent(CheckInPage.this, MainActivity.class);
        startActivity(home);
        finish();
    }
}
