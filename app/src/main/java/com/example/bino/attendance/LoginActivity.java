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
    EditText userName,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//full screen login activity
        setContentView(R.layout.activity_login);

        Intent nextActivity = new Intent(getApplicationContext(), AdminStudentSeachResultActivity.class);
        startActivity(nextActivity);

        typeOfUser=(Spinner)findViewById(R.id.typeOfUser);
        userName=(EditText) findViewById(R.id.userNameEditText);
        password=(EditText) findViewById(R.id.passwordEditText);

        ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,users);

        typeOfUser.setAdapter(userAdapter);

    }
            public void Login(View view){
                Button button = (Button) findViewById(R.id.loginButton);
                Intent nextActivity = new Intent(getApplicationContext(), AdminStudentSearchBy.class);
                startActivity(nextActivity);
            }
}
