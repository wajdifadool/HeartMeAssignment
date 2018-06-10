package com.tests.wajde.heartmeassignment.data_manager;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.tests.wajde.heartmeassignment.data_manager.Model.Attribuites.ACTION_RESP;
import static com.tests.wajde.heartmeassignment.data_manager.Model.Attribuites.KEY_RESULT;

public class DownloadDataService extends IntentService {

    public static final String url =
            "https://s3.amazonaws.com/s3.helloheart.home.assignment/bloodTestConfig.json";

    public DownloadDataService() {
        super("DONLOADING SERVICE");
    }

    /* will be called asynchronously meaning it wont block the Main UI-Thread*/
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        getJSONFromUrl(url);
    }

    public void getJSONFromUrl(String url) {
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
                //System.out.println(line);
            }
            is.close();
            json = sb.toString();
            //System.out.println(json);

        } catch (Exception e) {
            //buffering error
            e.printStackTrace();
        }

        // serialization
        try {
            jObj = new JSONObject(json);
            JSONArray jsonArray = jObj.getJSONArray("bloodTestConfig");

            Map<String, Integer> map = new HashMap<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String name = jsonObject.getString("name");
                int threshold = jsonObject.getInt("threshold");
                map.put(name, threshold);
                Model.getINSTANCE().setDataSetasMap(map);
            }

            notifyReciver(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }}

    private void notifyReciver(Boolean status) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(KEY_RESULT, status); //
        sendBroadcast(broadcastIntent);
    }


}//[End of DownloadDataService.java]
