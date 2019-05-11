package com.songu.pom.fragment.member;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.songu.pom.R;
import com.songu.pom.activity.MainActivity;
import com.songu.pom.adapter.AdapterMemberUpdate;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.model.PatientModel;
import com.songu.pom.model.UserModel;
import com.songu.pom.service.IServiceResult;
import com.songu.pom.service.ServiceManager;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener,IServiceResult,SwipeRefreshLayout.OnRefreshListener {

    public View mRootView = null;
    public ListView listMemberUpdate;
    public AdapterMemberUpdate adapterMemberUpdate;
    public SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_member_home, null);
        }
        initView();
        if (Globals.isFirstPatientLoad) {
            getInvitePatients();
        }
        getMessage();
        return mRootView;
    }

    public void initView()
    {
        swipeLayout = (SwipeRefreshLayout)mRootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        listMemberUpdate = (ListView) mRootView.findViewById(R.id.listUpdateMember);
        adapterMemberUpdate = new AdapterMemberUpdate(this.getActivity());
        listMemberUpdate.setAdapter(adapterMemberUpdate);
        listMemberUpdate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Globals.e_prevMode = Enums.MODE.MEMBER_HOME;
                Globals.e_mode = Enums.MODE.MEMBER_DETAIL;
                Globals.gCurrentMsg = Globals.g_messageList.get(position);
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                ((MainActivity)HomeFragment.this.getActivity()).setFragment();
            }
        });
    }

    public void getInvitePatients()
    {
        Globals.g_inviteList.clear();
        ((MainActivity)getActivity()).showLoader();
        ServiceManager.serviceGetInvitePatients1(Globals.mAccount.mID,this);
    }
    public void getMessage()
    {

        ServiceManager.serviceGetUpdate(Globals.mAccount.mID, this);
//        if (Globals.gSelectPatient != null) {
//
            //ServiceManager.serviceGetUpdate("PATID1234","98238947", this);
//        }
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResponse(int code) {
        if (((MainActivity)getActivity()) != null)
            ((MainActivity)getActivity()).dismissLoader();
        swipeLayout.setRefreshing(false);
        switch (code)
        {
            case 200:
                adapterMemberUpdate.update(Globals.g_messageList);
                break;
            case 201:
                Globals.isFirstPatientLoad = false;
                if (Globals.g_inviteList.size() > 0)
                {
                    for (int i = 0;i < Globals.g_inviteList.size();i++)
                    {
                        Globals.g_inviteList.get(i).isPatientSelect = true;
                    }
                    getMessage();
//                    if (Globals.gSelectPatient == null) {
//                        Globals.gSelectPatient = (PatientModel) Globals.g_inviteList.get(0);
//                        getMessage();
//                    }
                }
                break;
            case 400:
                break;
        }
    }

    @Override
    public void onRefresh() {
        getMessage();
    }
}
