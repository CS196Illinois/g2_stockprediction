package com.example.g2_stockprediction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    ListView listView;
    String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=RHK7YLY3DFGWV052";
    ProgressDialog dialog;
    private SearchView stocksearch;
    public String stockname;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        listView = (ListView)findViewById(R.id.listView);
        stocksearch = (SearchView) findViewById(R.id.svStockSearch);

        /**
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();
         **/


        myRef = FirebaseDatabase.getInstance().getReference().child("stocks");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Stock> al = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String name = child.child("name").getValue().toString();
                    String price = child.child("close").getValue().toString();
                    Stock stock = new Stock();
                    stock.setPrice(price);
                    stock.setSymbol(name);
                    al.add(stock);
                }
                MyAdapter myAdapter = new MyAdapter(HomePage.this);
                myAdapter.setStockList(al);
                listView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /**
        StringRequest request = new StringRequest(url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        RequestQueue rQueue = Volley.newRequestQueue(HomePage.this);
        rQueue.add(request);
         **/

        stocksearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                stockname = query;
                Intent intent = new Intent(HomePage.this, StockPage.class);
                intent.putExtra("stockname", stockname);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        transparentStatusAndNavigation();

    }

    private void transparentStatusAndNavigation() {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

/**
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
            MyAdapter myAdapter = new MyAdapter(HomePage.this);
            myAdapter.setStockList(al);
            listView.setAdapter(myAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }
 **/
}