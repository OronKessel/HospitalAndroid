package com.songu.pom.fragment.patient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.songu.pom.R;
import com.songu.pom.activity.MainActivity;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.service.IServiceResult;
import com.songu.pom.service.ServiceManager;
import com.songu.pom.util.Utils;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener,IServiceResult {
//Change Password Screen
    public View mRootView = null;
    public EditText editOldPw,editNewPw,editConfirmPw;
    public LinearLayout layoutDone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_patient_changepassword, null);
        }
        initView();
        return mRootView;
    }

    public void initView()
    {
        editOldPw = (EditText) mRootView.findViewById(R.id.txtOldPassword);
        editNewPw = (EditText)  mRootView.findViewById(R.id.txtNewPassword);
        editConfirmPw = (EditText) mRootView.findViewById(R.id.txtConfirmPassword);
        layoutDone = (LinearLayout) mRootView.findViewById(R.id.layoutChangeSubmit);

        layoutDone.setOnClickListener(this);
    }

    public void actionDone() //When click done
    {
        String strOldPw = editOldPw.getText().toString();
        String strNewPw = editNewPw.getText().toString();
        String strConfirmPw = editConfirmPw.getText().toString();

        if (strOldPw.equals(""))
        {
            ((MainActivity)getActivity()).dlgError.setTitle("Confirm Password");
            ((MainActivity)getActivity()).dlgError.setMessage("Please enter Old Password");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }
        if (strNewPw.equals(""))
        {
            ((MainActivity)getActivity()).dlgError.setTitle("Confirm Password");
            ((MainActivity)getActivity()).dlgError.setMessage("Please enter New Password");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }
        if (strConfirmPw.equals(""))
        {
            ((MainActivity)getActivity()).dlgError.setTitle("Confirm Password");
            ((MainActivity)getActivity()).dlgError.setMessage("Please enter Confirm Password");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }
        if (!strConfirmPw.equals(strNewPw))
        {
            ((MainActivity)getActivity()).dlgError.setTitle("Confirm Password");
            ((MainActivity)getActivity()).dlgError.setMessage("Passwords do not match");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }
        if (strNewPw.length() < 6 || !Utils.isValidPassword(strNewPw))
        {
            ((MainActivity)getActivity()).dlgError.setTitle("Confirm Password");
            ((MainActivity)getActivity()).dlgError.setMessage("Password must contain minimum 6 characters at least 1 Alphabet, 1 Number and 1 Special Character");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }
        ((MainActivity)getActivity()).showLoader();
        if (Globals.e_mode == Enums.MODE.RESETPASSWORD) //If Reset Password screen
        {
            ServiceManager.serviceResetPassword(strOldPw, strNewPw, this);
        }
        else {
            if (Globals.e_userType == Enums.USERTYPE.PATIENT)
                ServiceManager.serviceChangePassword(strOldPw, strNewPw, this);
            else
                ServiceManager.serviceChangeMemberPassword(strOldPw, strNewPw, this);
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.layoutChangeSubmit:
                actionDone();
                break;
        }
    }

    @Override
    public void onResponse(int code) {
        ((MainActivity)getActivity()).dismissLoader();
        switch (code)
        {
            case 200://Success Call Change Password
                Globals.e_mode = Enums.MODE.PATIENT_HOME;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                ((MainActivity)getActivity()).setFragment();
                break;
            case 201://Success Call Reset Password
                Globals.e_mode = Enums.MODE.MENULOGIN;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                ((MainActivity)getActivity()).setFragment();
                break;
            case 400:
                ((MainActivity)getActivity()).dlgError.setTitle("Confirm Password");
                ((MainActivity)getActivity()).dlgError.setMessage("Old Password does not correct");
                ((MainActivity)getActivity()).dlgError.show();
                break;
        }
    }
}

