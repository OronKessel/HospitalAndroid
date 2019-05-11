package com.songu.pom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.songu.pom.R;
import com.songu.pom.activity.MainActivity;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.service.IServiceResult;
import com.songu.pom.service.ServiceManager;

public class ForgetPasswordFragment extends Fragment implements View.OnClickListener,IServiceResult {

    public View mRootView = null;
    public EditText editEmail;
    public TextView txtSend;
    public ImageView imgSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_forget_password, null);
        }
        initView();
        return mRootView;
    }

    public void initView()
    {
        editEmail = (EditText) mRootView.findViewById(R.id.txtForgetEmail);
        txtSend = (TextView) mRootView.findViewById(R.id.txtForgetSend);
        imgSend = (ImageView) mRootView.findViewById(R.id.imgForgetSend);
        txtSend.setOnClickListener(this);
        imgSend.setOnClickListener(this);
    }

    public void actionSend()
    {
        String strEmail = editEmail.getText().toString();
        if (strEmail.equals(""))
        {
            ((MainActivity)getActivity()).dlgError.setMessage("Please enter email address");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }
        ((MainActivity)getActivity()).showLoader();
        ServiceManager.serviceForgetPassword(strEmail,this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.txtForgetSend:
            case R.id.imgForgetSend:
                actionSend();
                break;
        }
    }

    @Override
    public void onResponse(int code) {
        ((MainActivity)getActivity()).dismissLoader();
        switch (code)
        {
            case 200://Success sent email password
                ((MainActivity)getActivity()).dlgError.setMessage("A temporary password has been emailed to you");
                ((MainActivity)getActivity()).dlgError.setTitle("Password Reset");
                ((MainActivity)getActivity()).dlgError.show();
                Globals.gForgetEmail = editEmail.getText().toString();
                Globals.e_mode = Enums.MODE.RESETPASSWORD;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                ((MainActivity)getActivity()).setFragment();
                break;
            case 401://Fail sent email
                break;
        }
    }
}
