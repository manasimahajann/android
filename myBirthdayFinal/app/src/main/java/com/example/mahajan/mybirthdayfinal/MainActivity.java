package com.example.mahajan.mybirthdayfinal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    int day;
    int month;
    int year;
    EditText editText;
    EditText editText2;

    myDatabaseHelper db =  null;
    String nameFriend;
    String birthdayFriend;
    long phonenoFriend;

    Button saveButton;

    int thisDay;
    int thisMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new myDatabaseHelper(this);

        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        thisDay = datePicker.getDayOfMonth();
        thisMonth = datePicker.getMonth()+1;

        editText = (EditText)findViewById(R.id.editText);
        editText2 = (EditText)findViewById(R.id.editText2);

        Log.i("today", String.valueOf(thisDay+" "+thisMonth));

        saveButton = (Button)findViewById(R.id.button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("phone",editText2.getText().toString());
                phonenoFriend = Long.parseLong(editText2.getText().toString());


                DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
                day = datePicker.getDayOfMonth();
                month = datePicker.getMonth()+1;
                year = datePicker.getYear();
                nameFriend = editText.getText().toString();
                birthdayFriend = String.valueOf(month+"/"+day+"/"+year);
                db.addBirthday(nameFriend, birthdayFriend, phonenoFriend);
                Log.i("nameBir",nameFriend+" "+birthdayFriend + " "+phonenoFriend);
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Cursor AllBirthdays = db.getFriends();

        AllBirthdays.moveToFirst();
        SmsManager smsManager = SmsManager.getDefault();
        while(!AllBirthdays.isAfterLast()){
            String nameFriend = AllBirthdays.getString(1);
            String birthdayFriend = AllBirthdays.getString(2);
            String noFriend = AllBirthdays.getString(3);
            AllBirthdays.moveToNext();
            Date birthdate = null;
            try {
               birthdate = sdf.parse(birthdayFriend);
                Log.i("birthdate", sdf.format(birthdate));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.i("saved", nameFriend+" "+birthdayFriend+" "+noFriend);
            if(thisDay == birthdate.getDay() && thisMonth == birthdate.getMonth()+1) {

                Log.i("matched", String.valueOf(birthdate) + " " + noFriend);
                String sms = "Happy Birthday " + nameFriend;

                if (noFriend != null && !noFriend.isEmpty()) {

                    smsManager.sendTextMessage(noFriend, null, sms, null, null);
                }
            }
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
