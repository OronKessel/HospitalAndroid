package com.songu.pom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.songu.pom.R;
import com.songu.pom.model.UserModel;

import java.util.List;

public class AdapterBlast extends BaseAdapter {//Adapter for Blast Message ListView

    List<UserModel> lstItems;
    public int sel = -1;
    public Context mContext;

    public AdapterBlast(Context con) {
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
        localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_blast_contact, null);

        if (localViewHolder == null) {
            localViewHolder = new ViewHolder();
            localViewHolder.txtBlastEmail = ((TextView) localView.findViewById(R.id.txtItemBlastContact));
            localView.setTag(localViewHolder);
        }
        final UserModel hItem = lstItems.get(paramInt);
        localViewHolder.txtBlastEmail.setText(hItem.mEmail);
        return localView;
    }
    public void update(List<UserModel> paramList) {
        this.lstItems = paramList;
        this.notifyDataSetChanged();
    }

    public class ViewHolder {
        public TextView txtBlastEmail;
    }

}
