package com.songu.pom.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.songu.pom.R;
import com.songu.pom.activity.MainActivity;
import com.songu.pom.doc.Config;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.service.IServiceResult;
import com.songu.pom.service.ServiceManager;

public class ConsentFragment extends Fragment implements View.OnClickListener,IServiceResult{

    public View mRootView = null;
    public Button btnYes,btnNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_consent, null);
        }
        initView();
        return mRootView;
    }

    public void initView()
    {
        btnYes = (Button) mRootView.findViewById(R.id.btnConfirmYes);
        btnNo = (Button) mRootView.findViewById(R.id.btnConfirmNo);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnConfirmNo:
                actionRefuse();
                break;
            case R.id.btnConfirmYes:
                actionAgree();
                break;
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    getActivity().finish();
                    break;
            }
        }
    };

    public void actionRefuse()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        final AlertDialog dlgFinish = builder.setTitle("Warning").setMessage("You will not be able to use the App. Would you like to change your answer?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).create();

        dlgFinish.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dlgFinish.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(0xff006699);
                dlgFinish.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xff006699);
            }
        });
        dlgFinish.show();
    }
    public void actionAgree()
    {
        ((MainActivity)getActivity()).showLoader();
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String token = pref.getString("regId","");
        ServiceManager.serviceAgreeTerm(Globals.mAccount.mCode,token,this);
    }


    @Override
    public void onResponse(int code) {
        ((MainActivity)getActivity()).dismissLoader();
        switch (code) {
            case 200://Success Agree Term Service
                if (Globals.e_userType == Enums.USERTYPE.PATIENT) {
                    Globals.e_mode = Enums.MODE.PATIENT_HOME;
                    Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                    ((MainActivity) getActivity()).setFragment();
                }
                else if (Globals.e_userType == Enums.USERTYPE.USER)
                {
                    Globals.e_mode = Enums.MODE.MEMBER_HOME;
                    Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                    ((MainActivity) getActivity()).setFragment();
                }
                break;
            case 400://Fail
                break;
        }
    }
}
