package com.iprismtech.rawvana.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.iprismtech.rawvana.R;
import com.iprismtech.rawvana.others.HelperObj;


/**
 * Created by DEVELOPER on 12/12/2016.
 */
public class DialogLoader extends DialogFragment {
    Context context;
    String message = "";
    FragmentManager fragmentManager;
    //---empty constructor required
    public DialogLoader() {
    }
    public void setDialogTitle(Context ctx, String msg) {
        context = ctx;
        message = msg;
        if(message == null){
            message = "";
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loader, container);
        TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        ImageView ivLoader = (ImageView) view.findViewById(R.id.ivLoader);
        tvMessage.setText(message);
       // HelperObj.getInstance().setGlideDrawableImage(context,ivLoader,R.drawable.loader_gif,false);
//
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().setCancelable(true);
        return view;
    }
}