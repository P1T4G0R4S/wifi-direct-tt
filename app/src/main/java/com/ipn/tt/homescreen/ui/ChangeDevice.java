package com.ipn.tt.homescreen.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.ipn.tt.homescreen.R;
import com.ipn.tt.homescreen.network.DeviceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iguanna on 29/07/2017.
 */

public class ChangeDevice extends AppCompatActivity {
    RadioGroup radio_device;
    RadioButton rb_device;
    Button btn_device;
    DeviceType device;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devicetype_change);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Perfil de búsqueda");
        toolbar.setTitleTextColor(0xFFFFFFFF);

        //spn_device = (Spinner) findViewById(R.id.spn_device_type);

        radio_device = (RadioGroup) findViewById(R.id.radio_device);
        btn_device = (Button) findViewById(R.id.btn_save_device);

        btn_device.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId = radio_device.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rb_device = (RadioButton) findViewById(selectedId);

                //Toast.makeText(getApplicationContext(),rb_device.getText(), Toast.LENGTH_SHORT).show();

                switch(rb_device.getText().toString()){
                    case "Intermediario":
                        device = DeviceType.RANGE_EXTENDER;
                        break;
                    case "Maestro":
                        device = DeviceType.ACCESS_POINT;
                        break;
                    case "Emisor":
                        device = DeviceType.EMITTER;
                        break;
                    case "Buscador":
                        device = DeviceType.QUERIER;
                        break;
                    default:
                        device = DeviceType.ACCESS_POINT_WREQ;
                }

                //Set SharedPreferences
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE).edit();
                editor.putInt("devicetype", device.getCode());
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });
    }
}
