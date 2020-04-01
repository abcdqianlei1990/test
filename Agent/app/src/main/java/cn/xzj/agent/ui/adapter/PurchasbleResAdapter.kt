package cn.xzj.agent.ui.adapter

import android.support.v7.widget.AppCompatImageView
import cn.xzj.agent.R
import cn.xzj.agent.entity.customerres.PurchasbleResInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import com.channey.utils.ShapeUtil


class PurchasbleResAdapter : QuickAdapter<PurchasbleResInfo>(R.layout.item_purchable_res) {

    override fun convert(holder: BaseHolder, item: PurchasbleResInfo, position: Int) {
        holder.setText(R.id.item_purchable_res_name,item.desc)
        holder.setText(R.id.item_purchable_res_cost_tv,"${item.gold}")
        ShapeUtil.setShape(holder.itemView,radius = context!!.resources.getDimension(R.dimen.dp3),solidColor = context!!.resources.getColor(R.color.white))
        var cb = holder.getView<AppCompatImageView>(R.id.item_purchable_res_cb)
        if (item.selected){
            cb.setImageResource(R.drawable.ic_checkbox_on)
        }else{
            cb.setImageResource(R.drawable.ic_checkbox_off)
        }
    }
}