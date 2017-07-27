package com.ipn.tt.homescreen.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.ipn.tt.homescreen.network.DeviceType;

/**
 * Created by Iguanna on 25/07/2017.
 */

public class RegisterUser extends AppCompatActivity{
    Button btn_register;
    TextView tv_name, tv_curp, tv_mac_address;
    DBManager db;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Registro");
        toolbar.setTitleTextColor(0xFFFFFFFF);

        btn_register = (Button) findViewById(R.id.btn_register);
        tv_name = (TextView) findViewById(R.id.name_register);
        tv_curp = (TextView) findViewById(R.id.curp_register);
        tv_mac_address = (TextView) findViewById(R.id.mac_address_register);

        btn_register.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                if (tv_name.getText().toString().matches("") || tv_curp.getText().toString().matches("") ) {
                    Toast.makeText(getApplicationContext(), "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    /// /Insertar registro
                    db = new DBManager(getApplicationContext());
                    db.open();
                    User usr = new User();
                    usr.name = tv_name.getText().toString();
                    usr.type_user = UserType.MYSELF.id;
                    usr.mac = tv_mac_address.getText().toString();
                    usr.curp = tv_curp.getText().toString();

                    try{
                        db.insert(usr);
                        //Set SharedPreferences
                        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE).edit();
                        editor.putBoolean("registered", true);
                        editor.putInt("devicetype", DeviceType.EMITTER.getCode());
                        editor.commit();
                    }
                    catch(Exception ex){
                        Toast.makeText(getApplicationContext(), "Problema al registrarse en la aplicación: " + ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

            }
        });



    }
}
