package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ShowTakenAttendanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_taken_attendance);
    }

    public  void EditAttendance(View view){
        Button editbutton =(Button) findViewById(R.id.EditButton);
        Intent editattendanceactivity = new Intent(getApplicationContext(), EditAttendanceActivity.class);
        startActivity(editattendanceactivity);
    }

    public  void SaveAttendance(View view){
        Button editbutton =(Button) findViewById(R.id.SaveButton);
        Intent teacherhomeactivity = new Intent(getApplicationContext(), TeacherHomeActivity.class);
        startActivity(teacherhomeactivity);
    }
}
