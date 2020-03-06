package com.scudderapps.e_tracker.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.scudderapps.e_tracker.DATA.EmployeeData;
import com.scudderapps.e_tracker.Database.EmployeeDatabase;
import com.scudderapps.e_tracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9][0-9]{9}$");

    TextInputEditText searchEmpCode, empName, empPhone, empEmail, empPassword, empConfirmPassword;
    TextView empDob;
    Button searchEmpBtn, updateBtn, deleteBtn;
    String emp_dob_updated;
    List<EmployeeData> searchList;
    String emp_code;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
    private Calendar c;
    private int month, day, year;
    private EmployeeData employeeData;
    LinearLayout linearLayout;

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
        empConfirmPassword = findViewById(R.id.searchConfirmPassword);
        empDob = findViewById(R.id.searchDob);
        linearLayout = findViewById(R.id.updateLayout);
        linearLayout.setVisibility(View.INVISIBLE);
        employeeData = new EmployeeData();
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        final EmployeeDatabase employeeDatabase = EmployeeDatabase.getInstance(getApplicationContext());

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        empConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                String cPass = mEdit.toString();
                if (!cPass.isEmpty()){
                    updateBtn.setEnabled(true);
                    deleteBtn.setEnabled(true);
                    updateBtn.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                    updateBtn.setTextColor(getResources().getColor(R.color.white));
                } else {
                    updateBtn.setEnabled(false);
                    deleteBtn.setEnabled(false);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        searchEmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emp_code = searchEmpCode.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchEmpBtn.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                searchList = employeeDatabase.employeeDAO().getSelectedEmployee(emp_code);

                if (!searchList.isEmpty()) {
                    for (EmployeeData employeeData : searchList) {
                        linearLayout.setVisibility(View.VISIBLE);
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
                } else {
                    Snackbar snackbar = Snackbar.make(v, "No Records Found", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.BLACK);
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
                    snackbar.show();
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        UpdateActivity.this.emp_dob_updated = dateFormat.format(c.getTime());
                        empDob.setText(UpdateActivity.this.emp_dob_updated);
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

                if (validateUsername() && validatePhoneNumber() && validateEmail() && validatePassword() && validateDatOfBirth()) {
                    employeeData.setCode(emp_code);
                    employeeData.setName(emp_name_updated);
                    employeeData.setEmail(emp_email_updated);
                    employeeData.setPassword(emp_password_updated);
                    employeeData.setPhone(emp_phone_updated);
                    employeeData.setDate(emp_dob_updated);

                    List<EmployeeData> updatedDetails = employeeDatabase.employeeDAO().getSelectedEmployee(emp_code);

                    if (updatedDetails.size() != 0) {
                        employeeDatabase.employeeDAO().update(employeeData);
                        Toast.makeText(UpdateActivity.this, "Details updated", Toast.LENGTH_SHORT).show();
                        back();
                    } else {
                        searchEmpCode.setError("Employee code not found");
                        Toast.makeText(UpdateActivity.this, "No Employee Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePassword()) {
                    employeeDatabase.employeeDAO().delete(emp_code);
                    Toast.makeText(UpdateActivity.this, "Employee Deleted", Toast.LENGTH_SHORT).show();
                    back();
                } else {

                }
            }
        });
    }

    private boolean validateUsername() {
        String usernameInput = empName.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            empName.setError("Field can't be empty");
            return false;
        } else {
            empName.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String emailInput = empEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            empEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            empEmail.setError("Please enter a valid email address");
            return false;
        } else {
            empEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = empPassword.getText().toString().trim();
        String confirmPasswordInput = empConfirmPassword.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            empPassword.setError("Field can't be empty");
            return false;
        } else if (passwordInput.length() < 4) {
            empPassword.setError("Password too short");
            return false;
//        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
//            empPassword.setError("Password too weak");
//            return false;
        } else if (!passwordInput.equals(confirmPasswordInput)) {
            empConfirmPassword.setError("Password do not match");
            return false;
        } else {
            empPassword.setError(null);
            return true;
        }
    }

    private boolean validateDatOfBirth() {
        String dobInput = empDob.getText().toString().trim();
        if (dobInput.isEmpty()) {
            empDob.setError("Field can't be empty");
            return false;
        } else {
            empDob.setError(null);
            return true;
        }
    }

    private boolean validatePhoneNumber() {
        String phoneInput = empPhone.getText().toString().trim();
        if (phoneInput.isEmpty()) {
            empPhone.setError("Field can't be empty");
            return false;
        } else if (!PHONE_PATTERN.matcher(phoneInput).matches()) {
            empPhone.setError("Please enter a valid Phone Number");
            return false;
        } else {
            empPhone.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        back();
        super.onBackPressed();
    }

    public void back() {
        Intent home = new Intent(UpdateActivity.this, MainActivity.class);
        startActivity(home);
        finish();
    }
}
