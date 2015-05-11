package com.example.kirstiebooras.todoapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.kirstiebooras.todoapp.database.TodoListSQLHelper;

/**
 * Created by kirstiebooras on 5/6/15.
 */
public class TodoAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;

    public TodoAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.todo_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String description = cursor.getString(
                cursor.getColumnIndexOrThrow(TodoListSQLHelper.COLUMN_TODO));
        String status = cursor.getString(
                cursor.getColumnIndexOrThrow(TodoListSQLHelper.COLUMN_STATUS));

        TextView todoTextView = (TextView) view.findViewById(R.id.todoTextView);
        todoTextView.setText(description);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.completeTodo);
        if(status.equals(TodoListSQLHelper.COMPLETE)) {
            checkBox.setChecked(true);
            todoTextView.setTextColor(Color.parseColor("#808080"));
        }
    }
}
