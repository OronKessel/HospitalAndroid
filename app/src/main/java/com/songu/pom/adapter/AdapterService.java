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

import java.util.List;

public class AdapterService extends BaseAdapter {

    List<ServiceModel> lstItems;
    public int sel = -1;
    public Context mContext;

    public AdapterService(Context con) {
        super();
        mContext = con;
    }

    public int getCount() {
        if (this.lstItems == null)
            return 0;
        return this.lstItems.size();
    }

    public ServiceModel getItem(int paramInt) {
        return this.lstItems.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return 0L;
    }

    public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {
        View localView = paramView;
        ViewHolder localViewHolder = null;
        localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_patient_service, null);

        if (localViewHolder == null) {
            localViewHolder = new ViewHolder();
            localViewHolder.imgCheck = ((ImageView) localView.findViewById(R.id.img_item_service));
            localViewHolder.txtName = ((TextView) localView.findViewById(R.id.txt_item_service));
            localView.setTag(localViewHolder);
        }
        final ServiceModel hItem = lstItems.get(paramInt);
        localViewHolder.txtName.setText(hItem.mName);
        if (!hItem.isSelect)
        {
            localViewHolder.imgCheck.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_tick_2));
        }
        else
            localViewHolder.imgCheck.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_tick_1));
        return localView;
    }
    public void update(List<ServiceModel> paramList) {
        this.lstItems = paramList;
        this.notifyDataSetChanged();
    }

    public class ViewHolder {
        public ImageView imgCheck;
        public TextView txtName;
    }

}
