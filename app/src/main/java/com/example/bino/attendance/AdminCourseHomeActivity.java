package com.example.bino.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminCourseHomeActivity extends AppCompatActivity {

    private Spinner admincourseSpiner,adminyearSpiner,adminsemesterSpiner;
    SharedPreferences sharedPreferences;
     String[] admincoursename;
     String[] adminyearNo;
     String[] adminsemesterNo ;
     int noOfYears,noOfSemesters;
     String particularcoursename,particularyear,particularsemester;
    Handler handler =new Handler();

    public class ConnectToDB extends AsyncTask<String,Void,Boolean> {

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
                getandsetcourse();
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

                adminyearSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
        }//doInBackground

        public void getandsetcourse(){
            try{
                rs = stmt.executeQuery("select  count(*) as noofcourse  from Course");

                while(rs.next()) {
                    admincoursename = new String[(rs.getInt("noofcourse")) + 1];
                }
                int i=1;

                admincoursename[0]="Select Course";
                rs= stmt.executeQuery("select courseName from Course");
                while(rs.next()){
                    admincoursename[i]=rs.getString("courseName");
                    i++;
                }
                ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(AdminCourseHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,admincoursename);
                courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                admincourseSpiner.setAdapter(courseAdapter);
            }catch (Exception e){
                e.printStackTrace();
            }
        }//getandsetcourse


        public void getYear(){
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
                noOfYears = 0;

                rs = stmt.executeQuery("select count(*) as noOfYears from CourseYears where fkcourseIdCourseYears=(select courseId from Course where courseName='"+admincourseSpiner.getSelectedItem()+"')");
                if (rs.next()) {
                    noOfYears = (rs.getInt("noOfYears"));

                }
                adminyearNo=new String[noOfYears+1];
                adminyearNo[0]="Select Year";
                rs = stmt.executeQuery("select courseYears from CourseYears where fkcourseIdCourseYears=(select courseId from Course where courseName='"+admincourseSpiner.getSelectedItem()+"')");
                while (rs.next()) {
                    adminyearNo[i++] = rs.getString("courseYears");

                }

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }//getYear

        public void setYears(){

            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(AdminCourseHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,adminyearNo);
            yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adminyearSpiner.setAdapter(yearAdapter);
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
                rs = stmt.executeQuery("select Count(*) as noOfSemesters from Semester where semYear='"+adminyearSpiner.getSelectedItem()+"'");
                if (rs.next()) {
                    noOfSemesters = (rs.getInt("noOfSemesters"));

                }
                adminsemesterNo=new String[noOfSemesters+1];
                adminsemesterNo[0]="Select Semester";
                rs = stmt.executeQuery("select semName from Semester where semYear='"+adminyearSpiner.getSelectedItem()+"'");
                while (rs.next()) {
                    adminsemesterNo[i++] = rs.getString("semName");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }//getSemesters

        public void setSemester(){
            ArrayAdapter<String> semesterAdapter = new ArrayAdapter<String>(AdminCourseHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,adminsemesterNo);
            semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adminsemesterSpiner.setAdapter(semesterAdapter);
        }//setSemester

    }//AsyncTask

            public void AddNewCourse(View view){
                Intent adminCourseViewEditAddCourseDetails = new Intent(getApplicationContext(), AdminCourseViewEditAddCourseDetails.class);
                startActivity(adminCourseViewEditAddCourseDetails);
            }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_home);

        admincourseSpiner = (Spinner)findViewById(R.id.AdminCourseSpinner);
        adminyearSpiner = (Spinner)findViewById(R.id.AdminyearSpinner);
        adminsemesterSpiner = (Spinner)findViewById(R.id.AdminSemesterSpinner);

        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        AdminCourseHomeActivity.ConnectToDB connectToDB=new ConnectToDB();//obj of async class

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

    public void sumbitButtonClicked(View view){
        if(checkEmptyFields()) {
            sharedPreferences.edit().putString("adminCourseName", admincourseSpiner.getSelectedItem().toString()).apply();
            sharedPreferences.edit().putString("adminYearNo", adminyearSpiner.getSelectedItem().toString()).apply();
            sharedPreferences.edit().putString("adminSemNo", adminsemesterSpiner.getSelectedItem().toString()).apply();
            Intent adminCourseShowAllSubjectActivity = new Intent(getApplicationContext(), AdminCourseShowAllSubjectActivity.class);
            startActivity(adminCourseShowAllSubjectActivity);
        }
    }
    public boolean checkEmptyFields(){
        String error="";
        particularcoursename =admincourseSpiner.getSelectedItem().toString();
        particularyear = adminyearSpiner.getSelectedItem().toString();
        particularsemester = adminsemesterSpiner.getSelectedItem().toString();
        if( particularcoursename.equals("Select Course")){
            error="Please Select Course!!!";
        }else if( particularyear.equals("Select Year")){
            error="Please Select Year!!!";
        }else if( particularsemester.equals("Select Semester")){
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
