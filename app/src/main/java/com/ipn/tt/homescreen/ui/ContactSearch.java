package com.ipn.tt.homescreen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ipn.tt.homescreen.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iguanna on 18/07/2017.
 */

public class ContactSearch extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecycleAdapter mAdapter;
    private List<Detail> mList = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Buscando Dispositivos");
        toolbar.setTitleTextColor(0xFFFFFFFF);


        /*ArrayList<String> nombres = new ArrayList();
        nombres.add(0,"María");
        nombres.add(1,"Alberto");
        nombres.add(2,"Andrés");
        nombres.add(3,"Alfonso");
        nombres.add(4,"Roberto");
        nombres.add(5,"Celeste");
        for (int i = 0; i < 6; i++) {
            mList.add(new Detail(nombres.get(i)));
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(ContactSearch.this, 1));
        mAdapter = new RecycleAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);*/
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cambiar_perfil:
                //Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, ChangeDevice.class);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
