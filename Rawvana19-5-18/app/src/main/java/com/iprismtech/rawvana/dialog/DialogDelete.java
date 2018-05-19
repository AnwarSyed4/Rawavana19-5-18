package com.iprismtech.rawvana.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.iprismtech.rawvana.R;
import com.iprismtech.rawvana.database.DataBase;
import com.iprismtech.rawvana.others.HelperObj;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 7/11/2017.
 */

public class DialogDelete extends DialogFragment {
    Context context;
    String message = "";
    FragmentManager fragmentManager;
    ImageView img1,img2,img3,img4,img5,img11,img21,img31,img41,img51;
    EditText etRateUs;
    Button btnRateus;
    String RateUs="";
    String ServiceRateUs="";

    //---empty constructor required
    public DialogDelete() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.dialog_delete, container);
        context=getActivity();
        fragmentManager=getFragmentManager();

        Button btnRemove=(Button)view.findViewById(R.id.btnRemove);
        Button btnclose=(Button)view.findViewById(R.id.btnclose);

        //

        //
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DataBase dataBase=new DataBase(context);
                    dataBase.open();
                    dataBase.deleteAll();
                    dismiss();
                    HelperObj.getInstance().getFragShoopingList().setData();
                    dataBase.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().setCancelable(true);
        return view;
    }


}
