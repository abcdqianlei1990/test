package cn.xzj.agent.constants;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface Constants {
    String WX_ROOT_PATH = "/data/data/com.tencent.mm/";
    String WX_SP_UIN_PATH = WX_ROOT_PATH + "shared_prefs/auth_info_key_prefs.xml";
    String com_tencent_mm_preferences_path = WX_ROOT_PATH + "shared_prefs/com.tencent.mm_preferences.xml";
    String WX_DB_DIR_PATH = WX_ROOT_PATH + "MicroMsg";
    List<File> mWxDbPathList = new ArrayList<>();
    String WX_DB_FILE_NAME = "EnMicroMsg.db";
    String COPY_WX_DATA_DB = "wx_data.db";
    String SOURCE = "xiaozhijie-app-android";
    String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/xzjagent/";

    //*******************************埋点相关*******************************//
    String STATISTICS_SOURCE = "crm";
    String STATISTICS_DOMAIN = "xzj-crm";
    String STATISTICS_CHANNEL = "android";
    //*******************************视频播放相关*******************************//
    String AVPLAYER_COVER_LOADING = "LoadingCover";//加载视图
    String AVPLAYER_COVER_CONTROLLER = "ControllerCover";//控制视图暂停、播放等

    //**************埋点命名规则*******************//
    //统一添加前缀STATISTICS 模块和功能之间用"_"隔开
    //模块名称为多个单词组合时区分大小写  例如 historyNote
    //*******************************登录埋点id*******************************//
    String STATISTICS_LOGIN_PAGE_ID = "login";//登录
    String STATISTICS_LOGIN_CAPTCHA_ID = "login:captcha";//登陆-获取验证码
    String STATISTICS_LOGIN_SUBMIT_ID = "login:submit";//登陆-操作

    //*******************************任务列表埋点id*******************************//
    String STATISTICS_TASK_ID = "task";//任务列表
    String STATISTICS_TASK_taskType_ID = "task:taskType:";//任务列表-任务类型切换	btn	click task:taskType:$id
    String STATISTICS_TASK_changeDate_ID = "task:changeDate";//任务列表-切换今天、明天、昨天	btn	click
    String STATISTICS_TASK_historyNote_ID = "task:historyNote"; //任务列表-跳转到注记	btn	click
    String STATISTICS_TASK_COMPLETE_ID = "task:complete";//任务列表-完成任务	btn	click
    String STATISTICS_TASK_CALL_ID = "task:call";//任务列表-拨打电话	btn	click
    String STATISTICS_TASK_customerDetail_ID = "task:customerDetail";//任务列表-跳转客户详情	btn	click
    String STATISTICS_TASK_MESSAGE_ID = "task:message"; //任务列表-消息	btn	click

    //*******************************消息列表埋点id*******************************//
    String STATISTICS_MESSAGE_ID = "message";//	消息列表-page	page	view
    //*******************************客户列表埋点id*******************************//
    String STATISTICS_CUSTOMER_ID = "customer";//客户列表-page	page view
    String STATISTICS_CUSTOMER_SORT_ID = "customer:sort";//客户列表-排序	btn	click
    String STATISTICS_CUSTOMER_filterContactTime_ID="customer:filterContactTime";//客户列表-筛选联系时间	btn	click	customer:filterContactTime
    String STATISTICS_CUSTOMER_SEARCH_ID = "customer:search";//客户列表-搜索	btn	click
    String STATISTICS_CUSTOMER_CALL_ID = "customer:call";//客户列表-打电话 btn	click
    String STATISTICS_CUSTOMER_customerDetail_ID = "customer:customerDetail";//客户列表-跳转客户详情 btn	click

    //*******************************核心客户埋点id*******************************//
    String  STATISTICS_coreCustomer_ID="coreCustomer";//核心客户	核心客户-page	page	view	coreCustomer
    String STATISTICS_coreCustomer_SORT_ID="coreCustomer:sort";//核心客户-排序	btn	click	coreCustomer:sort
    String  STATISTICS_coreCustomer_filterCurrentStatus_ID="coreCustomer:filterCurrentStatus";//核心客户-筛选当前状态	btn	click	coreCustomer:filterCurrentStatus
    String  STATISTICS_coreCustomer_SEARCH_ID="coreCustomer:search";//核心客户-搜索	btn	click	coreCustomer:search
    String  STATISTICS_coreCustomer_customerDetail_ID="coreCustomer:customerDetail";//核心客户-客户详情	btn	click	coreCustomer:customerDetail

    //*******************************活跃客户埋点id*******************************//
    String  STATISTICS_activeCustomer_ID="activeCustomer";//  活跃客户	活跃客户-page	page	view	activeCustomer
    String  STATISTICS_activeCustomer_SORT_ID="activeCustomer:sort";// 活跃客户-排序	btn	click	activeCustomer:sort
    String  STATISTICS_activeCustomer_FILTER_ID="activeCustomer:filter";//活跃客户-筛选客户	btn	click	activeCustomer:filter
    String  STATISTICS_activeCustomer_SEARCH_ID="activeCustomer:search";//活跃客户-搜索	btn	click	activeCustomer:search
    String  STATISTICS_activeCustomer_activeEvents_ID="activeCustomer:activeEvents";// 活跃客户-活跃事件	btn	click	activeCustomer:activeEvents
    String  STATISTICS_activeCustomer_customerDetail_ID="activeCustomer:customerDetail";//活跃客户-客户详情 btn click activeCustomer:customerDetail

    //*******************************核心用户搜索页埋点id*******************************//
    String  STATISTICS_coreCustomerSearch_ID="coreCustomerSearch";//  核心用户搜索页	核心搜索页-page	page	view	coreCustomerSearch
    String  STATISTICS_coreCustomerSearch_BTN_ID="coreCustomerSearch:btn";// 核心搜索页-搜索	btn	click	coreCustomerSearch:btn

    //*******************************活跃客户搜索页埋点id*******************************//
    String  STATISTICS_activeCustomerSearch_ID="activeCustomerSearch";// 活跃客户搜索页	活跃客户搜索页-page	page	view	activeCustomerSearch
    String  STATISTICS_activeCustomerSearch_BTN_ID="activeCustomerSearch:btn";// 活跃客户搜索页-搜索 btn click activeCustomerSearch:btn

    //*******************************客户列表搜索页埋点id*******************************//
    String STATISTICS_customerSearch_ID = "customerSearch";//客户列表搜索页-page page	view
    String STATISTICS_customerSearch_customerDetail_ID = "customerSearch:customerDetail";//客户列表搜索页-跳转客户详情	btn	click
    //*******************************客户详情页埋点id*******************************//
    String STATISTICS_customerDetail_ID = "customerDetail";//客户详情-page	page	view
    String STATISTICS_customerDetail_positionApply_ID = "customerDetail:positionApply";//客户详情-岗位申请	btn	click
    String STATISTICS_customerDetail_positionApply_RESERVE_ID = "customerDetail:positionApply:reserve";//客户详情-岗位申请-预约	btn	click
    String STATISTICS_customerDetail_positionApply_cancelApply_ID = "customerDetail:positionApply:cancelApply";//客户详情-岗位申请-取消申请	btn	click
    String STATISTICS_customerDetail_positionApply_positionDetail_ID = "customerDetail:positionApply:positionDetail";//客户详情-岗位申请-跳转岗位详情	btn	click
    String STATISTICS_customerDetail_reserveRecord_ID = "customerDetail:reserveRecord";//客户详情-预约记录	btn	click
    String STATISTICS_customerDetail_customerDetail_reserveRecord_cancelReserve_ID = "customerDetail:reserveRecord:cancelReserve";//客户详情-预约记录-取消预约	btn	click
    String STATISTICS_customerDetail__pickupRecord_ID = "customerDetail:pickupRecord";//客户详情-接站记录	btn	click	customerDetail:pickupRecord
    String STATISTICS_customerDetail_pickupRecord_cancelPickup_ID = "customerDetail:pickupRecord:cancelPickup";//客户详情-接站记录-取消接站	btn	click
    String STATISTICS_customerDetail_signRecord_ID = "customerDetail:signRecord";//客户详情-签到记录	btn	click
    String STATISTICS_customerDetail_historyNote_ID = "customerDetail:historyNote";//客户详情-历史注记	btn	click
    String STATISTICS_customerDetail_recommendPosition_ID = "customerDetail:recommendPosition";//客户详情-推荐岗位	btn	click
    String STATISTICS_customerDetail_returnVisit_ID = "customerDetail:returnVisit";//客户详情-跟踪提醒	btn	click
    String STATISTICS_customerDetail_remarkAndNote_ID = "customerDetail:remarkAndNote";//客户详情-备注和注记	btn	click
    String STATISTICS_customerDetail_CALL_ID = "customerDetail:call";//客户详情-电话联系btn	click
    //客户详情-实名认证	btn	click	customerDetail:realNameAuthentication
    String STATISTICS_customerDetail_realNameAuthentication_ID = "customerDetail:realNameAuthentication";
    //*******************************实名认证	id*******************************//

    //实名认证-page	page	view	realNameAuthentication
    String STATISTICS_realNameAuthentication_ID = "realNameAuthentication";
    //实名认证-提交	btn	click	realNameAuthentication:submit
    String STATISTICS_realNameAuthentication_SUBMIT_ID = "realNameAuthentication:submit";



    //*******************************备注和注记搜索页埋点id*******************************//
    String STATISTICS_remarkAndNote_ID = "remarkAndNote";//备注和注记-page page	view remarkAndNote
    String STATISTICS_remarkAndNote_REMARK_SUBMIT_ID = "remarkAndNote:remark:submit";//备注和注记-提交备注	btn	click
    String STATISTICS_remarkAndNote_NOTE_SUBMIT_ID = "remarkAndNote:note:submit";//备注和注记-提交注记	btn	click
    //*******************************跟踪提醒埋点id*******************************//
    String STATISTICS_returnVisit_ID = "returnVisit";//跟踪提醒-page page	view
    String STATISTICS_returnVisit_SUBMIT_ID = "returnVisit:submit";// 跟踪提醒-提交	btn	click
    //*******************************历史注记埋点id*******************************//
    String STATISTICS_historyNote_ID = "historyNote"; //历史注记-page	page	view

    //*******************************推荐岗位埋点id*******************************//
    String STATISTICS_recommendPosition_ID = "recommendPosition";//推荐岗位-page	page	view
    String STATISTICS_recommendPosition_thinkSearch_ID = "recommendPosition:thinkSearch";//推荐岗位-搜索	btn	click
    String STATISTICS_recommendPosition_FILTER_SHIPPING_ID = "recommendPosition:filter:shipping";//推荐岗位-筛选-企业急招	btn	click
    String STATISTICS_recommendPosition_FILTER_CITY_ID = "recommendPosition:filter:city";//推荐岗位-筛选-工作城市	btn	click
    String STATISTICS_recommendPosition_FILTER_FEATURE_ID = "recommendPosition:filter:feature";//推荐岗位-筛选-岗位特征 btn	click

    //*******************************推荐岗位联想搜索埋点id*******************************//
    String STATISTICS_thinkSearch_ID = "thinkSearch";// 推荐岗位联想搜索-page	page	view
    String STATISTICS_thinkSearch_INPUT_ID = "thinkSearch:input";// 推荐岗位联想搜索-操作	btn	click
    String STATISTICS_thinkSearch_BTN_ID = "thinkSearch:btn";//推荐岗位联想搜索-搜索btn	btn	click
    String STATISTICS_thinkSearch_positionDetail_ID = "thinkSearch:positionDetail";//推荐岗位联想搜索-跳转岗位详情btn	click

    //*******************************岗位详情埋点id*******************************//
    //岗位详情-page page	view positionDetail
    String STATISTICS_positionDetail_ID = "positionDetail";
    //岗位详情-预约 btn click positionDetail:reserve
    String STATISTICS_positionDetail_RESERVE_ID = "positionDetail:reserve";

    //*******************************预约埋点id*******************************//
    //预约-page page	view reserve
    String STATISTICS_RESERVE_ID = "reserve";
    //预约-提交	btn	click	reserve:submit
    String STATISTICS_RESERVE_SUBMIT_ID = "reserve:submit";

    //*******************************我的埋点id*******************************//
    // 我的-page	page	view	my
    String STATISTICS_MY_ID = "my";
    //我的-扫一扫	btn	click	my:scan
    String STATISTICS_MY_SCAN_ID = "my:scan";
    //我的-上传微信数btn	click my:uploadWechat
    String STATISTICS_MY_uploadWechat_ID = "my:uploadWechat";
    //我的-代注册	btn	click	my:register
    String STATISTICS_MY_REGISTER_ID = "my:register";
    //我的-二维码	btn	click	my:qrcode
    String STATISTICS_MY_QRCODE_ID = "my:qrcode";
    //我的-发红包
    String STATISTICS_MY_REDPACKED_ID = "my:redpacket:submit";
    //我的-问题回答
    String STATISTICS_MY_problemReply_ID = "my:problemReply";
    //我的-知识库	btn	click	my:blogRepository
    String STATISTICS_MY_blogRepository_ID = "my:blogRepository";
    //我的-问题反馈	btn	click	my:problemFeedback
    String STATISTICS_MY_problemFeedback_ID="my:problemFeedback";
    //*******************************问题反馈埋点id*******************************//
   // 问题反馈	问题反馈-page	page	view	problemFeedback
    String STATISTICS_problemFeedback_ID="problemFeedback";
    //问题反馈-提交	btn	click	problemFeedback:submit
    String STATISTICS_problemFeedback_SUBMIT_ID="problemFeedback:submit";
    //*******************************扫一扫埋点id*******************************//
    // 扫一扫-登陆	btn	click	scan:login
    String STATISTICS_SCAN_LOGIN_ID = "scan:login";

    //*******************************上传微信数埋点id*******************************//
    //上传微信数 上传微信数-page page	view uploadWechat
    String STATISTICS_uploadWechat_ID = "uploadWechat";
    //上传微信数-提交	btn	click	uploadWechat:submit
    String STATISTICS_uploadWechat_SUBMIT_ID = "uploadWechat:submit";

    //*******************************代注册埋点id*******************************//
    // 代注册-page page	view  register
    String STATISTICS_REGISTER_ID = "register";
    //代注册-提交	btn	click	register:submit
    String STATISTICS_REGISTER_SUBMIT_ID = "register:submit";

    //*******************************二维码埋点id*******************************//
    //二维码-page page	view qrcode
    String STATISTICS_QRCODE_ID = "qrcode";
    //二维码-保存图片	btn	click	qrcode:saveImg
    String STATISTICS_QRCODE_saveImg_ID = "qrcode:saveImg";

    //*******************************配合pc端打电话id*******************************//
    //配合pc端打电话-拨打	btn click
    String STATISTICS_PCCLIENT_CALL_PHONE_CALL_ID = "pcClientCallPhone:call";
    //配合pc端打电话-挂断	btn click
    String STATISTICS_PCCLIENT_CALL_PHONE_KILL_ID = "pcClientCallPhone:kill";

    //*******************************企业职位埋点id*******************************//

    String STATISTICS_chineseNewYearReserveCustomer_ID = "chineseNewYearReserveCustomer";  //    春节预约客户	春节预约客户列表-page	page	view	chineseNewYearReserveCustomer
    String STATISTICS_chineseNewYearReserveCustomer_customerDetail_ID = "chineseNewYearReserveCustomer:customerDetail"; //    春节预约客户列表-跳转客户详情	btn	click	chineseNewYearReserveCustomer:customerDetail
    String STATISTICS_chineseNewYearReserveCustomer_SEARCH_ID = "chineseNewYearReserveCustomer:search";//    春节预约客户列表-跳转搜索	btn	click	chineseNewYearReserveCustomer:search
    //*******************************春节预约客户搜索id*******************************//
    String STATISTICS_chineseNewYearReserveCustomerSearch_ID = "chineseNewYearReserveCustomerSearch";//    春节预约客户搜索	春节预约客户搜索-page	page	view	chineseNewYearReserveCustomerSearch
    String STATISTICS_chineseNewYearReserveCustomerSearch_BTN_ID = "chineseNewYearReserveCustomerSearch:btn";//    春节预约客户搜索-搜索操作	btn	click	chineseNewYearReserveCustomerSearch:btn

    //*******************************企业职位埋点id*******************************//
    String STATISTICS_companyPosition_ID = "enterprisePosition";//推荐岗位-page	page	view
    String STATISTICS_companyPosition_thinkSearch_ID = "enterprisePosition:thinkSearch";//企业职位-搜索	btn	click
    String STATISTICS_companyPosition_FILTER_CITY_ID = "enterprisePosition:filter:city";//企业职位-筛选-工作城市	btn	click
    String STATISTICS_companyPosition_FILTER_FEATURE_ID = "enterprisePosition:filter:feature";//企业职位-筛选-岗位特征 btn	click

    //*******************************企业职位联想搜索埋点id*******************************//
    String STATISTICS_enterpriseThinkSearch_ID = "enterprisePositionThinkSearch";
    String STATISTICS_enterpriseThinkSearch_INPUT_ID = "enterprisePositionThinkSearch:input"; //企业职位联想搜索-操作	btn	click	enterprisePositionThinkSearch:input
    String STATISTICS_enterpriseThinkSearch_BTN_ID = "enterprisePositionThinkSearch:btn";//企业职位联想搜索-搜索btn	btn	click	enterprisePositionThinkSearch:btn
    String STATISTICS_enterpriseThinkSearch_positionDetail_ID = "enterprisePositionThinkSearch:positionDetail";//企业职位联想搜索-跳转岗位详情 btn	click enterprisePositionThinkSearch:positionDetail

    //*******************************机器人聊天页面id*******************************//

    //我的-问题回答
    String STATISTICS_problemReply_ID = "problemReply";
    String STATISTICS_problemReply_SHEND_ID = "problemReply:send";

    //    我的金豆
    String STATISTICS_MY_GOLDENBEANS_ID = "my:goldenbeans";
    //购买资源
    String STATISTICS_RES_PURCHASE_ID = "my:respurchase";
}
