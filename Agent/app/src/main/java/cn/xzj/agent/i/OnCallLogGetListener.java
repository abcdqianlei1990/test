package cn.xzj.agent.i;


import java.util.ArrayList;

import cn.xzj.agent.entity.privacy.CallLogInfo;

/**
 * Created by channey on 2017/10/18.
 */

public interface OnCallLogGetListener {
    void onGet(ArrayList<CallLogInfo> list);
}