package com.example.bino.attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;



public class AdminCourseParticularSemSubjects extends AppCompatActivity {

    static String[][] studentsarr =
            {
                    {"3001", "Advance Java"},
                    {"3001", "Advance Java"},
                    {"3001", "Advance Java"},
                    {"3001", "Advance Java"},
                    {"3001", "Advance Java"},
                    {"3001", "Advance Java"}
            };
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_particular_sem_subjects);
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
            view = getLayoutInflater().inflate(R.layout.customlayoutadmincourseparticularsemsubjects, null);
            TextView sCode=(TextView)view.findViewById(R.id.sCode);
            TextView sName=(TextView)view.findViewById(R.id.sName);
            sCode.setText(studentsarr[i][0]);
            sName.setText(studentsarr[i][1]);

            return  view;

        }
    }
}
