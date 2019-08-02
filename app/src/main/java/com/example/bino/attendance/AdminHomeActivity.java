package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void adminStudentSearchBy(View view){
        Button adminStudentSearchByButton=(Button)findViewById(R.id.viewStudent);
        Intent nextActivity = new Intent(getApplicationContext(), AdminStudentSearchBy.class);
        startActivity(nextActivity);
    }
/*
    public void adminTeacherSearchBy(View view){
        Button adminStudentSearchByButton=(Button)findViewById(R.id.viewStudent);
        Intent nextActivity = new Intent(getApplicationContext(), AdminTeacherSearchBy.class);
        startActivity(nextActivity);
    }


    public void adminCourseSearchBy(View view){
        Button adminStudentSearchByButton=(Button)findViewById(R.id.viewStudent);
        Intent nextActivity = new Intent(getApplicationContext(), AdminCourseSearchBy.class);
        startActivity(nextActivity);
    }

    public void adminViewAttendanceSearchBy(View view){
        Button adminStudentSearchByButton=(Button)findViewById(R.id.viewStudent);
        Intent nextActivity = new Intent(getApplicationContext(), AdminViewAttendanceSearchBy.class);
        startActivity(nextActivity);
    }
    */
}
