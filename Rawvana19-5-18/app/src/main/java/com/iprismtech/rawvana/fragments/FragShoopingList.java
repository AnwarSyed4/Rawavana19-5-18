package com.iprismtech.rawvana.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.iprismtech.rawvana.R;
import com.iprismtech.rawvana.database.DataBase;
import com.iprismtech.rawvana.dialog.DialogDelete;
import com.iprismtech.rawvana.others.HelperObj;
import com.iprismtech.rawvana.others.Values;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragShoopingList extends Fragment {
    private Context context;
    private FragmentManager fragmentManager;
    private RelativeLayout Challenge;
    private LinearLayout NoList;
    private LinearLayout llList;
    private ScrollView svList;
    private JSONArray jsonArrayData=null;
    private ImageView ivDelete,ivEmail;
    private Button btnAdd;
    String Message1="";
    private EditText etSearch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.frag_shooping_list, container, false);
        HelperObj.getInstance().setFragShoopingList(this);
        context= getActivity();
        fragmentManager=getFragmentManager();
        try {
            setUp(view);
            onClickEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after) { }
            @Override
            public void onTextChanged(final CharSequence s, int start,int before, int count) {
                try {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String searchText = etSearch.getText().toString();
                                if(searchText.length() > 0){

                                    setSearchList(searchText);
                                } else {
                                    resetSearchList();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    try {
                        String searchText = etSearch.getText().toString();
                        if(searchText.length() > 0){
                            setSearchList(searchText);
                        } else {
                            HelperObj.getInstance().cusToast(context,"Enter search text");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        return view;
    }
    private void setUp(View view) {
        etSearch=(EditText)view.findViewById(R.id.etSearch);
        llList=(LinearLayout)view.findViewById(R.id. llList);
        svList=(ScrollView)view.findViewById(R.id. svList);
        NoList=(LinearLayout)view.findViewById(R.id. NoList);
        ivDelete=(ImageView)view.findViewById(R.id.ivDelete);
        ivEmail=(ImageView)view.findViewById(R.id.ivEmail);
        btnAdd=(Button) view.findViewById(R.id.btnAdd);
        jsonArrayData=null;
        CallDatabase();

    }

    private void CallDatabase() {
        try {
            DataBase dataBase=new DataBase(context);
            dataBase.open();
            jsonArrayData=dataBase.GetDataCartTableAll();
            if(jsonArrayData!=null &&jsonArrayData.length()>0){
                llList.setVisibility(View.VISIBLE);
                NoList.setVisibility(View.GONE);
                setList(jsonArrayData);
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
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDelete dialogDelete=new DialogDelete();
                dialogDelete.show(fragmentManager,null);

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperObj.getInstance().getActivityMain().ChangFragment(Values.FragRecpipr,null);
            }
        });
        ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Message1="";
                    DataBase dataBase=new DataBase(context);
                    dataBase.open();
                    Message1="";
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
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
                        intent.putExtra(Intent.EXTRA_SUBJECT, Message1);
                        intent.putExtra(Intent.EXTRA_TEXT, "");
                        intent.setType("message/rfc822");
                        intent.setPackage("com.google.android.gm");
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

    }
    @SuppressLint({"InflateParams", "SetTextI18n"})
    public View getShoppingListItem(final Integer position, final JSONObject jsonObject){
        final TextView tvItemName,tvAll;
        ImageView ivImage;
        View rowView = null;
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            rowView = inflater.inflate(R.layout.item_shopping_list, null);
            tvItemName = (TextView) rowView.findViewById(R.id.tvTitle);
            tvAll = (TextView) rowView.findViewById(R.id.tvAll);
            final LinearLayout llSubList=(LinearLayout)rowView.findViewById(R.id.llSubList);
            //
            String ItemName=jsonObject.optString("ProductName");
            tvItemName.setText(ItemName);
            String Ingredients=jsonObject.optString("Ingredients");
            String[] elements = Ingredients.split("--");
            final ArrayList<String> subListArray = new ArrayList<String>();
            List<String> itemList = new ArrayList<String>();
            for (String item : elements) {
                subListArray.add(item);
            }
            setList1(subListArray,llSubList);
            tvAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value = tvAll.getText().toString();
                    if(value.equalsIgnoreCase("All")){
                        SelectAll(subListArray, llSubList, true);
                        tvAll.setText("Un Select");
                    } else {
                        SelectAll(subListArray, llSubList, false);
                        tvAll.setText("All");
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowView;
    }

    private void SelectAll(ArrayList<String> list, LinearLayout llSubList, boolean isAll) {
        try {
            for (int i = 0; i < list.size(); i++) {
                LinearLayout llParent = (LinearLayout) llSubList.getChildAt(i);
                LinearLayout llChild = (LinearLayout) llParent.getChildAt(0);
                CheckBox checkBox = (CheckBox) llChild.getChildAt(0);
                if(isAll){
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setList1(ArrayList subListArray,LinearLayout llSubList) {
        try {
            if (subListArray != null && subListArray.size() > 0) {
                llSubList.removeAllViews();
                for (int i = 0; i < subListArray.size(); i++) {
                    llSubList.addView(getSUblistIngredients(i, subListArray));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint({"InflateParams", "SetTextI18n"})
    public View getSUblistIngredients(final Integer position, final ArrayList indexArrayList) {
        final TextView textView;
        View rowView = null;
        final CheckBox ivCheckbox;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            rowView = inflater.inflate(R.layout.side_sublist_ingredients, null);
            textView = (TextView) rowView.findViewById(R.id.tvItemName);
            textView.setText("" + indexArrayList.get(position));
            ivCheckbox=(CheckBox) rowView.findViewById(R.id.ivCheckbox);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowView;
    }
    public void setList(JSONArray jsonArrayData){
        try {
            llList.setVisibility(View.VISIBLE);
            llList.removeAllViews();
            if(jsonArrayData != null && jsonArrayData.length() > 0){
                for(int i = 0;i < jsonArrayData.length();i++){
                    llList.addView(getShoppingListItem(i,jsonArrayData.getJSONObject(i)));
                }
            } else {
                llList.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setSearchList(String searchText){
        try {
            if(jsonArrayData != null && jsonArrayData.length() > 0){
                JSONArray jsonArray = new JSONArray();
                for(int i = 0;i < jsonArrayData.length();i++){
                    String Recipename =  jsonArrayData.getJSONObject(i).optString("ProductName").toLowerCase();
                    searchText = searchText.toLowerCase();
                    if(Recipename.contains(searchText)){
                        llList.setVisibility(View.VISIBLE);
                        jsonArray.put(jsonArrayData.getJSONObject(i));
                    }
                }
                if(jsonArray.isNull(0)){
                    llList.setVisibility(View.GONE);
                    NoList.setVisibility(View.VISIBLE);
                    HelperObj.getInstance().cusToast(context,"No Records Are Found");
                    setList(jsonArray);
                }else{
                    llList.setVisibility(View.VISIBLE);
                    //HelperObj.getInstance().HideKeyboard(getView());
                    setList(jsonArray);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void resetSearchList(){
        llList.setVisibility(View.VISIBLE);
        NoList.setVisibility(View.GONE);
        setList(jsonArrayData);
    }

    public void setData() {
        CallDatabase();
    }
}
