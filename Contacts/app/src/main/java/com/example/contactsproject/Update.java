package com.example.contactsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Update extends AppCompatActivity {
    protected String ID;
    protected EditText euName, euTel, euInfo, euCategory;
    protected Button btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        euName=findViewById(R.id.euName);
        euTel=findViewById(R.id.euTel);
        euInfo=findViewById(R.id.euInfo);
        euCategory=findViewById(R.id.euCategory);
        btnUpdate=findViewById(R.id.btnUpdate);
        btnDelete=findViewById(R.id.btnDelete);

        Bundle b=getIntent().getExtras();
        if(b!=null){
            ID=b.getString("ID");
            euName.setText(b.getString("name"));
            euTel.setText(b.getString("tel"));
            euInfo.setText(b.getString("info"));
            euCategory.setText(b.getString("category"));
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=null;
                try{
                    db=SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath()+"/contacts.db",
                            null

                    );
                    String name=euName.getText().toString();
                    String tel=euTel.getText().toString();
                    String info=euInfo.getText().toString();
                    String category=euCategory.getText().toString();
                    String s="UPDATE CONTACTS SET name=?, tel=?, info=?, category=? WHERE ";
                    s+="ID=? ";


                    db.execSQL(s, new Object[]{name, tel, info, category, ID});
                    Toast.makeText(getApplicationContext(),
                            "Record UPDATED",
                            Toast.LENGTH_LONG
                    ).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                }finally {
                    if(db!=null){
                        db.close();
                    }
                }

                finishActivity(200);

                Intent i=new Intent(Update.this, MainActivity.class);
                startActivity(i);


            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=null;
                try{
                    db=SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath()+"/contacts.db",
                            null

                    );
                    String s="DELETE FROM CONTACTS WHERE ";
                    s+="ID=? ";


                    db.execSQL(s, new Object[]{ID});
                    Toast.makeText(getApplicationContext(),
                            "Record DELETED",
                            Toast.LENGTH_LONG
                    ).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                }finally {
                    if(db!=null){
                        db.close();
                    }
                }

                finishActivity(200);

                Intent i=new Intent(Update.this, MainActivity.class);
                startActivity(i);


            }
        });


    }

}