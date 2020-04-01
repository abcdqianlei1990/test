package cn.xzj.agent.ui.adapter

import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/30
 * @Des
 */
class DropDownSelectMenuAdapter : QuickAdapter<String>(R.layout.item_dorp_down_select_menu) {
    private var selectPosition = -1
    override fun convert(holder: BaseHolder, item: String, position: Int) {
        holder.setText(R.id.tv_item_customer_sort_title, item)
        if (selectPosition == position) {
            holder.getView<TextView>(R.id.tv_item_customer_sort_title).setTextColor(holder.itemView.context.resources.getColor(R.color.green29AC3E))
        } else {
            holder.getView<TextView>(R.id.tv_item_customer_sort_title).setTextColor(holder.itemView.context.resources.getColor(R.color.black333333))
        }
    }

    fun selectPosition(selectPosition: Int) {
        this.selectPosition = selectPosition
        notifyDataSetChanged()
    }

     fun getSelectPosition(): Int {
        return selectPosition
    }
}