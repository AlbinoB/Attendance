package com.example.bino.attendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TeacherSearchByActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    int currentTeacherTextView;
    String particularcoursename;
    String particularyear;
    String particularsemester;
    String particularsubject;
    String particularstartdate;
    String particularenddate;
    Handler handler =new Handler();
    private Spinner courseSpiner,yearSpiner,semesterSpiner,subjectSpiner;
    String[] coursename ;
    String[] yearNo ;
    String[] semesterNo ;
    String[] subjectName ;
    int noOfSubject,noOfSemesters,noOfYears,noOfCourse=0;
    //kk

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


                getTeacherName();
                getAndSetCourseName();
                getYear();
                setYears();
                getSemesters();
                setSemester();
                getSubjectName();
                setSubjectName();

                courseSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        Log.i("course name", "" + courseSpiner.getSelectedItem());
                        Log.i("clicked", "on item selected");
                        // setYears();
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                getYear();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setYears();
                                        Log.i("set year", "on item selected");
                                    }
                                });
                            }
                        });

                        thread.start();
                        // setYears();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });




                yearSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                        Log.i("year no", "" + yearSpiner.getSelectedItem());
                        //  getSemesters();
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                getSemesters();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setSemester();
                                    }
                                });
                            }
                        });

                        thread.start();
                        // setYears();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });





                semesterSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        Log.i("course name", "" + semesterSpiner.getSelectedItem());
                        Log.i("clicked", "on item selected");
                        //   setYears();
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                getSubjectName();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setSubjectName();
                                    }
                                });
                            }
                        });

                        thread.start();
                        // setYears();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }//doInBackground;
        public void getTeacherName(){
            currentTeacherTextView =((Integer)sharedPreferences.getInt("currentUserId",0));
        }

        public void getAndSetCourseName(){
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                connection = DriverManager.getConnection(url);
                stmt = connection.createStatement();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try{
                int i=1;
                noOfCourse=0;
                rs = stmt.executeQuery("select count(courseName) as countOfCourse from Course where courseId in (select fkcourseIdSubject from Subject where fkteacherIdSubject=(select teacherId from Teacher where teacherId='"+(Integer)sharedPreferences.getInt("currentUserId",0)+"'))");

                if(rs.next()){
                    noOfCourse  = (rs.getInt("countOfCourse"));
                }

                coursename =new String[noOfCourse+1];
                coursename[0]="Select Course";
                rs = stmt.executeQuery("select courseName from Course where courseId in (select fkcourseIdSubject from Subject where fkteacherIdSubject=(select teacherId from Teacher where teacherId='"+(Integer)sharedPreferences.getInt("currentUserId",0)+"'))");

                while(rs.next()) {
                    coursename[i++] = rs.getString("courseName");
                }

                ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(TeacherSearchByActivity.this,android.R.layout.simple_spinner_dropdown_item,coursename);
                courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                courseSpiner.setAdapter(courseAdapter);



            }catch(Exception e){
                e.printStackTrace();
            }
        }//getAndSetCourseName

        public void getYear(){
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                connection = DriverManager.getConnection(url);
                stmt = connection.createStatement();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Log.i("Years:","year called");
                int i = 1;
                noOfYears = 0;

                rs = stmt.executeQuery("select count(*) as noOfYears from CourseYears where fkcourseIdCourseYears=(select courseId from Course where courseName='"+courseSpiner.getSelectedItem()+"')");
                if (rs.next()) {
                    noOfYears = (rs.getInt("noOfYears"));

                }
                Log.i("noOfYears:",noOfYears+"");
                yearNo=new String[noOfYears+1];
                yearNo[0]="Select Year";
                rs = stmt.executeQuery("select courseYears from CourseYears where fkcourseIdCourseYears=(select courseId from Course where courseName='"+courseSpiner.getSelectedItem()+"')");
                while (rs.next()) {
                    Log.i("courseYears:",rs.getString("courseYears"));
                    yearNo[i++] = rs.getString("courseYears");

                }

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }//getYear

        public void setYears(){

            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(TeacherSearchByActivity.this,android.R.layout.simple_spinner_dropdown_item,yearNo);
            yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            yearSpiner.setAdapter(yearAdapter);
        }//setYears


        public void getSemesters(){
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                connection = DriverManager.getConnection(url);
                stmt = connection.createStatement();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                int i = 1;
                noOfSemesters = 0;

                rs = stmt.executeQuery("select Count(*) as noOfSemesters from Semester where semYear='"+yearSpiner.getSelectedItem()+"'");
                if (rs.next()) {
                    noOfSemesters = (rs.getInt("noOfSemesters"));

                }
                Log.i("noOfsemester:",noOfSemesters+"");
                semesterNo=new String[noOfSemesters+1];
                semesterNo[0]="Select Semester";
                rs = stmt.executeQuery("select semName from Semester where semYear='"+yearSpiner.getSelectedItem()+"'");
                while (rs.next()) {
                    Log.i("semName:",rs.getString("semName"));
                    semesterNo[i++] = rs.getString("semName");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }//getSemesters

        public void setSemester(){
            ArrayAdapter<String> semesterAdapter = new ArrayAdapter<String>(TeacherSearchByActivity.this,android.R.layout.simple_spinner_dropdown_item,semesterNo);
            semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            semesterSpiner.setAdapter(semesterAdapter);
        }//setSemester


        public void getSubjectName(){
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                connection = DriverManager.getConnection(url);
                stmt = connection.createStatement();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try{
                int i=1;
                noOfSubject=0;
                rs = stmt.executeQuery("select count(subjectName) as countOfSubject from Subject where fkteacherIdSubject="+(Integer)sharedPreferences.getInt("currentUserId",0)+" and fkcourseIdSubject=(select courseId from Course where courseName='"+courseSpiner.getSelectedItem()+"') and  fksemIdSubject=(select semId from Semester where semName='"+semesterSpiner.getSelectedItem()+"')");

                if(rs.next()){
                    noOfSubject  = (rs.getInt("countOfSubject"));
                    Log.i("no of subject",""+rs.getInt("countOfSubject"));

                }
                subjectName =new String[noOfSubject+1];
                subjectName[0]="Select Subject";
                rs = stmt.executeQuery( "select subjectName from Subject where fkteacherIdSubject="+(Integer)sharedPreferences.getInt("currentUserId",0)+" and fkcourseIdSubject=(select courseId from Course where courseName='"+courseSpiner.getSelectedItem()+"') and  fksemIdSubject=(select semId from Semester where semName='"+semesterSpiner.getSelectedItem()+"')");

                while(rs.next()){
                    Log.i(" subject name",""+rs.getString("subjectName"));

                    subjectName[i++] = (rs.getString("subjectName"));

                }


            }catch(Exception e){
                e.printStackTrace();
            }

        }//getAndSetSubjectName

        public void setSubjectName(){
            ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(TeacherSearchByActivity.this,android.R.layout.simple_spinner_dropdown_item,subjectName);
            subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subjectSpiner.setAdapter(subjectAdapter);
        }//setSubjectName

    }//AsyncTask

        public void SearchAttendance(View view){

            if(checkEmptyFields()) {
                Log.i("check activity",""+checkEmptyFields());
                Log.i("check activity",""+particularstartdate);
                Log.i("check activity",""+particularenddate);
                Button teachersearchbutton = (Button) findViewById(R.id.TeacherSearchButton);
               sharedPreferences.edit().putString("currentstartdate",particularstartdate).apply();
                sharedPreferences.edit().putString("currentenddate",particularenddate).apply();
                sharedPreferences.edit().putString("currentcoursename",particularcoursename).apply();
                sharedPreferences.edit().putString("currentsubjectname",particularsubject).apply();
                sharedPreferences.edit().putString("currentyear",particularyear).apply();
                sharedPreferences.edit().putString("currentsem",particularsemester).apply();
                Intent teachersearchresult = new Intent(getApplicationContext(), TeacherSearchResult.class);
                startActivity(teachersearchresult);
            }
        }

    EditText startDate;
    EditText endDate;
    DatePickerDialog datePickerDialogStartDate,datePickerDialogEndDate;
    Calendar calendar;

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_search_by);

        courseSpiner = (Spinner)findViewById(R.id.SearchcourseSpinner);
        yearSpiner = (Spinner)findViewById(R.id.SearchyearSpinner);
        semesterSpiner = (Spinner)findViewById(R.id.SearchsemesterSpinner);
        subjectSpiner = (Spinner)findViewById(R.id.SearchsubjectSpinner);
        startDate=(EditText)findViewById(R.id.StartDate);
        endDate=(EditText)findViewById(R.id.EndDate);
        //startDate.setText(getDateTime());
        startDate.setInputType(InputType.TYPE_NULL);//disable softkey board
        endDate.setInputType(InputType.TYPE_NULL);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempdate=getDateTime();
                String[] dataarr=tempdate.split("/");
                Log.i("date",getDateTime());
                int day=Integer.parseInt(dataarr[2]);
                int month=Integer.parseInt(dataarr[1])-1;
                int year=Integer.parseInt(dataarr[0]);

                datePickerDialogStartDate=new DatePickerDialog(TeacherSearchByActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                startDate.setText((year+"/"+(month+1)+"/"+day));
                            }
                        },year,month,day);
                datePickerDialogStartDate.show();
            }
            });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempdate=getDateTime();
                String[] dataarr=tempdate.split("/");
                Log.i("date",getDateTime());
                int day=Integer.parseInt(dataarr[2]);
                int month=Integer.parseInt(dataarr[1])-1;
                int year=Integer.parseInt(dataarr[0]);

                datePickerDialogEndDate=new DatePickerDialog(TeacherSearchByActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                endDate.setText((year+"/"+(month+1)+"/"+day));
                            }
                        },year,month,day);
                datePickerDialogEndDate.show();
            }
        });

        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        TeacherSearchByActivity.ConnectToDB connectToDB = new ConnectToDB();


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

    public boolean checkEmptyFields(){
        String error="";
        particularcoursename =courseSpiner.getSelectedItem().toString();
        particularyear = yearSpiner.getSelectedItem().toString();
        particularsemester = semesterSpiner.getSelectedItem().toString();
        particularsubject = subjectSpiner.getSelectedItem().toString();
        particularstartdate=startDate.getText().toString();
        particularenddate=endDate.getText().toString();

        if( particularcoursename.equals("Select Course")){
            error="Please Select Course!!!";

        }else if( particularyear.equals("Select Year")){
            error="Please Select Year!!!";
        }else if( particularsemester.equals("Select Semester")){
            error="Please Select Semester!!!";
        }else if( particularsubject.equals("Select Subject")){
            error="Please Select Subject!!!";
        }else if(particularstartdate.equals("")){
            error="Please Select Start Date!!!";
        }else if(particularenddate.equals("")){
            error="Please Select End Date!!!";
        }

        if(error!="")
        {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            return false;
        }
        else
        {
            return true;
        }

    }

}
