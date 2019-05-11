package com.songu.pom.model;

import com.songu.pom.doc.Globals;
import com.songu.pom.service.ServiceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 7/31/2018.
 */

public class UserModel {
    public String mID;
    public String mHealthID;
    public String mFirstName;
    public String mLastName;
    public String mPhone;
    public String mHomePhone;
    public String mGender;
    public String mName;
    public String mEmail;
    public String mPassword;
    public String mQuestion;
    public String mAnswer;
    public String mEmergency;
    public String mCode;
    public String mToken;
    public String mStatus;
    public String mActive;
    public String mInvitedTimestamp;
    public HashMap<String,ServiceModel> mServices = new HashMap<>();
    public boolean isExpand = false;
    public boolean isSelectAll = false;
    public boolean isPatientSelect = false;

    public UserModel()
    {
        initServices();
    }
    public void initServices()
    {
        if (Globals.gServices == null) return;
        for (int i = 1;i < Globals.gServices.length;i++)
        {
            ServiceModel serviceModel = new ServiceModel();
            serviceModel.mName = Globals.gServices[i];
            serviceModel.isSelect = false;
            mServices.put(Globals.gServices[i],serviceModel);
        }
    }

    public String toJsonPatient()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", this.mID);
            jsonObject.put("healthId", this.mHealthID);
            jsonObject.put("email", this.mEmail);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    public JSONObject toJsonMember()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", this.mEmail);
            jsonObject.put("fname", this.mFirstName);
            jsonObject.put("lname", this.mLastName);
            //jsonObject.put("phone", this.mPhone);

            JSONArray serviceArray = new JSONArray();
            for (int i = 1;i < Globals.gServices.length;i++)
            {
                JSONObject serviceObj = new JSONObject();
                serviceObj.put("name",this.mServices.get(Globals.gServices[i]).mName);
                serviceObj.put("isSelect",this.mServices.get(Globals.gServices[i]).isSelect);
                serviceArray.put(serviceObj);
            }
            jsonObject.put("service",serviceArray);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }
    public String toJsonStringMember()
    {
        return toJsonMember().toString();
    }
}
