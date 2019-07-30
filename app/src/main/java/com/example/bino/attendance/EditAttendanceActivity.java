package com.example.bino.attendance;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

}
