package com.songu.pom.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.MultiSpinner;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;

import com.songu.pom.R;
import com.songu.pom.adapter.AdapterBlast;
import com.songu.pom.adapter.AdapterPatient;
import com.songu.pom.doc.Config;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.fragment.ConsentFragment;
import com.songu.pom.fragment.ForgetPasswordFragment;
import com.songu.pom.fragment.MenuLoginFragment;
import com.songu.pom.fragment.member.DetailFragment;
import com.songu.pom.fragment.member.InviteFragment;
import com.songu.pom.fragment.member.PatientUpdateFragment;
import com.songu.pom.fragment.patient.ChangePasswordFragment;
import com.songu.pom.fragment.patient.HomeFragment;
import com.songu.pom.fragment.patient.ProfileFragment;
import com.songu.pom.fragment.patient.SelectServiceFragment;
import com.songu.pom.fragment.patient.VisitFragment;
import com.songu.pom.listener.SimpleGestureFilter;
import com.songu.pom.model.PatientModel;
import com.songu.pom.model.UserModel;
import com.songu.pom.service.IServiceResult;
import com.songu.pom.service.ServiceManager;
import com.songu.pom.util.loader.ACProgressConstant;
import com.songu.pom.util.loader.ACProgressFlower;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;

public class MainActivity extends FragmentActivity implements View.OnClickListener,IServiceResult,SimpleGestureFilter.SimpleGestureListener {


    private SimpleGestureFilter detector;

    public TextView txtTitle;
    public Fragment currentFragment;
    public LinearLayout layoutBottombar;
    public ACProgressFlower dlgLoading;
    public AlertDialog dlgError;

    public RelativeLayout relPatientHome,relPatientBlast,relPatientVisit,relPatientOption;
    public LinearLayout linPatientHome,linPatientBlast,linPatientVisit,linPatientOption;

    public RelativeLayout relMemberHome,relMemberPatient,relMemberInvite,relMemberSetting;
    public LinearLayout linMemberHome,linMemberPatient,linMemberInvite,linMemberSetting;

    public LinearLayout layoutPatientTabLine,layoutPatientTab;
    public LinearLayout layoutMemberTabLine,layoutMemberTab;
    public RelativeLayout relOption,relBlast,relMemberOption,relMemberPatientMenu;
    public LinearLayout layoutMenu,layoutMask,layoutMemberMenu,layoutMemberMask,layoutMemberPatientMenu,layoutMemberPatientMask;
    public LinearLayout layoutBlast,layoutBlastMask;
    public TextView btnOptionPatient,btnOptionProfile,btnOptionPassword,btnOptionLogout;
    public TextView btnMemberOptionPatient,btnMemberOptionProfile,btnMemberOptionPassword,btnMemberOptionLogout;
    public ListView listMemberPatient;
    public MultiSpinner spBlastContact;
    public EditText editMessage;
    public ImageView imgBlastSend;
    public AdapterBlast adapterBlast;
    public AdapterPatient adapterPatient;
    public TextView txtUserName;
    public List<String> listBlastContacts = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grantPermission();  //Permission Check
        initView();
        initializeDlg();
        initActionBar();
        setFragment(); //Change Screen
    }

    public static boolean hasPermissions(Context context, String... permissions) {//Check Permission
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void grantPermission()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!hasPermissions(this, Config.PERMISSIONS))
            {
                ActivityCompat.requestPermissions(this, Config.PERMISSIONS, 1);
            }
        }
    }


    public void initializeDlg()
    {
        dlgLoading = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading ...")
                .fadeColor(Color.DKGRAY).build();
        dlgLoading.setCanceledOnTouchOutside(false);
        dlgError = new AlertDialog.Builder(this,THEME_DEVICE_DEFAULT_LIGHT).setTitle("Error").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create();

        dlgError.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dlgError.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(0xff006699);
                dlgError.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xff006699);
            }
        });


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
    public void setBlastMembers()
    {
        final LinkedHashMap<String,Boolean> listArray = new LinkedHashMap<String,Boolean>();

        listArray.put("Select All",false);

        for(int i=0; i<Globals.g_inviteList.size(); i++) {
            //listArray.put(Globals.g_inviteList.get(i).mEmail,false);
            String strName = Globals.g_inviteList.get(i).mFirstName + " " + Globals.g_inviteList.get(i).mLastName;
            listArray.put(strName,false);
        }

        spBlastContact.setItems(listArray,  new MultiSpinnerListener() {

            @Override
            public void onItemsSelected(boolean[] selected) {
                if (selected.length > 0) {
                    for (int i = 1; i < selected.length; i++) {
                        if (selected[i]) {
                            listBlastContacts.add(Globals.g_inviteList.get(i - 1).mID);
                        }
                    }
                }
            }
        });
    }
    public void initView()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        detector = new SimpleGestureFilter(MainActivity.this, this);
        adapterBlast = new AdapterBlast(this);
        adapterPatient = new AdapterPatient(this);
        relOption = (RelativeLayout) this.findViewById(R.id.relOptionMenu);
        relMemberOption = (RelativeLayout) this.findViewById(R.id.relOptionMemberMenu);
        relMemberPatientMenu = (RelativeLayout) this.findViewById(R.id.relOptionMemberPatient);
        relBlast = (RelativeLayout) this.findViewById(R.id.relBlastMenu);
        layoutMenu = (LinearLayout) this.findViewById(R.id.layoutOptionMenu);
        layoutMemberMenu = (LinearLayout) this.findViewById(R.id.layoutMemberOptionMenu);
        layoutMemberMask = (LinearLayout) this.findViewById(R.id.layoutMemberOptionMask);
        layoutMemberPatientMask = (LinearLayout) this.findViewById(R.id.layoutMemberPatientMask);
        layoutMemberPatientMenu = (LinearLayout) this.findViewById(R.id.layoutMemberPatientMenu);
        layoutMask = (LinearLayout) this.findViewById(R.id.layoutOptionMask);
        layoutBlast = (LinearLayout) this.findViewById(R.id.layoutBlastParent);
        layoutBlastMask = (LinearLayout) this.findViewById(R.id.layoutBlastMask);
        spBlastContact = (MultiSpinner) this.findViewById(R.id.spBlastContact);
        listMemberPatient = (ListView) this.findViewById(R.id.listMemberPatients);
        listMemberPatient.setAdapter(adapterPatient);
        listMemberPatient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Globals.gSelectPatient = (PatientModel) Globals.g_inviteList.get(position);
                if (currentFragment instanceof com.songu.pom.fragment.member.PatientUpdateFragment)
                {
                    ((com.songu.pom.fragment.member.PatientUpdateFragment)currentFragment).setTitle();
                    ((com.songu.pom.fragment.member.PatientUpdateFragment)currentFragment).getMessage();
                }
                else {
                    Globals.e_mode = Enums.MODE.MEMBER_PATIENT_UPDATE;
                    setFragment();
                }
                hidePatientList();
            }
        });

        spBlastContact.setAdapter(adapterBlast);
        spBlastContact.setDefaultText("Select Members");
        editMessage = (EditText) this.findViewById(R.id.editBlastMessage);
        imgBlastSend = (ImageView) this.findViewById(R.id.imgBlastSend);
        txtUserName = (TextView) this.findViewById(R.id.txtUserName);

        txtTitle = (TextView) this.findViewById(R.id.txtScreenTitle);
        layoutBottombar = (LinearLayout) this.findViewById(R.id.layoutMainBottomBar);
        layoutMemberTab = (LinearLayout) this.findViewById(R.id.layoutMemberBottomBar);
        layoutMemberTabLine = (LinearLayout) this.findViewById(R.id.layoutMemberBottomBarLine);
        layoutPatientTab = (LinearLayout) this.findViewById(R.id.layoutPatientBottomBar);
        layoutPatientTabLine = (LinearLayout) this.findViewById(R.id.layoutPatientBottomBarLine);

        relMemberHome = (RelativeLayout) this.findViewById(R.id.relMemberBottomHome);
        relMemberPatient = (RelativeLayout) this.findViewById(R.id.relMemberBottomBlast);
        relMemberInvite = (RelativeLayout) this.findViewById(R.id.relMemberBottomHistory);
        relMemberSetting = (RelativeLayout) this.findViewById(R.id.relMemberBottomOption);

        linMemberHome = (LinearLayout) this.findViewById(R.id.linMemberBottomHome);
        linMemberPatient = (LinearLayout) this.findViewById(R.id.linMemberBottomBlast);
        linMemberInvite = (LinearLayout) this.findViewById(R.id.linMemberBottomHistory);
        linMemberSetting = (LinearLayout) this.findViewById(R.id.linMemberBottomOption);


        relPatientHome = (RelativeLayout) this.findViewById(R.id.relPatientBottomHome);
        relPatientBlast = (RelativeLayout) this.findViewById(R.id.relPatientBottomBlast);
        relPatientVisit = (RelativeLayout) this.findViewById(R.id.relPatientBottomHistory);
        relPatientOption = (RelativeLayout) this.findViewById(R.id.relPatientBottomOption);

        linPatientHome = (LinearLayout) this.findViewById(R.id.linPatientBottomHome);
        linPatientBlast = (LinearLayout) this.findViewById(R.id.linPatientBottomBlast);
        linPatientVisit = (LinearLayout) this.findViewById(R.id.linPatientBottomHistory);
        linPatientOption = (LinearLayout) this.findViewById(R.id.linPatientBottomOption);

        btnOptionPatient = (TextView) this.findViewById(R.id.btnOptionPatient);
        btnOptionProfile = (TextView) this.findViewById(R.id.btnOptionProfile);
        btnOptionPassword = (TextView) this.findViewById(R.id.btnOptionChangePassword);
        btnOptionLogout = (TextView) this.findViewById(R.id.btnOptionLogout);

        btnMemberOptionPatient = (TextView) this.findViewById(R.id.btnMemberOptionPatient);
        btnMemberOptionProfile = (TextView) this.findViewById(R.id.btnMemberOptionProfile);
        btnMemberOptionPassword = (TextView) this.findViewById(R.id.btnMemberOptionChangePassword);
        btnMemberOptionLogout = (TextView) this.findViewById(R.id.btnMemberOptionLogout);


        btnOptionLogout.setOnClickListener(this);
        btnOptionProfile.setOnClickListener(this);
        btnOptionPassword.setOnClickListener(this);
        btnOptionPatient.setOnClickListener(this);

        btnMemberOptionPatient.setOnClickListener(this);
        btnMemberOptionProfile.setOnClickListener(this);
        btnMemberOptionPassword.setOnClickListener(this);
        btnMemberOptionLogout.setOnClickListener(this);

        relMemberHome.setOnClickListener(this);
        relMemberPatient.setOnClickListener(this);
        relMemberInvite.setOnClickListener(this);
        relMemberSetting.setOnClickListener(this);
        relPatientHome.setOnClickListener(this);
        relPatientBlast.setOnClickListener(this);
        relPatientVisit.setOnClickListener(this);
        relPatientOption.setOnClickListener(this);
        layoutMask.setOnClickListener(this);
        layoutMemberMask.setOnClickListener(this);
        layoutBlastMask.setOnClickListener(this);
        layoutMemberPatientMask.setOnClickListener(this);

        imgBlastSend.setOnClickListener(this);
        txtUserName.setVisibility(View.GONE);
        ServiceManager.init(this); //Init Webservice

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }



    public void showBlast()
    {
        listBlastContacts.clear();
        relBlast.setVisibility(View.VISIBLE);
    }
    public void hideBlast()
    {
        relBlast.setVisibility(View.GONE);
    }

    public void showPatientList()
    {
        relMemberPatientMenu.setVisibility(View.VISIBLE);
    }

    public void hidePatientList()
    {
        relMemberPatientMenu.setVisibility(View.GONE);
    }

    public void showMemberOption()
    {
        relMemberOption.setVisibility(View.VISIBLE);
    }

    public void hideMemberOption()
    {
        relMemberOption.setVisibility(View.GONE);
    }
    public void showOption()
    {
        relOption.setVisibility(View.VISIBLE);
    }


    public void hideOption()
    {
        relOption.setVisibility(View.GONE);
    }
    public void hideTab()
    {
        layoutBottombar.setVisibility(View.VISIBLE);
        layoutPatientTabLine.setVisibility(View.GONE);
        layoutPatientTab.setVisibility(View.GONE);
        layoutMemberTabLine.setVisibility(View.GONE);
        layoutMemberTab.setVisibility(View.GONE);
    }

    //Bottom Tab Setting with Line
    public void setTabMode()
    {
        layoutPatientTabLine.setVisibility(View.GONE);
        layoutPatientTab.setVisibility(View.GONE);
        layoutMemberTabLine.setVisibility(View.GONE);
        layoutMemberTab.setVisibility(View.GONE);
        if (Globals.e_userType == Enums.USERTYPE.PATIENT)
        {
            layoutBottombar.setVisibility(View.GONE);
            layoutPatientTabLine.setVisibility(View.VISIBLE);
            layoutPatientTab.setVisibility(View.VISIBLE);
        }
        else if (Globals.e_userType == Enums.USERTYPE.USER)
        {
            layoutBottombar.setVisibility(View.GONE);
            layoutMemberTabLine.setVisibility(View.VISIBLE);
            layoutMemberTab.setVisibility(View.VISIBLE);
        }
    }
    //Bottom Tab Setting for Patient
    public void setPatientTab()
    {
        linPatientHome.setVisibility(View.GONE);
        linPatientBlast.setVisibility(View.GONE);
        linPatientVisit.setVisibility(View.GONE);
        linPatientOption.setVisibility(View.GONE);

        if (Globals.e_mode == Enums.MODE.PATIENT_HOME)
        {
            linPatientHome.setVisibility(View.VISIBLE);
        }

        if (Globals.e_mode == Enums.MODE.PATIENT_BLAST)
        {
            linPatientBlast.setVisibility(View.VISIBLE);
        }

        if (Globals.e_mode == Enums.MODE.PATIENT_VISIT)
        {
            linPatientVisit.setVisibility(View.VISIBLE);
        }

        if (Globals.e_mode == Enums.MODE.PATIENT_OPTION)
        {
            linPatientOption.setVisibility(View.VISIBLE);
        }
    }

    //Bottom Tab Setting for Member
    public void setMemberTab()
    {
        linMemberHome.setVisibility(View.GONE);
        linMemberPatient.setVisibility(View.GONE);
        linMemberInvite.setVisibility(View.GONE);
        linMemberSetting.setVisibility(View.GONE);

        if (Globals.e_mode == Enums.MODE.MEMBER_HOME)
        {
            linMemberHome.setVisibility(View.VISIBLE);
        }

        if (Globals.e_mode == Enums.MODE.MEMBER_PATIENT || Globals.e_mode == Enums.MODE.MEMBER_PATIENT_UPDATE)
        {
            linMemberPatient.setVisibility(View.VISIBLE);
        }

        if (Globals.e_mode == Enums.MODE.MEMBER_SETTING)
        {
            linMemberSetting.setVisibility(View.VISIBLE);
        }

        if (Globals.e_mode == Enums.MODE.MEMBER_INVITE)
        {
            linMemberInvite.setVisibility(View.VISIBLE);
        }
    }
    private void initActionBar() {
        try {
            ActionBar ab = getActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
            ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.topbar)));
            //ab.setCustomView(R.layout.layout_actionbar);
            ab.setDisplayShowCustomEnabled(true);
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this res items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

        }
        return super.onOptionsItemSelected(item);
    }
    public void showUserName()
    {
        txtUserName.setText(Globals.mAccount.mName);
        txtUserName.setVisibility(View.VISIBLE);
    }
    public void actionTransition()
    {
        if (Globals.e_transDirection == Enums.SWIPDIRECTION.LEFT)
        {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_toleft).replace(R.id.frameHome, currentFragment).commit();
        }
        else
        {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_enter_from_left, R.anim.anim_exit_toright).replace(R.id.frameHome, currentFragment).commit();
        }
    }
    public void setFragment() {//Set Screen for State
        switch (Globals.e_mode) {
            case MENULOGIN:
                if (!(currentFragment instanceof MenuLoginFragment)) {
                    hideTab();
                    txtTitle.setText("Welcome to PoM");
                    currentFragment = new MenuLoginFragment();
                    actionTransition();
                }
                break;
            case CONSENT:
                if (!(currentFragment instanceof ConsentFragment)) {
                    hideTab();
                    txtTitle.setText("Welcome to PoM");
                    currentFragment = new ConsentFragment();
                    actionTransition();
                }
                break;
            case FORGETPASSWORD:
                if (!(currentFragment instanceof ForgetPasswordFragment)) {
                    setTabMode();
                    txtTitle.setText("Welcome to PoM");
                    currentFragment = new ForgetPasswordFragment();
                    actionTransition();
                }
                break;
            case RESETPASSWORD:
                if (!(currentFragment instanceof ChangePasswordFragment)) {
                    setTabMode();
                    txtTitle.setText("Welcome to PoM");
                    currentFragment = new ChangePasswordFragment();
                    actionTransition();
                }
                break;
            case PATIENT_HOME:
                if (!(currentFragment instanceof com.songu.pom.fragment.patient.HomeFragment)) {
                    setPatientTab();
                    setTabMode();
                    txtTitle.setText("");
                    currentFragment = new com.songu.pom.fragment.patient.HomeFragment();
                    actionTransition();
                }
                break;
            case PATIENT_INVITE:
                if (!(currentFragment instanceof com.songu.pom.fragment.patient.InviteFragment)) {
                    setPatientTab();
                    setTabMode();
                    txtTitle.setText("");
                    currentFragment = new com.songu.pom.fragment.patient.InviteFragment();
                    actionTransition();
                }
                break;
            case PATIENT_CHANGE_SERVICE:
            case PATIENT_SELECT_SERVICE:
                if (!(currentFragment instanceof SelectServiceFragment)) {
                    setPatientTab();
                    setTabMode();
                    txtTitle.setText("");
                    currentFragment = new SelectServiceFragment();
                    actionTransition();
                }
                break;
            case PATIENT_BLAST:
                //setPatientTab();
                setTabMode();
                txtTitle.setText("");
                showBlast();
                break;
            case PATIENT_VISIT:
                if (!(currentFragment instanceof VisitFragment)) {
                    setPatientTab();
                    setTabMode();
                    txtTitle.setText("");
                    currentFragment = new VisitFragment();
                    actionTransition();
                }
                break;
            case PATIENT_OPTION:
                //setPatientTab();
                setTabMode();
                txtTitle.setText("");
                showOption();
                break;
            case PATIENT_CHANGE_PASSWORD:
                if (!(currentFragment instanceof ChangePasswordFragment)) {
                    setPatientTab();
                    setTabMode();
                    txtTitle.setText("");
                    currentFragment = new ChangePasswordFragment();
                    actionTransition();
                }
                break;
            case PATIENT_PROFILE:
                if (!(currentFragment instanceof ProfileFragment)) {
                    setPatientTab();
                    setTabMode();
                    txtTitle.setText("");
                    currentFragment = new ProfileFragment();
                    actionTransition();
                }
                break;
            case MEMBER_HOME:
                if (!(currentFragment instanceof com.songu.pom.fragment.member.HomeFragment)) {
                    showUserName();
                    setMemberTab();
                    setTabMode();
                    txtTitle.setText("");
                    currentFragment = new com.songu.pom.fragment.member.HomeFragment();
                    actionTransition();
                }
                break;
            case MEMBER_INVITE:
                if (!(currentFragment instanceof InviteFragment)) {
                    setMemberTab();
                    setTabMode();
                    txtTitle.setText("");
                    currentFragment = new InviteFragment();
                    actionTransition();
                }
                break;
            case MEMBER_SETTING:
                //setMemberTab();
                setTabMode();
                txtTitle.setText("");
                showMemberOption();
                break;
            case MEMBER_PATIENT:
                //setMemberTab();
                setTabMode();
                txtTitle.setText("");
                if (Globals.g_inviteList.size() > 1) {
                    adapterPatient.update(Globals.g_inviteList);
                    showPatientList();
                }
                break;
            case MEMBER_PROFILE:
                if (!(currentFragment instanceof ProfileFragment)) {
                    setPatientTab();
                    setTabMode();
                    txtTitle.setText("");
                    currentFragment = new ProfileFragment();
                    actionTransition();
                }
                break;
            case MEMBER_CHANGE_PASSWORD:
                if (!(currentFragment instanceof ChangePasswordFragment)) {
                    setPatientTab();
                    setTabMode();
                    txtTitle.setText("");
                    currentFragment = new ChangePasswordFragment();
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_toleft).replace(R.id.frameHome, currentFragment).commit();
                }
                break;
            case MEMBER_DETAIL:
                if (!(currentFragment instanceof DetailFragment)) {
                    setPatientTab();
                    setTabMode();
                    txtTitle.setText("");
                    currentFragment = new DetailFragment();
                    actionTransition();
                }
                break;
            case MEMBER_PATIENT_UPDATE:
                if (!(currentFragment instanceof PatientUpdateFragment)) {
                    setMemberTab();
                    setTabMode();
                    txtTitle.setText("");
                    currentFragment = new PatientUpdateFragment();
                    actionTransition();
                }
                break;

        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
    private void setBackFragment() {//When click Back button
        if (relOption.getVisibility() == View.VISIBLE)
        {
            hideOption();
            return;
        }
        if (relBlast.getVisibility() == View.VISIBLE)
        {
            hideBlast();
            return;
        }
        if (relMemberOption.getVisibility() == View.VISIBLE)
        {
            hideMemberOption();
            return;
        }
        if (relMemberPatientMenu.getVisibility() == View.VISIBLE)
        {
            hidePatientList();
            return;
        }
        switch (Globals.e_mode) {
            case MENULOGIN:
            case PATIENT_HOME:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog dlgFinish = builder.setMessage("Are you sure you want to leave?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).create();

                dlgFinish.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dlgFinish.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(0xff006699);
                        dlgFinish.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xff006699);
                    }
                });
                dlgFinish.show();
                return;
            case FORGETPASSWORD:
                Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
                Globals.e_mode = Enums.MODE.MENULOGIN;
                setFragment();
                break;
            case RESETPASSWORD:
                Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
                Globals.e_mode = Enums.MODE.FORGETPASSWORD;
                setFragment();
                break;
            case PATIENT_VISIT:
            case PATIENT_INVITE:
                Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
                Globals.e_mode = Enums.MODE.PATIENT_HOME;
                setFragment();
                break;
            case MEMBER_DETAIL:
                if (Globals.e_userType == Enums.USERTYPE.USER) {
                    Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
                    Globals.e_mode = Globals.e_prevMode;
                    //Globals.e_mode = Enums.MODE.MEMBER_HOME;
                }
                else
                {
                    Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
                    Globals.e_mode = Enums.MODE.PATIENT_VISIT;
                }
                setFragment();
                break;
            case MEMBER_INVITE:
                Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
                Globals.e_mode = Enums.MODE.MEMBER_HOME;
                setFragment();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (currentFragment instanceof com.songu.pom.fragment.patient.InviteFragment)
//        {
//            ((com.songu.pom.fragment.patient.InviteFragment)currentFragment).onActivityResult(requestCode,resultCode,data);
//        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.relMemberBottomHome://When click Member Home Tab
                Globals.e_mode = Enums.MODE.MEMBER_HOME;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                break;
            case R.id.relMemberBottomBlast://When click Member Blast Tab
                Globals.e_mode = Enums.MODE.MEMBER_PATIENT;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                break;
            case R.id.relMemberBottomHistory:
                Globals.e_mode = Enums.MODE.MEMBER_INVITE;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                break;
            case R.id.relMemberBottomOption:
                Globals.e_mode = Enums.MODE.MEMBER_SETTING;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                break;
            case R.id.relPatientBottomHome:
                Globals.e_mode = Enums.MODE.PATIENT_HOME;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                break;
            case R.id.relPatientBottomBlast:
                Globals.e_mode = Enums.MODE.PATIENT_BLAST;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                break;
            case R.id.relPatientBottomHistory:
                Globals.e_mode = Enums.MODE.PATIENT_VISIT;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                break;
            case R.id.relPatientBottomOption:
                Globals.e_mode = Enums.MODE.PATIENT_OPTION;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                break;
            case R.id.layoutMemberOptionMask:
                hideMemberOption();
                break;
            case R.id.layoutOptionMask:
                hideOption();
                break;
            case R.id.layoutBlastMask:
                hideBlast();
                break;
            case R.id.layoutMemberPatientMask:
                hidePatientList();
                break;
            case R.id.btnOptionPatient:
                hideOption();
                Globals.e_mode = Enums.MODE.MENULOGIN;
                actionBeMember();
                break;
            case R.id.btnOptionChangePassword:
                Globals.e_mode = Enums.MODE.PATIENT_CHANGE_PASSWORD;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                hideOption();
                break;
            case R.id.btnOptionLogout:
                hideOption();
                Globals.isFirstPatientLoad = true;
                Globals.e_mode = Enums.MODE.MENULOGIN;
                Globals.e_userType = Enums.USERTYPE.NONE;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                break;
            case R.id.btnOptionProfile:
                Globals.e_mode = Enums.MODE.PATIENT_PROFILE;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                hideOption();
                break;
            case R.id.btnMemberOptionPatient:
                hideMemberOption();
                actionBePatient();
                break;
            case R.id.btnMemberOptionChangePassword:
                Globals.e_mode = Enums.MODE.MEMBER_CHANGE_PASSWORD;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                hideMemberOption();
                break;
            case R.id.btnMemberOptionProfile:
                Globals.e_mode = Enums.MODE.MEMBER_PROFILE;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                hideMemberOption();
                break;
            case R.id.btnMemberOptionLogout:
                hideMemberOption();
                Globals.e_mode = Enums.MODE.MENULOGIN;
                Globals.e_userType = Enums.USERTYPE.NONE;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                break;
            case R.id.imgBlastSend:
                actionSendBlastMembers();
                break;

        }
    }
    public void actionBeMember()
    {
        Intent m = new Intent(this,BeMemberActivity.class);
        m.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(m);
    }
    public void actionBePatient()//Get Patient Info
    {
        showLoader();
        ServiceManager.serviceGetPatientInfo(Globals.mAccount.mID,this);
    }
    public void actionSendBlastMembers() //Send Blast Message
    {
        String strMessage = editMessage.getText().toString();
        if (strMessage.equals(""))
        {
            dlgError.setMessage("Please fill message");
            dlgError.show();
            return;
        }
        JSONArray contactArray = new JSONArray();
        for (int i = 0; i < listBlastContacts.size();i++)
        {
            contactArray.put(listBlastContacts.get(i));
        }
        showLoader();
        ServiceManager.serviceSendBlastMessage(contactArray,strMessage,this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setBackFragment();
        }
        return true;
    }


    @Override
    public void onResponse(int code) {
        dismissLoader();
        switch (code)
        {
            case 200:
                hideBlast();
                editMessage.setText("");
                spBlastContact.clearSelection();
                spBlastContact.onCancel(null);
                listBlastContacts.clear();
                break;
            case 201:
                //Active
                Globals.e_userType = Enums.USERTYPE.PATIENT;
                Globals.e_mode = Enums.MODE.PATIENT_HOME;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                setFragment();
                break;
            case 202:
                //Inactive
                Intent m = new Intent(this,BePatientActivity.class);
                m.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(m);
                break;
            case 400:
                break;
            case 401://No Exist Patient
                dlgError.setMessage("You do not have a patient record yet");
                dlgError.show();
                break;
        }
    }

    @Override
    public void onSwipe(int direction) {
        //Detect the swipe gestures and display toast
        String showToastMessage = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT:
                swipeRight();
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                swipeLeft();
                break;
            case SimpleGestureFilter.SWIPE_DOWN:
                showToastMessage = "You have Swiped Down.";
                break;
            case SimpleGestureFilter.SWIPE_UP:
                showToastMessage = "You have Swiped Up.";
                break;

        }
        //Toast.makeText(this, showToastMessage, Toast.LENGTH_SHORT).show();
    }

    public void swipeRight()
    {
        if (currentFragment instanceof VisitFragment && relBlast.getVisibility() == View.GONE && relOption.getVisibility() == View.GONE)
        {
            Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
            Globals.e_mode = Enums.MODE.PATIENT_HOME;
            setFragment();
        }

        if (currentFragment instanceof SelectServiceFragment && relBlast.getVisibility() == View.GONE && relOption.getVisibility() == View.GONE)
        {
            Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
            if (Globals.e_mode == Enums.MODE.PATIENT_CHANGE_SERVICE)
                Globals.e_mode = Enums.MODE.PATIENT_HOME;
            else
                Globals.e_mode = Enums.MODE.PATIENT_INVITE;
            setFragment();
            return;
        }

        if (currentFragment instanceof com.songu.pom.fragment.patient.InviteFragment && relBlast.getVisibility() == View.GONE && relOption.getVisibility() == View.GONE)
        {
            Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
            Globals.e_mode = Enums.MODE.PATIENT_HOME;
            setFragment();
        }

        if (currentFragment instanceof ProfileFragment && Globals.e_userType == Enums.USERTYPE.PATIENT && relBlast.getVisibility() == View.GONE && relOption.getVisibility() == View.GONE)
        {
            Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
            Globals.e_mode = Enums.MODE.PATIENT_HOME;
            setFragment();
        }

        if (currentFragment instanceof ChangePasswordFragment && Globals.e_userType == Enums.USERTYPE.PATIENT && relBlast.getVisibility() == View.GONE && relOption.getVisibility() == View.GONE)
        {
            Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
            Globals.e_mode = Enums.MODE.PATIENT_HOME;
            setFragment();
            return;
        }

        if (currentFragment instanceof ChangePasswordFragment && Globals.e_userType == Enums.USERTYPE.USER && relMemberPatientMenu.getVisibility() == View.GONE && relMemberOption.getVisibility() == View.GONE)
        {
            Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
            Globals.e_mode = Enums.MODE.MEMBER_HOME;
            setFragment();
        }

        if (currentFragment instanceof ProfileFragment && Globals.e_userType == Enums.USERTYPE.USER && relMemberPatientMenu.getVisibility() == View.GONE && relMemberOption.getVisibility() == View.GONE)
        {
            Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
            Globals.e_mode = Enums.MODE.MEMBER_HOME;
            setFragment();
            return;
        }

        if (currentFragment instanceof InviteFragment && relMemberPatientMenu.getVisibility() == View.GONE && relMemberOption.getVisibility() == View.GONE)
        {
            Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
            Globals.e_mode = Enums.MODE.MEMBER_HOME;
            setFragment();
            return;
        }

        if (currentFragment instanceof DetailFragment && Globals.e_userType == Enums.USERTYPE.USER && relMemberPatientMenu.getVisibility() == View.GONE && relMemberOption.getVisibility() == View.GONE)
        {
            Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
            //Globals.e_mode = Enums.MODE.MEMBER_HOME;
            Globals.e_mode = Globals.e_prevMode;
            setFragment();
            return;
        }
        if (currentFragment instanceof DetailFragment && Globals.e_userType == Enums.USERTYPE.PATIENT && relBlast.getVisibility() == View.GONE && relOption.getVisibility() == View.GONE)
        {
            Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
            Globals.e_mode = Enums.MODE.PATIENT_VISIT;
            setFragment();
            return;
        }

        if (currentFragment instanceof PatientUpdateFragment && Globals.e_userType == Enums.USERTYPE.USER && relMemberPatientMenu.getVisibility() == View.GONE && relMemberOption.getVisibility() == View.GONE)
        {
            Globals.e_transDirection = Enums.SWIPDIRECTION.RIGHT;
            Globals.e_mode = Enums.MODE.MEMBER_HOME;
            setFragment();
            return;
        }


    }
    public void swipeLeft()
    {
        if (currentFragment instanceof HomeFragment && relBlast.getVisibility() == View.GONE
                && relOption.getVisibility() == View.GONE)
        {
            Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
            Globals.e_mode = Enums.MODE.PATIENT_VISIT;
            setFragment();
        }

        if (currentFragment instanceof com.songu.pom.fragment.member.HomeFragment && relMemberPatientMenu.getVisibility() == View.GONE && relMemberOption.getVisibility() == View.GONE)
        {
            Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
            Globals.e_mode = Enums.MODE.MEMBER_INVITE;
            setFragment();
        }

        if (currentFragment instanceof com.songu.pom.fragment.member.PatientUpdateFragment && relMemberPatientMenu.getVisibility() == View.GONE && relMemberOption.getVisibility() == View.GONE)
        {
            Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
            Globals.e_mode = Enums.MODE.MEMBER_INVITE;
            setFragment();
        }
    }
    @Override
    public void onDoubleTap() {

    }
}
