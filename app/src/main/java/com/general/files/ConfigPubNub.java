package com.general.files;

import android.content.Context;
import android.location.Location;
import android.os.Handler;

import com.sara.passenger.MainActivity;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.utils.CommonUtilities;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Admin on 05-10-2016.
 */
public class ConfigPubNub implements GetLocationUpdates.LocationUpdates, UpdateFrequentTask.OnTaskRunCalled {
    Context mContext;
    PubNub pubnub;
    GeneralFunctions generalFunc;
    private Location userLoc;
    private String iTripId = "";
    private String iDriverId = "";
    private ExecuteWebServerUrl currentExeTask;
    private UpdateFrequentTask updatepassengerStatustask;
    private InternetConnection intCheck;
    private GetLocationUpdates getLocUpdates;
    private boolean isPubnubInstKilled = false;


    public ConfigPubNub(Context mContext) {
        this.mContext = mContext;
        generalFunc = new GeneralFunctions(mContext);
        intCheck=new InternetConnection(mContext);
        PNConfiguration pnConfiguration = new PNConfiguration();

        pnConfiguration.setUuid(generalFunc.retrieveValue(Utils.DEVICE_SESSION_ID_KEY).equals("") ? generalFunc.getMemberId() : generalFunc.retrieveValue(Utils.DEVICE_SESSION_ID_KEY));

        pnConfiguration.setSubscribeKey(generalFunc.retrieveValue(CommonUtilities.PUBNUB_SUB_KEY));
        pnConfiguration.setPublishKey(generalFunc.retrieveValue(CommonUtilities.PUBNUB_PUB_KEY));
        pnConfiguration.setSecretKey(generalFunc.retrieveValue(CommonUtilities.PUBNUB_SEC_KEY));

        pubnub = new PubNub(pnConfiguration);

        pubnub.addListener(subscribeCallback);

        getLocUpdates = new GetLocationUpdates(mContext, Utils.LOCATION_UPDATE_MIN_DISTANCE_IN_MITERS);
        getLocUpdates.setLocationUpdatesListener(this);

        updatepassengerStatustask = new UpdateFrequentTask(generalFunc.parseIntegerValue(15, generalFunc.retrieveValue(Utils.FETCH_TRIP_STATUS_TIME_INTERVAL_KEY)) * 1000);
        updatepassengerStatustask.setTaskRunListener(this);
        updatepassengerStatustask.startRepeatingTask();


        subscribeToPrivateChannel();

        reConnectPubNub(2000);
        reConnectPubNub(5000);
        reConnectPubNub(10000);

    }

    public void setTripId(String iTripId, String iDriverId) {
        this.iTripId = iTripId;
        this.iDriverId = iDriverId;
    }

    public void reConnectPubNub(int duration) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                connectTopubNub();
            }
        }, duration);
    }

    private void connectTopubNub() {
        isPubnubInstKilled = false;
        pubnub.reconnect();
    }

    public void subscribeToPrivateChannel() {
        pubnub.subscribe()
                .channels(Arrays.asList("PASSENGER_" + generalFunc.getMemberId())) // subscribe to channels
                .execute();
    }

    public void unSubscribeToPrivateChannel() {
        pubnub.unsubscribe()
                .channels(Arrays.asList("PASSENGER_" + generalFunc.getMemberId())) // subscribe to channels
                .execute();
    }

    public void releaseInstances() {
        pubnub.removeListener(subscribeCallback);
        isPubnubInstKilled = true;
        if (updatepassengerStatustask != null) {
            updatepassengerStatustask.stopRepeatingTask();
            updatepassengerStatustask = null;
        }


        if (getLocUpdates != null) {
            getLocUpdates.stopLocationUpdates();
            getLocUpdates = null;
        }

    }

    public void subscribeToChannels(ArrayList<String> channels) {
        pubnub.subscribe()
                .channels(channels) // subscribe to channels
                .execute();
    }

    public void unSubscribeToChannels(ArrayList<String> channels) {
        pubnub.unsubscribe()
                .channels(channels)
                .execute();
    }

    SubscribeCallback subscribeCallback = new SubscribeCallback() {
        @Override
        public void status(final PubNub pubnub, final PNStatus status) {
            // the status object returned is always related to subscribe but could contain
            // information about subscribe, heartbeat, or errors
            // use the operationType to switch on different options
            if (status == null || status.getOperation() == null) {
//                Utils.printLog("status operation", ":::re connected::" + status.toString());
                connectTopubNub();
                return;
            }

            switch (status.getOperation()) {
                // let's combine unsubscribe and subscribe handling for ease of use
                case PNSubscribeOperation:
                case PNUnsubscribeOperation:
                    // note: subscribe statuses never have traditional
                    // errors, they just have categories to represent the
                    // different issues or successes that occur as part of subscribe
                    switch (status.getCategory()) {
                        case PNConnectedCategory:
                            // this is expected for a subscribe, this means there is no error or issue whatsoever
//                            Utils.printLog("PNConnectedCategory", ":::connected::" + status.toString());
                            if (mContext instanceof MainActivity) {
                                ((MainActivity) mContext).pubNubStatus(Utils.pubNubStatus_Connected);
                            }
                        case PNReconnectedCategory:
                            // this usually occurs if subscribe temporarily fails but reconnects. This means
                            // there was an error but there is no longer any issue
//                            Utils.printLog("PNReconnectedCategory", ":::re connected::" + status.toString());
                            if (mContext instanceof MainActivity) {
                                ((MainActivity) mContext).pubNubStatus(Utils.pubNubStatus_Connected);
                            }
                        case PNDisconnectedCategory:
                            // this is the expected category for an unsubscribe. This means there
                            // was no error in unsubscribing from everything
//                             Utils.printLog("PNDisconnectedCategory", ":::dis connected::" + status.toString());
//                            if(mContext instanceof MainActivity){
//                                ((MainActivity) mContext).pubNubStatus(Utils.pubNubStatus_DisConnected);
//                            }
                        case PNTimeoutCategory:
                        case PNUnexpectedDisconnectCategory:
                            // this is usually an issue with the internet connection, this is an error, handle appropriately
                            // retry will be called automatically
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    connectTopubNub();
                                }
                            }, 1500);

//                            Utils.printLog("PNUnexpectedDisconnect", ":::dis unexpected::" + status.toString());
                            if (mContext instanceof MainActivity) {
                                ((MainActivity) mContext).pubNubStatus(Utils.pubNubStatus_DisConnected);
                            }
                        case PNAccessDeniedCategory:
                            // this means that PAM does allow this client to subscribe to this
                            // channel and channel group configuration. This is another explicit error
//                             Utils.printLog("AccessDenied", ":::denied::" + status.toString());
//                            if(mContext instanceof MainActivity){
//                                ((MainActivity) mContext).pubNubStatus(Utils.pubNubStatus_Denied);
//                            }
                        default:
                            // More errors can be directly specified by creating explicit cases for other
                            // error categories of `PNStatusCategory` such as `PNTimeoutCategory` or `PNMalformedFilterExpressionCategory` or `PNDecryptionErrorCategory`
//                            Utils.printLog("Default", ":::default::" + status.toString());
                            if (mContext instanceof MainActivity) {
                                ((MainActivity) mContext).pubNubStatus(Utils.pubNubStatus_Error_Connection);
                            }
                    }

                case PNHeartbeatOperation:
                    // heartbeat operations can in fact have errors, so it is important to check first for an error.
                    // For more information on how to configure heartbeat notifications through the status
                    // PNObjectEventListener callback, consult <link to the PNCONFIGURATION heartbeart config>
                    if (status.isError()) {
                        // There was an error with the heartbeat operation, handle here
//                        Utils.printLog("PNHeartbeatOperation", ":::failed::" + status.toString());
                    } else {
                        // heartbeat operation was successful
//                        Utils.printLog("PNHeartbeatOperation", ":::success::" + status.toString());
                    }
                default: {
                    // Encountered unknown status type
//                    Utils.printLog("unknown status", ":::unknown::" + status.toString());
                }
            }
        }

        @Override
        public void message(PubNub pubnub, PNMessageResult message) {
            // handle incoming messages
            Utils.printLog("ONMessage", ":::got::" + message.getMessage().toString());

            dispatchMsg(message.getMessage().toString().replaceAll("^\"|\"$", ""));
        }

        @Override
        public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            // handle incoming presence data
            Utils.printLog("ON presence", ":::got::" + presence.toString());
        }
    };

    public boolean isJsonObj(String json) {

        try {
            JSONObject obj_check = new JSONObject(json);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void publishMsg(String channel, String message) {
//        .message(Arrays.asList("hello", "there"))
        pubnub.publish()
                .message(message)
                .channel(channel)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        // handle publish result, status always present, result if successful
                        // status.isError to see if error happened
                        Utils.printLog("Publish Res", "::::" + result.getTimetoken());
                    }
                });
    }


    private void getUpdatedPassengerStatus() {

        if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {

            return;
        }

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "configPassengerTripStatus");
        parameters.put("iTripId", iTripId);
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.userType);

        if (userLoc != null) {
            parameters.put("vLatitude", "" + userLoc.getLatitude());
            parameters.put("vLongitude", "" + userLoc.getLongitude());
        }

        if (Utils.checkText(iTripId)) {
            parameters.put("CurrentDriverIds", "" + iDriverId);
        } else if (mContext != null) {
            if (mContext instanceof MainActivity) {
                if (((MainActivity) mContext).getAvailableDriverIds() != null)
                    parameters.put("CurrentDriverIds", "" + ((MainActivity) mContext).getAvailableDriverIds());
            }


        }


        if (this.currentExeTask != null) {
            this.currentExeTask.cancel(true);
            this.currentExeTask=null;
            Utils.runGC();
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(mContext, parameters);
        this.currentExeTask = exeWebServer;
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                if (responseString != null && Utils.checkText(responseString)) {
                  //  Utils.printLog("Api", "configPassengerTripStatus ::" + responseString);

                    boolean isDataAvail = generalFunc.checkDataAvail(CommonUtilities.action_str, responseString);

                    if (isDataAvail == true) {
                        if (isPubnubInstKilled == false) {
                            dispatchMsg(generalFunc.getJsonValue(CommonUtilities.message_str, responseString));
                        }

                        JSONArray currentDrivers = generalFunc.getJsonArray("currentDrivers", responseString);
                        if (currentDrivers != null && currentDrivers.length() > 0 && isPubnubInstKilled == false) {

                            String PUBNUB_SUB_KEY = generalFunc.retrieveValue(CommonUtilities.PUBNUB_SUB_KEY);
                            String PUBNUB_PUB_KEY = generalFunc.retrieveValue(CommonUtilities.PUBNUB_PUB_KEY);
                            for (int i = 0; i < currentDrivers.length(); i++) {


                                String message = "";
                                try {
                                    String data_temp = currentDrivers.get(i).toString();
                                    JSONObject obj = new JSONObject();
                                    obj.put("MsgType", Utils.checkText(iTripId) ? "LocationUpdateOnTrip" : "LocationUpdate");
                                    obj.put("iDriverId", generalFunc.getJsonValue("iDriverId", data_temp));
                                    obj.put("vLatitude", generalFunc.getJsonValue("vLatitude", data_temp));
                                    obj.put("vLongitude", generalFunc.getJsonValue("vLongitude", data_temp));
                                    obj.put("ChannelName", Utils.pubNub_Update_Loc_Channel_Prefix + generalFunc.getJsonValue("iDriverId", data_temp));
                                    obj.put("LocTime", System.currentTimeMillis() + "");

                                    message = obj.toString();
                                    // dispatchMsg(message);

                                    if (PUBNUB_SUB_KEY.equals("") ||
                                            PUBNUB_PUB_KEY.equals("")){
                                        dispatchMsg(message);

                                    }
                                } catch (Exception e) {

                                }

                            }
                        }
                    }

                }


            }
        });
        exeWebServer.execute();
    }

    @Override
    public void onLocationUpdate(Location location) {
        if (location == null) {
            return;
        }
        this.userLoc = location;
    }

    @Override
    public void onTaskRun() {
        Utils.runGC();
        getUpdatedPassengerStatus();
    }


    private void dispatchMsg(String message) {

        if (isPubnubInstKilled == true) {
            return;
        }

        String finalMsg = message.replaceAll("^\"|\"$", "");

        if (!isJsonObj(finalMsg)) {
            finalMsg = message.replaceAll("\\\\", "");

            finalMsg = finalMsg.replaceAll("^\"|\"$", "");

            if (!isJsonObj(finalMsg)) {
                finalMsg = message.replace("\\\"", "\"").replaceAll("^\"|\"$", "");
            }

            finalMsg = finalMsg.replace("\\\\\"", "\\\"");
        }

        if (mContext instanceof MainActivity) {
            ((MainActivity) mContext).pubNubMsgArrived(finalMsg);
        }

    }

}
