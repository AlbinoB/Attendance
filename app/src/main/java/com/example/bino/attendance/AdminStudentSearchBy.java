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
    int currentTeacherTextView;
    String particularcoursename;
    String particularyear;
    String particularsemester;
    String particularsubject;
    SharedPreferences sharedPreferences;
    ConnectToDB connectToDB;

    Handler handler =new Handler();
    Handler handler1 =new Handler();

    public Spinner courseSpiners,yearSpiners;
    String[] coursenames ;
    String[] yearNo ;
    int noOfYears=0;

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

                courseSpiners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        Log.i("course name", "" + courseSpiners.getSelectedItem());
                        Log.i("clicked", "on item selected");
                        // setYears();
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                getYear();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setYears();
                                        Log.i("set year", "on item selected");
                                    }
                                });
                            }
                        });

                        thread.start();
                        // setYears();
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
                int i=1;
               int  noOfCourse=0;
                rs = stmt.executeQuery("select count(*) as countOfCourse from Course");

                if(rs.next()){
                    noOfCourse  = (rs.getInt("countOfCourse"));
                }
                Log.i("no of course is ",""+noOfCourse);
                coursenames =new String[noOfCourse+1];
                coursenames[0]="Select Course";
                rs = stmt.executeQuery("select courseName from Course");

                while(rs.next()) {
                    coursenames[i] = rs.getString("courseName");
                    Log.i("course nAME ",""+coursenames[i]);
                    i++;
                }

                ArrayAdapter<String> courseAdapters = new ArrayAdapter<String>(AdminStudentSearchBy.this,android.R.layout.simple_spinner_dropdown_item,coursenames);
                courseAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                courseSpiners.setAdapter(courseAdapters);



            }catch(Exception e){
                e.printStackTrace();
            }
        }//getAndSetCourseName

        public void getYear(){

            try {
                Log.i("Years:","year called");
                int i = 1;
                noOfYears = 0;

                rs = stmt.executeQuery("select count(*) as noOfYears from CourseYears where fkcourseIdCourseYears=(select courseId from Course where courseName='"+courseSpiners.getSelectedItem()+"')");
                if (rs.next()) {
                    noOfYears = (rs.getInt("noOfYears"));

                }
                Log.i("noOfYears:",noOfYears+"");
                yearNo=new String[noOfYears+1];
                yearNo[0]="Select Year";
                rs = stmt.executeQuery("select courseYears from CourseYears where fkcourseIdCourseYears=(select courseId from Course where courseName='"+courseSpiners.getSelectedItem()+"')");
                while (rs.next()) {
                    Log.i("courseYears:",rs.getString("courseYears"));
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
            yearSpiners.setAdapter(yearAdapter);
        }//setYears





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

        courseSpiners = (Spinner)findViewById(R.id.CourseSpinner);
        yearSpiners = (Spinner)findViewById(R.id.yearSpinner);

        connectToDB = new ConnectToDB();
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

    public void Search(View view){
        Log.i("inside ","nothing ");
        if(checkEmptyFields()) {
            sharedPreferences.edit().putString("currentCourseName",particularcoursename).apply();
            sharedPreferences.edit().putString("currentYearNo",particularyear).apply();
            Intent takeAttendanceActivity = new Intent(getApplicationContext(), com.example.bino.attendance.TakeAttendanceActivity.class);
            startActivity(takeAttendanceActivity);

        }

    }
    public boolean checkEmptyFields(){
        String error="";
        particularcoursename =courseSpiners.getSelectedItem().toString();
        particularyear = yearSpiners.getSelectedItem().toString();
        Log.i("course selected ",particularcoursename);
        if( particularcoursename.equals("Select Course")){
            error="Please Select Course!!!";

        }else if( particularyear.equals("Select Year")){
            error="Please Select Year!!!";
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
