package com.androidproject.iottechhrm.util;

import android.app.Dialog;
import android.content.Context;

import com.androidproject.iottechhrm.R;


public class MyProgresbar extends Dialog {

    public MyProgresbar(Context context) {
        super(context);
    }

    public MyProgresbar(Context context, int theme) {
        super(context, theme);
    }

    public static MyProgresbar show(Context context , int theme) {
       MyProgresbar dialog = new MyProgresbar(context, theme);
        dialog.setTitle("");
//        dialog.setContentView(R.layout.myprogress);
        dialog.setContentView(R.layout.myprogress2);
        dialog.setCancelable(false);
        try {
            dialog.show();
        }
        catch (Exception e)
        {}
        return dialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

}
