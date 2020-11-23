package com.android.sqlite_crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TableControllerStudent extends DatabaseHandler{

    public TableControllerStudent(Context context){
        super(context);
    }

    public boolean create(StudentData studentData){
        ContentValues values = new ContentValues();
        values.put("name", studentData.name);
        values.put("mail", studentData.mail);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("students", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public int count() {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM students";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;
    }

    public List<StudentData> read(){
        List<StudentData> recordsList = new ArrayList<StudentData>();

        String sql = "SELECT * FROM students ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));

                String studentName = cursor.getString(cursor.getColumnIndex("name"));
                String studentMail = cursor.getString(cursor.getColumnIndex("mail"));

                StudentData studentData = new StudentData();

                studentData.id = id;
                studentData.name = studentName;
                studentData.mail = studentMail;

                recordsList.add(studentData);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recordsList;
    }

    public StudentData readSingleRecord(int studentId){
        StudentData studentData = null;

        String sql = "SELECT * FROM students WHERE id = " + studentId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String mail = cursor.getString(cursor.getColumnIndex("mail"));

            studentData = new StudentData();
            studentData.id = id;
            studentData.name = name;
            studentData.mail = mail;
        }
        cursor.close();
        db.close();

        return studentData;
    }

    public boolean update(StudentData studentData){
        ContentValues values = new ContentValues();
        values.put("name", studentData.name);
        values.put("mail", studentData.mail);

        String where = "id = ?";
        String[] whereArgs = {Integer.toString(studentData.id)};

        SQLiteDatabase db = this.getWritableDatabase();
        boolean updateSuccessful = db.update("students", values, where, whereArgs) > 0;

        db.close();
        return updateSuccessful;
    }

    public boolean delete(int id){
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("students", "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;
    }

}
