package com.shaktipumplimted.serviceapp.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.shaktipumplimted.serviceapp.R;


public class CustomProgressDialog extends ProgressDialog {
    private TextView progressMessage;
    private final String mMessage;


    public CustomProgressDialog(Context context) {
        super(context, R.style.CustomAlertDialogStyle);
        this.mMessage = context.getResources().getString(R.string.Loading);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custom_progress_dialog);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }
}


