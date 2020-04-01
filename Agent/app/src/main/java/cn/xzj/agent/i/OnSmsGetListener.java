package cn.xzj.agent.i;


import java.util.ArrayList;

import cn.xzj.agent.entity.privacy.SmsInfo;

/**
 * Created by channey on 2017/10/18.
 */

public interface OnSmsGetListener {
    void onGet(ArrayList<SmsInfo> list);
}