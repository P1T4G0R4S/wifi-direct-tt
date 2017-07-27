package com.ipn.tt.homescreen.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.view.ViewPager;
import com.ipn.tt.homescreen.R;
import com.ipn.tt.homescreen.db.DBManager;
import com.ipn.tt.homescreen.db.User;
import com.ipn.tt.homescreen.db.UserType;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecycleAdapter mAdapter;
    SharedPreferences pref;
    DBManager db;
    User usr;
    private ViewPager viewPager;
    private View indicator1;
    private View indicator2;
    private View indicator3;
    private View indicator4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = this.getSharedPreferences("Options", MODE_PRIVATE);

        if(pref.getBoolean("registered",false)){
            List<Detail> mList = new ArrayList<>();


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ContactCreate.class);
                    startActivity(intent);
                }
            });

            //Obtener de la base de datos
            db = new DBManager(this);
            db.open();
            ArrayList<User> usrs = db.fetchAllUsuario(new String[]{"1"});
            db.close();

            for (int i = 0; i < usrs.size(); i++) {
                mList.add(new Detail(usrs.get(i)));
            }
            mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
            mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            mAdapter = new RecycleAdapter(this, mList);
            mRecyclerView.setAdapter(mAdapter);
        }
        else{
            //Set SharedPreferences
            finish();
            Intent intent = new Intent(this, RegisterUser.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume(){
        List<Detail> mList = new ArrayList<>();
        super.onResume();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ContactCreate.class);
                startActivity(intent);
            }
        });

        //Obtener de la base de datos
        db = new DBManager(this);
        db.open();
        ArrayList<User> usrs = db.fetchAllUsuario(new String[]{"1"});
        db.close();

        for (int i = 0; i < usrs.size(); i++) {
            mList.add(new Detail(usrs.get(i)));
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        mAdapter = new RecycleAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void makeToast(String input){
        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
            //return true;
        //}

        return super.onOptionsItemSelected(item);
    }
}
