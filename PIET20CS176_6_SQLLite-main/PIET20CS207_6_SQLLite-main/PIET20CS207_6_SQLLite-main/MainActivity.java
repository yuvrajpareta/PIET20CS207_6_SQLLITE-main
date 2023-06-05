package com.example.sqlite;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
public class MainActivity extends AppCompatActivity{
    EditText name,regno,mark;
    Button btnAdd,btnDelete,btnUpdate,btnView,btnViewAll;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        regno= (EditText)findViewById(R.id.editText1);
        name= (EditText)findViewById(R.id.editText2);
        mark=(EditText)findViewById(R.id.editText3);
        btnAdd=(Button)findViewById(R.id.button1);
        btnView=(Button)findViewById(R.id.button2);
        btnViewAll=(Button)findViewById(R.id.button3);
        btnUpdate=(Button)findViewById(R.id.button4);
        btnDelete=(Button)findViewById(R.id.button5);
        db=openOrCreateDatabase("Students", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(regno VARCHAR,name VARCHR,mark VARCHAR);");

        btnAdd.setOnClickListener(new OnClickListener()
        {
            public void onClick(View arg0) {
                if(regno.getText().toString().trim().length()==0||name.getText().toString().trim().length()==0||mark.getText().toString().trim().length()==0)
                {
                    showMessage("Error", "Please enter all values");
                    return;
                }
                db.execSQL("INSERT INTO student VALUES('"+regno.getText()+"','"+name.getText()+"','"+mark.getText()+"');");
                showMessage("Success", "Record added");
                clearText();
            }
        });
        btnDelete.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v) {
                if(regno.getText().toString().trim().length()==0)
                {
                    showMessage("Error", "Please enter Reg. No.");
                    return;	}
                Cursor c=db.rawQuery("SELECT * FROM student WHERE regno='"+regno.getText()+"'", null);
                if(c.moveToFirst())
                {
                    db.execSQL("DELETE FROM student WHERE regno='"+regno.getText()+"'");
                    showMessage("Success", "Record Deleted");
                }
                else
                {
                    showMessage("Error", "Invalid Reg. No.");
                }
                clearText();
            }
        });
        btnUpdate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (regno.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter Reg. No.");
                    return;
                }
                Cursor c = db.rawQuery("SELECT * FROM student WHERE regno='" + regno.getText() + "'", null);
                if (c.moveToFirst()) {
                    db.execSQL("UPDATE student SET name='" + name.getText() + "',mark='" + mark.getText() + "' WHERE regno='" + regno.getText() + "'");
                }
            }
        });
        btnView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick (View v){
                if (regno.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter Reg. No.");
                    return;
                }
                Cursor c = db.rawQuery("SELECT * FROM student WHERE regno='" + regno.getText() + "'", null);
                if (c.moveToFirst()) {
                    name.setText(c.getString(1));
                    mark.setText(c.getString(2));
                } else {
                    showMessage("Error", "Invalid Reg. No.");
                    clearText();
                }
            }
        });
        btnViewAll.setOnClickListener(new
                                              OnClickListener() {
                                                  @Override
                                                  public void onClick (View v){
                                                      Cursor c = db.rawQuery("SELECT * FROM student", null);
                                                      if (c.getCount() == 0) {
                                                          showMessage("Error", "No records found");
                                                          return;
                                                      }
                                                      StringBuffer buffer = new StringBuffer();
                                                      while (c.moveToNext()) {
                                                          buffer.append("Reg. No : " + c.getString(0) + "\n");
                                                          buffer.append("Name : " + c.getString(1) + "\n");
                                                          buffer.append("Mark : " + c.getString(2) + "\n\n");
                                                      }
                                                      showMessage("Student Details", buffer.toString());
                                                  }
                                              });
    }
    public void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText() {
        regno.setText("");
        name.setText("");
        mark.setText("");
        regno.requestFocus();
    }
};
