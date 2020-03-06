package com.scudderapps.e_tracker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class AttendanceActivity extends AppCompatActivity {

    String ECode, EPass;
    EditText eCode, ePass;
    Button CheckInBtn, CheckOutBtn, searchEmployee;
    TextView nameView, codeView, emailView;
    AttendanceDetails attendanceDetails;
    AttendanceDatabase attendanceDatabase;
    String code;
    LinearLayout dataView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_activity);
        CheckInBtn = findViewById(R.id.checkIn);
        CheckOutBtn = findViewById(R.id.checkOut);
        searchEmployee = findViewById(R.id.search);
        eCode = findViewById(R.id.ECode);
        ePass = findViewById(R.id.EPassword);
        nameView = findViewById(R.id.searchedName);
        codeView = findViewById(R.id.searchedCode);
        emailView = findViewById(R.id.searchedEmail);

        CheckOutBtn.setEnabled(false);
        CheckInBtn.setEnabled(false);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        attendanceDetails = new AttendanceDetails();
        attendanceDatabase = Room.databaseBuilder(getApplicationContext(),
                AttendanceDatabase.class, "AttendanceData")
                .allowMainThreadQueries()
                .build();

        String currentDate = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault()).format(new Date());
        final String check_in = currentDate;

        searchEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ECode = eCode.getText().toString();
                EPass = ePass.getText().toString();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchEmployee.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                if (validateFields(ECode) && validatePass(EPass)) {
                    List<EmployeeData> allEmployeeList = MainActivity.employeeDatabase.employeeDAO().allEmployee();
                    for (EmployeeData allData : allEmployeeList) {
                        final String allCode = allData.getCode();
                        String allPass = allData.getPassword();

                        if (ECode.equals(allCode) && EPass.equals(allPass)) {
                            List<EmployeeData> employeeDataList = MainActivity.employeeDatabase.employeeDAO().searchEmployee(ECode, EPass);
                            for (EmployeeData data : employeeDataList) {
                                String name = data.getName();
                                code = data.getCode();
                                String email = data.getEmail();
                                nameView.setText("Name : " + name);
                                codeView.setText("Code : " + code);
                                emailView.setText("Email : " + email);

                                List<AttendanceDetails> statusDetails = attendanceDatabase.attendanceDAO().latestEntry(ECode);
                                List<AttendanceDetails> empCode = attendanceDatabase.attendanceDAO().dataSelected(ECode);
                                if (empCode.size() == 0) {
                                    CheckInBtn.setEnabled(true);
                                    CheckOutBtn.setEnabled(false);
                                    CheckInBtn.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    CheckOutBtn.setBackground(getResources().getDrawable(R.drawable.list_bg));
                                } else {
                                    for (AttendanceDetails statusString : statusDetails) {
                                        String status = statusString.getStatus();
                                        if (status.equals("Checked In")) {
                                            CheckInBtn.setEnabled(false);
                                            CheckOutBtn.setEnabled(true);
                                            CheckOutBtn.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                            CheckInBtn.setBackground(getResources().getDrawable(R.drawable.list_bg));
                                        } else {
                                            CheckInBtn.setEnabled(true);
                                            CheckOutBtn.setEnabled(false);
                                            CheckInBtn.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                            CheckOutBtn.setBackground(getResources().getDrawable(R.drawable.list_bg));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        CheckInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "Checked In";
                attendanceDetails.setCode(code);
                attendanceDetails.setCreatedAt(check_in);
                attendanceDetails.setStatus(status);
                attendanceDatabase.attendanceDAO().addAttendance(attendanceDetails);
                Toast.makeText(AttendanceActivity.this, "Checked In", Toast.LENGTH_SHORT).show();
                back();
            }
        });

        CheckOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "Checked Out";
                attendanceDetails.setCode(code);
                attendanceDetails.setCreatedAt(check_in);
                attendanceDetails.setStatus(status);
                attendanceDatabase.attendanceDAO().addAttendance(attendanceDetails);
                Toast.makeText(AttendanceActivity.this, "Checked Out", Toast.LENGTH_SHORT).show();
                back();
            }
        });
    }

    private boolean validateFields(String Code) {
        if (Code.isEmpty()) {
            eCode.setError(getString(R.string.empty_field_error));
            return false;
        } else {
            eCode.setError(null);
            return true;
        }
    }

    private boolean validatePass(String Pass) {
        if (Pass.isEmpty()) {
            ePass.setError(getString(R.string.pass_error));
            return false;
        } else {
            ePass.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        back();
        super.onBackPressed();
    }

    public void back() {
        Intent home = new Intent(AttendanceActivity.this, MainActivity.class);
        startActivity(home);
        finish();
    }
}
