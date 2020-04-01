package cn.xzj.agent.entity.job

//岗位查询 {
//    cityId (string, optional): 城市ID ,
//    companyShortName (string, optional): 企业简称 ,
//    interviewDateFrom (string, optional): 面试时间 - 开始 ,
//    interviewDateTo (string, optional): 面试时间 - 结束 ,
//    pageNo (integer, optional): 页码 ,
//    pageSize (integer, optional): 页大小 ,
//    positionListType (string, optional): 过滤类型: MAJOR - 主推, INCENTIVE - 入职奖励, SHIPPING - 急聘, NONLOCAL - 异地, ZX - 周薪 = ['INCENTIVE', 'SHIPPING', 'NONLOCAL', 'ZX'],
//    positionName (string, optional): 岗位名称 ,
//    salary (number, optional): 薪资 ,
//    sex (integer, optional): 性别: 1 - 男, 2 - 女 ,
//    tagIds (Array[string], optional): 标签ID列表 ,
//    userId (string, optional): 用户ID
//term (string, optional): 关键字 ,
//}
// class JobListBody{
//    private var cityId:String?
//    private var pageNo:String
//    private var pageSize:String = EnumValue.PAGE_SIZE
//    private var positionListType:String?
//    private var userId:String
//
//    constructor(cityId:String?,pageNo:String,pageSize:String,positionListType:String?,userId:String){
//        this.cityId = cityId
//        this.pageNo = pageNo
//        this.pageSize = pageSize
//        this.positionListType = positionListType
//        this.userId = userId
//    }
//}

data class JobListBody(var cityId:String?,var pageNo:Int,var pageSize:String,var positionListType:String?,var userId:String?,var term:String?=null,var features:ArrayList<String>?=null,var positionName:String?=null)