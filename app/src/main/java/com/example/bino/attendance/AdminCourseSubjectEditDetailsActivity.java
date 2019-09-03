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
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminCourseSubjectEditDetailsActivity extends AppCompatActivity {

    Intent intent;
    int countid;
    String subjectcode,check,subjectnames,subjectTeachername,subjectids;
    EditText subjectname,subjectid,subjectteachername,subjectcoursename,subjectsemname;
    Button savesubjectdetails;
    AdminCourseSubjectEditDetailsActivity.ConnectToDB connectToDB;
    SharedPreferences sharedPreferences;

    public class ConnectToDB extends AsyncTask<String,Void,Boolean> {

        Connection connection = null;
        String url = null;
        Statement stmt;
        ResultSet rs = null;
        String sql = "";

        public  void insertnewSubjectdetails(){

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try{
                        int coursecode=0,teachercode=0,semcode=0;
                        rs=stmt.executeQuery("select courseId from Course where courseName='"+subjectcoursename.getText().toString()+"'   ");
                        if(rs.next()){
                            coursecode = rs.getInt("courseId");
                        }

                        rs = stmt.executeQuery("select teacherId from Teacher where TeacherName='"+subjectteachername.getText().toString()+"' ");
                        if(rs.next()){
                            teachercode = rs.getInt("courseId");
                        }

                        rs = stmt.executeQuery("select semId from Semester where semName='"+subjectsemname.getText().toString()+"' ");
                        if(rs.next()){
                            semcode = rs.getInt("semId");
                        }


                        rs=stmt.executeQuery("select top 1 subjectId from Subject order by subjectId desc ");
                        if(rs.next()){
                            countid = rs.getInt("subjectId");
                        }
                        countid=countid+1;
                        stmt.executeQuery(" insert into Subject values("+countid+",'"+subjectname.getText().toString()+"',"+coursecode+","+teachercode+","+semcode+")");
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Intent adminCourseShowAllSubjectActivity = new Intent(getApplicationContext(), AdminCourseShowAllSubjectActivity.class);
                    startActivity(adminCourseShowAllSubjectActivity);
                }

            });
            thread.start();

        }

        public  void updateSubjectdetails(){

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try{
                        stmt.executeUpdate(" update Subject set fkcourseIdSubject=(select courseId from Course where courseName='"+subjectcoursename.getText().toString()+"'," +
                                "fkteacherIdSubject=(select teacherId from Teacher where teacherName='"+subjectteachername.getText().toString()+"')," +
                                "fksemIdSubject=(select semId from Semester where semName='"+subjectsemname.getText().toString()+"')" +
                                " where subjectId="+subjectid.getText().toString()+"  ");
                      }catch(Exception e){
                        e.printStackTrace();
                    }
                    Intent adminCourseShowAllSubjectActivity = new Intent(getApplicationContext(), AdminCourseShowAllSubjectActivity.class);
                    startActivity(adminCourseShowAllSubjectActivity);
                }

            });
            thread.start();

        }


        @Override
        protected Boolean doInBackground(String... sqlarr) {


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                connection = DriverManager.getConnection(url);
                stmt = connection.createStatement();

                setSubjectDetails();


                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//doInBackground

            public void setSubjectDetails(){

                    subjectid.setText(subjectcode);
                    subjectname.setText(subjectnames);
                    subjectteachername.setText(subjectTeachername);
                    subjectcoursename.setText(sharedPreferences.getString("adminCourseName","no value"));
                    subjectsemname.setText(sharedPreferences.getString("adminSemNo","no value"));

            }

    }//AsyncTask
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_subject_edit_details);
        subjectname =findViewById(R.id.SubjectNameShowAddEditText);
        subjectid =findViewById(R.id.SubjectIdShowAddEditText);
        subjectteachername =findViewById(R.id.SubjectTeacherNameShowAddEditText);
        subjectcoursename =findViewById(R.id.CourseNameShowAddEditText);
        subjectsemname =findViewById(R.id.SemNameShowAddEditText);
        savesubjectdetails=findViewById(R.id.savesubjectdetails);

            sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

            intent = getIntent();
            subjectcode=intent.getStringExtra("subjectid");
            subjectnames=intent.getStringExtra("subjectname");
            subjectTeachername=intent.getStringExtra("subjectteachername");
            check=intent.getStringExtra("check");

        connectToDB=new ConnectToDB();//obj of async class

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




        if(check!=null && check.equals("show")){
            subjectname.setEnabled(false);
            subjectid.setEnabled(false);
            subjectteachername.setEnabled(false);
            subjectcoursename.setEnabled(false);
            subjectsemname.setEnabled(false);
            savesubjectdetails.setVisibility(View.INVISIBLE);

        }

            if(check!=null && check.equals("edit")){
                subjectname.setEnabled(false);
                subjectid.setEnabled(false);
                savesubjectdetails.setTag("updatesubject");
            }

        if(check!=null && check.equals("addnew")){
            subjectcoursename.setEnabled(false);
            subjectsemname.setEnabled(false);
            savesubjectdetails.setTag("insertsubject");
        }

            savesubjectdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(savesubjectdetails.getTag().toString().equals("updatesubject")){
                        connectToDB.updateSubjectdetails();
                    }else{
                        connectToDB.insertnewSubjectdetails();
                    }
                }
            });

    }

    public void saveSubjectDetails(View view){
        Button button=(Button) findViewById(R.id.savesubjectdetails);
        Intent adminCourseShowAllSubjectActivity = new Intent(getApplicationContext(), AdminCourseShowAllSubjectActivity.class);
        startActivity(adminCourseShowAllSubjectActivity);
    }
}
