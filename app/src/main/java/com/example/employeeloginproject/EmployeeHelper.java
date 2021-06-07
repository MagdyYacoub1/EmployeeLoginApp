package com.example.employeeloginproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class EmployeeHelper extends SQLiteOpenHelper {
    SQLiteDatabase employeeDatabase;
    private static final String databaseName = "EmployeeData";
    private static final int version = 1;
    private static final String tableName = "Employees";
    private static final String columnId = "id";
    private static final String columnName = "name";
    private static final String columnPassword = "password";
    private static final String columnImage = "image";
    private static final String columnLoginTime = "loginTime";
    private static final String columnSignOutTime = "signOutTime";
    private static final String columnDepartment = "department";

    public EmployeeHelper(@Nullable Context context) {
        super(context, databaseName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+tableName+"("+columnId+" integer primary key autoincrement , "+columnName+" text not null," +
                columnPassword+" text not null , "+columnImage+" blob not null , "+columnLoginTime+" text," +
                columnSignOutTime+" text, "+columnDepartment+" text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Employee ");
        onCreate(db);
    }

    public long InsertEmp (String empName, String empPassword, byte[] empImage, String empDepartment)
    {
        ContentValues row = new ContentValues();
        row.put(columnName, empName);
        row.put(columnPassword, empPassword);
        row.put(columnImage, empImage);
        row.put(columnDepartment, empDepartment);

        employeeDatabase = getWritableDatabase();
        long success = employeeDatabase.insert(tableName, null, row);
        employeeDatabase.close();
        return success;
    }

    Cursor getAllEmployees() {
        employeeDatabase= getReadableDatabase();
        Cursor cursor = employeeDatabase.rawQuery("SELECT * FROM " + tableName, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        employeeDatabase.close();
        return cursor;
    }
}
