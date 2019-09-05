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
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminStudentSeachResultActivity extends AppCompatActivity {

        String[][] studentsarr ;
        ListView adminstudentlistView;
        String currentyear;
        String currentsem;
        String currentcourse;
        SharedPreferences sharedPreferences;
        CustomAdapter customAdapter;
        TextView currentSem,currentCourse;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_student_search_result);

            sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);
            currentSem=findViewById(R.id.currentSem);
            currentCourse=findViewById(R.id.currentCourse);
            adminstudentlistView=(ListView)findViewById(R.id.displayStudents);

            ConnectToDB connectToDB = new ConnectToDB();
            String[] sql={

            };

            try {
                if(connectToDB.execute(sql).get()){
                    customAdapter=new CustomAdapter();
                    adminstudentlistView.setAdapter(customAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void addStudent(View view){
            Intent adminStudentEditAddDetails = new Intent(getApplicationContext(), AdminStudentEditAddDetails.class);
            startActivity(adminStudentEditAddDetails);
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
                view = getLayoutInflater().inflate(R.layout.customlayoutadminstudentsearchresultdisplaystudents, null);
                TextView textViewSsrno = (TextView) view.findViewById(R.id.srno);
                TextView textViewSrollno = (TextView) view.findViewById(R.id.rollno);
                TextView textViewSname = (TextView) view.findViewById(R.id.name);
                TextView textViewSpercentage= (TextView) view.findViewById(R.id.percentage);
                Button admineditstudent = (Button) view.findViewById(R.id.editAttendance1);

                textViewSsrno.setText(studentsarr[i][0]);
                textViewSrollno.setText(studentsarr[i][1]);
                textViewSname.setText(studentsarr[i][2]);
                textViewSpercentage.setText(studentsarr[i][3]);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view1) {

                        TextView textViewSrollno = (TextView) view1.findViewById(R.id.rollno);
                        TextView textViewSname = (TextView) view1.findViewById(R.id.name);
                            /*this code is for getting all the value which is present in
                             list view just we have to use this ids*/
                        Intent adminstudentviewallattendance = new Intent(getApplicationContext(), AdminStudentViewAllAttendance.class);
                        sharedPreferences.edit().putString("passStudentRoll",textViewSrollno.getText().toString()).apply();
                        sharedPreferences.edit().putString("passStudentName",textViewSname.getText().toString()).apply();

                        startActivity(adminstudentviewallattendance);
                    }
                });
                admineditstudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent adminstudenteditadddetails = new Intent(getApplicationContext(), AdminStudentEditAddDetails.class);
                        startActivity(adminstudenteditadddetails);
                    }
                });

                return view;
            }
        }


        public class ConnectToDB extends AsyncTask<String,Void,Boolean> {
            Connection connection = null;
            String url = null;
            Statement stmt,stmt2;
            ResultSet rs,resultSet = null;
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
                    stmt2 = connection.createStatement();

                    getandsetcurrentdetails();
                    getandsetrollnonamepercent();

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

            }//doInBackground;


            public void getandsetcurrentdetails(){

                currentyear =((String)sharedPreferences.getString("currentYearNo","no date"));
                currentsem =((String)sharedPreferences.getString("currentSemester","no date"));
                currentcourse =((String)sharedPreferences.getString("currentCourseName","no date"));

                currentSem.setText(currentsem);
                currentCourse.setText(currentcourse);
            }

            public void  getandsetrollnonamepercent(){
                try{
                    int i=0;
                    int totalstudent=0;
                    rs = stmt.executeQuery("select count(*) as totlastudent FROM Student,Semester where semId=fksemIdStudent and semName='"+currentsem+"'");
                    if(rs.next()){
                        totalstudent =rs.getInt("totlastudent");
                    }
                    studentsarr = new String[totalstudent][4];

                    rs = stmt.executeQuery("select studentRollNo,studentName FROM Student,Semester where semId=fksemIdStudent and semName='"+currentsem+"' order by studentRollNo");
                    while(rs.next()){
                        String studentRollNo =rs.getString("studentRollNo");
                        studentsarr[i][0]=Integer.toString(i+1);
                        studentsarr[i][1]=rs.getString("studentRollNo");
                        studentsarr[i][2]=rs.getString("studentName");
                        studentsarr[i][3]="N/A";
                            i++;
                    }

                    adminstudentlistView.setAdapter(customAdapter);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }//AsyncTask
    }

