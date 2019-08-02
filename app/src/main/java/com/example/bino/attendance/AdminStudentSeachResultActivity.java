package com.example.bino.attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AdminStudentSeachResultActivity extends AppCompatActivity {

    static String[][] studentsarr =
            {
                    {"01","09", "Albino Braganza", "90.2"},
                    {"01","09", "Albino Braganza", "90.2"},
                    {"01","09", "Albino Braganza", "90.2"},
                    {"01","09", "Albino Braganza", "90.2"},
                    {"01","09", "Albino Braganza", "90.2"},
                    {"01","09", "Albino Braganza", "90.2"},
                    {"01","09", "Albino Braganza", "90.2"},
                    {"01","09", "Albino Braganza", "90.2"},
                    {"01","09", "Albino Braganza", "90.2"},
                    {"01","09", "Albino Braganza", "90.2"},
                    {"01","09", "Albino Braganza", "90.2"},
                    {"01","09", "Albino Braganza", "90.2"},
                    {"01","09", "Albino Braganza", "90.2"}

            };
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_search_result);

        listView=(ListView)findViewById(R.id.displayStudents);
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
            view = getLayoutInflater().inflate(R.layout.customlayoutadminstudentsearchresultdisplaystudents, null);
            TextView textViewSsrno = (TextView) view.findViewById(R.id.srno);
            TextView textViewSrollno = (TextView) view.findViewById(R.id.rollno);
            TextView textViewSname = (TextView) view.findViewById(R.id.name);
            TextView textViewSpercentage= (TextView) view.findViewById(R.id.percentage);
            textViewSsrno.setText(studentsarr[i][0]);
            textViewSrollno.setText(studentsarr[i][1]);
            textViewSname.setText(studentsarr[i][2]);
            textViewSpercentage.setText(studentsarr[i][3]);

            return view;
        }
    }
    }

