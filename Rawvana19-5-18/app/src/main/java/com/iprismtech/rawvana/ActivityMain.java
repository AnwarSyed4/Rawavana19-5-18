package com.iprismtech.rawvana;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iprismtech.rawvana.database.DataBase;
import com.iprismtech.rawvana.dialog.DialogDelete;
import com.iprismtech.rawvana.fragments.FragAbout;
import com.iprismtech.rawvana.fragments.FragFavorites;
import com.iprismtech.rawvana.fragments.FragMore;
import com.iprismtech.rawvana.fragments.FragReciepe;
import com.iprismtech.rawvana.fragments.FragShoopingList;
import com.iprismtech.rawvana.others.HelperObj;
import com.iprismtech.rawvana.others.MyExceptionHandler;
import com.iprismtech.rawvana.others.Values;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ActivityMain extends FragmentActivity {
    private FragmentManager fragmentManager;
    private Context context;
    private TextView tvHeaderText;
    private LinearLayout llBottomMenu,llRecipeList;
    private ArrayList<Integer> menuImagesList,menuActiveImagesList;
    private ArrayList<String> menuTitleList;
    private Boolean isBackPressed = false,doublePressExit = false;
    private  ImageView ivdown,ivmenu,ivDelete,ivEmail,ivup;
    private String MenuTYPE="1";
    String Message1="";
    private JSONArray jsonArrayDataRecipes=null;
    String OpenOrClose="Close";
    private String Conditions="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();
        HelperObj.getInstance().setActivityMain(this);
        fragmentManager=getSupportFragmentManager();
        //
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, ActivityMain.class));
        //
        tvHeaderText=(TextView)findViewById(R.id.tvHeaderText);
        OpenOrClose="Close";
        setUP();
        onClickevents();
        tvHeaderText.setText(Values.RECIPENAME);
    }

    private void setUP() {
        try {

            llRecipeList=(LinearLayout)findViewById(R.id.llRecipeList);
            llBottomMenu=(LinearLayout)findViewById(R.id.llBottomMenu);
            ivdown=(ImageView)findViewById(R.id.ivdown);
            ivup=(ImageView) findViewById(R.id.ivup);
            ivmenu=(ImageView)findViewById(R.id.ivmenu);
            ivDelete=(ImageView)findViewById(R.id.ivDelete);
            ivEmail=(ImageView)findViewById(R.id.ivEmail);
            menuImagesList = new ArrayList<>();
            menuImagesList.add(R.drawable.recpe);
            menuImagesList.add(R.drawable.shop);
            menuImagesList.add(R.drawable.fav);
            menuImagesList.add(R.drawable.more);
            menuImagesList.add(R.drawable.nfo);
            //
            menuActiveImagesList = new ArrayList<>();
            menuActiveImagesList.add(R.drawable.recpe_b);
            menuActiveImagesList.add(R.drawable.shop_b);
            menuActiveImagesList.add(R.drawable.fav_b);
            menuActiveImagesList.add(R.drawable.more_b);
            menuActiveImagesList.add(R.drawable.nfo_b);
            //
            menuTitleList = new ArrayList<>();
            menuTitleList.add("Recipes");
            menuTitleList.add("Shopping List");
            menuTitleList.add("Favorites");
            menuTitleList.add("More");
            menuTitleList.add("Info");
            //
            createBottomMenu();
            Intent intent=getIntent();
            Conditions=intent.getExtras().getString("Conditions");
            if(Conditions.equalsIgnoreCase("fav")){
                ChangFragment(Values.FragFavorites,null);
            }else {
                ChangFragment(Values.FragRecpipr, null);
            }
            JSONObject jsonObject=new JSONObject(loadJSONFromAsset());
            jsonArrayDataRecipes=jsonObject.optJSONArray("recipelist");
            setList();
            //
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setList(){
        try {
            if(jsonArrayDataRecipes != null && jsonArrayDataRecipes.length() > 0){
                llRecipeList.removeAllViews();
                for(int i = 0;i < jsonArrayDataRecipes.length();i++){
                    llRecipeList.addView(getListItem(i,jsonArrayDataRecipes.getJSONObject(i)));
                }
            } else {
                llRecipeList.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("recipelist.json.txt");
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
            if(Values.RECIPENAME.equalsIgnoreCase(jsonObject.optString("Name"))){
                tvItemName.setText(jsonObject.optString("Name"));
                tvItemName.setTextColor(context.getResources().getColor(R.color.apptheme));
            }else {
                tvItemName.setText(jsonObject.optString("Name"));
            }
            //
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        llRecipeList.setVisibility(View.GONE);
                        ivdown.setVisibility(View.VISIBLE);
                        ivup.setVisibility(View.GONE);
                        Values.RECIPEID=jsonObject.optString("id");
                        Values.RECIPENAME=jsonObject.optString("Name");
                        tvHeaderText.setText(Values.RECIPENAME);
                        setHeader(jsonObject.optString("Name"));
                        ChangFragment(Values.FragRecpipr, null);

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

    private void setHeader(String name) {
        try {
            String Name=name;
            tvHeaderText.setText(name);
        } catch (Exception e) {
         e.printStackTrace();
        }
    }

    private void onClickevents() {

        tvHeaderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Values.isWhichFrag.equals(Values.FragRecpipr) ) {
                    if(OpenOrClose.equalsIgnoreCase("Close")){
                        llRecipeList.setVisibility(View.VISIBLE);
                        ivdown.setVisibility(View.GONE);
                        ivup.setVisibility(View.VISIBLE);
                        OpenOrClose="Open";
                    }
                    else if(OpenOrClose.equalsIgnoreCase("Open")){
                        llRecipeList.setVisibility(View.GONE);
                        ivdown.setVisibility(View.VISIBLE);
                        ivup.setVisibility(View.GONE);
                        OpenOrClose="Close";
                    }


                }
            }
        });
        ivdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OpenOrClose.equalsIgnoreCase("Close")){
                    llRecipeList.setVisibility(View.VISIBLE);
                    ivdown.setVisibility(View.GONE);
                    ivup.setVisibility(View.VISIBLE);
                    OpenOrClose="Open";
                }else if(OpenOrClose.equalsIgnoreCase("Open")){
                    llRecipeList.setVisibility(View.GONE);
                    ivdown.setVisibility(View.VISIBLE);
                    ivup.setVisibility(View.GONE);
                    OpenOrClose="Close";
                }

            }
        });
        ivup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OpenOrClose.equalsIgnoreCase("Close")){
                    llRecipeList.setVisibility(View.VISIBLE);
                    ivdown.setVisibility(View.GONE);
                    ivup.setVisibility(View.VISIBLE);
                    OpenOrClose="Open";
                }else if(OpenOrClose.equalsIgnoreCase("Open")){
                    llRecipeList.setVisibility(View.GONE);
                    ivdown.setVisibility(View.VISIBLE);
                    ivup.setVisibility(View.GONE);
                    OpenOrClose="Close";
                }


            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDelete dialogDelete=new DialogDelete();
                dialogDelete.show(fragmentManager,null);

            }
        });
        ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DataBase dataBase=new DataBase(context);
                    dataBase.open();
                    JSONArray jsonArrayData=new JSONArray();
                    jsonArrayData=dataBase.GetDataCartTableAll();
                    if(jsonArrayData!=null &&jsonArrayData.length()>0){

                            for(int i = 0;i < jsonArrayData.length();i++){
                                Message1 +=jsonArrayData.getJSONObject(i).optString("ProductName")+"\n"+"\n";
                                String msg=jsonArrayData.getJSONObject(i).optString("Ingredients");
                                String[] elements = msg.split("--");
                                ArrayList<String> subListArray = new ArrayList<String>();
                                List<String> itemList = new ArrayList<String>();
                                for (String item : elements) {
                                    subListArray.add(item);
                                }
                                int size=subListArray.size();

                                for (int j = 0; j < subListArray.size(); j++) {
                                    if(j==size-1){
                                        Message1 +=":-  "+  ""+subListArray.get(j)+"\n"+"\n";
                                    }else {
                                        Message1 +=":-  "+  ""+subListArray.get(j)+"\n";
                                    }
                                }
                        }
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rudaykumar39@gmail.com"});
                        intent.putExtra(Intent.EXTRA_SUBJECT, Message1);
                        intent.putExtra(Intent.EXTRA_TEXT, "");
                        intent.setType("message/rfc822");
                        startActivity(Intent.createChooser(intent, "Select Email Sending App :"));

                    }else {
                      HelperObj.getInstance().cusToast(context,"No Shopping List Found");
                    }
                    //dataBase.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        ivmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(MenuTYPE.equalsIgnoreCase("1")){
                        ivmenu.setImageResource(R.drawable.menu1);
                        HelperObj.getInstance().getFragReciepe().setMenu(MenuTYPE);
                        MenuTYPE="2";
                    }else if(MenuTYPE.equalsIgnoreCase("2")){
                        ivmenu.setImageResource(R.drawable.menu);
                        HelperObj.getInstance().getFragReciepe().setMenu(MenuTYPE);
                        MenuTYPE="1";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void createBottomMenu(){
        try {
            llBottomMenu.removeAllViews();
            for (int i = 0; i < menuImagesList.size(); i++) {
                llBottomMenu.addView(getMenuLayout(i));
            }
            /*for (int i = 0; i < menuImagesList.size(); i++) {
                LinearLayout llMenuItem = (LinearLayout) llTopMenu.getChildAt(i);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                llMenuItem.setLayoutParams(param);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("InflateParams")
    public View getMenuLayout(final Integer position){
        ImageView ivItemMenu;
        TextView tvTitle;
        View rowView = null,viewMenu;
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            rowView = inflater.inflate(R.layout.item_top_menu, null);
            ivItemMenu = (ImageView) rowView.findViewById(R.id.ivItemMenu);
            tvTitle = (TextView) rowView.findViewById(R.id.tvTitle);
            viewMenu = (View) rowView.findViewById(R.id.viewMenu);
            //
            tvTitle.setText(menuTitleList.get(position));
            float scale = getResources().getDisplayMetrics().density;
            /*Integer singleItemWidth = (int)(getSingleMenuWidth()*scale);
            Integer ivItemWidth = singleItemWidth-50;
            if(ivItemWidth > 30){
                ivItemWidth = (int)(30*scale);
            }*/
            Integer ivItemWidth = (int)(30*scale);
            ivItemMenu.getLayoutParams().width = ivItemWidth;
            ivItemMenu.getLayoutParams().height = ivItemWidth;
            ivItemMenu.requestLayout();
            //
            ivItemMenu.setImageResource(menuImagesList.get(position));
            viewMenu.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            //
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //setActiveMenu(position);
                    switch (position) {
                        case 0:
                            ChangFragment(Values.FragRecpipr, null);
                            setActiveMenu(0);
                            break;
                        case 1:
                            ChangFragment(Values.FragShopping, null);
                            HelperObj.getInstance().getFragReciepe().closeRecipelist();
                            setActiveMenu(1);
                            break;
                        case 2:
                            ChangFragment(Values.FragFavorites, null);
                            HelperObj.getInstance().getFragReciepe().closeRecipelist();
                            setActiveMenu(2);
                            break;
                        case 3:
                            ChangFragment(Values.FragMore, null);
                            HelperObj.getInstance().getFragReciepe().closeRecipelist();
                            setActiveMenu(3);
                            break;
                        case 4:
                            ChangFragment(Values.FragAbout, null);
                            HelperObj.getInstance().getFragReciepe().closeRecipelist();
                            setActiveMenu(4);
                            break;
                    }
                }
            });
            //
            //LinearLayout ll = (LinearLayout) ivItemMenu.getParent();
            Integer itemWidth = 0;
            Integer singleItemWidth = getSingleMenuWidth();
            if(singleItemWidth < 150){
                itemWidth = 150;
            } else {
                itemWidth = singleItemWidth;
            }
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(itemWidth,LinearLayout.LayoutParams.WRAP_CONTENT);
            rowView.setLayoutParams(llParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowView;
    }
    public void setActiveMenu(Integer position){
        try {
            for (int i = 0; i < menuImagesList.size(); i++) {
                LinearLayout llMenuItem = (LinearLayout) llBottomMenu.getChildAt(i);
                ImageView ivIcon = (ImageView) llMenuItem.getChildAt(0);
                TextView tvTitle = (TextView) llMenuItem.getChildAt(1);
                View viewMenu = (View) llMenuItem.getChildAt(2);
                if(position == i){
                    ivIcon.setImageResource(menuActiveImagesList.get(i));
                    tvTitle.setTextColor(context.getResources().getColor(R.color.apptheme));
                   // viewMenu.setBackgroundColor(context.getResources().getColor(R.color.theme));
                } else {
                    ivIcon.setImageResource(menuImagesList.get(i));
                    tvTitle.setTextColor(context.getResources().getColor(R.color.test));
                    //viewMenu.setBackgroundColor(context.getResources().getColor(R.color.color999));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Integer getSingleMenuWidth(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        return width/5;
    }
    public void ChangFragment(String tag,String[] parameter){
        try {
            try {
                //HelperObj.getInstance().HideKeyboard(viewGroup);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String title = context.getResources().getString(R.string.app_name);
            Fragment fragment = null;
            if(tag.equals(Values.FragRecpipr)){
                fragment = new FragReciepe();
                title = Values.RECIPENAME;
                Values.OpenOrClose="Close";
                setActiveMenu(0);
            } else if(tag.equals(Values.FragShopping)){
                fragment = new FragShoopingList();
                title = "Shopping List";
                Values.OpenOrClose="Close";
                setActiveMenu(1);
            } else if(tag.equals(Values.FragFavorites)){
                fragment = new FragFavorites();
                title = "Favorites";
                Values.OpenOrClose="Close";
                setActiveMenu(2);
            } else if(tag.equals(Values.FragMore)){
                fragment = new FragMore();
                title = "More";
                Values.OpenOrClose="Close";
                setActiveMenu(3);
            } else if(tag.equals(Values.FragAbout)){
                fragment = new FragAbout();
                title = "About";
                Values.OpenOrClose="Close";
                setActiveMenu(4);
            }
//            else if(tag.equals(Values.FragReeipeList)){
//                fragment = new FragReeipeList();
//                title = "Breakfast";
//            }
            if(fragment != null) {
                Bundle bundle = new Bundle();
                bundle.putStringArray("Parameter", parameter);
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment, tag).commit();
                //actionBar.setTitle(title);
                tvHeaderText.setText(title);
                //
                Values.isWhichFrag = tag;
                /*if(tag.equals(Values.FragWowWish) || tag.equals(Values.FragProfile) || tag.equals(Values.FragRedeem) || tag.equals(Values.FragRedeemHistory) || tag.equals(Values.FragServicesHistory) || tag.equals(Values.FragWowWishHistory) || tag.equals(Values.FragAppDesc) || tag.equals(Values.FragPointsDesc)){
                    Values.isWhichFrag = tag;
                }*/

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




//    @Override
//    public void onBackPressed() {
//        if (doublePressExit) {
//            isBackPressed = true;
//            doublePressExit = false;
//            super.onBackPressed();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                finishAffinity();
//                finish();
//            }
//            return;
//        }
//        doublePressExit = true;
//
//        final Toast toast = Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT);
//        toast.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                doublePressExit = false;
//                toast.cancel();
//            }
//        }, 10);
//    }





    @Override
    public void onBackPressed() {
        if (doublePressExit) {
            isBackPressed = true;
            doublePressExit = false;
            super.onBackPressed();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
                finish();
            }
            return;
        }
        doublePressExit = true;
        final Toast toast = Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT);
        toast.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doublePressExit = false;
                toast.cancel();
            }
        }, 1000);
    }

    public void FavoritesList() {
        ChangFragment(Values.FragFavorites, null);
    }
}
