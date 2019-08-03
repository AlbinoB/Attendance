package com.example.bino.attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class AdminShowTeacherNameActivity extends AppCompatActivity {

    ListView teachernamelistview;

     String[] teachernames={
            "amit1", "amit2", "amit3", "amit4", "amit5", "amit6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_teacher_name);

        teachernamelistview= (ListView)findViewById(R.id.TEacherNameListView);
        EditText searchnametextview =(EditText) findViewById(R.id.SearchNameTextView);
        CustomAdapter customAdapter =new CustomAdapter();
        teachernamelistview.setAdapter(customAdapter);


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
            view = getLayoutInflater().inflate(R.layout.custonadminshowteachername,null);
            TextView teachernametextview =(TextView)(view).findViewById(R.id.TeacherNameTextView);

            teachernametextview.setText(teachernames[i]);

            return view;
        }
    }
}
