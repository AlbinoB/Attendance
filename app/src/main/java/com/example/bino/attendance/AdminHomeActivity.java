package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

public class AdminHomeActivity extends AppCompatActivity {

  /*  public void adminStudentSearchBy(View view){
        Button adminStudentSearchByButton=(Button)findViewById(R.id.viewStudent);
        Intent nextActivity = new Intent(getApplicationContext(), AdminStudentSearchBy.class);
        startActivity(nextActivity);
    }

    public void adminTeacherSearchBy(View view){
        Button adminTeacherSearchByButton=(Button)findViewById(R.id.viewTeacher);
        Intent adminteacherhomeactivitys = new Intent(getApplicationContext(), AdminTeacherHomeActivity.class);
        startActivity(adminteacherhomeactivitys);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        final GridView gridview = (GridView) findViewById(R.id.gridLayout);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if((gridview.getTag().toString()).equals("studentTag")){
                    Intent nextActivity = new Intent(getApplicationContext(), AdminStudentSearchBy.class);
                    startActivity(nextActivity);
                }else if((gridview.getTag().toString()).equals("teacherTag")){
                    Intent adminteacherhomeactivitys = new Intent(getApplicationContext(), AdminTeacherHomeActivity.class);
                    startActivity(adminteacherhomeactivitys);
                }
            }//
        });
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
