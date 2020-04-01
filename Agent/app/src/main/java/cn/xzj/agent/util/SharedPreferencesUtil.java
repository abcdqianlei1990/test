package cn.xzj.agent.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.channey.utils.SharedPreferencesUtils;

import java.util.List;

import cn.xzj.agent.MyApplication;
import cn.xzj.agent.constants.Keys;
import cn.xzj.agent.entity.RobotChatInfo;
import cn.xzj.agent.entity.DeviceInfo;
import cn.xzj.agent.entity.WechatFriendsUploadDTO;
import cn.xzj.agent.entity.agentinfo.AgentInfo;

/**
 * @ Author YeMao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/29
 * @ Des SharedPreferences 工具类
 */
public class SharedPreferencesUtil {

    private static SharedPreferences getSharedPreferences(Context mContext) {
        if (mContext == null) {
            return PreferenceManager.getDefaultSharedPreferences(MyApplication.application);
        } else {
            return PreferenceManager.getDefaultSharedPreferences(mContext);
        }
    }

    public static DeviceInfo getDeviceInfo(Context context) {
        String strData = getSharedPreferences(context).getString(Keys.DEVICE_INFO, null);
        if (TextUtils.isEmpty(strData)) {
            return null;
        }
        try {
            return JSON.parseObject(strData, DeviceInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置设备信息
     */
    public static void setDeviceInfo(Context context, DeviceInfo mDeviceInfo) {
        String strDeviceInfo = JSON.toJSONString(mDeviceInfo);
        getSharedPreferences(context).edit().putString(Keys.DEVICE_INFO, strDeviceInfo)
                .apply();
    }

    public static String getToken(Context context) {
        return SharedPreferencesUtils.INSTANCE.getString(context, Keys.TOKEN);
    }

    public static void setToken(Context context, String token) {
        if (TextUtils.isEmpty(token)) {
            SharedPreferencesUtils.INSTANCE.remove(context, Keys.TOKEN);
        } else {
            SharedPreferencesUtils.INSTANCE.saveString(context, Keys.TOKEN, "Bearer " + token);
        }
    }


    /**
     * 保存当前登录小职姐的信息
     */
    public static void setCurrentAgentInfo(Context context, AgentInfo agentInfo) {
        if (agentInfo == null) {
            getSharedPreferences(context).edit().putString(Keys.AGENT_INFO, null).apply();

        } else {
            String strAgentInfo = JSON.toJSONString(agentInfo);
            getSharedPreferences(context).edit().putString(Keys.AGENT_INFO, strAgentInfo).apply();

        }
    }

    /**
     * 获取当前登录小职姐的信息
     */
    public static AgentInfo getCurrentAgentInfo(Context context) {
        String strAgentInfo = getSharedPreferences(context).getString(Keys.AGENT_INFO, null);
        if (strAgentInfo == null)
            return null;
        return JSON.parseObject(strAgentInfo, AgentInfo.class);
    }

    /**
     * 保存上传微信截图信息
     */
    public static void setWechatFriendUploadCache(Context context, WechatFriendsUploadDTO dto) {
        if (dto == null) {
            getSharedPreferences(context).edit().putString(Keys.WECHAT_FRIEND_UPLOAD_CACHE, null).apply();
        } else {
            String strWechatFriendUploadCache = JSON.toJSONString(dto);
            getSharedPreferences(context).edit().putString(Keys.WECHAT_FRIEND_UPLOAD_CACHE, strWechatFriendUploadCache).apply();

        }
    }

    public static WechatFriendsUploadDTO getWechatFriendUploadCache(Context context) {
        String strWechatFriendUploadCache = getSharedPreferences(context).getString(Keys.WECHAT_FRIEND_UPLOAD_CACHE, null);
        if (strWechatFriendUploadCache == null)
            return null;
        return JSON.parseObject(strWechatFriendUploadCache, WechatFriendsUploadDTO.class);
    }

    /**
     * 保存机器人聊天缓存数据
     */
    public static void setRobotChatDataCache(Context context, List<RobotChatInfo> data, String agentId){
        if (data==null){
            getSharedPreferences(context).edit().putString(Keys.ROBOT_CHAT_DATA+agentId,null).apply();
        }else {
            String strRobotChatData=JSON.toJSONString(data);
            getSharedPreferences(context).edit().putString(Keys.ROBOT_CHAT_DATA+agentId,strRobotChatData).apply();

        }
    }
    /**
     * 保存机器人聊天缓存数据
     */
    public static List<RobotChatInfo> getRobotChatDataCache(Context context, String agentId){
        String strRobotChatData=getSharedPreferences(context).getString(Keys.ROBOT_CHAT_DATA+agentId,null);
        if (strRobotChatData==null){
            return null;
        }
        return JSON.parseArray(strRobotChatData,RobotChatInfo.class);
    }

    /**
     * 设置缓存环境默认 0 dev 1 test 2 pro
     */
    public static void setEnvironment(Context context, int environment) {
        getSharedPreferences(context).edit().putInt(Keys.ENVIRONMENT, environment).apply();
    }

    /**
     */
    public static int getEnvironment(Context context) {
        return getSharedPreferences(context).getInt(Keys.ENVIRONMENT, -1);
    }


    public static void clear(Context context) {
        setToken(context, null);
//        setCurrentAgentInfo(context, null);当前用户信息不用清空
        setWechatFriendUploadCache(context, null);
    }

}
