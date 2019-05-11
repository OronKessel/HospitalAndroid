package com.songu.pom.doc;


import com.songu.pom.model.MessageModel;
import com.songu.pom.model.PatientModel;
import com.songu.pom.model.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 1/12/2018.
 */

public class Globals {

    public static UserModel mAccount = new UserModel();
    public static Enums.MODE e_prevMode = Enums.MODE.MENULOGIN;
    public static Enums.MODE e_mode =  Enums.MODE.MENULOGIN;
    public static Enums.USERTYPE e_userType =  Enums.USERTYPE.NONE;
    public static Enums.SWIPDIRECTION e_transDirection = Enums.SWIPDIRECTION.LEFT;
    public static String g_fcmToken = "";
    public static List<UserModel> g_inviteList = new ArrayList<>();
    public static List<UserModel> g_inviteList1 = new ArrayList<>();
    public static List<UserModel> g_inviteMemberList = new ArrayList<>();
    public static UserModel g_pendingInvite = new UserModel();
    public static String[] gServices = {"Select All","Patient Status (Bed, in OR, Recovering)","Orders (Labs, Imaging, Consultation)","Observations (Pre and Post Operation)","Documents/Notes (Physician Notes)"};
    public static PatientModel gSelectPatient = null;
    public static List<MessageModel> g_messageList = new ArrayList<>();
    public static MessageModel gCurrentMsg = new MessageModel();
    public static String gForgetEmail = "";
    public static boolean isFirstPatientLoad = true;
}
