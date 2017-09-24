package com.ipn.tt.homescreen.Utils;

import android.util.Log;

import com.ipn.tt.homescreen.network.DeviceType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.rit.se.wifibuddy.DnsSdService;
import edu.rit.se.wifibuddy.DnsSdTxtRecord;

/**
 * Created by osvaldo on 9/10/17.
 */

public class NetworkUtil {
    private static NetworkUtil singleton;
    private DeviceType myType;
    private Map<String, DnsSdService> connectedDevices;

    private NetworkUtil() { }

    private NetworkUtil(DeviceType myType) {
        this.myType = myType;
    }

    public Map<String, DnsSdService> getConnectedDevices() {
        return connectedDevices;
    }

    public void setConnectedDevices(Map<String, DnsSdService> connectedDevices) {
        this.connectedDevices = connectedDevices;
    }

    public boolean canConnectTo(DnsSdService service) {
        return true;
    }

    public boolean canDiscoverTo(String discoveredDeviceType) {
        if(myType == DeviceType.EMITTER) {
            // ACCESS_POINT, because it has to send its information to a free ACCESS_POINT
            return discoveredDeviceType.equals(DeviceType.ACCESS_POINT.toString());
        } else if(myType == DeviceType.ACCESS_POINT_WREQ || myType == DeviceType.ACCESS_POINT_WRES) {
            // RANGE_EXTENDER, because it has to redirect its information to the RANGE_EXTENDER
            // !!! but we don't need to see the RANGE_EXTENDER, it has to see the ACCESS_POINT
            //return discoveredDeviceType.equals(DeviceType.RANGE_EXTENDER.toString());
            return false;
        } else if(myType == DeviceType.QUERIER) {
            // ACCESS_POINT, because the target ACCESS_POINT must not be involved in a searching process
            return discoveredDeviceType.equals(DeviceType.ACCESS_POINT.toString());
        } else if(myType == DeviceType.QUERIER_ASK) {
            // ACCESS_POINT_WRES, because the target ACCESS_POINT must gives us a response
            return discoveredDeviceType.equals(DeviceType.ACCESS_POINT_WRES.toString());
        } else if(myType == DeviceType.RANGE_EXTENDER) {
            // ACCESS_POINT_WREQ && ACCESS_POINT_WRES && !ACCESS_POINT
            // because the target must have interesting information (a request or response)
            // or must be a clean ACCESS_POINT to send it a request ONLY
            String str = DeviceType.ACCESS_POINT.toString();
            return discoveredDeviceType.startsWith(str) && !discoveredDeviceType.endsWith(str);
        } else if(myType == DeviceType.RANGE_EXTENDER_WREQ || myType == DeviceType.RANGE_EXTENDER_WRES) {
            return discoveredDeviceType.equals(DeviceType.ACCESS_POINT.toString());
        }
        return false;
    }

    public static NetworkUtil getInstance(DeviceType deviceType) {
        if(singleton == null) {
            synchronized(NetworkUtil.class) {
                if(singleton == null) {
                    singleton = new NetworkUtil();
                    Log.d("DEBUG", "getInstance(): inside");
                }
            }
        }
        Log.d("DEBUG", "getInstance(): ");
        if(deviceType != null) {
            singleton.myType = deviceType;
        }

        if (singleton.connectedDevices == null) {
            singleton.connectedDevices = new HashMap<>();
        }

        return singleton;
    }
}
