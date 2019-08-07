package com.example.bino.attendance;

/**
 * Created by Bino on 8/6/2019.
 */

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class DatabaseConnection {
/*
    public ResultSet ReturnResultSet(String sql){
        String[] sqlToPass={sql};
        ConnectToDB connectToDB=new ConnectToDB();
        return (connectToDB.execute(sqlToPass));
    }
*/
/*
    public static class ConnectToDB extends AsyncTask<String,Void,ResultSet> {
        @Override

          public ResultSet doInBackground(String... sqlToRecieved) {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection connection=null;
            String url=null;
            Statement stmt=null;
            ResultSet rs=null;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                connection= DriverManager.getConnection(url);
                stmt = connection.createStatement();
                rs=null;
                rs = stmt.executeQuery(sqlToRecieved[0]);
                Log.i("dataConnectToDB",sqlToRecieved[0]);
                    return (ResultSet)stmt.executeQuery(sqlToRecieved[0]);
            }
            catch (Exception e){
                e.printStackTrace();
                    return  null;
            }
        }
    }
    */
}
