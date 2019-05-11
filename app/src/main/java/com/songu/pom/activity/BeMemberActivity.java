package com.songu.pom.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.songu.pom.R;
import com.songu.pom.adapter.AdapterMemberInvite;
import com.songu.pom.doc.Config;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.fragment.member.InviteFragment;
import com.songu.pom.service.IServiceResult;
import com.songu.pom.service.ServiceManager;
import com.songu.pom.util.loader.ACProgressConstant;
import com.songu.pom.util.loader.ACProgressFlower;

import static android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;

public class BeMemberActivity extends Activity implements View.OnClickListener,IServiceResult {

    public ListView listMembers;
    public AdapterMemberInvite adapterInvite;
    public ACProgressFlower dlgLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bemember);
        initView();
        initializeDlg();
        getInvitedMember(); //Call service to get invited members
    }
    public void initView()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        listMembers = (ListView) this.findViewById(R.id.listInviteMember);
        adapterInvite = new AdapterMemberInvite(this);
        listMembers.setAdapter(adapterInvite);
        listMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //When select single member
                if (Globals.g_inviteList.get(position).mActive.equals("0")) //Non-activated Invite Member
                {
                    showLoader();
                    SharedPreferences pref = BeMemberActivity.this.getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                    String token = pref.getString("regId","");
                    Globals.e_userType = Enums.USERTYPE.USER;
                    Globals.mAccount.mID = Globals.mAccount.mEmail;
                    ServiceManager.serviceAgreeTerm(Globals.g_inviteList.get(position).mCode,token,BeMemberActivity.this);
                }
                else
                {//Activated Invite member
                    showLoader();
                    SharedPreferences pref = BeMemberActivity.this.getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                    String token = pref.getString("regId","");
                    ServiceManager.serviceLogin("User",Globals.mAccount.mEmail,Globals.mAccount.mPassword,token,BeMemberActivity.this);
                }
            }
        });
    }

    public void initializeDlg()
    {
        dlgLoading = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading ...")
                .fadeColor(Color.DKGRAY).build();
        dlgLoading.setCanceledOnTouchOutside(false);

    }

    public void showLoader(){
        try {
            if (!dlgLoading.isShowing()) {
                dlgLoading.show();
            }
        }catch (Exception ex){

        }
    }

    public void dismissLoader(){
        try {
            if(dlgLoading.isShowing()) {
                dlgLoading.dismiss();
            }
        }catch (Exception ex){

        }
    }
    public void getInvitedMember()
    {
        showLoader();//Show Indicator
        Globals.g_inviteList.clear();
        ServiceManager.serviceGetInvitePatients(Globals.mAccount.mEmail,this); //Call Webservice for Invited members
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

        }
    }

    @Override
    public void onResponse(int code) {
        dismissLoader();
        switch (code)
        {
            case 202:
            case 200://When success select single member
                for (int i = 0;i < Globals.g_inviteList.size();i++)
                {
                    Globals.g_inviteList.get(i).isPatientSelect = true;
                }
                Globals.e_mode = Enums.MODE.MEMBER_HOME;
                Intent m = new Intent(this,MainActivity.class);
                m.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(m);
                break;
            case 201://When success get invite members
                adapterInvite.update(Globals.g_inviteList);
                break;
            case 400:
                break;
        }
    }
}
