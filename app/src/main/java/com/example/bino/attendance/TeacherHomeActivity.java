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
    ConnectToDB connectToDB;

    private Spinner courseSpiner,yearSpiner,semesterSpiner,subjectSpiner;
    String[] coursename ;
    String[] yearNo ;
    String[] semesterNo ;
    String[] subjectName ;
    int noOfSubject,noOfSemesters,noOfYears,noOfCourse=0;



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




                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }//doInBackground;
            public void getTeacherName(){
                 currentTeacherTextView =((Integer)sharedPreferences.getInt("currentUserId",0));

                Log.i("sadascurrentTead",currentTeacherTextView+"");

            }

            public void getAndSetCourseName(){

                Log.i("sadasd","aspublic void getAndSetCourseName(){dfaf");
                try{
                    int i=0;
                    noOfCourse=0;
                    rs = stmt.executeQuery("select count(courseName) as countOfCourse from Course where courseId in (select fkcourseIdSubject from Subject where fkteacherIdSubject=(select teacherId from Teacher where teacherId='"+(Integer)sharedPreferences.getInt("currentUserId",0)+"'))");

                    if(rs.next()){
                       noOfCourse  = (rs.getInt("countOfCourse"));

                  }
                    coursename =new String[noOfCourse];
                    rs = stmt.executeQuery("select courseName from Course where courseId in (select fkcourseIdSubject from Subject where fkteacherIdSubject=(select teacherId from Teacher where teacherId='"+(Integer)sharedPreferences.getInt("currentUserId",0)+"'))");
                   while(rs.next()){

                       coursename[i++] = rs.getString("courseName");

                   }
                    ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(TeacherHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,coursename);
                    courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    courseSpiner.setAdapter(courseAdapter);

                    courseSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            connectToDB.getYears();
                            connectToDB.getSemesters();
                            connectToDB.getAndSetSubjectName();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });



                }catch(Exception e){
                    e.printStackTrace();
                }
            }//getAndSetCourseName

        public void getYears(){
            try {
                int i = 0;
                noOfYears = 0;

                rs = stmt.executeQuery("select count(*) as noOfYears from CourseYears where fkcourseIdCourseYears=(select courseId from Course where courseName='"+courseSpiner.getSelectedItem()+"')");
                if (rs.next()) {
                    noOfYears = (rs.getInt("noOfYears"));

                }
                Log.i("noOfYears:",noOfYears+"");
                yearNo=new String[noOfYears];
                rs = stmt.executeQuery("select courseYears from CourseYears where fkcourseIdCourseYears=(select courseId from Course where courseName='"+courseSpiner.getSelectedItem()+"')");
                while (rs.next()) {
                    Log.i("courseYears:",rs.getString("courseYears"));
                    yearNo[i++] = rs.getString("courseYears");

                }
                ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(TeacherHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,yearNo);
                yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                yearSpiner.setAdapter(yearAdapter);

            }
                catch(Exception e){
                    e.printStackTrace();
                }
        }


        public void getSemesters(){
            try {
                int i = 0;
                noOfSemesters = 0;

                rs = stmt.executeQuery("select Count(*) as noOfSemesters from Semester where semYear='"+yearSpiner.getSelectedItem()+"'");
                if (rs.next()) {
                    noOfSemesters = (rs.getInt("noOfSemesters"));

                }
                Log.i("noOfYears:",noOfSemesters+"");
                semesterNo=new String[noOfSemesters];
                rs = stmt.executeQuery("select semName from Semester where semYear='"+yearSpiner.getSelectedItem()+"'");
                while (rs.next()) {
                    Log.i("semName:",rs.getString("semName"));
                    semesterNo[i++] = rs.getString("semName");

                }
                ArrayAdapter<String> semesterAdapter = new ArrayAdapter<String>(TeacherHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,semesterNo);
                semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                semesterSpiner.setAdapter(semesterAdapter);


            }
            catch(Exception e){
                e.printStackTrace();
            }
        }


        public void getAndSetSubjectName(){
            try{
                int i=0;
                noOfSubject=0;
                rs = stmt.executeQuery("select count(subjectName) as countOfSubject from Subject where fkteacherIdSubject="+(Integer)sharedPreferences.getInt("currentUserId",0)+" and fkcourseIdSubject=(select courseId from Course where courseName='"+courseSpiner.getSelectedItem()+"') and  fksemIdSubject=(select semId from Semester where semName='"+semesterSpiner.getSelectedItem()+"')");

                if(rs.next()){
                    noOfSubject  = (rs.getInt("countOfSubject"));
                    Log.i("no of subject",""+rs.getInt("countOfSubject"));

                }
                subjectName =new String[noOfSubject];
                rs = stmt.executeQuery( "select subjectName from Subject where fkteacherIdSubject="+(Integer)sharedPreferences.getInt("currentUserId",0)+" and fkcourseIdSubject=(select courseId from Course where courseName='"+courseSpiner.getSelectedItem()+"') and  fksemIdSubject=(select semId from Semester where semName='"+semesterSpiner.getSelectedItem()+"')");

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

        Bundle bundle=new Bundle();
        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        courseSpiner = (Spinner)findViewById(R.id.CourseSpinner);
        yearSpiner = (Spinner)findViewById(R.id.yearSpinner);
        semesterSpiner = (Spinner)findViewById(R.id.SemesterSpinner);
        subjectSpiner = (Spinner)findViewById(R.id.SubjectSpinner);



        connectToDB = new  ConnectToDB();
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
