package com.ipn.tt.homescreen.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.lang3.SerializationUtils;

import com.ipn.tt.homescreen.network.Message;
import com.ipn.tt.homescreen.network.MessageType;
import com.ipn.tt.homescreen.network.ObjectType;
import com.ipn.tt.homescreen.ui.ContactSearch;

import edu.rit.se.wifibuddy.CommunicationManager;
import edu.rit.se.wifibuddy.WifiDirectHandler;

/**
 * Created by osvaldo on 8/27/17.
 */

public class SendMessageTask extends AsyncTask<Object, Void, String> {
    String TAG = "SendMessageTask";

    protected String doInBackground(Object... objects) {
        WifiDirectHandler wifiHandler = (WifiDirectHandler) objects[0];
        String message = objects[1].toString();
        ObjectType objectType = (ObjectType) objects[2];

        Log.d(TAG, "Sending: " + message);
        CommunicationManager communicationManager = wifiHandler.getCommunicationManager();
        if (communicationManager != null && !message.equals("")) {
            // Gets first word of device name
            //String author = getWifiHandler().getThisDevice().deviceName.split(" ")[0];
            byte[] messageBytes = (message).getBytes();
            Message finalMessage = new Message(MessageType.TEXT, messageBytes);
            finalMessage.objectType = objectType;
            communicationManager.write(SerializationUtils.serialize(finalMessage));
        } else {
            Log.e(TAG, "Communication Manager is null");
        }
        Log.d(TAG, "SendMessageTask");
        return "";
    }

    protected void onProgressUpdate(Void... progress) {
    }

    protected void onPostExecute(String result) {
    }
}
