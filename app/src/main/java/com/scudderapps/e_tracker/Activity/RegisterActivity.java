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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private Calendar c;
    private int month, day, year;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");

    private TextInputEditText name, code, pass, phone, email, cPass;
    private TextView dob;
    private Button save;
    private String eName, ePass, eEmail, eCode, ePhone, eDob,eCPass;

    private EmployeeData employeeData;

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[7-9][0-9]{9}$");

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
        cPass = findViewById(R.id.confirmPassword);
        save = findViewById(R.id.save);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        c = Calendar.getInstance();
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        year = c.get(Calendar.YEAR);
        c.set(year, month, day);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        RegisterActivity.this.eDob = dateFormat.format(c.getTime());
                        dob.setText(RegisterActivity.this.eDob);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eCode = code.getText().toString().trim();
                eName = name.getText().toString().trim();
                ePhone = phone.getText().toString().trim();
                eEmail = email.getText().toString().trim();
                ePass = pass.getText().toString().trim();
                eDob = dob.getText().toString().trim();
                eCPass = cPass.getText().toString().trim();

                if (validateUsername(eName) && validatePhoneNumber(ePhone) && validateEmail(eEmail) && validatePassword(ePass, eCPass) && validateDatOfBirth(eDob)) {

                    employeeData.setCode(eCode);
                    employeeData.setName(eName);
                    employeeData.setPhone(ePhone);
                    employeeData.setEmail(eEmail);
                    employeeData.setPassword(ePass);
                    employeeData.setDate(eDob);

                    List<EmployeeData> allEmployeeList = MainActivity.employeeDatabase.employeeDAO().getSelectedEmployee(eCode);

                    if (allEmployeeList.size() == 0) {
                        MainActivity.employeeDatabase.employeeDAO().addEmployee(employeeData);
                        Toast.makeText(RegisterActivity.this, R.string.registration_done, Toast.LENGTH_SHORT).show();
                        back();
                    } else {
                        code.setError("Employee code already present");
                        Toast.makeText(RegisterActivity.this, R.string.registration_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateUsername(String usernameInput) {
        if (usernameInput.isEmpty()) {
            name.setError("Field can't be empty");
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }

    private boolean validateEmail(String emailInput) {
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

    private boolean validatePassword(String passwordInput, String confirmPasswordInput) {

        if (passwordInput.isEmpty()) {
            pass.setError("Field can't be empty");
            return false;
        } else if (passwordInput.length() < 4) {
            pass.setError("Password too short");
            return false;
        }else if (!passwordInput.equals(confirmPasswordInput)) {
            cPass.setError("Passwords do not match");
            return false;
//        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
//            pass.setError("Password too weak");
//            return false;
        } else {
            pass.setError(null);
            return true;
        }
    }

    private boolean validateDatOfBirth(String dobInput) {
        if (dobInput.isEmpty()) {
            dob.setError("Field can't be empty");
            return false;
        } else {
            dob.setError(null);
            return true;
        }
    }

    private boolean validatePhoneNumber(String phoneInput) {
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

    @Override
    public void onBackPressed() {
        back();
        super.onBackPressed();
    }

    public void back() {
        Intent home = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(home);
        finish();
    }
}
