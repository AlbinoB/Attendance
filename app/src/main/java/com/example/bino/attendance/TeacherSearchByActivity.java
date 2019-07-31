package com.example.bino.attendance;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TeacherSearchByActivity extends AppCompatActivity {

    private Spinner SearchcourseSpiner, SearchyearSpiner, SearchsemesterSpiner, SearchsubjectSpiner;
    private static final String[] coursenames = {"Select Course Name", "MCA", "BCA", "BBA", "BCS"};
    private static final String[] yearNos = {"Select Year", "1st Year", "2nd Year", "3rd Year"};
    private static final String[] semesterNos = {"Select Semester", "1st Sem", "2nd Sem", "3rd Sem"};
    private static final String[] subjectNames = {"Select subject", "C++", "Java", "Android", "Advance Java"};
    //kk

    EditText startDate;
    EditText endDate;
    DatePickerDialog datePickerDialogStartDate,datePickerDialogEndDate;
    Calendar calendar;

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_search_by);

        startDate=(EditText)findViewById(R.id.StartDate);
        endDate=(EditText)findViewById(R.id.EndDate);
        //startDate.setText(getDateTime());
        startDate.setInputType(InputType.TYPE_NULL);//disable softkey board
        endDate.setInputType(InputType.TYPE_NULL);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempdate=getDateTime();
                String[] dataarr=tempdate.split("/");
                Log.i("date",getDateTime());
                int day=Integer.parseInt(dataarr[2]);
                int month=Integer.parseInt(dataarr[1])-1;
                int year=Integer.parseInt(dataarr[0]);

                datePickerDialogStartDate=new DatePickerDialog(TeacherSearchByActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                startDate.setText((day+"/"+month+"/"+year));
                            }
                        },year,month,day);
                datePickerDialogStartDate.show();
            }
            });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempdate=getDateTime();
                String[] dataarr=tempdate.split("/");
                Log.i("date",getDateTime());
                int day=Integer.parseInt(dataarr[2]);
                int month=Integer.parseInt(dataarr[1])-1;
                int year=Integer.parseInt(dataarr[0]);

                datePickerDialogEndDate=new DatePickerDialog(TeacherSearchByActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                endDate.setText((day+"/"+month+"/"+year));
                            }
                        },year,month,day);
                datePickerDialogEndDate.show();
            }
        });












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
