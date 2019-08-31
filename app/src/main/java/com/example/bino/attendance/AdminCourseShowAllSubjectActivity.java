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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminCourseShowAllSubjectActivity extends AppCompatActivity {

    ListView subjectnamelistview;
    SharedPreferences sharedPreferences;
    String[][] subjectdetails;
    String course,year,sem;
    TextView coursename,courseyear,coursesem,subjectcode,subjectname,teachername;

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


              getCourseYearAndSem();
              getandsetsubject();


                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//doInBackground

        public void getCourseYearAndSem(){
            course =((String) sharedPreferences.getString("adminCourseName", "no name"));
            year =((String) sharedPreferences.getString("adminYearNo", "no name"));
            sem =((String) sharedPreferences.getString("adminSemNo", "no name"));
            coursename.setText(course);
            courseyear.setText(year);
            coursesem.setText(sem);
        }

        public void getandsetsubject(){
            int j=0;
            try{
                rs = stmt.executeQuery("select  count(*) as noofsubject from Subject,Course,Teacher,Semester where semId=fksemIdSubject and fkcourseIdSubject=courseId and fkcourseIdTeacher=courseId and fkteacherIdSubject=teacherId  and semName='"+sem+"' and  courseName='"+course+"'");
                if(rs.next()){
                    Log.i("no of subject",""+rs.getInt("noofsubject"));
                    subjectdetails = new String[rs.getInt("noofsubject")][3];
                }

                rs = stmt.executeQuery("select  subjectId ,subjectName,teacherName from Subject,Course,Teacher,Semester where semId=fksemIdSubject and fkcourseIdSubject=courseId and fkcourseIdTeacher=courseId and fkteacherIdSubject=teacherId  and semName='"+sem+"' and  courseName='"+course+"'  ");
                while(rs.next()){
                    subjectdetails[j][0] =Integer.toString( rs.getInt("subjectId"));
                    subjectdetails[j][1]=rs.getString("subjectName");
                    subjectdetails[j][2]=rs.getString("teacherName");
                    j++;
                }
                CustomAdapter customadapters = new CustomAdapter();
                subjectnamelistview.setAdapter(customadapters);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }//AsyncTask


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_show_all_subject);

        subjectnamelistview= (ListView)findViewById(R.id.AllSubjectListView);
        coursename =(TextView) findViewById(R.id.AdminCourseNameTextView);
         courseyear =(TextView) findViewById(R.id.AdminYearTextView);
         coursesem =(TextView) findViewById(R.id.AdminSemTextView);


        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);





        AdminCourseShowAllSubjectActivity.ConnectToDB connectToDB=new ConnectToDB();//obj of async class

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

    public void editCourseButton(View view){
        Intent adminCourseEditAddSubject = new Intent(getApplicationContext(), AdminCourseViewEditAddCourseDetails.class);
        startActivity(adminCourseEditAddSubject);
    }

        class CustomAdapter extends BaseAdapter{

            @Override
            public int getCount() {
                return subjectdetails.length;
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
                view = getLayoutInflater().inflate(R.layout.customlayoutadmincourseshowallsubject,null);
                subjectcode = (TextView) (view).findViewById(R.id.SubjectCodeTextView);
                 subjectname = (TextView)  (view).findViewById(R.id.SubjectNameTextView);
                 teachername = (TextView) (view). findViewById(R.id.TeacherNamesTextView);

                    subjectcode.setText(subjectdetails[i][0]);
                    subjectname.setText(subjectdetails[i][1]);
                    teachername.setText(subjectdetails[i][2]);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent adminCourseEditAddSubject = new Intent(getApplicationContext(), AdminCourseViewEditAddCourseDetails.class);
                        adminCourseEditAddSubject.putExtra("subjectid",subjectcode.getText().toString());
                        adminCourseEditAddSubject.putExtra("check","show");
                        startActivity(adminCourseEditAddSubject);
                    }
                });
                Button button =(Button)view.findViewById(R.id.editAttendance);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent adminCourseEditAddSubject = new Intent(getApplicationContext(), AdminCourseViewEditAddCourseDetails.class);
                        adminCourseEditAddSubject.putExtra("subjectid",subjectcode.getText().toString());
                        adminCourseEditAddSubject.putExtra("check","edit");

                        startActivity(adminCourseEditAddSubject);
                    }
                });
                return view;
            }
        }
}
