package com.example.bino.attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class TeacherSearchResult extends AppCompatActivity {

    static String[][] studentsarr =
            {       {"01", "Mca01", "Albino", "90.2"},
                    {"02", "Mca01", "amit", "90.2"},
                    {"03", "Mca01", "Ronak", "90.2"},
                    {"04", "Mca01", "Ravi", "90.2"},
                    {"01", "Mca01", "Albino", "90.2"},
                    {"02", "Mca01", "amit", "90.2"},
                    {"03", "Mca01", "Ronak", "90.2"},
                    {"04", "Mca01", "Ravi", "90.2"},
                    {"01", "Mca01", "Albino", "90.2"},
                    {"02", "Mca01", "amit", "90.2"},
                    {"03", "Mca01", "Ronak", "90.2"},
                    {"04", "Mca01", "Ravi", "90.2"},
                    {"01", "Mca01", "Albino", "90.2"},
                    {"02", "Mca01", "amit", "90.2"},
                    {"03", "Mca01", "Ronak", "90.2"},
                    {"04", "Mca01", "Ravi", "90.2"},
                    {"01", "Mca01", "Albino", "90.2"},
                    {"02", "Mca01", "amit", "90.2"},
                    {"03", "Mca01", "Ronak", "90.2"},
                    {"04", "Mca01", "Ravi", "90.2"},

                    {"05", "Mca01", "ramesh", "90.2"}
            };
    ListView listView;
    Button download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_search_result);
        listView=(ListView)findViewById(R.id.listView);
        download=(Button)findViewById(R.id.download);
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
            view = getLayoutInflater().inflate(R.layout.customlayoutteachersearchdisplay, null);
            TextView textViewSrno = (TextView) view.findViewById(R.id.srno);
            TextView textViewRollno = (TextView) view.findViewById(R.id.rollno);
            TextView textViewName = (TextView) view.findViewById(R.id.name);
            TextView textViewPercentage = (TextView) view.findViewById(R.id.percentage);
            textViewSrno.setText(studentsarr[i][0]);
            textViewRollno.setText(studentsarr[i][1]);
            textViewName.setText(studentsarr[i][2]);
            textViewPercentage.setText(studentsarr[i][3]);

            return view;
        }
    }
}
