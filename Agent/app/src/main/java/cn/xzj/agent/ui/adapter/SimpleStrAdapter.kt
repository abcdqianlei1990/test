package cn.xzj.agent.ui.adapter

import cn.xzj.agent.R
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter


class SimpleStrAdapter : QuickAdapter<String>(R.layout.item_simple_str) {

    override fun convert(holder: BaseHolder, item: String, position: Int) {
        holder.setText(R.id.item_simple_str_tv,item)
    }
}