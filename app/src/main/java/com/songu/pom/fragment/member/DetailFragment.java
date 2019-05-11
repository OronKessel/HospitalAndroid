package com.songu.pom.fragment.member;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.songu.pom.R;
import com.songu.pom.adapter.AdapterMemberUpdate;
import com.songu.pom.doc.Globals;
import com.songu.pom.model.UserModel;
import com.songu.pom.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {//Detail Screen for Message

    public View mRootView = null;
    public TextView txtContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_customer_detail, null);
        }
        initView();
        setData();
        return mRootView;
    }

    public void initView()
    {
        txtContent = (TextView) mRootView.findViewById(R.id.txtUpdatePatientContent);
    }
    public void setData() //Set Data for each Type of Message
    {
        String content = "";
        if (Globals.gCurrentMsg.mType.equals("PR"))
        {
            content = getContentProcedure();
        }
        if (Globals.gCurrentMsg.mType.equals("POBV"))
        {
            content = getContentPObv();
        }
        if (Globals.gCurrentMsg.mType.equals("BLT"))
        {
            content = getBlastMessage();
        }
        if (Globals.gCurrentMsg.mType.equals("DOC"))
        {
            content = getDocumentMessage();
        }
        if (Globals.gCurrentMsg.mType.equals("ORD"))
        {
            content = getOrderMessage();
        }
        if (Globals.gCurrentMsg.mType.equals("OBR"))
        {
            content = getOBRMessage();
        }
        txtContent.setText(content);
    }
    public String getOBRMessage()
    {
        String result = "";
        String datetime = Utils.getFieldFromJson("observation_request_requested_datetime",Globals.gCurrentMsg.mMessageJson);
        String clinical = Utils.getFieldFromJson("observation_request_clinical_information",Globals.gCurrentMsg.mMessageJson);
        String comment = "";
        String comment_type = "";
        try {
            JSONObject noteObj = Globals.gCurrentMsg.mMessageJson.getJSONObject("notes");
            comment = Utils.getFieldFromJson("comment",noteObj);
            comment_type = Utils.getFieldFromJson("comment_type",noteObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        result = result +
                getFormatString("Observation DateTime",datetime) +
                getFormatString("Observation Clinical Information",clinical) +
                getFormatString("Note Comment",comment) +
                getFormatString("Note Comment Type",comment_type);
        return result;
    }
    public String getOrderMessage()
    {
        String result = "";
        String datetime = Utils.getFieldFromJson("observation_request_requested_datetime",Globals.gCurrentMsg.mMessageJson);
        String clinical = Utils.getFieldFromJson("observation_request_clinical_information",Globals.gCurrentMsg.mMessageJson);
        String comment = "";
        String comment_type = "";
        try {
            JSONObject noteObj = Globals.gCurrentMsg.mMessageJson.getJSONObject("notes");
            comment = Utils.getFieldFromJson("comment",noteObj);
            comment_type = Utils.getFieldFromJson("comment_type",noteObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        result = result +
                getFormatString("Observation DateTime",datetime) +
                getFormatString("Observation Clinical Information",clinical) +
                getFormatString("Note Comment",comment) +
                getFormatString("Note Comment Type",comment_type);
        return result;
    }
    public String getContentProcedure()
    {
        String result = "";
        String description = Utils.getFieldFromJson("procedure_description",Globals.gCurrentMsg.mMessageJson);
        String datetime = Utils.getFieldFromJson("procedure_datetime",Globals.gCurrentMsg.mMessageJson);
        String minute = Utils.getFieldFromJson("procedure_minutes",Globals.gCurrentMsg.mMessageJson);
        String anesCode = Utils.getFieldFromJson("anesthesia_code",Globals.gCurrentMsg.mMessageJson);
        String anesMinute = Utils.getFieldFromJson("anesthesia_minutes",Globals.gCurrentMsg.mMessageJson);

        result = result +
                getFormatString("Description",description) +
                getFormatString("DateTime",datetime) +
                getFormatString("Procedure Minute",minute) +
                getFormatString("Anesthesia Code",anesCode) +
                getFormatString("Anesthesia Minute",anesMinute);
        return result;
    }
    public String getBlastMessage()
    {
        try {
            return Globals.gCurrentMsg.mMessageJson.getString("message");
        } catch (JSONException e) {
            return "";
        }
    }
    public String getDocumentMessage()
    {
        String result = "";
        String code = Utils.getFieldFromJson("event_type_code",Globals.gCurrentMsg.mMessageJson);
        String record_time = Utils.getFieldFromJson("event_recorded_datetime",Globals.gCurrentMsg.mMessageJson);

        result = result +
                getFormatString("Event Type Code",code) +
                getFormatString("Event Recorded Datetime",record_time);

        return result;
    }
    public String getContentPObv()
    {
        String result = "";
        String value = Utils.getFieldFromJson("observation_value",Globals.gCurrentMsg.mMessageJson);
        String unit = Utils.getFieldFromJson("observation_units",Globals.gCurrentMsg.mMessageJson);
        String flags = Utils.getFieldFromJson("observation_abnormal_flags",Globals.gCurrentMsg.mMessageJson);
        String status = Utils.getFieldFromJson("observation_result_status",Globals.gCurrentMsg.mMessageJson);
        String method = Utils.getFieldFromJson("observation_method",Globals.gCurrentMsg.mMessageJson);
        String site = Utils.getFieldFromJson("observation_site",Globals.gCurrentMsg.mMessageJson);
        String datetime = Utils.getFieldFromJson("observation_datetime",Globals.gCurrentMsg.mMessageJson);


        result = result +
                getFormatString("DateTime",datetime) +
                getFormatString("Observation Value",value) +
                getFormatString("Observation Units",unit) +
                getFormatString("Observation Flags",flags) +
                getFormatString("Observation Status",status) +
                getFormatString("Observation Method",method) +
                getFormatString("Observation Site",site);

        return result;
    }
    public String getFormatString(String field,String value)
    {
        String result = "";
        if (value.equals("")) return "";
        result = field + ": " + value + "\n";
        return result;
    }
}
