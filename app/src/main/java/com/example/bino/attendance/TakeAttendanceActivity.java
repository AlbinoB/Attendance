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
import android.widget.TextView;

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
    String[]studentName;
    String[] studentRollNo ;
    String[][] studentNameRollno;
    TextView studentshowname;
    TextView studentshowrollno;
    int countfornoofstudent=1;
    int lengthofstudentarray;
    Button absentbutton;
    Button presentbutton;
    Button saveattendancebutton;

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
                getNameAndRollNo();

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

        public void getNameAndRollNo(){
            try{
                int noOfStudent=0;
                int i=0;
                rs = stmt.executeQuery("select count(*) As totalstudent from Student where (fkcourseIdStudent=(select courseId from Course where courseName='"+currentcourse+"')   AND fksemIdStudent=(select semId from Semester where semName='"+currentsem+"'))");
                if(rs.next()){
                    noOfStudent  = (rs.getInt("totalstudent"));
                    lengthofstudentarray=noOfStudent;
                }
                studentName =new String[noOfStudent];
                studentRollNo=new String[noOfStudent];
                studentNameRollno =new String[noOfStudent][2];

                rs = stmt.executeQuery("select studentName,studentRollNo from Student where (fkcourseIdStudent=(select courseId from Course where courseName='"+currentcourse+"')   AND fksemIdStudent=(select semId from Semester where semName='"+currentsem+"'))ORDER BY studentRollNo");

                    while(rs.next()){
                      //  studentName[i] = rs.getString("studentName");
                      //  studentRollNo[i]=rs.getString("studentRollNo");

                            studentNameRollno[i][0] =rs.getString("studentName");
                        studentNameRollno[i][1] =rs.getString("studentRollNo");
                            Log.i("name no ",""+studentNameRollno[i][0]);
                        Log.i("roll no ",""+studentNameRollno[i][1]);
                       i++;
                      //  Log.i("name ",""+studentName[i]);
                      //  Log.i("roll no ",""+studentRollNo[i]);
                    }

                studentshowname =(TextView)findViewById(R.id.StudentShowNameTextView);
                studentshowrollno =(TextView)findViewById(R.id.StudentShowRollNoTextView);
                studentshowname.setText(studentNameRollno[0][0]);
                studentshowrollno.setText(studentNameRollno[0][1]);

            }catch(Exception e){
                e.printStackTrace();
            }
        }


    }//AsyncTask

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);
        absentbutton =(Button)findViewById(R.id.AbsentButton);
        presentbutton=(Button)findViewById(R.id.PresentButton);
        saveattendancebutton=(Button)findViewById(R.id.SaveAttendanceButton);

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
        public void markAbsent(View view){
        if(countfornoofstudent<lengthofstudentarray) {
            studentshowname.setText(studentNameRollno[countfornoofstudent][0]);
            studentshowrollno.setText(studentNameRollno[countfornoofstudent][1]);
            countfornoofstudent++;
        } else{
            saveattendancebutton.setVisibility(View.VISIBLE);
            absentbutton.setVisibility(View.INVISIBLE);
            presentbutton.setVisibility(View.INVISIBLE);
        }
        }

    public void markPresent(View view){
        if(countfornoofstudent<lengthofstudentarray) {
            studentshowname.setText(studentNameRollno[countfornoofstudent][0]);
            studentshowrollno.setText(studentNameRollno[countfornoofstudent][1]);
            countfornoofstudent++;
        } else{
            saveattendancebutton.setVisibility(View.VISIBLE);
            absentbutton.setVisibility(View.INVISIBLE);
            presentbutton.setVisibility(View.INVISIBLE);
        }
    }

    public  void ShowAttendance(View view){
        Button savebutton =(Button) findViewById(R.id.SaveAttendanceButton);
        Intent showtakenattendance = new Intent(getApplicationContext(), ShowTakenAttendanceActivity.class);
        startActivity(showtakenattendance);
    }
}
