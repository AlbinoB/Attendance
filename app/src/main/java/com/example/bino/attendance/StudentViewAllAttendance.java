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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class StudentViewAllAttendance extends AppCompatActivity {
          String[][] studentsarr ;



    ListView studentlistView;

    SharedPreferences sharedPreferences;

    TextView currentUserTextView,studentRollNoTextView,studentCourseNameTextView;

    CustomAdapter customAdapter;

    int numberOfSubjects=0;


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


                getAndSetStudentName();

                getCourseName();

                getSubjectCount();

                getSubjectCodeAndNamesOfPaticularCourse();

                getStudentRoll();



                //getSem();

                //getYear();
                //percentage


                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//doInBackground

        public void getAndSetStudentName() {
            currentUserTextView.setText((String)sharedPreferences.getString("currentUserName","no  name"));

        }


        public void getCourseName() {

         //  sql="select courseName from Course where courseId=(select fkcourseIdStudent from Student where studentName='"+currentUserTextView.getText().toString()+"')";
            Log.i("data:::::::::::::", sql);
            try {
                rs=null;
                rs = stmt.executeQuery("select courseName from Course where courseId=(select fkcourseIdStudent from Student where studentName='"+currentUserTextView.getText().toString()+"')");


                if (rs.next())
                    studentCourseNameTextView.setText(rs.getString("courseName"));
                else {
                    Log.i("nothing", "nothing");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }//getCourseName

        public void getSubjectCount() {
            sql="select count(*) as countOfSubjects from Subject where fkcourseIdSubject in (select courseId from Course where courseName='"+studentCourseNameTextView.getText().toString()+"')";
            try {
            rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    numberOfSubjects = (rs.getInt("countOfSubjects"));
                    studentsarr =new String[numberOfSubjects][3];
                }
                else {
                    Log.i("nothing", "nothing");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }



        }//getSubjectCount








        public void getSubjectCodeAndNamesOfPaticularCourse() {


            try {
                sql = "select subjectId,subjectName from Subject,Course where fkcourseIdSubject =courseId and  courseName='" + studentCourseNameTextView.getText().toString() + "'";

                Log.i("data:::::::cccc::::::", sql);

                rs = null;
                rs = stmt.executeQuery(sql);
                int indexOfstudentarr = 0;
                while (rs.next()) {
                    Log.i("values from db:", Integer.toString(rs.getInt("subjectId")) + rs.getString("subjectName"));


                    // studentsarr= Arrays.copyOf(studentsarr, studentsarr.length + 1);
                    studentsarr[indexOfstudentarr][0] = Integer.toString(rs.getInt("subjectId"));
                    studentsarr[indexOfstudentarr][1] = rs.getString("subjectName");

                    studentsarr[indexOfstudentarr][2] = "";
                    indexOfstudentarr++;


                }

                studentlistView=(ListView)findViewById(R.id.listView);
                customAdapter=new CustomAdapter();
                studentlistView.setAdapter(customAdapter);

                studentlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent studentviewindividualattendance = new Intent(getApplicationContext(), StudentViewIndividualAttendance.class);
                        studentviewindividualattendance.putExtra("passScode",studentsarr[i][0]);
                        studentviewindividualattendance.putExtra("passSname",studentsarr[i][1]);
                        studentviewindividualattendance.putExtra("courseName",studentCourseNameTextView.getText().toString());
                        startActivity(studentviewindividualattendance);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


        }//getSubjectCodeAndNamesOfPaticularCourse


        public void getStudentRoll() {

            try {
                sql="select studentRollNo from Student where studentName='"+currentUserTextView.getText().toString()+"'";
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    studentRollNoTextView.setText(Integer.toString(rs.getInt("studentRollNo")));
                }
                else {
                    Log.i("nothing", "nothing");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }//getStudentRoll


    }//AsyncTask









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_all_attendance);

        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        currentUserTextView=(TextView)findViewById(R.id.currentUser);
        studentRollNoTextView=(TextView)findViewById(R.id.studentRollNo);
        studentCourseNameTextView=(TextView)findViewById(R.id.studentCourseName);





        ConnectToDB connectToDB=new ConnectToDB();//obj of async class

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
            view = getLayoutInflater().inflate(R.layout.customlayoutstudentallattendance, null);
            TextView textViewScode = (TextView) view.findViewById(R.id.sCode);
            TextView textViewSname = (TextView) view.findViewById(R.id.sName);
            TextView textViewSpercentage= (TextView) view.findViewById(R.id.sPercentage);
            textViewScode.setText(studentsarr[i][0]);
            textViewSname.setText(studentsarr[i][1]);
            textViewSpercentage.setText(studentsarr[i][2]);

            return view;
        }
    }

}
