package com.ipn.tt.homescreen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ipn.tt.homescreen.R;
import com.ipn.tt.homescreen.db.DBManager;
import com.ipn.tt.homescreen.db.User;
import com.ipn.tt.homescreen.db.UserType;

/**
 * Created by Iguanna on 05/07/2017.
 */

public class ContactDetails extends AppCompatActivity {
    TextView tv_name, tv_mac, tv_curp;
    Button btn_edit;
    DBManager db;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Detalle");
        toolbar.setTitleTextColor(0xFFFFFFFF);

        final Intent i = getIntent();
        //Toast.makeText(this,i.getStringExtra("id"), Toast.LENGTH_LONG).show();
        db = new DBManager(this);
        db.open();
        User usr = db.fetchUsuario(new String[]{i.getStringExtra("id")});
        db.close();

        tv_name = (TextView) findViewById(R.id.name_contact);
        tv_curp = (TextView) findViewById(R.id.curp_contact);
        tv_mac = (TextView) findViewById(R.id.mac_contact);
        btn_edit = (Button) findViewById(R.id.btn_edit_contact);

        tv_name.setText(usr.name);
        tv_curp.setText(usr.curp);
        tv_mac.setText(usr.mac);

        btn_edit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i2 = new Intent(view.getContext(), ContactEdit.class);
                i2.putExtra("id",i.getStringExtra("id") );
                getApplicationContext().startActivity(i2);
            }});
    }
}
