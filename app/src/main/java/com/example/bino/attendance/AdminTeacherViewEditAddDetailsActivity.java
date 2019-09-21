package com.example.bino.attendance;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminTeacherViewEditAddDetailsActivity extends AppCompatActivity {

    Intent intent;
    String teacherid,check1;
    EditText tname,tdob,temail,tcontact,tcourseid,tgender,tadharno,tid,tsalary,tjoindate,tpassword,taddress ;
    Button savebutton;
    int countid;
    AdminTeacherViewEditAddDetailsActivity.ConnectToDB connectToDB;

    public class ConnectToDB extends AsyncTask<String,Void,Boolean> {

        Connection connection = null;
        String url = null;
        Statement stmt;
        ResultSet rs = null;
        String sql = "";

        public  void insertnewteacherdetails(){

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try{
                        rs=stmt.executeQuery("select top 1 teacherId from Teacher order by teacherId desc ");
                        if(rs.next()){
                            countid = rs.getInt("teacherId");
                        }

                        countid=countid+1;
                        stmt.executeQuery(" insert into Teacher values("+countid+",'"+tname.getText().toString()+"', " +
                                " '"+tpassword.getText().toString()+"', "+0+",  " +
                                " '"+temail.getText().toString()+"',  " +
                                "  '"+tgender.getText().toString()+"',  " +
                                "  '"+taddress.getText().toString()+"',  " +
                                "   "+tadharno.getText().toString()+" ,  " +
                                "  '"+null+"' ,  " +
                                "  "+tcourseid.getText().toString()+" ,   " +
                                "   "+tcontact.getText().toString()+" ,   " +
                                "   '"+tdob.getText().toString()+"'  )");

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    Intent adminTeacherShowAllNamesActivity = new Intent(getApplicationContext(), AdminTeacherShowAllNamesActivity.class);
                    startActivity(adminTeacherShowAllNamesActivity);
                }

            });
                thread.start();

    }

        public  void updateteacherdetails(){

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                   try{
                       stmt.executeUpdate("update Teacher set teachername='"+tname.getText().toString()+"', " +
                               " teacherPassword='"+tpassword.getText().toString()+"', " +
                               " teacherEmailId='"+temail.getText().toString()+"', " +
                               " teacherGender='"+tgender.getText().toString()+"' ," +
                               " teacherAddress='"+taddress.getText().toString()+"', " +
                               " teacheradharno="+tadharno.getText().toString()+", " +
                               " fkcourseIdTeacher="+tcourseid.getText().toString()+", " +
                               " teacherContactNo="+tcontact.getText().toString()+",  " +
                               "teacherDateOfBirth= '"+tdob.getText().toString()+"' where teacherId="+tid.getText().toString()+" ");
                   }catch(Exception e){
                       e.printStackTrace();
                   }

                    Intent adminTeacherShowAllNamesActivity = new Intent(getApplicationContext(), AdminTeacherShowAllNamesActivity.class);
                   startActivity(adminTeacherShowAllNamesActivity);

                }

            });
            thread.start();
        }

        @Override
        protected Boolean doInBackground(String... sqlarr) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                connection = DriverManager.getConnection(url);
                stmt = connection.createStatement();
                    if(check1!=null) {

                        getandsetTeacherdetails();
                    }

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//doInBackground

       public void  getandsetTeacherdetails(){
            try {
                rs = stmt.executeQuery("select * from Teacher where teacherId='" + teacherid + "'");
                while(rs.next()){
                    tname.setText( rs.getString("teacherName"));
                    tdob.setText( rs.getString("teacherDateOfBirth"));
                    temail.setText( rs.getString("teacherEmailId"));
                    tcontact.setText( rs.getString("teacherContactNo"));
                    tcourseid.setText( rs.getString("fkcourseIdTeacher"));
                    tgender.setText( rs.getString("teacherGender"));
                    tadharno.setText( rs.getString("teacheradharno"));
                    tid.setText( rs.getString("teacherId"));
                    tpassword.setText(rs.getString("teacherPassword"));
                    taddress.setText(rs.getString(("teacherAddress")));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }//AsyncTask

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teacher_view_edit_add_details);

         tname = (EditText) findViewById(R.id.TeacherNameShowAddEditText);
         tdob = (EditText) findViewById(R.id.TeacherAgeShowAddEditText);
         temail = (EditText) findViewById(R.id.TeacherEmailShowAddEditText);
         tcontact = (EditText) findViewById(R.id.TeacherContactShowAddEditText);
         tcourseid = (EditText) findViewById(R.id.TeacherCourseIdShowAddEditText);
         tgender = (EditText) findViewById(R.id.TeacherGenderShowAddEditText);
         tadharno = (EditText) findViewById(R.id.TeacherAdharNoShowAddEditText1);
         tid = (EditText) findViewById(R.id.TeacherIdShowAddEditTex2t);
         tsalary = (EditText) findViewById(R.id.TeacherSalaryShowAddEdi2tText);
         tjoindate = (EditText) findViewById(R.id.TeacherJoinDateSho2wAddEditText);
         tpassword =(EditText) findViewById(R.id.TeacherPasswordSho2wAddEditText);
         taddress =(EditText) findViewById(R.id.TeacherAddressSho2wAddEditText);
         savebutton =(Button)findViewById(R.id.save) ;

         intent = getIntent();
         teacherid=intent.getStringExtra("teacherid");
         check1=intent.getStringExtra("check1");

        connectToDB=new ConnectToDB();//obj of async class

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

        if(check1!=null && check1.equals("show")){
            tname.setEnabled(false);
            tdob.setEnabled(false);
            temail.setEnabled(false);
            tcontact.setEnabled(false);
            tcourseid.setEnabled(false);
            tgender.setEnabled(false);
            tadharno.setEnabled(false);
            tid.setEnabled(false);
            tsalary.setEnabled(false);
            tjoindate.setEnabled(false);
            tpassword.setEnabled(false);
            taddress.setEnabled(false);
            savebutton.setVisibility(View.INVISIBLE);

        }
        if(check1!=null && check1.equals("edit")){
            tid.setEnabled(false);
            savebutton.setTag("update");
        }

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(savebutton.getTag().toString().equals("update")){
                    connectToDB.updateteacherdetails();

                }
                if(savebutton.getTag().toString().equals("addnew")){
                    connectToDB.insertnewteacherdetails();

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:{
                Intent homeActivity=new Intent();
                homeActivity=new Intent(getApplicationContext(),LoginActivity.class);
                finish();
                startActivity(homeActivity);
                return true;
            }

            default:{
                return false;
            }
        }
    }
}
