package com.test.learningapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView countdownText, tvCountDown, tvDebug, tvTaskName, tvCurrentTask;
    private Button startStop_button, resetButton, debug_button;
    private CountDownTimer countDownTimer;
    private long timeLeftInMiliseconds = 1501000;
    private boolean timerRunning = false;
    private ProgressBar determinateBar;
    private ImageButton startStopBtn, resetBtn, taskListBtn;
    private int progress = 0;
    private String currentTask, selectedTask;
    private int enterCounter, taskIteration;
    private ListView listView;
    private String debug = "debug msg";




    ArrayList<String> taskList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        determinateBar = (ProgressBar) findViewById(R.id.determinateBar);

        startStopBtn = (ImageButton) findViewById(R.id.startStop_btn);
        resetBtn = (ImageButton) findViewById(R.id.reset_btn);
        taskListBtn = findViewById(R.id.taskListBtn);

        countdownText = findViewById(R.id.tvCountDown);
        tvCountDown = findViewById(R.id.tvCountDown);
        tvDebug = findViewById(R.id.tvDebug);
        tvCurrentTask = findViewById(R.id.tvCurrentTask);
        tvTaskName = findViewById(R.id.tvTaskName);

        debug_button = findViewById(R.id.debug_button);


        taskList.add("task1");
        taskList.add("task2");
        taskList.add("task3");
        taskList.add("task4");



        tvTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0) {
                    tvCurrentTask.setText("null");
                }
                else {
                    checkIsLetterInWorld(s.charAt(0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        tvTaskName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                    enterCounter++;

                    taskList.add(tvTaskName.getText().toString());


                   // resetIteration();


                 Log.d("MyLog", "Нажат ввод : " + enterCounter + " раз " + "Поставленна задача : " + taskList.get(enterCounter - 1) + " Всего задач : " + taskList.size());


                 if(!timerRunning) {
                     startProcess();
                 }




                    return true;
                }else {
                    return false;
                }
            }
        });




        debug_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String debugmsg = taskList.get(taskList.size()-1) + " last task";
                Toast.makeText(MainActivity.this, debugmsg, Toast.LENGTH_SHORT).show();
            }
        });

        updateBar();
        fullReset();

        Intent intentToSecondActyvity = new Intent(this, Second_Activity.class);

        taskListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskListBtn.setImageResource(R.drawable.menu_pressed);

                intentToSecondActyvity.putExtra("taskList", taskList);
                intentToSecondActyvity.putExtra("taskInput", tvTaskName.getText().toString());
                startActivity(intentToSecondActyvity);
            }
        });

        startStopBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startProcess();
            }
        });


        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBtn.setImageResource(R.drawable.reset_pushed);
                confirmReset();
            }
        });
        determinateBar.setMax(100);
    }

    private void startProcess() {
        boolean isRun = timerRunning;

        if(!isRun) {
            startStopBtn.setImageResource(R.drawable.resume);
            startStop();
        }
        else {
            startStopBtn.setImageResource(R.drawable.start);
            stopTimer();
        }
    }

    public void resetIteration (){

        if (enterCounter == 10) {
            taskIteration++;
            enterCounter = 0;
        }

}


    private void checkIsLetterInWorld(char charAt) {
            currentTask = tvTaskName.getText().toString();
            tvCurrentTask.setText(currentTask);
            tvDebug.setText(currentTask);
        //        tvTaskName.setFocusable(false);
    }

    public void startStop(){

        if (timerRunning == false) {
            startTimer();
        }
        else
        {
            stopTimer();
        }
    }


    public void startTimer() {

        countDownTimer = new CountDownTimer(timeLeftInMiliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMiliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                //empty
            }
        }.start();
        timerRunning = true;
    }


    public void stopTimer () {

        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerRunning = false;
    }

    public void updateBar(){

        progress = (int) (timeLeftInMiliseconds * 100/1501000);
        int progress_percent = (100 - progress);
        tvDebug.setText("Прогресс: " + progress_percent + "%");
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            determinateBar.setProgress((progress + 1),true);
        }

    }

    public void confirmReset(){
        AlertDialog.Builder confirm_builder = new AlertDialog.Builder(MainActivity.this);
        confirm_builder.setMessage("Сбросить выполнение задачи?")
                .setCancelable(false)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopTimer();
                        fullReset();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetBtn.setImageResource(R.drawable.reset);
                       dialog.cancel();
                    }
                });


        AlertDialog alert = confirm_builder.create();
        alert.setTitle("Отмена задачи");
        alert.show();
    }


    public void fullReset(){
        timerRunning = false;
        stopTimer();
        timeLeftInMiliseconds = 1501000;
        countdownText.setText("25:00");
        startStopBtn.setImageResource(R.drawable.start);
        resetBtn.setImageResource(R.drawable.reset);
        taskListBtn.setImageResource(R.drawable.menu);
        updateBar();
    }

    public void updateTimer(){

        int minutes = (int) timeLeftInMiliseconds / 60000;
        int seconds = (int) timeLeftInMiliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" +  minutes;
        timeLeftText += ":";

        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        countdownText.setText(timeLeftText);
        updateBar();
    }
}