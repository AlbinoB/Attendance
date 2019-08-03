package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AdminTeacherHomeActivity extends AppCompatActivity {


    private Spinner admincourseSpiner;
    private static final String[] admincoursename ={"Select Course Name","MCA","BCA","BBA","BCS"};

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_admin_teacher_home);

       admincourseSpiner = (Spinner)findViewById(R.id.AdmincourseSpinner);
       ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(AdminTeacherHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,admincoursename);
       courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       admincourseSpiner.setAdapter(courseAdapter);



   }

   public  void showAllTeachers(View view){
       Intent adminShowTeacherNamenActivity = new Intent(getApplicationContext(), AdminShowTeacherNameActivity.class);
       startActivity(adminShowTeacherNamenActivity);
   }
}
