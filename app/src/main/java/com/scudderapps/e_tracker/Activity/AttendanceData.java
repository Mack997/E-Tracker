package com.scudderapps.e_tracker.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.scudderapps.e_tracker.AttendanceAdapter;
import com.scudderapps.e_tracker.DATA.AttendanceDetails;
import com.scudderapps.e_tracker.Database.AttendanceDatabase;
import com.scudderapps.e_tracker.R;

import java.util.List;

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
    }


}
