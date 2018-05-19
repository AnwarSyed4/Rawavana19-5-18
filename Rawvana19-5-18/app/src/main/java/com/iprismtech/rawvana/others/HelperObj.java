package com.iprismtech.rawvana.others;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.iprismtech.rawvana.ActivityMain;
import com.iprismtech.rawvana.ActivitySplash;
import com.iprismtech.rawvana.R;
import com.iprismtech.rawvana.database.WebService;
import com.iprismtech.rawvana.dialog.DialogLoader;
import com.iprismtech.rawvana.dialog.DialogNetworkError;
import com.iprismtech.rawvana.fragments.FragFavorites;
import com.iprismtech.rawvana.fragments.FragReciepe;
import com.iprismtech.rawvana.fragments.FragShoopingList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;



public class HelperObj {
    private Toast toast;
    public static String DatePickerValue = "",TimePickerValue = "",isWhichWindowRegister = "Login",isWhichActivity = "",whichWebServiceRequest = "";
    public static Boolean isAsyncTaskComplete = true;
    private DialogLoader dialogLoader = null;
    public static DialogNetworkError dialogNetworkError = null;
    public static Integer requestTime = 30000;
    public static String locationNotFound = "location not found";
    private Integer loopGetLocation = 0;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    AlertDialog.Builder alertBuilder = null;


    private HelperObj() {
    }
    private static HelperObj objInstance;
    public static HelperObj getInstance() {
        if (objInstance == null) {
            objInstance = new HelperObj();
        }
        return objInstance;
    }
    //
    private ActivitySplash activitySplash;
    public ActivitySplash getActivitySplash() {
        return activitySplash;
    }
    public void setActivitySplash(ActivitySplash activitySplash) {
        this.activitySplash = activitySplash;
    }
    //
    private ActivityMain activityMain;
    public ActivityMain getActivityMain() {
        return activityMain;
    }
    public void setActivityMain(ActivityMain activityMain) {
        this.activityMain = activityMain;
    }
    //

    //
    private FragReciepe fragReciepe;
    public FragReciepe getFragReciepe() {
        return fragReciepe;
    }
    public void setFragReciepe(FragReciepe fragReciepe) {
        this.fragReciepe = fragReciepe;
    }
    //
    private FragShoopingList fragShoopingList;
    public FragShoopingList getFragShoopingList() {
        return fragShoopingList;
    }
    public void setFragShoopingList(FragShoopingList fragShoopingList) {
        this.fragShoopingList = fragShoopingList;
    }
    private FragFavorites fragFavorites;
    public FragFavorites getFragFavorites() {
        return fragFavorites;
    }
    public void setFragFavorites(FragFavorites fragShoopingList) {
        this.fragFavorites = fragFavorites;
    }
    //

    //

    //
    public void TestHandler(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    //
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }
    public void WSPostDataJsonRequest(final Context context, final FragmentManager fragmentManager, final UpdateWSResponse updateListener, final String methodName, final Map<String, String> params, String requestType, String loaderMessage) {
        if(requestType == null){
            requestType = "";
        }
        Loader(context, fragmentManager, true, loaderMessage);
        final String finalRequestType = requestType;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebService.ServerUrl + methodName, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response != null && response.length() > 0){
                                updateListener.updateWSResponse(response, finalRequestType);
                            } else {
                                HelperObj.getInstance().cusToast(context,"response data not found!!!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Loader(context, fragmentManager, false, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        showVolleyError(context, fragmentManager, error);
                    }
                });
        jsonObjectRequest.setTag(methodName);
        setJsonRequestQueue(context,jsonObjectRequest,requestType);
    }
    public void WSPostDataJsonRequestNoLoader(final Context context, final FragmentManager fragmentManager, final UpdateWSResponse updateListener, final String methodName, final Map<String, String> params, String requestType, String loaderMessage) {
        if(requestType == null){
            requestType = "";
        }
        //Loader(context, fragmentManager, true, loaderMessage);
        final String finalRequestType = requestType;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebService.ServerUrl + methodName, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response != null && response.length() > 0){
                                updateListener.updateWSResponse(response, finalRequestType);
                            } else {
                                cusToast(context,"response data not found!!!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Loader(context, fragmentManager, false, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        showVolleyError(context, fragmentManager, error);
                    }
                });
        jsonObjectRequest.setTag(methodName);
        setJsonRequestQueue(context,jsonObjectRequest,requestType);
    }
    public void WSGetDataJsonRequest(final Context context, final FragmentManager fragmentManager, final UpdateWSResponse updateListener, final String methodName, final Map<String, String> params, String requestType, String loaderMessage) {
        if(requestType == null){
            requestType = "";
        }
        Loader(context, fragmentManager, true, loaderMessage);
        final String finalRequestType = requestType;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebService.ServerUrl + methodName, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response != null && response.length() > 0){
                                updateListener.updateWSResponse(response, finalRequestType);
                            } else {
                                HelperObj.getInstance().cusToast(context,"response data not found!!!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Loader(context, fragmentManager, false, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if(HelperObj.isWhichActivity.equals("ActivityLocationChange")){
                            Loader(context,fragmentManager,false,null);
                            //HelperObj.getInstance().getActivityLocationChange().reloadData();
                        } else {
                            showVolleyError(context, fragmentManager, error);
                        }
                    }
                });
        jsonObjectRequest.setTag(methodName);
        setJsonRequestQueue(context,jsonObjectRequest,requestType);
    }
    public void WSGetDataJsonRequestNoLoader(final Context context, final FragmentManager fragmentManager, final UpdateWSResponse updateListener, final String methodName, final Map<String, String> params, String requestType, String loaderMessage) {
        if(requestType == null){
            requestType = "";
        }
        //Loader(context, fragmentManager, true, loaderMessage);
        final String finalRequestType = requestType;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebService.ServerUrl + methodName, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response != null && response.length() > 0){
                                updateListener.updateWSResponse(response, finalRequestType);
                            } else {
                                cusToast(context,"response data not found!!!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Loader(context, fragmentManager, false, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        showVolleyError(context, fragmentManager, error);
                    }
                });
        jsonObjectRequest.setTag(methodName);
        setJsonRequestQueue(context,jsonObjectRequest,requestType);
    }
    public void WSGetDataJsonRequestNoLoaderFullURL(final Context context, final FragmentManager fragmentManager, final UpdateWSResponse updateListener, final String methodName, final Map<String, String> params, String requestType, String loaderMessage) {
        if(requestType == null){
            requestType = "";
        }
        //Loader(context, fragmentManager, true, loaderMessage);
        final String finalRequestType = requestType;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,methodName, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response != null && response.length() > 0){
                                updateListener.updateWSResponse(response, finalRequestType);
                            } else {
                                cusToast(context,"response data not found!!!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Loader(context, fragmentManager, false, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        showVolleyError(context, fragmentManager, error);
                    }
                });
        jsonObjectRequest.setTag(methodName);
        setJsonRequestQueue(context,jsonObjectRequest,requestType);
    }
    public void WSPostDataJsonRequestFullUrl(final Context context, final FragmentManager fragmentManager, final UpdateWSResponse updateListener, final String methodName, final Map<String, String> params, String requestType, String loaderMessage) {
        if(requestType == null){
            requestType = "";
        }
        Loader(context, fragmentManager, true, loaderMessage);
        final String finalRequestType = requestType;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,methodName, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response != null && response.length() > 0){
                                updateListener.updateWSResponse(response, finalRequestType);
                            } else {
                                HelperObj.getInstance().cusToast(context,"response data not found!!!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Loader(context, fragmentManager, false, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        showVolleyError(context, fragmentManager, error);
                    }
                });
        jsonObjectRequest.setTag(methodName);
        setJsonRequestQueue(context,jsonObjectRequest,requestType);
    }
    private void setJsonRequestQueue(Context context, JsonObjectRequest request, String requestType){
        try {
            if(requestQueue != null){
                requestQueue.cancelAll(requestType);
            }
            //
            request.setRetryPolicy(new DefaultRetryPolicy(requestTime,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Loader(Context context, FragmentManager fragmentManager, Boolean isHidden, String message){
        try {
            if(message == null || message.isEmpty()){
                message = "Please wait...";
            }
            if(isHidden){
                isAsyncTaskComplete = false;
                dialogLoader = new DialogLoader();
                dialogLoader.setDialogTitle(context,message);
                dialogLoader.setCancelable(false);
                if(dialogLoader != null){
                    try {
                        dialogLoader.show(fragmentManager, "DialogLoader");
                    } catch (Exception e) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(dialogLoader, "DialogLoader");
                        transaction.commitAllowingStateLoss();
                    }
                    //
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                isAsyncTaskComplete = true;
                                if(dialogLoader != null){
                                    dialogLoader.dismiss();
                                    dialogLoader = null;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, requestTime);
                }
            } else {
                isAsyncTaskComplete = true;
                if(dialogLoader != null){
                    dialogLoader.dismiss();
                    dialogLoader = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showVolleyError(Context context, FragmentManager fragmentManager, VolleyError error){
        /*String message = "";
        if (error instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (error instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (error instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }*/
        /*if(error.networkResponse == null){
            showNetworkError(context,fragmentManager);
        }*/
        if(error instanceof NetworkError || error instanceof TimeoutError){
            showNetworkError(context,fragmentManager);
            if (error instanceof TimeoutError) {
                cusToastLong(context,"Time out error :"+error.getMessage());
            }
        } else if(error instanceof ServerError){
            cusToastLong(context,"Unable to reach server :" + error.getMessage());
        } else {
            try {
                String errorMessage = error.getMessage();
                if(errorMessage == null || errorMessage.isEmpty()){
                    errorMessage = "Unknown error occurred";
                }
                cusToast(context,errorMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Loader(context,fragmentManager,false,null);
    }
    public void showNetworkError(Context context, FragmentManager fragmentManager){
        if(dialogNetworkError == null){
            dialogNetworkError = new DialogNetworkError();
            dialogNetworkError.setDialogTitle(context);
            dialogNetworkError.setCancelable(false);
            dialogNetworkError.show(fragmentManager, "DialogNetworkError");
        }
    }
    public Boolean checkResponse(Context context, JSONObject response){
        try {
            String status = response.optString("Status");
            String message = response.optString("DisplayMessage");
            if(status.equalsIgnoreCase("true")){
                return true;
            } else {
                if(!message.isEmpty()){
                    cusToast(context,message);
                } else {
                    cusToast(context,response.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public String GetJson(ArrayList<String> ValuesList){
        //String json = "{\"json\":[{";
        String json = "{";
        for(int i=0;i<ValuesList.size();i++){
            String[] Split = ValuesList.get(i).split(" ☢");
            if(Split.length == 1){
                Split = new String[]{Split[0], " "};
            }
            json += '"'+Split[0]+'"'+':'+'"'+jsonEscapeCharacters(Split[1])+'"';
            if(i+1 != ValuesList.size()){
                json += ",";
            }
        }
        //json += "}]}";
        json += "}";
        return json;
    }
    public String jsonEscapeCharacters(String value){
        try {
            value = value.replace("\n", "\\n")
                    .replace("'", "\"")
                    .replace("\"", "\\\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
    public void HideKeyboard(View v){
        try {
            if(v != null){
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ShowKeyboard(View v){
        try {
            /*if(v != null){
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    //imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    imm.showSoftInput(v,0);
                }
            }*/
            InputMethodManager inputMethodManager = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public File CreateSDFile(String FileName, String FolderName){
        File dir = new File(Environment.getExternalStorageDirectory(), FolderName);
        if(!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        File Destination = new File(dir,FileName);
        if(Destination.exists()){
            Destination.delete();
        }
        try {
            Destination.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Destination;
    }
    public ProgressDialog showProgressDialog(Context context) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        return pDialog;
    }

    public void hideProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog.dismiss();

    }

    public File CreateInternalFile(Context context, String FileName){
        String appPath = context.getFilesDir().getAbsolutePath();
        File dir = new File(appPath, "Temp");
        if(!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        File Destination = new File(dir,FileName);
        if(Destination.exists()){
            Destination.delete();
        }
        try {
            Destination.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Destination;
    }

    public String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is DropBox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    public boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (Exception e) {
            app_installed = false;
        }
        return app_installed;
    }
    public boolean isServiceRunning(Class<?> serviceClass, Context context) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public Boolean IsEvenNumber(Integer value){
        return (value % 2) == 0;
    }
    public Boolean IsNull(String value){
        if(value == null){
            return true;
        }else if(value.trim().equals("") || value.equals("null") || value.equals("NULL") || value.equals("Null")){
            return true;
        } else {
            return false;
        }
    }
    public String IsNullReturnValue(String value, String returnValue){
        if(value == null){
            return returnValue;
        }else if(value.trim().equals("") || value.equals("null") || value.equals("NULL") || value.equals("Null")){
            return returnValue;
        } else {
            return value;
        }
    }
    public Boolean IsEmpty(String value){
        if(value == null){
            return true;
        }else if(value.trim().equals("") || value.equals("null") || value.equals("NULL") || value.equals("Null")){
            return true;
        } else {
            return false;
        }
    }
    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public JSONArray GetJSONArray(Cursor cursor) {
        JSONArray resultSet = new JSONArray();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int totalColumn = cursor.getColumnCount();
                JSONObject rowObject = new JSONObject();
                for( int i=0 ;  i< totalColumn ; i++ )
                {
                    if( cursor.getColumnName(i) != null )
                    {
                        try
                        {
                            if( cursor.getString(i) != null )
                            {
                                rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                            }
                            else
                            {
                                rowObject.put( cursor.getColumnName(i) ,  "" );
                            }
                        }
                        catch( Exception e )
                        {
                            Log.d("TAG_NAME", e.getMessage()  );
                        }
                    }
                }
                resultSet.put(rowObject);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public int ArrayListStringIndex(ArrayList<String> arrayList, String value) {
        if(arrayList != null){
            return arrayList.indexOf(value);
        } else {
            return -1;
        }
    }
    public int ArrayListIntIndex(ArrayList<Integer> arrayList, Integer value) {
        if(arrayList != null){
            return arrayList.indexOf(value);
        } else {
            return -1;
        }
    }
    public String getSum(ArrayList<String> arrayList, String value){
        String result = "";
        Integer intValue = 0;
        Double doubleValue = 0.00;
        if(value.equals("int")){
            for (int i=0; i<arrayList.size(); i++){
                intValue += Integer.parseInt(arrayList.get(i));
            }
            result = String.valueOf(intValue);
        } else if(value.equals("double")){
            for (int i=0; i<arrayList.size(); i++){
                doubleValue += Double.parseDouble(arrayList.get(i));
            }
            result = String.valueOf(new DecimalFormat("##.##").format(doubleValue));
            Double valueDouble = Double.parseDouble(result);
            int splitter = valueDouble.toString().length();
            DecimalFormat format;
            if(splitter > 7){
                format = new DecimalFormat("#0,00,000.00");
            } else if(splitter > 5){
                format = new DecimalFormat("#0,000.00");
            } else{
                format = new DecimalFormat("#0.00");
            }
            result = format.format(valueDouble);
        }
        return result;
    }
    public String formatAmount(Integer price){
        try {
            String result = String.valueOf(new DecimalFormat("##.##").format(price));
            Double valueDouble = Double.parseDouble(result);
            int splitter = valueDouble.toString().length();
            DecimalFormat format;
            if(splitter > 7){
                format = new DecimalFormat("#0,00,000.00");
            } else if(splitter > 5){
                format = new DecimalFormat("#0,000.00");
            } else{
                format = new DecimalFormat("#0.00");
            }
            return format.format(valueDouble);
        } catch (Exception e) {
            return "";
        }
    }
    public String formatAmountWithOutZero(Integer price){
        try {
            String result = String.valueOf(new DecimalFormat("##").format(price));
            Double valueDouble = Double.parseDouble(result);
            int splitter = valueDouble.toString().length();
            DecimalFormat format;
            if(splitter > 7){
                format = new DecimalFormat("#0,00,000");
            } else if(splitter > 5){
                format = new DecimalFormat("#0,000");
            } else{
                format = new DecimalFormat("#0");
            }
            return format.format(valueDouble);
        } catch (Exception e) {
            return "";
        }
    }
    public double RoundDouble(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        //String result = String.valueOf(new DecimalFormat("##.##").format(bd.doubleValue()));
        return bd.doubleValue();
    }
    public int ArrayIndex(String searchString, String[] domain){
        for(int i = 0; i < domain.length; i++){
            if(searchString.equals(domain[i])){
                return i;
            }
        }
        return -1;
    }
    public Boolean isInt(String input) {
        Boolean id;
        try {
            Integer ID = Integer.parseInt(input.trim());
            id = ID != 0;
        }
        catch(Exception e) {
            id = false;
        }
        return id;
    }
    public void cusToast(final Context context, final String message){
        try {
            Handler UIHandler = new Handler(Looper.getMainLooper());
            UIHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cusToastLong(final Context context, final String message){
        try {
            Handler UIHandler = new Handler(Looper.getMainLooper());
            UIHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                    toast.show();
                }
            }, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void alertInformation(Context context, String message){
        try {
            alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setTitle("Information")
                    .setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void alertError(Context context, String message){
        try {
            alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setTitle("Error")
                    .setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    public File CreateThumbImage(File Source, File Destination, int quality) {
        try {
            Bitmap scaledBitmap = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(Source.getPath(), options);
            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
//      max Height and width values of the compressed image is taken as 816x612
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;
//      width and height values are set maintaining the aspect ratio of the image
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }
            }
//      setting inSampleSize value allows to load a scaled down version of the original image
            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
//      inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;
//      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];
            try {
                //          load the bitmap from its path
                bmp = BitmapFactory.decodeFile(Source.getPath(), options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }
            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;
            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
//      check the rotation of the image and display it properly
            ExifInterface exif;
            try {
                exif = new ExifInterface(Source.getPath());
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                if(scaledBitmap != null){
                    scaledBitmap = Bitmap.createBitmap(scaledBitmap,0,0,scaledBitmap.getWidth(),
                            scaledBitmap.getHeight(), matrix,true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(scaledBitmap != null){
                try {
                    FileOutputStream outStream = new FileOutputStream(Destination);
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG,quality, outStream);
                    outStream.flush();
                    outStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Destination;
    }
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }
    public void setRoundImage(Bitmap bitmap, ImageView imageView){
        try {
            bitmap = getCircularBitmap(bitmap);
            bitmap = addBorderToCircularBitmap(bitmap, 3, Color.WHITE);
            bitmap = addShadowToCircularBitmap(bitmap, 2, Color.GRAY);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Bitmap getCircularBitmap(Bitmap srcBitmap) {int squareBitmapWidth = Math.min(srcBitmap.getWidth(), srcBitmap.getHeight());
        Bitmap dstBitmap = Bitmap.createBitmap(
                squareBitmapWidth, // Width
                squareBitmapWidth, // Height
                Bitmap.Config.ARGB_8888 // Config
        );
        // Initialize a new Canvas to draw circular bitmap
        Canvas canvas = new Canvas(dstBitmap);
        // Initialize a new Paint instance
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        // Initialize a new Rect instance
        Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);
        // Initialize a new RectF instance
        RectF rectF = new RectF(rect);
        // Draw an oval shape on Canvas
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // Calculate the left and top of copied bitmap
        float left = (squareBitmapWidth-srcBitmap.getWidth())/2;
        float top = (squareBitmapWidth-srcBitmap.getHeight())/2;
        // Make a rounded image by copying at the exact center position of source image
        canvas.drawBitmap(srcBitmap, left, top, paint);
        // Free the native object associated with this bitmap.
        //srcBitmap.recycle();
        // Return the circular bitmap
        return dstBitmap;
    }
    public Bitmap addBorderToCircularBitmap(Bitmap srcBitmap, int borderWidth, int borderColor){
        // Calculate the circular bitmap width with border
        int dstBitmapWidth = srcBitmap.getWidth()+borderWidth*2;
        // Initialize a new Bitmap to make it bordered circular bitmap
        Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth,dstBitmapWidth, Bitmap.Config.ARGB_8888);
        // Initialize a new Canvas instance
        Canvas canvas = new Canvas(dstBitmap);
        // Draw source bitmap to canvas
        canvas.drawBitmap(srcBitmap, borderWidth, borderWidth, null);
        // Initialize a new Paint instance to draw border
        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);
        // Draw the circular border around circular bitmap
        canvas.drawCircle(
                canvas.getWidth() / 2, // cx
                canvas.getWidth() / 2, // cy
                canvas.getWidth() / 2 - borderWidth / 2, // Radius
                paint // Paint
        );
        // Free the native object associated with this bitmap.
        //srcBitmap.recycle();
        // Return the bordered circular bitmap
        return dstBitmap;
    }
    public Bitmap addShadowToCircularBitmap(Bitmap srcBitmap, int shadowWidth, int shadowColor){
        // Calculate the circular bitmap width with shadow
        int dstBitmapWidth = srcBitmap.getWidth()+shadowWidth*2;
        Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth,dstBitmapWidth, Bitmap.Config.ARGB_8888);
        // Initialize a new Canvas instance
        Canvas canvas = new Canvas(dstBitmap);
        canvas.drawBitmap(srcBitmap, shadowWidth, shadowWidth, null);
        // Paint to draw circular bitmap shadow
        Paint paint = new Paint();
        paint.setColor(shadowColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(shadowWidth);
        paint.setAntiAlias(true);
        // Draw the shadow around circular bitmap
        canvas.drawCircle(
                dstBitmapWidth / 2, // cx
                dstBitmapWidth / 2, // cy
                dstBitmapWidth / 2 - shadowWidth / 2, // Radius
                paint // Paint
        );
        //srcBitmap.recycle();
        // Return the circular bitmap with shadow
        return dstBitmap;
    }
//    public void setGlideImage(Context context, final ImageView imageView, String Url, final Boolean isRound){
//        try {
//            if(!Url.startsWith("/")){
//                Url = "/"+Url;
//            }
//            Glide.with(context)
//                    .load(WebService.ImgPath+Url)
//                    .apply(new RequestOptions()
//                            .placeholder(R.drawable.error)
//                            .error(R.drawable.error))
//                    .into(imageView);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public void setGlideImageWH(Context context, final ImageView imageView, String Url, final Boolean isRound, final Integer width, final Integer height){
//        try {
//            if(!Url.startsWith("/")){
//                Url = "/"+Url;
//            }
//            Glide.with(context)
//                    .load(WebService.ImgPath+Url)
//                    .apply(new RequestOptions()
//                            .placeholder(R.drawable.error)
//                            .error(R.drawable.error))
//                    .into(imageView);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public void setGlideFullURLImage(Context context, final ImageView imageView, String Url, final Boolean isRound){
//        try {
//            /*Glide.with(context)
//                    .load(Url)
//                    .asBitmap()
//                    .centerCrop()
//                    .placeholder(R.drawable.dummy_image)
//                    .error(R.drawable.dummy_image)
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//                            if (resource != null) {
//                                if(isRound){
//                                    setRoundImage(resource,imageView);
//                                } else {
//                                    imageView.setImageBitmap(resource);
//                                }
//                            }
//                        }
//                        @Override
//                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            imageView.setImageResource(R.drawable.error);
//                        }
//                    });*/
//            Glide.with(context)
//                    .asBitmap()
//                    .load(Url)
//                    .apply(new RequestOptions()
//                            .placeholder(R.drawable.error)
//                            .error(R.drawable.error))
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                            if (resource != null) {
//                                if(isRound){
//                                    setRoundImage(resource,imageView);
//                                } else {
//                                    imageView.setImageBitmap(resource);
//                                }
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public void setGlideFullURLImageWH(Context context, final ImageView imageView, String Url, final Boolean isRound, final Integer width, final Integer height){
//        try {
//            /*Glide.with(context)
//                    .load(Url)
//                    .asBitmap()
//                    .centerCrop()
//                    .placeholder(R.drawable.dummy_image)
//                    .error(R.drawable.dummy_image)
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//                            if (resource != null) {
//                                if(isRound){
//                                    setRoundImage(resource,imageView);
//                                } else {
//                                    imageView.setImageBitmap(resource);
//                                }
//                            }
//                        }
//                        @Override
//                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            imageView.setImageResource(R.drawable.error);
//                        }
//                    });*/
//            Glide.with(context)
//                    .asBitmap()
//                    .load(Url)
//                    .apply(new RequestOptions()
//                            .placeholder(R.drawable.error)
//                            .error(R.drawable.error))
//                    .into(new SimpleTarget<Bitmap>(width,height) {
//                        @Override
//                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                            if (resource != null) {
//                                if(isRound){
//                                    setRoundImage(resource,imageView);
//                                } else {
//                                    imageView.setImageBitmap(resource);
//                                }
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public String getLocationName(Context context, double latitude, double longitude) {
        String cityName = locationNotFound;
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude,10);
            for (Address adrs : addresses) {
                if (adrs != null) {
                    String area = adrs.getFeatureName();
                    String city = adrs.getSubLocality();
                    if (city != null && !city.equals("")) {
                        cityName = city;
                        return area+", "+cityName;
                    } else {
                    }
                    // // you should also try with addresses.get(0).toSring();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cityName;
    }
    public void getLocationFullAddress(final Context context, final double latitude, final double longitude, final String condition) {
        class RetrieveFeedTask extends AsyncTask<String, Void, String> {
            protected String doInBackground(String... urls) {
                String fullAddress = locationNotFound;
                try {
                    Geocoder gcd = new Geocoder(context, Locale.getDefault());
                    try {
                        List<Address> addresses = gcd.getFromLocation(latitude, longitude,10);
                        /*for (Address adrs : addresses) { // you should also try with addresses.get(0).toSring();
                        }*/
                        Address adrs = addresses.get(0);
                        if (adrs != null) {
                            /*fullAddress = HelperObj.getInstance().IsNullReturnValue(adrs.getAddressLine(0),"");
                            if(fullAddress.isEmpty()){
                                String area = adrs.getFeatureName();
                                String city = adrs.getSubLocality();
                                String locality = adrs.getLocality();
                                String state = adrs.getAdminArea();
                                if (city != null && !city.equals("")) {
                                    fullAddress =  area+", "+city+", "+locality*//*+","+state*//*;
                                }
                            }*/
                            String area = adrs.getFeatureName();
                            String subLocality = adrs.getSubLocality();
                            String locality = adrs.getLocality();
                            String state = adrs.getAdminArea();
                            if (subLocality != null && !subLocality.equals("")) {
                                fullAddress =  subLocality+", "+locality;
                            } else {
                                fullAddress =  locality;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return fullAddress;
            }
            protected void onPostExecute(String result) {
                if(result.contains(locationNotFound)) {
                    try {
                        setFullAddressFromGoogleMaps(context, latitude, longitude, condition);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if(condition.equals("ActivityLocationChange")){
                      //  HelperObj.getInstance().getActivityLocationChange().setCurrentLocationText(result);
                    } else if(condition.equals("ActivityMain")){
                       // HelperObj.getInstance().getActivityMain().setCurrentLocationText(result);
                    }
                }
            }
        }
        new RetrieveFeedTask().execute();
    }
    public void setFullAddressFromGoogleMaps(final Context context, final double lat, final double lon, final String condition){
        final String apiRequest = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon; //+ "&ka&sensor=false"
        class RetrieveFeedTask extends AsyncTask<String, Void, String> {
            private StringBuilder stringBuilder = new StringBuilder();
            protected String doInBackground(String... urls) {
                try {
                    URL urlObj = new URL(apiRequest);
                    HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    int b;
                    while ((b = inputStream.read()) != -1) {
                        stringBuilder.append((char) b);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                    String address = getFullAddress(jsonObject);
                    if(address.contains(locationNotFound)){
                        try {
                            getLocationFullAddress(context,lat,lon,condition);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        /*if(loopGetLocation == 0){
                            loopGetLocation = 1;
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        getLocationFullAddress(context,lat,lon,condition);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 1000);
                        } else {
                            loopGetLocation = 0;
                        }*/
                    } else {
                        //loopGetLocation = 0;
                        if(condition.equals("ActivityLocationChange")){
                           // HelperObj.getInstance().getActivityLocationChange().setCurrentLocationText(address);
                        } else if(condition.equals("ActivityMain")){
                            //HelperObj.getInstance().getActivityMain().setCurrentLocationText(address);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        new RetrieveFeedTask().execute();
    }
    private String getFullAddress(JSONObject result){
        String finalResult = locationNotFound;
        if(result.has("results")){
            try {
                JSONArray array = result.getJSONArray("results");
                if( array.length() > 0 ){
                    JSONObject place = array.getJSONObject(0);
                    //finalResult = place.optString("formatted_address");
                    JSONArray components = place.getJSONArray("address_components");
                    finalResult = "";
                    for( int i = 0 ; i < components.length() ; i++ ){
                        JSONObject component = components.getJSONObject(i);
                        JSONArray types = component.getJSONArray("types");
                        for( int j = 0 ; j < types.length() ; j ++ ){
                            if(types.getString(j).equals("sublocality_level_1")){
                                finalResult += component.getString("long_name");
                            }
                            if(types.getString(j).equals("locality")){
                                finalResult += ", "+component.getString("long_name");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return finalResult;
    }
    public void showMapRoute(Context context, String latitude, String longitude) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+ Values.latitude+","+Values.longitude+"&daddr="+ latitude+","+longitude));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showMap(Context context, String latitude, String longitude) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+ latitude+","+longitude));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setGlideImageWH(Context context, final ImageView imageView,String Url, final Boolean isRound, final Integer width,final Integer height){
        try {
            if(!Url.startsWith("/")){
                Url = "/"+Url;
            }
            Glide.with(context)
                    .load(WebService.ImgPath+Url)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.about_logo)
                            .error(R.drawable.about_logo))
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setGlideFullURLImage(Context context, final ImageView imageView,String Url, final Boolean isRound){
        try {

            Glide.with(context)
                    .asBitmap()
                    .load(Url)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.about_logo)
                            .error(R.drawable.about_logo))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            if (resource != null) {
                                if(isRound){
                                    setRoundImage(resource,imageView);
                                } else {
                                    imageView.setImageBitmap(resource);
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ZoomInAnimation(final View view,Integer animationDuration) {
        try {
            Animation slide = null;
            slide = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            slide.setDuration(animationDuration);
            slide.setFillAfter(true);
            slide.setFillEnabled(true);
            view.startAnimation(slide);
            slide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    view.clearAnimation();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ZoomOutAnimation(final View view,Integer animationDuration) {
        try {
            Animation slide = null;
            slide = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            slide.setDuration(animationDuration);
            slide.setFillAfter(true);
            slide.setFillEnabled(true);
            view.startAnimation(slide);
            slide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    view.clearAnimation();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void SlideToAboveAnimation(final View view,Integer animationDuration) {
        try {
            Animation slide = null;
            slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
            slide.setDuration(animationDuration);
            slide.setFillAfter(true);
            slide.setFillEnabled(true);
            view.startAnimation(slide);
            slide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    view.clearAnimation();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void SlideToDownAnimation(final View view,Integer animationDuration) {
        try {
            Animation slide = null;
            slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            slide.setDuration(animationDuration);
            slide.setFillAfter(true);
            slide.setFillEnabled(true);
            view.startAnimation(slide);
            slide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    view.clearAnimation();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void TopToBottomSelfAnimation(final View view,Integer animationDuration) {
        try {
            Animation slide = null;
            slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
            slide.setDuration(animationDuration);
            slide.setFillAfter(true);
            slide.setFillEnabled(true);
            view.startAnimation(slide);
            slide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    view.clearAnimation();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void FadeInAnimation(final View view,Integer animationDuration) {
        try {
            Animation slide = null;
            slide = new AlphaAnimation(0.0f,1.0f);
            slide.setDuration(animationDuration);
            slide.setFillAfter(true);
            slide.setFillEnabled(true);
            view.startAnimation(slide);
            slide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    view.clearAnimation();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void FadeOutAnimation(final View view,Integer animationDuration) {
        try {
            Animation slide = null;
            slide = new AlphaAnimation(1.0f,0.0f);
            slide.setDuration(animationDuration);
            slide.setFillAfter(true);
            slide.setFillEnabled(true);
            view.startAnimation(slide);
            slide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    view.clearAnimation();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*public void VolleyImageLoader(Context context, final ImageView imageView, String url, final Boolean isRound){
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            ImageLoader imageLoader = new ImageLoader(queue,new BitmapLruCache());
            imageLoader.get(url, new ImageLoader.ImageListener() {
                public void onErrorResponse(VolleyError error) {
                    imageView.setImageResource(R.drawable.dummy_image);
                }
                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                    Bitmap bitmap = response.getBitmap();
                    if (bitmap != null) {
                        if(isRound){
                            HelperObj.getInstance().setRoundImage(bitmap,imageView);
                        } else {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void VolleyImageBitmap(Context context, final String url){
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            ImageLoader imageLoader = new ImageLoader(queue,new BitmapLruCache());
            imageLoader.get(Values.IMG_PATH+url, new ImageLoader.ImageListener() {
                public void onErrorResponse(VolleyError error) {
                    //
                }
                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                    try {
                        Bitmap bitmap = response.getBitmap();
                        if (bitmap != null) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPicassoImage(Context context, final ImageView imageView,String Url, final Boolean isRound){
        try {
            if(!Url.startsWith("/")){
                Url = "/"+Url;
            }
            Picasso.with(context)
                    .load(WebService.ImgPath+Url)
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.dummy_image)
                    .noFade()
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            if(isRound){
                                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                if (bitmap != null) {
                                    setRoundImage(bitmap,imageView);
                                }
                            }
                        }
                        @Override
                        public void onError() {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPicassoImageWH(Context context, final ImageView imageView,String Url, final Boolean isRound,Integer width,Integer height){
        try {
            if(!Url.startsWith("/")){
                Url = "/"+Url;
            }
            Picasso.with(context)
                    .load(WebService.ImgPath+Url)
                    .resize(width,height)
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.dummy_image)
                    .noFade()
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            if(isRound){
                                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                if (bitmap != null) {
                                    setRoundImage(bitmap,imageView);
                                }
                            }
                        }
                        @Override
                        public void onError() {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPicassoFullURLImage(Context context, final ImageView imageView,String Url, final Boolean isRound){
        try {
            Picasso.with(context)
                    .load(Url)
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.dummy_image)
                    .noFade()
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            if(isRound){
                                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                if (bitmap != null) {
                                    setRoundImage(bitmap,imageView);
                                }
                            }
                        }
                        @Override
                        public void onError() {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setGlideImage(Context context, final ImageView imageView,String Url, final Boolean isRound){
        try {
            if(!Url.startsWith("/")){
                Url = "/"+Url;
            }
            Glide.with(context)
                    .asBitmap()
                    .load(WebService.ImgPath+Url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            if (resource != null) {
                                if(isRound){
                                    setRoundImage(resource,imageView);
                                } else {
                                    imageView.setImageBitmap(resource);
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setGlideDrawableImage(Context context, final ImageView imageView,Integer resourceId, final Boolean isRound){
        try {
            Glide.with(context)
                    .load(resourceId)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setGlideFullURLImage(Context context, final ImageView imageView,String Url, final Boolean isRound){
        try {
            Glide.with(context)
                    .asBitmap()
                    .load(Url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            if (resource != null) {
                                if(isRound){
                                    setRoundImage(resource,imageView);
                                } else {
                                    imageView.setImageBitmap(resource);
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setGlideFullURLImageWH(Context context, final ImageView imageView,String Url, final Boolean isRound, final Integer width,final Integer height){
        try {
            Glide.with(context)
                    .asBitmap()
                    .load(Url)
                    .into(new SimpleTarget<Bitmap>(width,height) {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            if (resource != null) {
                                if(isRound){
                                    setRoundImage(resource,imageView);
                                } else {
                                    imageView.setImageBitmap(resource);
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
