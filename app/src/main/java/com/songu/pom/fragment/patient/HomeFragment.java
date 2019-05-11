package com.songu.pom.fragment.patient;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.songu.pom.R;
import com.songu.pom.activity.MainActivity;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.model.UserModel;
import com.songu.pom.service.IServiceResult;
import com.songu.pom.service.ServiceManager;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener,IServiceResult{

    public View mRootView = null;
    public Button btnInvite,btnSecurity,btnHistory,btnBlast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_patient_home, null);
        }
        initView();
        getInviteMembers();

        return mRootView;
    }

    public void initView()
    {
        btnInvite = (Button) mRootView.findViewById(R.id.btnInviteMember);
        btnSecurity = (Button) mRootView.findViewById(R.id.btnSecuritySetting);
        btnHistory = (Button) mRootView.findViewById(R.id.btnVisitHistory);
        btnBlast = (Button) mRootView.findViewById(R.id.btnBlastMessage);

        btnInvite.setOnClickListener(this);
        btnSecurity.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        btnBlast.setOnClickListener(this);
    }
    public void getInviteMembers()
    {
        Globals.g_inviteList.clear();
        ((MainActivity)getActivity()).showLoader();
        ServiceManager.serviceGetInviteMembers(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnInviteMember:
                Globals.g_inviteList1.clear();
                Globals.e_mode = Enums.MODE.PATIENT_INVITE;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                ((MainActivity) getActivity()).setFragment();
//                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
//                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
//                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
                break;
            case R.id.btnSecuritySetting:
                Globals.g_inviteList1 = Globals.g_inviteList;
                Globals.e_mode = Enums.MODE.PATIENT_CHANGE_SERVICE;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                ((MainActivity) getActivity()).setFragment();
                break;
            case R.id.btnBlastMessage:
                Globals.e_mode = Enums.MODE.PATIENT_BLAST;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                ((MainActivity)getActivity()).setFragment();
                break;
            case R.id.btnVisitHistory:
                Globals.e_mode = Enums.MODE.PATIENT_VISIT;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                ((MainActivity)getActivity()).setFragment();
                break;
        }
    }

    @Override
    public void onResponse(int code) {
        ((MainActivity)getActivity()).dismissLoader();
        switch (code)
        {
            case 200:
                ((MainActivity)getActivity()).setBlastMembers();
                break;
            case 400:
                break;
        }
    }
}
