package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class AdminStudentSearchBy extends AppCompatActivity {

    private Spinner SearchcourseSpiner, SearchyearSpiner, SearchsemesterSpiner;
    private static final String[] coursenames = {"Select Course Name", "MCA", "BCA", "BBA", "BCS"};
    private static final String[] yearNos = {"Select Year", "1st Year", "2nd Year", "3rd Year"};
    private static final String[] semesterNos = {"Select Semester", "1st Sem", "2nd Sem", "3rd Sem"};

    public void  SearchStudent(View view){
        Button searchbutton = (Button)findViewById(R.id.search);
        Intent adminstudentsearchresultactivity = new Intent(getApplicationContext(), AdminStudentSeachResultActivity.class);
        startActivity(adminstudentsearchresultactivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_search_by);

        SearchcourseSpiner = (Spinner)findViewById(R.id.spinnerCourse);
        SearchyearSpiner = (Spinner)findViewById(R.id.spinnerYear);
        SearchsemesterSpiner = (Spinner)findViewById(R.id.spinnerSem);


        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(AdminStudentSearchBy.this,android.R.layout.simple_spinner_dropdown_item,coursenames);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SearchcourseSpiner.setAdapter(courseAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(AdminStudentSearchBy.this,android.R.layout.simple_spinner_dropdown_item,yearNos);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SearchyearSpiner.setAdapter(yearAdapter);

        ArrayAdapter<String> semesterAdapter = new ArrayAdapter<String>(AdminStudentSearchBy.this,android.R.layout.simple_spinner_dropdown_item,semesterNos);
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SearchsemesterSpiner.setAdapter(semesterAdapter);


    }
}
