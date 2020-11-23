package com.android.sqlite_crud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddData = (Button) findViewById(R.id.btnAddData);

        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getRootView().getContext();

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = inflater.inflate(R.layout.data_input_form, null, false);

                final EditText edtName = (EditText) formElementsView.findViewById(R.id.edtName);
                final EditText edtMail = (EditText) formElementsView.findViewById(R.id.edtMail);

                new AlertDialog.Builder(context)
                        .setView(formElementsView)
                        .setTitle("Tambah Data")
                        .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String name = edtName.getText().toString();
                                String mail = edtMail.getText().toString();

                                StudentData studentData = new StudentData();
                                studentData.name = name;
                                studentData.mail = mail;

                                boolean createSuccessful = new TableControllerStudent(context).create(studentData);

                                if(createSuccessful){
                                    Toast.makeText(context, "Data tersimpan", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Tidak dapat menyimpan data", Toast.LENGTH_SHORT).show();
                                }

                                countRecords();
                                readRecords();

                                dialog.cancel();
                            }
                        }).show();
            }
        });

        countRecords();
        readRecords();
    }

    public void countRecords(){
        int recordCount = new TableControllerStudent(this).count();

        TextView tvRecordCount = (TextView) findViewById(R.id.tvRecordCount);

        tvRecordCount.setText(recordCount + " data tersimpan");;
    }

    public void readRecords(){
        LinearLayout lnRecords = (LinearLayout) findViewById(R.id.lnRecords);

        lnRecords.removeAllViews();

        List<StudentData> students = new TableControllerStudent(this).read();

        if(students.size() > 0){
            for(StudentData obj : students){
                int id = obj.id;
                String name = obj.name;
                String mail = obj.mail;
                String tvContents = name + " - " + mail;

                TextView tvStudentItem = new TextView(this);
                tvStudentItem.setPadding(0,10,0,10);;
                tvStudentItem.setText(tvContents);
                tvStudentItem.setTag(Integer.toString(id));
                tvStudentItem.setOnLongClickListener(new OnLongClickListenerStudentData());

                lnRecords.addView(tvStudentItem);
            }
        }else{
            TextView locationItem = new TextView(this);
            locationItem.setPadding(8,8,8,8);
            locationItem.setText("Belum ada data tersimpan");
            lnRecords.addView(locationItem);
        }
    }
}