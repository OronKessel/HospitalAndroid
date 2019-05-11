package com.songu.pom.fragment.patient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.songu.pom.R;
import com.songu.pom.activity.MainActivity;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.service.IServiceResult;
import com.songu.pom.service.ServiceManager;

public class ProfileFragment extends Fragment implements View.OnClickListener,IServiceResult{//Profile Screen

    public View mRootView = null;
    public LinearLayout layoutDone;
    public EditText editFirstName,editLastName,editEmail,editPhone;
    public LinearLayout layoutFemale,layoutMale;
    public ImageView imgFemale,imgMale;
    public boolean isMale = true;

    public TextView txtEmail;
    public LinearLayout layoutEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_patient_profile, null);
        }
        initView();
        initGender();
        getProfile();
        return mRootView;
    }

    public void initGender()
    {
        imgFemale.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick_2));
        imgMale.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick_2));
    }
    public void setGender()
    {
        initGender();
        if (isMale)
            imgMale.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick_1));
        else
            imgFemale.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick_1));
    }
    public void initView()
    {
        layoutDone = (LinearLayout) mRootView.findViewById(R.id.layoutProfileSubmit);
        editFirstName = (EditText) mRootView.findViewById(R.id.txtFirstName);
        editLastName = (EditText) mRootView.findViewById(R.id.txtLastName);
        editEmail = (EditText) mRootView.findViewById(R.id.txtProfileEmail);
        editPhone = (EditText) mRootView.findViewById(R.id.txtProfileNumber);
        layoutFemale = (LinearLayout) mRootView.findViewById(R.id.layout_profile_female);
        layoutMale = (LinearLayout) mRootView.findViewById(R.id.layout_profile_male);
        imgFemale = (ImageView) mRootView.findViewById(R.id.img_profile_female);
        imgMale = (ImageView) mRootView.findViewById(R.id.img_profile_male);
        txtEmail = (TextView) mRootView.findViewById(R.id.txtProfileEmailText);
        layoutEmail = (LinearLayout)mRootView.findViewById(R.id.layoutProfileEmailbar);

        if (Globals.e_userType == Enums.USERTYPE.PATIENT)
        {
            editEmail.setVisibility(View.VISIBLE);
            txtEmail.setVisibility(View.VISIBLE);
            layoutEmail.setVisibility(View.VISIBLE);
        }
        else
        {
            editEmail.setVisibility(View.GONE);
            txtEmail.setVisibility(View.GONE);
            layoutEmail.setVisibility(View.GONE);
        }

        layoutFemale.setOnClickListener(this);
        layoutMale.setOnClickListener(this);
        layoutDone.setOnClickListener(this);
    }
    public void getProfile() //Get Profile From Webservice
    {
        ((MainActivity)getActivity()).showLoader();
        if (Globals.e_userType == Enums.USERTYPE.PATIENT)
            ServiceManager.serviceGetProfilePatient(this);
        else
            ServiceManager.serviceGetProfileMember(this);
    }

    public void actionSubmit()  //When click submit button
    {
        String strFirstName = editFirstName.getText().toString();
        String strLastName = editLastName.getText().toString();
        String strEmail = editEmail.getText().toString();
        String strPhone = editPhone.getText().toString();
        if (strFirstName.equals(""))
        {
            ((MainActivity)getActivity()).dlgError.setMessage("Please enter Firstname");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }
        if (strLastName.equals(""))
        {
            ((MainActivity)getActivity()).dlgError.setMessage("Please enter Lastname");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }
        if (Globals.e_userType == Enums.USERTYPE.PATIENT && strEmail.equals(""))
        {
            ((MainActivity)getActivity()).dlgError.setMessage("Please enter Email");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }
        if (strPhone.equals(""))
        {
            ((MainActivity)getActivity()).dlgError.setMessage("Please enter Phone");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }
        String strGender = "M";
        if (!isMale)
            strGender = "F";
        ((MainActivity)getActivity()).showLoader();
        if (Globals.e_userType == Enums.USERTYPE.USER)
            ServiceManager.serviceUpdateProfileMember(strFirstName,strLastName,strPhone,strGender,this);
        else {
            if (!strEmail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                ((MainActivity)getActivity()).dlgError.setMessage("Invalid Email Address");
                ((MainActivity)getActivity()).dlgError.show();
                return;
            }
            ServiceManager.serviceUpdateProfilePatient(strFirstName, strLastName, strEmail, strPhone, strGender, this);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.layout_profile_female:
                isMale = false;
                setGender();
                break;
            case R.id.layout_profile_male:
                isMale = true;
                setGender();
                break;
            case R.id.layoutProfileSubmit:
                actionSubmit();
                break;
        }
    }

    public void setData()
    {
        editFirstName.setText(Globals.mAccount.mFirstName);
        editLastName.setText(Globals.mAccount.mLastName);
        editEmail.setText(Globals.mAccount.mEmail);
        editPhone.setText(Globals.mAccount.mPhone);
        if (Globals.mAccount.mGender == null || Globals.mAccount.mGender.equals("M"))
            isMale = true;
        else isMale = false;
        setGender();
    }

    @Override
    public void onResponse(int code) {
        ((MainActivity)getActivity()).dismissLoader();
        switch (code)
        {
            case 200://Success Get Profile
                setData();
                break;
            case 201://Sucess Update Profile
                if (Globals.e_userType == Enums.USERTYPE.PATIENT) {
                    Globals.e_mode = Enums.MODE.PATIENT_HOME;
                    Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
                    ((MainActivity) getActivity()).setFragment();
                }
                else
                {
                    Globals.e_mode = Enums.MODE.MEMBER_HOME;
                    Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
                    ((MainActivity) getActivity()).setFragment();
                }
                break;
            case 400://Fail Call Webservice
                break;
        }
    }
}

