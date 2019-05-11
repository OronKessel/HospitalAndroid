package com.songu.pom.fragment.patient;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.songu.pom.R;
import com.songu.pom.activity.MainActivity;
import com.songu.pom.adapter.AdapterInvite;
import com.songu.pom.doc.Enums;
import com.songu.pom.doc.Globals;
import com.songu.pom.model.UserModel;
import com.songu.pom.service.IServiceResult;
import com.songu.pom.util.loader.ACProgressFlower;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class InviteFragment extends Fragment implements View.OnClickListener,IServiceResult{

    public View mRootView = null;
    public LinearLayout layoutContacts;
    public Button btnContact,btnManual,btnDone;
    public List<InviteViewHolder> listHolders = new ArrayList<>();
    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(//Pattern for Email
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_patient_invite, null);
        }
        initView();
        addContactView(null);
        return mRootView;
    }
//    public void setData()
//    {
//        if (Globals.g_inviteList.size() > 0)
//        {
//
//        }
//    }

    public void initView()
    {
        Globals.g_pendingInvite = null;
        Globals.g_inviteList1.clear();
        listHolders.clear();
        layoutContacts = (LinearLayout)mRootView.findViewById(R.id.layoutContacts);
        btnContact = (Button) mRootView.findViewById(R.id.btnInviteAddress);
        btnManual = (Button)mRootView.findViewById(R.id.btnInviteNew);
        btnDone = (Button) mRootView.findViewById(R.id.btnInviteDone);

        btnContact.setOnClickListener(this);
        btnManual.setOnClickListener(this);
        btnDone.setOnClickListener(this);
    }
    public void addContactView(final UserModel userModel)
    {
        View inviteView = LayoutInflater.from(this.getContext()).inflate(R.layout.layout_invite_member, null);
        final InviteViewHolder iHolder = new InviteViewHolder();
        iHolder.layoutConfirm = inviteView.findViewById(R.id.layoutInviteConfirm);
        iHolder.imgTick = inviteView.findViewById(R.id.imgInviteConfirm);
        iHolder.editFirst = inviteView.findViewById(R.id.editInviteFirstName);
        iHolder.editLast = inviteView.findViewById(R.id.editInviteLastName);
        iHolder.editEmail = inviteView.findViewById(R.id.editInviteEmail);
        iHolder.editPhone = inviteView.findViewById(R.id.editInvitePhone);
        iHolder.layoutConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHolder.isChecked = !iHolder.isChecked;
                if (iHolder.isChecked)
                {
                    iHolder.imgTick.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick_1));
                }
                else
                {
                    iHolder.imgTick.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick_2));
                }
            }
        });
        layoutContacts.addView(inviteView);
        listHolders.add(iHolder);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10)
        {
            if (contactPicked(data)) {
                //addContactView(Globals.g_pendingInvite);
                listHolders.get(listHolders.size() - 1).editEmail.setText(Globals.g_pendingInvite.mEmail);
                listHolders.get(listHolders.size() - 1).editPhone.setText(Globals.g_pendingInvite.mPhone);
                listHolders.get(listHolders.size() - 1).editFirst.setText(Globals.g_pendingInvite.mFirstName);
                listHolders.get(listHolders.size() - 1).editLast.setText(Globals.g_pendingInvite.mLastName);
            }
        }
    }

    private boolean contactPicked(Intent data) {//When select contact from list
        if (data == null || data.getData() == null) return false;
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            Uri uri = data.getData();
            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID));
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            String email = "";
            String rawSort, rawSelect, dataSort, dataSelect;
            String[] rawProj, dataProj, dataArgs;
            rawProj = new String[]{ContactsContract.RawContacts._ID, ContactsContract.RawContacts.ACCOUNT_NAME, ContactsContract.RawContacts.ACCOUNT_TYPE};
            rawSort = ContactsContract.RawContacts._ID + " ASC";
            rawSelect = ContactsContract.RawContacts._ID + "=?";


            Cursor rawC = getActivity().getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, rawProj, rawSelect, new String[]{String.valueOf(id)}, rawSort);
            while(rawC.moveToNext()) {
                dataProj = new String[]{ContactsContract.Data._ID, ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1,ContactsContract.Contacts.DISPLAY_NAME};
                dataSelect = ContactsContract.Data.RAW_CONTACT_ID + "=? and " + ContactsContract.Data.DATA1 + "!=?";
                dataArgs = new String[]{String.valueOf(rawC.getInt(0)), "null"};
                dataSort = ContactsContract.Data._ID + " ASC";
                Cursor dataC =getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, dataProj, dataSelect, dataArgs, dataSort);
                while(dataC.moveToNext()) {
                    String type = dataC.getString(1);
                    String dt = dataC.getString(2);
                    if(type.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                        //String dt = dataC.getString(2);
                        email = dt;
                    }
                }
            }
            String firstName = "";
            String lastName = "";
            if (name.indexOf(" ",-1) != -1)
            {
                firstName = name.substring(0, name.indexOf(" ", -1));
                lastName = name.substring(name.indexOf(" ", -1)).trim();
            }
            if (firstName.equals(""))
                firstName = name;
            UserModel uModel = new UserModel();
            uModel.mPhone = phoneNo;
            uModel.mID = email;
            uModel.mName = name;
            uModel.mLastName = lastName;
            uModel.mFirstName = firstName;
            uModel.mEmail = email;
            Globals.g_pendingInvite = uModel;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void actionDone() //When click done button
    {
        for (int i = 0;i < listHolders.size();i++)
        {
            if (listHolders.get(i).isChecked)
            {
                UserModel uModel = new UserModel();
                String strFirstname = listHolders.get(i).editFirst.getText().toString();
                String strLastname = listHolders.get(i).editLast.getText().toString();
                //String strPhone = listHolders.get(i).editPhone.getText().toString();
                String strEmail = listHolders.get(i).editEmail.getText().toString();
                if (strFirstname.equals(""))
                {
                    ((MainActivity)getActivity()).dlgError.setMessage("Please enter firstname");
                    ((MainActivity)getActivity()).dlgError.show();
                    return;
                }
                if (strLastname.equals(""))
                {
                    ((MainActivity)getActivity()).dlgError.setMessage("Please enter lastname");
                    ((MainActivity)getActivity()).dlgError.show();
                    return;
                }
//                if (strPhone.equals(""))
//                {
//                    ((MainActivity)getActivity()).dlgError.setMessage("Please input phone number");
//                    ((MainActivity)getActivity()).dlgError.show();
//                    return;
//                }
                if (strEmail.equals(""))
                {
                    ((MainActivity)getActivity()).dlgError.setMessage("Please enter email address");
                    ((MainActivity)getActivity()).dlgError.show();
                    return;
                }

                if(!checkEmail(strEmail))
                {
//                if (!strEmail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    ((MainActivity)getActivity()).dlgError.setMessage("Invalid Email Address");
                    ((MainActivity)getActivity()).dlgError.show();
                    return;
                }

                uModel.mFirstName = strFirstname;
                uModel.mLastName = strLastname;
                //uModel.mPhone = strPhone;
                uModel.mEmail = strEmail;
                Globals.g_inviteList1.add(uModel);
            }
            else
            {
                ((MainActivity)getActivity()).dlgError.setMessage("Please make sure all information is correct before proceeding");
                ((MainActivity)getActivity()).dlgError.show();
                return;
            }
        }
        if (Globals.g_inviteList1.size() > 0) {
            Globals.e_mode = Enums.MODE.PATIENT_SELECT_SERVICE;
            Globals.e_transDirection = Enums.SWIPDIRECTION.LEFT;
            ((MainActivity) getActivity()).setFragment();
        }
    }
    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnInviteAddress:
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 10);
                break;
            case R.id.btnInviteDone:
                actionDone();
                break;
            case R.id.btnInviteNew:
                addContactView(null);
                break;
        }
    }

    @Override
    public void onResponse(int code) {
        switch (code)
        {

        }
    }

    class InviteViewHolder
    {
        public EditText editFirst;
        public EditText editLast;
        public EditText editEmail;
        public EditText editPhone;
        public ImageView imgTick;
        public LinearLayout layoutConfirm;
        public boolean isChecked = false;
    }
}
