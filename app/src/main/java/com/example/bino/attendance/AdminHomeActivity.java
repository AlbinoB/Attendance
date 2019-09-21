package com.example.bino.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class AdminHomeActivity extends AppCompatActivity {

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_admin_home);

            }




    public void goToNextActivity(View view){

        String nextActivity=view.getTag().toString();
        switch (nextActivity){
            case "1":{
                Intent nextActadminstudentsearchby = new Intent(getApplicationContext(), AdminStudentSearchBy.class);
                startActivity(nextActadminstudentsearchby);
            }break;
            case "2":{
                Intent adminteacherhomeactivitys = new Intent(getApplicationContext(), AdminTeacherHomeActivity.class);
                startActivity(adminteacherhomeactivitys);
            }break;
            case "3":{
                Intent adminCourseHomeActivity = new Intent(getApplicationContext(), AdminCourseHomeActivity.class);
                startActivity(adminCourseHomeActivity);
            }break;
            case "4":{
                Toast.makeText(this, "you cleicked:"+nextActivity, Toast.LENGTH_SHORT).show();
            }break;

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
