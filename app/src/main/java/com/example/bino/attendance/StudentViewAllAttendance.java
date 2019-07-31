package com.example.bino.attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class StudentViewAllAttendance extends AppCompatActivity {
    static String[][] studentsarr =
            {
                    {"3001", "Advance Java", "90.2"},
                    {"3001", "Advance Java", "90.2"},
                    {"3001", "Advance Java", "90.2"},
                    {"3001", "Advance Java", "90.2"},
                    {"3001", "Advance Java", "90.2"},
                    {"3001", "Advance Java", "90.2"},
                    {"3001", "Advance Java", "90.2"},
                    {"3001", "Advance Java", "90.2"},
                    {"3001", "Advance Java", "90.2"},
                    {"3001", "Advance Java", "90.2"},
                    {"3001", "Advance Java", "90.2"}
            };
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_all_attendance);
        listView=(ListView)findViewById(R.id.listView);
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
