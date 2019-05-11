package com.songu.pom.doc;

import android.Manifest;

/**
 * Created by Administrator on 2/20/2017.
 */
public class Config {

    //public static String mBaseUrl = "http://ec2-18-222-134-204.us-east-2.compute.amazonaws.com/index.php";
    public static String mBaseUrl = "https://nu3iwgn4yi.execute-api.us-east-1.amazonaws.com/default/mobile_service";
    public static String mApiKey = "tNECQoX8HK9SsYtwVTKvM4SFtFVAYYsB1ce46zLk";
    public static String mRegisterService = "serviceRegister";
    public static String mLoginService = "serviceLogin";
    public static String mAgreeTermService = "serviceAgreeTerm";
    public static String mRefuseTermService = "serviceRefuseTerm";
    public static String mGetInviteMembers = "serviceGetInviteMembers";
    public static String mInviteMembers = "serviceInviteMembers";
    public static String mGetProfilePatient = "serviceProfilePatient";
    public static String mUpdateProfilePatient = "serviceUpdateProfilePatient";
    public static String mChangePasswordPatient = "serviceChangePasswordPatient";
    public static String mSendBlastMembers = "serviceSendBlastMembers";
    public static String mGetInvitePatients = "serviceGetInvitePatients";
    public static String mGetProfileMember = "serviceProfileMember";
    public static String mUpdateMemberPatient = "serviceUpdateProfileMember";
    public static String mChangePasswordMember = "serviceChangePasswordMember";
    public static String mGetPatientInfo = "serviceGetPatientInfo";
    public static String mGetMessageUpdate = "serviceGetSummary";
    public static String mGetSelfMessageUpdate = "serviceGetSelfSummary";
    public static String mForgetPassword = "serviceForgetPassword";
    public static String mResetPassword = "serviceResetPassword";




    public static String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS};


    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";


}

