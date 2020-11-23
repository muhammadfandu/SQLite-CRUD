package com.android.sqlite_crud;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class OnLongClickListenerStudentData implements View.OnLongClickListener {

    Context context;
    String id;

    @Override
    public boolean onLongClick(View v){
        context = v.getContext();
        id = v.getTag().toString();

        final CharSequence[] items = {"Edit", "Hapus"};

        new AlertDialog.Builder(context)
                .setTitle("Data mahasiswa")
                .setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    editRecord(Integer.parseInt(id));
                }else if(which == 1){
                    boolean deleteSuccessful = new TableControllerStudent(context).delete(Integer.parseInt(id));

                    if(deleteSuccessful){
                        Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Tidak dapat menghapus data", Toast.LENGTH_SHORT).show();
                    }
                    ((MainActivity) context).countRecords();
                    ((MainActivity) context).readRecords();
                }
                dialog.dismiss();
            }
        }).show();

        return false;
    }

    public void editRecord(int studentId) {
        final TableControllerStudent tableControllerStudent = new TableControllerStudent(context);

        StudentData studentData = tableControllerStudent.readSingleRecord(studentId);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementView = inflater.inflate(R.layout.data_input_form, null, false);

        final EditText edtName = (EditText) formElementView.findViewById(R.id.edtName);
        final EditText edtMail = (EditText) formElementView.findViewById(R.id.edtMail);

        edtName.setText(studentData.name);
        edtMail.setText(studentData.mail);

        new AlertDialog.Builder(context)
                .setView(formElementView)
                .setTitle("Edit data")
                .setPositiveButton("Simpan",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StudentData studentData = new StudentData();

                                studentData.id = studentId;
                                studentData.name = edtName.getText().toString();
                                studentData.mail = edtMail.getText().toString();

                                boolean updateSuccessful = tableControllerStudent.update(studentData);
                                if(updateSuccessful){
                                    Toast.makeText(context, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Tidak dapat memperbarui data", Toast.LENGTH_SHORT).show();
                                }

                                ((MainActivity) context).countRecords();
                                ((MainActivity) context).readRecords();

                                dialog.cancel();
                            }
                        }).show();
    }

}
