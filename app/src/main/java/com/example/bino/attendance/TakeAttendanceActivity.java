package com.example.bino.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TakeAttendanceActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String currentcourse;
    String currentyear;
    String currentsem;
    String currentsubject;

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

                getCourseYearSemSubject();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }//doInBackground

        public void getCourseYearSemSubject(){
            currentcourse =((String)sharedPreferences.getString("currentCourseName","no name"));
            currentyear =((String)sharedPreferences.getString("currentYearNo","no year"));
            currentsem =((String)sharedPreferences.getString("currentSemNo","no sem"));
            currentsubject =((String)sharedPreferences.getString("currentSubjectName","no subject"));

        }

        
    }//AsyncTask

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);


        TakeAttendanceActivity.ConnectToDB connectToDB = new ConnectToDB();


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

    public  void ShowAttendance(View view){
        Button savebutton =(Button) findViewById(R.id.SaveAttendanceButton);
        Intent showtakenattendance = new Intent(getApplicationContext(), ShowTakenAttendanceActivity.class);
        startActivity(showtakenattendance);
    }
}
