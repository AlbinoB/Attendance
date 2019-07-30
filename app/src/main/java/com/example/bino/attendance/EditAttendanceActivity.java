package com.example.bino.attendance;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class EditAttendanceActivity extends AppCompatActivity {

    TableLayout markedAttendanceTable;
    Button save,cancel;
    ArrayList<String> students;
    ArrayAdapter<String> arrayAdapter;
    static  String[][] studentsarr=
            {   {"01","Albino","P","A"},
                    {"01","Albino","P","A"},
                    {"01","Albino","P","A"},
                    {"01","Albino","P","A"},
                    {"01","Albino","P","A"}
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_attendance);

        //markedAttendanceTable=(TableLayout)findViewById(R.id.markedAttendanceTableLayout);
        save=(Button)findViewById(R.id.saveButtonEditAttendance);
        cancel=(Button)findViewById(R.id.cancelButtonEditAttendance);


        TableRow tr1 = new TableRow(this);
        tr1.setLayoutParams(new TableLayout.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView textview = new TextView(this);
        textview.setText("data");
//textview.getTextColors(R.color.)
        textview.setTextColor(Color.BLACK);
        tr1.addView(textview);
        textview.setPaddingRelative(110,10,50,10);

        textview.setTextSize(16);


        TextView textview1 = new TextView(this);
        textview1.setText("data");
//textview.getTextColors(R.color.)
        textview1.setTextColor(Color.BLACK);
        tr1.addView(textview1);
        textview1.setGravity(100);


        TextView textview2 = new TextView(this);
        textview2.setText("data");
//textview.getTextColors(R.color.)
        textview2.setTextColor(Color.BLACK);
        tr1.addView(textview2);
        textview2.setGravity(Gravity.CENTER);



        TextView textview3 = new TextView(this);
        textview3.setText("data");
//textview.getTextColors(R.color.)
        textview3.setTextColor(Color.BLACK);
        tr1.addView(textview3);
        textview3.setGravity(Gravity.CENTER);
        markedAttendanceTable.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        textview1.setPaddingRelative(110,10,50,10);
        textview2.setPaddingRelative(110,10,50,10);
        textview3.setPaddingRelative(110,10,50,10);

        TableRow tr2 = new TableRow(this);
        tr1.removeAllViews();
        tr2.addView(textview);
        tr2.addView(textview1);
        tr2.addView(textview2);
        tr2.addView(textview3);

        markedAttendanceTable.addView(tr2, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));



    }
}
