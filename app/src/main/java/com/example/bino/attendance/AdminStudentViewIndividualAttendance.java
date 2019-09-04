package com.example.bino.attendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminStudentViewIndividualAttendance extends AppCompatActivity {

    ListView listView;
    Intent previousIntent;
    SharedPreferences sharedPreferences;
    String currentyear;
    String currentsem;
    String currentcourse;
    String studentNameText;
    String studentRollnoText;
    String passScode;
    String passSname;
    String semStartDate=null,semEndDate=null;
    static String[][] studentsarr ;
    ConnectToDB connectToDB;
    Handler handler=new Handler();
    EditText startDate;
    EditText endDate;
    DatePickerDialog datePickerDialogStartDate,datePickerDialogEndDate;

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void SaveDetailsofStudent(View view){
        Intent adminstudenviewallattendance = new Intent(getApplicationContext(), AdminStudentViewAllAttendance.class);
        finish();
        startActivity(adminstudenviewallattendance);
    }

    public void applyFilter(View view){

        semStartDate=startDate.getText().toString();
        semEndDate=endDate.getText().toString();

        if(!semStartDate.equals("") &&!semEndDate.equals(""))
        connectToDB.getNumberOfdays();
        else {
            Toast.makeText(this, "Please select Start and End dates", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_view_individual_attendance);


        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        listView=(ListView)findViewById(R.id.listView);

        startDate=(EditText)findViewById(R.id.StartDate);
        endDate=(EditText)findViewById(R.id.EndDate);
        startDate.setInputType(InputType.TYPE_NULL);//disable softkey board
        endDate.setInputType(InputType.TYPE_NULL);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempdate=getDateTime();
                String[] dataarr=tempdate.split("/");
                int day=Integer.parseInt(dataarr[2]);
                int month=Integer.parseInt(dataarr[1])-1;
                int year=Integer.parseInt(dataarr[0]);

                datePickerDialogStartDate=new DatePickerDialog(AdminStudentViewIndividualAttendance.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                startDate.setText((year+"-"+(month+1)+"-"+day));
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
                int day=Integer.parseInt(dataarr[2]);
                int month=Integer.parseInt(dataarr[1])-1;
                int year=Integer.parseInt(dataarr[0]);

                datePickerDialogEndDate=new DatePickerDialog(AdminStudentViewIndividualAttendance.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                endDate.setText((year+"-"+(month+1)+"-"+day));
                            }
                        },year,month,day);
                datePickerDialogEndDate.show();
            }
        });

        previousIntent=getIntent();

        TextView studentName,studentRollNo,semName,currentYear,courseName,subjectName;
        studentName=(TextView)findViewById(R.id.studentName);
        studentRollNo=(TextView)findViewById(R.id.studentRollNo);
        semName=(TextView)findViewById(R.id.semName);
        currentYear=(TextView)findViewById(R.id.semYear);
        courseName=(TextView)findViewById(R.id.courseName);
        subjectName=(TextView)findViewById(R.id.subjectName);

        previousIntent=getIntent();

        currentyear =((String)sharedPreferences.getString("currentYearNo","no date"));
        currentsem =((String)sharedPreferences.getString("currentSemester","no date"));
        currentcourse =((String)sharedPreferences.getString("currentCourseName","no date"));
        studentNameText=((String)sharedPreferences.getString("passStudentName","no date"));
        studentRollnoText=((String)sharedPreferences.getString("passStudentRoll","no date"));
        passScode=previousIntent.getStringExtra("passScode");
        passSname=previousIntent.getStringExtra("passSname");

        studentName.setText(studentNameText);
        studentRollNo.setText(studentRollnoText);
        semName.setText(currentsem);
        currentYear.setText(currentyear);
        courseName.setText(currentcourse);
        subjectName.setText(passSname);

        connectToDB=new ConnectToDB();//obj of async class

        String[] sql={
        };
        try {
            if(connectToDB.execute(sql).get()){
                {
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

            view = getLayoutInflater().inflate(R.layout.customlayoutadminstudentviewindividualattendance, null);
            final TextView dateTextView=(TextView)view.findViewById(R.id.dateTextView);
            final TextView timeTextView=(TextView)view.findViewById(R.id.timeTextView);
            final CheckBox presentabsent=(CheckBox)view.findViewById(R.id.presentabsentcheckBox);
            final Button editAttendanceButton=(Button)view.findViewById(R.id.editAttendance);
            editAttendanceButton.setTag(""+i);
            editAttendanceButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view1) {

                    presentabsent.setEnabled(true);

                    if(presentabsent.isEnabled()){
                        editAttendanceButton.setBackground(null);
                        editAttendanceButton.setBackgroundColor(Color.GREEN);
                        editAttendanceButton.setText("Save");

                        if(!presentabsent.getTag().toString().equals(""+presentabsent.isChecked())){
                            presentabsent.setTag(""+presentabsent.isChecked());
                            presentabsent.setChecked(presentabsent.isChecked());
                            int attendanceToBeChangedAtindex=Integer.parseInt(editAttendanceButton.getTag().toString());

                            if(presentabsent.isChecked()){
                                studentsarr[attendanceToBeChangedAtindex][1]="P";
                            }
                            else{
                                studentsarr[attendanceToBeChangedAtindex][1]="A";
                            }

                            Toast.makeText(AdminStudentViewIndividualAttendance.this, "Saved to database", Toast.LENGTH_SHORT).show();
                            presentabsent.setEnabled(false);
                            editAttendanceButton.setBackgroundColor(Color.rgb(105,105,105));
                            editAttendanceButton.setBackground(getResources().getDrawable(R.drawable.editicon));
                            editAttendanceButton.setText("");
                            connectToDB.updateAttendance(dateTextView.getText().toString(),timeTextView.getText().toString(),studentsarr[attendanceToBeChangedAtindex][1]);
                        }
                    }
                }
            });

            dateTextView.setText(studentsarr[i][0]);
            timeTextView.setText(studentsarr[i][2]);

            if(studentsarr[i][1].equalsIgnoreCase("P")){
                presentabsent.setChecked(true);
                presentabsent.setTag("true");
                presentabsent.setEnabled(false);
            }else{
                presentabsent.setEnabled(false);
                presentabsent.setTag("false");
                presentabsent.setChecked(false);
            }
            return  view;
        }
    }

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

                getStartAndEndDate();
                getNumberOfdays();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//doInBackground

        public void getStartAndEndDate(){

            sql="select semStartDate,semEndDate from Semester where semId=(select fksemIdStudent from Student where studentErpNo=(select studentErpNo from Student where studentName='"+studentNameText+"'))";
            try {
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    semStartDate = rs.getString("semStartDate");
                    semEndDate = rs.getString("semEndDate");
                }
            } catch (Exception e) {
                Log.i("nothing", "nothing");
                e.printStackTrace();
            }
        }

        public void getNumberOfdays(){

            Thread threadGetNumberOfdays=new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    sql="select fksubjectId,count(*) as totalLectures from Attendance where takenDate between '"+semStartDate+"' and '"+semEndDate+"' and fkstudentErpNo=(select studentErpNo from Student where studentErpNo=(select studentErpNo from Student where studentName='"+studentNameText+"')) and fksubjectId=(select subjectId from Subject where subjectId='"+passScode+"') group by fksubjectId";

                    try {
                        rs = stmt.executeQuery(sql);
                        if (rs.next()) {
                            int numberOfdays = (rs.getInt("totalLectures"));
                            studentsarr=new String[numberOfdays][3];
                            getDatesPresentAbsent();//if days are fetched then call else
                        }
                        else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AdminStudentViewIndividualAttendance.this, "No record found", Toast.LENGTH_SHORT).show();
                                           studentsarr=new String[0][3];
                                            listView=(ListView)findViewById(R.id.listView);
                                            CustomAdapter customAdapter=new CustomAdapter();
                                            listView.setAdapter(customAdapter);
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            threadGetNumberOfdays.start();
        }

        public void getDatesPresentAbsent(){

            sql="select takenDate,presentabsent,convert(varchar, takenTime, 8) as takenDateFormatted  from Attendance where fksubjectId="+passScode+" and fkstudentErpNo=(select studentErpNo from Student where studentErpNo=(select studentErpNo from Student where studentName='"+studentNameText+"')) and takenDate between '"+semStartDate+"' and '"+semEndDate+"'";

            try {
                rs = stmt.executeQuery(sql);
                int i=0;
                while(rs.next()) {
                    studentsarr[i][0]=rs.getDate("takenDate")+"";
                    studentsarr[i][1]=rs.getString("presentabsent");
                    studentsarr[i][2]=rs.getString("takenDateFormatted");
                    i++;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView=(ListView)findViewById(R.id.listView);
                        CustomAdapter customAdapter=new CustomAdapter();
                        listView.setAdapter(customAdapter);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public  void updateAttendance(final String updateTakenDate, final String updateTakenTime, final String absentOrPresent){
            Thread updateAttendaceThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    sql="update Attendance set presentabsent='"+absentOrPresent+"' where fksubjectId="+passScode+" and fkstudentErpNo=(select studentErpNo from Student where studentErpNo=(select studentErpNo from Student where studentName='"+studentNameText+"')) and takenDate='"+updateTakenDate+"' and takenTime='"+updateTakenTime+"'";
                    try {
                        stmt.executeQuery(sql);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            updateAttendaceThread.start();

        }

    }

}
