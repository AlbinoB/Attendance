package com.example.bino.attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
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
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {


        private static final String[] users ={"Select User","Teacher","Student","Admin"};
        Spinner typeOfUser;
        EditText usernametext,passwordtext;

        String userName="",password="",user="";

        SharedPreferences sharedPreferences;
        ProgressDialog progressdialog;
        Handler handler =new Handler();



        public class ConnectToDB extends AsyncTask<String,Void,Boolean>{



            @Override
            protected void onPreExecute() {

            }
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
                        sharedPreferences.edit().putString("currentUserName",rs.getString("currentUserName")).apply();

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
            @Override
            protected void onPostExecute(Boolean result) {
                // do UI work here


            }
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//full screen login activity
            setContentView(R.layout.activity_login);



            typeOfUser=(Spinner)findViewById(R.id.typeOfUser);
            usernametext=(EditText) findViewById(R.id.userNameEditText);
            passwordtext=(EditText) findViewById(R.id.passwordEditText);
            progressdialog = new ProgressDialog(LoginActivity.this);

            ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,users);

            typeOfUser.setAdapter(userAdapter);

        }
        @Override
        protected void onPause(){
            super.onPause();
            progressdialog.dismiss();
        }
        public void Login(View view) throws Exception{


            userName = usernametext.getText().toString();
            password = passwordtext.getText().toString();
            Log.i("llllllllllllllllllll",userName +" "+password);
            user = typeOfUser.getSelectedItem().toString();


            progressdialog.setMessage("Please Wait....");
            progressdialog.show();

            Thread validationThread=new Thread(new Runnable() {

                ConnectToDB connectToDB=new ConnectToDB();//obj of async class
                @Override
                public void run() {
                    Looper.prepare();
                    if (checkEmptyFields()) {
                        try{
                        switch (user) {


                            case "Select User": {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressdialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Please select Type of User!!!", Toast.LENGTH_LONG).show();

                                    }
                                });

                            }
                            break;
                            case "Teacher": {
                                String[] sql = {"SELECT teacherName as currentUserName FROM Teacher where teacherId= '" + userName + "' and teacherPassword='" + password + "' "};


                                if (connectToDB.execute(sql).get() && user.equals("Teacher")) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressdialog.dismiss();
                                            Toast.makeText(LoginActivity.this, "Welcome " + (String) sharedPreferences.getString("currentUserName", "no  name"), Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    sharedPreferences.edit().putInt("currentUserId", Integer.parseInt(userName)).apply();
                                    Intent teacherHomeActivity = new Intent(getApplicationContext(), TeacherHomeActivity.class);

                                    startActivity(teacherHomeActivity);
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressdialog.dismiss();
                                            Toast.makeText(LoginActivity.this, "Wrong Credentials!!!", Toast.LENGTH_LONG).show();

                                        }
                                    });


                                }

                            }
                            break;
                            case "Student": {
                                String sql = "SELECT studentName as currentUserName FROM Student where studentErpNo= '" + userName + "' and studentPassword= '" + password+"' ";
                                if(connectToDB.execute(sql).get()&&user.equals("Student")){
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressdialog.dismiss();
                                            Toast.makeText(LoginActivity.this, "Welcome "+(String)sharedPreferences.getString("currentUserName","no  name"), Toast.LENGTH_LONG).show();

                                        }
                                    });

                                    Intent studentViewAllAttendance = new Intent(getApplicationContext(), StudentViewAllAttendance.class);

                                    sharedPreferences.edit().putInt("currentUserErpNo",Integer.parseInt(userName)).apply();
                                    startActivity(studentViewAllAttendance);
                                }else
                                {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressdialog.dismiss();
                                            Toast.makeText(LoginActivity.this, "Wrong Credentials!!!", Toast.LENGTH_LONG).show();

                                        }
                                    });


                                }

                            }
                            break;
                            case "Admin": {
                                if (userName.equals("admin") && password.equals("admin") && user.equals("Admin")) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressdialog.dismiss();
                                            Toast.makeText(LoginActivity.this, "Welcome "+userName, Toast.LENGTH_LONG).show();

                                        }
                                    });

                                    Intent adminhomeactivity = new Intent(getApplicationContext(), AdminHomeActivity.class);
                                    sharedPreferences.edit().putString("currentUserName",userName).apply();

                                    startActivity(adminhomeactivity);
                                }else
                                {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressdialog.dismiss();
                                            Toast.makeText(LoginActivity.this, "Wrong Credentials!!! ", Toast.LENGTH_LONG).show();

                                        }
                                    });


                                }
                                break;

                            }

                        }

                    } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });



            validationThread.start();

        }

        public boolean checkEmptyFields() {
            int flag1 = 0;
            if (userName.equals("") && userName.equals("")) {


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Please enter User Name and Password!!!", Toast.LENGTH_SHORT).show();
                        progressdialog.dismiss();
                        passwordtext.setText("");
                    }
                });
                flag1 = 0;
            } else {
                if (userName.equals("")) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressdialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Please enter User Name!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    flag1 = 0;


                } else {
                    flag1 = 1;
                }
                if (password.equals("")) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressdialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Please enter Password!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    flag1 = 0;


                } else {
                    flag1 = 1;
                }
            }
            if(flag1==1)
            {
            return true;
            }else
            {
            return false;}
        }

        }









