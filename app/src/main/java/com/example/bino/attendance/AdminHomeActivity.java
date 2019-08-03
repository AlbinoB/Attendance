package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class AdminHomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

    }
    public void goToNextActivity(View view){

        String nextActivity=view.getTag().toString();
        switch (nextActivity){
            case "1":{
                Intent nextActadminstudentsearchby = new Intent(getApplicationContext(), AdminStudentSearchBy.class);
                startActivity(nextActadminstudentsearchby);
            }break;
            case "2":{
                Intent adminteacherhomeactivitys = new Intent(getApplicationContext(), AdminTeacherHomeActivity.class);
                startActivity(adminteacherhomeactivitys);
            }break;
            case "3":{
                Toast.makeText(this, "you cleicked:"+nextActivity, Toast.LENGTH_SHORT).show();
            }break;
            case "4":{
                Toast.makeText(this, "you cleicked:"+nextActivity, Toast.LENGTH_SHORT).show();
            }break;

        }
    }




/*    public void adminCourseSearchBy(View view){
        Button adminStudentSearchByButton=(Button)findViewById(R.id.viewStudent);
        Intent nextActivity = new Intent(getApplicationContext(), AdminCourseSearchBy.class);
        startActivity(nextActivity);
    }

    public void adminViewAttendanceSearchBy(View view){
        Button adminStudentSearchByButton=(Button)findViewById(R.id.viewStudent);
        Intent nextActivity = new Intent(getApplicationContext(), AdminViewAttendanceSearchBy.class);
        startActivity(nextActivity);
    }
    */
}
