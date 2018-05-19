package com.iprismtech.rawvana.others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Developer on 22/2/17.
 */

public class UserSession {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private static String KEY_UserID = "KEY_UserID";
    private static String KEY_UserNameID = "KEY_UserNameID";
    private static String KEY_Email = "KEY_Email";
    private static String KEY_Number = "KEY_Number";
    private static String KEY_FullName = "KEY_FullName";
    private static String KEY_UserImage = "KEY_UserImage";
    private static String KEY_DOB = "KEY_DOB";
    private static String KEY_Gender = "KEY_Gender";
    private static String KEY_FCM_Token = "KEY_FCM_Token";

    @SuppressLint("StaticFieldLeak")
    private static UserSession objInstance;
    public static UserSession getInstance(Context context) {
        if (objInstance == null) {
            objInstance = new UserSession(context);
        }
        return objInstance;
    }
    @SuppressLint("CommitPrefEdits")
    private UserSession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    public void createUserSession(Integer userId, String userNameId, String FullName, String email, String mobileNo, String userImage, String dob, String gender) {
        editor.putInt(KEY_UserID, userId);
        editor.putString(KEY_UserNameID, HelperObj.getInstance().IsNullReturnValue(userNameId,""));
        editor.putString(KEY_FullName, HelperObj.getInstance().IsNullReturnValue(FullName,""));
        editor.putString(KEY_Email, HelperObj.getInstance().IsNullReturnValue(email,""));
        editor.putString(KEY_Number, HelperObj.getInstance().IsNullReturnValue(mobileNo,""));
        editor.putString(KEY_UserImage, HelperObj.getInstance().IsNullReturnValue(userImage,""));
        editor.putString(KEY_DOB, HelperObj.getInstance().IsNullReturnValue(dob,""));
        editor.putString(KEY_Gender, HelperObj.getInstance().IsNullReturnValue(gender,""));
        editor.commit();
        //
        getUserDetails();
    }
    public void CreateFCMToken(String token){
        editor.putString(KEY_FCM_Token, HelperObj.getInstance().IsNullReturnValue(token,""));
        editor.commit();
    }
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_UserID, String.valueOf(pref.getInt(KEY_UserID, 0)));
        user.put(KEY_UserNameID, pref.getString(KEY_UserNameID, ""));
        user.put(KEY_FullName, pref.getString(KEY_FullName, ""));
        user.put(KEY_Email, pref.getString(KEY_Email, ""));
        user.put(KEY_Number, pref.getString(KEY_Number, ""));
        user.put(KEY_UserImage, pref.getString(KEY_UserImage, ""));
        user.put(KEY_DOB, pref.getString(KEY_DOB, ""));
        user.put(KEY_Gender, pref.getString(KEY_Gender, ""));
        user.put(KEY_FCM_Token, pref.getString(KEY_FCM_Token, ""));
        //
        Values.UserID = Integer.parseInt(user.get(UserSession.KEY_UserID));
        Values.UserNameID = HelperObj.getInstance().IsNullReturnValue(user.get(UserSession.KEY_UserNameID),"");
        Values.UserFullName = HelperObj.getInstance().IsNullReturnValue(user.get(UserSession.KEY_FullName),"");
        Values.UserNumber = HelperObj.getInstance().IsNullReturnValue(user.get(UserSession.KEY_Number),"");
        Values.UserEmail = HelperObj.getInstance().IsNullReturnValue(user.get(UserSession.KEY_Email),"");
        Values.UserImageName = HelperObj.getInstance().IsNullReturnValue(user.get(UserSession.KEY_UserImage),"");
        Values.UserDOB = HelperObj.getInstance().IsNullReturnValue(user.get(UserSession.KEY_DOB),"");
        Values.UserGender = HelperObj.getInstance().IsNullReturnValue(user.get(UserSession.KEY_Gender),"");
        Values.UserFCMToken = HelperObj.getInstance().IsNullReturnValue(user.get(UserSession.KEY_FCM_Token),"");
        //
        return user;
    }
    public void logoutUser() {
        editor.remove(KEY_UserID);
        editor.remove(KEY_UserNameID);
        editor.remove(KEY_FullName);
        editor.remove(KEY_Number);
        editor.remove(KEY_Email);
        editor.remove(KEY_UserImage);
        editor.remove(KEY_DOB);
        editor.remove(KEY_Gender);
        editor.remove(KEY_FCM_Token);
        editor.clear();
        editor.commit();
        //
        Values.UserID = 0;
        Values.UserNameID = "";
        Values.UserFullName = "";
        Values.UserNumber = "";
        Values.UserEmail = "";
        Values.UserImageName = "";
        Values.UserDOB = "";
        Values.UserGender = "";
        Values.UserFCMToken = "";
    }
    public Integer getUserId() {
        return pref.getInt(KEY_UserID, 0);
    }
    public boolean isUserLoggedIn() {
        if(Values.UserID > 0 && !Values.UserNameID.isEmpty()){
            return true;
        } else {
            return false;
        }
    }
}
