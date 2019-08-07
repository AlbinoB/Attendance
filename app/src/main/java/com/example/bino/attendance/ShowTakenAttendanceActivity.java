package com.example.bino.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowTakenAttendanceActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_taken_attendance);

        TextView ShowSubjectTextView,ShowDateTextView,ShowTotalTextView,ShowPresentTextView,ShowAbsentTextView;

        ShowSubjectTextView=(TextView)findViewById(R.id.ShowSubjectTextView);
        ShowDateTextView=(TextView)findViewById(R.id.ShowDateTextView);
        ShowTotalTextView=(TextView)findViewById(R.id.ShowTotalTextView);
        ShowPresentTextView=(TextView)findViewById(R.id.ShowPresentTextView);
        ShowAbsentTextView=(TextView)findViewById(R.id.ShowAbsentTextView);
        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);
        String stringFullArray=(String)sharedPreferences.getString("stringFullArray","no value");
        Log.i("nextfullarr",stringFullArray);
        int noOfStudent=sharedPreferences.getInt("noOfStudent",0);
        String[][] studentNameRollno=new String[noOfStudent][9];

        studentNameRollno=to2dim(stringFullArray,";"," ");
        int presentCount=0,absentCount=0;
        for (int f = 0; f < studentNameRollno.length; f++) {

            if(studentNameRollno[f][6]=="P"){
                presentCount++;
            }else
            {
                absentCount++;
            }

            Log.i("nextdetails", " " + studentNameRollno[f][0] + " " + studentNameRollno[f][1] + " " + studentNameRollno[f][2] + " " + studentNameRollno[f][3] + " " + studentNameRollno[f][4] + " " + studentNameRollno[f][5] + " " + studentNameRollno[f][6] + " " + studentNameRollno[f][7] + " " + studentNameRollno[f][8]);
        }

        //ShowSubjectTextView.setText();
                ShowDateTextView.setText(studentNameRollno[0][4]+"");
        ShowTotalTextView.setText(noOfStudent+"");
                ShowPresentTextView.setText(presentCount+"");
        ShowAbsentTextView.setText(absentCount+"");



        }

    public static String [][] to2dim (String source, String outerdelim, String innerdelim) {
        // outerdelim may be a group of characters
        String [] sOuter = source.split ("[" + outerdelim + "]");
        int size = sOuter.length;
        // one dimension of the array has to be known on declaration:
        String [][] result = new String [size][];
        int count = 0;
        for (String line : sOuter)
        {
            result [count] = line.split (innerdelim);
            ++count;
        }
        return result;
    }



    public  void EditAttendance(View view){
        Button editbutton =(Button) findViewById(R.id.EditButton);
        Intent editattendanceactivity = new Intent(getApplicationContext(), EditAttendanceActivity.class);
        startActivity(editattendanceactivity);
    }

    public  void SaveAttendance(View view){
        Button editbutton =(Button) findViewById(R.id.SaveButton);
        Intent teacherhomeactivity = new Intent(getApplicationContext(), TeacherHomeActivity.class);
        startActivity(teacherhomeactivity);
    }
}
