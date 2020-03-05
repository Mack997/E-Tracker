package com.scudderapps.e_tracker.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.scudderapps.e_tracker.AttendanceAdapter;
import com.scudderapps.e_tracker.DATA.AttendanceDetails;
import com.scudderapps.e_tracker.Database.AttendanceDatabase;
import com.scudderapps.e_tracker.R;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class AttendanceData extends AppCompatActivity {

    TextInputEditText empCode;
    Button attendanceBtn, deleteAllDataBtn;
    RecyclerView dataView;
    AttendanceAdapter attendanceAdapter;
    AttendanceDatabase attendanceDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_data);
        attendanceBtn = findViewById(R.id.attendanceBtn);
        deleteAllDataBtn = findViewById(R.id.deleteAllData);
        dataView = findViewById(R.id.dataView);
        empCode = findViewById(R.id.getDateCode);
        dataView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        dataView.setHasFixedSize(true);
        deleteAllDataBtn.setEnabled(false);

        attendanceDatabase = Room.databaseBuilder(getApplicationContext(),
                AttendanceDatabase.class, "AttendanceData")
                .allowMainThreadQueries()
                .build();

        attendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String code = empCode.getText().toString();
                if (validateFields()) {
                    List<AttendanceDetails> attendanceDetailsList = attendanceDatabase.attendanceDAO().employeeSearched(code);
                    if (!attendanceDetailsList.isEmpty()) {
                        deleteAllDataBtn.setEnabled(true);
                        attendanceAdapter = new AttendanceAdapter();
                        dataView.setAdapter(attendanceAdapter);
                        attendanceAdapter.setAttendanceData(attendanceDetailsList);
                        attendanceAdapter.setOnItemClickListener(new AttendanceAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(final AttendanceDetails attendanceDetails, final int position) {
                                attendanceDatabase.attendanceDAO().delete(attendanceAdapter.getAttendanceAt(position));
                                createSnackbar(v, "Deleted");

                            }
                        });
                    } else {
                        createSnackbar(v, "No Records Founds");
                    }
                }
            }
        });

        deleteAllDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = empCode.getText().toString();
                attendanceDatabase.attendanceDAO().deleteALl(code);
                createSnackbar(v, "All records deleted");
                back();
            }
        });
    }

    public void createSnackbar(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.BLACK);
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.export, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.exportAllEmpDetails:
                exportAllEmployeeData();
                return true;
            case R.id.exportCurrentEmpRecord:
                exportCurrentEmployeeData();
                return true;
            case R.id.exportAllAttendanceRecord:
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean validateFields() {
        String Code = empCode.getText().toString().trim();

        if (Code.isEmpty()) {
            empCode.setError(getString(R.string.empty_field_error));
            return false;
        } else {
            empCode.setError(null);
            return true;
        }
    }

    public void back() {
        Intent home = new Intent(AttendanceData.this, MainActivity.class);
        startActivity(home);
        finish();
    }

    public void exportCurrentEmployeeData() {
        String CODE = empCode.getText().toString();
        String currentTime = new SimpleDateFormat(" d MMM yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        final String fileName = "AttendanceData-" + currentTime + ".xls";
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        Cursor cursor = attendanceDatabase.attendanceDAO().cursor(CODE);

        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/ETracker/Attendance Data");
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        File file = new File(directory, fileName);

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Attendance Data", 0);

            try {
                sheet.addCell(new Label(0, 0, "Employee Code"));
                sheet.addCell(new Label(1, 0, "CheckIn Time"));
                sheet.addCell(new Label(2, 0, "CheckOut Time"));
//                sheet.addCell(new Label(3, 0, "Total Time"));
                if (cursor.moveToFirst()) {
                    do {
                        String emp_code = cursor.getString(cursor.getColumnIndex("code"));
                        String checkIn = cursor.getString(cursor.getColumnIndex("checkin_Time"));
                        String checkOut = cursor.getString(cursor.getColumnIndex("checkout_Time"));
//                        String duration = cursor.getString(cursor.getColumnIndex("HoursWorked"));
                        System.out.print(emp_code);
                        System.out.print(checkIn);
                        System.out.print(checkOut);
//                        System.out.print(duration);
                        Date date = null;
                        Date date2 = null;
                        try {
                            date = new SimpleDateFormat(getString(R.string.date_format)).parse(checkIn);
                            date2 = new SimpleDateFormat(getString(R.string.date_format)).parse(checkOut);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        int i = cursor.getPosition() + 1;
                        sheet.addCell(new Label(0, i, emp_code));
                        sheet.addCell(new Label(1, i, date.toString()));
                        sheet.addCell(new Label(2, i, date2.toString()));
//                        sheet.addCell(new Label(3, i, duration));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                Toast.makeText(AttendanceData.this, R.string.export_msg, Toast.LENGTH_SHORT).show();
                back();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportAllEmployeeData() {
        String currentTime = new SimpleDateFormat(" d MMM yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        final String fileName = "EmployeeData-" + currentTime + ".xls";
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        Cursor cursor = MainActivity.employeeDatabase.employeeDAO().allEmployeeData();

        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/ETracker/Employee Data");
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        File file = new File(directory, fileName);

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Employee Data", 0);

            try {
                sheet.addCell(new Label(0, 0, "Employee Code"));
                sheet.addCell(new Label(1, 0, "Name"));
                sheet.addCell(new Label(2, 0, "Email"));
                sheet.addCell(new Label(3, 0, "Contact"));
                sheet.addCell(new Label(4, 0, "Date of Birth"));
                if (cursor.moveToFirst()) {
                    do {
                        String emp_code = cursor.getString(cursor.getColumnIndex("emp_code"));
                        String name = cursor.getString(cursor.getColumnIndex("Name"));
                        String email = cursor.getString(cursor.getColumnIndex("email"));
                        String contact = cursor.getString(cursor.getColumnIndex("phone"));
                        String dob = cursor.getString(cursor.getColumnIndex("date"));
                        int i = cursor.getPosition() + 1;
                        sheet.addCell(new Label(0, i, emp_code));
                        sheet.addCell(new Label(1, i, name));
                        sheet.addCell(new Label(2, i, email));
                        sheet.addCell(new Label(3, i, contact));
                        sheet.addCell(new Label(4, i, dob));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                Toast.makeText(AttendanceData.this, R.string.export_msg, Toast.LENGTH_SHORT).show();
                back();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
