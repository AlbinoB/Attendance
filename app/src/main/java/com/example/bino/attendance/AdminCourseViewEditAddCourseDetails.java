package com.example.bino.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminCourseViewEditAddCourseDetails extends AppCompatActivity {

            EditText newcoursename,newcourseid,sdate;
            int countid;

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

                    rs=stmt.executeQuery("select top 1 courseId from Course order by courseId desc ");
                    if(rs.next()){
                        countid = rs.getInt("subjectId");
                    }
                    countid=countid+1;

                    stmt.executeQuery(" insert into Course values("+countid+",'"+newcoursename.getText().toString()+"','"+sdate.getText().toString()+"')   ");



                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                    }
            }//doInBackground
        }//AsyncTask

         @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_course_view_edit_add_course_details);

            newcoursename = (EditText)findViewById(R.id.CourseNameShowAddEditText) ;
            newcourseid = (EditText)findViewById(R.id.CourseCourseIdAddEditView) ;
            sdate = (EditText)findViewById(R.id.CourseJoinDateShowAddEditText) ;

            AdminCourseViewEditAddCourseDetails.ConnectToDB connectToDB=new ConnectToDB();//obj of async class

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

            public void editCourseDetails(View view){

                 }
            public void saveCourseDetails(View view){
              Intent adminCourseHomeActivity = new Intent(getApplicationContext(), AdminCourseHomeActivity.class);
             AdminCourseViewEditAddCourseDetails.this.finish();
             startActivity(adminCourseHomeActivity);
            }
    }
