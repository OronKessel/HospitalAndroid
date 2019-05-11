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

public class AdapterPatient extends BaseAdapter {

    List<UserModel> lstItems;
    public int sel = -1;
    public Context mContext;

    public AdapterPatient(Context con) {
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
        localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_patient_select, null);

        if (localViewHolder == null) {
            localViewHolder = new ViewHolder();
            localViewHolder.txtName = ((TextView) localView.findViewById(R.id.txtSelectPatientName));
            localView.setTag(localViewHolder);
        }
        final UserModel hItem = lstItems.get(paramInt);
        localViewHolder.txtName.setText(hItem.mFirstName + " " + hItem.mLastName);
        if (hItem.isPatientSelect)
        {
            //localViewHolder.txtName.setBackground();
            localViewHolder.txtName.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.drawable_btn_option2));
        }
        else
        {
            localViewHolder.txtName.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.drawable_btn_option1));
        }
        return localView;
    }
    public void update(List<UserModel> paramList) {
        this.lstItems = paramList;
        this.notifyDataSetChanged();
    }

    public class ViewHolder {
        public TextView txtName;
    }

}

