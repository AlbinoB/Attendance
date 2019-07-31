package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TakeAttendanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
    }

    public  void ShowAttendance(View view){
        Button savebutton =(Button) findViewById(R.id.SaveAttendanceButton);
        Intent showtakenattendance = new Intent(getApplicationContext(), ShowTakenAttendanceActivity.class);
        startActivity(showtakenattendance);
    }
}
