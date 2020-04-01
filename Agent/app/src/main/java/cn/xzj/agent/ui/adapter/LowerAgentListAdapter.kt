package cn.xzj.agent.ui.adapter

import android.support.v7.widget.AppCompatTextView
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import com.channey.utils.FormatUtils
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils

class LowerAgentListAdapter : QuickAdapter<AgentInfo>(R.layout.item_my_team) {
    override fun convert(holder: BaseHolder, item: AgentInfo, position: Int) {
        ShapeUtil.setShape(holder.itemView
                ,radius = context!!.resources.getDimension(R.dimen.dp_4)
                ,solidColor = context!!.resources.getColor(R.color.white))
        var dateTv = holder.getView<AppCompatTextView>(R.id.item_my_team_entryDate_tv)
        var nicknameTv = holder.getView<AppCompatTextView>(R.id.item_my_team_supperAgentNickName_tv)
        if (StringUtils.isNotEmpty(item.nickname)){
            nicknameTv.text = "昵称：${item.nickname}"
            nicknameTv.visibility = View.VISIBLE
        }else{
            nicknameTv.visibility = View.GONE
        }
        holder.setText(R.id.item_my_team_supperAgentName_tv, "姓名：${item.name}")
        holder.setText(R.id.item_my_team_supperAgentPhone_tv, StringUtils.phoneNumberParseWithStars(item.cellPhone))
        if (StringUtils.isNotEmpty(item.updateTime)){
            dateTv.text = "加入时间：${FormatUtils.timeStamp2String(item.updateTime.toLong(),"yyyy-MM-dd HH:mm")}"
            dateTv.visibility = View.VISIBLE
        }else{
            dateTv.visibility = View.GONE
        }
    }
}