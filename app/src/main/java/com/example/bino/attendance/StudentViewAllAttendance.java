package com.example.bino.attendance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentViewAllAttendance extends AppCompatActivity {

    String[][] studentsarr ;
    ListView studentlistView;
    SharedPreferences sharedPreferences;
    TextView currentUserTextView,studentRollNoTextView,studentCourseNameTextView,semName,semYear;
    CustomAdapter customAdapter;
    int numberOfSubjects=0;

    Handler handler =new Handler();

    public class ConnectToDB extends AsyncTask<String,Void,Boolean> {

            Connection connection = null;
            String url = null;
            Statement stmt,stmt2;
            ResultSet rs = null,rs2=null;
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
                    stmt2 = connection.createStatement();

                    getAndSetStudentName();
                    getCourseName();
                    getSubjectCount();
                    getStudentRoll();
                    getSubjectCodeAndNamesOfPaticularCourse();
                    getSemYear();

                    return true;
                }catch (SQLException e) {
                    e.printStackTrace();
                    if(!isOnline()){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(StudentViewAllAttendance.this)
                                        .setTitle("No Internet!")
                                        .setMessage("Please check your Internet Connection.")
                                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User clicked OK button
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                        });

                    }
                    return  false;
                }
                catch (Exception e){
                    e.printStackTrace();
                    return  false;
                }
            }//doInBackground

        public void getAndSetStudentName() {
            currentUserTextView.setText((String)sharedPreferences.getString("currentUserName","no  name"));
        }

        public void getSemYear(){
            sql="select semName,semYear from Semester where semId=(select fksemIdStudent from Student where studentErpNo='"+(Integer)sharedPreferences.getInt("currentUserErpNo",0)+"')";
            rs=null;
            try {
                rs = stmt.executeQuery(sql);
                if (rs.next()){

                 semName.setText(rs.getString("semName"));
                 semYear.setText(rs.getString("semYear"));
                }
                else {
                    Log.i("nothing", "nothing");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void getCourseName() {

            try {
                rs=null;
                rs = stmt.executeQuery("select courseName from Course where courseId=(select fkcourseIdStudent from Student where studentErpNo='"+(Integer)sharedPreferences.getInt("currentUserErpNo",0)+"')");

                if (rs.next())
                    studentCourseNameTextView.setText(rs.getString("courseName"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }//getCourseName

        public void getSubjectCount() {
            sql="select count(*) as countOfSubjects from Subject where fkcourseIdSubject in (select courseId from Course where courseName='"+studentCourseNameTextView.getText().toString()+"')and fksemIdSubject=(select fksemIdStudent from Student where studentName='"+currentUserTextView.getText().toString()+"')";
            try {
            rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    numberOfSubjects = (rs.getInt("countOfSubjects"));
                    studentsarr =new String[numberOfSubjects][3];
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }//getSubjectCount

        public void getSubjectCodeAndNamesOfPaticularCourse() {

            int totallecture=0;
            String semStartDate=null,semEndDate=null;
            try {
                //get start and end date from semester table
                sql="select semStartDate,semEndDate from Semester where semId=(select fksemIdStudent from Student where studentErpNo="+(Integer)sharedPreferences.getInt("currentUserErpNo",0)+")";
                rs = stmt.executeQuery(sql);
                if(rs.next()){
                    semStartDate=rs.getString("semStartDate");
                    semEndDate=rs.getString("semEndDate");
                }

                sql = "select subjectId,subjectName from Subject,Course where fkcourseIdSubject =courseId and  courseName='" + studentCourseNameTextView.getText().toString() + "' and fksemIdSubject=(select fksemIdStudent from Student where studentName='"+currentUserTextView.getText().toString()+"')";
                rs = null;
                rs = stmt.executeQuery(sql);
                int indexOfstudentarr = 0;
                while (rs.next()) {
                    studentsarr[indexOfstudentarr][0] = Integer.toString(rs.getInt("subjectId"));
                    studentsarr[indexOfstudentarr][1] = rs.getString("subjectName");
                    rs2 = stmt2.executeQuery("select count(*) as totallecture from Attendance where takenDate between '"+semStartDate+"' and '"+semEndDate+"' and fksubjectId=(select subjectId from Subject where subjectName='"+rs.getString("subjectName")+"') and fkstudentErpNo=(select studentErpNo from Student where studentRollNo='"+studentRollNoTextView.getText()+"')");
                    if(rs2.next()){
                        totallecture =(Integer)rs2.getInt("totallecture");
                    }

                    int totalpresent=0;
                    sql="select count(*) as totalpresent from Attendance where( (takenDate between '"+semStartDate+"' and '"+semEndDate+"') and( fkstudentErpNo=(select studentErpNo from Student where studentRollNo='"+studentRollNoTextView.getText()+"'))and( presentabsent='P') and (fksubjectId=(select subjectId from Subject where subjectName='"+rs.getString("subjectName")+"')))";
                    rs2 = stmt2.executeQuery(sql);
                    if(rs2.next()){
                        totalpresent =(Integer)rs2.getInt("totalpresent");
                    }

                    float percent=0;

                    if(totallecture!=0){
                        percent =(100*totalpresent)/totallecture;
                        studentsarr[indexOfstudentarr][2]=Float.toString(percent);

                        indexOfstudentarr++;
                    }else
                    {
                        studentsarr[indexOfstudentarr][2]="N/A";
                        indexOfstudentarr++;
                    }
                }

                studentlistView=(ListView)findViewById(R.id.listView);
                customAdapter=new CustomAdapter();
                studentlistView.setAdapter(customAdapter);

                studentlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent studentviewindividualattendance = new Intent(getApplicationContext(), StudentViewIndividualAttendance.class);
                        studentviewindividualattendance.putExtra("passScode",studentsarr[i][0]);
                        studentviewindividualattendance.putExtra("passSname",studentsarr[i][1]);
                        studentviewindividualattendance.putExtra("courseName",studentCourseNameTextView.getText().toString());
                        studentviewindividualattendance.putExtra("semName",semName.getText().toString());
                        studentviewindividualattendance.putExtra("semYear",semYear.getText().toString());

                        startActivity(studentviewindividualattendance);
                    }
                });

            } catch (SQLException e) {
                e.printStackTrace();
                if(!isOnline()){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(StudentViewAllAttendance.this).
                                    setTitle("No Internet!").
                                    setMessage("Please check your Internet Connection.").
                                    setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User clicked OK button
                                        }
                                    })
                                    .show();
                        }
                    });

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }//getSubjectCodeAndNamesOfPaticularCourse

            public boolean isOnline() {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                return (networkInfo != null && networkInfo.isConnected());
            }

        public void getStudentRoll() {

            try {
                sql="select studentRollNo from Student where studentErpNo='"+(Integer)sharedPreferences.getInt("currentUserErpNo",0)+"'";
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    studentRollNoTextView.setText(Integer.toString(rs.getInt("studentRollNo")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }//getStudentRoll

    }//AsyncTask

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_all_attendance);

        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        currentUserTextView=(TextView)findViewById(R.id.studentName);
        studentRollNoTextView=(TextView)findViewById(R.id.studentRollNo);
        studentCourseNameTextView=(TextView)findViewById(R.id.studentCourseName);
        semName=(TextView)findViewById(R.id.semName);
        semYear=(TextView)findViewById(R.id.semYear);

        ConnectToDB connectToDB=new ConnectToDB();//obj of async class

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


    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return studentsarr.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.customlayoutstudentallattendance, null);
            TextView textViewScode = (TextView) view.findViewById(R.id.sCode);
            TextView textViewSname = (TextView) view.findViewById(R.id.studentRollNo);
            TextView textViewSpercentage= (TextView) view.findViewById(R.id.sPercentage);
            textViewScode.setText(studentsarr[i][0]);
            textViewSname.setText(studentsarr[i][1]);
            textViewSpercentage.setText(studentsarr[i][2]);

            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:{
                Intent homeActivity=new Intent();
                homeActivity=new Intent(getApplicationContext(),LoginActivity.class);
                finish();
                startActivity(homeActivity);
                return true;
            }

            default:{
                return false;
            }
        }
    }

}
