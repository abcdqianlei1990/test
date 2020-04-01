package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CityInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.PositionRequestPermissionInfo
import cn.xzj.agent.entity.job.JobFeature
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.entity.job.JobSearchSuggestion
import cn.xzj.agent.entity.job.RewardConditionInfo

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/10
 * @ Des
 */
interface ICompanyJobSearchView : BaseView {
    fun getSearchSuggestionsSuccess(data: List<JobSearchSuggestion>, term: String)
    fun onCityListGetSuccess(list: List<CityInfo>)
    fun onJobListGetSuccess(info: CommonListBody<JobInfo>)
    fun onJobListGetFailure()
    fun onPermissionGetSuccess(info: PositionRequestPermissionInfo, position: Int)
    fun onGetJobFeaturesSuceess(jobFeatures: List<JobFeature>)
    fun onGetJobFeaturesFailure()

    fun onRewardConditionGetSuccess(condition: List<RewardConditionInfo>?)
    fun onRewardConditionGetFailure()
}
