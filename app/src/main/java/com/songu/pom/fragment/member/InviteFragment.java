package com.songu.pom.fragment.member;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.songu.pom.R;
import com.songu.pom.activity.MainActivity;
import com.songu.pom.adapter.AdapterMemberInvite;
import com.songu.pom.adapter.AdapterMemberUpdate;
import com.songu.pom.doc.Config;
import com.songu.pom.doc.Globals;
import com.songu.pom.model.UserModel;
import com.songu.pom.service.IServiceResult;
import com.songu.pom.service.ServiceManager;

import java.util.ArrayList;
import java.util.List;

public class InviteFragment extends Fragment implements IServiceResult {

    public View mRootView = null;
    public ListView listInvite;
    public AdapterMemberInvite adapterInvite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_member_invite, null);
        }
        initView();
        if (Globals.g_inviteList.size() == 0)
            getInvitePatients();
        else
            adapterInvite.update(Globals.g_inviteList);
        return mRootView;
    }

    public void initView()
    {
        adapterInvite = new AdapterMemberInvite(this.getActivity());
        listInvite = (ListView) mRootView.findViewById(R.id.listInvitePatient);
        listInvite.setAdapter(adapterInvite);
        listInvite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Globals.g_inviteList.get(position).mActive.equals("0"))
                {
                    String selectCode = Globals.g_inviteList.get(position).mCode;
                    SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                    String token = pref.getString("regId","");
                    ServiceManager.serviceAgreeTerm(selectCode,token,InviteFragment.this);
                }
            }
        });
    }

    public void getInvitePatients()
    {
        Globals.g_inviteList.clear();
        ((MainActivity)getActivity()).showLoader();
        ServiceManager.serviceGetInvitePatients(Globals.mAccount.mID,this);//Invite Patients list
    }

    @Override
    public void onResponse(int code) {
        ((MainActivity)getActivity()).dismissLoader();
        switch (code)
        {
            case 200:
                getInvitePatients();
                break;
            case 201:
                adapterInvite.update(Globals.g_inviteList);
                break;
            case 400:
                break;
        }
    }
}
