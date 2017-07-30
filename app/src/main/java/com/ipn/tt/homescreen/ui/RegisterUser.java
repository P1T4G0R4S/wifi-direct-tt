package com.ipn.tt.homescreen.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ipn.tt.homescreen.R;
import com.ipn.tt.homescreen.db.DBManager;
import com.ipn.tt.homescreen.db.User;
import com.ipn.tt.homescreen.db.UserType;
import com.ipn.tt.homescreen.network.DeviceType;

import edu.rit.se.wifibuddy.WifiDirectHandler;

/**
 * Created by Iguanna on 25/07/2017.
 */

public class RegisterUser extends AppCompatActivity  {
    String TAG = "RegisterUser";
    Button btn_register;
    TextView tv_name, tv_curp, tv_mac_address;
    DBManager db;
    WifiDirectHandler wifiDirectHandler;
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

        registerCommunicationReceiver();

        Intent intent = new Intent(this, WifiDirectHandler.class);
        bindService(intent, wifiServiceConnection, BIND_AUTO_CREATE);
    }

    private void registerCommunicationReceiver() {
        CommunicationReceiver communicationReceiver = new CommunicationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiDirectHandler.Action.DEVICE_CHANGED);
        filter.addAction(WifiDirectHandler.Action.WIFI_STATE_CHANGED);
        LocalBroadcastManager.getInstance(this).registerReceiver(communicationReceiver, filter);
        Log.i(TAG, "Communication Receiver registered");
    }

    public class CommunicationReceiver extends BroadcastReceiver {

        private static final String TAG = WifiDirectHandler.TAG + "CommReceiver";
        private MainActivity activity;

        @Override
        public void onReceive(Context context, Intent intent) {
            //activity = (MainActivity) context;
            // Get the intent sent by WifiDirectHandler when a service is found
            if (intent.getAction().equals(WifiDirectHandler.Action.DEVICE_CHANGED)) {
                // This device's information has changed
                Log.i(TAG, "This device changed");
                //deviceInfoTextView.setText(wifiDirectHandler.getThisDeviceInfo());
                Log.d(TAG + "TEST-2", wifiDirectHandler.getThisDeviceAddress());

                if (tv_mac_address != null) {
                    tv_mac_address.setText(wifiDirectHandler.getThisDeviceAddress());
                }
            } else if (intent.getAction().equals(WifiDirectHandler.Action.WIFI_STATE_CHANGED)) {
                // Wi-Fi has been enabled or disabled
                Log.i(TAG, "Wi-Fi state changed");
                //mainFragment.handleWifiStateChanged();
            }
        }
    }

    private ServiceConnection wifiServiceConnection = new ServiceConnection() {

        /**
         * Called when a connection to the Service has been established, with the IBinder of the
         * communication channel to the Service.
         * @param name The component name of the service that has been connected
         * @param service The IBinder of the Service's communication channel
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "Binding WifiDirectHandler service");
            Log.i(TAG, "ComponentName: " + name);
            Log.i(TAG, "Service: " + service);
            WifiDirectHandler.WifiTesterBinder binder = (WifiDirectHandler.WifiTesterBinder) service;

            wifiDirectHandler = binder.getService();
            Log.i(TAG, "WifiDirectHandler service bound");

            // Add MainFragment to the 'fragment_container' when wifiDirectHandler is bound
            //mainFragment = new MainFragment();
            //replaceFragment(mainFragment);

            //Log.d(TAG + "TEST", wifiDirectHandler.getThisDeviceInfo());

            //setUser(getWifiHandler().getThisDeviceAddress());
            //setupInitialWifiP2p();
        }

        /**
         * Called when a connection to the Service has been lost.  This typically
         * happens when the process hosting the service has crashed or been killed.
         * This does not remove the ServiceConnection itself -- this
         * binding to the service will remain active, and you will receive a call
         * to onServiceConnected when the Service is next running.
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "WifiDirectHandler service unbound");
        }
    };
}
