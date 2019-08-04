package com.example.bino.attendance;
import java.sql.*;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        //startActivity(loginActivity);
        connectionClass();
    }

    public  Connection connectionClass(){
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection=null;
        String url=null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            connection=DriverManager.getConnection(url);
            Statement stmt = connection.createStatement();
            String sql="Insert into Course VALUES(111111,'MCA','')";
            ResultSet rs=null;
            int sucessOrFail = stmt.executeUpdate(sql);
           if(sucessOrFail!=-1){
               Log.i("table:","created");
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
}
