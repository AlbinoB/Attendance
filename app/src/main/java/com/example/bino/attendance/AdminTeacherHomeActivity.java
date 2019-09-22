package com.example.bino.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminTeacherHomeActivity extends AppCompatActivity {


    private Spinner admincourseSpiner;
    String[] course;

    SharedPreferences sharedPreferences;

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

                getandsetcourse();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//doInBackground

        public void getandsetcourse(){
            try{
                rs = stmt.executeQuery("select  count(*) as noofcourse  from Course");
                while(rs.next()) {
                    course = new String[(rs.getInt("noofcourse")) + 1];
                }

                int i=1;
                course[0]="Select Course";
                rs= stmt.executeQuery("select courseName from Course");
                while(rs.next()){
                    course[i]=rs.getString("courseName");
                    i++;
                }

                ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(AdminTeacherHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,course);
                courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                admincourseSpiner.setAdapter(courseAdapter);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }//AsyncTask

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_admin_teacher_home);
       sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

       admincourseSpiner = (Spinner)findViewById(R.id.AdmincourseSpinner);

       AdminTeacherHomeActivity.ConnectToDB connectToDB=new ConnectToDB();//obj of async class

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
    }

   public  void showAllTeachers(View view){

       if(admincourseSpiner.getSelectedItem().equals( "Select Course")){
           Toast.makeText(this, "Please Select Course!!!", Toast.LENGTH_SHORT).show();
       }else {
           sharedPreferences.edit().putString("coursename",admincourseSpiner.getSelectedItem().toString()).apply();
           Intent adminShowTeacherNamenActivity = new Intent(getApplicationContext(), AdminTeacherShowAllNamesActivity.class);
           startActivity(adminShowTeacherNamenActivity);
       }
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
                finishAffinity();
                startActivity(homeActivity);
                return true;
            }

            default:{
                return false;
            }
        }
    }

}
