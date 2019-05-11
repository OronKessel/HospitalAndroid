package com.songu.pom.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.songu.pom.R;
import com.songu.pom.doc.Config;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.service.IServiceResult;
import com.songu.pom.service.ServiceManager;
import com.songu.pom.util.loader.ACProgressConstant;
import com.songu.pom.util.loader.ACProgressFlower;

public class BePatientActivity extends Activity implements View.OnClickListener,IServiceResult {

    public EditText editUnique;
    public TextView txtDone;
    public ImageView imgDone;
    public ACProgressFlower dlgLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bepatient);
        initView();
        initializeDlg();
    }

    public void initView()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        editUnique = (EditText) this.findViewById(R.id.txtUniqueId);
        txtDone = (TextView) this.findViewById(R.id.txtRegisterRegister);
        imgDone = (ImageView) this.findViewById(R.id.imgRegisterRegister);
        editUnique.setText(Globals.mAccount.mCode);
        txtDone.setOnClickListener(this);
        imgDone.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.txtRegisterRegister:
            case R.id.imgRegisterRegister://click patient info
                showLoader();
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                String token = pref.getString("regId","");
                Globals.e_userType = Enums.USERTYPE.PATIENT;
                ServiceManager.serviceAgreeTerm(Globals.mAccount.mCode,token,this);
                break;
        }
    }

    @Override
    public void onResponse(int code) {
        dismissLoader();
        switch (code)
        {
            case 200://Success get patient info from webservice
                Globals.e_mode = Enums.MODE.PATIENT_HOME;
                Intent m = new Intent(this,MainActivity.class);
                m.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(m);
                break;
            case 400:
                break;
        }
    }
}
