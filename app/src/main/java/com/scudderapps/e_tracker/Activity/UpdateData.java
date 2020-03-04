package com.scudderapps.e_tracker.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.scudderapps.e_tracker.DATA.EmployeeData;
import com.scudderapps.e_tracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateData extends AppCompatActivity {

    TextInputEditText searchEmpCode, empName, empPhone, empEmail, empPassword;
    TextView empDob;
    Button searchEmpBtn, updateBtn, deleteBtn;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
    private Calendar c;
    private int month, day, year;
    String emp_dob_updated;
    List<EmployeeData> searchList;
    private EmployeeData employeeData;
    String emp_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_data);
        searchEmpBtn = findViewById(R.id.searchUpdateBtn);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        searchEmpCode = findViewById(R.id.searchUpdateCode);
        empName = findViewById(R.id.searchName);
        empEmail = findViewById(R.id.searchEmail);
        empPhone = findViewById(R.id.searchNumber);
        empPassword = findViewById(R.id.searchPassword);
        empDob = findViewById(R.id.searchDob);
        employeeData = new EmployeeData();


        searchEmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emp_code = searchEmpCode.getText().toString();
                searchList = MainActivity.employeeDatabase.employeeDAO().Employee(emp_code);

                for (EmployeeData employeeData : searchList) {
                    String emp_name = employeeData.getName();
                    String emp_phone = employeeData.getPhone();
                    String emp_email = employeeData.getEmail();
                    String emp_pass = employeeData.getPassword();
                    String emp_dob = employeeData.getDate();

                    empName.setText(emp_name);
                    empPhone.setText(emp_phone);
                    empEmail.setText(emp_email);
                    empPassword.setText(emp_pass);
                    empDob.setText(emp_dob);
                }
            }
        });

        c = Calendar.getInstance();
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        year = c.get(Calendar.YEAR);
        c.set(year, month, day);

        empDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateData.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        UpdateData.this.emp_dob_updated = dateFormat.format(c.getTime());
                        empDob.setText(UpdateData.this.emp_dob_updated);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emp_name_updated = empName.getText().toString();
                String emp_phone_updated = empPhone.getText().toString();
                String emp_email_updated = empEmail.getText().toString();
                String emp_password_updated = empPassword.getText().toString();
                emp_dob_updated = empDob.getText().toString();

                System.out.println(emp_email_updated);
                System.out.println(emp_name_updated);
                System.out.println(emp_password_updated);
                System.out.println(emp_phone_updated);
                System.out.println(emp_dob_updated);

                employeeData.setCode(emp_code);
                employeeData.setName(emp_name_updated);
                employeeData.setEmail(emp_email_updated);
                employeeData.setPassword(emp_password_updated);
                employeeData.setPhone(emp_phone_updated);
                employeeData.setDate(emp_dob_updated);

                List<EmployeeData> updatedDetails = MainActivity.employeeDatabase.employeeDAO().Employee(emp_code);

                if (updatedDetails.size() != 0) {
                    MainActivity.employeeDatabase.employeeDAO().update(employeeData);
                    Toast.makeText(UpdateData.this, "Details updated", Toast.LENGTH_SHORT).show();
                    back();
                } else {
                    searchEmpCode.setError("Employee code already present");
                    Toast.makeText(UpdateData.this, "No Employee Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.employeeDatabase.employeeDAO().delete(emp_code);
                Toast.makeText(UpdateData.this, "Employee Deleted", Toast.LENGTH_SHORT).show();
                back();
            }
        });
    }

    public void back() {
        Intent home = new Intent(UpdateData.this, MainActivity.class);
        startActivity(home);
        finish();
    }
}
