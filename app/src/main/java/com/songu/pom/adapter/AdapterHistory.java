package com.songu.pom.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.songu.pom.R;
import com.songu.pom.model.UserModel;

import java.util.Calendar;
import java.util.List;

public class AdapterHistory extends BaseAdapter {//Adapter for History ListView

    List<UserModel> lstItems;
    public int sel = -1;
    public Context mContext;

    public AdapterHistory(Context con) {
        super();
        mContext = con;
    }

    public int getCount() {
        if (this.lstItems == null)
            return 0;
        return this.lstItems.size();
    }

    public UserModel getItem(int paramInt) {
        return this.lstItems.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return 0L;
    }

    public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {
        View localView = paramView;
        ViewHolder localViewHolder = null;
        localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_visit_history, null);

        if (localViewHolder == null) {
            localViewHolder = new ViewHolder();
            localViewHolder.txtHistory = ((TextView) localView.findViewById(R.id.txtVisitHistoryItem));
            localView.setTag(localViewHolder);
        }
        final UserModel hItem = lstItems.get(paramInt);
        //localViewHolder.txtHistory.setText(hItem);
        String date = "";
        if (!hItem.mInvitedTimestamp.equals(""))
        {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(hItem.mInvitedTimestamp) * 1000);
            date = DateFormat.format("yyyy-MM-dd hh:mm", cal).toString();
        }
        localViewHolder.txtHistory.setText(date + " " + hItem.mEmail + " invited");
        return localView;
    }
    public void update(List<UserModel> paramList) {
        this.lstItems = paramList;
        this.notifyDataSetChanged();
    }

    public class ViewHolder {
        public TextView txtHistory;
    }
}
