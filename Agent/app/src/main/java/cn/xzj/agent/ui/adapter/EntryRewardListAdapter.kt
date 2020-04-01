package cn.xzj.agent.ui.adapter

import cn.xzj.agent.R
import cn.xzj.agent.entity.reward.HierarchicalRewardInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import com.channey.utils.FormatUtils
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils

class EntryRewardListAdapter : QuickAdapter<HierarchicalRewardInfo>(R.layout.item_entryreward_list) {
    override fun convert(holder: BaseHolder, item: HierarchicalRewardInfo, position: Int) {
        ShapeUtil.setShape(holder.itemView
                ,radius = context!!.resources.getDimension(R.dimen.dp_4)
                ,solidColor = context!!.resources.getColor(R.color.white))
        holder.setText(R.id.item_entryreward_date_tv, FormatUtils.timeStamp2String(item.day.toLong(),"yyyy-MM-dd"))
        holder.setText(R.id.item_entryreward_amount_tv, StringUtils.doubleFormat(item.amount))
        holder.setText(R.id.item_entryreward_namephone_tv, "${item.agentName} ${item.phone}")
        holder.setText(R.id.item_entryreward_positionName_tv, "岗位名称：${item.positionName}")
        holder.setText(R.id.item_entryreward_entryDate_tv, "入职时间：${item.onboardingTime}")
    }
}