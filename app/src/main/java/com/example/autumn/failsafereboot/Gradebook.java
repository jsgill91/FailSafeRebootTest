package com.example.autumn.failsafereboot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

/**
 * Created by Autumn on 9/8/2016.
 *
 * 10/19: Gradebook is now Course + Gradebook
 */
public class Gradebook extends SQLiteOpenHelper implements Serializable{
    /*
        Key for the switch statements:
        0 - test
        1 - homework
        2 - lab
        3 - in class assignment
        4 - project
        5 - quiz
     */

    //needs to be a decimal
    // in order: test, hw, lab, ica, project, quiz
    private String courseName;
    private String courseID;

    private double[] weight_list = {1, 1, 1, 1, 1, 1};

    private int idGenerator;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Schedule"; //database name
    //Table 1
    private static final String TABLE_ASSIGNMENTS = "Assignments";
    //Table 1 column names
    private static final String ASSG_ID = "ID";
    private static final String ASSG_NAME = "Name";
    private static final String ASSG_DUE_DATE = "Due Date";
    private static final String ASSG_TYPE = "Type";
    private static final String ASSG_DESCRIPTION = "Description";
    private static final String ASSG_GRADE = "Grade";

    //Table 2
    private static final String TABLE_ASSIGNMENT_DROPS = "Dropped Assignments";
    //Table 2 column names
    private static final String DROP_ASSG_TYPE = "Assg_Type";
    private static final String DROP_NUMBER = "Drop_Number";

    private Gradebook book;

    public Gradebook(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void setCourseName(String name){
        courseName = name;
    }

    public void setCourseID(String id){
        courseID = id;
    }

    public String getCourseName(){
        return courseName;
    }

    public String getCourseID(){
        return courseID;
    }


    /*------ DATABASE METHODS ------*/
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_ASSIGNMENTS_TABLE = "CREATE TABLE" + TABLE_ASSIGNMENTS + "(" + ASSG_ID +
                "INTEGER PRIMARY KEY," + ASSG_NAME + "," + ASSG_DUE_DATE + "," + ASSG_TYPE + "," +
                ASSG_DESCRIPTION + "," + ASSG_GRADE + ")";
        String CREATE_DROPS_TABLE = "CREATE TABLE" + TABLE_ASSIGNMENT_DROPS + "(" + DROP_ASSG_TYPE +
                "INTEGER PRIMARY KEY," + DROP_NUMBER + ")";
        db.execSQL(CREATE_ASSIGNMENTS_TABLE);
        db.execSQL(CREATE_DROPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENT_DROPS);
        onCreate(db);
    }
    /*
        Okay, the way the addAssignments CURRENTLY works is an com.example.autumn.failsafe.Assignment object
        is still created in FailSafe and stored in the Calendar. Before exiting
        the activity, however, the information is stored here.

        A note: Assignments still need to be given numbers to uniquely identify
        them in the database since i can't figure out how to make a superkey
     */
    public void add_assignment(String name, String due_date, String type, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ASSG_ID, idGenerator);
        values.put(ASSG_NAME, name);
        values.put(ASSG_DUE_DATE, due_date);
        values.put(ASSG_TYPE, type);
        values.put(ASSG_DESCRIPTION, description);

        db.insert(TABLE_ASSIGNMENTS, null, values);
        db.close();
        idGenerator++;
    }

    public int add_grade(int id, String name, String due_date, String type, String description, int grade){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ASSG_ID, id);
        values.put(ASSG_NAME, name);
        values.put(ASSG_DUE_DATE, due_date);
        values.put(ASSG_TYPE, type);
        values.put(ASSG_DESCRIPTION, description);
        values.put(String.valueOf(ASSG_GRADE), grade);

        return db.update(TABLE_ASSIGNMENTS, values, ASSG_ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    public void set_weight(int assignment, double weight) {
        weight_list[assignment] = weight;
    }

    public void set_drop(int assignment, int drop_num) {
        //need to check if the assignment is already in this table and replace
        //the values if so
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        switch(assignment){
            case 0:
                values.put(DROP_ASSG_TYPE, "Test");
                values.put(DROP_NUMBER, drop_num);
                break;
            case 1:
                values.put(DROP_ASSG_TYPE, "HW");
                values.put(DROP_NUMBER, drop_num);
                break;
            case 2:
                values.put(DROP_ASSG_TYPE, "Lab");
                values.put(DROP_NUMBER, drop_num);
                break;
            case 3:
                values.put(DROP_ASSG_TYPE, "ICA");
                values.put(DROP_NUMBER, drop_num);
                break;
            case 4:
                values.put(DROP_ASSG_TYPE, "Project");
                values.put(DROP_NUMBER, drop_num);
                break;
            case 5:
                values.put(DROP_ASSG_TYPE, "Quiz");
                values.put(DROP_NUMBER, drop_num);
                break;
        }

        db.insert(TABLE_ASSIGNMENTS, null, values);
        db.close();
    }

    public double calculate_assignment_average(int assignment){
        SQLiteDatabase db = this.getReadableDatabase();

        String myQuery;

        myQuery = "SELECT Type, AVG(CAST(Grade AS DOUBLE)) AS Average FROM TABLE_ASSIGNMENTS " +
                "ORDER BY Grade DESC LIMIT COUNT(Type)-(Select " + "drop_num FROM TABLE_DROPS " +
                "WHERE assg_type = Type)";
        Cursor cursor = db.rawQuery(myQuery,null);
        double averages[] = new double[6];
        if(cursor != null){
            while(cursor.moveToNext()){
                if(cursor.getString(cursor.getColumnIndex("Type")).equalsIgnoreCase("test")){
                    averages[0] = cursor.getDouble(cursor.getColumnIndex("Average"));
                }
                else if(cursor.getString(cursor.getColumnIndex("Type")).equalsIgnoreCase("hw")){
                    averages[1] = cursor.getDouble(cursor.getColumnIndex("Average"));
                }
                else if(cursor.getString(cursor.getColumnIndex("Type")).equalsIgnoreCase("lab")){
                    averages[2] = cursor.getDouble(cursor.getColumnIndex("Average"));
                }
                else if(cursor.getString(cursor.getColumnIndex("Type")).equalsIgnoreCase("ica")){
                    averages[3] = cursor.getDouble(cursor.getColumnIndex("Average"));
                }
                else if(cursor.getString(cursor.getColumnIndex("Type")).equalsIgnoreCase("project")){
                    averages[4] = cursor.getDouble(cursor.getColumnIndex("Average"));
                }
                else if(cursor.getString(cursor.getColumnIndex("Type")).equalsIgnoreCase("quiz")){
                    averages[5] = cursor.getDouble(cursor.getColumnIndex("Average"));
                }
            }
        }
        cursor.close();
        switch(assignment){
            case 0:
                return averages[0];
            case 1:
                return averages[1];
            case 2:
                return averages[2];
            case 3:
                return averages[3];
            case 4:
                return averages[4];
        }
        return averages[5];
    }

    public double calculate_average(){
        SQLiteDatabase db = this.getReadableDatabase();

        String myQuery;
        /*
             maybe create another db table "drop table" and have 2 cols, assg_type and
             drop_num, and let assg_type and type (from assignments table) have a foreign key
             relationship. And in that case, make the bit in the LIMIT statement be a correlated
             nested query : LIMIT COUNT(Type)-(Select drop_num FROM TABLE_DROPS WHERE assg_type = Type)
             That SHOULD work.

             The question is, if the nested query returns an empty value (the assignment is not in
             the drops table and therefore has no assignments to be dropped), will the query cause
             an error or will it just subtract zero and keep moving?
        */

        myQuery = "SELECT Type, AVG(CAST(Grade AS DOUBLE)) AS Average FROM TABLE_ASSIGNMENTS " + 
                "ORDER BY Grade DESC LIMIT COUNT(Type)-(Select " + "drop_num FROM TABLE_DROPS " +
                "WHERE assg_type = Type)";
        Cursor cursor = db.rawQuery(myQuery,null);
        double avg;
        double total_average = 0;
        if(cursor != null){
            while(cursor.moveToNext()){
                if(cursor.getString(cursor.getColumnIndex("Type")).equalsIgnoreCase("test")){
                    avg = cursor.getDouble(cursor.getColumnIndex("Average")) * weight_list[0];
                    total_average += avg;
                }
                else if(cursor.getString(cursor.getColumnIndex("Type")).equalsIgnoreCase("hw")){
                    avg = cursor.getDouble(cursor.getColumnIndex("Average")) * weight_list[1];
                    total_average += avg;
                }
                else if(cursor.getString(cursor.getColumnIndex("Type")).equalsIgnoreCase("lab")){
                    avg = cursor.getDouble(cursor.getColumnIndex("Average")) * weight_list[2];
                    total_average += avg;
                }
                else if(cursor.getString(cursor.getColumnIndex("Type")).equalsIgnoreCase("ica")){
                    avg = cursor.getDouble(cursor.getColumnIndex("Average")) * weight_list[3];
                    total_average += avg;
                }
                else if(cursor.getString(cursor.getColumnIndex("Type")).equalsIgnoreCase("project")){
                    avg = cursor.getDouble(cursor.getColumnIndex("Average")) * weight_list[4];
                    total_average += avg;
                }
                else if(cursor.getString(cursor.getColumnIndex("Type")).equalsIgnoreCase("quiz")){
                    avg = cursor.getDouble(cursor.getColumnIndex("Average")) * weight_list[5];
                    total_average += avg;
                }
            }
        }

        cursor.close();
        return total_average;
    }

    /*public double what_if(){

            Could write this to "temporarily" add Assignment objects to the database
            (just DON'T FORGET TO DELETE THEM WHEN FINISHED!) then run calculate_full_grade
            over the modified database.


    }*/

}
