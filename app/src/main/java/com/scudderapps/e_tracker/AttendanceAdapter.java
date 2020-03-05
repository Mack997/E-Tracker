package com.scudderapps.e_tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scudderapps.e_tracker.DATA.AttendanceDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceHolder> {

    private List<AttendanceDetails> attendanceDetails;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public AttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_list_item, parent, false);
        return new AttendanceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceHolder holder, int position) {

        AttendanceDetails attendanceEntries = attendanceDetails.get(position);
        String createdAt = attendanceEntries.getCreatedAt();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyMMddHHmmss").parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.time.setText(date.toString());
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(AttendanceDetails attendanceDetails, int position);
    }

    public class AttendanceHolder extends ViewHolder {

        TextView status, time;

        public AttendanceHolder(@NonNull final View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.statusView);
            time = itemView.findViewById(R.id.dateView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(attendanceDetails.get(position), position);
                        attendanceDetails.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, attendanceDetails.size());
                        itemView.setVisibility(View.GONE);
                    }
                }
            });
        }

    }
}