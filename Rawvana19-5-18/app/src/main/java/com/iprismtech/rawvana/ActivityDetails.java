package com.iprismtech.rawvana;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.iprismtech.rawvana.database.DataBase;
import com.iprismtech.rawvana.others.HelperObj;
import com.iprismtech.rawvana.others.Values;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ActivityDetails extends FragmentActivity {
    private Context context;
    private FragmentManager fragmentManager;
    private LinearLayout llListIngredients,llListMethod;
    private ImageView ivBack,ivLike,ivItemImage,ivReview,ivFacebook,ivGmail,ivLikeImage,ivVideoImage;
    private TextView tvItemName,tvRecipeName,tvDescription;
    private Button btnAdd;
    private JSONArray JsonArrayIngredients=null;
    private JSONArray JsonArrayMethods=null;
    private String Id="",whFacebookOrGmail="";
    private SQLiteDatabase sqLiteDatabase;
    String Url="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setUP();
        onClickEvents();
    }
    private void setUP() {
        try {

            context=getApplicationContext();
            fragmentManager=getSupportFragmentManager();
            //
            ivVideoImage=(ImageView)findViewById(R.id.ivVideoImage);
            ivLikeImage=(ImageView)findViewById(R.id.ivLikeImage);
            ivReview=(ImageView)findViewById(R.id.ivReview);
            ivFacebook=(ImageView)findViewById(R.id.ivFacebook);
            ivGmail=(ImageView)findViewById(R.id.ivGmail);
            ivBack=(ImageView)findViewById(R.id.ivBack);
            ivLike=(ImageView)findViewById(R.id.ivLike);
            ivItemImage=(ImageView)findViewById(R.id.ivItemImage);
            tvItemName=(TextView)findViewById(R.id.tvItemName);
            tvRecipeName=(TextView)findViewById(R.id.tvRecipeName);
            tvDescription=(TextView)findViewById(R.id.tvDescription);
            btnAdd=(Button)findViewById(R.id.btnAdd);
            llListIngredients=(LinearLayout) findViewById(R.id.llListIngredients);
            llListMethod=(LinearLayout) findViewById(R.id.llListMethod);
            Intent intent = getIntent();
            try {
                Id=intent.getExtras().getString("id");
                tvItemName.setText(intent.getExtras().getString("Name"));
                String type=intent.getExtras().getString("Type");
                 Url=intent.getExtras().getString("Url");
                if(Url.isEmpty()){
                    ivVideoImage.setVisibility(View.GONE);
                }else {
                    ivVideoImage.setVisibility(View.VISIBLE);
                }
                tvRecipeName.setText(type);
                tvDescription.setText(intent.getExtras().getString("description"));

                String jsonArray = intent.getStringExtra("Ingredients");
                String jsonArray2 = intent.getStringExtra("Method");

                try {
                    JsonArrayIngredients = new JSONArray(jsonArray);
                    JsonArrayMethods= new JSONArray(jsonArray2);
                    setLlListIngredients();
                    setLlListMethods();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String image=intent.getExtras().getString("Image");
                InputStream ims = getResources().getAssets().open(image);
                Drawable d = Drawable.createFromStream(ims, null);
                ivItemImage.setImageDrawable(d);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //
            DataBase dataBase=new DataBase(context);
            dataBase.open();
            Boolean isExist=dataBase.CheckProductExist(Id);
            if(isExist){
                ivLike.setImageResource(R.drawable.fav_yes);
            }else {
                ivLike.setImageResource(R.drawable.like);
            }

            Boolean isExistCart=dataBase.CheckProductExistCart(Id);
            if(isExistCart){
                btnAdd.setText("Remove From Shopping List");
                btnAdd.setBackgroundColor(context.getResources().getColor(R.color.test2));
            }else {
                btnAdd.setText("Add To Shopping List");
                btnAdd.setBackgroundColor(context.getResources().getColor(R.color.test1));
            }
            dataBase.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private void onClickEvents() {
        try {
            ivVideoImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(getApplicationContext(),ActivityYouTube.class);
                    intent.putExtra("URL",Url);
                    startActivity(intent);
                }
            });
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = getIntent();
                        String InArray = intent.getStringExtra("Ingredients");
                        String MethodArray = intent.getStringExtra("Method");
                        String type=intent.getExtras().getString("Type");
                        String Des=intent.getExtras().getString("description");
                        String Url=intent.getExtras().getString("Url");
                        long result=0;
                        DataBase dataBase=new DataBase(context);
                        dataBase.open();
                        Boolean isExist=dataBase.CheckProductExist(Id);
                        if(isExist){
                            dataBase.DeleteFavoriteTableRow(Id);
                            ivLike.setImageResource(R.drawable.like);
                            HelperObj.getInstance().cusToast(context," Unfavorite Successfully" );
                        }else {
                            result = dataBase.Createfavorite(Id,intent.getExtras().getString("Name"),intent.getExtras().getString("Image"),InArray,MethodArray,type,Des,Url);
                            if(result > 0){
                                ivLike.setImageResource(R.drawable.fav_yes);
                                ivLikeImage.setVisibility(View.VISIBLE);
                               // HelperObj.getInstance().cusToast(context," added to favorite");
                                fadeOutAndHideImage(ivLikeImage);
                            } else {
                                HelperObj.getInstance().cusToast(context,"Something went wrong, Try again.");
                            }
                        }
                        dataBase.close();
                    } catch (Exception e) {
                          e.printStackTrace();
                    }

                }
            });

            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Values.isWhichFrag.equals(Values.FragFavorites))
                    {
                        Intent intent =new Intent(getApplicationContext(),ActivityMain.class);
                        intent.putExtra("Conditions","fav");
                        startActivity(intent);

                    }else {
                        onBackPressed();
                    }

                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    long result=0;
                    DataBase dataBase=new DataBase(context);
                    dataBase.open();
                    Boolean isExistCart=dataBase.CheckProductExistCart(Id);
                    if(isExistCart){
                        dataBase.DeleteCartTableRow(Id);
                        btnAdd.setText("Add To Shopping List");
                        btnAdd.setBackgroundColor(context.getResources().getColor(R.color.test1));
                    }else {
                        String Ingredients=getSelectedItems1();
                        if(Ingredients.isEmpty()){
                            HelperObj.getInstance().cusToast(context,"Something went wrong, Try again.");
                        }else {
                            result = dataBase.CartTable(Id,intent.getExtras().getString("Name"),Ingredients);
                            if(result > 0){
                                HelperObj.getInstance().cusToast(context," added to Shopping List");
                                btnAdd.setText("Remove From Shopping List");
                                btnAdd.setBackgroundColor(context.getResources().getColor(R.color.test2));
                            } else {
                                HelperObj.getInstance().cusToast(context,"Something went wrong, Try again.");
                            }
                        }
                    }
                    dataBase.close();
                }

            });
            ivReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.ranchosoftware.rawvana"));
                    startActivity(i);
                }
            });
            ivFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = getIntent();
                        String url = "file:///android_asset/"+intent.getExtras().getString("Image");
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("image/jpg");
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT,intent.getExtras().getString("Name"));
                        emailIntent.putExtra(Intent.EXTRA_TEXT, intent.getExtras().getString("description"));
                        emailIntent.putExtra(Intent.EXTRA_STREAM, url);
                        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            ivGmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = getIntent();
                        String Ingredients="\n"+"\n"+"Ingredients"+"\n"+"\n";
                        String Method="\n"+"\n"+"Method"+"\n"+"\n";

                        for(int i=0;i<JsonArrayIngredients.length();i++){
                            Ingredients +=":-  "+  ""+JsonArrayIngredients.getJSONObject(i).optString("steps")+"\n";
                        }
                        for(int i=0;i<JsonArrayMethods.length();i++){
                            Method +=":-  "+  ""+JsonArrayMethods.getJSONObject(i).optString("steps")+"\n";
                        }
                        String TotalDescription=intent.getExtras().getString("description")+Ingredients+Method;
                        String url = "file:///android_asset/"+intent.getExtras().getString("Image");

                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("image/jpg");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT,intent.getExtras().getString("Name"));
                        emailIntent.putExtra(Intent.EXTRA_TEXT, TotalDescription);

                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setLlListIngredients(){
        try {
            llListIngredients.removeAllViews();
            if(JsonArrayIngredients != null && JsonArrayIngredients.length() > 0){
                for(int i = 0;i < JsonArrayIngredients.length();i++){
                    llListIngredients.addView(getListItemIngredients(i,JsonArrayIngredients.getJSONObject(i)));
                }
                llListIngredients.setVisibility(View.VISIBLE);

            } else {
                llListIngredients.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint({"InflateParams", "SetTextI18n"})
    public View getListItemIngredients(final Integer position, final JSONObject jsonObject){
        View rowView = null;
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            rowView = inflater.inflate(R.layout.item_ingredients_list, null);
            //
            TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
            tvName.setText(jsonObject.optString("steps"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowView;
    }
    public void setLlListMethods(){
        try {
            llListMethod.removeAllViews();
            if(JsonArrayMethods != null && JsonArrayMethods.length() > 0){
                for(int i = 0;i < JsonArrayMethods.length();i++){
                    llListMethod.addView(getListItemMethods(i,JsonArrayMethods.getJSONObject(i)));
                }
                llListMethod.setVisibility(View.VISIBLE);
            } else {
                llListMethod.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getSelectedItems1() {

        String selectedItemsStrings = "";
        try {
            for (int i = 0; i < JsonArrayIngredients.length(); i++) {
                    selectedItemsStrings += JsonArrayIngredients.getJSONObject(i).optString("steps") + "--";
            }
            selectedItemsStrings = selectedItemsStrings.substring(0, selectedItemsStrings.length() - 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedItemsStrings;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    public View getListItemMethods(final Integer position, final JSONObject jsonObject){
        View rowView = null;
        int i=0;
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            rowView = inflater.inflate(R.layout.item_method_list, null);
            //
            i=position+1;
            TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
            TextView tvStepNo= (TextView) rowView.findViewById(R.id.tvStepNo);
            tvName.setText(jsonObject.optString("steps"));
            tvStepNo.setText("Step"+i);
            //

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowView;
    }
    @Override
    public void onBackPressed() {
        if(Values.isWhichFrag.equals(Values.FragFavorites))
        {
            Intent intent =new Intent(getApplicationContext(),ActivityMain.class);
            intent.putExtra("Conditions","fav");
            startActivity(intent);
        }else {
            super.onBackPressed();
            finish();
        }
    }
    private void fadeOutAndHideImage(final ImageView img)
    {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(900);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                img.setVisibility(View.GONE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        img.startAnimation(fadeOut);
    }

    }

