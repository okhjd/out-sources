package com.dw.imximeng.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\23 0023
 */

public class SharedPreferencesHelper {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    private int PRIVATE_MODE = 0;

    //SharedPreferences 文件名
    private static final String PREF_NAME = "xmq_info";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String SWITCH_LANGUAGE = "SwitchLanguage";
    private static final String USER_PHONE = "UserPhone";
    private static final String USER_PASSWORD = "UserPassword";
    private static final String HISTORICAL_SEARCH_DATA = "Historical_search_data";

    public SharedPreferencesHelper(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setSwitchLanguage(boolean switchLanguage) {
        editor.putBoolean(SWITCH_LANGUAGE, switchLanguage);
        editor.apply();
    }

    public boolean isSwitchLanguage() {
        return pref.getBoolean(SWITCH_LANGUAGE, true);
    }

    public void setUserPhone(String userPhone) {
        editor.putString(USER_PHONE, userPhone);
        editor.apply();
    }

    public String getUserPhone() {
        return pref.getString(USER_PHONE, "");
    }

    public void setUserPassword(String password) {
        editor.putString(USER_PASSWORD, password);
        editor.apply();
    }

    public String getUserPassword() {
        return pref.getString(USER_PASSWORD, "");
    }

    public void setHistoricalSearchData(String data) {
        List<String> set = getHistoricalSearchData();
        List<String> s = new ArrayList<>();
        s.add(0,data);
        for (int i=0;i<set.size();i++){
            if (!data.equals(set.get(i))){
                s.add(set.get(i));
            }
        }
        if (s.size() > 10){
            s.remove(s.size()-1);
        }
        editor.putString(HISTORICAL_SEARCH_DATA, new Gson().toJson(s));
        editor.apply();
    }

    public List<String> getHistoricalSearchData() {
        String result = pref.getString(HISTORICAL_SEARCH_DATA, "");
        if (result.isEmpty()){
            return null;
        }
        return new Gson().fromJson(result, new TypeToken<List<String>>(){}.getType());
    }

    public void clearHistoricalSearch(){
        editor.remove(HISTORICAL_SEARCH_DATA);
        editor.apply();
    }
}
