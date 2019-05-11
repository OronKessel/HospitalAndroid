package com.songu.pom.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.songu.pom.R;
import com.songu.pom.activity.MainActivity;
import com.songu.pom.doc.Config;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.service.IServiceResult;
import com.songu.pom.service.ServiceManager;
import com.songu.pom.util.Utils;

import static android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;

public class MenuLoginFragment extends Fragment implements View.OnClickListener,IServiceResult{


    public View mRootView = null;
    public LinearLayout layoutNew,layoutExist;
    public LinearLayout layoutFormRegister,layoutFormLogin,layoutMenuContainer;

    public EditText editId,editEmail,editPassword;
    public EditText editLoginEmail,editLoginPassword;
    public Enums.LOGINTYPE mLoginType = Enums.LOGINTYPE.NONE;
    public TextView txtRegisterBack,txtLoginBack,txtLoginLogin,txtRegisterRegister,txtLoginForget;
    public ImageView imgLoginLogin,imgLoginBack,imgRegisterRegister,imgRegisterBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_menu_login, null);
        }
        initView();
        return mRootView;
    }

    public void initView()
    {
        Globals.gForgetEmail = "";

        layoutNew = (LinearLayout) mRootView.findViewById(R.id.layoutMenuNew);
        layoutExist = (LinearLayout) mRootView.findViewById(R.id.layoutMenuExist);
        layoutFormRegister = (LinearLayout) mRootView.findViewById(R.id.layoutMenuRegister);
        layoutFormLogin = (LinearLayout) mRootView.findViewById(R.id.layoutMenuLogin);
        layoutMenuContainer = (LinearLayout) mRootView.findViewById(R.id.layoutMenuContainer);
        txtRegisterBack = (TextView) mRootView.findViewById(R.id.txtRegisterBack);
        txtLoginBack = (TextView) mRootView.findViewById(R.id.txtLoginBack);
        txtRegisterRegister = (TextView) mRootView.findViewById(R.id.txtRegisterRegister);
        imgRegisterRegister = (ImageView) mRootView.findViewById(R.id.imgRegisterRegister);
        imgRegisterBack = (ImageView)mRootView.findViewById(R.id.imgRegisterBack);
        imgLoginBack = (ImageView) mRootView.findViewById(R.id.imgLoginBack);
        txtLoginLogin = (TextView) mRootView.findViewById(R.id.txtLoginLogin);
        imgLoginLogin = (ImageView) mRootView.findViewById(R.id.imgLoginLogin);
        txtLoginForget = (TextView) mRootView.findViewById(R.id.txtLoginForget);

        editId = mRootView.findViewById(R.id.txtUniqueId);
        editEmail = mRootView.findViewById(R.id.txtEmail);
        editPassword = mRootView.findViewById(R.id.txtPassword);

        editLoginEmail = mRootView.findViewById(R.id.txtLoginEmail);
        editLoginPassword = mRootView.findViewById(R.id.txtLoginPassword);


        txtLoginLogin.setOnClickListener(this);
        imgLoginLogin.setOnClickListener(this);
        imgRegisterRegister.setOnClickListener(this);
        imgRegisterBack.setOnClickListener(this);
        txtRegisterRegister.setOnClickListener(this);
        txtLoginForget.setOnClickListener(this);

        layoutNew.setOnClickListener(this);
        layoutExist.setOnClickListener(this);
        txtRegisterBack.setOnClickListener(this);
        txtLoginBack.setOnClickListener(this);
        imgLoginBack.setOnClickListener(this);



        layoutNew.setOnClickListener(this);
        layoutExist.setOnClickListener(this);

        layoutFormLogin.setVisibility(View.GONE);
        layoutFormRegister.setVisibility(View.GONE);
    }
    //Call Webservice for login
    public void actionLogin()
    {
        String strEmail = editLoginEmail.getText().toString();
        String strPassword = editLoginPassword.getText().toString();

        if (strEmail.equals(""))
        {
            ((MainActivity)getActivity()).dlgError.setMessage("Please enter email");
            ((MainActivity)getActivity()).dlgError.setTitle("Unable to sign in");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }

        if (strPassword.equals(""))
        {
            ((MainActivity)getActivity()).dlgError.setMessage("Please enter password");
            ((MainActivity)getActivity()).dlgError.setTitle("Unable to sign in");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }
        ((MainActivity)getActivity()).showLoader();
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String token = pref.getString("regId","");
        ServiceManager.serviceLogin("Patient",strEmail,strPassword,token,this);
    }
    //Call Webservice for Register
    public void actionRegister()
    {
        String strCode = editId.getText().toString();
        String strEmail = editEmail.getText().toString();
        String strPassword = editPassword.getText().toString();

        if (strCode.equals(""))
        {
            ((MainActivity)getActivity()).dlgError.setMessage("Please enter code");
            ((MainActivity)getActivity()).dlgError.setTitle("Unable to sign up");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }

        if (strEmail.equals(""))
        {
            ((MainActivity)getActivity()).dlgError.setMessage("Please enter email");
            ((MainActivity)getActivity()).dlgError.setTitle("Unable to sign up");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }

        if (strPassword.equals(""))
        {
            ((MainActivity)getActivity()).dlgError.setMessage("Please enter password");
            ((MainActivity)getActivity()).dlgError.setTitle("Unable to sign up");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }
        if (strPassword.length() < 6 || !Utils.isValidPassword(strPassword))
        {
            ((MainActivity)getActivity()).dlgError.setMessage("Password must contain minimum 6 characters at least 1 Alphabet, 1 Number and 1 Special Character");
            ((MainActivity)getActivity()).dlgError.setTitle("Unable to sign up");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }
        if (!strEmail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            ((MainActivity)getActivity()).dlgError.setMessage("Invalid Email Address");
            ((MainActivity)getActivity()).dlgError.setTitle("Unable to sign up");
            ((MainActivity)getActivity()).dlgError.show();
            return;
        }

//        ((MainActivity)getActivity()).dlgError.setMessage("OK");
//        ((MainActivity)getActivity()).dlgError.show();
//        return;
        ((MainActivity)getActivity()).showLoader();
        ServiceManager.serviceRegister(strCode,strEmail,strPassword,this);
    }
    public void initForm()
    {
        editLoginEmail.setText("will@test.com");
        editLoginPassword.setText("qqqqqq");
        editId.setText("P11111111");
        editEmail.setText("will@test.com");
        editPassword.setText("qqqqqq");

//          editLoginEmail.setText("tenstar718@gmail.com");
//          editLoginPassword.setText("qqqqqq");
//          editId.setText("P11111111");
//          editEmail.setText("tenstar718@gmail.com");
//          editPassword.setText("qqqqqq");


//          editLoginEmail.setText("tenstar718@gmail.com");
//          editLoginPassword.setText("qqqqqq");
//          editId.setText("P11111111");
//          editEmail.setText("tenstar718@gmail.com");
//          editPassword.setText("qqqqqq");

//        editLoginEmail.setText("pgyhw718@hotmail.com");
//        editLoginPassword.setText("qqqqqq");
//        editId.setText("U88488117");
//        editEmail.setText("pgyhw718@hotmail.com");
//        editPassword.setText("qqqqqq");

//        editLoginEmail.setText("sheriefahmed@gmail.com");
//        editLoginPassword.setText("Arsenal1!");
//        editId.setText("U88488117");
//        editEmail.setText("sheriefahmed@hotmail.com");
//        editPassword.setText("Arsenal1!");
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.txtLoginForget: //click forget
                Globals.e_mode = Enums.MODE.FORGETPASSWORD;
                Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
                ((MainActivity)getActivity()).setFragment();
                break;
            case R.id.txtRegisterBack:
            case R.id.txtLoginBack:
            case R.id.imgLoginBack:
            case R.id.imgRegisterBack:
                layoutExist.setVisibility(View.VISIBLE);
                layoutNew.setVisibility(View.VISIBLE);
                mLoginType = Enums.LOGINTYPE.NONE;
                layoutFormLogin.setVisibility(View.GONE);
                layoutFormRegister.setVisibility(View.GONE);
                break;
            case R.id.layoutMenuNew:
                layoutExist.setVisibility(View.GONE);
                layoutNew.setVisibility(View.VISIBLE);
                initForm();
                mLoginType = Enums.LOGINTYPE.NEW;

                //layoutFormRegister.animate().alpha(1.0f).setDuration(2000);
                //layoutFormLogin.animate().alpha(0.0f).setDuration(2000);
                layoutFormLogin.setVisibility(View.GONE);
                layoutFormRegister.setVisibility(View.VISIBLE);
                Animation slide_register = AnimationUtils.loadAnimation(this.getContext(),R.anim.anim_top_menu);
                layoutFormRegister.startAnimation(slide_register);
                slide_register.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case R.id.layoutMenuExist:
                layoutExist.setVisibility(View.VISIBLE);
                layoutNew.setVisibility(View.GONE);
                initForm();
                mLoginType = Enums.LOGINTYPE.EXIST;
                layoutFormLogin.setVisibility(View.VISIBLE);
                layoutFormRegister.setVisibility(View.GONE);
                Animation slide_login = AnimationUtils.loadAnimation(this.getContext(),R.anim.anim_top_menu);
                layoutFormLogin.startAnimation(slide_login);
                slide_login.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                break;
            case R.id.imgRegisterRegister:
            case R.id.txtRegisterRegister:
                actionRegister();
                break;
            case R.id.imgLoginLogin:
            case R.id.txtLoginLogin:
                actionLogin();
                break;
        }
    }

    @Override
    public void onResponse(int code) {
        ((MainActivity)getActivity()).dismissLoader();
        switch (code)
        {
            case 202://Success Login
                if (Globals.e_userType == Enums.USERTYPE.PATIENT) {
                    Globals.e_mode = Enums.MODE.PATIENT_HOME;
                    ((MainActivity) getActivity()).setFragment();
                }
                else if (Globals.e_userType == Enums.USERTYPE.USER)
                {
                    Globals.e_mode = Enums.MODE.MEMBER_HOME;
                    ((MainActivity) getActivity()).setFragment();
                }
                break;
            case 200://Success Register
                if (mLoginType == Enums.LOGINTYPE.NEW)
                {
                    Globals.e_mode = Enums.MODE.CONSENT;
                    ((MainActivity)getActivity()).setFragment();
                }
                else
                {
                    if (Globals.e_userType == Enums.USERTYPE.PATIENT) {
                        Globals.e_mode = Enums.MODE.PATIENT_HOME;
                        ((MainActivity) getActivity()).setFragment();
                    }
                    else if (Globals.e_userType == Enums.USERTYPE.USER)
                    {
                        Globals.e_mode = Enums.MODE.MEMBER_HOME;
                        ((MainActivity) getActivity()).setFragment();
                    }

                }
                break;
            case 400://Fail Register
                ((MainActivity)getActivity()).dlgError.setMessage("Unique ID does not exist. Please check the ID again.");
                ((MainActivity)getActivity()).dlgError.setTitle("Unable to sign up");
                ((MainActivity)getActivity()).dlgError.show();
                break;
            case 401://Fail Login
                ((MainActivity)getActivity()).dlgError.setMessage("You have incorrect credentials, please try again.");
                ((MainActivity)getActivity()).dlgError.setTitle("Unable to sign in");
                ((MainActivity)getActivity()).dlgError.show();
                break;
        }
    }
}
