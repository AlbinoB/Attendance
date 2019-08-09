package com.example.bino.attendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
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

    private Spinner SearchcourseSpiner, SearchyearSpiner, SearchsemesterSpiner, SearchsubjectSpiner;
    String[] coursenames;
    private static final String[] yearNos = {"Select Year","FY","SY","TY"};
    private static final String[] semesterNos = {"Select Semester","SEM 1","SEM 2","SEM 3","SEM 4","SEM 5","SEM 6","SEM 7","SEM 8","SEM 9"};
     String[] subjectNames;
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

                Log.i("sadasd","asdfaf");
                getTeacherName();
                getAndSetCourseName();
                getAndSetSubjectName();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }//doInBackground;
        public void getTeacherName(){
            currentTeacherTextView =((Integer)sharedPreferences.getInt("currentUserId",0));

            Log.i("sadascurrentTead",currentTeacherTextView+"");

        }

        public void getAndSetCourseName(){

            Log.i("sadasd","aspublic void getAndSetCourseName(){dfaf");
            try{
                int i=1;
                int noOfCourse=0;
                rs = stmt.executeQuery("select count(*) as countOfCourse from Course where courseId IN (select fkcourseIdTeacher from Teacher where teacherId  = '"+currentTeacherTextView+"') ");
                if(rs.next()){
                    noOfCourse  = (rs.getInt("countOfCourse"));

                }
                coursenames =new String[noOfCourse+1];
                coursenames[0] ="Select Course" ;
                rs = stmt.executeQuery("select courseName from Course where courseId IN (select fkcourseIdTeacher from Teacher where teacherId = '"+currentTeacherTextView+"') ");
                while(rs.next()){

                    coursenames[i++] = rs.getString("courseName");

                }
                ArrayAdapter<String> searchcourseAdapter = new ArrayAdapter<String>(TeacherSearchByActivity.this,android.R.layout.simple_spinner_dropdown_item,coursenames);
                searchcourseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SearchcourseSpiner.setAdapter(searchcourseAdapter);

            }catch(Exception e){
                e.printStackTrace();
            }
        }//getAndSetCourseName

        public void getAndSetSubjectName(){
            try{
                int i=1;
                int noOfSubject=0;
                rs = stmt.executeQuery("select count(*) as countOfSubject from Subject where fkcourseIdSubject IN (select fkcourseIdTeacher from Teacher where teacherId = '"+currentTeacherTextView+"') ");
                if(rs.next()){
                    noOfSubject  = (rs.getInt("countOfSubject"));
                    Log.i("no of subject",""+rs.getInt("countOfSubject"));

                }
                subjectNames =new String[noOfSubject+1];
                subjectNames[0] ="Select Subject" ;
                rs = stmt.executeQuery( "select subjectName from Subject  where fkcourseIdSubject IN (select fkcourseIdTeacher from Teacher where teacherId = '"+currentTeacherTextView+"') ");

                while(rs.next()){
                    Log.i(" subject name",""+rs.getString("subjectName"));

                    subjectNames[i++] = (rs.getString("subjectName"));

                }
                ArrayAdapter<String> searchsubjectAdapter = new ArrayAdapter<String>(TeacherSearchByActivity.this,android.R.layout.simple_spinner_dropdown_item,subjectNames);
                searchsubjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SearchsubjectSpiner.setAdapter(searchsubjectAdapter);

            }catch(Exception e){
                e.printStackTrace();
            }
        }//getAndSetSubjectName
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

        SearchcourseSpiner = (Spinner)findViewById(R.id.SearchcourseSpinner);
        SearchyearSpiner = (Spinner)findViewById(R.id.SearchyearSpinner);
        SearchsemesterSpiner = (Spinner)findViewById(R.id.SearchsemesterSpinner);
        SearchsubjectSpiner = (Spinner)findViewById(R.id.SearchsubjectSpinner);
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
                int month=Integer.parseInt(dataarr[1]);
                int year=Integer.parseInt(dataarr[0]);

                datePickerDialogStartDate=new DatePickerDialog(TeacherSearchByActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                startDate.setText((year+"/"+month+"/"+day));
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
                int month=Integer.parseInt(dataarr[1]);
                int year=Integer.parseInt(dataarr[0]);

                datePickerDialogEndDate=new DatePickerDialog(TeacherSearchByActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                endDate.setText((year+"/"+month+"/"+day));
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



        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(TeacherSearchByActivity.this,android.R.layout.simple_spinner_dropdown_item,yearNos);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SearchyearSpiner.setAdapter(yearAdapter);

        ArrayAdapter<String> semesterAdapter = new ArrayAdapter<String>(TeacherSearchByActivity.this,android.R.layout.simple_spinner_dropdown_item,semesterNos);
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SearchsemesterSpiner.setAdapter(semesterAdapter);

    }

    public boolean checkEmptyFields(){
        String error="";
        particularcoursename =SearchcourseSpiner.getSelectedItem().toString();
        particularyear = SearchyearSpiner.getSelectedItem().toString();
        particularsemester = SearchsemesterSpiner.getSelectedItem().toString();
        particularsubject = SearchsubjectSpiner.getSelectedItem().toString();
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
