package com.songu.pom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.songu.pom.R;
import com.songu.pom.model.ServiceModel;
import com.songu.pom.model.UserModel;

import java.util.List;

public class AdapterInvite extends BaseAdapter {//Adapter for Invite ListView

    List<UserModel> lstItems;
    public int sel = -1;
    public Context mContext;

    public AdapterInvite(Context con) {
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
        localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_patient_contact, null);

        if (localViewHolder == null) {
            localViewHolder = new ViewHolder();
            localViewHolder.txtEmail = ((TextView) localView.findViewById(R.id.txt_item_contact_email));
            localViewHolder.txtPhone = ((TextView) localView.findViewById(R.id.txt_item_contact_phone));
            localView.setTag(localViewHolder);
        }
        final UserModel hItem = lstItems.get(paramInt);
        localViewHolder.txtEmail.setText(hItem.mEmail);
        localViewHolder.txtPhone.setText(hItem.mPhone);
        return localView;
    }
    public void update(List<UserModel> paramList) {
        this.lstItems = paramList;
        this.notifyDataSetChanged();
    }

    public class ViewHolder {
        public TextView txtEmail;
        public TextView txtPhone;
    }

}
