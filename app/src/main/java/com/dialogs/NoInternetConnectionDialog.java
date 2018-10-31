package com.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sara.passenger.MainActivity;
import com.sara.passenger.R;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.InternetConnection;
import com.general.files.StartActProcess;
import com.general.files.UpdateFrequentTask;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import java.util.HashMap;

/**
 * Created by Admin on 30-08-2017.
 */

public class NoInternetConnectionDialog implements UpdateFrequentTask.OnTaskRunCalled {

    Dialog dialogNoInternetConnection;
    Activity mContext1;
    Context mContext;
    GeneralFunctions generalFunc;
    ExecuteWebServerUrl executeWebServerUrl;
    boolean showDialog;
    private UpdateFrequentTask updateInternetConnTask;
    boolean isTaskKilled = false;

    public NoInternetConnectionDialog(Context mContext, boolean showDialog) {

        this.mContext = mContext;
        this.generalFunc = new GeneralFunctions(mContext);
        this.showDialog = showDialog;

        if (showDialog) {
            show();
        }

    }

    public void show() {

        dialogNoInternetConnection = new Dialog(mContext, R.style.Theme_Dialog);
        dialogNoInternetConnection.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogNoInternetConnection.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogNoInternetConnection.setContentView(R.layout.no_location_view);
        ((MTextView) dialogNoInternetConnection.findViewById(R.id.noLocTitleTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_NO_INTERNET_TITLE"));
        ((MTextView) dialogNoInternetConnection.findViewById(R.id.noLocMsgTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_NO_INTERNET_SUB_TITLE"));
        ((MTextView) dialogNoInternetConnection.findViewById(R.id.settingTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_SETTINGS"));
        ((MTextView) dialogNoInternetConnection.findViewById(R.id.pickupredirectTxt)).setVisibility(View.GONE);
        ((MTextView) dialogNoInternetConnection.findViewById(R.id.pickupredirectTxt)).setOnClickListener(null);


        if (mContext instanceof MainActivity && ((MainActivity) mContext).getMainHeaderFrag() != null && ((MainActivity) mContext).getMainHeaderFrag().menuImgView.getVisibility() == View.VISIBLE) {
            ((ImageView) dialogNoInternetConnection.findViewById(R.id.nolocmenuImgView)).setVisibility(View.VISIBLE);
            ((ImageView) dialogNoInternetConnection.findViewById(R.id.nolocbackImgView)).setVisibility(View.GONE);
        }else if (mContext instanceof MainActivity && ((MainActivity) mContext).requestNearestCab!=null) {
            ((ImageView) dialogNoInternetConnection.findViewById(R.id.nolocmenuImgView)).setVisibility(View.GONE);
            ((ImageView) dialogNoInternetConnection.findViewById(R.id.nolocbackImgView)).setVisibility(View.VISIBLE);
        } else if (mContext instanceof Activity) {
            ((ImageView) dialogNoInternetConnection.findViewById(R.id.nolocmenuImgView)).setVisibility(View.GONE);
            ((ImageView) dialogNoInternetConnection.findViewById(R.id.nolocbackImgView)).setVisibility(View.VISIBLE);
        }
//        } else {
//            ((ImageView) dialogNoInternetConnection.findViewById(R.id.nolocmenuImgView)).setVisibility(View.GONE);
//            ((ImageView) dialogNoInternetConnection.findViewById(R.id.nolocbackImgView)).setVisibility(View.GONE);
//
//        }


        (dialogNoInternetConnection.findViewById(R.id.nolocmenuImgView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (((MainActivity) mContext).addDrawer != null) {
                        setTaskKilledValue(true);
                        ((MainActivity) mContext).addDrawer.checkDrawerState(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        (dialogNoInternetConnection.findViewById(R.id.settingTxt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((MainActivity) mContext).settingTxt.performClick();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        (dialogNoInternetConnection.findViewById(R.id.nolocbackImgView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mContext instanceof Activity) {
                    setTaskKilledValue(true);
                    try {
//                        ((MainActivity) mContext).onBackPressed();
                        ((MainActivity) mContext).callBackEvent(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        if (generalFunc.isRTLmode() == true) {
            generalFunc.forceRTLIfSupported(dialogNoInternetConnection);
        }


        dialogNoInternetConnection.setCancelable(false);
        dialogNoInternetConnection.setCanceledOnTouchOutside(false);
        dialogNoInternetConnection.show();

        updateInternetConnTask = new UpdateFrequentTask(200);
        onResumeCalled();
        updateInternetConnTask.setTaskRunListener(this);

    }

    public void setTaskKilledValue(boolean isTaskKilled) {
        this.isTaskKilled = isTaskKilled;

        if (isTaskKilled == true) {
            onPauseCalled();
        }
    }


    public void onPauseCalled() {

        if (updateInternetConnTask != null) {
            dismissDialog();
//            executeWebServerUrl.setResponse();
            updateInternetConnTask.stopRepeatingTask();
        }
    }


    public void onResumeCalled() {
        if (updateInternetConnTask != null && isTaskKilled == false) {
            updateInternetConnTask.startRepeatingTask();
        }
    }

    public void dismissDialog() {
        try {
            if (dialogNoInternetConnection != null) {
                dialogNoInternetConnection.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isShowingDialog() {
        boolean isShowing = false;
        if (dialogNoInternetConnection != null) {
            isShowing = dialogNoInternetConnection.isShowing();
        }
        return isShowing;
    }

    @Override
    public void onTaskRun() {
        if ((new InternetConnection(mContext).isNetworkConnected() && new InternetConnection(mContext).check_int())) {
            setTaskKilledValue(true);
        }
    }

}
