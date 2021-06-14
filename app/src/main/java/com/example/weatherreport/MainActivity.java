package com.example.weatherreport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;
    public void onclick(View view){
        downloadTask task=new downloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() + "&appid=4218cc184a87f6e2327f1a0703a36741\n");
        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView);
        editText=findViewById(R.id.editText);
    }
    public class downloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlconnection=null;
            try {
                url=new URL(urls[0]);
                urlconnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlconnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char curr=(char)data;
                    result+=curr;
                    data=reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject= null;
            try {
                jsonObject = new JSONObject(s);
                String weatherinfo=jsonObject.getString("weather");
                Log.i("weather",weatherinfo);
                JSONArray arr=new JSONArray(weatherinfo);
                String message="";
                for(int i=0;i<arr.length();i++){
                    JSONObject part=arr.getJSONObject(i);
                    String main=part.getString("main");
                    String description=part.getString("description");
                    if(!main.equals("") && !description.equals("")){
                        message+=main + ":" + description + "\r\n";
                    }
                    if(!message.equals("")){
                        textView.setText(message);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "weather not found:(", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "weather not found:(", Toast.LENGTH_SHORT).show();
            }

        }
    }
}