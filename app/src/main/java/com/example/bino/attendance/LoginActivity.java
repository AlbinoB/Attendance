package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private static final String[] users ={"Select User","Teacher","Student","Admin"};
    Spinner typeOfUser;
    EditText usernametext,passwordtext;
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
            public void Login(View view){
                Button button = (Button) findViewById(R.id.loginButton);
                String userName =usernametext.getText().toString();
                String password = passwordtext.getText().toString();
                String user=typeOfUser.getSelectedItem().toString();
                if(userName.equals("student") && password.equals("student") && user.equals("Student")){
                    Intent studentviewallattendance = new Intent(getApplicationContext(), StudentViewAllAttendance.class);
                    startActivity(studentviewallattendance);
                } else if(userName.equals("teacher") && password.equals("teacher") && user.equals("Teacher")){
                    Intent teacherhomeactivity = new Intent(getApplicationContext(), TeacherHomeActivity.class);
                    startActivity(teacherhomeactivity);
                } else if(userName.equals("admin") && password.equals("admin") && user.equals("Admin")) {
                    Intent adminhomeactivity = new Intent(getApplicationContext(), AdminHomeActivity.class);
                    startActivity(adminhomeactivity);

                }
            }
}
