package com.example.apipractice;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=RHK7YLY3DFGWV052";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);
    }

    void parseJsonData(String jsonString) {
        try {
            ArrayList<Stock> al = new ArrayList<>();
            JSONObject object = new JSONObject(jsonString);
            JSONObject metaData= object.getJSONObject("Meta Data");
            String symbol = metaData.getString("2. Symbol");
            String lastRefreshed = metaData.getString("3. Last Refreshed");
            JSONObject timeSeries = object.getJSONObject("Time Series (5min)");
            JSONObject refreshed = timeSeries.getJSONObject(lastRefreshed);
            String price = refreshed.getString("4. close");

            Stock stock1 = new Stock();
            stock1.setPrice(price);
            stock1.setSymbol(symbol);
            al.add(stock1);
            //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al);
            MyAdapter myAdapter = new MyAdapter(MainActivity.this);
            myAdapter.setStockList(al);
            listView.setAdapter(myAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }
}