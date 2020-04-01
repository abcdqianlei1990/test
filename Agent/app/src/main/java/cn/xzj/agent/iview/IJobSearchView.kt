package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.job.JobSearchSuggestion

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/10
 * @ Des
 */
interface IJobSearchView : BaseView {
    fun getSearchSuggestionsSuccess(data: List<JobSearchSuggestion>, term: String)
}
