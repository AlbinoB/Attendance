package com.example.bino.attendance;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    /** Duration of wait **/
    //private final int SPLASH_DISPLAY_LENGTH = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

           /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
           public void run() {
                /* Create an Intent that will start the Menu-Activity. */
              Intent mainIntent = new Intent(MainActivity.this,LoginActivity.class);
              MainActivity.this.startActivity(mainIntent);
               MainActivity.this.finish();
           }
        }, 3000);

       //Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
       //finish();
       //startActivity(loginActivity);
       //connectionClass();
    }
/*
    public  Connection connectionClass(){
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection=null;
        String url=null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.n

            String sql="SELECT * FROM Course";
            ResultSet rs=null;
            rs = stmt.executeQuery(sql);
           if(rs!=null){
               Log.i("table:","fetched");
               DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
               java.util.Date date = new java.util.Date();

               while(rs.next()){
                   int courseId  = rs.getInt(1);
                   String courseName = rs.getString(2);
                   date = rs.getDate(3);
                   String fetchedDate=dateFormat.format(date);
                   System.out.println("courseId:"+courseId  + "\tcourseName:" + courseName + "\tfetchedDate:" + fetchedDate+"\n");
                   System.out.println();
               }

           }else
           {
               Log.i("table:","failed");
           }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  connection;
    }
    */
}
