package com.songu.pom.fragment.patient;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.songu.pom.R;
import com.songu.pom.activity.MainActivity;
import com.songu.pom.adapter.AdapterHistory;
import com.songu.pom.adapter.AdapterMemberUpdate;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.fragment.member.HomeFragment;
import com.songu.pom.model.UserModel;
import com.songu.pom.service.IServiceResult;
import com.songu.pom.service.ServiceManager;

import java.util.ArrayList;
import java.util.List;

public class VisitFragment extends Fragment implements IServiceResult,SwipeRefreshLayout.OnRefreshListener {

    public View mRootView = null;
    public ListView listVisitHistory;
    //public AdapterHistory adapterHistory;
    public AdapterMemberUpdate adapterMemberUpdate;
    public SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_patient_history, null);
        }
        initView();
        getMessage();
        return mRootView;
    }

    public void initView()
    {
        swipeLayout = (SwipeRefreshLayout)mRootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        listVisitHistory = (ListView) mRootView.findViewById(R.id.listVisitHistory);
        //adapterHistory = new AdapterHistory(this.getActivity());
        adapterMemberUpdate = new AdapterMemberUpdate(this.getActivity());
        listVisitHistory.setAdapter(adapterMemberUpdate);
        //adapterMemberUpdate.update(Globals.g_inviteList);
        listVisitHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Globals.e_mode = Enums.MODE.MEMBER_DETAIL;
                Globals.gCurrentMsg = Globals.g_messageList.get(position);
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                ((MainActivity)VisitFragment.this.getActivity()).setFragment();
            }
        });
    }

    @Override
    public void onRefresh() {//Refresh When Scroll Up
       getMessage();
    }

    public void getMessage()
    {
        ServiceManager.serviceGetPatientUpdate("None",Globals.mAccount.mID,Globals.mAccount.mHealthID, this);
    }

    public void getInviteMembers()
    {
        ((MainActivity)getActivity()).showLoader();
        ServiceManager.serviceGetInviteMembers(this);
    }

    @Override
    public void onResponse(int code) {
        ((MainActivity)getActivity()).dismissLoader();
        swipeLayout.setRefreshing(false);
        switch (code)
        {
            case 200://Success Get Message
                adapterMemberUpdate.update(Globals.g_messageList);
                break;
            case 400://Fail Get message
                break;
        }
    }
}
