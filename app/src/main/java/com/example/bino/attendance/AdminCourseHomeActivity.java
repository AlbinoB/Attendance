package com.example.bino.attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AdminCourseHomeActivity extends AppCompatActivity {

    private Spinner admincourseSpiner,adminyearSpiner,adminsemesterSpiner;
    private static final String[] admincoursename ={"Select Course Name","MCA","BCA","BBA","BCS"};
    private static final String[] adminyearNo ={"Select Year","1st Year","2nd Year","3rd Year"};
    private static final String[] adminsemesterNo ={"Select Semester","1st Sem","2nd Sem","3rd Sem"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_home);

        admincourseSpiner = (Spinner)findViewById(R.id.AdminCourseSpinner);
        adminyearSpiner = (Spinner)findViewById(R.id.AdminyearSpinner);
        adminsemesterSpiner = (Spinner)findViewById(R.id.AdminSemesterSpinner);

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(AdminCourseHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,admincoursename);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        admincourseSpiner.setAdapter(courseAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(AdminCourseHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,adminyearNo);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adminyearSpiner.setAdapter(yearAdapter);

        ArrayAdapter<String> semesterAdapter = new ArrayAdapter<String>(AdminCourseHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,adminsemesterNo);
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adminsemesterSpiner.setAdapter(semesterAdapter);


    }
}
