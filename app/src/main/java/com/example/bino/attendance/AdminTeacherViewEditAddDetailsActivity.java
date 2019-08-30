package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AdminTeacherViewEditAddDetailsActivity extends AppCompatActivity {

    Intent intent;
    String teacherid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teacher_view_edit_add_details);
            intent = new Intent();
             teacherid=intent.getStringExtra("teacherid");


    }

    public  void saveTeacherDetails(View view){

        Intent adminTeacherShowAllNamesActivity = new Intent(getApplicationContext(), AdminTeacherShowAllNamesActivity.class);
        startActivity(adminTeacherShowAllNamesActivity);
    }

    public void editTeacherDetails(View view){

    }
}
//