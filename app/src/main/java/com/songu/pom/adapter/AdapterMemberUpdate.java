package com.songu.pom.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.songu.pom.R;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.model.MessageModel;
import com.songu.pom.model.UserModel;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterMemberUpdate extends BaseAdapter {

    List<MessageModel> lstItems;
    public int sel = -1;
    public Context mContext;

    public AdapterMemberUpdate(Context con) {
        super();
        mContext = con;
    }

    public int getCount() {
        if (this.lstItems == null)
            return 0;
        return this.lstItems.size();
    }

    public MessageModel getItem(int paramInt) {
        return this.lstItems.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return 0L;
    }

    public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {
        View localView = paramView;
        ViewHolder localViewHolder = null;
        localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_member_update, null);

        if (localViewHolder == null) {
            localViewHolder = new ViewHolder();
            localViewHolder.txtUpdate = ((TextView) localView.findViewById(R.id.txtUpdateInfo));
            localViewHolder.txtDate = ((TextView) localView.findViewById(R.id.txtDateInfo));
            localView.setTag(localViewHolder);
        }
        final MessageModel hItem = lstItems.get(paramInt);
        String content = "";
        if (!hItem.mTimestamp.equals("")) {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            double timestamp = Double.parseDouble(hItem.mTimestamp);
            long ts = (long) (timestamp * 1000);
            cal.setTimeInMillis(ts);
            String date = DateFormat.format("EEEE MMM. dd", cal).toString();
            String time = DateFormat.format("hh:mm aa", cal).toString();
            content = time + " ";
            localViewHolder.txtDate.setText(date);
            if (paramInt > 0)
            {
                Calendar calOld = Calendar.getInstance(Locale.ENGLISH);
                if (!lstItems.get(paramInt - 1).mTimestamp.equals("")) {
                    double timestampOld = Double.parseDouble(lstItems.get(paramInt - 1).mTimestamp);
                    long tsOld = (long) (timestampOld * 1000);
                    calOld.setTimeInMillis(tsOld);
                    String dateOld = DateFormat.format("EEEE MMM. dd", calOld).toString();
                    if (date.equals(dateOld))
                    {
                        localViewHolder.txtDate.setVisibility(View.GONE);
                    }
                    else
                    {
                        localViewHolder.txtDate.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    localViewHolder.txtDate.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                localViewHolder.txtDate.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            localViewHolder.txtDate.setVisibility(View.GONE);
        }

        String name = "";
        if (hItem.mFirstName != null && hItem.mFirstName.length() > 0)
        {
            name = name + hItem.mFirstName.substring(0,1).toUpperCase() + ".";
        }

        if (hItem.mLastName != null && hItem.mLastName.length() > 0)
        {
            name = name + hItem.mLastName.substring(0,1).toUpperCase();
        }

        if (hItem.mType.equals("PR") && Globals.e_userType == Enums.USERTYPE.USER) //Procedure
        {
            content = content + "Procedure update for " + name;
        }
        else if (hItem.mType.equals("PR") && Globals.e_userType == Enums.USERTYPE.PATIENT) //Procedure
        {
            content = content + "Procedure update";
        }
        else if (hItem.mType.equals("POBV") && Globals.e_userType == Enums.USERTYPE.USER) //PatientObservation
        {
            content = content +  "Patient Observation update for " + name;
        }
        else if (hItem.mType.equals("POBV") && Globals.e_userType == Enums.USERTYPE.PATIENT) //PatientObservation
        {
            content = content +  "Patient Observation update";
        }
        else if (hItem.mType.equals("BLT") && Globals.e_userType == Enums.USERTYPE.USER)
        {
            content = content + "You have a direct message from " + name;
        }
        else if (hItem.mType.equals("BLT") && Globals.e_userType == Enums.USERTYPE.PATIENT)
        {
            content = content + "You sent a direct message";
        }
        else if (hItem.mType.equals("DOC") && Globals.e_userType == Enums.USERTYPE.USER)
        {
            content = content +  "Document update for " + name;
        }
        else if (hItem.mType.equals("DOC") && Globals.e_userType == Enums.USERTYPE.PATIENT)
        {
            content = content +  "Document update";
        }
        else if (hItem.mType.equals("OBR")  && Globals.e_userType == Enums.USERTYPE.USER)
        {
            content = content +  "Observation update for " + name;
        }
        else if (hItem.mType.equals("OBR")  && Globals.e_userType == Enums.USERTYPE.PATIENT)
        {
            content = content +  "Observation update";
        }
        else if (hItem.mType.equals("ORD") && Globals.e_userType == Enums.USERTYPE.USER)
        {
            content = content + "Orders update for " + name;
        }
        else if (hItem.mType.equals("ORD") && Globals.e_userType == Enums.USERTYPE.PATIENT)
        {
            content = content + "Orders update";
        }

        localViewHolder.txtUpdate.setText(content);//"2:30PM - Update f
        // or S.A regarding medication");
        return localView;
    }
    public void update(List<MessageModel> paramList) {
        this.lstItems = paramList;
        this.notifyDataSetChanged();
    }

    public class ViewHolder {
        public TextView txtUpdate;
        public TextView txtDate;
    }
}
