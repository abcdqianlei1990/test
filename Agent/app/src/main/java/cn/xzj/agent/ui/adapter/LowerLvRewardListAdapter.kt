package cn.xzj.agent.ui.adapter

import cn.xzj.agent.R
import cn.xzj.agent.entity.reward.HierarchicalRewardInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import com.channey.utils.FormatUtils
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils

class LowerLvRewardListAdapter : QuickAdapter<HierarchicalRewardInfo>(R.layout.item_hierarchical_reward_lower) {
    override fun convert(holder: BaseHolder, item: HierarchicalRewardInfo, position: Int) {
        ShapeUtil.setShape(holder.itemView
                ,radius = context!!.resources.getDimension(R.dimen.dp_4)
                ,solidColor = context!!.resources.getColor(R.color.white))
        holder.setText(R.id.item_hierarchical_reward_lower_date_tv, FormatUtils.timeStamp2String(item.day.toLong(),"yyyy-MM-dd"))
        holder.setText(R.id.item_hierarchical_reward_lower_amount_tv, StringUtils.doubleFormat(item.amount))
        holder.setText(R.id.item_hierarchical_reward_lower_agentName_tv, item.lowerLevelAgentName)
        holder.setText(R.id.item_hierarchical_reward_lower_customerName_tv, "用户姓名：${item.userName}")
    }
}