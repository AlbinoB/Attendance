package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent teacherHomeActivity = new Intent(getApplicationContext(), EditAttendanceActivity.class);
        startActivity(teacherHomeActivity);
    }
}
//this is latest