package com.example.bino.attendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditAttendanceActivity extends AppCompatActivity {

    TableLayout markedAttendanceTable;
    Button save,cancel;
    ArrayList<String> students;
    ListView listView;
    static int j=0;
    static  String[][] studentsarr=
            {       {"01","Albino","P",""},
                    {"01","amit","","A"},
                    {"01","Ronak","P",""},
                    {"01","Ravi","P",""},
                    {"01","ramesh","P",""},
                    {"01","Albino","P",""},
                    {"01","amit","","A"},
                    {"01","Ronak","P",""},
                    {"01","Ravi","P",""},
                    {"01","ramesh","P",""},
                    {"01","Albino","P",""},
                    {"01","amit","","A"},
                    {"01","Ronak","P",""},
                    {"01","Ravi","P",""},
                    {"01","ramesh","P",""},
                    {"01","Albino","P",""},
                    {"01","amit","","A"},
                    {"01","Ronak","P",""},
                    {"01","Ravi","P",""},
                    {"01","ramesh","P",""},
                    {"01","Albino","P",""},
                    {"01","amit","","A"},
                    {"01","Ronak","P",""},
                    {"01","Ravi","P",""},
                    {"01","ramesh","P",""}
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_attendance);

        //markedAttendanceTable=(TableLayout)findViewById(R.id.markedAttendanceTableLayout);
        listView=(ListView)findViewById(R.id.displaystudents);
        save=(Button)findViewById(R.id.saveButtonEditAttendance);
        cancel=(Button)findViewById(R.id.cancelButtonEditAttendance);
        CustomAdapter customAdapter=new CustomAdapter();
        listView.setAdapter(customAdapter);

    }

    public  class CustomAdapter extends BaseAdapter{

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
            view=getLayoutInflater().inflate(R.layout.customlayoutdisplayeditstudentslist,null);
            TextView textViewRoll=(TextView)view.findViewById(R.id.rollno);
            TextView textViewName=(TextView)view.findViewById(R.id.name);
            TextView textViewPresent=(TextView)view.findViewById(R.id.present);
            TextView textViewAbsent=(TextView)view.findViewById(R.id.absent);
            textViewRoll.setText(studentsarr[i][0]);
            textViewName.setText(studentsarr[i][1]);
            textViewPresent.setText(studentsarr[i][2]);
            textViewAbsent.setText(studentsarr[i][3]);
            return view;
        }
    }

    public void confirmSave(View view){
        Button save=(Button)findViewById(R.id.saveButtonEditAttendance);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Do to want to save")
                .setMessage("")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(EditAttendanceActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        Intent gototeacherHomeActivity = new Intent(getApplicationContext(), TeacherHomeActivity.class);
                        finish();//kill current activity
                        startActivity(gototeacherHomeActivity);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent gotoshowtakenattendanceActivity = new Intent(getApplicationContext(), ShowTakenAttendanceActivity.class);
                        startActivity(gotoshowtakenattendanceActivity);
                    }
                })
                .show();{

        }
    }

}
