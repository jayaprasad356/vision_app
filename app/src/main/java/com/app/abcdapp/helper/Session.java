package com.app.abcdapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.app.abcdapp.activities.LoginActivity;


public class Session {
    public static final String PREFER_NAME = "bigwigg";
    final int PRIVATE_MODE = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _activity;

    public Session(Context activity) {
        try {
            this._activity = activity;
            pref = _activity.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
            editor = pref.edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setData(String id, String val) {
        editor.putString(id, val);
        editor.commit();
    }

    public void setBoolean(String id, boolean val) {
        editor.putBoolean(id, val);
        editor.commit();
    }

    public void setUserData(String id, String name, String mobile, String password, String dob, String email, String city, String referred_by,
                            String earn, String withdrawal, String total_referrals, int today_codes, int total_codes, String balance, String refer_balance,
                            String device_id, String status, String refer_code, String refer_bonus_sent, String code_generate,
                            String code_generate_time, String last_updated, String joined_date, String withdrawal_status, String task_type,
                            String trial_expired,String champion_task_eligible,String trial_count,String mcg_timer,String security,
                            String ongoing_sa_balance,String salary_advance_balance,String sa_refer_count,String refund_wallet,String total_refund,String level,String per_code_val,String worked_days,String project_type,String earning_wallet,String bonus_wallet,int today_mails,int total_mails,String target_refers,String daily_wallet,String monthly_wallet,String ch_daily_wallet,String ch_monthly_wallet) {
        editor.putString(Constant.USER_ID, id);
        editor.putString(Constant.NAME, name);
        editor.putString(Constant.MOBILE, mobile);
        editor.putString(Constant.PASSWORD, password);
        editor.putString(Constant.DOB, dob);
        editor.putString(Constant.EMAIL, email);
        editor.putString(Constant.CITY, city);
        editor.putString(Constant.REFERRED_BY, referred_by);
        editor.putString(Constant.EARN, earn);
        editor.putString(Constant.EARN, earn);
        editor.putString(Constant.WITHDRAWAL, withdrawal);
        editor.putString(Constant.TOTAL_REFERRALS, total_referrals);
        editor.putInt(Constant.TODAY_CODES, today_codes);
        editor.putInt(Constant.TOTAL_CODES, total_codes);
        editor.putString(Constant.BALANCE, balance);
        editor.putString(Constant.REFER_BALANCE, refer_balance);
        editor.putString(Constant.DEVICE_ID, device_id);
        editor.putString(Constant.STATUS, status);
        editor.putString(Constant.REFER_CODE, refer_code);
        editor.putString(Constant.REFER_BONUS_SENT, refer_bonus_sent);
        editor.putString(Constant.CODE_GENERATE, code_generate);
        editor.putString(Constant.CODE_GENERATE_TIME, code_generate_time);
        editor.putString(Constant.LAST_UPDATED, last_updated);
        editor.putString(Constant.JOINED_DATE, joined_date);
        editor.putString(Constant.WITHDRAWAL_STATUS, withdrawal_status);
        editor.putString(Constant.TASK_TYPE, task_type);
        editor.putString(Constant.TRIAL_EXPIRED, trial_expired);
        editor.putString(Constant.CHAMPION_TASK_ELIGIBLE, champion_task_eligible);
        editor.putString(Constant.TRIAL_COUNT,trial_count);
        editor.putString(Constant.MCG_TIMER,mcg_timer);
        editor.putString(Constant.SECURITY,security);
        editor.putString(Constant.ONGOING_SA_BALANCE,ongoing_sa_balance);
        editor.putString(Constant.SALARY_ADVANCE_BALANCE,salary_advance_balance);
        editor.putString(Constant.SA_REFER_COUNT,sa_refer_count);
        editor.putString(Constant.REFUND_WALLET,refund_wallet);
        editor.putString(Constant.TOTAL_REFUND,total_refund);
        editor.putString(Constant.LEVEL,level);
        editor.putString(Constant.PER_CODE_VAL,per_code_val);
        editor.putString(Constant.WORKED_DAYS,worked_days);
        editor.putString(Constant.PROJECT_TYPE,project_type);
        editor.putString(Constant.EARNINGS_WALLET,earning_wallet);
        editor.putString(Constant.BONUS_WALLET,bonus_wallet);
        editor.putInt(Constant.TODAY_MAILS,today_mails);
        editor.putInt(Constant.TOTAL_MAILS,total_mails);
        editor.putString(Constant.TARGET_REFERS,target_refers);
        editor.putString(Constant.DAILY_WALLET,daily_wallet);
        editor.putString(Constant.MONTHLY_WALLET,monthly_wallet);
        editor.putString(Constant.CH_DAILY_WALLET,ch_daily_wallet);
        editor.putString(Constant.CH_MONTHLY_WALLET,ch_monthly_wallet);
        editor.commit();
    }

    public String getData(String id) {
        return pref.getString(id, "");
    }

    public int getInt(String id) {
        return pref.getInt(id, 0);
    }

    public void setInt(String id, Integer val) {
        editor.putInt(id, val);
        editor.commit();
    }


    public void logoutUser(Activity activity) {
        Intent i = new Intent(activity, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i);
        activity.finish();
        editor.clear();
        editor.commit();

        new Session(_activity).setBoolean("is_logged_in", false);

    }

    public boolean getBoolean(String id) {
        return pref.getBoolean(id, false);
    }


}