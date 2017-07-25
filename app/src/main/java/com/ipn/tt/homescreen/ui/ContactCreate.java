package com.ipn.tt.homescreen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ipn.tt.homescreen.R;
import com.ipn.tt.homescreen.db.DBManager;
import com.ipn.tt.homescreen.db.User;
import com.ipn.tt.homescreen.db.UserType;

/**
 * Created by Iguanna on 23/07/2017.
 */

public class ContactCreate extends AppCompatActivity {
    Button btn_cancel,btn_save_contact;
    TextView lbl_name, lbl_curp, lbl_mac;
    TextView tv_name, tv_curp,tv_mac;
    DBManager db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_edit);

        lbl_name = (TextView) findViewById(R.id.lbl_nombre_edit);
        lbl_mac = (TextView) findViewById(R.id.lbl_MAC_edit);
        lbl_curp = (TextView) findViewById(R.id.lbl_CURP_edit);

        tv_mac =(TextView) findViewById(R.id.mac_address_contact);
        tv_name =(TextView) findViewById(R.id.name_contact);
        tv_curp=(TextView) findViewById(R.id.curp_contact);

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_save_contact = (Button) findViewById(R.id.btn_save);

        btn_cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });

        btn_save_contact.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                User usr = new User();
                usr.name = tv_name.getText().toString();
                usr.curp = tv_curp.getText().toString();
                usr.mac = tv_mac.getText().toString();
                usr.type_user = UserType.CONTACT.id;
                db = new DBManager(view.getContext());
                db.open();
                db.insert(usr);
                db.close();
                finish();
                Intent i2 = new Intent(view.getContext(), MainActivity.class);
                getApplicationContext().startActivity(i2);
                finish();
            }
        });

        lbl_name.setOnFocusChangeListener( new View.OnFocusChangeListener(){
            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    view.setBackgroundResource( R.drawable.focus_border_style);
                }
                else{
                    view.setBackgroundResource( R.drawable.lost_focus_style);
                }
            }
        });

        lbl_mac.setOnFocusChangeListener( new View.OnFocusChangeListener(){
            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    view.setBackgroundResource( R.drawable.focus_border_style);
                }
                else{
                    view.setBackgroundResource( R.drawable.lost_focus_style);
                }
            }
        });

        lbl_curp.setOnFocusChangeListener( new View.OnFocusChangeListener(){
            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    view.setBackgroundResource( R.drawable.focus_border_style);
                }
                else{
                    view.setBackgroundResource( R.drawable.lost_focus_style);
                }
            }
        });
    }
}