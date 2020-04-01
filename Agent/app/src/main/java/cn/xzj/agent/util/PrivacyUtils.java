package cn.xzj.agent.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;

import com.channey.utils.FormatUtils;
import com.channey.utils.SharedPreferencesUtils;
import com.channey.utils.StringUtils;

import java.util.ArrayList;

import cn.xzj.agent.constants.EnumValue;
import cn.xzj.agent.constants.Keys;
import cn.xzj.agent.entity.privacy.CallLogInfo;
import cn.xzj.agent.entity.privacy.SmsInfo;
import cn.xzj.agent.i.OnCallLogGetListener;
import cn.xzj.agent.i.OnSmsGetListener;

public class PrivacyUtils {
    static final String SMS_URI_ALL = "content://sms/";

    /**
     * 获取手机通话记录
     * Note：如果是第一次上传，那么采集所有信息，否则只采集当天的记录
     *
     * @param fastDate 查询的起始时间点
     */
    public static void getUserCallLogs(final Context activity, long fastDate, final OnCallLogGetListener listener) {
        ArrayList<CallLogInfo> dataList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            //提示用户授权
            return;
        }
        Cursor cursor = null;
        String selection = null;
        if (fastDate > 0) {
            String date = FormatUtils.INSTANCE.timeStamp2String(System.currentTimeMillis(), EnumValue.DATE_FORMAT_3);
            long to = FormatUtils.INSTANCE.string2TimeStamp(EnumValue.DATE_FORMAT_4, date + " 23:59:59");
            selection = "date Between " + fastDate + " and " + to;
        } else {
            boolean alreadyUploadCallLog = SharedPreferencesUtils.INSTANCE.getBoolean(activity, Keys.ALREADY_UPLOAD_CALL_LOG, Activity.MODE_MULTI_PROCESS);
            if (alreadyUploadCallLog) {
                String date = FormatUtils.INSTANCE.timeStamp2String(System.currentTimeMillis(), EnumValue.DATE_FORMAT_3);
                long from = FormatUtils.INSTANCE.string2TimeStamp(EnumValue.DATE_FORMAT_4, date + " 00:00:00");
                long to = FormatUtils.INSTANCE.string2TimeStamp(EnumValue.DATE_FORMAT_4, date + " 23:59:59");
                selection = "date Between " + from + " and " + to;
            }
        }

        cursor = activity.getContentResolver()
                .query(CallLog.Calls.CONTENT_URI, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    /* Reading Name */
                    String name = cursor.getString(cursor
                            .getColumnIndex(CallLog.Calls.CACHED_NAME));

                    /* Reading Date */
                    long date = cursor.getLong(cursor
                            .getColumnIndex(CallLog.Calls.DATE));

                    /* Reading duration */
                    long duration = cursor.getLong(cursor
                            .getColumnIndex(CallLog.Calls.DURATION));

                    /* Reading Date */
                    int type = cursor
                            .getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));

                    String phoneNumber = cursor.getString(cursor
                            .getColumnIndex(CallLog.Calls.NUMBER));
                    if (StringUtils.INSTANCE.isEmpty(name)) {
                        name = phoneNumber;
                    }
                    CallLogInfo info = new CallLogInfo(duration, name, date, type, phoneNumber);
                    dataList.add(info);
                } while (cursor.moveToNext());
            }
        }
        listener.onGet(dataList);
        if (cursor != null) {
            cursor.close();
        }
    }



    /**
     * 获取用户短信
     *
     * @param fastDate fastDate 查询的起始时间点
     */

    public static void getUserSms(final Context activity, long fastDate, final OnSmsGetListener listener) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Uri uri = Uri.parse(SMS_URI_ALL);
        ArrayList<SmsInfo> list = new ArrayList<>();
        Cursor cur;
        String[] projection = {Telephony.Sms.ADDRESS, Telephony.Sms.DATE, Telephony.Sms.DATE_SENT, Telephony.Sms.READ, Telephony.Sms.TYPE, Telephony.Sms.BODY};
        String selection = null;
        if (fastDate > 0) {
            String date = FormatUtils.INSTANCE.timeStamp2String(System.currentTimeMillis(), EnumValue.DATE_FORMAT_3);
            long to = FormatUtils.INSTANCE.string2TimeStamp(EnumValue.DATE_FORMAT_4, date + " 23:59:59");
            selection = "date Between " + fastDate + " and " + to;
        } else {
            boolean alreadyUploadSms = SharedPreferencesUtils.INSTANCE.getBoolean(activity, Keys.ALREADY_UPLOAD_SMS, Activity.MODE_MULTI_PROCESS);
            if (alreadyUploadSms) {
                String date = FormatUtils.INSTANCE.timeStamp2String(System.currentTimeMillis(), EnumValue.DATE_FORMAT_3);
                long from = FormatUtils.INSTANCE.string2TimeStamp(EnumValue.DATE_FORMAT_4, date + " 00:00:00");
                long to = FormatUtils.INSTANCE.string2TimeStamp(EnumValue.DATE_FORMAT_4, date + " 23:59:59");
                selection = "date Between " + from + " and " + to;
            }
        }

        cur = activity.getContentResolver()
                .query(uri, projection, selection, null, Telephony.Sms.DEFAULT_SORT_ORDER);
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                int index_Type = cur.getColumnIndex(Telephony.Sms.TYPE);
                int intType = cur.getInt(index_Type);
                int index_Address = cur.getColumnIndex(Telephony.Sms.ADDRESS);
                int index_Date = cur.getColumnIndex(Telephony.Sms.DATE);
                int index_sentDate = cur.getColumnIndex(Telephony.Sms.DATE_SENT);
                int index_read = cur.getColumnIndex(Telephony.Sms.READ);
                int index_Body = cur.getColumnIndex(Telephony.Sms.BODY);
                String strAddress = cur.getString(index_Address);
                int intRead = cur.getInt(index_read);
                String strBody = cur.getString(index_Body);
                long date = cur.getLong(index_Date);    //接收时间
                long sentDate = cur.getLong(index_sentDate);    //发送时间
                //因为暂时发现type=MESSAGE_TYPE_SENT 时，没有sent date
                if (intType == Telephony.Sms.MESSAGE_TYPE_SENT && sentDate != 0) {
                    date = sentDate;
                }
                SmsInfo info = new SmsInfo(strAddress, date, intRead, intType, strBody);
                list.add(info);
            } while (cur.moveToNext());
        }
        listener.onGet(list);
        if (cur != null) {
            cur.close();
            cur = null;
        }

    }
}
