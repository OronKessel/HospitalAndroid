package com.songu.pom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.songu.pom.R;
import com.songu.pom.fragment.patient.SelectServiceFragment;
import com.songu.pom.model.UserModel;

import java.util.HashMap;
import java.util.List;

public class AdapterInviteExp extends BaseExpandableListAdapter {

    private Context _context;
    private List<UserModel> _listDataHeader;
    private String[] listDataChild = {"Select All","Patient Status (Bed, in OR, Recovering)","Orders (Labs, Imaging, Consultation)","Observations (Pre and Post Operation)","Documents/Notes (Physician Notes)"};

    public AdapterInviteExp(Context context, List<UserModel> listDataHeader) {
        this._context = context;
        this._listDataHeader = listDataHeader;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild[childPosititon];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_patient_service, null);
        }


        final ServiceItemViewHolder itemHolder = new ServiceItemViewHolder();
        itemHolder.layoutService = convertView.findViewById(R.id.layout_item_service);
        itemHolder.imgService = convertView.findViewById(R.id.img_item_service);
        itemHolder.txtServiceName = convertView.findViewById(R.id.txt_item_service);
        itemHolder.txtServiceName.setText(listDataChild[childPosition]);
        itemHolder.imgService.setImageDrawable(_context.getResources().getDrawable(R.drawable.ic_tick_2));
        if (childPosition == 0) //Select All
        {
            if (this._listDataHeader.get(groupPosition).isSelectAll)
            {
                itemHolder.imgService.setImageDrawable(_context.getResources().getDrawable(R.drawable.ic_tick_1));
            }
        }
        else {
            if ((this._listDataHeader.get(groupPosition).mServices.get(listDataChild[childPosition]) != null) && (this._listDataHeader.get(groupPosition).mServices.get(listDataChild[childPosition]).isSelect)) {
                itemHolder.imgService.setImageDrawable(_context.getResources().getDrawable(R.drawable.ic_tick_1));
            }
        }

        //final int index = i;
//        itemHolder.layoutService.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sHolder.serviceItems.get(index).isChecked = !sHolder.serviceItems.get(index).isChecked;
//                if (sHolder.serviceItems.get(index).isChecked)
//                {
//                    sHolder.serviceItems.get(index).imgService.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick_1));
//                }
//                else {
//                    sHolder.isSelectAll = false;
//                    sHolder.serviceItems.get(index).imgService.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick_2));
//                }
//                //Check All checked
//                boolean isSelectAll = true;
//                for (int i = 0;i < sHolder.serviceItems.size();i++)
//                {
//                    if (!sHolder.serviceItems.get(i).isChecked) isSelectAll = false;
//                }
//                sHolder.isSelectAll = isSelectAll;
//                if (sHolder.isSelectAll)
//                {
//                    sHolder.imgSelectAll.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick_1));
//                }
//                else {
//                    sHolder.imgSelectAll.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick_2));
//                }
//
//            }
//        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.length;
    }

    @Override
    public UserModel getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_service_header, null);
        }

        ServiceViewHolder holder = new ServiceViewHolder();
        holder.txtServiceMember = (TextView) convertView
                .findViewById(R.id.txt_service_item_member);
        holder.imgExpand = (ImageView) convertView.findViewById(R.id.img_service_item_member_expand);

        if (_listDataHeader.get(groupPosition).isExpand) {
            holder.imgExpand.setImageDrawable(_context.getResources().getDrawable(R.drawable.ic_down));
        } else {
            holder.imgExpand.setImageDrawable(_context.getResources().getDrawable(R.drawable.ic_right));
        }
        //holder.txtServiceMember.setText(_listDataHeader.get(groupPosition).mEmail);
        holder.txtServiceMember.setText(_listDataHeader.get(groupPosition).mFirstName + " " + _listDataHeader.get(groupPosition).mLastName);
        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class ServiceItemViewHolder
    {
        public LinearLayout layoutService;
        public ImageView imgService;
        public TextView txtServiceName;
        public boolean isChecked = true;
    }

    public class ServiceViewHolder
    {
        public TextView txtServiceMember;
        public ImageView imgExpand;
        public List<ServiceItemViewHolder> serviceItems;
        public boolean isSelectAll = true;
    }

}
