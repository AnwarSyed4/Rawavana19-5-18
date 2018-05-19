package com.iprismtech.rawvana.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.iprismtech.rawvana.R;
import com.iprismtech.rawvana.database.NetworkCheck;
import com.iprismtech.rawvana.others.HelperObj;
import com.iprismtech.rawvana.others.Values;


/**
 * Created by iPrism on 7/2/2016.
 */
public class DialogNetworkError extends DialogFragment {
    Context context;
    Button btnOK;
    FragmentManager fragmentManager;
    //---empty constructor required
    public DialogNetworkError() {
    }
    public void setDialogTitle(Context ctx) {
        context = ctx;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.dialog_network_error, container);
        context=getActivity();
        fragmentManager=getFragmentManager();
        //
        btnOK = (Button) view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkCheck networkCheck=new NetworkCheck();
                if(networkCheck.isConnected(context)){
                    if(Values.UserID>0){
                        dismiss();
                    }
                }else {
                    DialogNetworkError networkErrorDialog = new DialogNetworkError();
                    networkErrorDialog.setDialogTitle(context);
                    networkErrorDialog.show(fragmentManager, null);
                    HelperObj.getInstance().cusToast(context,"No Internet Connection");
                    dismiss();
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
