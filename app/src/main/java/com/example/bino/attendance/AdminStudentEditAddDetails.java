package com.example.bino.attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminStudentEditAddDetails extends AppCompatActivity {
    Intent intent;
    EditText serpNo,srollno,sname,spassword,sDOB,semail,sgender,scontactno,scoursename,ssemestername;
    Button saveDetail,editButton;
    ProgressDialog progressdialog;
    Connection connection = null;
    String url = null;
    Statement stmt;
    ResultSet rs= null;
    String sql = "";
    Handler handler=new Handler();


    public void StudentDetaileSave(View view){
        Thread saveToDBThread=new Thread(new Runnable() {
            @Override
            public void run() {
                if(saveDetail.getTag().toString().equals("edit student")){
                sql = "update Student set studentRollNo=" + srollno.getText().toString() + " ,studentName='" + sname.getText().toString() + "' ,studentPassword ='" + spassword.getText().toString() + "',studentEmailId='" + semail.getText().toString() + "'  ,studentGender='" + sgender.getText().toString() + "',studentrContactNo=" + scontactno.getText().toString() + " where studentErpNo=" + serpNo.getText().toString();
                Log.i("sql update",sql);
                try {
                    stmt.executeUpdate(sql);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AdminStudentEditAddDetails.this, "Saved", Toast.LENGTH_SHORT).show();
                            }
                        });

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                viewMode();
             }
                else{
                    sql = "insert into Student values("+ serpNo.getText().toString()+","+srollno.getText().toString()+",'"+sname.getText().toString()+"','"+spassword.getText().toString()+"',"+semail.getText().toString()+"','"+sgender.getText().toString()+"','"+scontactno.getText().toString()+",select courseId from Course where courseName='"+scoursename.getText().toString()+"',select semId from Semester where semName='"+ssemestername.getText().toString()+"'";
                    Log.i("sql save",sql);
                    try {
                        stmt.executeUpdate(sql);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AdminStudentEditAddDetails.this, "Saved", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    viewMode();
                }
            }


        });
        saveToDBThread.start();

        Intent adminstudentseachresultactivity = new Intent(getApplicationContext(), AdminStudentSeachResultActivity.class);
        startActivity(adminstudentseachresultactivity);
    }


    public void editstudentFunction(View view){
        saveDetail.setTag("edit student");
        editMode();
    }

    public void initializeAllViews(){
        Thread initializeThread =new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                serpNo=(EditText)findViewById(R.id.StudentErpNoShowAddEditText);
                srollno=(EditText)findViewById(R.id.StudentRollNoShowAddEditText);
                sname=(EditText)findViewById(R.id.StudentNameShowAddEditText);
                spassword=(EditText)findViewById(R.id.StudentPasswordShowAddEditText);
                sDOB=(EditText)findViewById(R.id.StudentDOBShowAddEditText);
                semail=(EditText)findViewById(R.id.StudentEmailShowAddEditText);
                sgender=(EditText)findViewById(R.id.StudentGenderShowAddEditText);
                scontactno=(EditText)findViewById(R.id.StudentContactShowAddEditText);
                scoursename=(EditText)findViewById(R.id.StudentCourseNameShowAddEditText);
                ssemestername=(EditText)findViewById(R.id.StudentSemesterNameShowAddEditText);
                saveDetail=(Button)findViewById(R.id.saveStudentEditDetails) ;
                editButton=(Button)findViewById(R.id.editStudent);
                viewMode();
               try {
                   Class.forName("net.sourceforge.jtds.jdbc.Driver");
                   url = "jdbc:jtds:sqlserver://androidattendancedbserver.database.windows.net:1433;DatabaseName=AndroidAttendanceDB;user=AlbinoAmit@androidattendancedbserver;password=AAnoit$321;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                   connection = DriverManager.getConnection(url);
                   stmt = connection.createStatement();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }




                intent=getIntent();
                String temp=intent.getStringExtra("flag");
                if(temp.equals("new student"))
                {
                    addNewStudent();
                }else{
                    sql = "select studentErpNo ,studentRollNo ,studentName ,studentPassword ,studentEmailId  ,studentGender,studentrContactNo from Student,Course,Semester where studentRollNo="+intent.getStringExtra("currentRollNo")+" and studentName='"+intent.getStringExtra("currentStudentName")+"' and semId=fksemIdStudent and courseId=fkcourseIdStudent";
                    try {
                        rs=stmt.executeQuery(sql);
                        if (rs.next()) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        serpNo.setText(rs.getString("studentErpNo"));
                                        srollno.setText(rs.getString("studentRollNo"));
                                        sname.setText(rs.getString("studentName"));
                                        spassword.setText(rs.getString("studentPassword"));
                                        sDOB.setText("null");
                                        semail.setText(rs.getString("studentEmailId"));
                                        sgender.setText(rs.getString("studentGender"));
                                        scontactno.setText(rs.getString("studentrContactNo"));
                                        scoursename.setText("MCA");
                                        ssemestername.setText("SEM 4");
                                        progressdialog.dismiss();
                                    }
                                    catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        initializeThread.start();

    }

    public void viewMode(){
        Thread viewModeThread =new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        saveDetail.setVisibility(View.INVISIBLE);
                        serpNo.setEnabled(false);
                        srollno.setEnabled(false);
                        sname.setEnabled(false);
                        spassword.setEnabled(false);
                        sDOB.setEnabled(false);
                        semail.setEnabled(false);
                        sgender.setEnabled(false);
                        scontactno.setEnabled(false);
                        scoursename.setEnabled(false);
                        ssemestername.setEnabled(false);
                    }
                });

            }
        });
        viewModeThread.start();

    }

    public void editMode(){
        Thread viewModeThread =new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        saveDetail.setVisibility(View.VISIBLE);
                        serpNo.setEnabled(false);
                        srollno.setEnabled(true);
                        sname.setEnabled(true);
                        spassword.setEnabled(true);
                        sDOB.setEnabled(true);
                        semail.setEnabled(true);
                        sgender.setEnabled(true);
                        scontactno.setEnabled(true);
                        scoursename.setEnabled(false);
                        ssemestername.setEnabled(false);
                    }
                });

            }
        });
        viewModeThread.start();

    }

    public void addNewStudent(){
        Thread addNewStudentThread =new Thread(new Runnable() {
             int countid;
            @Override
            public void run() {
                try {
                    rs = stmt.executeQuery("select top 1 studentErpNo from Student order by studentErpNo desc ");
                    if (rs.next()) {
                        countid = rs.getInt("studentErpNo") + 1;
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        saveDetail.setTag("save student");
                        editButton.setVisibility(View.INVISIBLE);
                        editMode();
                        serpNo.setText(""+countid);
                        srollno.setText("");
                        sname.setText("");
                        spassword.setText("");
                        sDOB.setText("");
                        semail.setText("");
                        sgender.setText("");
                        scontactno.setText("");
                        scoursename.setText(intent.getStringExtra("currentCourse"));
                        ssemestername.setText(intent.getStringExtra("currentSem"));
                        progressdialog.dismiss();

                    }
                });
            }
        });
        addNewStudentThread.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_edit_add_details);

        progressdialog = new ProgressDialog(AdminStudentEditAddDetails.this);
        progressdialog.setMessage("Just a moment....");
        progressdialog.setCancelable(false);
        progressdialog.show();
        Thread firstThreadToInitialize=new Thread(new Runnable() {
            @Override
            public void run() {
                initializeAllViews();
            }
        });
        firstThreadToInitialize.start();



    }
}
