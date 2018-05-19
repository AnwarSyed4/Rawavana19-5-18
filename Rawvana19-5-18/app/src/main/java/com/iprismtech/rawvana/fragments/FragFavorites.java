package com.iprismtech.rawvana.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.iprismtech.rawvana.ActivityDetails;
import com.iprismtech.rawvana.R;
import com.iprismtech.rawvana.database.DataBase;
import com.iprismtech.rawvana.others.HelperObj;
import com.iprismtech.rawvana.others.Values;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragFavorites extends Fragment {
    private Context context;
    private FragmentManager fragmentManager;
    private RelativeLayout Challenge;
    private LinearLayout NoList;
    private LinearLayout llList;
    private ScrollView svList;
    private JSONArray jsonArrayData=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_frag_favorites, container, false);
        HelperObj.getInstance().setFragFavorites(this);
        context= getActivity();
        fragmentManager=getFragmentManager();
        try {
            setUp(view);
            onClickEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    private void setUp(View view) {
        llList=(LinearLayout)view.findViewById(R.id. llList);
        svList=(ScrollView)view.findViewById(R.id. svList);
        NoList=(LinearLayout)view.findViewById(R.id. NoList);
        jsonArrayData=null;
        CallDatabase();
    }

    public void CallDatabase() {
        try {
            DataBase dataBase=new DataBase(context);
            dataBase.open();
            jsonArrayData=dataBase.GetDataFavoriteTableAll();
            if(jsonArrayData!=null &&jsonArrayData.length()>0){
                llList.setVisibility(View.VISIBLE);
                NoList.setVisibility(View.GONE);
                setList();
            }else {
                llList.setVisibility(View.GONE);
                NoList.setVisibility(View.VISIBLE);
            }
            //dataBase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void onClickEvents() {

    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    public View getFavoriteListItem(final Integer position, final JSONObject jsonObject){
        final TextView tvItemName;
        ImageView ivImage;
        View rowView = null;
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            rowView = inflater.inflate(R.layout.item_favoriterecipe_list, null);
            tvItemName = (TextView) rowView.findViewById(R.id.tvItemName);
            ivImage = (ImageView) rowView.findViewById(R.id.ivImage);

            String ItemName=jsonObject.optString("ProductName");
            String ImageName=jsonObject.optString("Imagepath");
            tvItemName.setText(ItemName);
            //
            try {
                InputStream ims = getResources().getAssets().open(ImageName);
                Drawable d = Drawable.createFromStream(ims, null);
                ivImage.setImageDrawable(d);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent=new Intent(context,ActivityDetails.class);
                        intent.putExtra("Image",jsonObject.optString("Imagepath"));
                        intent.putExtra("Name",jsonObject.optString("ProductName"));
                        intent.putExtra("id",jsonObject.optString("ProductID"));
                        intent.putExtra("Type",jsonObject.optString("Type"));
                        intent.putExtra("description",jsonObject.optString("Des"));
                        intent.putExtra("Method",jsonObject.optString("MethodArray"));
                        intent.putExtra("Ingredients",jsonObject.optString("InArray"));
                        intent.putExtra("Url",jsonObject.optString("Url"));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
            llList.setVisibility(View.VISIBLE);
            llList.removeAllViews();
            if(jsonArrayData != null && jsonArrayData.length() > 0){
                for(int i = 0;i < jsonArrayData.length();i++){
                    llList.addView(getFavoriteListItem(i,jsonArrayData.getJSONObject(i)));
                }
            } else {
                llList.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SetFavorites() {
        jsonArrayData=null;
        CallDatabase();
    }
}
