package com.ipn.tt.homescreen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ipn.tt.homescreen.R;
import com.ipn.tt.homescreen.db.DBManager;
import com.ipn.tt.homescreen.db.User;
import com.ipn.tt.homescreen.db.UserType;

/**
 * Created by Iguanna on 16/07/2017.
 */

public class ContactEdit extends AppCompatActivity {
    DBManager db;
    TextView tv_name, tv_curp, tv_mac;
    Button btn_cancel, btn_save;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_edit);

        TextView tv=(TextView) findViewById(R.id.lbl_MAC_edit);
        TextView tv2=(TextView) findViewById(R.id.lbl_nombre_edit);
        TextView tv3=(TextView) findViewById(R.id.lbl_CURP_edit);

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_save = (Button) findViewById(R.id.btn_save);

        tv_name = (TextView) findViewById(R.id.name_contact);
        tv_mac = (TextView) findViewById(R.id.mac_address_contact);
        tv_curp = (TextView) findViewById(R.id.curp_contact);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Intent i = getIntent();
        //Toast.makeText(this,i.getStringExtra("id"), Toast.LENGTH_LONG).show();
        db = new DBManager(this);
        db.open();
        User usr = db.fetchUsuario(new String[]{i.getStringExtra("id")});
        db.close();

        tv_name.setText(usr.name);
        tv_curp.setText(usr.curp);
        tv_mac.setText(usr.mac);

        tv.setOnFocusChangeListener( new View.OnFocusChangeListener(){
            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    view.setBackgroundResource( R.drawable.focus_border_style);
                }
                else{
                    view.setBackgroundResource( R.drawable.lost_focus_style);
                }
            }
        });

        tv2.setOnFocusChangeListener( new View.OnFocusChangeListener(){
            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    view.setBackgroundResource( R.drawable.focus_border_style);
                }
                else{
                    view.setBackgroundResource( R.drawable.lost_focus_style);
                }
            }
        });

        tv3.setOnFocusChangeListener( new View.OnFocusChangeListener(){
            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    view.setBackgroundResource( R.drawable.focus_border_style);
                }
                else{
                    view.setBackgroundResource( R.drawable.lost_focus_style);
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                User usr = new User();
                usr.name = tv_name.getText().toString();
                usr.curp = tv_curp.getText().toString();
                usr.mac = tv_mac.getText().toString();
                usr.type_user = UserType.CONTACT.id;
                db = new DBManager(view.getContext());
                db.open();
                db.update(usr);
                db.close();
                Intent i2 = new Intent(view.getContext(), MainActivity.class);
                getApplicationContext().startActivity(i2);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i2 = new Intent(view.getContext(), MainActivity.class);
                getApplicationContext().startActivity(i2);
            }
        });
    }
}
