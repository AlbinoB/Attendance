package com.example.bino.attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TeacherHomeActivity extends AppCompatActivity {

    private Spinner courseSpiner,yearSpiner,semesterSpiner,subjectSpiner;
    private static final String[] coursename ={"Select Course Name","MCA","BCA","BBA","BCS"};
    private static final String[] yearNo ={"Select Year","1st Year","2nd Year","3rd Year"};
    private static final String[] semesterNo ={"Select Semester","1st Sem","2nd Sem","3rd Sem"};
    private static final String[] subjectName ={"Select subject","C++","Java","Android","Advance Java"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        courseSpiner = (Spinner)findViewById(R.id.courseSpinner);
        yearSpiner = (Spinner)findViewById(R.id.yearSpinner);
        semesterSpiner = (Spinner)findViewById(R.id.semesterSpinner);
        subjectSpiner = (Spinner)findViewById(R.id.subjectSpinner);

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(TeacherHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,coursename);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpiner.setAdapter(courseAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(TeacherHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,yearNo);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpiner.setAdapter(yearAdapter);

        ArrayAdapter<String> semesterAdapter = new ArrayAdapter<String>(TeacherHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,semesterNo);
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpiner.setAdapter(semesterAdapter);

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(TeacherHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,subjectName);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpiner.setAdapter(subjectAdapter);
    }
}
