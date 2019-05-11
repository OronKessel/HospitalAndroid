package com.songu.pom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.songu.pom.R;
import com.songu.pom.model.UserModel;

import java.util.List;

public class AdapterMemberInvite extends BaseAdapter {//Adapter for Invite Member ListView

    List<UserModel> lstItems;
    public int sel = -1;
    public Context mContext;

    public AdapterMemberInvite(Context con) {
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
        localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_member_invite, null);

        if (localViewHolder == null) {
            localViewHolder = new ViewHolder();
            localViewHolder.txtInfo = ((TextView) localView.findViewById(R.id.txtInviteInfo));
            localViewHolder.imgInfo = (ImageView) localView.findViewById(R.id.imgInviteInfo);
            localView.setTag(localViewHolder);
        }
        final UserModel hItem = lstItems.get(paramInt);
        localViewHolder.txtInfo.setText("Invitation from " + hItem.mFirstName + " " +  hItem.mLastName + " - Unique Code " + hItem.mCode);
        if (hItem.mActive.equals("1"))
        {
            localViewHolder.imgInfo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ok));
        }
        else
        {
            localViewHolder.imgInfo.setImageDrawable(null);
        }
        return localView;
    }
    public void update(List<UserModel> paramList) {
        this.lstItems = paramList;
        this.notifyDataSetChanged();
    }

    public class ViewHolder {
        public TextView txtInfo;
        public ImageView imgInfo;
    }
}

