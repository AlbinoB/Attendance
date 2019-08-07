package com.example.bino.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TeacherHomeActivity extends AppCompatActivity {

    Intent teacherNextActivity;
    int currentTeacherTextView;
    String particularcoursename;
    String particularyear;
    String particularsemester;
    String particularsubject;
    SharedPreferences sharedPreferences;

    private Spinner courseSpiner,yearSpiner,semesterSpiner,subjectSpiner;
    String[] coursename ;
    private static final String[] yearNo ={"Select Year","FY","SY","TY"};
    private static final String[] semesterNo ={"Select Semester","SEM 1","SEM 2","SEM 3","SEM 4","SEM 5","SEM 6","SEM 7","SEM 8","SEM 9",};
     String[] subjectName ;



    public class ConnectToDB extends AsyncTask<String,Void,Boolean>{
        Connection connection = null;
        String url = null;
        Statement stmt;
        ResultSet rs = null;
        String sql = "";

        @Override
        protected Boolean doInBackground(String... sqlarr) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                connection = DriverManager.getConnection(url);
                stmt = connection.createStatement();

                Log.i("sadasd","asdfaf");
                getTeacherName();
                getAndSetCourseName();
                getAndSetSubjectName();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }//doInBackground;
            public void getTeacherName(){
                 currentTeacherTextView =((Integer)sharedPreferences.getInt("currentUserId",0));

                Log.i("sadascurrentTead",currentTeacherTextView);

            }

            public void getAndSetCourseName(){

                Log.i("sadasd","aspublic void getAndSetCourseName(){dfaf");
                try{
                    int i=1;
                    int noOfCourse=0;
                    rs = stmt.executeQuery("select count(*) as countOfCourse from Course where courseId IN (select fkcourseIdTeacher from Teacher where teacherId  = '"+currentTeacherTextView+"') ");
                  if(rs.next()){
                       noOfCourse  = (rs.getInt("countOfCourse"));

                  }
                    coursename =new String[noOfCourse+1];
                   coursename[0] ="Select Course" ;
                    rs = stmt.executeQuery("select courseName from Course where courseId IN (select fkcourseIdTeacher from Teacher where teacherId = '"+currentTeacherTextView+"') ");
                   while(rs.next()){

                       coursename[i++] = rs.getString("courseName");

                   }
                    ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(TeacherHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,coursename);
                    courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    courseSpiner.setAdapter(courseAdapter);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }//getAndSetCourseName

        public void getAndSetSubjectName(){
            try{
                int i=1;
                int noOfSubject=0;
                rs = stmt.executeQuery("select count(*) as countOfSubject from Subject where fkcourseIdSubject IN (select fkcourseIdTeacher from Teacher where teacherId = '"+currentTeacherTextView+"') ");
                if(rs.next()){
                    noOfSubject  = (rs.getInt("countOfSubject"));
                    Log.i("no of subject",""+rs.getInt("countOfSubject"));

                }
                subjectName =new String[noOfSubject+1];
                subjectName[0] ="Select Subject" ;
                rs = stmt.executeQuery( "select subjectName from Subject  where fkcourseIdSubject IN (select fkcourseIdTeacher from Teacher where teacherId = '"+currentTeacherTextView+"') ");

                while(rs.next()){
                    Log.i(" subject name",""+rs.getString("subjectName"));

                    subjectName[i++] = (rs.getString("subjectName"));

                }
                ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(TeacherHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,subjectName);
                subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subjectSpiner.setAdapter(subjectAdapter);

            }catch(Exception e){
                e.printStackTrace();
            }
        }//getAndSetSubjectName
    }//AsyncTask

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.teacher_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.teacherHome:{
                teacherNextActivity=new Intent(getApplicationContext(),TeacherHomeActivity.class);
                startActivity(teacherNextActivity);
                return true;
            }
            case R.id.teacherTakeAttendance:{
                teacherNextActivity=new Intent(getApplicationContext(), TakeAttendanceActivity.class);
                startActivity(teacherNextActivity);
                return true;
            }
            case R.id.teacherViewAttendance:{
                teacherNextActivity=new Intent(getApplicationContext(), TeacherSearchByActivity.class);
                startActivity(teacherNextActivity);

                return true;

            }
            case R.id.teacherEditProfile:{

                teacherNextActivity=new Intent(getApplicationContext(), TeacherHomeActivity.class);
                startActivity(teacherNextActivity);

                return true;

            }

             default:{
                return false;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        courseSpiner = (Spinner)findViewById(R.id.CourseSpinner);
        yearSpiner = (Spinner)findViewById(R.id.yearSpinner);
        semesterSpiner = (Spinner)findViewById(R.id.SemesterSpinner);
        subjectSpiner = (Spinner)findViewById(R.id.SubjectSpinner);

       ConnectToDB connectToDB = new  ConnectToDB();


        String[] sql={

        };

        try {
            if(connectToDB.execute(sql).get()){
                {
                    Log.i("updated:mmmmm","doneee");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(TeacherHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,yearNo);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpiner.setAdapter(yearAdapter);

        ArrayAdapter<String> semesterAdapter = new ArrayAdapter<String>(TeacherHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,semesterNo);
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpiner.setAdapter(semesterAdapter);


    }

    public void takeAttendance(View view){
        Button teacherhomebutton = (Button) findViewById(R.id.teacherHomeSubmitButton);
        Log.i("inside ","nothing ");
        if(checkEmptyFields()) {
            sharedPreferences.edit().putString("currentCourseName",particularcoursename).apply();
            sharedPreferences.edit().putString("currentYearNo",particularyear).apply();
            sharedPreferences.edit().putString("currentSemNo",particularsemester).apply();
            sharedPreferences.edit().putString("currentSubjectName",particularsubject).apply();
            Intent takeAttendanceActivity = new Intent(getApplicationContext(), com.example.bino.attendance.TakeAttendanceActivity.class);
            startActivity(takeAttendanceActivity);

        }

    }
    public boolean checkEmptyFields(){
        String error="";
        particularcoursename =courseSpiner.getSelectedItem().toString();
        particularyear = yearSpiner.getSelectedItem().toString();
        particularsemester = semesterSpiner.getSelectedItem().toString();
        particularsubject = subjectSpiner.getSelectedItem().toString();
        Log.i("course selected ",particularcoursename);
        if( particularcoursename.equals("Select Course")){
            error="Please Select Course!!!";

        }else if( particularyear.equals("Select Year")){
            error="Please Select Year!!!";
        }else if( particularsemester.equals("Select Semester")){
            error="Please Select Semester!!!";
        }else if( particularsubject.equals("Select Subject")){
            error="Please Select Subject!!!";
        }

        if(error!="")
        {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            return false;
        }
        else
        {
            return true;
        }

    }
}
