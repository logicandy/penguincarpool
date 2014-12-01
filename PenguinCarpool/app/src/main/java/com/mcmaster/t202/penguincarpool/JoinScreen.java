package com.mcmaster.t202.penguincarpool;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// Array of option --> ArrayAdapter --> ListView

// List view: {views: available_taxis.xml}


public class JoinScreen extends LoginScreen {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_screen);
        findViewById(R.id.rand_btn).setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onClick(View arg0) {
        Button rand_btn = (Button) findViewById(R.id.rand_btn);
        rand_btn.setClickable(false);
        //execute get and post
        new JoinGetIO().execute();
    }
  //  public String[] taxi_loc;
    public class JoinGetIO extends AsyncTask<Void, Void, String> {
        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);
                if (n > 0) out.append(new String(b, 0, n));
            }
            return out.toString();
        }
        public ArrayAdapter<String> adapter;

        // @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... params) {
            HttpContext localContext = new BasicHttpContext();
            //get available taxis
            HttpGet httpGet = new HttpGet("http://172.17.31.169/penguin-carpool/public/carpool");
            String text = null;
            try {
                HttpResponse response2 = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response2.getEntity();
                text = getASCIIContentFromEntity(entity);

                JSONObject text_obj = new JSONObject(text);
                JSONArray jsonArray = text_obj.getJSONArray("taxi");
                int len = jsonArray.length();
                String taxi_loc[] = new String[len];
                //user array
                String user_id[] = new String[len];

                for (int i = 0; i < len; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    taxi_loc[i] = jsonObject.getString("taxi_location");
                    user_id[i]=jsonObject.getString("user_id");
                }

                adapter = new ArrayAdapter<String>(
                        JoinScreen.this,                       // Context for the activity
                        R.layout.available_taxis,   // Layout to use
                        taxi_loc);                     // Items to display

//                Log.d("before",Arrays.toString(taxi_loc));
//                Log.d("after",Arrays.toString(taxi_loc));

                return Arrays.toString(taxi_loc);
            /*
            * taxi_loc is the string array
            * To-do:
            * Populate list view with the array contents - done
            * Get onclick on a specific item in list view working - done
            * Send message to user who is carpooling that taxi (arka)
            */
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
        }

        protected void onPostExecute(String results) {
            Button rand_btn = (Button) findViewById(R.id.rand_btn);
            rand_btn.setClickable(true);
            ListView list = (ListView) findViewById(R.id.joinListView);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                    TextView textview = (TextView) viewClicked;
                    String message = "You clicked # " + position + " which is string: " + textview.getText().toString();
                    Toast.makeText(JoinScreen.this, message, Toast.LENGTH_LONG).show();
//                    HttpPost httpPost = new HttpPost("http://172.17.31.169/penguin-carpool/public/message");
//                    //Post Data
//                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
//                    nameValuePair.add(new BasicNameValuePair("taxi_location", textview.getText().toString()));
//
//                    //Encoding POST data
//                    try {
//                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
//                    } catch (UnsupportedEncodingException e) {
//                        // log exception
//                        e.printStackTrace();
//                    }
//
//                    //making POST request.
//                    try {
//                        HttpResponse response = httpClient.execute(httpPost);
//                        // write response to log
//                        Log.d("Http Post Response:", response.toString());
//
//                    } catch (ClientProtocolException e) {
//                        // Log exception
//                        Log.d("Client Exception", e.toString());
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        // Log exception
//                        Log.d("IO Exception", e.toString());
//                        e.printStackTrace();
//                    }
//                    startActivity(new Intent(JoinScreen.this, MapScreen.class));
                }
            });
        }


    }







}