package com.example.g2_stockprediction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SavedStockPage extends AppCompatActivity {
    ListView listView;
    public String stockname;
    private DatabaseReference usersRef;
    private DatabaseReference myRef;
    private String currentUser;
    private ArrayList<Stock> al;
    private String savedStocks;
    private String[] stocks;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_stock_page);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra("currentUser");
        listView = (ListView) findViewById(R.id.listvieww);
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser);
        myRef = FirebaseDatabase.getInstance().getReference().child("stocks");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                savedStocks = dataSnapshot.child("stocks").getValue().toString();
                stocks = savedStocks.split(",");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                al = new ArrayList<>();
                for (String stock : stocks) {
                    String name = dataSnapshot.child(stock).child("name").getValue().toString();
                    String price = dataSnapshot.child(stock).child("close").getValue().toString();
                    String lowin = dataSnapshot.child(stock).child("currLow").getValue().toString();
                    String logoin = dataSnapshot.child(stock).child("background").getValue().toString();
                    String key = dataSnapshot.child(stock).getKey();
                    Stock newStock = new Stock();
                    newStock.setPrice(price);
                    newStock.setSymbol(name);
                    newStock.setLow(lowin);
                    newStock.setKey(key);
                    newStock.setLogo(logoin);
                    al.add(newStock);
                }
                MyAdapter myAdapter = new MyAdapter(SavedStockPage.this);
                myAdapter.setStockList(al);
                listView.setAdapter(myAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SavedStockPage.this, StockPage.class);
                String selectedStock = al.get(position).getKey();
                intent.putExtra("stockname", selectedStock);
                startActivity(intent);
            }
        });

    }
}
