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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentViewIndividualAttendance extends AppCompatActivity {

    ListView listView;

    SharedPreferences sharedPreferences;

    Intent previousIndent;
    String[][] studentsarr ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_individual_attendance);
        previousIndent=getIntent();
        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        listView=(ListView)findViewById(R.id.listView);
        CustomAdapter customAdapter=new CustomAdapter();
        listView.setAdapter(customAdapter);
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
            CheckBox presentabsent=(CheckBox)view.findViewById(R.id.presentabsentcheckBox);
            dateTextView.setText(studentsarr[i][0]);

            if(studentsarr[i][1].equalsIgnoreCase("true")){
                presentabsent.setChecked(true);
            }else{

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
                setSubjectSName();
                setSubjectScode();


                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//doInBackground



        public void setcurrentUser(){
            TextView currentUser=(TextView)findViewById(R.id.currentUser);

            currentUser.setText((String)sharedPreferences.getString("currentUserName","no  name"));

        }
        public void setCourseName(){
            TextView courseName=(TextView)findViewById(R.id.courseName);
            courseName.setText(previousIndent.getStringExtra("courseName"));
        }
        public void setSubjectSName(){
            TextView passSname=(TextView)findViewById(R.id.sName);
            passSname.setText(previousIndent.getStringExtra("passSname"));

        }
        public void setSubjectScode(){
            TextView sCode=(TextView)findViewById(R.id.sCode);
            sCode.setText(previousIndent.getStringExtra("passScode"));

        }

        public void getNumberOfdays(){
            sql="sel";//////////////////////////////
            try {
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    int numberOfdays = (rs.getInt("countOfDays"));
                    studentsarr=new String[numberOfdays][2];
                }
                else {
                    Log.i("nothing", "nothing");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void getDatesPresentAbsent(){
            sql="sel";//////////////////////////////
            try {
                rs = stmt.executeQuery(sql);
                int i=0;
                while(rs.next()) {
                   studentsarr[i][0]=rs.getDate("takenDate")+"";
                   studentsarr[i][1]=rs.getString("presentabsent");
                   i++;
                }


            } catch (Exception e) {
                Log.i("nothing", "nothing");
                e.printStackTrace();
            }
        }


    }
}
