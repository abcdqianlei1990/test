package cn.xzj.agent.entity.job

import java.io.Serializable

//岗位详情 {
//    applied (boolean, optional): 是否已申请 ,
//    city (string, optional): 工作城市 ,
//    applyCount (integer, optional): 报名人数 ,
//    companyName (string, optional): 企业名称 ,
//    companyProfile (string, optional): 企业简介 ,
//    companyShortName (string, optional): 企业简称 ,
//    content (string, optional): 工作内容 ,
//    entryCount (integer, optional): 入职奖励人数 ,
//    images (Array[string], optional): 企业图片 ,
//    interviewRequirements (string, optional): 面试要求 ,
//    living (string, optional): 衣食住行 ,
//    maxSalary (number, optional): 工资范围 ,
//    minSalary (number, optional): 工资范围 ,
//    notice (string, optional): 注意事项 ,
//    positionTypes (Array[string], optional): 岗位特性 ,
//    positionId (string, optional): 岗位ID ,
//    positionName (string, optional): 岗位名称 ,
//    privateInfo (string, optional): 隐私信息 ,
//    recruitmentNeeds (Array[招聘需求], optional): 招聘需求 ,
//    requirement (string, optional): 任职要求 ,
//    reservedFemaleQuantity (integer, optional): 已预约的女性数量 ,
//    reservedMaleQuantity (integer, optional): 已预约的男性数量 ,
//    reservedUnknownSexQuantity (integer, optional): 已预约的未知性别数量 ,
//    salaryDescription (string, optional): 工资说明 ,
//    staffSize (string, optional): 员工规模 ,
//    status (integer, optional): 岗位状态: 1 - 上架, 0 - 下架  7 - 停招,
//    welfare (Array[string], optional): 福利 ,
//    workTime (string, optional): 工作时间 ,
//    workingAddress (string, optional): 工作地址 ,
//    workingArea (string, optional): 工作区 ,
//    workingCity (string, optional): 工作市 ,
//    workingProvince (string, optional): 工作省
//    positionFeatures岗位特征
//status (integer, optional): 岗位状态: 1 - 上架, 0 - 下架 ,
//}
data class JobInfo(var positionId: String
                   , var applied: Boolean
                   , var city: String
                   , var companyName: String
                   , var companyShortName: String
                   , var positionName: String
                   , var workingProvince: String
                   , var workingCity: String
                   , var workingArea: String
                   , var workingAddress: String
                   , var recruitmentNeeds: List<RecruitmentNeedsInfo>
                   , var interviewRequirements: String
                   , var reservedMaleQuantity: Int
                   , var reservedFemaleQuantity: Int
                   , var reservedUnknownSexQuantity: Int
                   , var applyCount: Int
                   , var entryCount: Int
                   , var minSalary: Double
                   , var maxSalary: Double
                   , var salaryDescription: String
                   , var living: String
                   , var privateInfo: String
                   , var requirement: String
                   , var welfare: List<String>
                   , var content: String
                   , var workTime: String
                   , var notice: String
                   , var companyProfile: String
                   , var images: List<String>
                   , var status: Int
                   , var staffSize: String
                   , var positionTypes: List<String>
                   , var positionFeatures: List<String>?
                   , var preReserve: Boolean = false,
                   var companyInnerImages:List<String>?,
                   var companyInnerVideos:List<VideoBean>?
): Serializable