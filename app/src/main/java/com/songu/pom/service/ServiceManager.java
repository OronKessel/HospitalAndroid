package com.songu.pom.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.UserManager;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.songu.pom.activity.MainActivity;
import com.songu.pom.doc.Config;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.model.MessageModel;
import com.songu.pom.model.PatientModel;
import com.songu.pom.model.ServiceModel;
import com.songu.pom.model.UserModel;
import com.songu.pom.util.HttpUtil;
import com.songu.pom.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2/20/2017.
 */
public class ServiceManager {

    private static RequestQueue mRequestQueue;

    public static void init(Context context)
    {
        mRequestQueue = Volley.newRequestQueue(context);
    }
    public static void serviceResetPassword(final String oldwp,final String newpw,final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mResetPassword);
            requestObj.put("email",Globals.gForgetEmail);
            requestObj.put("oldpw",oldwp);
            requestObj.put("newpw",newpw);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                caller.onResponse(201);
                            }
                            else
                            {
                                caller.onResponse(401);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(401);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(401);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
    public static void serviceForgetPassword(final String email,final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mForgetPassword);
            requestObj.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                caller.onResponse(200);
                            }
                            else
                            {
                                caller.onResponse(401);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(401);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(401);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
    public static void serviceGetPatientUpdate(final String memberId,final String mPatientId,final String mHealthID,final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mGetSelfMessageUpdate);
            requestObj.put("member",memberId);
            requestObj.put("patientId",mPatientId);
            requestObj.put("healthId",mHealthID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                Globals.g_messageList.clear();
                                JSONArray messageArray = response.getJSONArray("messages");
                                for (int i = 0;i < messageArray.length();i++)
                                {
                                    JSONObject msgObj = messageArray.getJSONObject(i);
                                    MessageModel msgModel = new MessageModel();
                                    msgModel.mTimestamp = msgObj.getString("timestamp");
                                    msgModel.mType = msgObj.getString("type");
                                    msgModel.mMessageJson = msgObj.getJSONObject("info");
                                    Globals.g_messageList.add(msgModel);
                                }
                                caller.onResponse(200);
                            }
                            else
                            {
                                caller.onResponse(401);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(401);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(401);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }

    public static void serviceGetUpdatePatient(final String memberId,final String patiendId, final String healthId, final IServiceResult caller )
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mGetMessageUpdate);
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("patientId",patiendId);
            jsonObj.put("healthId",healthId);
            jsonArray.put(jsonObj);
            requestObj.put("member",memberId);
            requestObj.put("patients",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                Globals.g_messageList.clear();
                                JSONArray messageArray = response.getJSONArray("messages");
                                for (int i = 0;i < messageArray.length();i++)
                                {
                                    JSONObject msgObj = messageArray.getJSONObject(i);
                                    MessageModel msgModel = new MessageModel();
                                    msgModel.mTimestamp = msgObj.getString("timestamp");
                                    msgModel.mType = msgObj.getString("type");
                                    msgModel.mMessageJson = msgObj.getJSONObject("info");
                                    msgModel.mName = Utils.getFieldFromJson("name",msgObj);
                                    msgModel.mFirstName = Utils.getFieldFromJson("fname",msgObj);
                                    msgModel.mLastName = Utils.getFieldFromJson("lname",msgObj);
                                    Globals.g_messageList.add(msgModel);
                                }
                                caller.onResponse(200);
                            }
                            else
                            {
                                caller.onResponse(401);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(401);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(401);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
    public static void serviceGetUpdate(final String memberId,final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mGetMessageUpdate);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0;i < Globals.g_inviteList.size();i++)
            {
                if (Globals.g_inviteList.get(i).isPatientSelect)
                {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("patientId",Globals.g_inviteList.get(i).mID);
                    jsonObj.put("healthId",Globals.g_inviteList.get(i).mHealthID);
                    jsonArray.put(jsonObj);
                }
            }
            requestObj.put("member",memberId);
            requestObj.put("patients",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                Globals.g_messageList.clear();
                                JSONArray messageArray = response.getJSONArray("messages");
                                for (int i = 0;i < messageArray.length();i++)
                                {
                                    JSONObject msgObj = messageArray.getJSONObject(i);
                                    MessageModel msgModel = new MessageModel();
                                    msgModel.mTimestamp = msgObj.getString("timestamp");
                                    msgModel.mType = msgObj.getString("type");
                                    msgModel.mMessageJson = msgObj.getJSONObject("info");
                                    msgModel.mName = Utils.getFieldFromJson("name",msgObj);
                                    msgModel.mFirstName = Utils.getFieldFromJson("fname",msgObj);
                                    msgModel.mLastName = Utils.getFieldFromJson("lname",msgObj);
                                    Globals.g_messageList.add(msgModel);
                                }
                                caller.onResponse(200);
                            }
                            else
                            {
                                caller.onResponse(401);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(401);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(401);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
    public static void serviceGetPatientInfo(final String mEmail,final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mGetPatientInfo);
            requestObj.put("email",mEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                JSONObject patientObj = response.getJSONObject("patient");

                                PatientModel uModel = new PatientModel();
                                uModel.mBed = Utils.getFieldFromJson("bed",patientObj);
                                uModel.mDischargeDatetime = Utils.getFieldFromJson("discharge_datetime",patientObj);
                                uModel.mBedStatus = Utils.getFieldFromJson("bed_status",patientObj);
                                uModel.mAdmissionType = Utils.getFieldFromJson("admission_type",patientObj);
                                uModel.mGender = Utils.getFieldFromJson("sex",patientObj);
                                uModel.mPatientClass = Utils.getFieldFromJson("patient_class",patientObj);
                                uModel.mHealthID = Utils.getFieldFromJson("health_system_id",patientObj);
                                uModel.mVisitNumber = Utils.getFieldFromJson("visit_number",patientObj);
                                uModel.mEmail = Utils.getFieldFromJson("email_address",patientObj);
                                uModel.mFloor = Utils.getFieldFromJson("floor",patientObj);
                                uModel.mLastName = Utils.getFieldFromJson("last_name",patientObj);
                                uModel.mHospitalService = Utils.getFieldFromJson("hospital_service",patientObj);
                                uModel.mPreAdmitTestIndicator = Utils.getFieldFromJson("preadmit_test_indicator",patientObj);
                                uModel.mFirstName = Utils.getFieldFromJson("first_name",patientObj);
                                uModel.mPhone = Utils.getFieldFromJson("mobile_phone",patientObj);
                                uModel.mHomePhone = Utils.getFieldFromJson("home_phone",patientObj);
                                uModel.mAdmitDatetime = Utils.getFieldFromJson("admit_datetime",patientObj);
                                uModel.mMiddleName = Utils.getFieldFromJson("middle_name",patientObj);
                                uModel.mID = Utils.getFieldFromJson("patient_id",patientObj);
                                uModel.mRoom = Utils.getFieldFromJson("room",patientObj);
                                uModel.mActive = Utils.getFieldFromJson("active",patientObj);
                                uModel.mCode = Utils.getFieldFromJson("code",patientObj);
                                uModel.mPassword = Utils.getFieldFromJson("password",patientObj);
                                Globals.mAccount = uModel;
                                if (uModel.mActive.equals("1"))
                                {
                                    caller.onResponse(201);
                                }
                                else
                                {
                                    caller.onResponse(202);
                                }
                            }
                            else
                            {
                                caller.onResponse(401);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(401);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(401);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }

    public static void serviceGetInvitePatients1(final String mID,final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mGetInvitePatients);
            requestObj.put("userId",mID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                JSONArray inviteArray = response.getJSONArray("invites");
                                for (int i = 0;i < inviteArray.length();i++)
                                {
                                    JSONObject inviteObj = inviteArray.getJSONObject(i);

                                    PatientModel uModel = new PatientModel();
                                    uModel.mBed = Utils.getFieldFromJson("bed",inviteObj);
                                    uModel.mDischargeDatetime = Utils.getFieldFromJson("discharge_datetime",inviteObj);
                                    uModel.mBedStatus = Utils.getFieldFromJson("bed_status",inviteObj);
                                    uModel.mAdmissionType = Utils.getFieldFromJson("admission_type",inviteObj);
                                    uModel.mGender = Utils.getFieldFromJson("sex",inviteObj);
                                    uModel.mPatientClass = Utils.getFieldFromJson("patient_class",inviteObj);
                                    uModel.mHealthID = Utils.getFieldFromJson("health_system_id",inviteObj);
                                    uModel.mVisitNumber = Utils.getFieldFromJson("visit_number",inviteObj);
                                    uModel.mEmail = Utils.getFieldFromJson("email_address",inviteObj);
                                    uModel.mFloor = Utils.getFieldFromJson("floor",inviteObj);
                                    uModel.mLastName = Utils.getFieldFromJson("last_name",inviteObj);
                                    uModel.mHospitalService = Utils.getFieldFromJson("hospital_service",inviteObj);
                                    uModel.mPreAdmitTestIndicator = Utils.getFieldFromJson("preadmit_test_indicator",inviteObj);
                                    uModel.mFirstName = Utils.getFieldFromJson("first_name",inviteObj);
                                    uModel.mPhone = Utils.getFieldFromJson("mobile_phone",inviteObj);
                                    uModel.mHomePhone = Utils.getFieldFromJson("home_phone",inviteObj);
                                    uModel.mAdmitDatetime = Utils.getFieldFromJson("admit_datetime",inviteObj);
                                    uModel.mMiddleName = Utils.getFieldFromJson("middle_name",inviteObj);
                                    uModel.mID = Utils.getFieldFromJson("patient_id",inviteObj);
                                    uModel.mRoom = Utils.getFieldFromJson("room",inviteObj);

                                    JSONObject inviteInfo = inviteObj.getJSONObject("invite_info");
                                    uModel.mCode = Utils.getFieldFromJson("code",inviteInfo);
                                    uModel.mActive = Utils.getFieldFromJson("active",inviteInfo);
                                    uModel.mInvitedTimestamp = Utils.getFieldFromJson("invite_time",inviteInfo);

                                    if (uModel.mActive.equals("1"))
                                        Globals.g_inviteList.add(uModel);
                                }
                                caller.onResponse(201);
                            }
                            else
                            {
                                caller.onResponse(400);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(400);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(400);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }


    public static void serviceGetInvitePatients(final String mID,final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mGetInvitePatients);
            requestObj.put("userId",mID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                JSONArray inviteArray = response.getJSONArray("invites");
                                for (int i = 0;i < inviteArray.length();i++)
                                {
                                    JSONObject inviteObj = inviteArray.getJSONObject(i);

                                    PatientModel uModel = new PatientModel();
                                    uModel.mBed = Utils.getFieldFromJson("bed",inviteObj);
                                    uModel.mDischargeDatetime = Utils.getFieldFromJson("discharge_datetime",inviteObj);
                                    uModel.mBedStatus = Utils.getFieldFromJson("bed_status",inviteObj);
                                    uModel.mAdmissionType = Utils.getFieldFromJson("admission_type",inviteObj);
                                    uModel.mGender = Utils.getFieldFromJson("sex",inviteObj);
                                    uModel.mPatientClass = Utils.getFieldFromJson("patient_class",inviteObj);
                                    uModel.mHealthID = Utils.getFieldFromJson("health_system_id",inviteObj);
                                    uModel.mVisitNumber = Utils.getFieldFromJson("visit_number",inviteObj);
                                    uModel.mEmail = Utils.getFieldFromJson("email_address",inviteObj);
                                    uModel.mFloor = Utils.getFieldFromJson("floor",inviteObj);
                                    uModel.mLastName = Utils.getFieldFromJson("last_name",inviteObj);
                                    uModel.mHospitalService = Utils.getFieldFromJson("hospital_service",inviteObj);
                                    uModel.mPreAdmitTestIndicator = Utils.getFieldFromJson("preadmit_test_indicator",inviteObj);
                                    uModel.mFirstName = Utils.getFieldFromJson("first_name",inviteObj);
                                    uModel.mPhone = Utils.getFieldFromJson("mobile_phone",inviteObj);
                                    uModel.mHomePhone = Utils.getFieldFromJson("home_phone",inviteObj);
                                    uModel.mAdmitDatetime = Utils.getFieldFromJson("admit_datetime",inviteObj);
                                    uModel.mMiddleName = Utils.getFieldFromJson("middle_name",inviteObj);
                                    uModel.mID = Utils.getFieldFromJson("patient_id",inviteObj);
                                    uModel.mRoom = Utils.getFieldFromJson("room",inviteObj);

                                    JSONObject inviteInfo = inviteObj.getJSONObject("invite_info");
                                    uModel.mCode = Utils.getFieldFromJson("code",inviteInfo);
                                    uModel.mActive = Utils.getFieldFromJson("active",inviteInfo);
                                    uModel.mInvitedTimestamp = Utils.getFieldFromJson("invite_time",inviteInfo);

                                    Globals.g_inviteList.add(uModel);
                                }
                                caller.onResponse(201);
                            }
                            else
                            {
                                caller.onResponse(400);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(400);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(400);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
    public static void serviceSendBlastMessage(final JSONArray jsonMembers,final String message, final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mSendBlastMembers);
            requestObj.put("patientId",Globals.mAccount.mID);
            requestObj.put("healthId",Globals.mAccount.mHealthID);
            requestObj.put("members",jsonMembers);
            requestObj.put("message",message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                caller.onResponse(200);
                            }
                            else
                            {
                                caller.onResponse(400);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(400);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(400);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
    public static void serviceChangeMemberPassword(final String oldPw, final String newPw, final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mChangePasswordMember);
            requestObj.put("userId",Globals.mAccount.mID);
            requestObj.put("newPw",newPw);
            requestObj.put("oldPw",oldPw);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                caller.onResponse(200);
                            }
                            else
                            {
                                caller.onResponse(400);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(400);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(400);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
    public static void serviceChangePassword(final String oldPw, final String newPw, final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mChangePasswordPatient);
            requestObj.put("patientId",Globals.mAccount.mID);
            requestObj.put("healthId",Globals.mAccount.mHealthID);
            requestObj.put("newPw",newPw);
            requestObj.put("oldPw",oldPw);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                caller.onResponse(200);
                            }
                            else
                            {
                                caller.onResponse(400);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(400);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(400);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }

    public static void serviceUpdateProfileMember(final String firstName, final String lastName,final String phone,final String gender, final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mUpdateMemberPatient);
            requestObj.put("userId",Globals.mAccount.mID);
            requestObj.put("firstName",firstName);
            requestObj.put("lastName",lastName);
            requestObj.put("phone",phone);
            requestObj.put("gender",gender);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                JSONObject userObject = response.getJSONObject("info");
                                UserModel uModel = new UserModel();
                                uModel.mID = Utils.getFieldFromJson("user_id",userObject);
                                uModel.mEmail = Utils.getFieldFromJson("user_id",userObject);
                                uModel.mFirstName = Utils.getFieldFromJson("fname",userObject);
                                uModel.mLastName = Utils.getFieldFromJson("lname",userObject);
                                uModel.mPhone = Utils.getFieldFromJson("phone",userObject);
                                uModel.mGender = Utils.getFieldFromJson("sex",userObject);
                                Globals.mAccount = uModel;
                                caller.onResponse(201);
                            }
                            else
                            {
                                caller.onResponse(400);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(400);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(400);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
    public static void serviceUpdateProfilePatient(final String firstName, final String lastName, final String email,final String phone,final String gender, final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mUpdateProfilePatient);
            requestObj.put("patientId",Globals.mAccount.mID);
            requestObj.put("healthId",Globals.mAccount.mHealthID);
            requestObj.put("firstName",firstName);
            requestObj.put("lastName",lastName);
            requestObj.put("email",email);
            requestObj.put("phone",phone);
            requestObj.put("gender",gender);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                JSONObject userObject = response.getJSONObject("info");
                                PatientModel uModel = new PatientModel();
                                uModel.mBed = Utils.getFieldFromJson("bed",userObject);
                                uModel.mDischargeDatetime = Utils.getFieldFromJson("discharge_datetime",userObject);
                                uModel.mBedStatus = Utils.getFieldFromJson("bed_status",userObject);
                                uModel.mAdmissionType = Utils.getFieldFromJson("admission_type",userObject);
                                uModel.mGender = Utils.getFieldFromJson("sex",userObject);
                                uModel.mPatientClass = Utils.getFieldFromJson("patient_class",userObject);
                                uModel.mHealthID = Utils.getFieldFromJson("health_system_id",userObject);
                                uModel.mVisitNumber = Utils.getFieldFromJson("visit_number",userObject);
                                uModel.mEmail = Utils.getFieldFromJson("email_address",userObject);
                                uModel.mFloor = Utils.getFieldFromJson("floor",userObject);
                                uModel.mLastName = Utils.getFieldFromJson("last_name",userObject);
                                uModel.mHospitalService = Utils.getFieldFromJson("hospital_service",userObject);
                                uModel.mPreAdmitTestIndicator = Utils.getFieldFromJson("preadmit_test_indicator",userObject);
                                uModel.mFirstName = Utils.getFieldFromJson("first_name",userObject);
                                uModel.mPhone = Utils.getFieldFromJson("mobile_phone",userObject);
                                uModel.mHomePhone = Utils.getFieldFromJson("home_phone",userObject);
                                uModel.mAdmitDatetime = Utils.getFieldFromJson("admit_datetime",userObject);
                                uModel.mMiddleName = Utils.getFieldFromJson("middle_name",userObject);
                                uModel.mID = Utils.getFieldFromJson("patient_id",userObject);
                                uModel.mRoom = Utils.getFieldFromJson("room",userObject);
                                Globals.mAccount = uModel;
                                caller.onResponse(201);
                            }
                            else
                            {
                                caller.onResponse(400);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(400);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(400);
            }
        });
        mRequestQueue.add(req);
    }
    public static void serviceGetProfileMember(final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mGetProfileMember);
            requestObj.put("userId",Globals.mAccount.mID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                JSONObject userObject = response.getJSONObject("info");
                                UserModel uModel = new UserModel();
                                uModel.mID = Utils.getFieldFromJson("user_id",userObject);
                                uModel.mEmail = Utils.getFieldFromJson("user_id",userObject);
                                uModel.mFirstName = Utils.getFieldFromJson("fname",userObject);
                                uModel.mLastName = Utils.getFieldFromJson("lname",userObject);
                                uModel.mPhone = Utils.getFieldFromJson("phone",userObject);
                                uModel.mGender = Utils.getFieldFromJson("sex",userObject);
                                Globals.mAccount = uModel;
                                caller.onResponse(200);
                            }
                            else
                            {
                                caller.onResponse(400);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(400);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(400);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
    public static void serviceGetProfilePatient(final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mGetProfilePatient);
            requestObj.put("patientId",Globals.mAccount.mID);
            requestObj.put("healthId",Globals.mAccount.mHealthID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                JSONObject userObject = response.getJSONObject("info");
                                PatientModel uModel = new PatientModel();
                                uModel.mBed = Utils.getFieldFromJson("bed",userObject);
                                uModel.mDischargeDatetime = Utils.getFieldFromJson("discharge_datetime",userObject);
                                uModel.mBedStatus = Utils.getFieldFromJson("bed_status",userObject);
                                uModel.mAdmissionType = Utils.getFieldFromJson("admission_type",userObject);
                                uModel.mGender = Utils.getFieldFromJson("sex",userObject);
                                uModel.mPatientClass = Utils.getFieldFromJson("patient_class",userObject);
                                uModel.mHealthID = Utils.getFieldFromJson("health_system_id",userObject);
                                uModel.mVisitNumber = Utils.getFieldFromJson("visit_number",userObject);
                                uModel.mEmail = Utils.getFieldFromJson("email_address",userObject);
                                uModel.mFloor = Utils.getFieldFromJson("floor",userObject);
                                uModel.mLastName = Utils.getFieldFromJson("last_name",userObject);
                                uModel.mHospitalService = Utils.getFieldFromJson("hospital_service",userObject);
                                uModel.mPreAdmitTestIndicator = Utils.getFieldFromJson("preadmit_test_indicator",userObject);
                                uModel.mFirstName = Utils.getFieldFromJson("first_name",userObject);
                                uModel.mPhone = Utils.getFieldFromJson("mobile_phone",userObject);
                                uModel.mHomePhone = Utils.getFieldFromJson("home_phone",userObject);
                                uModel.mAdmitDatetime = Utils.getFieldFromJson("admit_datetime",userObject);
                                uModel.mMiddleName = Utils.getFieldFromJson("middle_name",userObject);
                                uModel.mID = Utils.getFieldFromJson("patient_id",userObject);
                                uModel.mRoom = Utils.getFieldFromJson("room",userObject);
                                uModel.mPassword = Utils.getFieldFromJson("password",userObject);
                                uModel.mCode = Utils.getFieldFromJson("code",userObject);
                                Globals.mAccount = uModel;
                                caller.onResponse(200);
                            }
                            else
                            {
                                caller.onResponse(400);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(400);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(400);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
    public static void serviceInviteMembers(final JSONArray inviteJson,final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mInviteMembers);
            requestObj.put("inviteJson",inviteJson);
            requestObj.put("patientId",Globals.mAccount.mID);
            requestObj.put("healthId",Globals.mAccount.mHealthID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                caller.onResponse(200);
                            }
                            else
                            {
                                caller.onResponse(400);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(400);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(400);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
    public static void serviceGetInviteMembers(final IServiceResult caller)
    {
        Globals.g_inviteList.clear();
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mGetInviteMembers);
            requestObj.put("patientId",Globals.mAccount.mID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                JSONArray userArray = response.getJSONArray("users");
                                for (int i = 0;i < userArray.length();i++)
                                {
                                    JSONObject userObj = userArray.getJSONObject(i);
                                    UserModel uModel = new UserModel();
                                    uModel.mID = Utils.getFieldFromJson("user_id",userObj);
                                    uModel.mEmail = Utils.getFieldFromJson("user_id",userObj);
                                    uModel.mInvitedTimestamp = Utils.getFieldFromJson("invite_time",userObj);
                                    JSONObject infoObj = userObj.getJSONObject("info");
                                    uModel.mFirstName = Utils.getFieldFromJson("fname",infoObj);
                                    uModel.mLastName = Utils.getFieldFromJson("lname",infoObj);

                                    String serviceJsonString = Utils.getFieldFromJson("service",userObj);
                                    if (!serviceJsonString.equals(""))
                                    {
                                        JSONArray serviceArray = new JSONArray((serviceJsonString));
                                        for (int k = 0;k < serviceArray.length();k++)
                                        {
                                            JSONObject serviceObj = serviceArray.getJSONObject(k);
                                            ServiceModel sModel = uModel.mServices.get(serviceObj.getString("name"));
                                            if (sModel == null)
                                            {
                                                sModel = new ServiceModel();
                                                sModel.mName = Globals.gServices[k];
                                                sModel.isSelect = false;
                                            }
                                            else
                                                sModel.isSelect = serviceObj.getBoolean("isSelect");
                                            uModel.mServices.put(serviceObj.getString("name"),sModel);
                                        }
                                    }
                                    Globals.g_inviteList.add(uModel);
                                }
                                caller.onResponse(200);
                            }
                            else
                            {
                                caller.onResponse(400);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(400);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(400);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
    public static void serviceAgreeTerm(final String code,final String token,final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mAgreeTermService);
            requestObj.put("type",Globals.e_userType.ordinal());
            requestObj.put("id",Globals.mAccount.mID);
            requestObj.put("healthId",Globals.mAccount.mHealthID);
            requestObj.put("password",Globals.mAccount.mPassword);
            requestObj.put("code",code);
            if (Globals.e_userType == Enums.USERTYPE.USER)
                requestObj.put("healthId","None");
            requestObj.put("token",token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                caller.onResponse(200);
                            }
                            else
                            {
                                caller.onResponse(400);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(400);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(400);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
    public static void serviceLogin(final String type,final String email,final String password,final String token, final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mLoginService);
            requestObj.put("email",email);
            requestObj.put("password",password);
            requestObj.put("token",token);
            requestObj.put("type",type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                String type = response.getString("type");
                                if (type.equals("User"))
                                {
                                    Globals.e_userType = Enums.USERTYPE.USER;
                                    UserModel uModel = new UserModel();
                                    uModel.mID = response.getJSONObject("info").getString("user_id");
                                    uModel.mEmail = email;
                                    uModel.mPassword = password;
                                    //uModel.mCode = response.getJSONObject("info").getString("code");
                                    Globals.mAccount = uModel;
                                }
                                else
                                {
                                    Globals.e_userType = Enums.USERTYPE.PATIENT;
                                    UserModel uModel = new UserModel();
                                    uModel.mID = response.getJSONObject("info").getString("patient_id");
                                    uModel.mHealthID = response.getJSONObject("info").getString("health_system_id");
                                    uModel.mEmail = email;
                                    uModel.mPassword = password;
                                    uModel.mCode = Utils.getFieldFromJson("code",response.getJSONObject("info"));
                                    Globals.mAccount = uModel;
                                }
                                //String type = response.getJSONObject("info").getString("")
                                caller.onResponse(202);
                            }
                            else
                            {
                                caller.onResponse(401);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(401);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(401);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
    public static void serviceRegister(final String code, final String email,final String password,final IServiceResult caller)
    {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("service",Config.mRegisterService);
            requestObj.put("code",code);
            requestObj.put("email",email);
            requestObj.put("password",password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.mBaseUrl, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("200"))
                            {
                                String type = response.getString("type");
                                if (type.equals("User"))
                                {
                                    Globals.e_userType = Enums.USERTYPE.USER;
                                    UserModel uModel = new UserModel();
                                    uModel.mID = response.getJSONObject("info").getString("user_id");
                                    uModel.mEmail = email;
                                    uModel.mPassword = password;
                                    uModel.mCode = code;
                                    Globals.mAccount = uModel;
                                }
                                else
                                {
                                    Globals.e_userType = Enums.USERTYPE.PATIENT;
                                    UserModel uModel = new UserModel();
                                    uModel.mID = response.getJSONObject("info").getString("patient_id");
                                    uModel.mHealthID = response.getJSONObject("info").getString("health_system_id");
                                    uModel.mEmail = email;
                                    uModel.mPassword = password;
                                    uModel.mCode = code;
                                    Globals.mAccount = uModel;
                                }
                                //String type = response.getJSONObject("info").getString("")
                                caller.onResponse(200);
                            }
                            else
                            {
                                caller.onResponse(400);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            caller.onResponse(400);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                caller.onResponse(400);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key",Config.mApiKey);
                return params;
            }
        };
        mRequestQueue.add(req);
    }
}

