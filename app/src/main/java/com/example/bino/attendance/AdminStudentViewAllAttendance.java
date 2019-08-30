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
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminStudentViewAllAttendance extends AppCompatActivity {


    static String[][] studentsarr ;
    ListView listView;
    Intent previousIntent;
    SharedPreferences sharedPreferences;
    String currentyear;
    String currentsem;
    String currentcourse;
    String studentNameText;
    String studentRollnoText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_view_all_attendance);

        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        listView=(ListView)findViewById(R.id.listView);

        TextView studentName,studentRollNo,semName,currentYear,courseName;
        studentName=(TextView)findViewById(R.id.studentName);
        studentRollNo=(TextView)findViewById(R.id.studentRollNo);
        semName=(TextView)findViewById(R.id.semName);
        currentYear=(TextView)findViewById(R.id.semYear);
        courseName=(TextView)findViewById(R.id.courseName);
        previousIntent=getIntent();

        currentyear =((String)sharedPreferences.getString("currentYearNo","no date"));
        currentsem =((String)sharedPreferences.getString("currentSemester","no date"));
        currentcourse =((String)sharedPreferences.getString("currentCourseName","no date"));
        studentNameText=((String)sharedPreferences.getString("passStudentName","no date"));
        studentRollnoText=((String)sharedPreferences.getString("passStudentRoll","no date"));


        studentName.setText(studentNameText);
        studentRollNo.setText(studentRollnoText);
        semName.setText(currentsem);
        currentYear.setText(currentyear);
        courseName.setText(currentcourse);


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



        CustomAdapter customAdapter=new CustomAdapter();
        listView.setAdapter(customAdapter);



    }



    public class ConnectToDB extends AsyncTask<String,Void,Boolean> {

        int numberOfSubjects=0;
        Connection connection = null;
        String url = null;
        Statement stmt,stmt2;
        ResultSet rs = null,rs2=null;
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




                getSubjectCount();


                getSubjectCodeAndNamesOfPaticularCourse();


                //percentage


                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//doInBackground



        public void getSubjectCount() {
            sql="select count(*) as countOfSubjects from Subject where fkcourseIdSubject in (select courseId from Course where courseName='"+currentcourse+"')and fksemIdSubject=(select fksemIdStudent from Student where studentName='"+studentNameText+"')";
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

            int totallecture=0;
            String semStartDate=null,semEndDate=null;
            try {

                //get start and end date from semester table
                sql="select semStartDate,semEndDate from Semester where semId=(select fksemIdStudent from Student where studentErpNo="+(Integer)sharedPreferences.getInt("currentUserErpNo",0)+")";
                rs = stmt.executeQuery(sql);
                if(rs.next()){
                    semStartDate=rs.getString("semStartDate");
                    semEndDate=rs.getString("semEndDate");
                }


                sql = "select subjectId,subjectName from Subject,Course where fkcourseIdSubject =courseId and  courseName='" + currentcourse + "' and fksemIdSubject=(select fksemIdStudent from Student where studentName='"+studentNameText+"')";

                Log.i("data:::::::cccc::::::", sql);

                rs = null;
                rs = stmt.executeQuery(sql);
                int indexOfstudentarr = 0;

                while (rs.next()) {
                    Log.i("values from db:", Integer.toString(rs.getInt("subjectId")) + rs.getString("subjectName"));


                    // studentsarr= Arrays.copyOf(studentsarr, studentsarr.length + 1);
                    studentsarr[indexOfstudentarr][0] = Integer.toString(rs.getInt("subjectId"));
                    studentsarr[indexOfstudentarr][1] = rs.getString("subjectName");

                    String sqltemp="select count(*) as totallecture from Attendance where takenDate between '"+semStartDate+"' and '"+semEndDate+"' and fksubjectId=(select subjectId from Subject where subjectName='"+rs.getString("subjectName")+"') and fkstudentErpNo=(select studentErpNo from Student where studentRollNo='"+studentRollnoText+"')";
                    rs2 = stmt2.executeQuery(sqltemp);
                    if(rs2.next()){
                        totallecture =(Integer)rs2.getInt("totallecture");
                    }

                    int totalpresent=0;
                    sql="select count(*) as totalpresent from Attendance where( (takenDate between '"+semStartDate+"' and '"+semEndDate+"') and( fkstudentErpNo=(select studentErpNo from Student where studentRollNo='"+studentRollnoText+"'))and( presentabsent='P') and (fksubjectId=(select subjectId from Subject where subjectName='"+rs.getString("subjectName")+"')))";
                    Log.i("totalpre",sql);
                    rs2 = stmt2.executeQuery(sql);
                    if(rs2.next()){
                        totalpresent =(Integer)rs2.getInt("totalpresent");

                    }
                    float percent=0;
                    Log.i("total lecture ",""+totallecture);
                    Log.i("total present ",""+totalpresent);
                    if(totallecture!=0){
                        percent =(100*totalpresent)/totallecture;
                        studentsarr[indexOfstudentarr][2]=Float.toString(percent);

                        indexOfstudentarr++;
                    }else
                    {

                        studentsarr[indexOfstudentarr][2]="N/A";
                        indexOfstudentarr++;

                    }






                }





            } catch (Exception e) {
                e.printStackTrace();
            }


        }//getSubjectCodeAndNamesOfPaticularCourse


    }//AsyncTask



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
            view = getLayoutInflater().inflate(R.layout.customlayoutadminstudentviewallattendance, null);
            TextView textViewScode = (TextView) view.findViewById(R.id.sCode);
            TextView textViewSname = (TextView) view.findViewById(R.id.studentRollNo);
            TextView textViewSpercentage= (TextView) view.findViewById(R.id.sPercentage);
            textViewScode.setText(studentsarr[i][0]);
            textViewSname.setText(studentsarr[i][1]);
            textViewSpercentage.setText(studentsarr[i][2]);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textViewScode = (TextView) view.findViewById(R.id.sCode);
                    TextView textViewSname = (TextView) view.findViewById(R.id.studentRollNo);

                    Intent adminstudentviewindividualattendance = new Intent(getApplicationContext(), AdminStudentViewIndividualAttendance.class);
                    adminstudentviewindividualattendance.putExtra("passScode",textViewScode.getText().toString());
                    adminstudentviewindividualattendance.putExtra("passSname",textViewSname.getText().toString());

                    startActivity(adminstudentviewindividualattendance);

                }
            });

            return view;

        }
    }
}
