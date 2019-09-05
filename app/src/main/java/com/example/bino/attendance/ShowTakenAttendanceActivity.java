package com.example.bino.attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ShowTakenAttendanceActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String[][] studentNameRollno;
    ProgressDialog progressdialog;
    Handler handler =new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_taken_attendance);

        TextView ShowSubjectTextView,ShowDateTextView,ShowTotalTextView,ShowPresentTextView,ShowAbsentTextView;

        ShowSubjectTextView=(TextView)findViewById(R.id.ShowSubjectTextView);
        ShowDateTextView=(TextView)findViewById(R.id.ShowDateTextView);
        ShowTotalTextView=(TextView)findViewById(R.id.ShowTotalTextView);
        ShowPresentTextView=(TextView)findViewById(R.id.ShowPresentTextView);
        ShowAbsentTextView=(TextView)findViewById(R.id.ShowAbsentTextView);

        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        String stringFullArray=(String)sharedPreferences.getString("stringFullArray","no value");
        int noOfStudent=sharedPreferences.getInt("noOfStudent",0);
        studentNameRollno=new String[noOfStudent][9];
        studentNameRollno=to2dim(stringFullArray,";",",");
        int presentCount=0,absentCount=0;

        for (int f = 0; f < studentNameRollno.length; f++) {

            if(studentNameRollno[f][6].equals("P")){
                presentCount++;
            }else
            {
                absentCount++;
            }
        }

        ShowSubjectTextView.setText((String)sharedPreferences.getString("currentSubjectName","no subject"));
        ShowDateTextView.setText(studentNameRollno[0][4]);
        ShowTotalTextView.setText(noOfStudent+"");
        ShowPresentTextView.setText(presentCount+"");
        ShowAbsentTextView.setText(absentCount+"");
    }

    public static String [][] to2dim (String source, String outerdelim, String innerdelim) {
        // outerdelim may be a group of characters
        String [] sOuter = source.split ("[" + outerdelim + "]");
        int size = sOuter.length;
        // one dimension of the array has to be known on declaration:
        String [][] result = new String [size][];
        int count = 0;
        for (String line : sOuter)
        {
            result [count] = line.split (innerdelim);
            ++count;
        }
        return result;
    }

    public  void EditAttendance(View view){

        Intent editattendanceactivity = new Intent(getApplicationContext(), EditAttendanceActivity.class);
        finish();
        startActivity(editattendanceactivity);
    }

    public  void SaveAttendance(View view){
        progressdialog = new ProgressDialog(ShowTakenAttendanceActivity.this);
        progressdialog.setMessage("Saving To Database....");
        progressdialog.setCancelable(false);
        progressdialog.show();
        Thread savingDetailsThread=new Thread(new Runnable() {
            @Override
            public void run() {
                String[] sql={""};
                ConnectToDB connectToDB=new ConnectToDB();
                connectToDB.execute(sql);
            }
        });
        savingDetailsThread.start();

        Intent teacherhomeactivity = new Intent(getApplicationContext(), TeacherHomeActivity.class);
        finish();
        startActivity(teacherhomeactivity);
    }

    public class ConnectToDB extends AsyncTask<String,Void,Boolean> {
        @Override
        protected Boolean doInBackground(String... sqlarr) {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection connection=null;
            String url=null;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                connection= DriverManager.getConnection(url);
                Statement stmt = connection.createStatement();
                ResultSet rs=null;
                String stringFullArrayUpdated="";
                String sql="";
                for (int f = 0; f < studentNameRollno.length; f++) {

                    sql="insert into Attendance values("+Integer.parseInt(studentNameRollno[f][0]) + "," + Integer.parseInt(studentNameRollno[f][1]) + "," + Integer.parseInt(studentNameRollno[f][2]) + "," + Integer.parseInt(studentNameRollno[f][3]) + ",convert(varchar,'" + studentNameRollno[f][4] + "', 8),'" + studentNameRollno[f][5] + "','" + studentNameRollno[f][6]+"')";
                    stmt.executeUpdate(sql);

                }
                    return true;
            }
            catch (Exception e){
                e.printStackTrace();
                return  false;
            }
        }
    }
}
