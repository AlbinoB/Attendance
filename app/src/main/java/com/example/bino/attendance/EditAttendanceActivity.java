package com.example.bino.attendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditAttendanceActivity extends AppCompatActivity {

    TableLayout markedAttendanceTable;
    Button save,cancel;
    ArrayList<String> students;
    ListView listView;
    static int j=0;
    static  int updateAttendanceIndex;
    SharedPreferences sharedPreferences;
    String[][] studentNameRollno;

    TextView textViewPresent,textViewAbsent;
            /*
            {       {"01","Albino","P",""},
                    {"01","amit","","A"},
                    {"01","Ronak","P",""},
                    {"01","Ravi","P",""},
                    {"01","ramesh","P",""},
                    {"01","Albino","P",""},
                    {"01","amit","","A"},
                    {"01","Ronak","P",""},
                    {"01","Ravi","P",""},
                    {"01","ramesh","P",""},
                    {"01","Albino","P",""},
                    {"01","amit","","A"},
                    {"01","Ronak","P",""},
                    {"01","Ravi","P",""},
                    {"01","ramesh","P",""},
                    {"01","Albino","P",""},
                    {"01","amit","","A"},
                    {"01","Ronak","P",""},
                    {"01","Ravi","P",""},
                    {"01","ramesh","P",""},
                    {"01","Albino","P",""},
                    {"01","amit","","A"},
                    {"01","Ronak","P",""},
                    {"01","Ravi","P",""},
                    {"01","ramesh","P",""}
            };
            */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_attendance);

        sharedPreferences = this.getApplicationContext().getSharedPreferences("om.example.bino.attendance", MODE_PRIVATE);
        String stringFullArray = (String) sharedPreferences.getString("stringFullArray", "no value");
        Log.i("nextfullarr", stringFullArray);
        int noOfStudent = sharedPreferences.getInt("noOfStudent", 0);
        studentNameRollno=new String[noOfStudent][9];

        studentNameRollno = to2dim(stringFullArray, ";", " ");


            //markedAttendanceTable=(TableLayout)findViewById(R.id.markedAttendanceTableLayout);
            listView = (ListView) findViewById(R.id.displaystudents);
            save = (Button) findViewById(R.id.saveButtonEditAttendance);
            cancel = (Button) findViewById(R.id.cancelButtonEditAttendance);
            CustomAdapter customAdapter = new CustomAdapter();
            listView.setAdapter(customAdapter);

            listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    updateAttendanceIndex=i;
                    textViewPresent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View viewAP) {

                            if(textViewPresent.getText().toString().equals("")){
                                Log.i("beforetextViewPresent:",studentNameRollno[updateAttendanceIndex][6]);
                                studentNameRollno[updateAttendanceIndex][6]="P";
                                textViewPresent.setText("P");
                                textViewAbsent.setText("");
                                Log.i("aftertextViewPresent:",studentNameRollno[updateAttendanceIndex][6]);
                            }
                        }
                    });

                    textViewAbsent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View viewAP) {

                            if(textViewAbsent.getText().toString().equals("")){
                                Log.i("beforetextViewAbsent:",studentNameRollno[updateAttendanceIndex][6]);
                                studentNameRollno[updateAttendanceIndex][6]="A";
                                textViewPresent.setText("");
                                textViewAbsent.setText("A");
                                Log.i("aftertextViewAbsent:",studentNameRollno[updateAttendanceIndex][6]);
                            }

                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


    }


    public static String [][] to2dim (String source, String outerdelim, String innerdelim) {
        // outerdelim may be a group of characters
        String [] sOuter = source.split ("[" + outerdelim + "]");
        int size = sOuter.length;
        // one dimension of the array has to be known on declaration:
        String [][] result = new String [size][];
        int count = 0;
        for (String line : sOuter)
        {
            result [count] = line.split (innerdelim);
            ++count;
        }
        return result;
    }


    public  class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return studentNameRollno.length;
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
        public View getView( int i, View view, ViewGroup viewGroup) {
            view=getLayoutInflater().inflate(R.layout.customlayoutdisplayeditstudentslist,null);
            TextView textViewRoll=(TextView)view.findViewById(R.id.rollno);
            TextView textViewName=(TextView)view.findViewById(R.id.name);
             textViewPresent=(TextView)view.findViewById(R.id.present);
             textViewAbsent=(TextView)view.findViewById(R.id.absent);


            textViewRoll.setText(studentNameRollno[i][8]);
            textViewName.setText(studentNameRollno[i][7]);


            if (studentNameRollno[i][6].equals("P")) {
                textViewPresent.setText(studentNameRollno[i][6]);
                textViewAbsent.setText("");

            } else {
                textViewPresent.setText("");
                textViewAbsent.setText(studentNameRollno[i][6]);

            }
            return view;
        }
    }

    public void confirmSave(View view){
        Button save=(Button)findViewById(R.id.saveButtonEditAttendance);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Do to want to save")
                .setMessage("")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(EditAttendanceActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        Intent showTakenAttendanceActivity = new Intent(getApplicationContext(), ShowTakenAttendanceActivity.class);
                        String stringFullArrayUpdated="";
                        for (int f = 0; f < studentNameRollno.length; f++) {


                            Log.i("details", " " + studentNameRollno[f][0] + " " + studentNameRollno[f][1] + " " + studentNameRollno[f][2] + " " + studentNameRollno[f][3] + " " + studentNameRollno[f][4] + " " + studentNameRollno[f][5] + " " + studentNameRollno[f][6] + " " + studentNameRollno[f][7] + " " + studentNameRollno[f][8]);
                            stringFullArrayUpdated=stringFullArrayUpdated+studentNameRollno[f][0] + " " + studentNameRollno[f][1] + " " + studentNameRollno[f][2] + " " + studentNameRollno[f][3] + " " + studentNameRollno[f][4] + " " + studentNameRollno[f][5] + " " + studentNameRollno[f][6] + " " + studentNameRollno[f][7] + " " + studentNameRollno[f][8]+";";


                        }
                        Log.i("fullarrbbbb",stringFullArrayUpdated);



                        sharedPreferences.edit().putString("stringFullArray",stringFullArrayUpdated).apply();
                        finish();//kill current activity

                        startActivity(showTakenAttendanceActivity);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent gotoshowtakenattendanceActivity = new Intent(getApplicationContext(), ShowTakenAttendanceActivity.class);
                        finish();
                        startActivity(gotoshowtakenattendanceActivity);
                    }
                })
                .show();{

        }
    }

}
