package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminStudentEditAddDetails extends AppCompatActivity {

    public void StudentDetaileSave(View view){
        Button studenteditsave =(Button) findViewById(R.id.saveStudentEditDetails);
        Intent adminstudentseachresultactivity = new Intent(getApplicationContext(), AdminStudentSeachResultActivity.class);
        startActivity(adminstudentseachresultactivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_edit_add_details);
    }
}
