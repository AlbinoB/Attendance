package com.example.bino.attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class StudentViewIndividualAttendance extends AppCompatActivity {

    ListView listView;
    static String[][] studentsarr =
            {
                    {"21/07/2019", "true"},
                    {"21/07/2019", "true"},
                    {"21/07/2019", "false"},
                    {"21/07/2019", "true"}

            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_individual_attendance);
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
            view = getLayoutInflater().inflate(R.layout.customlayoutstudentindividualattendance, null);
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
