package com.iprismtech.rawvana.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.iprismtech.rawvana.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragMore extends Fragment {
    private Context context;
    private FragmentManager fragmentManager;
    private RelativeLayout Challenge;
    private RelativeLayout RawvanaRenwal;
    private Integer screenWidth = 0;
    private Integer screenHeight = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.frag_more, container, false);
        context= getActivity();
        fragmentManager=getFragmentManager();

        try {
            setUp(view);
            onClickEvents();
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            screenWidth = displaymetrics.widthPixels;
            screenHeight = displaymetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    private void setUp(View view) {
        Challenge=(RelativeLayout)view.findViewById(R.id.RlList1);
        RawvanaRenwal=(RelativeLayout)view.findViewById(R.id.RlList2);
    }
    private void onClickEvents() {
        Challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.touchzenmedia.rawvanasmoothies&hl=en"));
                startActivity(i);
            }
        });
        RawvanaRenwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.rawvana.com/ebooks"));
                startActivity(i);
            }
        });
    }

}
