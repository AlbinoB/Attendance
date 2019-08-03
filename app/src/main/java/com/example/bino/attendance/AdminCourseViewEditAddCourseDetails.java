package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AdminCourseViewEditAddCourseDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_view_edit_add_course_details);
    }

    public void editCourseDetails(View view){

    }

    public void saveCourseDetails(View view){
        Intent adminCourseShowAllSubjectActivity = new Intent(getApplicationContext(), AdminCourseShowAllSubjectActivity.class);
        startActivity(adminCourseShowAllSubjectActivity);
    }
}
