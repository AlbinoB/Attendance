package com.example.bino.attendance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    Handler handler =new Handler();


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



    public class SaveFile extends AsyncTask<String,Void,Boolean> {


        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Boolean doInBackground(String... args) {

                Thread thread = new Thread(new Runnable() {


                    @Override
                    public void run() {
                        Looper.prepare();
                        WritableCell makeCellRed;
                        WritableCellFormat newFormat;


                        if(download.getTag().equals("not downloaded")) {


                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                download.setTag("downloaded");
                                download.setBackgroundColor(Color.rgb(105, 105, 105));
                            }
                        });


                        String filename = "/Download/" + currentsubject + ".xls";

                        File f1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);

                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        Date date = new Date();
                        String todaysDate=dateFormat.format(date);

                        try {
                            WritableWorkbook myexcel = Workbook.createWorkbook(f1);
                            WritableSheet sheet1 = myexcel.createSheet(currentstartdate+"_to_"+currentenddate, 0);
                            WritableSheet defaulters = myexcel.createSheet("Defaulters", 1);

                            //All labels are incremented by to to add subj name course subject name, date to date current falculty
                            Label l1;

                            l1 = new Label(0, 0, "Faculty Name:");
                            sheet1.addCell(l1);
                            l1 = new Label(1, 0,currentteacher );
                            sheet1.addCell(l1);

                            l1 = new Label(0, 1, "Department:");
                            sheet1.addCell(l1);
                            l1 = new Label(1, 1, currentcourse+"    Subject:"+currentsubject+"    Semester:"+currentsem);
                            sheet1.addCell(l1);
                            l1 = new Label(3, 0, "Downloaded:");
                            sheet1.addCell(l1);
                            l1 = new Label(4, 0, todaysDate);
                            sheet1.addCell(l1);

                            l1 = new Label(6, 0, "From:");
                            sheet1.addCell(l1);
                            l1 = new Label(7, 0, currentstartdate);
                            sheet1.addCell(l1);
                            l1 = new Label(8, 0, "    to    ");
                            sheet1.addCell(l1);
                            l1 = new Label(9, 0, currentenddate);
                            sheet1.addCell(l1);


                            //defaulter sheet
                            l1 = new Label(0, 0, "Faculty Name:");
                            defaulters.addCell(l1);
                            l1 = new Label(1, 0,currentteacher );
                            defaulters.addCell(l1);

                            l1 = new Label(0, 1, "Department:");
                            defaulters.addCell(l1);
                            l1 = new Label(1, 1, currentcourse+"    Subject:"+currentsubject+"    Semester:"+currentsem);
                            defaulters.addCell(l1);
                            l1 = new Label(3, 0, "Downloaded:");
                            defaulters.addCell(l1);
                            l1 = new Label(4, 0, todaysDate);
                            defaulters.addCell(l1);

                            l1 = new Label(6, 0, "From:");
                            defaulters.addCell(l1);
                            l1 = new Label(7, 0, currentstartdate);
                            defaulters.addCell(l1);
                            l1 = new Label(8, 0, "    to    ");
                            defaulters.addCell(l1);
                            l1 = new Label(9, 0, currentenddate);
                            defaulters.addCell(l1);


                            l1 = new Label(0, 1+2, "Date Taken");
                            sheet1.addCell(l1);
                            l1 = new Label(0, 0+2, "Time Taken");
                            sheet1.addCell(l1);

                            l1 = new Label(totallecture + 2, 1+2, "Percentage");
                            sheet1.addCell(l1);


                            for (int i = 0; i < totalstudent; i++) {

                                l1 = new Label(0, i + 2+2, studentsarr[i][1]);//rollno
                                sheet1.addCell(l1);
                                l1 = new Label(1, i + 2+2, studentsarr[i][2]);//name
                                sheet1.addCell(l1);
                                for (int j = 0; j < totallecture; j++) {

                                    CellView cell = sheet1.getColumnView(j);
                                    cell.setSize(3000);
                                    sheet1.setColumnView(j, cell);

                                    Log.i("second for j", "" + j);
                                    if (i == 0) {
                                        Log.i("second for if i==0", "" + i);
                                        l1 = new Label(j + 2, 1+2, attendancarray[0][j]);
                                        sheet1.addCell(l1);
                                        Log.i("secattendancarray[0][j]", "" + attendancarray[0][j]);
                                        l1 = new Label(j + 2, 0+2, attendancarray[1][j]);
                                        sheet1.addCell(l1);
                                        Log.i("secattendancarray[0][j]", "" + attendancarray[0][j]);
                                    }
                                    l1 = new Label(j + 2, i + 2+2, attendancarray[i + 2][j]);
                                    sheet1.addCell(l1);

                                    l1 = new Label(totallecture + 2, i + 2+2, attendancarray[i + 2][totallecture]);

                                    sheet1.addCell(l1);

                                    if(Float.parseFloat(attendancarray[i+2][totallecture])<75.0) {
                                        l1 = new Label(0, i + 2+2, studentsarr[i][1]);//rollno
                                        defaulters.addCell(l1);
                                        l1 = new Label(1, i + 2+2, studentsarr[i][2]);//name
                                        defaulters.addCell(l1);
                                        l1 = new Label(2, i + 2+2, attendancarray[i + 2][totallecture]);//name
                                        defaulters.addCell(l1);

                                        makeCellRed = sheet1.getWritableCell(totallecture+2, i + 2+2);//colour bug perc column

                                        newFormat = new WritableCellFormat();

                                        newFormat.setBackground(Colour.RED);

                                        makeCellRed.setCellFormat(newFormat);
                                    }
                                    Log.i("secattendancarray[i][j]", "" + attendancarray[i + 2][j]);

                                }


                                CellView rollno = defaulters.getColumnView(0);
                                rollno.setSize(3000);
                                defaulters.setColumnView(0, rollno);
                                CellView name = defaulters.getColumnView(1);
                                name.setSize(10000);
                                defaulters.setColumnView(1, name);
                                CellView perc = defaulters.getColumnView(2);
                                perc.setSize(2200);
                                defaulters.setColumnView(2, perc);
                                CellView downloaded = defaulters.getColumnView(3);
                                downloaded.setSize(3800);
                                defaulters.setColumnView(3, downloaded);
                                CellView downloadedDate = defaulters.getColumnView(5);
                                downloadedDate.setSize(3500);
                                defaulters.setColumnView(5, downloadedDate);
                                CellView startDate = defaulters.getColumnView(7);
                                startDate.setSize(3500);
                                defaulters.setColumnView(7, startDate);
                                CellView endDate = defaulters.getColumnView(9);
                                endDate.setSize(3500);
                                defaulters.setColumnView(9, endDate);




                                CellView sheet1rollno = sheet1.getColumnView(0);
                                sheet1rollno.setSize(3000);
                                sheet1.setColumnView(0, sheet1rollno);
                                CellView sheet1name = sheet1.getColumnView(1);
                                sheet1name.setSize(10000);
                                sheet1.setColumnView(1, sheet1name);
                                CellView sheet1perc = sheet1.getColumnView(2);
                                sheet1perc.setSize(2200);
                                sheet1.setColumnView(2, sheet1perc);
                                CellView sheet1downloaded = sheet1.getColumnView(3);
                                sheet1downloaded.setSize(3800);
                                sheet1.setColumnView(2, sheet1downloaded);
                                CellView sheet1downloadedDate = sheet1.getColumnView(3);
                                sheet1downloadedDate.setSize(3500);
                                sheet1.setColumnView(2, sheet1downloadedDate);
                                CellView sheet1startDate = sheet1.getColumnView(3);
                                sheet1startDate.setSize(3500);
                                sheet1.setColumnView(2, sheet1startDate);
                                CellView sheet1endDate = sheet1.getColumnView(3);
                                sheet1endDate.setSize(3500);
                                sheet1.setColumnView(2, sheet1endDate);
                                CellView cellsecondlast = sheet1.getColumnView(totallecture);
                                cellsecondlast.setSize(3000);
                                sheet1.setColumnView(totallecture, cellsecondlast);
                                CellView celllast = sheet1.getColumnView(totallecture + 1);
                                celllast.setSize(3000);
                                sheet1.setColumnView(totallecture + 1, celllast);
                                CellView cellperc = sheet1.getColumnView(totallecture + 2);
                                cellperc.setSize(2200);
                                sheet1.setColumnView(totallecture + 2, cellperc);
                            }


                            myexcel.write();
                            myexcel.close();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TeacherSearchResult.this, "Saved to Downloads", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();

                        } catch (RowsExceededException e) {
                            e.printStackTrace();
                        } catch (WriteException e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TeacherSearchResult.this, "Give storage permissions", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }else
                    {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TeacherSearchResult.this, "Already downloaded", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }
                    });
                thread.start();



            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            // do UI work here

        }

    }

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
        SaveFile saveFile= new SaveFile();


        String[] sql={

        };

        try {
            if(saveFile.execute(sql).get()){
                {
                    Log.i("updated:mmmmm","doneee");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
