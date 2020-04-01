package cn.xzj.agent.ui.adapter

import android.view.View
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.entity.common.ActionInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/30
 * @Des
 */
class ActionMenuAdapter : QuickAdapter<ActionInfo>(R.layout.item_action_menu) {
    override fun convert(holder: BaseHolder, item: ActionInfo, position: Int) {
        holder.setText(R.id.item_action_menu_tv, item.name)
        setOnItemClickListener(object:QuickAdapter.OnItemClickListener<ActionInfo>{
            override fun onItemClick(view: View, itemData: ActionInfo, i: Int) {
                itemData.clickListener.onClick(view)
            }
        })
    }
}