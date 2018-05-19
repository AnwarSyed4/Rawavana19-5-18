package com.iprismtech.rawvana.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iprismtech.rawvana.R;
import com.iprismtech.rawvana.others.HelperObj;
import com.iprismtech.rawvana.others.Values;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragReeipeList extends Fragment {

    private FragmentManager fragmentManager;
    private Context context;
    private LinearLayout llList;
    private JSONArray jsonArrayData=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.frag_reeipe_list, container, false);
        context =getActivity();
        fragmentManager=getFragmentManager();
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        try {
            llList=(LinearLayout)view.findViewById(R.id.llList);
            JSONObject jsonObject=new JSONObject(loadJSONFromAsset());
            jsonArrayData=jsonObject.optJSONArray("recipelist");
            setList();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("recipelist.json.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }



    @SuppressLint({"InflateParams", "SetTextI18n"})
    public View getListItem(final Integer position, final JSONObject jsonObject){
        final TextView tvItemName;
        ImageView ivImage;
        View rowView = null;
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            rowView = inflater.inflate(R.layout.item_recipe_totla_list, null);
            tvItemName = (TextView) rowView.findViewById(R.id.tvItemName);
            tvItemName.setText(jsonObject.optString("Name"));
            //
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            //
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowView;
    }
    public void setList(){
        try {
            if(jsonArrayData != null && jsonArrayData.length() > 0){
                llList.removeAllViews();
                for(int i = 0;i < jsonArrayData.length();i++){
                    llList.addView(getListItem(i,jsonArrayData.getJSONObject(i)));
                }
            } else {
                llList.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
