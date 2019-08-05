package com.example.bino.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class LoginActivity extends AppCompatActivity {


        private static final String[] users ={"Select User","Teacher","Student","Admin"};
        Spinner typeOfUser;
        EditText usernametext,passwordtext;

        String userName="",password="",user="";

        SharedPreferences sharedPreferences;



        public class ConnectToDB extends AsyncTask<String,Void,Boolean>{
            @Override
            protected Boolean doInBackground(String... sqlarr) {
                StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Connection connection=null;
                String url=null;
                try {
                    Class.forName("net.sourceforge.jtds.jdbc.Driver");
                    url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                    connection= DriverManager.getConnection(url);
                    Statement stmt = connection.createStatement();
                    ResultSet rs=null;
                    rs = stmt.executeQuery(sqlarr[0]);
                    //Log.i("data:::::::::::::",Integer.toString(rs.getInt(1)));

                    Log.i("data:::::::::::::",sqlarr[0]);

                    if(rs.next()){
                        return true;
                    }else
                    {
                        return false;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    return  false;
                }
            }
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//full screen login activity
            setContentView(R.layout.activity_login);


            typeOfUser=(Spinner)findViewById(R.id.typeOfUser);
            usernametext=(EditText) findViewById(R.id.userNameEditText);
            passwordtext=(EditText) findViewById(R.id.passwordEditText);

            ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,users);

            typeOfUser.setAdapter(userAdapter);

        }
        public void Login(View view) throws Exception{
            ConnectToDB connectToDB=new ConnectToDB();//obj of async class

            userName = usernametext.getText().toString();
            password = passwordtext.getText().toString();
            Log.i("llllllllllllllllllll",userName +" "+password);
            user = typeOfUser.getSelectedItem().toString();
            if (checkEmptyFields()) {

                switch (user) {
                    case "Select User": {
                        Toast.makeText(this, "Please select Type of User!!!", Toast.LENGTH_LONG).show();
                    }
                    break;
                    case "Teacher": {
                        String[] sql = {"SELECT teacherId FROM Teacher where teacherName= '" + userName + "' and teacherPassword='" + password+"' "};

                        if(connectToDB.execute(sql).get()&&user.equals("Teacher")){
                            Toast.makeText(this, "Welcome "+userName, Toast.LENGTH_LONG).show();
                            Intent teacherHomeActivity = new Intent(getApplicationContext(), TeacherHomeActivity.class);

                            sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);
                            sharedPreferences.edit().putString("currentUserName",userName).apply();

                            startActivity(teacherHomeActivity);
                        }else
                        {
                            Toast.makeText(this, "Wrong Credentials!!!", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                    case "Student": {
                        String sql = "SELECT studentErpNo FROM Student where studentName= '" + userName + "' and studentPassword= '" + password+"' ";
                        if(connectToDB.execute(sql).get()&&user.equals("Student")){
                            Toast.makeText(this, "Welcome "+userName, Toast.LENGTH_LONG).show();
                            Intent studentViewAllAttendance = new Intent(getApplicationContext(), StudentViewAllAttendance.class);

                            sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);
                            sharedPreferences.edit().putString("currentUserName",userName).apply();

                            startActivity(studentViewAllAttendance);
                        }else
                        {
                            Toast.makeText(this, "Wrong Credentials!!!", Toast.LENGTH_LONG).show();
                        }

                    }
                    break;
                    case "Admin": {
                        if (userName.equals("admin") && password.equals("admin") && user.equals("Admin")) {
                            Toast.makeText(this, "Welcome "+userName, Toast.LENGTH_LONG).show();
                            Intent adminhomeactivity = new Intent(getApplicationContext(), AdminHomeActivity.class);
                            sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);
                            sharedPreferences.edit().putString("currentUserName",userName).apply();

                            startActivity(adminhomeactivity);
                        }else
                        {
                            Toast.makeText(this, "Wrong Credentials!!! ", Toast.LENGTH_LONG).show();
                        }
                        break;

                    }

                }

            }
        }

        public boolean checkEmptyFields(){
            String error="";
            if(userName.equals("")&&userName.equals("")){
                error="Please enter User Name and Password!!!";
                passwordtext.setText("");
            }else
            {
                if(userName.equals("")){
                    error="Please enter User Name!!!";
                }
                else {
                    if(password.equals("")){
                        error="Please enter Password!!!";
                    }
                }
            }

            if(error!="")
            {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                return false;
            }
            else
            {
                return true;
            }

        }



    }




