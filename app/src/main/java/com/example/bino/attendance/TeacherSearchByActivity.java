package com.example.bino.attendance;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class TeacherSearchByActivity extends AppCompatActivity {

    private Spinner SearchcourseSpiner, SearchyearSpiner, SearchsemesterSpiner, SearchsubjectSpiner;
    private static final String[] coursenames = {"Select Course Name", "MCA", "BCA", "BBA", "BCS"};
    private static final String[] yearNos = {"Select Year", "1st Year", "2nd Year", "3rd Year"};
    private static final String[] semesterNos = {"Select Semester", "1st Sem", "2nd Sem", "3rd Sem"};
    private static final String[] subjectNames = {"Select subject", "C++", "Java", "Android", "Advance Java"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_search_by);

        SearchcourseSpiner = (Spinner)findViewById(R.id.SearchcourseSpinner);
        SearchyearSpiner = (Spinner)findViewById(R.id.SearchyearSpinner);
        SearchsemesterSpiner = (Spinner)findViewById(R.id.SearchsemesterSpinner);
        SearchsubjectSpiner = (Spinner)findViewById(R.id.SearchsubjectSpinner);

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(TeacherSearchByActivity.this,android.R.layout.simple_spinner_dropdown_item,coursenames);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SearchcourseSpiner.setAdapter(courseAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(TeacherSearchByActivity.this,android.R.layout.simple_spinner_dropdown_item,yearNos);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SearchyearSpiner.setAdapter(yearAdapter);

        ArrayAdapter<String> semesterAdapter = new ArrayAdapter<String>(TeacherSearchByActivity.this,android.R.layout.simple_spinner_dropdown_item,semesterNos);
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SearchsemesterSpiner.setAdapter(semesterAdapter);

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(TeacherSearchByActivity.this,android.R.layout.simple_spinner_dropdown_item,subjectNames);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SearchsubjectSpiner.setAdapter(subjectAdapter);

    }

}
