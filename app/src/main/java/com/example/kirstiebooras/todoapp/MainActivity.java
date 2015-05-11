package com.example.kirstiebooras.todoapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kirstiebooras.todoapp.database.TodoListSQLHelper;

public class MainActivity extends ListActivity {

    private Button mButton;
    private TodoListSQLHelper mTodoListHelper;
    private boolean mShowComplete;
    private static final String SHOW_COMPLETE = "showComplete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences prefs = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        mButton = (Button) findViewById(R.id.showHideCompletedButton);
        mTodoListHelper = new TodoListSQLHelper(this);
        mShowComplete = prefs.getBoolean(SHOW_COMPLETE, false);
        
        updateShowHideButton();
        updateTodoList();

        boolean completedTasksFlag = mTodoListHelper.hasCompletedTasks();
        if (completedTasksFlag) {
            // Show button to show or hide completed
            mButton.setVisibility(View.VISIBLE);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mShowComplete = !mShowComplete;
                    // Save this preference for later use
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(SHOW_COMPLETE, mShowComplete);
                    editor.apply();
                    updateShowHideButton();
                    updateTodoList();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_add:
                displayAddTodoDialog();
                return true;
            default:
                return false;
        }
    }

    // Update the text in the button
    private void updateShowHideButton() {
        if (mShowComplete) {
            mButton.setText(R.string.hide_completed);
        } else {
            mButton.setText(R.string.show_completed);
        }
    }

    private void displayAddTodoDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.add_todo, null);
        final EditText editText = (EditText) view.findViewById(R.id.todoDescription);

        new AlertDialog.Builder(this)
                .setTitle("Add Todo")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Save to the DB and update the listview
                        String todoDescription = editText.getText().toString();
                        mTodoListHelper.addTodo(todoDescription);
                        updateTodoList();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Update the cursor in the ListView
    private void updateTodoList() {
        Cursor cursor;
        if (mShowComplete) {
            cursor = mTodoListHelper.getAllTodos();
        } else {
            cursor = mTodoListHelper.getAllIncompleteTodos();
        }
        TodoAdapter todoListAdapter = new TodoAdapter(this, cursor);
        this.setListAdapter(todoListAdapter);
    }

    public void onCompleteButtonClick(View view) {
        View todoView = (View) view.getParent();
        TextView todoTextView = (TextView) todoView.findViewById(R.id.todoTextView);
        String todoDescription = todoTextView.getText().toString();
        if (((CheckBox) view).isChecked()) {
            mTodoListHelper.updateTodoStatus(todoDescription, true);
        } else {
            mTodoListHelper.updateTodoStatus(todoDescription, false);
        }
        updateTodoList();
    }
}
