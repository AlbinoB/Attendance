package com.example.bino.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
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
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminStudentSearchBy extends AppCompatActivity {

    Intent teacherNextActivity;
    String particularcoursename;
    String particularyear;
    String particularsemester;
    SharedPreferences sharedPreferences;
    ConnectToDB connectToDB;

    Handler handler =new Handler();

    public Spinner admincourseSpiner,yearSpiner,semesterSpiner;
    String[] course ;
    String[] yearNo ;
    String[] semesterNo ;
    int noOfYears=0,noOfSemesters=0;

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

                getAndSetCourseName();
                getYear();
                setYears();
                getSemesters();
                setSemester();

                admincourseSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                getYear();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setYears();
                                    }
                                });
                            }
                        });

                        thread.start();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                yearSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                getSemesters();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setSemester();
                                    }
                                });
                            }
                        });

                        thread.start();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//doInBackground;

        public void getAndSetCourseName(){
            try{
                rs = stmt.executeQuery("select  count(*) as noofcourse  from Course");
                while(rs.next()) {
                    course = new String[(rs.getInt("noofcourse")) + 1];
                }

                int i=1;
                course[0]="Select Course";
                rs= stmt.executeQuery("select courseName from Course");
                while(rs.next()){
                    course[i]=rs.getString("courseName");
                    i++;
                }

                ArrayAdapter<String> courseAdapter1 = new ArrayAdapter<String>(AdminStudentSearchBy.this,android.R.layout.simple_spinner_dropdown_item,course);
                courseAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                admincourseSpiner.setAdapter(courseAdapter1);
            }catch (Exception e){
                e.printStackTrace();
            }
        }//getAndSetCourseName

        public void getYear(){

            try {
                int i = 1;
                noOfYears = 0;

                rs = stmt.executeQuery("select count(*) as noOfYears from CourseYears where fkcourseIdCourseYears=(select courseId from Course where courseName='"+admincourseSpiner.getSelectedItem()+"')");
                if (rs.next()) {
                    noOfYears = (rs.getInt("noOfYears"));
                }

                yearNo=new String[noOfYears+1];
                yearNo[0]="Select Year";
                rs = stmt.executeQuery("select courseYears from CourseYears where fkcourseIdCourseYears=(select courseId from Course where courseName='"+admincourseSpiner.getSelectedItem()+"')");
                while (rs.next()) {
                    yearNo[i++] = rs.getString("courseYears");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }//getYear

        public void setYears(){

            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(AdminStudentSearchBy.this,android.R.layout.simple_spinner_dropdown_item,yearNo);
            yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            yearSpiner.setAdapter(yearAdapter);
        }//setYears



        public void getSemesters(){
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                connection = DriverManager.getConnection(url);
                stmt = connection.createStatement();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                int i = 1;
                noOfSemesters = 0;

                rs = stmt.executeQuery("select Count(*) as noOfSemesters from Semester where semYear='"+yearSpiner.getSelectedItem()+"'");
                if (rs.next()) {
                    noOfSemesters = (rs.getInt("noOfSemesters"));
                }

                semesterNo=new String[noOfSemesters+1];
                semesterNo[0]="Select Semester";
                rs = stmt.executeQuery("select semName from Semester where semYear='"+yearSpiner.getSelectedItem()+"'");
                while (rs.next()) {
                    semesterNo[i++] = rs.getString("semName");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }//getSemesters

        public void setSemester(){
            ArrayAdapter<String> semesterAdapter = new ArrayAdapter<String>(AdminStudentSearchBy.this,android.R.layout.simple_spinner_dropdown_item,semesterNo);
            semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            semesterSpiner.setAdapter(semesterAdapter);
        }//setSemester

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
        setContentView(R.layout.activity_admin_student_search_by);

        Bundle bundle=new Bundle();
        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        admincourseSpiner = (Spinner)findViewById(R.id.spinnerCourse);
        yearSpiner = (Spinner)findViewById(R.id.spinnerYear);
        semesterSpiner=(Spinner)findViewById(R.id.spinnerSem);

        connectToDB = new ConnectToDB();
        String[] sql={
        };
        try {
            if(connectToDB.execute(sql).get()){
                {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Search(View view){

        if(checkEmptyFields()) {
            sharedPreferences.edit().putString("currentCourseName",particularcoursename).apply();
            sharedPreferences.edit().putString("currentYearNo",particularyear).apply();
            sharedPreferences.edit().putString("currentSemester",particularsemester).apply();
            Intent takeAttendanceActivity = new Intent(getApplicationContext(), com.example.bino.attendance.AdminStudentSeachResultActivity.class);
            startActivity(takeAttendanceActivity);
        }
    }
    public boolean checkEmptyFields(){
        String error="";
        particularcoursename =admincourseSpiner.getSelectedItem().toString();
        particularyear = yearSpiner.getSelectedItem().toString();
        particularsemester = semesterSpiner.getSelectedItem().toString();
        if( particularcoursename.equals("Select Course")){
            error="Please Select Course!!!";
        }else if( particularyear.equals("Select Year")){
            error="Please Select Year!!!";
        }else if( particularyear.equals("Select Semester")){
            error="Please Select Semester!!!";
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
