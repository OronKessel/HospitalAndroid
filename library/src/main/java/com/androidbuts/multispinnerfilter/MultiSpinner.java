package com.androidbuts.multispinnerfilter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MultiSpinner extends AppCompatSpinner implements OnMultiChoiceClickListener, OnCancelListener {

    private List<String> items;
    private boolean[] selected;
    private String defaultText = "Select Items";
    private String spinnerTitle = "";
    private MultiSpinnerListener listener;
    private AlertDialog.Builder builder;
    private AlertDialog mDialog;

    public MultiSpinner(Context context) {
        super(context);
    }

    public void setDefaultText(String text)
    {
        this.defaultText = text;
    }
    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        TypedArray a = arg0.obtainStyledAttributes(arg1, R.styleable.MultiSpinnerSearch);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.MultiSpinnerSearch_hintText) {
                spinnerTitle = a.getString(attr);
            }
        }
        a.recycle();
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

        if (which == 0) //Select All
        {
            for (int i = 0;i < selected.length;i++)
            {
                selected[i] = isChecked;
                mDialog.getListView().setItemChecked(i,isChecked);
            }
        }
        else {
            selected[0] = true;
            mDialog.getListView().setItemChecked(0,true);
            selected[which] = isChecked;
            mDialog.getListView().setItemChecked(which,isChecked);
            for (int i = 0;i < selected.length;i++)
            {
                if (!selected[i]) {
                    selected[0] = false;
                    mDialog.getListView().setItemChecked(0, false);
                }
            }

        }


    }

    public void clearSelection()
    {
        for (int i = 0;i < selected.length;i++)
        {
            selected[i] = false;
        }
        listener.onItemsSelected(selected);
    }
    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuilder spinnerBuffer = new StringBuilder();
        String spinnerText = "";
        if (items.size() > 0 && selected[0])
        {
            spinnerText = items.get(0);
        }
        else {
            for (int i = 0; i < items.size(); i++) {
                if (selected[i]) {
                    spinnerBuffer.append(items.get(i));
                    spinnerBuffer.append(", ");
                }
            }
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2) {
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
            } else {
                spinnerText = defaultText;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.textview_for_spinner, new String[]{spinnerText});
        setAdapter(adapter);
        if (selected.length > 0) {
            listener.onItemsSelected(selected);
        }

    }

    @Override
    public boolean performClick() {
        builder = new AlertDialog.Builder(getContext(), R.style.myDialog);
        builder.setTitle(spinnerTitle);

        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        mDialog = builder.show();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("Item Selected","Alert Dialog");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return true;
    }

    /**
     * Sets items to this spinner.
     *
     * @param items    A TreeMap where the keys are the values to display in the spinner
     *                 and the value the initial selected state of the key.
     * @param listener A MultiSpinnerListener.
     */
    public void setItems(LinkedHashMap<String, Boolean> items, MultiSpinnerListener listener) {
        this.items = new ArrayList<>(items.keySet());
        this.listener = listener;

        List<Boolean> values = new ArrayList<>(items.values());
        selected = new boolean[values.size()];
        for (int i = 0; i < items.size(); i++) {
            selected[i] = values.get(i);
        }

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.textview_for_spinner, new String[]{defaultText});
        setAdapter(adapter);

        // Set Spinner Text
        onCancel(null);
    }

}
