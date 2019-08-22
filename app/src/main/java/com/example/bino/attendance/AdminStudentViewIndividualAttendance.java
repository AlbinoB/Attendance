package com.example.bino.attendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
    static String[][] studentsarr =
            {
                    {"21/07/2019", "true"},
                    {"21/07/2019", "true"},
                    {"21/07/2019", "true"},
                    {"21/07/2019", "false"},
                    {"21/07/2019", "true"},
                    {"21/07/2019", "false"},
                    {"21/07/2019", "true"},
                    {"21/07/2019", "false"},
                    {"21/07/2019", "true"},
                    {"21/07/2019", "false"},
                    {"21/07/2019", "false"},
                    {"21/07/2019", "true"}

            };

    EditText startDate;
    EditText endDate;
    DatePickerDialog datePickerDialogStartDate,datePickerDialogEndDate;

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void SaveDetailsofStudent(View view){
        Button savestudentdetails =(Button) findViewById(R.id.SaveIndividualStudentDetail);
        Intent adminstudenviewallattendance = new Intent(getApplicationContext(), AdminStudentViewAllAttendance.class);
        startActivity(adminstudenviewallattendance);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_view_individual_attendance);


        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        listView=(ListView)findViewById(R.id.listView);


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

                datePickerDialogStartDate=new DatePickerDialog(AdminStudentViewIndividualAttendance.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                startDate.setText((day+"/"+month+"/"+year));
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

                datePickerDialogEndDate=new DatePickerDialog(AdminStudentViewIndividualAttendance.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                endDate.setText((day+"/"+month+"/"+year));
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






        CustomAdapter customAdapter=new CustomAdapter();
        listView.setAdapter(customAdapter);

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
            TextView dateTextView=(TextView)view.findViewById(R.id.dateTextView);
            CheckBox presentabsent=(CheckBox)view.findViewById(R.id.presentabsentcheckBox);
            dateTextView.setText(studentsarr[i][0]);

            if(studentsarr[i][1].equalsIgnoreCase("true")){
                presentabsent.setChecked(true);
            }else{

                presentabsent.setChecked(false);
            }


            return  view;

        }
    }




}
