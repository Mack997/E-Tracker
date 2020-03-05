package com.scudderapps.e_tracker.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterEmployee extends AppCompatActivity {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[7-9][0-9]{9}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^((?=.*[a-zA-Z]).{8,20})");
    private Calendar c;
    private int month, day, year;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
    private TextInputEditText name, code, pass, phone, email;
    private TextView dob;
    private Button save;
    private String eName, ePass, eEmail, eCode, ePhone, eDob;
    private EmployeeData employeeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_employee);

        employeeData = new EmployeeData();

        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        code = findViewById(R.id.ecode);
        pass = findViewById(R.id.password);
        phone = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
        save = findViewById(R.id.save);

        c = Calendar.getInstance();
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        year = c.get(Calendar.YEAR);
        c.set(year, month, day);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterEmployee.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        RegisterEmployee.this.eDob = dateFormat.format(c.getTime());
                        dob.setText(RegisterEmployee.this.eDob);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eCode = code.getText().toString();
                eName = name.getText().toString();
                ePhone = phone.getText().toString();
                eEmail = email.getText().toString();
                ePass = pass.getText().toString();
                eDob = dob.getText().toString();

                if (validateUsername() && validatePhoneNumber() && validateEmail() && validatePassword() && validateDatOfBirth()) {

                    employeeData.setCode(eCode);
                    employeeData.setName(eName);
                    employeeData.setPhone(ePhone);
                    employeeData.setEmail(eEmail);
                    employeeData.setPassword(ePass);
                    employeeData.setDate(eDob);

                    List<EmployeeData> allEmployeeList = MainActivity.employeeDatabase.employeeDAO().getSelectedEmployee(eCode);

                    if (allEmployeeList.size() == 0) {
                        MainActivity.employeeDatabase.employeeDAO().insert(employeeData);
                        Toast.makeText(RegisterEmployee.this, R.string.registration_done, Toast.LENGTH_SHORT).show();
                        back();
                    } else {
                        code.setError("Employee code already present");
                        Toast.makeText(RegisterEmployee.this, R.string.registration_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateUsername() {
        String usernameInput = name.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            name.setError("Field can't be empty");
            return false;
        } else if (!NAME_PATTERN.matcher(usernameInput).matches()) {
            name.setError("Username not valid");
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String emailInput = email.getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = pass.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            pass.setError("Field can't be empty");
            return false;
        } else if (passwordInput.length() < 8) {
            pass.setError("Password too short");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            pass.setError("Password too weak");
            return false;
        } else {
            pass.setError(null);
            return true;
        }
    }

    private boolean validateDatOfBirth() {
        String dobInput = dob.getText().toString().trim();
        if (dobInput.isEmpty()) {
            dob.setError("Field can't be empty");
            return false;
        } else {
            dob.setError(null);
            return true;
        }
    }

    private boolean validatePhoneNumber() {
        String phoneInput = phone.getText().toString().trim();
        if (phoneInput.isEmpty()) {
            phone.setError("Field can't be empty");
            return false;
        } else if (!PHONE_PATTERN.matcher(phoneInput).matches()) {
            phone.setError("Please enter a valid Phone Number");
            return false;
        } else {
            phone.setError(null);
            return true;
        }
    }

    public void back() {
        Intent home = new Intent(RegisterEmployee.this, MainActivity.class);
        startActivity(home);
        finish();
    }
}
