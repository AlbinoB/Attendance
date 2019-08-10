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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TeacherSearchResultIndividual extends AppCompatActivity {

    ListView listView;
    Button download;
    String[][] studentsarr ;


    public class ConnectToDB extends AsyncTask<String,Void,Boolean> {
        Intent previousIndent=getIntent();
        Connection connection = null;
        String url = null;
        Statement stmt;
        ResultSet rs = null;
        String sql = "";
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        @Override
        protected Boolean doInBackground(String... sqlarr) {


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                connection = DriverManager.getConnection(url);
                stmt = connection.createStatement();

                getDatesPresentAbsent();


                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//doInBackground




        public void getDatesPresentAbsent(){

            studentsarr=new String[(previousIndent.getIntExtra("totallecture",0))][3];
            sql="select takenDate,presentabsent,convert(varchar, takenTime, 8) as takenTime from Attendance where takenDate between '"+(String)sharedPreferences.getString("currentstartdate","no subject")+"' and '"+(String)sharedPreferences.getString("currentenddate","no subject")+"' and fkstudentErpNo=(select studentErpNo from Student where studentRollNo="+previousIndent.getStringExtra("passStudentRoll")+") and fksubjectId=(select subjectId from Subject where subjectName='"+(String)sharedPreferences.getString("currentsubjectname","no subject")+"' and fksemIdSubject=(select fksemIdStudent from Student where studentErpNo=(select studentErpNo from Student where studentRollNo="+previousIndent.getStringExtra("passStudentRoll")+")))";

            Log.i("sqldatas",sql);


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


    }//AsyncTask


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_search_result_individual);
        download = (Button) findViewById(R.id.download);
            ConnectToDB connectToDB = new ConnectToDB();
            String[] sql={

            };

            connectToDB.execute(sql);
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
            view = getLayoutInflater().inflate(R.layout.customlayoutteachersearchresultindividual, null);
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
}
