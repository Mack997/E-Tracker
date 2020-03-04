package com.scudderapps.e_tracker;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scudderapps.e_tracker.DATA.AttendanceDetails;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceHolder> {

    private List<AttendanceDetails> attendanceDetails;

    @NonNull
    @Override
    public AttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_list_item, parent, false);
        return new AttendanceHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AttendanceHolder holder, int position) {

        AttendanceDetails attendanceEntries = attendanceDetails.get(position);
        String createdAt = attendanceEntries.getCreatedAt();
        long _date = Long.parseLong(createdAt);
        final String check_in = new SimpleDateFormat("yy-MM-dd, HH:mm:ss", Locale.getDefault()).format(new Date(_date));

        holder.time.setText(createdAt);
        holder.status.setText(attendanceEntries.getStatus());
    }

    @Override
    public int getItemCount() {
        return attendanceDetails.size();
    }

    public void setAttendanceData(List<AttendanceDetails> attendanceDetails) {
        this.attendanceDetails = attendanceDetails;
        notifyDataSetChanged();
    }

    public AttendanceDetails getAttendanceAt(int position) {
        return attendanceDetails.get(position);
    }

    public class AttendanceHolder extends RecyclerView.ViewHolder {

        TextView status, time;

        public AttendanceHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.statusView);
            time = itemView.findViewById(R.id.dateView);
        }
    }

}
