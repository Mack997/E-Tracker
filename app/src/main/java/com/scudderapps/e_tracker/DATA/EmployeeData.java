package com.scudderapps.e_tracker.DATA;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "employee_data")
public class EmployeeData {

    @PrimaryKey @NonNull
    @ColumnInfo(name = "emp_code")
    private String Code;
    private String Name;
    private String date;
    private String password;
    private String email;
    private String phone;


    public String getName() {
        return Name;
    }

    public String getCode() {
        return Code;
    }

    public String getDate() {
        return date;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setCode(String code) {
        Code = code;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

