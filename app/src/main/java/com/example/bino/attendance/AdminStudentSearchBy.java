package com.example.bino.attendance;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminStudentSearchBy extends AppCompatActivity {

    static Spinner SearchcourseSpiner, SearchyearSpiner, SearchsemesterSpiner;
     static String[] coursenames, yearNos ,semesterNos ;
    static ArrayAdapter<String> courseAdapter,yearAdapter,semesterAdapter;

    public void  SearchStudent(View view){
        Button searchbutton = (Button)findViewById(R.id.search);
        Intent adminstudentsearchresultactivity = new Intent(getApplicationContext(), AdminStudentSeachResultActivity.class);
        startActivity(adminstudentsearchresultactivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_search_by);



        ConnectToDB connectToDB=new ConnectToDB();
        String[] sql={""};
        connectToDB.execute(sql);

    }



    public class ConnectToDB extends AsyncTask<String,Void,Boolean> {

        Connection connection=null;
        String url=null;
        Statement stmt=null;
        ResultSet rs=null;
        String sql="";


        @Override
        public Boolean doInBackground(String... sqlRecieved) {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);




            SearchyearSpiner.setEnabled(false);
            SearchsemesterSpiner.setEnabled(false);


            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                connection= DriverManager.getConnection(url);
                stmt = connection.createStatement();
                rs=null;
                sql=sqlRecieved[0];
                getCourseCount();


                return  true;

            }
            catch (Exception e){
                e.printStackTrace();
                return  false;
            }


        }

        public void getCourseCount(){


            int numberOfCourses=0;
            sql="select count(*) as numberOfCourses from Course";
            try {
                rs = stmt.executeQuery(sql);
                if(rs.next()){
                    numberOfCourses=rs.getInt("numberOfCourses");
                    coursenames=new String[numberOfCourses] ;
                    courseAdapter = new ArrayAdapter<String>(AdminStudentSearchBy.this, android.R.layout.simple_spinner_dropdown_item, coursenames);
                    courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    getCourses();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public  void getCourses(){
            sql="select courseName from Course";
            try {
                rs = stmt.executeQuery(sql);
                int i=0;
                while(rs.next()){
                    coursenames[i]=rs.getString("courseName");
                    Log.i("ssss",coursenames[i]);
                    i++;
                }
                getNumberOfYear();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public void getNumberOfYear(){
            int numberOfYears=0;
            sql="select count(*) as numberOfYears from CourseYears where fkcourseIdCourseYears=(select courseId from Course where courseName='"+SearchcourseSpiner.getSelectedItem()+"')";
            try {
                rs = stmt.executeQuery(sql);
                if(rs.next()){
                    numberOfYears=rs.getInt("numberOfYears");
                    yearNos=new String[numberOfYears] ;
                    getYears();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public  void getYears(){
            sql="select courseYears from CourseYears where fkcourseIdCourseYears=(select courseId from Course where courseName='"+SearchcourseSpiner.getSelectedItem()+"')";
            try {
                rs = stmt.executeQuery(sql);
                int i=0;
                while(rs.next()){
                    yearNos[i]=rs.getString("courseYears");
                    i++;
                }

                yearAdapter = new ArrayAdapter<String>(AdminStudentSearchBy.this, android.R.layout.simple_spinner_dropdown_item, yearNos);
                yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                SearchyearSpiner.setSelection(0);
                getCountSemestersCount();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }


        public void getCountSemestersCount(){
            int numberOfSemesters=0;
            sql="select count(*) as numberOfSemesters from Semester where semName=('"+SearchyearSpiner.getSelectedItem()+"')";
            Log.i("getCountSemestersCount",sql);
            try {
                rs = stmt.executeQuery(sql);
                if(rs.next()){
                    numberOfSemesters=rs.getInt("numberOfYears");
                    yearNos=new String[numberOfSemesters] ;
                    SearchyearSpiner.setSelection(0);
                    getSemesters();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public  void getSemesters(){
            sql="select semName from Semester where semYear='("+SearchyearSpiner.getSelectedItem()+"')";
            Log.i("getSemesters",sql);

            try {
                rs = stmt.executeQuery(sql);
                int i=0;
                while(rs.next()){
                    yearNos[i]=rs.getString("courseYears");
                    i++;
                }
                semesterAdapter = new ArrayAdapter<String>(AdminStudentSearchBy.this, android.R.layout.simple_spinner_dropdown_item, semesterNos);
                semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SearchsemesterSpiner.setEnabled(true);

                SearchcourseSpiner.setAdapter(courseAdapter);
                SearchyearSpiner.setAdapter(yearAdapter);
                SearchsemesterSpiner.setAdapter(semesterAdapter);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }





    }
}
