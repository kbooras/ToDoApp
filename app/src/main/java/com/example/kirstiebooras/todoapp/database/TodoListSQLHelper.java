package com.example.kirstiebooras.todoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by kirstiebooras on 5/6/15.
 */
public class TodoListSQLHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "com.example.kirstiebooras.todo.db";
    public static final String TABLE_NAME = "TODO_LIST";
    public static final String COLUMN_TODO = "todo";
    public static final String COLUMN_STATUS = "status";
    public static final String _ID = BaseColumns._ID;
    public static final int DB_VERSION = 1;

    public static final String COMPLETE = "complete";
    public static final String INCOMPLETE = "incomplete";

    public TodoListSQLHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TODO + " TEXT, " +
                COLUMN_STATUS + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean hasCompletedTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{ _ID }, COLUMN_STATUS + " = ?",
                new String[]{ COMPLETE }, null, null, null);
        return cursor.getCount() > 0;
    }

    public Cursor getAllTodos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, new String[]{ _ID, COLUMN_TODO, COLUMN_STATUS }, null, null,
                null, null, COLUMN_STATUS + " DESC");
    }

    public Cursor getAllIncompleteTodos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, new String[]{ _ID, COLUMN_TODO, COLUMN_STATUS },
                COLUMN_STATUS + " = ?", new String[]{ INCOMPLETE }, null, null, null);
    }

    public void addTodo(String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO, description);
        values.put(COLUMN_STATUS, INCOMPLETE);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int updateTodoStatus(String description, boolean complete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO, description);
        if(complete) {
            values.put(COLUMN_STATUS, COMPLETE);
        } else {
            values.put(COLUMN_STATUS, INCOMPLETE);
        }

        return db.update(TABLE_NAME, values, COLUMN_TODO + " = ?", new String[] { description });
    }

    public void deleteTodo(String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteTodo = "DELETE FROM " + TABLE_NAME + " WHERE " +
                COLUMN_TODO + " = '" + description + "'";
        db.execSQL(deleteTodo);
    }
}
