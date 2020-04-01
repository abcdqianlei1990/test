package cn.xzj.agent.entity.company

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/2
 * @Des
 */
class CompanyJobDTO {

    /**
     * cityId : string
     * companyShortName : string
     * features : ["string"]
     * interviewDateFrom : 2019-01-02T01:13:08.719Z
     * interviewDateTo : 2019-01-02T01:13:08.719Z
     * major : false
     * pageNo : 0
     * pageSize : 0
     * positionName : string
     * status : 0
     * zx : false
     */

    var cityId: String? = null
    var companyShortName: String? = null
    var interviewDateFrom: String? = null
    var interviewDateTo: String? = null
    var isMajor: Boolean?=null
    var pageNo: Int = 0
    var pageSize: Int = 0
    var positionName: String? = null
    var status: Int?=null
    var isZx: Boolean?=null
    var features: List<String>? = null
}
