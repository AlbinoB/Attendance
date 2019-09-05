package com.example.bino.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminTeacherShowAllNamesActivity extends AppCompatActivity {

    ListView teachernamelistview;
    SharedPreferences sharedPreferences;
    String currentcourse;
    CustomAdapter customAdapter;
    String[] teachernames;
    String[] teacherid;

    public class ConnectToDB extends AsyncTask<String,Void,Boolean> {

        Connection connection = null;
        String url = null;
        Statement stmt;
        ResultSet rs = null;
        String sql = "";

        @Override
        protected Boolean doInBackground(String... sqlarr) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                connection = DriverManager.getConnection(url);
                stmt = connection.createStatement();

                getcoursename();
                getandSetTeachers();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//doInBackground

        public void getcoursename(){
            currentcourse =((String)sharedPreferences.getString("coursename","no course"));
        }

        void getandSetTeachers(){
            try {
                rs = stmt.executeQuery("select  count(*) as noofcourse from Teacher,Course where fkcourseIdTeacher=courseId and courseName='"+currentcourse+"'");
                if(rs.next()) {
                    teachernames = new String[(rs.getInt("noofcourse"))];
                    teacherid = new String [(rs.getInt("noofcourse"))];
                }

                rs = stmt.executeQuery("select  teacherName from Teacher,Course where fkcourseIdTeacher=courseId and courseName='"+currentcourse+"'");
                int i=0;
                while(rs.next()) {
                    teachernames[i++] =(rs.getString("teacherName"));
                }

                rs = stmt.executeQuery("select teacherId from Teacher,Course where fkcourseIdTeacher=courseId and courseName='"+currentcourse+"'");
                int j=0;
                while(rs.next()) {
                    teacherid[j++] =(rs.getString("teacherId"));
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }//AsyncTask

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teacher_show_all_names);

        teachernamelistview= (ListView)findViewById(R.id.TEacherNameListView);

        EditText searchnametextview =(EditText) findViewById(R.id.SearchNameTextView);
        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        AdminTeacherShowAllNamesActivity.ConnectToDB connectToDB=new ConnectToDB();//obj of async class

        String[] sql={
        };

        try {
            if(connectToDB.execute(sql).get()){
                {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        customAdapter =new CustomAdapter();

        teachernamelistview.setAdapter(customAdapter);
        teachernamelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i1, long l) {

            }
        });

    }

    public void goToEditTeacher(View view){
        Intent loginActivity = new Intent(getApplicationContext(),AdminTeacherViewEditAddDetailsActivity.class);
        loginActivity.putExtra("check1","addnew");
        startActivity(loginActivity);
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return teachernames.length;
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
            view = getLayoutInflater().inflate(R.layout.customlayoutadminsteachershowallname,null);
            final TextView teachernametextview =(TextView)(view).findViewById(R.id.TeacherNameTextView);
           final TextView teacheridlistview= (TextView) (view).findViewById(R.id.TeacherIdTextView);
            teachernametextview.setText(teachernames[i]);
           teacheridlistview.setText(teacherid[i]);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent adminTeacherViewEditAddDetailsActivity = new Intent(getApplicationContext(), AdminTeacherViewEditAddDetailsActivity.class);
                    adminTeacherViewEditAddDetailsActivity.putExtra("teacherid",teacheridlistview.getText().toString());
                    adminTeacherViewEditAddDetailsActivity.putExtra("check1","show");

                  startActivity(adminTeacherViewEditAddDetailsActivity);

                }
            });
            Button button =(Button)view.findViewById(R.id.editAttendance1);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent adminTeacherViewEditAddDetailsActivity = new Intent(getApplicationContext(), AdminTeacherViewEditAddDetailsActivity.class);
                    adminTeacherViewEditAddDetailsActivity.putExtra("teacherid",teacheridlistview.getText().toString());
                    adminTeacherViewEditAddDetailsActivity.putExtra("check1","edit");

                    startActivity(adminTeacherViewEditAddDetailsActivity);
                }
            });
            return view;
        }
    }
}
