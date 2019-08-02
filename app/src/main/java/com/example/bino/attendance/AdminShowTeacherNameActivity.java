package com.example.bino.attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class AdminShowTeacherNameActivity extends AppCompatActivity {

    ArrayAdapter<String> teachernameadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_teacher_name);

        ListView teachernamelistview= (ListView)findViewById(R.id.TEacherNameListView);
        EditText searchnametextview =(EditText) findViewById(R.id.SearchNameTextView);
        ArrayList<String> teachername =new ArrayList<String>();
        teachername.add("pankaj");
        teachername.add("ankit");
        teachername.add("jayyu");
        teachername.add("radhe");
        teachername.add("neeraj");
         teachernameadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,teachername);
        teachernamelistview.setAdapter(teachernameadapter);

        searchnametextview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (AdminShowTeacherNameActivity.this).teachernameadapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
