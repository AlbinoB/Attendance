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
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentViewIndividualAttendance extends AppCompatActivity {

    ListView listView;
    SharedPreferences sharedPreferences;
    TextView currentUser,courseName,passSname,sCode,semName,semYear;
    String semStartDate=null,semEndDate=null;
    Intent previousIndent;
    String[][] studentsarr ;
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

                setcurrentUser();
                setCourseName();
                setSemNameAndYear();
                setSubjectSName();
                setSubjectScode();
                getStartAndEndDate();
                getNumberOfdays();

                return true;
            }catch (SQLException e) {
                e.printStackTrace();

                if(!isOnline()){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(StudentViewIndividualAttendance.this)
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

        public void setcurrentUser(){

            currentUser=(TextView)findViewById(R.id.studentName);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    currentUser.setText((String)sharedPreferences.getString("currentUserName","no  name"));
                }
            });
        }

        public void setCourseName(){

            handler.post(new Runnable() {
                @Override
                public void run() {
                    courseName=(TextView)findViewById(R.id.courseName);
                    courseName.setText(previousIndent.getStringExtra("courseName"));
                }
            });
        }

        public void setSemNameAndYear(){

            handler.post(new Runnable() {
                @Override
                public void run() {
                    semName=(TextView)findViewById(R.id.semName);
                    semYear=(TextView)findViewById(R.id.semYear);
                    semName.setText(previousIndent.getStringExtra("semName"));
                    semYear.setText(previousIndent.getStringExtra("semYear"));
                }
            });
        }

        public void setSubjectSName(){

            handler.post(new Runnable() {
                @Override
                public void run() {
                    passSname=(TextView)findViewById(R.id.studentRollNo);
                    passSname.setText(previousIndent.getStringExtra("passSname"));
                }
            });
        }

        public void setSubjectScode(){

            handler.post(new Runnable() {
                @Override
                public void run() {
                    sCode=(TextView)findViewById(R.id.sCode);
                    sCode.setText(previousIndent.getStringExtra("passScode"));
                }
            });
        }

        public void getStartAndEndDate(){

            sql="select semStartDate,semEndDate from Semester where semId=(select fksemIdStudent from Student where studentErpNo="+(Integer)sharedPreferences.getInt("currentUserErpNo",0)+")";
            try {
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    semStartDate = rs.getString("semStartDate");
                    semEndDate = rs.getString("semEndDate");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void getNumberOfdays(){

            sql="select fksubjectId,count(*) as totalLectures from Attendance where takenDate between '"+semStartDate+"' and '"+semEndDate+"' and fkstudentErpNo=(select studentErpNo from Student where studentErpNo='"+(Integer)sharedPreferences.getInt("currentUserErpNo",0)+"') and fksubjectId=(select subjectId from Subject where subjectId="+previousIndent.getStringExtra("passScode")+") group by fksubjectId";

            try {
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    int numberOfdays = (rs.getInt("totalLectures"));
                    studentsarr=new String[numberOfdays][3];

                    getDatesPresentAbsent();//if days are fetched then call else
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void getDatesPresentAbsent(){

            String semStartDate=null,semEndDate=null;
            sql="select semStartDate,semEndDate from Semester where semId=(select fksemIdStudent from Student where studentErpNo="+(Integer)sharedPreferences.getInt("currentUserErpNo",0)+")";

            try {
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    semStartDate = rs.getString("semStartDate");
                    semEndDate = rs.getString("semEndDate");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            sql="select takenDate,presentabsent,convert(varchar, takenTime, 8) as takenTime from Attendance where takenDate between '"+semStartDate+"' and '"+semEndDate+"' and fkstudentErpNo=(select studentErpNo from Student where studentErpNo="+(Integer)sharedPreferences.getInt("currentUserErpNo",0)+") and fksubjectId=(select subjectId from Subject where subjectId="+previousIndent.getStringExtra("passScode")+" and fksemIdSubject=(select fksemIdStudent from Student where studentErpNo="+(Integer)sharedPreferences.getInt("currentUserErpNo",0)+"))";

            listView=(ListView)findViewById(R.id.listView);
            CustomAdapter customAdapter=new CustomAdapter();
            listView.setAdapter(customAdapter);

            try {
                rs = stmt.executeQuery(sql);
                int i=0;
                while(rs.next()) {
                    studentsarr[i][0]=rs.getDate("takenDate")+"";
                    studentsarr[i][1]=rs.getString("presentabsent");
                    studentsarr[i][2]=rs.getString("takenTime");
                    i++;
                }
            } catch (SQLException e) {
                e.printStackTrace();

                if(!isOnline()){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(StudentViewIndividualAttendance.this).
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
        }

        public boolean isOnline() {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_individual_attendance);

        previousIndent=getIntent();
        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

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

            view = getLayoutInflater().inflate(R.layout.customlayoutstudentindividualattendance, null);
            TextView dateTextView=(TextView)view.findViewById(R.id.dateTextView);
            TextView takenTime=(TextView)view.findViewById(R.id.takenTime);
            CheckBox presentabsent=(CheckBox)view.findViewById(R.id.presentabsentcheckBox);

            dateTextView.setText(studentsarr[i][0]);
            takenTime.setText(studentsarr[i][2]);

            if(studentsarr[i][1].equalsIgnoreCase("P")){
                presentabsent.setChecked(true);
                presentabsent.setEnabled(false);
            }else{
                presentabsent.setEnabled(false);
                presentabsent.setChecked(false);
            }

            return  view;
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
                finishAffinity();
                startActivity(homeActivity);
                return true;
            }

            default:{
                return false;
            }
        }
    }

}
