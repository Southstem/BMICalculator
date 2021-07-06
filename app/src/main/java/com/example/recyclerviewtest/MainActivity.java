package com.example.recyclerviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public RecyclerView RE;
    public recyclerViewAdapter adapter;
    public item[] items;
    public boolean flag = false;
    public String resultStr = "";
    public int maxNumb = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        RE = (RecyclerView) findViewById(R.id.recycler);
        RE.setLayoutManager(manager);
        initItems();
        while (items == null) ;
        adapter = new recyclerViewAdapter(items);
        RE.setAdapter(adapter);
    }

    public void initItems() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                item[] itemsHere = new item[maxNumb];
                int k = 0;
                BufferedReader reader;
                StringBuilder jsonData = new StringBuilder();
                HttpURLConnection connection;
                try {
                    URL requestUrl = new URL("https://southstem.cloud/getBmiData");
                    connection = (HttpURLConnection) requestUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(8000);
                    if (connection.getResponseCode() == 200) {
                        InputStream in = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(in));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            jsonData.append(line);
                        }
                    }
                    JSONArray jsonArray = new JSONArray(jsonData.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        item newItemHere = new item(Float.parseFloat(jsonObject.getString("height")), Float.parseFloat(jsonObject.getString("weight")), jsonObject.getString("time"));
                        itemsHere[k] = newItemHere;
                        System.out.println("item ====> " + newItemHere.time + "==>" + itemsHere[k].time);
                        k++;
                    }
                    connection.disconnect();
                } catch (MalformedURLException | ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                items = itemsHere;
            }
        }).start();
    }

    public void submmit(View view) {
        RE.scrollToPosition(0);
        int k = 0;

        EditText weightHereStr = (EditText) findViewById(R.id.weight);
        EditText heightHereStr = (EditText) findViewById(R.id.height);
        String weightHere = weightHereStr.getText().toString();
        String heightHere = heightHereStr.getText().toString();
        float weight = Float.parseFloat(weightHere);
        float height = Float.parseFloat(heightHere);
        SimpleDateFormat formatter = new SimpleDateFormat("  yyyy.MM.dd");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!weightHere.isEmpty() && !heightHere.isEmpty() && flag == false) {
                    try {
                        String uu = "添加记录：" + weightHere + "kg ," + heightHere + "m 成功。";
                        URL reqUrl = new URL("https://southstem.cloud/addNewBmi?height=" + heightHere + "&weight=" + weightHere);
                        HttpURLConnection connection2 = (HttpURLConnection) reqUrl.openConnection();
                        connection2.setRequestMethod("GET");
                        connection2.setConnectTimeout(5000);
                        connection2.setReadTimeout(8000);
                        if (connection2.getResponseCode() == 200) {
                            resultStr = uu;
                        } else {
                            resultStr = "ERROR!";
                        }
                        connection2.disconnect();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ProtocolException protocolException) {
                        protocolException.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                   // adapter.notifyDataSetChanged();
                } else {
                    resultStr = "ERROR";
                }
                flag = true;
            }
        }).start();
        Date curDate = new Date(System.currentTimeMillis());
        String timeHere = formatter.format(curDate);
        item itemNew = new item(height, weight, timeHere);
        adapter.newItem(itemNew);
    }
}

