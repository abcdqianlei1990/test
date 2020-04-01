package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.reward.FirstRewardInfo
import cn.xzj.agent.entity.reward.HierarchicalRewardInfo

interface IMyRewardView : BaseView {
    fun onCurLvRewardGetSuccess(list: CommonListBody<HierarchicalRewardInfo>)
    fun onCurLvRewardGetFailure()

    fun onLowerLvRewardGetSuccess(list: CommonListBody<HierarchicalRewardInfo>)
    fun onLowervRewardGetFailure()

    fun onFirstRewardGetSuccess(list: CommonListBody<FirstRewardInfo>)
    fun onFirstRewardGetFailure()

    fun onEntryRewardGetSuccess(list: CommonListBody<HierarchicalRewardInfo>)
    fun onEntryRewardGetFailure()

    fun onTotalRewardGetSuccess(amount: Double)
    fun onTotalRewardGetFailure()
}