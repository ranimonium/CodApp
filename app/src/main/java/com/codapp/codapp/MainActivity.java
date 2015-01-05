package com.codapp.codapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.CallLog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Date;


public class MainActivity extends ActionBarActivity {
    public final static String CALLSLOG = "com.codapp.codapp.MESSAGE";
    public final static String MSGSLOG = "com.codapp.codapp.MESSAGE";

    private String callslog = "";
    private String msgslog = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            getCalls();
            getMsgs();
        } catch (Exception e){
            System.out.println("ERROR: " + e);
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

    public void getCalls() throws Exception {

        File callsFile = new File( Environment.getExternalStorageDirectory().getPath().toString() + "/callsLog.txt");
        callsFile.createNewFile();

        System.out.println("PATH: " + callsFile.getPath());

        PrintWriter callsPrinter = new PrintWriter(callsFile);

        StringBuilder sb = new StringBuilder();

        Cursor cursor = getContentResolver().query( CallLog.Calls.CONTENT_URI,null, null,null, null);
        cursor.moveToFirst();

        int number = cursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = cursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = cursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex( CallLog.Calls.DURATION);

        // comma-separated column labels
        sb.append("phNumber,dircode,callDayTime,duration" + "\n");

        while ( cursor.moveToNext() ) {
            String phNumber = cursor.getString( number );
            String callType = cursor.getString( type );
            String callDate = cursor.getString( date );
            Time t = new Time();
            t.set(Long.valueOf(callDate));
            String callDayTime = t.format("%Y:%m:%d %H:%M:%S");
            String callDuration = cursor.getString( duration );
            int dircode = Integer.parseInt( callType );

            sb.append(phNumber + ',' + dircode + ',' + callDayTime + ',' + callDuration + '\n');

        }
        cursor.close();
        callsPrinter.write(sb.toString());
        System.out.println(sb);

        callslog = sb.toString();
    }

    public void getMsgs() throws Exception{
        File msgsFile = new File( Environment.getExternalStorageDirectory().getPath().toString() + "/msgsLog.txt");
        msgsFile.createNewFile();

        System.out.println("PATH: " + msgsFile.getPath());

        PrintWriter msgsPrinter = new PrintWriter(msgsFile);

        StringBuilder sb = new StringBuilder();

        Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);

        sb.append("number,type,date,body"+"\n");

        int bodyInt = cursor.getColumnIndex("body");
        int addressInt = cursor.getColumnIndex("address");
        int dateInt = cursor.getColumnIndex("date");
        int typeInt = cursor.getColumnIndex("type");

        while ( cursor.moveToNext() ) {
            String body = cursor.getString(bodyInt);
            String number = cursor.getString(addressInt);
            String date = cursor.getString(dateInt);
            Time t = new Time();
            t.set(Long.valueOf(date));
            String smsDayTime = t.format("%Y:%m:%d %H:%M:%S");
            String type = cursor.getString(typeInt);
            //1 inbox; 2 sent; 3 draft

            sb.append(number + "," + type + "," + smsDayTime + "," + body + "\n");

        }
        cursor.close();
        msgsPrinter.write(sb.toString());
        System.out.println(sb);

        msgslog = sb.toString();
    }

    public void showCalls(View view){

    }

    public void showMsgs(View view){

    }
}
