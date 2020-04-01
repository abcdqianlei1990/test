package cn.xzj.agent.net;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xzj.agent.entity.AgentWechatAccountInfo;
import cn.xzj.agent.entity.AppVersionInfo;
import cn.xzj.agent.entity.BaseResponseInfo;
import cn.xzj.agent.entity.CityInfo;
import cn.xzj.agent.entity.CommonListBody;
import cn.xzj.agent.entity.FeedbackBody;
import cn.xzj.agent.entity.FileUploadDTO;
import cn.xzj.agent.entity.FileUploadVO;
import cn.xzj.agent.entity.RobotChatInfo;
import cn.xzj.agent.entity.ScanLoginBody;
import cn.xzj.agent.entity.Token;
import cn.xzj.agent.entity.WechatFriendsUploadDTO;
import cn.xzj.agent.entity.active_customer.ActiveCustomerInfo;
import cn.xzj.agent.entity.active_customer.CustomerActiveEventInfo;
import cn.xzj.agent.entity.agentinfo.AgentInfo;
import cn.xzj.agent.entity.agentinfo.JiezhanCancelBody;
import cn.xzj.agent.entity.agentinfo.MsgInfo;
import cn.xzj.agent.entity.agentinfo.NoticeSettingBody;
import cn.xzj.agent.entity.agentinfo.RequestCancelBody;
import cn.xzj.agent.entity.agentinfo.YuyueCancelBody;
import cn.xzj.agent.entity.baidu.BaiDuCustomTemplateTextRecognitionBody;
import cn.xzj.agent.entity.baidu.BaiDuTokenInfo;
import cn.xzj.agent.entity.certificate.BindCardPostBody;
import cn.xzj.agent.entity.certificate.FaceVerifyPostBody;
import cn.xzj.agent.entity.certificate.IDCardInfo;
import cn.xzj.agent.entity.certificate.RealNamePostBody;
import cn.xzj.agent.entity.common.CaptchaPostBody;
import cn.xzj.agent.entity.common.PgyAppInfo;
import cn.xzj.agent.entity.common.PgyBaseResponseInfo;
import cn.xzj.agent.entity.common.QrCodeTemplateInfo;
import cn.xzj.agent.entity.customer.BadgeUploadRequestBody;
import cn.xzj.agent.entity.customer.WorkExperienceCompanyInfo;
import cn.xzj.agent.entity.customer.WorkingRecordInfo;
import cn.xzj.agent.entity.customer.WorkingRecordPostBody;
import cn.xzj.agent.entity.goldenbeans.GoldenBeansPaymentPostBody;
import cn.xzj.agent.entity.payment.PaymentRetInfo;
import cn.xzj.agent.entity.common.SignUpPostBody;
import cn.xzj.agent.entity.company.CompanyJobDTO;
import cn.xzj.agent.entity.core_customer.CoreCustomerInfo;
import cn.xzj.agent.entity.customer.City;
import cn.xzj.agent.entity.customer.CustomerDetailInfo;
import cn.xzj.agent.entity.customer.JiezhanRecordInfo;
import cn.xzj.agent.entity.customer.MemberSuggestBody;
import cn.xzj.agent.entity.customer.NoteRemarkBody;
import cn.xzj.agent.entity.customer.NoteRemarkBodyNew;
import cn.xzj.agent.entity.customer.OriginalCustomerInfo;
import cn.xzj.agent.entity.customer.PositionRequestPermissionInfo;
import cn.xzj.agent.entity.customer.QiandaoRecordInfo;
import cn.xzj.agent.entity.customer.RealNameVerificationBody;
import cn.xzj.agent.entity.customer.RemarkHistoryInfo;
import cn.xzj.agent.entity.customer.RemarkVO;
import cn.xzj.agent.entity.customer.RequestRecordInfo;
import cn.xzj.agent.entity.customer.RequestRecordsBodySimple;
import cn.xzj.agent.entity.customer.YuyueRecordInfo;
import cn.xzj.agent.entity.customerres.PurchasbleCustomerResResp;
import cn.xzj.agent.entity.customerres.ResPurchasePostBody;
import cn.xzj.agent.entity.customerres.ResPurchaseRecordInfo;
import cn.xzj.agent.entity.customerres.ResPurchaseResp;
import cn.xzj.agent.entity.payment.PaymentPostBody;
import cn.xzj.agent.entity.goldenbeans.GoldenBeansChangeRecordInfo;
import cn.xzj.agent.entity.goldenbeans.GoldenBeansProductListInfo;
import cn.xzj.agent.entity.payment.PaymentStatusInfo;
import cn.xzj.agent.entity.job.InterviewInfo;
import cn.xzj.agent.entity.job.JobFeature;
import cn.xzj.agent.entity.job.JobInfo;
import cn.xzj.agent.entity.job.JobListBody;
import cn.xzj.agent.entity.job.JobSearchSuggestion;
import cn.xzj.agent.entity.job.JobYuyueBody;
import cn.xzj.agent.entity.job.PickLocationVO;
import cn.xzj.agent.entity.job.ReservationDTO;
import cn.xzj.agent.entity.job.RewardConditionInfo;
import cn.xzj.agent.entity.newyearreservation.NewYearReservation;
import cn.xzj.agent.entity.privacy.CallLogUploadBody;
import cn.xzj.agent.entity.privacy.InstalledAppUploadBody;
import cn.xzj.agent.entity.privacy.SmsUploadBody;
import cn.xzj.agent.entity.privacy.WeChatRecordsUploadBody;
import cn.xzj.agent.entity.privacy.WechatContactsUploadBody;
import cn.xzj.agent.entity.reward.FirstRewardInfo;
import cn.xzj.agent.entity.reward.HierarchicalRewardInfo;
import cn.xzj.agent.entity.share.WechatHongbaoShareInfo;
import cn.xzj.agent.entity.store.StoreInfo;
import cn.xzj.agent.entity.store.StoreJiezhanInfo;
import cn.xzj.agent.entity.task.TaskCompleteDTO;
import cn.xzj.agent.entity.task.TaskInfo;
import cn.xzj.agent.entity.task.TaskTypeInfo;
import cn.xzj.agent.entity.task.TasksRequestBody;
import cn.xzj.agent.entity.task.TasksRequestBody2;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * Created by channey on 2016/10/20.
 * version:1.0
 * desc:
 */

public interface ApiManager {

    /**
     * 获取最新app版本信息
     *
     * @ Header("x-os-type")String osType
     */
    @GET("resources/app-info")
    @Headers("x-os-type: android")
    Observable<BaseResponseInfo<AppVersionInfo>> getAppVersionInfo();

    /**
     *  扫描登录
     */
    @POST("agents/qr-access-token")
    Observable<BaseResponseInfo<Object>> scanLogin(@Body ScanLoginBody scanLoginBody);


    /**
     * 登录
     *
     * @param username 手机号
     * @param password 验证码
     */
    @POST("auth/token")
    @FormUrlEncoded
    Observable<Token> login(@Field("username") String username, @Field("password") String password);

    /**
     * 手机号登录
     *
     * @param phone 手机号
     * @param code  验证码
     */
    @POST("auth/xzj/login")
    Observable<Token> phoneLogin(@Query("phone") String phone, @Query("code") String code);

    /**
     * 获取登录验证码
     *
     * @param phone 手机号
     */
    @POST("/auth/xzj/verify-code")
    Observable<BaseResponseInfo> getVerificationCode(@Query("phone") String phone);


    /**
     * 获取经纪人信息
     *
     * @return
     */
    @GET("agent-profile")
    Observable<BaseResponseInfo<AgentInfo>> getAgentInfo();

    @GET("agent-profile")
    Observable<BaseResponseInfo<AgentInfo>> getAgentInfo(@Query("agent_id") String agentId);

    /**
     * 查询下级经纪人帐号列表
     *
     * @param agentId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GET("agents/lower-levels")
    Observable<BaseResponseInfo<CommonListBody<AgentInfo>>> getLowerAgents(@Query("agent_id") String agentId
            , @Query("page_no") int pageNo
            , @Query("page_size") String pageSize);

    /**
     * 获取任务类型
     *
     * @return
     */
    @GET("agent-tasks/types")
    Observable<BaseResponseInfo<List<TaskTypeInfo>>> getTaskTypes(@QueryMap Map<String, String> map);

    /**
     * 获取经纪人任务列表
     *
     * @param body
     * @return
     */
    @POST("agent-tasks")
    Observable<BaseResponseInfo<CommonListBody<TaskInfo>>> getTasks(@Body TasksRequestBody body);

    /**
     * 获取经纪人任务列表（请求某个时间段的任务）
     *
     * @param body
     * @return
     */
    @POST("agent-tasks")
    Observable<BaseResponseInfo<CommonListBody<TaskInfo>>> getTasks(@Body TasksRequestBody2 body);


    /**
     * 完成任务
     *
     * @param body
     * @return
     */
    @POST("agent-tasks/completion")
    Observable<BaseResponseInfo<Boolean>> makeTaskDone(@Body TaskCompleteDTO body);

    /**
     * 获取用户新注册任务
     */
    @GET("agent-tasks/sign-up-task")
    Observable<BaseResponseInfo<TaskInfo>> getRegisterTask(@Query("user_id") String user_id);


    /**
     * 获取可分配客户列表
     *
     * @param map
     * @return
     */
    @GET("agent-assignments/customers")
    Observable<BaseResponseInfo<CommonListBody<OriginalCustomerInfo>>> getOriginalCustomers(@QueryMap HashMap<String, String> map);

    /**
     * 核心客户
     */
    @GET("agent-assignments/core-customers")
    Observable<BaseResponseInfo<CommonListBody<CoreCustomerInfo>>> getCoreCustomers(@QueryMap HashMap<String, Object> map);

    /**
     * 活跃客户
     */
    @GET("agent-assignments/active-customers")
    Observable<BaseResponseInfo<CommonListBody<ActiveCustomerInfo>>> getActiveCustomers(@QueryMap HashMap<String, Object> map);

    @GET("agent-assignments/customer-active-events")
    Observable<BaseResponseInfo<List<CustomerActiveEventInfo>>> getCustomerActiveEvents(@Query("user_id") String user_id);


    /**
     * 获取客户详情
     *
     * @param map
     * @return
     */
    @GET("customers")
    Observable<BaseResponseInfo<CustomerDetailInfo>> getCustomerDetailInfo(@QueryMap Map<String, String> map);

    /**
     * 获取客户岗位申请记录
     *
     * @param body
     * @return
     */
    @POST("customers/positions-application")
    Observable<BaseResponseInfo<CommonListBody<RequestRecordInfo>>> getCustomerRequestRecords(@Body RequestRecordsBodySimple body);

    //    Observable<BaseResponseInfo<CommonListBody<RequestRecordInfo>>> getCustomerRequestRecords(@Body RequestRecordsBody body);
    @POST("customers/real-name-verification")
    Observable<BaseResponseInfo<Integer>> realNameVerification(@Body RealNameVerificationBody realNameVerificationBody);

    /**
     * 取消申请
     *
     * @param body
     * @return
     */
    @POST("application/positions-application-cancel")
    Observable<BaseResponseInfo<Boolean>> requestCancel(@Body RequestCancelBody body);

    /**
     * 获取岗位预约记录
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GET("customers/positions-reservation")
    Observable<BaseResponseInfo<CommonListBody<YuyueRecordInfo>>> getCustomerYuyueRecords(@Query("user_id") String userId
            , @Query("page_no") int pageNo
            , @Query("page_size") String pageSize);

    /**
     * 取消预约
     *
     * @param body
     * @return
     */
    @POST("application/positions-reservation-cancel")
    Observable<BaseResponseInfo<Boolean>> yuyueCancel(@Body YuyueCancelBody body);

    /**
     * 获取接站记录
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GET("customers/pick-up")
    Observable<BaseResponseInfo<CommonListBody<JiezhanRecordInfo>>> getCustomerJiezhanRecords(@Query("user_id") String userId
            , @Query("page_no") int pageNo
            , @Query("page_size") String pageSize);

    /**
     * 取消接站
     *
     * @param body
     * @return
     */
    @POST("application/pick-up-cancel")
    Observable<BaseResponseInfo<Boolean>> jiezhanCancel(@Body JiezhanCancelBody body);

    /**
     * 获取签到记录
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GET("customers/sign-in")
    Observable<BaseResponseInfo<CommonListBody<QiandaoRecordInfo>>> getCustomerQiandaoRecords(@Query("user_id") String userId
            , @Query("page_no") int pageNo
            , @Query("page_size") String pageSize);

    /**
     * 推荐客户
     *
     * @param body
     * @return
     */
    @POST("customers/referral")
    Observable<BaseResponseInfo<Boolean>> memberSuggest(@Body MemberSuggestBody body);

    /**
     * 设置跟踪提醒
     *
     * @param body
     * @return
     */
    @POST("application/reserve-notification")
    Observable<BaseResponseInfo<Boolean>> noticeSetting(@Body NoticeSettingBody body);

    /**
     * 岗位预约
     *
     * @param body
     * @return
     */
    @POST("application/position")
    Observable<BaseResponseInfo<Boolean>> jobYuyue(@Body JobYuyueBody body);

    @POST("reservation")
    Observable<BaseResponseInfo<Object>> jobReservation(@Body ReservationDTO reservationDTO);

    /**
     * 获取门店列表
     *
     * @return
     */
    @GET("stores")
    Observable<BaseResponseInfo<List<StoreInfo>>> getStores();

    /**
     * 获取门店接站信息
     *
     * @return
     */
    @GET("stores/pick-up-locations")
    Observable<BaseResponseInfo<List<StoreJiezhanInfo>>> getStoreJiezhanInfo(@Query("store_id") String storeId);


    //***************************岗位相关positions****************************//


    /**
     * 获取城市列表
     *
     * @return
     */
    @GET("positions/cities")
    Observable<BaseResponseInfo<List<CityInfo>>> getCities();

    /**
     * 获取职位详情
     *
     * @param positionId
     * @return
     */
    @GET("positions/details")
    Observable<BaseResponseInfo<JobInfo>> getPositionDetails(@Query("position_id") String positionId, @Query("user_id") String user_id);

    /**
     * 搜索联想结果
     */
    @GET("positions/suggestions")
    Observable<BaseResponseInfo<List<JobSearchSuggestion>>> getSearchSuggestions(@Query("term") String term);


    /**
     * 获取面试信息
     *
     * @param positionId 职位id
     * @return
     */
    @GET("positions/interview-date")
    Observable<BaseResponseInfo<List<InterviewInfo>>> getInterviewInfos(@Query("position_id") String positionId);

    /**
     * 获取岗位信息
     *
     * @param body
     * @return
     */
    @POST("positions")
    Observable<BaseResponseInfo<CommonListBody<JobInfo>>> getJobList(@Body JobListBody body);

    /**
     * 岗位特征列表
     */
    @GET("positions/features")
    Observable<BaseResponseInfo<List<JobFeature>>> getJobFeatures();

    /**
     * 获取面试时间
     *
     * @param position_id     岗位ID
     * @param pick_up_address 接站地址
     */
    @GET("reservation/pick-up-dates")
    Observable<BaseResponseInfo<List<Long>>> getReservationDate(@Query("position_id") String position_id
            , @Query("pick_up_address") String pick_up_address);


    /**
     * 获取接站地址
     *
     * @param position_id 岗位ID
     */
    @GET("reservation/pick-up-locations")
    Observable<BaseResponseInfo<List<PickLocationVO>>> getReservationLocations(@Query("position_id") String position_id
    );


    /**
     * 消息列表查询
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GET("agent-tasks/by-types")
    Observable<BaseResponseInfo<CommonListBody<MsgInfo>>> getMsgList(@Query("page_no") int pageNo, @Query("page_size") String pageSize);

    /**
     * 获取未读消息数
     *
     * @return
     */
    @GET("messages/unread")
    Observable<BaseResponseInfo<Integer>> getUnreadMsg();

    /**
     * 获取客户注记列表
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GET("customer-comments")
    Observable<BaseResponseInfo<CommonListBody<RemarkHistoryInfo>>> getHistories(@Query("user_id") String userId, @Query("page_no") int pageNo, @Query("page_size") String pageSize);

    /**
     * 提交注记
     *
     * @param body
     * @return
     */
    @POST("customer-comments")
    Observable<BaseResponseInfo<Boolean>> commitRemark(@Body NoteRemarkBodyNew body);

    @POST("customer-comments")
    Observable<BaseResponseInfo<Boolean>> commitRemark(@Body NoteRemarkBody body);

    /**
     * 提交用户备注
     *
     * @param body
     * @return
     */
    @POST("customers/remark")
    Observable<BaseResponseInfo<Boolean>> commitUserInfoRemark(@Body RemarkVO body);

    /**
     * 查询用户备注
     *
     * @param userId
     * @return
     */
    @GET("customers/remark")
    Observable<BaseResponseInfo<RemarkVO>> getUserInfoRemark(@Query("user_id") String userId);

    /**
     * 用户是否具备申请岗位的权限
     *
     * @param map
     * @return
     */
    @GET("customers/position-applied")
    Observable<BaseResponseInfo<PositionRequestPermissionInfo>> positionRequestPermissionCheck(@QueryMap Map<String, String> map);

    //*************************************上传隐私数据相关********************************************//

    /**
     * 上传微信联系人
     *
     * @param agentUserName 经纪人用户名
     * @param body          微信联系人
     * @return
     */
    @POST("privacy/wechat-contact/{agent_id}")
    Observable<BaseResponseInfo<Boolean>> uploadWeChatContacts(@Path("agent_id") String agentUserName, @Body WechatContactsUploadBody body);

    /**
     * 上传手机通话记录
     *
     * @param agentUserName 经纪人用户名
     * @param body          通话记录
     * @return true-之前没有记录且本次保存成功 false-未执行保存或者保存失败
     */
    @POST("privacy/cellphone-call-record/{agent_id}")
    Observable<BaseResponseInfo<Boolean>> uploadCallLog(@Path("agent_id") String agentUserName, @Body CallLogUploadBody body);

    /**
     * 上传短信
     *
     * @param agentUserName 经纪人用户名
     * @param body          短信
     * @return true-之前没有记录且本次保存成功 false-未执行保存或者保存失败
     */
    @POST("privacy/sms-records/{agent_id}")
    Observable<BaseResponseInfo<Boolean>> uploadSms(@Path("agent_id") String agentUserName, @Body SmsUploadBody body);

    /**
     * 上传微信聊天记录
     *
     * @param agentUserName
     * @param body          微信聊天记录
     * @return true-之前没有记录且本次保存成功 false-未执行保存或者保存失败
     */
    @POST("privacy/wechat-records/{agent_id}")
    Observable<BaseResponseInfo<Boolean>> uploadWeChatRecords(@Path("agent_id") String agentUserName, @Body WeChatRecordsUploadBody body);


    /**
     * @param agentUserName 小职姐 userName
     * @param type          消息类型: CALL_PHONE - 电话, SMS_RECORD - 短信, WECHAT_CONTACT - 微信联系人, WECHANT_RECORD - 微信聊天记录
     */
    @GET("privacy/last-update/{agent_id}")
    Observable<BaseResponseInfo<Long>> getLastUploadDate(@Path("agent_id") String agentUserName, @Query("type") String type);

    /***
     * 上传手机安装的非系统APP
     */
    @POST("privacy/installed-apps/{agent_id}")
    Observable<BaseResponseInfo<Object>> uploadAllInstallApp(@Path("agent_id") String agentUserName, @Body InstalledAppUploadBody installedAppUploadBody);

    /**
     * 获取登录验证码
     *
     * @param body
     * @return
     */
    @POST("resources/verify-code")
    Observable<BaseResponseInfo<Object>> getCaptcha(@Body CaptchaPostBody body);

    /**
     * 客户注册（小职姐代注册）
     *
     * @param body
     * @return
     */
    @POST("customers/sign-up")
    Observable<BaseResponseInfo<Object>> customerSignUp(@Body SignUpPostBody body);

    //***************************工作区域相关*************************//
    @GET("work-regions/cities")
    Observable<BaseResponseInfo<List<City>>> getRegions();

    //***************************data-reporting*************************//

    /**
     * 上传微信数
     */
    @POST("data-reporting/wechat-friends-uploading")
    Observable<BaseResponseInfo<Object>> wechatFriendsUpload(@Body WechatFriendsUploadDTO body);

    /**
     * @param month 传入年月
     */
    @GET("data-reporting/wechat-friends-uploaded")
    Observable<BaseResponseInfo<List<Integer>>> getWechatFriendsUploadRecord(@Query("month") long month);

    //***************************上传文件相关*************************//
    @POST("file/upload")
    @Multipart
    Observable<BaseResponseInfo<Object>> uploadFile(@Part MultipartBody.Part img);

    @POST("file/upload-base64")
    Observable<BaseResponseInfo<List<FileUploadVO>>> uploadFiles(@Body ArrayList<FileUploadDTO> fileUploadDTOS);

    /**
     * 埋点接口所有接口都需要调用
     *
     * @param domain   xzj-crm
     * @param channel  [ios, android, web]
     * @param source   [crm]
     * @param userId   小职姐id
     * @param deviceId 设备id
     * @param date     当前时间戳
     * @param type     类型 btn 或 page
     * @param metric   view or click
     * @param id       事件id
     */
    @GET("tick")
    Observable<Object> statistics(@Query("domain") String domain,
                                  @Query("channel") String channel,
                                  @Query("source") String source,
                                  @Query("u") String userId,
                                  @Query("d") String deviceId,
                                  @Query("t") long date,
                                  @Query("id") String id,
                                  @Query("type") String type,
                                  @Query("metric") String metric);

    /**
     * GET /public/weixin-hongbao/share
     * 小职姐微信红包分享信息
     */
    @GET("public/weixin-hongbao/share")
    Observable<BaseResponseInfo<WechatHongbaoShareInfo>> getWeiXinHongBaoShareMessage();

    /**
     * 企业岗位列表
     *
     * @param body
     * @return
     */
    @POST("enterprise/positions")
    Observable<BaseResponseInfo<CommonListBody<JobInfo>>> getCompanyJobList(@Body CompanyJobDTO body);

    /**
     * 获取提成规则
     * @param recruitId
     * @return
     */
    @GET("positions/reward-condition")
    Observable<BaseResponseInfo<List<RewardConditionInfo>>> getRewardCondition(@Query("recruit_id") String recruitId);

    /**
     * 企业岗位列表
     */
    @GET("enterprise/cities")
    Observable<BaseResponseInfo<List<CityInfo>>> getCompanyCityList();

    /**
     * 春节预约
     *
     * @param page_no
     * @param page_size
     * @param agentId
     * @return
     */
    @GET("customers/chinese-new-year-reservation")
    Observable<BaseResponseInfo<CommonListBody<NewYearReservation>>> getNewYearReservation(@Query("page_no") int page_no, @Query("page_size") int page_size,
                                                                                           @Query("agent_id") String agentId, @Query("term") String term);

    /**
     * 问题回复接口
     */
    @POST("https://h5.xiaozhijie.com/chatbot/public/talk")
    Observable<BaseResponseInfo<RobotChatInfo>> sendProblem(@Query("userId") String userId, @Query("text") String text);

    //获取小职姐当前微信账号
    @GET("agent-wx-accounts/self")
    Observable<BaseResponseInfo<List<AgentWechatAccountInfo>>> getAgentWechatAccountList();

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Header("Range") String range, @Url String fileUrl);

    //*************************百度API********************************/

    /**
     * @param grantType    client_credentials 固定
     * @param clientId     API Key
     * @param clientSecret Secret Key
     */
    @POST("https://aip.baidubce.com/oauth/2.0/token")
    Observable<BaiDuTokenInfo> getBaiduAccessToken(@Query("grant_type") String grantType, @Query("client_id") String clientId, @Query("client_secret") String clientSecret);

    /**
     * 自定义模板文字识别
     */
    @POST("https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise")
    Observable<Object> getCustomTemplateTextRecognition(@Header("Content-Type") String contentType
            , @Query("access_token") String accessToken
            , @Body BaiDuCustomTemplateTextRecognitionBody body, @Query("image") String image);

    @POST("issues")
    Observable<BaseResponseInfo<Object>> feedback(@Body FeedbackBody feedbackBody);

    //*************************分级提成********************************/
    /**
     * 获取小职姐提成列表
     *
     * @param map
     * @return
     */
    @GET("agent-rewards")
    Observable<BaseResponseInfo<CommonListBody<HierarchicalRewardInfo>>> getAgentReward(@QueryMap HashMap<String, String> map);

    /**
     * 获取服务报酬信息
     *
     * @param map
     * @return
     */
    @GET("agent-rewards/hierarchical-commission/current-level")
    Observable<BaseResponseInfo<CommonListBody<HierarchicalRewardInfo>>> getCurLvRewardInfo(@QueryMap HashMap<String, String> map);

    /**
     * 获取培训津贴信息
     *
     * @param map
     * @return
     */
    @GET("agent-rewards/hierarchical-commission/lower-level")
    Observable<BaseResponseInfo<CommonListBody<HierarchicalRewardInfo>>> getLowerLvRewardInfo(@QueryMap HashMap<String, String> map);

    /**
     * 获取首单提成列表
     *
     * @param map
     * @return
     */
    @GET("agent-rewards/hierarchical-commission/1st-order")
    Observable<BaseResponseInfo<CommonListBody<FirstRewardInfo>>> getFirstRewardInfo(@QueryMap HashMap<String, String> map);


    /**
     * 获取提成总额
     * @param rewardType 提成类型: 0 - 服务报酬, 1 - 培训津贴
     * @param from 时间戳
     * @param to
     * @return
     */
    @GET("agent-rewards/hierarchical-commission/total")
    Observable<BaseResponseInfo<Double>> getTotalRewardAmount(@Query("reward_type")int rewardType,
                                                              @Query("reward_date_from")long from,
                                                              @Query("reward_date_to")long to);


    /****************************** 金豆相关 ******************************/
    /**
     * 查询金豆总数
     * @return
     */
    @GET("gold/total")
    Observable<BaseResponseInfo<Integer>> getGoldenBeansCount();

    /**
     * 金豆产品列表
     * @return
     */
    @GET("gold/purchasable")
    Observable<BaseResponseInfo<GoldenBeansProductListInfo>> getGoldenBeansProductList();

    /**
     * 购买金豆
     * @param body
     * @return
     */
    @POST("gold")
    Observable<BaseResponseInfo<PaymentRetInfo>> goldenBeanPurchase(@Body GoldenBeansPaymentPostBody body);

    /**
     * 查询金豆购买状态
     * @param orderId
     * @return
     */
    @GET("gold")
    Observable<BaseResponseInfo<PaymentStatusInfo>> getPaymentStatus(@Query("order_id")String orderId);

    /**
     * 金豆变动情况
     * @return
     */
    @GET("gold/updating-records")
    Observable<BaseResponseInfo<CommonListBody<GoldenBeansChangeRecordInfo>>> getGoldenBeansChangeRecords(@Query("page_no")int no,@Query("page_size")String size);

    /****************************** 客户资源 ******************************/

    /**
     * 获取可采购的流量（用户信息）
     * @param no
     * @param size
     * @return
     */
    @GET("flow-purchasing/purchasable")
    Observable<BaseResponseInfo<PurchasbleCustomerResResp>> getPurchasbleCustomerRes(@Query("page_no")int no, @Query("page_size")String size);

    /**
     * 购买流量
     * @param purchase
     * @return
     */
    @POST("flow-purchasing")
    Observable<BaseResponseInfo<ResPurchaseResp>> customerResPurchase(@Body ResPurchasePostBody purchase);

    /**
     * 资源购买记录
     * @param no
     * @param size
     * @return
     */
    @GET("flow-purchasing/purchased")
    Observable<BaseResponseInfo<CommonListBody<ResPurchaseRecordInfo>>> getResPurchaseRecords(@Query("page_no")int no, @Query("page_size")String size);

    /**
     * 兼职小职姐付费
     * @param body
     * @return
     */
    @POST("agents/part-time-agent-charge")
    Observable<BaseResponseInfo<PaymentRetInfo>> partTimeAgentPurchase(@Body PaymentPostBody body);

    /**
     * 绑定银行卡
     * @param body
     * @return
     */
    @POST("agent-profile/bank-card-binding")
    Observable<BaseResponseInfo<Boolean>> bindCard(@Body BindCardPostBody body);

    /**
     * 实名认证
     * @param body
     * @return
     */
    @POST("agent-profile/real-name-verification")
    Observable<BaseResponseInfo<Boolean>> realNameCertificate(@Body RealNamePostBody body);

    /**
     * 身份证识别
     * @return
     */
    @Multipart
    @POST("https://api.megvii.com/faceid/v3/ocridcard")
    Observable<IDCardInfo> idCardOCR(@Part("api_key") RequestBody api_key, @Part("api_secret") RequestBody api_secret
            , @Part MultipartBody.Part image);

    /**
     * 刷脸认证
     * @param body
     * @return
     */
    @POST("agent-profile/face-verification")
    Observable<BaseResponseInfo<Boolean>> faceVerify(@Body FaceVerifyPostBody body);

    /**
     * 上传可以选择的职位
     * @return
     */
    @GET("badge/position-applies")
    Observable<BaseResponseInfo<ArrayList<WorkExperienceCompanyInfo>>> getWorkExpCompany(@Query("user_id")String userId);

    /**
     * 上传工牌
     * @param body
     * @return
     */
    @POST("badge")
    Observable<BaseResponseInfo<Boolean>> uploadBadge(@Body BadgeUploadRequestBody body);

    /**
     * 获取二维码模板列表
     * @return
     */
    @GET("qr-code-templates")
    Observable<BaseResponseInfo<ArrayList<QrCodeTemplateInfo>>> getQrCodeTemplate();

    //=================================== 蒲公英api ===================================
    /**
     * 获取App详细信息,该接口可以获取某个 App 的某个具体版本的详细信息。
     * @param aKey (App Key是唯一标识应用的索引ID，可以通过短链接接口获取，或者我的应用接口获取)
     * @param uKey (选填) 用户Key
     * @param apiKey API Key
     * @return
     */
    @POST("http://www.pgyer.com/apiv1/app/view")
    @FormUrlEncoded
    Observable<PgyBaseResponseInfo<PgyAppInfo>> getAPPInfoFromPGY(@Field("aKey")String aKey, @Field("uKey")String uKey, @Field("_api_key")String apiKey);

    /**
     * 使用短链接获取App信息
     * @param shortcut 应用短链接，例如pgyer.com/PgY8，只需要传入PgY8参数即可
     * @param apiKey
     * @return
     */
    @POST("http://www.pgyer.com/apiv1/app/getAppKeyByShortcut")
    @FormUrlEncoded
    Observable<PgyBaseResponseInfo<PgyAppInfo>> getAPPInfoFromPGY(@Field("shortcut")String shortcut, @Field("_api_key")String apiKey);

    /**
     * 安装App
     * @return
     */
    @GET("http://www.pgyer.com/apiv1/app/install")
    Observable<String> pgyApiInstallApp(@QueryMap HashMap<String,String> map);


    /**
     * 获取工作记录
     * @return
     */
    @GET("customers/onboarding-log")
    Observable<BaseResponseInfo<CommonListBody<WorkingRecordInfo>>> getWorkingRecords(@Query("user_id")String userId,@Query("page_no")int pageNo,@Query("page_size")String pagSize);

    /**
     * 添加工作记录
     * @param onboarding
     * @return
     */
    @POST("customers/onboarding-log")
    Observable<BaseResponseInfo<Boolean>> addWorkingRecord(@Body WorkingRecordPostBody onboarding);

    /**
     * 删除工作记录
     * @param userId
     * @param id
     * @return
     */
    @DELETE("customers/onboarding-log")
    Observable<BaseResponseInfo<Boolean>> deleteWorkingRecord(@Query("user_id")String userId,@Query("id")String id);
}
