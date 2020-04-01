package cn.xzj.agent.ui.adapter

import android.support.v7.widget.AppCompatTextView
import cn.xzj.agent.R
import cn.xzj.agent.entity.reward.FirstRewardInfo
import cn.xzj.agent.entity.reward.HierarchicalRewardInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.util.UIUtil
import com.channey.utils.FormatUtils
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils

class FirstRewardListAdapter : QuickAdapter<FirstRewardInfo>(R.layout.item_firstreward_list) {
    override fun convert(holder: BaseHolder, item: FirstRewardInfo, position: Int) {
        ShapeUtil.setShape(holder.itemView
                ,radius = context!!.resources.getDimension(R.dimen.dp_4)
                ,solidColor = context!!.resources.getColor(R.color.white))
        holder.setText(R.id.item_firstreward_date_tv, FormatUtils.timeStamp2String(item.day.toLong(),"yyyy-MM-dd"))
        holder.setText(R.id.item_firstreward_amount_tv, StringUtils.doubleFormat(item.amount))
        if (StringUtils.isNotEmpty(item.lowerLevelAgentName)){
            var tv = "培训队员姓名：${item.lowerLevelAgentName}"
            var textView = holder.getView<AppCompatTextView>(R.id.item_firstreward_name_tv)
            UIUtil.changeColorInTv(tv,context!!.resources.getColor(R.color.green29AC3E),tv.indexOf(item.lowerLevelAgentName),tv.length,textView)
        }else{
            holder.setText(R.id.item_firstreward_name_tv, "")
        }
        var sourceTv = holder.getView<AppCompatTextView>(R.id.item_firstreward_source_tv)
        sourceTv.text = "奖励来源：${item.cause}"
        var event:String
        if (StringUtils.isNotEmpty(item.currentLevelCustomerEvent)){
            event = item.currentLevelCustomerEvent
            ShapeUtil.setShape(sourceTv,radius = context!!.resources.getDimension(R.dimen.dp_2),solidColor = context!!.resources.getColor(R.color.greenC6FFB8))
        }else{
            event = item.lowerLevelCustomerEvent
            ShapeUtil.setShape(sourceTv,radius = context!!.resources.getDimension(R.dimen.dp_2),solidColor = context!!.resources.getColor(R.color.yellowFFFAB8))
        }
        holder.setText(R.id.item_firstreward_event_tv, event)
    }
}