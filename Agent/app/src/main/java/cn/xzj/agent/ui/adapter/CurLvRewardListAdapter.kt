package cn.xzj.agent.ui.adapter

import android.support.v7.widget.AppCompatTextView
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.entity.reward.HierarchicalRewardInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import com.channey.utils.FormatUtils
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils

class CurLvRewardListAdapter : QuickAdapter<HierarchicalRewardInfo>(R.layout.item_hierarchical_reward_cur) {
    override fun convert(holder: BaseHolder, item: HierarchicalRewardInfo, position: Int) {
        ShapeUtil.setShape(holder.itemView
                ,radius = context!!.resources.getDimension(R.dimen.dp_4)
                ,solidColor = context!!.resources.getColor(R.color.white))
        holder.setText(R.id.item_hierarchical_reward_date_tv, FormatUtils.timeStamp2String(item.day.toLong(),"yyyy-MM-dd"))
        holder.setText(R.id.item_hierarchical_reward_amount_tv, StringUtils.doubleFormat(item.amount))
        holder.setText(R.id.item_hierarchical_reward_namephone_tv, "${item.userName} ${item.phone}")
        holder.setText(R.id.item_hierarchical_reward_type_tv, "提成模式：${item.getRewardModeStr()}")
        //提成条件
        var conditionTv = holder.getView<AppCompatTextView>(R.id.item_hierarchical_reward_rewardCondition_tv)
        if (StringUtils.isNotEmpty(item.rewardCondition)){
            conditionTv.visibility = View.VISIBLE
            conditionTv.text = "提成条件：${item.rewardCondition}"
        }else{
            conditionTv.visibility = View.GONE
        }
        holder.setText(R.id.item_hierarchical_reward_entryCompanyName_tv, "入职企业：${item.positionName}")
        var onboardingTime = ""
        if (StringUtils.isNotEmpty(item.onboardingTime)){
            onboardingTime = FormatUtils.timeStamp2String(item.onboardingTime.toLong(),"yyyy-MM-dd")
        }

        var quitTimeTv = holder.getView<AppCompatTextView>(R.id.item_hierarchical_reward_quitDate_tv)
        if (StringUtils.isNotEmpty(item.quitTime)){
            var quitTime = FormatUtils.timeStamp2String(item.quitTime.toLong(),"yyyy-MM-dd")
            quitTimeTv.text = "离职时间：$quitTime"
            quitTimeTv.visibility = View.VISIBLE
        }else{
            quitTimeTv.visibility = View.GONE
        }
        holder.setText(R.id.item_hierarchical_reward_entryDate_tv, "入职时间：$onboardingTime")
    }
}