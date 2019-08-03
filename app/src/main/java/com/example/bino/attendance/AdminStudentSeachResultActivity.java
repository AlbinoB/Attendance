package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
    ListView adminstudentlistView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_search_result);

        adminstudentlistView=(ListView)findViewById(R.id.displayStudents);
        CustomAdapter customAdapter=new CustomAdapter();
        adminstudentlistView.setAdapter(customAdapter);



    }

    public void addStudent(View view){
        Intent adminStudentEditAddDetails = new Intent(getApplicationContext(), AdminStudentEditAddDetails.class);
        startActivity(adminStudentEditAddDetails);
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
            Button admineditstudent = (Button) view.findViewById(R.id.editTeacher);

                    textViewSsrno.setText(studentsarr[i][0]);
             textViewSrollno.setText(studentsarr[i][1]);
            textViewSname.setText(studentsarr[i][2]);
            textViewSpercentage.setText(studentsarr[i][3]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {

                   /* TextView textViewSsrno = (TextView) view1.findViewById(R.id.srno);
                    TextView textViewSrollno = (TextView) view1.findViewById(R.id.rollno);
                    TextView textViewSname = (TextView) view1.findViewById(R.id.name);
                    TextView textViewSpercentage= (TextView) view1.findViewById(R.id.percentage);
                        this code is for getting all the value which is present in
                         list view just we have to use this ids*/
                    Intent adminstudentviewallattendance = new Intent(getApplicationContext(), AdminStudentViewAllAttendance.class);
                    startActivity(adminstudentviewallattendance);
                }
            });
            admineditstudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent adminstudenteditadddetails = new Intent(getApplicationContext(), AdminStudentEditAddDetails.class);
                    startActivity(adminstudenteditadddetails);
                }
            });

            return view;
        }
    }
    }

