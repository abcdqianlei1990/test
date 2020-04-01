package cn.xzj.agent.constants

class EnumValue {
    companion object {
        const val PAGE_SIZE = "20"
        //================================= 职位类型 =================================
        const val JOB_FILTER_TYPE_MAJOR = "MAJOR" //主推
        const val JOB_FILTER_TYPE_INCENTIVE = "INCENTIVE" //高额奖励
        const val JOB_FILTER_TYPE_SHIPPING = "SHIPPING"   //企业急招
        const val JOB_FILTER_TYPE_NONLOCAL = "NONLOCAL"   //异地招聘
        const val JOB_FILTER_TYPE_HOURWORK = "HOUR"   //小时工
        const val JOB_FILTER_TYPE_ZX = "ZX"   //周薪职位
        const val JOB_FILTER_TYPE_NO_REFUND = "NO_REFUND" //无返费
        const val JOB_FILTER_TYPE_MAJOR_DESC = "主推"
        const val JOB_FILTER_TYPE_INCENTIVE_DESC = "高额奖励"
        const val JOB_FILTER_TYPE_SHIPPING_DESC = "企业急招"
        const val JOB_FILTER_TYPE_NONLOCAL_DESC = "异地招聘"
        const val JOB_FILTER_TYPE_HOURWORK_DESC = "小时工"
        const val JOB_FILTER_TYPE_ZX_DESC = "周薪职位"
        const val JOB_FILTER_TYPE_NO_REFUND_DESC = "可预报名"

        //================================= 用户角色 =================================
        const val ROLE_AGENT_MEMBER = "AGENT_MEMBER"//小职姐
        const val ROLE_AGENT_XX="AGENT_XX"//薪薪客服
        //================================= TASK TYPE =================================
        const val TASK_TYPE_NOTICE = "14"
        const val TASK_TYPE_REGIST = "00"
        const val TASK_TYPE_REQUEST = "01"
        const val TASK_TYPE_JIEZHAN = "02"
        const val TASK_TYPE_RESERVATION = "03"
        const val TASK_TYPE_QIANDAO = "04"
        const val TASK_TYPE_SHANGCHE = "05"
        const val TASK_TYPE_LUQU = "06"   //录取任务
        const val TASK_TYPE_RUZHI = "07"   //入职任务
        const val TASK_TYPE_GUANHUAI = "08"   //入职关怀任务
        const val TASK_TYPE_JIANGLIDAIFA = "09"   //奖励待发任务
        const val TASK_TYPE_JIANGLIYIFA = "10"   //奖励已发任务
        const val TASK_TYPE_TIXIANTIXING = "11"   //提现提醒任务
        const val TASK_TYPE_SHENGRI = "12"   //生日提醒
        const val TASK_TYPE_LIZHI = "13"   //离职提醒
        const val TASK_TYPE_ZHOUXINDAKA = "15"   //周薪打卡提醒

        //================================= DATE FORMAT =================================
        const val DATE_FORMAT_1 = "yyyy-MM-dd HH:mm"
        const val DATE_FORMAT_2 = "MM月dd日"
        const val DATE_FORMAT_3 = "yyyy年MM月dd日"
        const val DATE_FORMAT_4 = "yyyy年MM月dd日 HH:mm"
        const val DATE_FORMAT_DATE = "yyyy年MM月dd日HH时mm分"
        const val DATA_FORMAT_5 = "yyyy-MM-dd HH:mm:ss"
        const val DATE_FORMAT_6 = "yyyy-MM-dd"
        const val DATE_FORMAT_7 = "yyyy年MM月"


        //================================= APPLY STATUS =================================
//        1-已申请 2-取消申请 10-已预约 11-取消预约 20-已签到 30－以上车 40-已录取 41-未录取 50-已入职 51-未入职 60-已结算返费 97-已外派 98-已转正 99-已离职
        const val APPLY_STATUS_REQUEST_ALREADY_1 = 1
        const val APPLY_STATUS_REQUEST_CANCEL_2 = 2
        const val APPLY_STATUS_YUYUE_ALREADYL_10 = 10
        const val APPLY_STATUS_YUYUE_CANCEL_11 = 11
        const val APPLY_STATUS_QIANDAO_ALREADY_20 = 20
        const val APPLY_STATUS_SHANGCHE_ALREADY_30 = 30
        const val APPLY_STATUS_LUQU_ALREADY_40 = 40
        const val APPLY_STATUS_LUQU_NOTYET_41 = 41
        const val APPLY_STATUS_RUZHI_ALREADY_50 = 50
        const val APPLY_STATUS_RUZHI_NOTYET_51 = 51
        const val APPLY_STATUS_JIESUAN_ALREADY_60 = 60
        const val APPLY_STATUS_WAIPAI_ALREADY_97 = 97
        const val APPLY_STATUS_ZHUANZHENG_ALREADY_98 = 98
        const val APPLY_STATUS_LIZHI_ALREADY_99 = 99

        //================================= customer level =================================
        const val CUSTOMER_LEVEL_A = "A"
        const val CUSTOMER_LEVEL_B = "B"
        //================================= job status =================================
        const val JOB_STATUS_STOP = 7//停招
        //================================= statistics =================================
        const val STATISTICS_TYPE_BTN = "btn"//类型 按钮
        const val STATISTICS_TYPE_PAGE = "page"//类型 页面
        const val STATISTICS_METRIC_VIEW = "view"//视图
        const val STATISTICS_METRIC_CLICK = "click"//点击
        //================================= 预约相关 =================================
        //0 - 未支付, 1 - 支付中, 2 - 已支付, 3 - 已取消
        const val   YUYUE_PAY_STATUS_NO_PAY=0
        const val   YUYUE_PAY_STATUS_PAYING=1
        const val   YUYUE_PAY_STATUS_PAYED=2
        const val   YUYUE_PAY_STATUS_CANCEL=3
        //("退款状态: 0 - 未退款, 1 - 退款中, 2 - 已退款")
        const val YUYUE_REFUND_STATUS_NO_REFUND=0
        const val YUYUE_REFUND_STATUS_REFUNDING=1
        const val YUYUE_REFUND_STATUS_REFUNDED=2
        //预约状态  0 - 取消, 1 - 已签到, 2 - 未签到
        const val YUYUE_STATUS_CANCEL=0
        const val YUYUE_STATUS_CANCEL_10=10
        const val YUYUE_STATUS_CANCEL_20=20
        const val YUYUE_STATUS_CANCEL_30=30
        const val YUYUE_STATUS_SINGNED=1
        const val YUYUE_STATUS_UNSIGNIN=2

        //支付方式
        const val PAYMENT_TYPE_NONE = "0"
        const val PAYMENT_TYPE_WX = "1"
        const val PAYMENT_TYPE_ZFB = "2"
        //================================= 加盟小职姐订单支付状态 =================================
        const val PARTTIME_PAYMENT_STATUS_UNPAID = 0    //待支付
        const val PARTTIME_PAYMENT_STATUS_PAYING = 1    //支付中
        const val PARTTIME_PAYMENT_STATUS_FAIL = 2  //支付失败
        const val PARTTIME_PAYMENT_STATUS_SUCC = 3  //支付成功
        const val PARTTIME_PAYMENT_STATUS_TIMEOUT = 4   //支付超时
    }

}