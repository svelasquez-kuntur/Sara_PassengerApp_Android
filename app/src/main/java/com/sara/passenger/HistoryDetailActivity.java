package com.sara.passenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.blurry.Blurry;
import com.fragments.CustomSupportMapFragment;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.SelectableRoundedImageView;
import com.view.editBox.MaterialEditText;
import com.view.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.sara.passenger.R.id.ratingBar;
import static com.utils.CommonUtilities.APP_DESTINATION_MODE;

public class HistoryDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    MTextView subTitleTxt;
    ImageView backImgView;
   // CustomSupportMapFragment map;
    GoogleMap gMap;
    LinearLayout fareDetailDisplayArea;
    View convertView = null;

    LinearLayout beforeServiceArea, afterServiceArea;
    String before_serviceImg_url = "";
    String after_serviceImg_url = "";
    String isRatingDone = "";
    private int rateBtnId;
    MButton btn_type2;
    private MaterialEditText commentBox;

    String userProfileJson;
    MTextView ratingDriverHTxt;

    LinearLayout profilebgarea;

    MTextView cartypeTxt;

    MTextView ufxratingDriverHTxt;
    SimpleRatingBar ufxratingBar;

    MTextView tipHTxt, tipamtTxt, tipmsgTxt;
    CardView tiparea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);


        generalFunc = new GeneralFunctions(getActContext());


        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        subTitleTxt = (MTextView) findViewById(R.id.subTitleTxt);
        commentBox = (MaterialEditText) findViewById(R.id.commentBox);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        fareDetailDisplayArea = (LinearLayout) findViewById(R.id.fareDetailDisplayArea);
        afterServiceArea = (LinearLayout) findViewById(R.id.afterServiceArea);
        beforeServiceArea = (LinearLayout) findViewById(R.id.beforeServiceArea);
        ratingDriverHTxt = (MTextView) findViewById(R.id.ratingDriverHTxt);
        //map = (CustomSupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapV2);
        profilebgarea = (LinearLayout) findViewById(R.id.profilebgarea);
        cartypeTxt = (MTextView) findViewById(R.id.cartypeTxt);

        ufxratingDriverHTxt = (MTextView) findViewById(R.id.ufxratingDriverHTxt);
        ufxratingBar = (SimpleRatingBar) findViewById(R.id.ufxratingBar);

        tipHTxt = (MTextView) findViewById(R.id.tipHTxt);
        tipamtTxt = (MTextView) findViewById(R.id.tipamtTxt);
        tipmsgTxt = (MTextView) findViewById(R.id.tipmsgTxt);

        tiparea = (CardView) findViewById(R.id.tiparea);


        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        rateBtnId = Utils.generateViewId();
        btn_type2.setId(rateBtnId);

        btn_type2.setOnClickListener(new setOnClickList());


        setLabels();
        setData();


        commentBox.setTextColor(getResources().getColor(R.color.mdtp_transparent_black));

       // map.getMapAsync(this);

        backImgView.setOnClickListener(new setOnClickList());
        subTitleTxt.setOnClickListener(new setOnClickList());
        afterServiceArea.setOnClickListener(new setOnClickList());
        beforeServiceArea.setOnClickListener(new setOnClickList());

//        map.setListener(new CustomSupportMapFragment.OnTouchListener() {
//            @Override
//            public void onTouch() {
//                ((ScrollView) findViewById(R.id.scrollContainer)).requestDisallowInterceptTouchEvent(true);
//            }
//        });

    }


    public void setLabels() {
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RECEIPT_HEADER_TXT"));
        subTitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_GET_RECEIPT_TXT"));

        ((MTextView) findViewById(R.id.headerTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_THANKS_RIDING_TXT"));
        ((MTextView) findViewById(R.id.rideNoHTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_BOOKING") + "#");
        ((MTextView) findViewById(R.id.ratingHTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_RATING"));
        ((MTextView) findViewById(R.id.driverHTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_DRIVER"));
        ((MTextView) findViewById(R.id.tripdateHTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_TRIP_REQUEST_DATE_TXT"));
        ((MTextView) findViewById(R.id.pickUpHTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_PICKUP_LOCATION_HEADER_TXT"));
        ((MTextView) findViewById(R.id.dropOffHTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_DROP_OFF_LOCATION_TXT"));
        ((MTextView) findViewById(R.id.chargesHTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_CHARGES_TXT"));
        btn_type2.setText(generalFunc.retrieveLangLBl("Rate", "LBL_RATE_DRIVER_TXT"));
        ratingDriverHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RATING"));
        ufxratingDriverHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RATE_HEADING_DRIVER_TXT"));

        tipHTxt.setText(generalFunc.retrieveLangLBl("Tip Amount", "LBL_TIP_AMOUNT"));
        tipmsgTxt.setText(generalFunc.retrieveLangLBl("Thank you for giving tip for this trip.", "LBL_TIP_INFO_SHOW_RIDER"));
    }

    public void setData() {
        String tripData = getIntent().getStringExtra("TripData");

        ((MTextView) findViewById(R.id.rideNoVTxt)).setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("vRideNo", tripData)));
        ((MTextView) findViewById(R.id.nameDriverVTxt)).setText(generalFunc.getJsonValue("vName", generalFunc.getJsonValue("DriverDetails", tripData)) + " " +
                generalFunc.getJsonValue("vLastName", generalFunc.getJsonValue("DriverDetails", tripData)));
        ((MTextView) findViewById(R.id.tripdateVTxt)).setText(generalFunc.getDateFormatedType(generalFunc.getJsonValue("tTripRequestDateOrig", tripData), Utils.OriginalDateFormate, Utils.DateFormatewithTime));

        ((MTextView) findViewById(R.id.pickUpVTxt)).setText(generalFunc.getJsonValue("tSaddress", tripData));
        ((MTextView) findViewById(R.id.dropOffVTxt)).setText(generalFunc.getJsonValue("tDaddress", tripData));
        if (generalFunc.getJsonValue("tDaddress", tripData).equals("")) {
            (findViewById(R.id.dropOffVTxt)).setVisibility(View.GONE);
            (findViewById(R.id.dropOffHTxt)).setVisibility(View.GONE);
        }

        userProfileJson = generalFunc.retrieveValue(CommonUtilities.USER_PROFILE_JSON);

        if (!generalFunc.getJsonValue("fTipPrice", tripData).equals("0") && !generalFunc.getJsonValue("fTipPrice", tripData).equals("0.0") &&
                !generalFunc.getJsonValue("fTipPrice", tripData).equals("0.00") &&
                !generalFunc.getJsonValue("fTipPrice", tripData).equals("")) {
            tiparea.setVisibility(View.VISIBLE);

            tipamtTxt.setText(generalFunc.getJsonValue("fTipPrice", tripData));

        } else {
            tiparea.setVisibility(View.GONE);
        }


        cartypeTxt.setText(generalFunc.getJsonValue("carTypeName", tripData));

        String trip_status_str = generalFunc.getJsonValue("iActive", tripData);

        isRatingDone = generalFunc.getJsonValue("is_rating", tripData);

        if (isRatingDone.equalsIgnoreCase("No") && trip_status_str.contains("Finished")) {
            findViewById(R.id.rateDriverArea).setVisibility(View.VISIBLE);
            findViewById(R.id.rateCardDriverArea).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.rateDriverArea).setVisibility(View.GONE);
            findViewById(R.id.rateCardDriverArea).setVisibility(View.GONE);
        }

        if (trip_status_str.contains("Canceled")) {
            ((MTextView) findViewById(R.id.tripStatusTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_CANCELED_TRIP_TXT"));
            (findViewById(R.id.tripDetailArea)).setVisibility(View.VISIBLE);
        } else if (trip_status_str.contains("Finished")) {

            ((MTextView) findViewById(R.id.tripStatusTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_FINISHED_TRIP_TXT"));

            (findViewById(R.id.tripDetailArea)).setVisibility(View.VISIBLE);
            subTitleTxt.setVisibility(View.VISIBLE);
        } else {
            ((MTextView) findViewById(R.id.tripStatusTxt)).setText(trip_status_str);

        }

        if (generalFunc.getJsonValue("vTripPaymentMode", tripData).equals("Cash")) {
            ((MTextView) findViewById(R.id.paymentTypeTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_CASH_PAYMENT_TXT"));
        } else {
            ((MTextView) findViewById(R.id.paymentTypeTxt)).setText(generalFunc.retrieveLangLBl("Card Payment", "LBL_CARD_PAYMENT"));
            ((ImageView) findViewById(R.id.paymentTypeImgeView)).setImageResource(R.mipmap.ic_card_new);
        }

        if (generalFunc.getJsonValue("eCancelled", tripData).equals("Yes")) {
            //  subTitleTxt.setVisibility(View.GONE);
            ((MTextView) findViewById(R.id.tripStatusTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_PREFIX_TRIP_CANCEL_DRIVER") + " " +
                    generalFunc.getJsonValue("vCancelReason", tripData));
        }

        ((SimpleRatingBar) findViewById(ratingBar)).setRating(generalFunc.parseFloatValue(0, generalFunc.getJsonValue("TripRating", tripData)));


        String driverImageName = generalFunc.getJsonValue("vImage", generalFunc.getJsonValue("DriverDetails", tripData));
        if (driverImageName == null || driverImageName.equals("") || driverImageName.equals("NONE")) {
            ((SelectableRoundedImageView) findViewById(R.id.driverImgView)).setImageResource(R.mipmap.ic_no_pic_user);
        } else {
            String image_url = CommonUtilities.SERVER_URL_PHOTOS + "upload/Driver/" + generalFunc.getJsonValue("iDriverId", tripData) + "/"
                    + driverImageName;
            Picasso.with(getActContext())
                    .load(image_url)
                    .placeholder(R.mipmap.ic_no_pic_user)
                    .error(R.mipmap.ic_no_pic_user)
                    .into((SelectableRoundedImageView) findViewById(R.id.driverImgView));

        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Blurry.with(getActContext())
                        .radius(15)
                        .sampling(8)
                        .color(Color.argb(25, 0, 255, 255))
                        .async()
                        .capture(findViewById(R.id.driverImgView))
                        .into((ImageView) findViewById(R.id.profileimageback));
            }
        }, 300);

        if (generalFunc.retrieveValue(APP_DESTINATION_MODE).equalsIgnoreCase(CommonUtilities.NONE_DESTINATION)) {
//            (findViewById(R.id.dropOffHTxt)).setVisibility(View.GONE);
//            (findViewById(R.id.dropOffVTxt)).setVisibility(View.GONE);
        }

        if (generalFunc.getJsonValue("eType", tripData).equalsIgnoreCase("UberX") || generalFunc.getJsonValue("eFareType", tripData).equalsIgnoreCase("Fixed")) {
//            findViewById(R.id.tripDetailArea).setVisibility(View.GONE);
            findViewById(R.id.service_area).setVisibility(View.GONE);
            findViewById(R.id.serviceHTxt).setVisibility(View.GONE);
            findViewById(R.id.photoArea).setVisibility(View.VISIBLE);


            ((MTextView) findViewById(R.id.beforeImgHTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_BEFORE_SERVICE"));
            ((MTextView) findViewById(R.id.afterImgHTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_AFTER_SERVICE"));

            if (!TextUtils.isEmpty(generalFunc.getJsonValue("vBeforeImage", tripData))) {
                findViewById(R.id.beforeServiceArea).setVisibility(View.VISIBLE);
                before_serviceImg_url = generalFunc.getJsonValue("vBeforeImage", tripData);
                displayPic(before_serviceImg_url, (ImageView) findViewById(R.id.iv_before_img), "before");
            } else {
                findViewById(R.id.beforeServiceArea).setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(generalFunc.getJsonValue("vAfterImage", tripData))) {
                findViewById(R.id.afterServiceArea).setVisibility(View.VISIBLE);
                after_serviceImg_url = generalFunc.getJsonValue("vAfterImage", tripData);
                displayPic(after_serviceImg_url, (ImageView) findViewById(R.id.iv_after_img), "after");
            } else {
                findViewById(R.id.afterServiceArea).setVisibility(View.GONE);
            }

            ((MTextView) findViewById(R.id.pickUpVTxt)).setText(generalFunc.getJsonValue("tSaddress", tripData));
            ((MTextView) findViewById(R.id.serviceTypeVTxt)).setText(generalFunc.getJsonValue("vVehicleCategory", tripData) + " - " + generalFunc.getJsonValue("vVehicleType", tripData));
            ((MTextView) findViewById(R.id.serviceTypeHTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_Car_Type"));


        } else {
            findViewById(R.id.tripDetailArea).setVisibility(View.VISIBLE);
            findViewById(R.id.service_area).setVisibility(View.GONE);
            findViewById(R.id.serviceHTxt).setVisibility(View.GONE);
            findViewById(R.id.photoArea).setVisibility(View.GONE);
        }

        boolean FareDetailsArrNew = generalFunc.isJSONkeyAvail("HistoryFareDetailsNewArr", tripData);

        JSONArray FareDetailsArrNewObj = null;
        if (FareDetailsArrNew == true) {
            FareDetailsArrNewObj = generalFunc.getJsonArray("HistoryFareDetailsNewArr", tripData);
        }
        if (FareDetailsArrNewObj != null)
            addFareDetailLayout(FareDetailsArrNewObj);
    }


    public void displayPic(String image_url, ImageView view, final String imgType) {

        Picasso.with(getActContext())
                .load(image_url)
                .into(view, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if (imgType.equalsIgnoreCase("before")) {
                            findViewById(R.id.before_loading).setVisibility(View.GONE);
                            findViewById(R.id.iv_before_img).setVisibility(View.VISIBLE);
                        } else if (imgType.equalsIgnoreCase("after")) {
                            findViewById(R.id.after_loading).setVisibility(View.GONE);
                            findViewById(R.id.iv_after_img).setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError() {
                        if (imgType.equalsIgnoreCase("before")) {
                            findViewById(R.id.before_loading).setVisibility(View.VISIBLE);
                            findViewById(R.id.iv_before_img).setVisibility(View.GONE);
                        } else if (imgType.equalsIgnoreCase("after")) {
                            findViewById(R.id.after_loading).setVisibility(View.VISIBLE);
                            findViewById(R.id.iv_after_img).setVisibility(View.GONE);

                        }
                    }
                });

    }

    private void addFareDetailLayout(JSONArray jobjArray) {

        if (fareDetailDisplayArea.getChildCount() > 0) {
            fareDetailDisplayArea.removeAllViewsInLayout();
        }

        for (int i = 0; i < jobjArray.length(); i++) {
            JSONObject jobject = generalFunc.getJsonObject(jobjArray, i);
            try {
                addFareDetailRow(jobject.names().getString(0), jobject.get(jobject.names().getString(0)).toString(), (jobjArray.length() - 1) == i ? true : false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void addFareDetailRow(String row_name, String row_value, boolean isLast) {
        LayoutInflater infalInflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.design_fare_deatil_row, null);
        TableRow FareDetailRow = (TableRow) convertView.findViewById(R.id.FareDetailRow);
        TableLayout fair_area_table_layout = (TableLayout) convertView.findViewById(R.id.fair_area);
        MTextView titleHTxt = (MTextView) convertView.findViewById(R.id.titleHTxt);
        MTextView titleVTxt = (MTextView) convertView.findViewById(R.id.titleVTxt);

        titleHTxt.setText(row_name);
        titleVTxt.setText(row_value);

        if (isLast == true) {
            TableLayout.LayoutParams tableRowParams =
                    new TableLayout.LayoutParams
                            (TableLayout.LayoutParams.FILL_PARENT, Utils.pxToDp(getActContext(), 40));
            tableRowParams.setMargins(0, 10, 0, 0);

            fair_area_table_layout.setLayoutParams(tableRowParams);
            FareDetailRow.setLayoutParams(tableRowParams);
            fair_area_table_layout.setBackgroundColor(Color.parseColor("#EBEBEB"));
            fair_area_table_layout.getChildAt(0).setPadding(5, 0, 5, 10);
            titleVTxt.setTextColor(Color.parseColor("#f9970b"));
            titleHTxt.setTextColor(Color.parseColor("#000000"));

        } else {
            titleHTxt.setTextColor(Color.parseColor("#303030"));
            titleVTxt.setTextColor(Color.parseColor("#000000"));
        }
        if (convertView != null)
            fareDetailDisplayArea.addView(convertView);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;

        String tripData = getIntent().getStringExtra("TripData");

        String tStartLat = generalFunc.getJsonValue("tStartLat", tripData);
        String tStartLong = generalFunc.getJsonValue("tStartLong", tripData);
        String tEndLat = generalFunc.getJsonValue("tEndLat", tripData);
        String tEndLong = generalFunc.getJsonValue("tEndLong", tripData);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Marker pickUpMarker = null;
        Marker destMarker = null;
        if (!tStartLat.equals("") && !tStartLat.equals("0.0") && !tStartLong.equals("") && !tStartLong.equals("0.0")) {
            LatLng pickUpLoc = new LatLng(generalFunc.parseDoubleValue(0.0, tStartLat), generalFunc.parseDoubleValue(0.0, tStartLong));
            MarkerOptions marker_opt = new MarkerOptions().position(pickUpLoc);
            marker_opt.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_source_marker)).anchor(0.5f, 0.5f);
            pickUpMarker = this.gMap.addMarker(marker_opt);

            builder.include(pickUpLoc);

            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pickUpLoc, 10));
        }

        if (generalFunc.getJsonValue("iActive", tripData).equals("Finished")) {
            if (!tEndLat.equals("") && !tEndLat.equals("0.0") && !tEndLong.equals("") && !tEndLong.equals("0.0")) {
                LatLng destLoc = new LatLng(generalFunc.parseDoubleValue(0.0, tEndLat), generalFunc.parseDoubleValue(0.0, tEndLong));
                MarkerOptions marker_opt = new MarkerOptions().position(destLoc);
                marker_opt.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_dest_marker)).anchor(0.5f, 0.5f);
                destMarker = this.gMap.addMarker(marker_opt);

                builder.include(destLoc);

                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destLoc, 10));
            }
        }


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width, Utils.dipToPixels(getActContext(), 200), 100));
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                // TODO Auto-generated method stub
                marker.hideInfoWindow();

                return true;
            }
        });

        if (pickUpMarker != null && destMarker != null) {
            drawRoute(pickUpMarker.getPosition(), destMarker.getPosition());
        }

    }

    public void drawRoute(LatLng pickUpLoc, LatLng destinationLoc) {
        String originLoc = pickUpLoc.latitude + "," + pickUpLoc.longitude;
        String destLoc = destinationLoc.latitude + "," + destinationLoc.longitude;
        String serverKey = getResources().getString(R.string.google_api_get_address_from_location_serverApi);
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + originLoc + "&destination=" + destLoc + "&sensor=true&key=" + serverKey + "&language=" + generalFunc.retrieveValue(CommonUtilities.GOOGLE_MAP_LANGUAGE_CODE_KEY) + "&sensor=true";

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), url, true);

        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                if (responseString != null && !responseString.equals("")) {

                    String status = generalFunc.getJsonValue("status", responseString);

                    if (status.equals("OK")) {

                        JSONArray obj_routes = generalFunc.getJsonArray("routes", responseString);
                        if (obj_routes != null && obj_routes.length() > 0) {

                            PolylineOptions lineOptions = generalFunc.getGoogleRouteOptions(responseString, Utils.dipToPixels(getActContext(), 5), getActContext().getResources().getColor(R.color.appThemeColor_1));

                            if (lineOptions != null) {
                                gMap.addPolyline(lineOptions);
                            }
                        }

                    }

                }
            }
        });
        exeWebServer.execute();
    }

    public void sendReceipt() {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getReceipt");
        parameters.put("UserType", CommonUtilities.app_type);
        parameters.put("iTripId", generalFunc.getJsonValue("iTripId", getIntent().getStringExtra("TripData")));

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                if (responseString != null && !responseString.equals("")) {

                    boolean isDataAvail = GeneralFunctions.checkDataAvail(CommonUtilities.action_str, responseString);

                    if (isDataAvail == true) {
                        generalFunc.showGeneralMessage("",
                                generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(CommonUtilities.message_str, responseString)));
                    } else {
                        generalFunc.showGeneralMessage("",
                                generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(CommonUtilities.message_str, responseString)));
                    }
                } else {
                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }

    public Context getActContext() {
        return HistoryDetailActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            switch (view.getId()) {
                case R.id.backImgView:
                    HistoryDetailActivity.super.onBackPressed();
                    break;

                case R.id.subTitleTxt:
                    sendReceipt();
                    break;

                case R.id.beforeServiceArea:
                    new StartActProcess(getActContext()).openURL(before_serviceImg_url);
                    break;

                case R.id.afterServiceArea:
                    new StartActProcess(getActContext()).openURL(after_serviceImg_url);
                    break;
            }

            if (view.getId() == rateBtnId) {
                if (((SimpleRatingBar) findViewById(R.id.ufxratingBar)).getRating() < 1) {
                    generalFunc.showMessage(generalFunc.getCurrentView(HistoryDetailActivity.this), generalFunc.retrieveLangLBl("", "LBL_ERROR_RATING_DIALOG_TXT"));
                    return;
                }
                submitRating();
            }
        }
    }

    public void submitRating() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "submitRating");
        parameters.put("iGeneralUserId", generalFunc.getMemberId());
        parameters.put("tripID", generalFunc.getJsonValue("iTripId", getIntent().getStringExtra("TripData")));
        parameters.put("rating", "" + ufxratingBar.getRating());
        parameters.put("message", Utils.getText(commentBox));
        parameters.put("UserType", CommonUtilities.app_type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                if (responseString != null && !responseString.equals("")) {

                    boolean isDataAvail = GeneralFunctions.checkDataAvail(CommonUtilities.action_str, responseString);

                    if (isDataAvail == true) {

                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                        generateAlert.setCancelable(true);
                        generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                            @Override
                            public void handleBtnClick(int btn_id) {
                                generateAlert.closeAlertBox();

                                Intent returnIntent = new Intent();
//                                returnIntent.putExtra("UserProfileJson", getIntent().getStringExtra("UserProfileJson"));
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        });
                        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", "LBL_SUCCESS_RATING_SUBMIT_TXT"));
                        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                        generateAlert.showAlertBox();


                    } else {
                        resetRatingData();
                        generalFunc.showGeneralMessage("",
                                generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(CommonUtilities.message_str, responseString)));
                    }
                } else {
                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }

    private void resetRatingData() {
        commentBox.setText("");
        ((RatingBar) findViewById(ratingBar)).setRating(0);
    }
}
