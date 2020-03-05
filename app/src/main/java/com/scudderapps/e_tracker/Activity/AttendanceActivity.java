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
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class AttendanceActivity extends AppCompatActivity {

    String empCode, empPass;
    EditText editCode, editPass;
    Button CheckInBtn, CheckOutBtn, searchEmployeeBtn;
    TextView nameView, codeView, emailView;

    AttendanceDetails attendanceDetails;
    AttendanceDatabase attendanceDatabase;
    String fetchedEmpCode;
    LinearLayout dataView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_activity);
        CheckInBtn = findViewById(R.id.checkIn);
        CheckOutBtn = findViewById(R.id.checkOut);
        searchEmployeeBtn = findViewById(R.id.search);
        editCode = findViewById(R.id.ECode);
        editPass = findViewById(R.id.EPassword);
        nameView = findViewById(R.id.searchedName);
        codeView = findViewById(R.id.searchedCode);
        emailView = findViewById(R.id.searchedEmail);

        CheckOutBtn.setEnabled(false);
        CheckInBtn.setEnabled(false);
        dataView = findViewById(R.id.dataView);
        dataView.setVisibility(View.INVISIBLE);

        attendanceDetails = new AttendanceDetails();
        attendanceDatabase = Room.databaseBuilder(getApplicationContext(),
                AttendanceDatabase.class, "AttendanceData")
                .allowMainThreadQueries()
                .build();

        final String check_in = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault()).format(new Date());

        searchEmployeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empCode = editCode.getText().toString();
                empPass = editPass.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchEmployeeBtn.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                if (validateFields() && validatePass()) {
                    List<EmployeeData> allEmployeeList = MainActivity.employeeDatabase.employeeDAO().allEmployee();
                    for (EmployeeData allEmployeeData : allEmployeeList) {
                        final String allEmpCode = allEmployeeData.getCode();
                        String allEmpPass = allEmployeeData.getPassword();

                        if (empCode.equals(allEmpCode) && empPass.equals(allEmpPass)) {
                            dataView.setVisibility(View.VISIBLE);
                            List<EmployeeData> fetchedEmployeeDataList = MainActivity.employeeDatabase.employeeDAO().searchEmployee(empCode, empPass);

                            for (EmployeeData data : fetchedEmployeeDataList) {

                                if (fetchedEmployeeDataList.size() > 0) {
                                    String fetchedEmpName = data.getName();
                                    fetchedEmpCode = data.getCode();
                                    String fetchedEmpEmail = data.getEmail();

                                    nameView.setText(fetchedEmpName);
                                    codeView.setText(empCode);
                                    emailView.setText(fetchedEmpEmail);

                                    List<AttendanceDetails> statusDetails = attendanceDatabase.attendanceDAO().latestEntry(empCode);

                                    List<AttendanceDetails> empCode = attendanceDatabase.attendanceDAO().employeeSearched(AttendanceActivity.this.empCode);

                                    if (empCode.size() == 0) {
                                        CheckInBtn.setEnabled(true);
                                        CheckOutBtn.setEnabled(false);
                                        CheckInBtn.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                        CheckOutBtn.setBackground(getResources().getDrawable(R.drawable.list_bg));
                                    } else {
                                        for (AttendanceDetails statusString : statusDetails) {
                                            String status = statusString.getStatus();
                                            String size = String.valueOf(statusDetails.size());
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
                                } else {
                                    dataView.setVisibility(View.INVISIBLE);
                                    Toast.makeText(AttendanceActivity.this, "Please enter the correct details", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            dataView.setVisibility(View.INVISIBLE);
                            Toast.makeText(AttendanceActivity.this, "Please enter the correct details", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        CheckInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "Checked In";
                attendanceDetails.setCode(empCode);
                attendanceDetails.setCreatedAt(check_in);
                attendanceDetails.setStatus(status);

                attendanceDatabase.attendanceDAO().insert(attendanceDetails);
                Toast.makeText(AttendanceActivity.this, "Checked In", Toast.LENGTH_SHORT).show();
                back();
            }
        });

        CheckOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "Checked Out";
                attendanceDetails.setCode(empCode);
                attendanceDetails.setCreatedAt(check_in);
                attendanceDetails.setStatus(status);

                attendanceDatabase.attendanceDAO().insert(attendanceDetails);
                Toast.makeText(AttendanceActivity.this, "Checked Out", Toast.LENGTH_SHORT).show();
                back();
            }
        });
    }

    private boolean validateFields() {
        String Code = editCode.getText().toString().trim();

        if (Code.isEmpty()) {
            editCode.setError(getString(R.string.empty_field_error));
            return false;
        } else {
            editCode.setError(null);
            return true;
        }
    }

    private boolean validatePass() {
        String Pass = editPass.getText().toString().trim();
        if (Pass.isEmpty()) {
            editPass.setError(getString(R.string.pass_error));
            return false;
        } else {
            editPass.setError(null);
            return true;
        }
    }

    public void back() {
        Intent home = new Intent(AttendanceActivity.this, MainActivity.class);
        startActivity(home);
        finish();
    }
}
