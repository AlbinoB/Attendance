package com.example.bino.attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class AdminCourseShowAllSubjectActivity extends AppCompatActivity {

    ListView subjectnamelistview;

    String[][] subjectdetails={
            {"1001","java","amit"},
            {"1002","c++","amit2"},
            {"1003","php","amit3"},
            {"1004","asp","amit4"},
            {"1005","html","amit5"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_show_all_subject);

        subjectnamelistview= (ListView)findViewById(R.id.AllSubjectListView);
        TextView coursename =(TextView) findViewById(R.id.AdminCourseNameTextView);
        TextView courseyear =(TextView) findViewById(R.id.AdminYearTextView);
        TextView coursesem =(TextView) findViewById(R.id.AdminSemTextView);
        CustomAdapter customadapters = new CustomAdapter();
        subjectnamelistview.setAdapter(customadapters);


    }

        class CustomAdapter extends BaseAdapter{

            @Override
            public int getCount() {
                return subjectdetails.length;
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
                view = getLayoutInflater().inflate(R.layout.customlayoutadmincourseshowallsubject,null);
                TextView subjectcode = (TextView) (view).findViewById(R.id.SubjectCodeTextView);
                TextView subjectname = (TextView)  (view).findViewById(R.id.SubjectNameTextView);
                TextView teachername = (TextView) (view). findViewById(R.id.TeacherNamesTextView);

                subjectcode.setText(subjectdetails[i][0]);
                subjectname.setText(subjectdetails[i][1]);
                teachername.setText(subjectdetails[i][2]);

                return view;
            }
        }
}
