package com.example.contactsproject;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected EditText editName, editTel, editInfo, editCategory;
    protected Button btnInsert;
    // protected TextView viewResults;
    protected ListView simpleList;

    protected void initDB() throws SQLException {
        SQLiteDatabase db=null;
        db=SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath()+"/contacts.db",
                null

        );
        String q="CREATE TABLE if not exists CONTACTS( ";
        q+="ID integer primary key AUTOINCREMENT, ";
        q+="name text not null, ";
        q+="tel text not null, ";
        q+="info text not null, ";
        q+="category text not null, ";
        q+="unique(name, tel) ); ";
        db.execSQL(q);
        db.close();



    }

    public void selectDB() throws SQLException{
        SQLiteDatabase db=null;
        db=SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath()+"/contacts.db",
                null

        );

        String q="SELECT * FROM CONTACTS ORDER BY name; ";
        Cursor c=db.rawQuery(q, null);
        StringBuilder sb=new StringBuilder();
        ArrayList<String> listResults=new ArrayList<String>();
        while (c.moveToNext()){
            String name=c.getString(c.getColumnIndex("name"));
            String tel=c.getString(c.getColumnIndex("tel"));
            String info=c.getString(c.getColumnIndex("info"));
            String categor=c.getString(c.getColumnIndex("category"));
            String ID=c.getString(c.getColumnIndex("ID"));
            //sb.append(name+"\t"+email+"\t"+tel+"\n");
            listResults.add(ID+"\t"+name+"\t"+tel+"\t"+info+"\t"+categor+"\n");
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.activity_listview,
                R.id.textView,
                listResults
        );
        simpleList.setAdapter(arrayAdapter);
        db.close();
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try {
            selectDB();
        }catch (Exception e){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName=findViewById(R.id.editName);
        editTel=findViewById(R.id.editTel);
        editInfo=findViewById(R.id.editInfo);
        editCategory=findViewById(R.id.editCategory);
        btnInsert=findViewById(R.id.btnInsert);
        simpleList=findViewById(R.id.simpleListView);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected="";
                TextView clickedText=view.findViewById(R.id.textView);
                selected=clickedText.getText().toString();

                String[] elements=selected.split("\t");
                String ID=elements[0];
                Toast.makeText(getApplicationContext(), ID, Toast.LENGTH_LONG).show();

                Intent intent=new Intent(MainActivity.this, Update.class);

                Bundle b=new Bundle();
                b.putString(
                        "ID", ID
                );
                b.putString("name", elements[1]);
                b.putString("tel", elements[2]);
                b.putString("info", elements[3]);
                b.putString("category", elements[4]);
                intent.putExtras(b);
                startActivityForResult(intent, 200, b);



            }
        });


        try{
            initDB();
            selectDB();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_LONG
            ).show();
        }

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=null;
                try{
                    db=SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath()+"/contacts.db",
                            null

                    );
                    String name=editName.getText().toString();
                    String tel=editTel.getText().toString();
                    String info=editInfo.getText().toString();
                    String category=editCategory.getText().toString();
                    String s="INSERT INTO CONTACTS(name, tel, info, category) ";
                    s+=" VALUES(?, ?, ?, ?);";
                    db.execSQL(s, new Object[]{name, tel, info, category});
                    Toast.makeText(getApplicationContext(),
                            "Record Inserted",
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
                try {
                    selectDB();
                }catch (Exception e){

                }
            }
        });




    }

}