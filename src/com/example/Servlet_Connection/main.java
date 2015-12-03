package com.example.Servlet_Connection;

import android.app.Activity;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Kevin on 12/1/2015.
 */
public class main extends Activity {
    private static final String MY_PREF_NAME ="File" ;
    int[] numbers = new int[5];
    Button StopService;
    Button sendNums;
    Intent intentCallService5;
    BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        StopService =(Button) findViewById(R.id.button3);
        StopService.setEnabled(false);
        sendNums = (Button) findViewById(R.id.button);
        sendNums.setEnabled(false);

        intentCallService5 = new Intent(this, MyService5Async.class);//creates service on create

        IntentFilter filter5 = new IntentFilter("NameOfIntent");//broadcast return numbers in intent

        receiver = new MyEmbeddedBroadcastReceiver();   //calls the broadcast receiver class
        registerReceiver(receiver, filter5);            //with the contents of the intent from the onPost service

    }

    public void sendNums (View v){                          //send numbers button
        JSONObject json = new JSONObject(); //create json object of returned numbers
        try {

            json.put("one", numbers[0]);                         //put numbers in json object with a key
            json.put("two", numbers[1]);
            json.put("three", numbers[2]);
            json.put("four", numbers[3]);
            json.put("five", numbers[4]);
           String baseUrl = "http://10.12.11.250:8080/MinMax";  //send ip port and servlet id

            new HttpAsyncTask().execute(baseUrl, json.toString());// call async task
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void StartServ (View v){
        StopService.setEnabled(true);
        startService(intentCallService5);       //call service
    }
    public void StopService (View v){
        sendNums.setEnabled(true);
        stopService(intentCallService5);        //stop service
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt("one", numbers[0]);
        editor.putInt("two", numbers[1]);
        editor.putInt("three", numbers[2]);
        editor.putInt("four", numbers[3]);
        editor.putInt("five", numbers[4]);
        editor.commit();
        unregisterReceiver(receiver);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String jsonString = "";
            try {
                jsonString = HttpUtils.urlContentPost(urls[0], "num", urls[1]);//sends json object base url key & values
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(jsonString);
            return jsonString;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonResult = null;
            try {
                jsonResult = new JSONObject(result);

                String num1 = jsonResult.getString("min");
                String num2 = jsonResult.getString("max");
                String num3 = jsonResult.getString("sum");

                TextView view1 = (TextView)findViewById(R.id.textView);

                view1.setText("Min is: " +num1);
                view1.append("\nMax is: " +num2);
                view1.append("\nSum is: "+num3);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class MyEmbeddedBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView txt2 = (TextView)findViewById(R.id.textView2);     //text view

            if (intent.getAction().equals("NameOfIntent")) {    //if the key is correct go in
                //String service5data = intent.getStringExtra("KeyForValue");
                numbers = intent.getIntArrayExtra("array");     //use the key array and put values into numbers
                txt2.append("Random Numbers: "+ numbers[0]+", "+ numbers[1]+", "+ numbers[2]+", "+
                numbers[3]+", "+ numbers[4]+"\n");//print numbers to screen
                //txt2.append(""+ service5data);
            }
        }//onReceive
    }// MyEmbeddedBroadcastReceiver
}
