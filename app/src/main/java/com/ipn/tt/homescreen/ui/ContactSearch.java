package com.ipn.tt.homescreen.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ipn.tt.homescreen.R;
import com.ipn.tt.homescreen.Utils.FileUtils;
import com.ipn.tt.homescreen.Utils.UpgradeAppUtils;
import com.ipn.tt.homescreen.adapters.ServiceListViewAdapter;
import com.ipn.tt.homescreen.network.DeviceType;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import edu.rit.se.wifibuddy.DnsSdService;
import edu.rit.se.wifibuddy.WifiDirectHandler;

/**
 * Created by Iguanna on 18/07/2017.
 */

public class ContactSearch extends AppCompatActivity {
    String TAG = "ContactSearch";
    private SharedPreferences pref;
    private ListView deviceList;
    private ServiceListViewAdapter servicesListAdapter;
    private List<DnsSdService> services = new ArrayList<>();
    WifiDirectHandler wifiDirectHandler;
    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_search);
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Buscando Dispositivos");
        toolbar.setTitleTextColor(0xFFFFFFFF);
    }

    @Override
    public void onResume() {
        super.onResume();

        setServiceList();
        initEnableExternalStorage();
        registerCommunicationReceiver();
        addWifiService();

        showDeviceInformation();

        prepareResetButton();
    }

    @Override
    public void onPause() {
        super.onPause();

        removeWifiP2pService();
        removeWifiService();
        finish();
    }

    private void prepareResetButton(){
        ImageButton resetButton = (ImageButton)findViewById(R.id.btn_search_wifi);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetServiceDiscovery();

            }
        });
    }

    private void resetServiceDiscovery(){

        removeWifiP2pService();
        removeWifiService();

        setServiceList();
        initEnableExternalStorage();
        //registerCommunicationReceiver();
        addWifiService();


        // Clear the list, notify the list adapter, and start discovering services again
        Log.i(TAG, "Resetting Service discovery");
        services.clear();
        servicesListAdapter.notifyDataSetChanged();
        wifiDirectHandler.resetServiceDiscovery();
    }

    private void setServiceList() {
        deviceList = (ListView)findViewById(R.id.device_list);
        servicesListAdapter = new ServiceListViewAdapter(this, services);
        deviceList.setAdapter(servicesListAdapter);
        services.clear();
        servicesListAdapter.notifyDataSetChanged();
    }

    private void addWifiService() {
        Log.w(TAG, "Service Wifi added");
        Intent intent = new Intent(context, WifiDirectHandler.class);
        bindService(intent, wifiServiceConnection, BIND_AUTO_CREATE);
        startService(intent);
    }

    private void addWifiP2pService() {
        pref = this.getSharedPreferences("Options", MODE_PRIVATE);
        int deviceTypePref = pref.getInt("devicetype",999);
        DeviceType deviceType = DeviceType.get(deviceTypePref);

        if (wifiDirectHandler.getWifiP2pServiceInfo() == null) {
            HashMap<String, String> record = new HashMap<>();
            record.put("Name", wifiDirectHandler.getThisDevice().deviceName);
            record.put("Address", wifiDirectHandler.getThisDevice().deviceAddress);
            record.put("DeviceType", deviceType.toString());
            wifiDirectHandler.addLocalService("Wi-Fi Buddy", record);
            Log.d(TAG, "Service P2p added");
        } else {
            Log.w(TAG, "Service P2p already added");
        }
    }

    private void removeWifiService() {
        Log.w(TAG, "Service Wifi removed");
        wifiDirectHandler.removeGroup();
        context.stopService(new Intent(context, ServiceConnection.class));
        context.unbindService(wifiServiceConnection);
    }

    private void removeWifiP2pService() {
        Log.w(TAG, "Service P2p removed");
        wifiDirectHandler.removeService();
    }

    private void showDeviceInformation() {
        pref = this.getSharedPreferences("Options", MODE_PRIVATE);
        int deviceTypePref = pref.getInt("devicetype",999);
        DeviceType deviceType = DeviceType.get(deviceTypePref);

        TextView device_role = (TextView) findViewById(R.id.device_role);
        device_role.setText(deviceType.toString());

        if (wifiDirectHandler == null) {
            return;
        }
        if (wifiDirectHandler.getThisDevice() == null) {
            return;
        }

        TextView lbl_device_name = (TextView) findViewById(R.id.lbl_device_name);
        lbl_device_name.setText(wifiDirectHandler.getThisDevice().deviceName);

        TextView lbl_device_info = (TextView) findViewById(R.id.lbl_device_info);
        lbl_device_info.setText(wifiDirectHandler.getThisDeviceInfo());

    }

    private void initEnableExternalStorage() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }

    private void registerCommunicationReceiver() {
        CommunicationReceiver communicationReceiver = new CommunicationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiDirectHandler.Action.DNS_SD_SERVICE_AVAILABLE);
        filter.addAction(WifiDirectHandler.Action.WIFI_STATE_CHANGED);
        filter.addAction(WifiDirectHandler.Action.SERVICE_CONNECTED);
        filter.addAction(WifiDirectHandler.Action.MESSAGE_RECEIVED);
        filter.addAction(WifiDirectHandler.Action.DEVICE_CHANGED);
        LocalBroadcastManager.getInstance(this).registerReceiver(communicationReceiver, filter);
        Log.i(TAG, "Communication Receiver registered");
    }

    public class CommunicationReceiver extends BroadcastReceiver {

        private static final String TAG = WifiDirectHandler.TAG + "CSearchReceiver";
        //private MainActivity activity;

        @Override
        public void onReceive(Context context, Intent intent) {
            //activity = (MainActivity) context;
            // Get the intent sent by WifiDirectHandler when a service is found
            if (intent.getAction().equals(WifiDirectHandler.Action.DEVICE_CHANGED)) {
                // This device's information has changed
                Log.i(TAG, "This device changed");
                Log.d(TAG, wifiDirectHandler.getThisDeviceAddress());
                addWifiP2pService();
                prepareResetButton();
                showDeviceInformation();
                wifiDirectHandler.continuouslyDiscoverServices();

            } else if (intent.getAction().equals(WifiDirectHandler.Action.WIFI_STATE_CHANGED)) {
                // Wi-Fi has been enabled or disabled
                Log.i(TAG, "Wi-Fi state changed");
            } else if (intent.getAction().equals(WifiDirectHandler.Action.SERVICE_CONNECTED)) {
                // This device has connected to another device broadcasting the same service
                Log.i(TAG, "Service connected onReceive()");

            } else if (intent.getAction().equals(WifiDirectHandler.Action.MESSAGE_RECEIVED)) {
                // A message from the Communication Manager has been received
                Log.i(TAG, "Message received");

            } else if (intent.getAction().equals(WifiDirectHandler.Action.DNS_SD_SERVICE_AVAILABLE)) {
                Log.d(TAG, "Service Discovered and Accessed " + (new Date()).getTime());
                String serviceKey = intent.getStringExtra(WifiDirectHandler.SERVICE_MAP_KEY);
                DnsSdService service = wifiDirectHandler.getDnsSdServiceMap().get(serviceKey);

                if (service.getSrcDevice() == null) {
                    Log.w(TAG, "Unaccesible source device.");
                    return;
                }

                if (service.getSrcDevice().deviceName.equals("")) {
                    Log.w(TAG, "Unaccesible source device name.");
                    return;
                }

                servicesListAdapter.addUnique(service);

                Log.d(TAG + "TEST", "ServicesList count: " + servicesListAdapter.getCount());
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

            showDeviceInformation();
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

    public WifiDirectHandler getWifiHandler() {
        return wifiDirectHandler;
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
            case R.id.actualizar_app:
                FileUtils fileUtils = new FileUtils(getApplicationContext());
                File file = fileUtils.createFile("text.txt");
                String fileText = fileUtils.readAllText(file);
                Log.d("onOptionsItemSelected", fileText);

                UpgradeAppUtils task = new UpgradeAppUtils(getApplicationContext());
                task.execute("http://192.168.0.5:8080/app-debug.apk");
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void onServiceClick(DnsSdService service) {
        Log.i(TAG, "\nService List item tapped");

        if (service.getSrcDevice().status == WifiP2pDevice.CONNECTED) {
            /*if (chatFragment == null) {
                chatFragment = new ChatFragment();
            }
            replaceFragment(chatFragment);*/
            Log.i(TAG, "Switching to Chat fragment");
        } else if (service.getSrcDevice().status == WifiP2pDevice.AVAILABLE) {
            String sourceDeviceName = service.getSrcDevice().deviceName;
            if (sourceDeviceName.equals("")) {
                sourceDeviceName = "other device";
            }
            Toast.makeText(this, "Inviting " + sourceDeviceName + " to connect", Toast.LENGTH_LONG).show();
            wifiDirectHandler.initiateConnectToService(service);
        } else {
            Log.e(TAG, "Service not available");
            Toast.makeText(this, "Service not available", Toast.LENGTH_LONG).show();
        }
    }
}
