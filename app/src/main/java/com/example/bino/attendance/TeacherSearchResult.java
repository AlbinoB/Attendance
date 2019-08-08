package com.example.bino.attendance;

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
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class TeacherSearchResult extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ListView listView;
    Button download;
    int currentteacherid;
    String currentstartdate;
    String currentenddate;
    String currentsubject;
    String currentyear;
    String currentsem;
    String currentcourse;
    String currentteacher;
    TextView teachername;
    TextView subjectname ;
    TextView coursename ;
    TextView yearno ;
    CustomAdapter customAdapter;

    static String[][] studentsarr ;


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

                Log.i("sadasd", "asdfaf");
                getandsetcurrentdetails();
                getandsetrollnonamepercent();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }//doInBackground;


    public void getandsetcurrentdetails(){

       currentteacherid =((Integer)sharedPreferences.getInt("currentUserId",0));
       Log.i("teacher id ",""+currentteacherid);
        currentstartdate =((String)sharedPreferences.getString("currentenddate","no date"));
        currentenddate =((String)sharedPreferences.getString("currentenddate","no date"));
        currentsubject =((String)sharedPreferences.getString("currentsubjectname","no date"));
        currentyear =((String)sharedPreferences.getString("currentyear","no date"));
        currentsem =((String)sharedPreferences.getString("currentsem","no date"));
        currentcourse =((String)sharedPreferences.getString("currentcoursename","no date"));
        try{
            rs = stmt.executeQuery("select teacherName from Teacher where teacherId='"+currentteacherid+"'");
            if(rs.next()){
                currentteacher =rs.getString("teacherName") ;
                Log.i("teacher name ",""+currentteacher);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

      teachername.setText(currentteacher);
        subjectname.setText(currentsubject);
        yearno.setText(currentyear);
        coursename.setText(currentcourse);

    }

       public void  getandsetrollnonamepercent(){
                    try{
                        int i=0;
                        int totalstudent=0;
                        rs = stmt.executeQuery("select count(*) as totlastudent from Student where fksemIdStudent=(select semId from Semester where semName='"+currentsem+"')");
                        if(rs.next()){
                            totalstudent =rs.getInt("totlastudent");
                        }
                        studentsarr = new String[totalstudent][4];
                        int totallecture=0;

                        rs = stmt.executeQuery("select count(*) as totallecture from Attendance where fksubjectId=(select subjectId from Subject where subjectName = '"+currentsubject+"')");
                        if(rs.next()){
                            totallecture =rs.getInt("totallecture");
                        }
                        rs = stmt.executeQuery("select studentRollNo,studentName from Student where fksemIdStudent=(select semId from Semester where semName='"+currentsem+"')");

                        while(rs.next()){
                           String student =rs.getString("studentRollNo");
                                    studentsarr[i][0]=Integer.toString(i+1);
                                    studentsarr[i][1]=rs.getString("studentRollNo");
                                    studentsarr[i][2]=rs.getString("studentName");
                            int totalpresent=0;
                            rs = stmt.executeQuery("select count(*) as totalpresent from Attendance where (fkstudentErpNo=(select studentErpNo from Student where studentRollNo='"+student+"') AND presentabsent='P' AND fksubjectId=( select subjectId from Subject where subjectName='"+currentsubject+"' ))");
                            if(rs.next()){
                                totalpresent =rs.getInt("totalpresent");
                            }
                            float persent=0;
                            persent =(totallecture*totalpresent)/100;
                                    studentsarr[i][3]=Float.toString(persent);
                                    i++;
                                }
                        listView.setAdapter(customAdapter);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

        }


    }//AsyncTask


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_search_result);
        Log.i("new activity","teacher search result");

        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        teachername =(TextView) findViewById(R.id.textView2);
        subjectname =(TextView) findViewById(R.id.textView3);
        coursename =(TextView) findViewById(R.id.textView5);
        yearno =(TextView) findViewById(R.id.textView6);
        listView=(ListView)findViewById(R.id.listView);
        download=(Button)findViewById(R.id.download);
         customAdapter = new CustomAdapter();



        TeacherSearchResult.ConnectToDB connectToDB = new ConnectToDB();

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
            view = getLayoutInflater().inflate(R.layout.customlayoutteachersearchdisplay, null);
            TextView textViewSrno = (TextView) view.findViewById(R.id.srno);
            TextView textViewRollno = (TextView) view.findViewById(R.id.rollno);
            TextView textViewName = (TextView) view.findViewById(R.id.name);
            TextView textViewPercentage = (TextView) view.findViewById(R.id.percentage);
            textViewSrno.setText(studentsarr[i][0]);
            textViewRollno.setText(studentsarr[i][1]);
            textViewName.setText(studentsarr[i][2]);
            textViewPercentage.setText(studentsarr[i][3]);

            return view;
        }
    }
}
