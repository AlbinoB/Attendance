package com.example.bino.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentViewIndividualAttendance extends AppCompatActivity {

    ListView listView;

    SharedPreferences sharedPreferences;
    TextView currentUser,courseName,passSname,sCode,semName,semYear;

    Intent previousIndent;
    String[][] studentsarr ;

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
                    Log.i("updated:mmmmm","doneenbbfge");

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
                getNumberOfdays();



                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//doInBackground



        public void setcurrentUser(){
            currentUser=(TextView)findViewById(R.id.currentUser);

            currentUser.setText((String)sharedPreferences.getString("currentUserName","no  name"));

        }

        public void setCourseName(){
            courseName=(TextView)findViewById(R.id.courseName);
            courseName.setText(previousIndent.getStringExtra("courseName"));
        }

        public void setSemNameAndYear(){
            semName=(TextView)findViewById(R.id.semName);
            semYear=(TextView)findViewById(R.id.semYear);
            semName.setText(previousIndent.getStringExtra("semName"));
            semYear.setText(previousIndent.getStringExtra("semYear"));
        }
        public void setSubjectSName(){
            passSname=(TextView)findViewById(R.id.sName);
            passSname.setText(previousIndent.getStringExtra("passSname"));

        }
        public void setSubjectScode(){
            sCode=(TextView)findViewById(R.id.sCode);
            sCode.setText(previousIndent.getStringExtra("passScode"));

        }

        public void getNumberOfdays(){
            sql="select fksubjectId,count(*) as totalLectures from Attendance where takenDate between '2019-08-01' and '2019-08-28' and fkstudentErpNo=(select studentErpNo from Student where studentErpNo='"+(Integer)sharedPreferences.getInt("currentUserErpNo",0)+"') and fksubjectId=(select subjectId from Subject where subjectId='"+sCode.getText().toString()+"') group by fksubjectId";

            Log.i("sqldays",sql);
            try {
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    int numberOfdays = (rs.getInt("totalLectures"));
                    studentsarr=new String[numberOfdays][3];

                    getDatesPresentAbsent();//if days are fetched then call else
                }
                else {

                    Log.i("nothing", "nothing");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void getDatesPresentAbsent(){
            sql="select takenDate,presentabsent,convert(varchar, takenTime, 8) as takenTime from Attendance where takenDate between '2019-08-01' and '2019-08-28' and fkstudentErpNo=(select studentErpNo from Student where studentErpNo="+(Integer)sharedPreferences.getInt("currentUserErpNo",0)+") and fksubjectId=(select subjectId from Subject where subjectId="+Integer.parseInt(sCode.getText().toString())+" and fksemIdSubject=(select fksemIdStudent from Student where studentErpNo="+(Integer)sharedPreferences.getInt("currentUserErpNo",0)+"))";

            Log.i("sqldatas",sql);
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


            } catch (Exception e) {
                Log.i("nothing", "nothing");
                e.printStackTrace();
            }
        }


    }
}
