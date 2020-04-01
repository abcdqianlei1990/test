package cn.xzj.agent.constants

class Code {

    class RequestCode{
        companion object {
            val LOGIN = 0x000001
            val FOLLOW_NOTICE_SETTING = 0x000002
            val YUYUE = 0x000003
            val SEARCH = 0x000004
            val POSITION_DETAIL = 0x000005
            val POSITION_YUYUE = 0x000006   //岗位预约
            val PROVINCE_SELECT = 0x000007   //省份选择
            val CITY_SELECT = 0x000008   //市选择
            val AREA_SELECT = 0x000009   //县、区选择
            val REMARK = 0x000010
            val AppointmentFailReason = 0x000011
            val RemarkHistoryActivity = 0x000012
            val NoteRemarkActivity = 0x000013
            val MyTeamActivity = 0x000014
            val MyRewardsActivity = 0x000015
            val WebViewActivity = 0x000016
            val PartTimeAgentPurchaseActivity = 0x000017
            val PartTimeAgentPurchaseSucActivity = 0x000018
            val BindCardStep1Activity = 0x000019
            val RealNameIdentityActivity = 0x000020
            val FACE_IDENTITY = 0x000021
            val AgentProfileActivity = 0x000022
            val GoldenBeansPurchaseActivity = 0x000023
            val AddWorkExperienceActivity = 0x000024
            val CompanySelectActivity = 0x000025
            val AddWorkingRecordActivity = 0x000026
        }
    }
    class ResultCode{
        companion object {
            val OK = 0x0010086
        }
    }
}