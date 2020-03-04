package com.scudderapps.e_tracker.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.scudderapps.e_tracker.AttendanceAdapter;
import com.scudderapps.e_tracker.CSVWriter;
import com.scudderapps.e_tracker.DATA.AttendanceDetails;
import com.scudderapps.e_tracker.Database.AttendanceDatabase;
import com.scudderapps.e_tracker.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class AttendanceData extends AppCompatActivity {

    TextInputEditText empCode;
    Button attendanceBtn, exportBtn;
    RecyclerView dataView;
    AttendanceAdapter attendanceAdapter;
    AttendanceDatabase attendanceDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_data);
        attendanceBtn = findViewById(R.id.attendanceBtn);
        exportBtn = findViewById(R.id.exportBtn);
        dataView = findViewById(R.id.dataView);
        empCode = findViewById(R.id.getDateCode);
        dataView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        dataView.setHasFixedSize(true);

        attendanceDatabase = Room.databaseBuilder(getApplicationContext(),
                AttendanceDatabase.class, "AttendanceData")
                .allowMainThreadQueries()
                .build();

        attendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = empCode.getText().toString();
                List<AttendanceDetails> attendanceDetailsList = attendanceDatabase.attendanceDAO().empCode(code);
                attendanceAdapter = new AttendanceAdapter();
                dataView.setAdapter(attendanceAdapter);
                attendanceAdapter.setAttendanceData(attendanceDetailsList);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                attendanceDatabase.attendanceDAO().delete(attendanceAdapter.getAttendanceAt(viewHolder.getAdapterPosition()));
                Toast.makeText(AttendanceData.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(dataView);

        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fileName = "AttendanceData.xls";
                String CODE = empCode.getText().toString();
                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale("en", "EN"));
                WritableWorkbook workbook;

                Cursor cursor = attendanceDatabase.attendanceDAO().getCursorAll(CODE);

                File sdCard = Environment.getExternalStorageDirectory();
                File directory = new File(sdCard.getAbsolutePath() + "/attendance");
                if(!directory.isDirectory()){
                    directory.mkdirs();
                }
                File file = new File(directory, fileName);

                try {
                    workbook = Workbook.createWorkbook(file, wbSettings);
                    //Excel sheet name. 0 represents first sheet
                    WritableSheet sheet = workbook.createSheet("Attendance Details", 0);

                    try {
                        sheet.addCell(new Label(0, 0, "Id"));
                        sheet.addCell(new Label(1, 0, "Employee Code"));
                        sheet.addCell(new Label(2, 0, "Created Time"));
                        sheet.addCell(new Label(3, 0, "Status"));
                        if (cursor.moveToFirst()) {
                            do {
                                String id = cursor.getString(cursor.getColumnIndex("id"));
                                String emp_code = cursor.getString(cursor.getColumnIndex("Code"));
                                String createdAt = cursor.getString(cursor.getColumnIndex("createdAt"));
                                String status = cursor.getString(cursor.getColumnIndex("Status"));

                                int i = cursor.getPosition() + 1;
                                sheet.addCell(new Label(0, i, id));
                                sheet.addCell(new Label(1, i, emp_code));
                                sheet.addCell(new Label(2, i, createdAt));
                                sheet.addCell(new Label(3, i, status));
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
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
        });
    }
}
