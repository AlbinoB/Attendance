package com.example.bino.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import jxl.Cell;
import jxl.CellView;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class TeacherSearchResult extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ListView listView;
    Button download;
    int currentteacherid;
    String currentstartdate;
    String currentenddate;
    String currentsubject;
    String currentyear;
    String currentsem;
    String currentcourse;
    String currentteacher;
    TextView teachername;
    TextView subjectname ;
    TextView coursename ;
    TextView yearno ;
    CustomAdapter customAdapter;
    int totalstudent=0;

    static String[][] studentsarr ;
    static String[][] attendancarray;
    int totallecture=0;


    public class ConnectToDB extends AsyncTask<String,Void,Boolean> {
        Connection connection = null;
        String url = null;
        Statement stmt,stmt2,stmt3;
        ResultSet rs,resultSet = null;
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
                stmt2 = connection.createStatement();
                stmt3 = connection.createStatement();

                Log.i("sadasd", "asdfaf");
                getandsetcurrentdetails();
                getandsetrollnonamepercent();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }//doInBackground;


    public void getandsetcurrentdetails(){

       currentteacherid =((Integer)sharedPreferences.getInt("currentUserId",0));
       Log.i("teacher id ",""+currentteacherid);
        currentstartdate =((String)sharedPreferences.getString("currentstartdate","no date"));
        currentenddate =((String)sharedPreferences.getString("currentenddate","no date"));
        currentsubject =((String)sharedPreferences.getString("currentsubjectname","no date"));
        currentyear =((String)sharedPreferences.getString("currentyear","no date"));
        currentsem =((String)sharedPreferences.getString("currentsem","no date"));
        currentcourse =((String)sharedPreferences.getString("currentcoursename","no date"));


        try{
            rs = stmt.executeQuery("select teacherName from Teacher where teacherId='"+currentteacherid+"'");
            if(rs.next()){
                currentteacher =rs.getString("teacherName") ;

            }
        }catch(Exception e){
            e.printStackTrace();
        }

      teachername.setText(currentteacher);
        subjectname.setText(currentsubject);
        yearno.setText(currentyear);
        coursename.setText(currentcourse);

    }

       public void  getandsetrollnonamepercent(){
                    try{
                        int i=0;

                        rs = stmt.executeQuery("select count(*) as totlastudent from Student where fksemIdStudent=(select semId from Semester where semName='"+currentsem+"')");
                        if(rs.next()){
                         totalstudent =rs.getInt("totlastudent");


                        }
                        studentsarr = new String[totalstudent][4];//index:value->00:srno,01:rollno,02:name,03:perc


                        rs = stmt.executeQuery("select count(*) as totallecture from Attendance where takenDate between '"+currentstartdate+"' and '"+currentenddate+"' and fksubjectId=(select subjectId from Subject where subjectName='"+currentsubject+"') and fkstudentErpNo=(select studentErpNo from Student where studentRollNo='"+16+"')");
                        if(rs.next()){
                            totallecture =(Integer)rs.getInt("totallecture");

                        }

                        attendancarray=new String[totalstudent+2][totallecture+1];//+2 for taken date and taken time ,+1 for perc


                        rs = stmt.executeQuery("select studentRollNo,studentName from Student where fksemIdStudent=(select semId from Semester where semName='"+currentsem+"') order by studentRollNo");

                        while(rs.next()){



                          String studentRollNo =rs.getString("studentRollNo");
                                    studentsarr[i][0]=Integer.toString(i+1);
                                    studentsarr[i][1]=rs.getString("studentRollNo");
                                    studentsarr[i][2]=rs.getString("studentName");
                          int totalpresent=0;
                         resultSet = stmt2.executeQuery("select count(*) as totalpresent from Attendance where( (takenDate between '"+currentstartdate+"' and '"+currentenddate+"') and( fkstudentErpNo=(select studentErpNo from Student where studentRollNo='"+studentRollNo+"'))and( presentabsent='P') and (fksubjectId=(select subjectId from Subject where subjectName='"+currentsubject+"')))");
                            if(resultSet.next()){
                                totalpresent =(Integer)resultSet.getInt("totalpresent");

                            }
                            float percent=0;
                            Log.i("total lecture ",""+totallecture);
                            Log.i("total present ",""+totalpresent);
                            if(totallecture!=0){
                                percent =(100*totalpresent)/totallecture;
                                studentsarr[i][3]=Float.toString(percent);
                                attendancarray[i+2][totallecture]=Float.toString(percent);


                            }else
                            {

                                studentsarr[i][3]="No Lectures";


                            }

                            //get attendance of all lectures of particular student
                            int days=0;
                            ResultSet resultSetForAttendance=stmt3.executeQuery("select takenDate,presentabsent,convert(varchar, takenTime, 8) as takenTime from Attendance where takenDate between '"+currentstartdate+"' and '"+currentenddate+"' and fkstudentErpNo=(select studentErpNo from Student where studentRollNo="+studentsarr[i][1]+") and fksubjectId=(select subjectId from Subject where subjectName='"+currentsubject+"' and fksemIdSubject=(select fksemIdStudent from Student where studentErpNo=(select studentErpNo from Student where studentRollNo="+studentsarr[i][1]+"))) order by takenDate asc");
                            while(resultSetForAttendance.next()){
                                if(i==0)
                                {
                                    attendancarray[0][days]=resultSetForAttendance.getString("takenTime");
                                    attendancarray[1][days]=resultSetForAttendance.getDate("takenDate")+"";
                                    Log.i("datestaken",attendancarray[0][days]+" _"+attendancarray[1][days]);
                                }

                                attendancarray[i+2][days]=resultSetForAttendance.getString("presentabsent");//i+2=>index 0 and 1 stores the date and time of the lecture
                                Log.i("days present",attendancarray[i+2][days]);
                                days++;

                            }






                            i++;


                            }

                        listView.setAdapter(customAdapter);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

        }


    }//AsyncTask


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_search_result);
        Log.i("new activity","teacher search result");

        sharedPreferences=this.getApplicationContext().getSharedPreferences("om.example.bino.attendance",MODE_PRIVATE);

        teachername =(TextView) findViewById(R.id.textView2);
        subjectname =(TextView) findViewById(R.id.textView3);
        coursename =(TextView) findViewById(R.id.textView5);
        yearno =(TextView) findViewById(R.id.textView6);
        listView=(ListView)findViewById(R.id.listView);
        download=(Button)findViewById(R.id.download);
        customAdapter = new CustomAdapter();



        ConnectToDB connectToDB = new ConnectToDB();

        String[] sql={

        };

        try {
            if(connectToDB.execute(sql).get()){
                {
                    Log.i("updated:mmmmm","doneee");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent teacherSearchResultIndividual = new Intent(getApplicationContext(), TeacherSearchResultIndividual.class);
                teacherSearchResultIndividual.putExtra("passStudentRoll",studentsarr[i][1]);
                teacherSearchResultIndividual.putExtra("passStudentName",studentsarr[i][2]);

                teacherSearchResultIndividual.putExtra("totallecture",totallecture);
                startActivity(teacherSearchResultIndividual);
            }
        });

        Log.i("data",""+attendancarray[0][0]+"__"+attendancarray[1][0]);

    }

    public void download(View view){
        WritableCell makeCellRed;
        WritableCellFormat newFormat;

        if(view.getTag().equals("not downloaded")) {
            view.setTag("downloaded");
            view.setBackgroundColor(Color.rgb(105,105,105));
            String filename = "/Download/" + currentsubject + ".xls";

            File f1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);


            try {
                WritableWorkbook myexcel = Workbook.createWorkbook(f1);
                WritableSheet sheet1 = myexcel.createSheet(currentstartdate+"_to_"+currentenddate, 0);

                Label l1;
                l1 = new Label(0, 0, "Date Taken");
                sheet1.addCell(l1);
                l1 = new Label(0, 1, "Time Taken");
                sheet1.addCell(l1);

                l1 = new Label(totallecture + 2, 2, "Percentage");
                sheet1.addCell(l1);


                for (int i = 0; i < totalstudent; i++) {

                    l1 = new Label(0, i + 2, studentsarr[i][1]);
                    sheet1.addCell(l1);
                    l1 = new Label(1, i + 2, studentsarr[i][2]);
                    sheet1.addCell(l1);
                    for (int j = 0; j < totallecture; j++) {

                        CellView cell = sheet1.getColumnView(j);
                        cell.setSize(3000);
                        sheet1.setColumnView(j, cell);

                        Log.i("second for j", "" + j);
                        if (i == 0) {
                            Log.i("second for if i==0", "" + i);
                            l1 = new Label(j + 2, 0, attendancarray[0][j]);
                            sheet1.addCell(l1);
                            Log.i("secattendancarray[0][j]", "" + attendancarray[0][j]);
                            l1 = new Label(j + 2, 1, attendancarray[1][j]);
                            sheet1.addCell(l1);
                            Log.i("secattendancarray[0][j]", "" + attendancarray[0][j]);
                        }
                        l1 = new Label(j + 2, i + 2, attendancarray[i + 2][j]);
                        sheet1.addCell(l1);
                        if(Float.parseFloat(attendancarray[i+2][totallecture])<75.0) {
                            makeCellRed = sheet1.getWritableCell(totallecture+2, i + 2);

                            newFormat = new WritableCellFormat();

                            newFormat.setBackground(Colour.RED);

                            makeCellRed.setCellFormat(newFormat);
                        }
                        Log.i("secattendancarray[i][j]", "" + attendancarray[i + 2][j]);

                    }
                    l1 = new Label(totallecture + 2, i + 2, attendancarray[i + 2][totallecture]);

                    sheet1.addCell(l1);


                    CellView cell0 = sheet1.getColumnView(0);
                    cell0.setSize(3000);
                    sheet1.setColumnView(0, cell0);
                    CellView cell1 = sheet1.getColumnView(1);
                    cell1.setSize(10000);
                    sheet1.setColumnView(1, cell1);
                    CellView cellsecondlast = sheet1.getColumnView(totallecture);
                    cellsecondlast.setSize(3000);
                    sheet1.setColumnView(totallecture, cellsecondlast);
                    CellView celllast = sheet1.getColumnView(totallecture + 1);
                    celllast.setSize(3000);
                    sheet1.setColumnView(totallecture + 1, celllast);
                    CellView cellperc = sheet1.getColumnView(totallecture + 2);
                    cellperc.setSize(3000);
                    sheet1.setColumnView(totallecture + 2, cellperc);
                }


                myexcel.write();
                myexcel.close();

                Toast.makeText(this, "Saved to Downloads", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
                Toast.makeText(this, "Give storage permissions", Toast.LENGTH_SHORT).show();
            }
        }else
        {
            Toast.makeText(this, "Already downloaded", Toast.LENGTH_SHORT).show();
        }
    }


    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return studentsarr.length;
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
            view = getLayoutInflater().inflate(R.layout.customlayoutteachersearchdisplay, null);
            TextView textViewSrno = (TextView) view.findViewById(R.id.srno);
            TextView textViewRollno = (TextView) view.findViewById(R.id.rollno);
            TextView textViewName = (TextView) view.findViewById(R.id.name);
            TextView textViewPercentage = (TextView) view.findViewById(R.id.percentage);
            textViewSrno.setText(studentsarr[i][0]);
            textViewRollno.setText(studentsarr[i][1]);
            textViewName.setText(studentsarr[i][2]);
            textViewPercentage.setText(studentsarr[i][3]);

            return view;
        }
    }
}
