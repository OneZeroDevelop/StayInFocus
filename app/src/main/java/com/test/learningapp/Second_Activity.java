package com.test.learningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class Second_Activity extends AppCompatActivity {

private Button remove_button;
//private TextView textView;
private int taskIteration;
private String taskInput;

ArrayList<String> taskList;
ArrayAdapter<String> adapter;
ListView listViewId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        remove_button = findViewById(R.id.remove_button);
        taskInput = getIntent().getStringExtra("taskInput");


//        textView = findViewById(R.id.tvDebug);
//        taskIteration = getIntent().getIntExtra("taskIteration", 0);

        Log.d("MyLog", "getting task input  " + taskInput + " ");

        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskInput == taskInput){
                    taskList.remove(0);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(Second_Activity.this, "Неудаляемая запись", Toast.LENGTH_LONG).show();
                }
            }
        });


        listViewId = findViewById(R.id.listViewId);

        taskList = getIntent().getStringArrayListExtra("taskList");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taskList);

        listViewId.setAdapter(adapter);


        listViewId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String task = adapter.getItem(position);
                Toast.makeText(Second_Activity.this, task, Toast.LENGTH_SHORT).show();
            }
        });





    }
}