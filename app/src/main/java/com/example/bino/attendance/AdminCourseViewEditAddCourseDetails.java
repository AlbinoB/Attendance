package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AdminCourseViewEditAddCourseDetails extends AppCompatActivity {
    Intent intent;
    String subjectcode,check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_view_edit_add_course_details);

        intent = getIntent();
        subjectcode=intent.getStringExtra("subjectid");
        check=intent.getStringExtra("check");
    }

    public void editCourseDetails(View view){

    }

    public void saveCourseDetails(View view){
        Intent adminCourseShowAllSubjectActivity = new Intent(getApplicationContext(), AdminCourseShowAllSubjectActivity.class);
        startActivity(adminCourseShowAllSubjectActivity);
    }
}
