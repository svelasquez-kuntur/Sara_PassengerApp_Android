package com.sara.passenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.google.android.gms.gcm.GcmListenerService;
import com.utils.CommonUtilities;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyGcmListenerService extends GcmListenerService {

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs. For
     * Set of keys use data.keySet().
     */
    // [START receive_message]

    GeneralFunctions generalFunctions = null;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");


//        if(message!=null && Utils.checkText(message)) {
//
//            if (isJSONValid(message)) {
//                Intent intent_broad = new Intent(CommonUtilities.driver_message_arrived_intent_action);
//                intent_broad.putExtra(CommonUtilities.driver_message_arrived_intent_key, message);
//                this.sendBroadcast(intent_broad);
//
////                if (!generalFunctions.getMemberId().equals("")) {
////                    onGcmMessageArrived(message, getApplicationContext());
////                }
//
//            } else {
//                try {
//                    PowerManager powerManager = (PowerManager) MyApp.getCurrentAct().getSystemService(Context.POWER_SERVICE);
//                    boolean isScreenOn = powerManager.isScreenOn();
//                    if (isScreenOn) {
//                        buildMessage(message);
//                        Utils.generateNotification(getApplicationContext(), message);
//                    } else {
//                        Utils.generateNotification(getApplicationContext(), message);
//                    }
//                } catch (Exception e) {
//                    Utils.generateNotification(getApplicationContext(), message);
//
//                }
//            }
     //   }


    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


    public void buildMessage(final String message) {

        MyApp.getCurrentAct().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GeneralFunctions generalFunc = new GeneralFunctions(MyApp.getCurrentAct());
                generalFunc.showGeneralMessage("", message);

            }
        });

    }




}
