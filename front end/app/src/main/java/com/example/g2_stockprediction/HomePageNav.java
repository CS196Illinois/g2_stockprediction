package com.example.g2_stockprediction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//This java file is for the home page

public class HomePageNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Initializing variables
    ListView listView;
    String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=RHK7YLY3DFGWV052";
    ProgressDialog dialog;
    private SearchView stocksearch;
    public String stockname;
    private DatabaseReference myRef;
    private DatabaseReference usersRef;
    private String currentUser;
    private ImageView profpic;
    private TextView name;
    private TextView email;
    private ArrayList<Stock> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_nav);
        final Context context = this;

        //Declaring the variables
        listView = (ListView)findViewById(R.id.listView);
        stocksearch = (SearchView) findViewById(R.id.svStockSearch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        //Getting all of the stocks which are in the database
        myRef = FirebaseDatabase.getInstance().getReference().child("stocks");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                al = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String name = child.child("name").getValue().toString();
                    String price = child.child("close").getValue().toString();
                    String lowin = child.child("currLow").getValue().toString();
                    String logoin = child.child("background").getValue().toString();
                    String key = child.getKey();
                    Stock stock = new Stock();
                    stock.setPrice(price);
                    stock.setSymbol(name);
                    stock.setLow(lowin);
                    stock.setKey(key);
                    stock.setLogo(logoin);
                    al.add(stock);
                }
                MyAdapter myAdapter = new MyAdapter(HomePageNav.this);
                myAdapter.setStockList(al);
                listView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //This is to show the details of a specific stock when clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomePageNav.this, StockPage.class);
                String selectedStock = al.get(position).getKey();
                intent.putExtra("stockname", selectedStock);
                startActivity(intent);
            }
        });

        //This is what runs when a stock is searched for
        stocksearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                stockname = query;
                Intent intent = new Intent(HomePageNav.this, StockPage.class);
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

        //Hidden button right now, but would ideally be used for saving stocks
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //This is the navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page_nav, menu);
        profpic = (ImageView) findViewById(R.id.imgProfPicNav);
        name = (TextView) findViewById(R.id.txtNameNav);
        email = (TextView) findViewById(R.id.txtEmailNav);
        currentUser = FirebaseAuth.getInstance().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser);

        //This shows the users profile info in the header
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String namein = dataSnapshot.child("name").getValue().toString();
                    String emailin = dataSnapshot.child("email").getValue().toString();
                    String profpicin = dataSnapshot.child("profilepic").getValue().toString();

                    name.setText(namein);
                    email.setText(emailin);
                    Picasso.with(HomePageNav.this).load(profpicin).into(profpic);
                } catch (NullPointerException e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }

    //This function runs when things on the navigation bar is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final Context context = this;
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final Context context = this;
        int id = item.getItemId();

        if (id == R.id.nav_find) {
            dialog myDialog = new dialog(this);
            myDialog.show();
        } else if (id == R.id.nav_toppicks) {
            Intent intent = new Intent(HomePageNav.this, TopStockOfTheDay.class);
            startActivity(intent);

        } else if (id == R.id.nav_saved) {
            Intent intent = new Intent(HomePageNav.this, SavedStockPage.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(HomePageNav.this, Settings.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //This is used to search for a stock
    public void searchStock(final String text) {
        stockname = text;
        Intent intent = new Intent(HomePageNav.this, StockPage.class);
        intent.putExtra("stockname", stockname);
        startActivity(intent);
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
}

