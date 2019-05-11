package com.songu.pom.fragment.patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.songu.pom.R;
import com.songu.pom.activity.MainActivity;
import com.songu.pom.adapter.AdapterInviteExp;
import com.songu.pom.adapter.AdapterService;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.model.ServiceModel;
import com.songu.pom.model.UserModel;
import com.songu.pom.service.IServiceResult;
import com.songu.pom.service.ServiceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SelectServiceFragment extends Fragment implements View.OnClickListener,IServiceResult{

    public View mRootView = null;
    public LinearLayout layoutNext;

    public List<ServiceViewHolder> lstServices;
    public ExpandableListView expServices;
    public AdapterInviteExp adapterInvite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_patient_selectservice, null);
        }
        initView();
        return mRootView;
    }
    public void initSelectAll()
    {
        for (int k = 0;k < Globals.g_inviteList1.size();k++) {
            Globals.g_inviteList1.get(k).isSelectAll = true;
            for (int i = 1; i < Globals.gServices.length; i++) {
                if (!Globals.g_inviteList1.get(k).mServices.get(Globals.gServices[i]).isSelect)
                    Globals.g_inviteList1.get(k).isSelectAll = false;
            }
        }
    }
    public void initView()
    {
        lstServices = new ArrayList<>();
        expServices = mRootView.findViewById(R.id.lvExpService);
        initSelectAll();
        adapterInvite = new AdapterInviteExp(this.getContext(),Globals.g_inviteList1);
        layoutNext = (LinearLayout) mRootView.findViewById(R.id.layout_select_service_next);
        layoutNext.setOnClickListener(this);
        expServices.setAdapter(adapterInvite);
        expServices.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                adapterInvite.getGroup(i).isExpand = !adapterInvite.getGroup(i).isExpand;
                adapterInvite.notifyDataSetChanged();
                return false;
            }
        });

        expServices.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                if (childPosition == 0) // Click Select All
                {
                    Globals.g_inviteList1.get(groupPosition).isSelectAll = !Globals.g_inviteList1.get(groupPosition).isSelectAll;
                    for (int i = 1;i < Globals.gServices.length;i++)
                    {
                        ServiceModel service = Globals.g_inviteList1.get(groupPosition).mServices.get(Globals.gServices[i]);
                        service.isSelect = Globals.g_inviteList1.get(groupPosition).isSelectAll;
                        Globals.g_inviteList1.get(groupPosition).mServices.put(Globals.gServices[i],service);
                    }
                }
                else
                {
                    ServiceModel service  = Globals.g_inviteList.get(groupPosition).mServices.get(Globals.gServices[childPosition]);
                    service.isSelect = !service.isSelect;
                    Globals.g_inviteList1.get(groupPosition).mServices.put(Globals.gServices[childPosition],service);
                    Globals.g_inviteList1.get(groupPosition).isSelectAll = true;
                    for (int i = 1;i < Globals.gServices.length;i++)
                    {
                        if (!Globals.g_inviteList1.get(groupPosition).mServices.get(Globals.gServices[i]).isSelect)
                            Globals.g_inviteList1.get(groupPosition).isSelectAll = false;
                    }
                }
                //Check Select All Item
                adapterInvite.notifyDataSetChanged();
                return false;
            }
        });
        if (adapterInvite.getGroupCount() > 0) {
            adapterInvite.getGroup(0).isExpand = true;
            adapterInvite.notifyDataSetChanged();
            expServices.expandGroup(0);
        }
    }

    public void actionDone()
    {
        if (Globals.g_inviteList1.size() == 0)
        {
            onResponse(200);
            return;
        }
        String inviteJson = "";
        JSONArray inviteArray = new JSONArray();
        for (int i = 0; i < Globals.g_inviteList1.size();i++)
        {
            inviteArray.put(Globals.g_inviteList1.get(i).toJsonMember());

        }
        inviteJson = inviteArray.toString();
        ((MainActivity)getActivity()).showLoader();
        ServiceManager.serviceInviteMembers(inviteArray,this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.layout_select_service_next:
                actionDone();
                break;
        }
    }


    @Override
    public void onResponse(int code) {
        ((MainActivity)getActivity()).dismissLoader();
        switch (code)
        {
            case 200:
                Globals.e_mode = Enums.MODE.PATIENT_HOME;
                Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
                ((MainActivity)getActivity()).setFragment();
                break;
            case 400:
                ((MainActivity)getActivity()).dlgError.setMessage("Invite unsuccessful");
                ((MainActivity)getActivity()).dlgError.show();
                break;

        }
    }

    class ServiceViewHolder
    {
        public LinearLayout layoutSelectAll;
        public LinearLayout layoutServiceParent;
        public ImageView imgSelectAll;
        public TextView txtServiceMember;
        public List<ServiceItemViewHolder> serviceItems;
        public boolean isSelectAll = true;
    }

    class ServiceItemViewHolder
    {
        public LinearLayout layoutService;
        public ImageView imgService;
        public TextView txtServiceName;
        public boolean isChecked = true;
    }
}
