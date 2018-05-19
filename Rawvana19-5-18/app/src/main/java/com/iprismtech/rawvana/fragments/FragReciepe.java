package com.iprismtech.rawvana.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iprismtech.rawvana.ActivityDetails;
import com.iprismtech.rawvana.ActivityMain;
import com.iprismtech.rawvana.R;
import com.iprismtech.rawvana.others.HelperObj;
import com.iprismtech.rawvana.others.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;


public class FragReciepe extends Fragment {

    private FragmentManager fragmentManager;
    private Context context;
    private LinearLayout llList,llList2,llRecipeList,llHeader;
    private JSONArray jsonArrayData=null,jsonArrayDataRecipes=null;
    private AssetManager assetManager;
    private InputStream is;
    private Bitmap bitmap;
    private JSONObject MainJSon=null;
    private ImageView ivdown,ivup,ivmenu;
    private TextView tvHeaderText;
    private String MenuTYPE="1";
    private ProgressDialog pDialog;
    private ScrollView svList1,svList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.frag_reciepe, container, false);
        context =getActivity();
        Values.OpenOrClose="Close";
        fragmentManager=getFragmentManager();
        HelperObj.getInstance().setFragReciepe(this);
        //HelperObj.getInstance().getActivityMain().setHeader();
        setUp(view);
        OnclickeEvents();
        pDialog = new ProgressDialog(getActivity());
        return view;
    }

    private void setUp(View view) {
        try {
            ivdown=(ImageView)view.findViewById(R.id.ivdown);
            ivup=(ImageView)view. findViewById(R.id.ivup);
            ivmenu=(ImageView)view.findViewById(R.id.ivmenu);
            llList=(LinearLayout)view.findViewById(R.id.llList);
            llList2=(LinearLayout)view.findViewById(R.id.llList2);
            llRecipeList=(LinearLayout)view.findViewById(R.id.llRecipeList);
            tvHeaderText=(TextView)view.findViewById(R.id.tvHeaderText);
            llHeader=(LinearLayout)view.findViewById(R.id.llHeader) ;
            svList1=(ScrollView)view .findViewById(R.id.svList1);
            svList=(ScrollView)view .findViewById(R.id.svList);
            //
            CalJson();
            tvHeaderText.setText(Values.RECIPENAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void FooterAnimation() {
        Animation hide = AnimationUtils.loadAnimation(context, R.anim.bootom_top);
        tvHeaderText.startAnimation(hide);
    }

    public void headerAnimation() {
        Animation hide = AnimationUtils.loadAnimation(context, R.anim.top_bottom);
        svList1.startAnimation(hide);
    }

    private void CalJson() {
        try {
            assetManager = getActivity().getAssets();
            MainJSon=new JSONObject(loadJSONFromAsset());
            jsonArrayData=MainJSon.optJSONArray("array");
            JSONObject jsonObject=new JSONObject(loadJSONFromAsset1());
            jsonArrayDataRecipes=jsonObject.optJSONArray("recipelist");
            setList();
            setList2();
            setListRecipe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void OnclickeEvents() {
        ivmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(MenuTYPE.equalsIgnoreCase("1")){
                        ivmenu.setImageResource(R.drawable.menu1);
                        setMenu(MenuTYPE);
                        svList1.setVisibility(View.GONE);
                        MenuTYPE="2";
                    }else if(MenuTYPE.equalsIgnoreCase("2")){
                        ivmenu.setImageResource(R.drawable.menu);
                        setMenu(MenuTYPE);
                        svList1.setVisibility(View.GONE);
                        MenuTYPE="1";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        llHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Values.isWhichFrag.equals(Values.FragRecpipr) ) {
                    if(Values.OpenOrClose.equalsIgnoreCase("Close")){
                        svList1.setVisibility(View.VISIBLE);
                        ivdown.setVisibility(View.GONE);
                        ivmenu.setVisibility(View.INVISIBLE);
                        ivup.setVisibility(View.VISIBLE);
                        Values.OpenOrClose="Open";
                        //headerAnimation();
                        HelperObj.getInstance().SlideToDownAnimation(svList1,400);
                    }
                    else if(Values.OpenOrClose.equalsIgnoreCase("Open")){
                        HelperObj.getInstance().SlideToAboveAnimation(svList1,400);
                        svList1.setVisibility(View.GONE);
                        ivdown.setVisibility(View.VISIBLE);
                        ivup.setVisibility(View.GONE);
                        ivmenu.setVisibility(View.VISIBLE);
                        Values.OpenOrClose="Close";
                       // FooterAnimation();
                    }


                }
            }
        });


    }
    public void setListRecipe(){
        try {
            if(jsonArrayDataRecipes != null && jsonArrayDataRecipes.length() > 0){
                llRecipeList.removeAllViews();
                for(int i = 0;i < jsonArrayDataRecipes.length();i++){
                    llRecipeList.addView(getListItemRecipe(i,jsonArrayDataRecipes.getJSONObject(i)));
                }
            } else {
                llRecipeList.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String loadJSONFromAsset1() {
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
    public View getListItemRecipe(final Integer position, final JSONObject jsonObject){
        final TextView tvItemName;
        ImageView ivImage;
        View rowView = null;
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            rowView = inflater.inflate(R.layout.item_recipe_totla_list, null);
            tvItemName = (TextView) rowView.findViewById(R.id.tvItemName);
            tvItemName.setText(jsonObject.optString("Name"));
            if(Values.RECIPENAME.equalsIgnoreCase(jsonObject.optString("Name"))){
                tvItemName.setTextColor(context.getResources().getColor(R.color.apptheme));
            }else {

            }
            //
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        //HelperObj.getInstance().SlideToAboveAnimation(svList1,1000);
                        svList1.setVisibility(View.GONE);
                        ivdown.setVisibility(View.VISIBLE);
                        ivup.setVisibility(View.GONE);
                        ivmenu.setVisibility(View.VISIBLE);
                        Values.OpenOrClose="Close";
                        Values.RECIPEID=jsonObject.optString("id");
                        Values.RECIPENAME=jsonObject.optString("Name");
                        tvHeaderText.setText(Values.RECIPENAME);
                        CalJson();
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
    private void setList2() {
        try {
            llList2.removeAllViews();
            if(jsonArrayData != null && jsonArrayData.length() > 0){
                llList2.addView(getListItem(jsonArrayData));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            if(Values.RECIPEID.equalsIgnoreCase("1")){
                is = getActivity().getAssets().open("breakfast.txt");
            }else if(Values.RECIPEID.equalsIgnoreCase("2")){
                is = getActivity().getAssets().open("desserts.txt");
            }else if(Values.RECIPEID.equalsIgnoreCase("3")){
                is = getActivity().getAssets().open("entress.txt");
            }else if(Values.RECIPEID.equalsIgnoreCase("4")){
                is = getActivity().getAssets().open("juices.txt");
            }else if(Values.RECIPEID.equalsIgnoreCase("5")){
                is = getActivity().getAssets().open("salads.txt");
            }else if(Values.RECIPEID.equalsIgnoreCase("6")){
                is = getActivity().getAssets().open("saucesdips.txt");
            }else if(Values.RECIPEID.equalsIgnoreCase("7")){
                is = getActivity().getAssets().open("smoothies.txt");
            }else if(Values.RECIPEID.equalsIgnoreCase("8")){
                is = getActivity().getAssets().open("soups.txt");
            }

//            if(Values.RECIPEID.equalsIgnoreCase("1")){
//                is = getActivity().getAssets().open("breakfast.txt");
//            }else if(Values.RECIPEID.equalsIgnoreCase("2")){
//                is = getActivity().getAssets().open("desserts.txt");
//            }else if(Values.RECIPEID.equalsIgnoreCase("3")){
//                is = getActivity().getAssets().open("entress.txt");
//            }else if(Values.RECIPEID.equalsIgnoreCase("4")){
//                is = getActivity().getAssets().open("juices.txt");
//            }else if(Values.RECIPEID.equalsIgnoreCase("5")){
//                is = getActivity().getAssets().open("salads.txt");
//            }else if(Values.RECIPEID.equalsIgnoreCase("6")){
//                is = getActivity().getAssets().open("saucesdps.txt");
//            }else if(Values.RECIPEID.equalsIgnoreCase("7")){
//                is = getActivity().getAssets().open("smoothies.txt");
//            }else if(Values.RECIPEID.equalsIgnoreCase("8")){
//                is = getActivity().getAssets().open("soups.txt");
//            }

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return json;
    }
    public View getListItem(JSONArray jsonArray){
        LinearLayout llParent = null;
        try {
            float scale = getResources().getDisplayMetrics().density;
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = displaymetrics.widthPixels;
            int height = displaymetrics.heightPixels;
            int singleWidth = (width/2)-10;
            //
            llParent = new LinearLayout(context);
            LinearLayout.LayoutParams llParentParams;
            llParentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            //llParentParams.setMargins(5, 5, 5, 5);
            llParent.setLayoutParams(llParentParams);
            llParent.setOrientation(LinearLayout.VERTICAL);
            //
            Integer count = jsonArray.length();
            LinearLayout llRow = null;
            for (int i = 0; i < count; i++) {
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                if ((i%2) == 0) {
                    llRow = new LinearLayout(context);
                    LinearLayout.LayoutParams llRowParams;
                    llRowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    //llParentParams.setMargins(5, 5, 5, 5);
                    llRow.setLayoutParams(llRowParams);
                    llRow.setOrientation(LinearLayout.HORIZONTAL);
                }
                //
                LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.item_category, null);
                rowView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
                TextView tvTitle = (TextView) rowView.findViewById(R.id.tvTitle);
                ImageView ivIcon = (ImageView) rowView.findViewById(R.id.ivIcon);
                RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(singleWidth,(singleWidth+120));
                ivIcon.setLayoutParams(ivParams);
                //
               
                tvTitle.setText(jsonObject.optString("Name"));
                String url = "file:///android_asset/"+jsonObject.optString("image")+".jpg";
                HelperObj.getInstance().setGlideFullURLImage(context,ivIcon,url,false);
//                String imageURL = jsonObject.optString("image");
//
//                try {
//                    InputStream ims = getResources().getAssets().open(jsonObject.optString("image")+".jpg");
//                    Drawable d = Drawable.createFromStream(ims, null);
//                    ivIcon.setImageDrawable(d);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
                //
                View view = new View(context);
                LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
                //viewParams.setMargins(5, 5, 5, 5);
                view.setLayoutParams(viewParams);
                final Integer index = i;
                //
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Values.OpenOrClose.equalsIgnoreCase("Open")){

                        }else{
                            Intent intent=new Intent(context,ActivityDetails.class);
                            intent.putExtra("Image",jsonObject.optString("image")+".jpg");
                            intent.putExtra("Name",jsonObject.optString("Name"));
                            intent.putExtra("id",jsonObject.optString("id"));
                            intent.putExtra("Type",MainJSon.optString("Type"));
                            intent.putExtra("description",jsonObject.optString("description"));
                            intent.putExtra("Method",jsonObject.optJSONArray("Method").toString());
                            intent.putExtra("Ingredients",jsonObject.optJSONArray("Ingredients").toString());
                            intent.putExtra("Url",jsonObject.optString("YoutubeURL"));
                            startActivity(intent);
                        }

                    }
                });
                //
                if(llRow != null){
                    if (HelperObj.getInstance().IsEvenNumber(i)) {
                        llParent.addView(llRow);
                        llRow.addView(rowView);
                    } else {
                        llRow.addView(rowView);
                    }
                    if(HelperObj.getInstance().IsEvenNumber(i) && i==(count-1)){
                        llRow.addView(view);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return llParent;
    }



    @SuppressLint({"InflateParams", "SetTextI18n"})
    public View getListItem(final Integer position, final JSONObject jsonObject){
        final TextView tvItemName;
        ImageView ivImage;
        View rowView = null;
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            rowView = inflater.inflate(R.layout.item_recipe_list, null);
            tvItemName = (TextView) rowView.findViewById(R.id.tvItemName);
            ivImage = (ImageView) rowView.findViewById(R.id.ivImage);
            //
            tvItemName.setText(jsonObject.optString("Name"));
            //
            String url = "file:///android_asset/"+jsonObject.optString("image")+".jpg";
            HelperObj.getInstance().setGlideFullURLImage(context,ivImage,url,false);
//            try {
//                InputStream ims = getResources().getAssets().open(jsonObject.optString("image")+".jpg");
//                Drawable d = Drawable.createFromStream(ims, null);
//                ivImage.setImageDrawable(d);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if(Values.OpenOrClose.equalsIgnoreCase("Open")){

                        }else{
                            Intent intent=new Intent(context,ActivityDetails.class);
                            intent.putExtra("Image",jsonObject.optString("image")+".jpg");
                            intent.putExtra("Name",jsonObject.optString("Name"));
                            intent.putExtra("id",jsonObject.optString("id"));
                            intent.putExtra("Type",MainJSon.optString("Type"));
                            intent.putExtra("description",jsonObject.optString("description"));
                            intent.putExtra("Method",jsonObject.optJSONArray("Method").toString());
                            intent.putExtra("Ingredients",jsonObject.optJSONArray("Ingredients").toString());
                            intent.putExtra("Url",jsonObject.optString("YoutubeURL"));
                            startActivity(intent);
                        }

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
//            llList.addView(getListItem(jsonArrayData));

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

    public void setMenu(String menuTYPE) {
        if(menuTYPE.equalsIgnoreCase("1")){
            llList.setVisibility(View.GONE);
            llList2.setVisibility(View.VISIBLE);
        }else if(menuTYPE.equalsIgnoreCase("2")){
            llList.setVisibility(View.VISIBLE);
            llList2.setVisibility(View.GONE);
        }
    }
    public void closeLoader(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    llRecipeList.setVisibility(View.GONE);
                    ivdown.setVisibility(View.VISIBLE);
                    ivup.setVisibility(View.GONE);
                    tvHeaderText.setText(Values.RECIPENAME);
                    pDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1500);

    }
    public  void closeRecipelist(){

        svList1.setVisibility(View.GONE);
        ivdown.setVisibility(View.VISIBLE);
        ivup.setVisibility(View.GONE);

    }



}

