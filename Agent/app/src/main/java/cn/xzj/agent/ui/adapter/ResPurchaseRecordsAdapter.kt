package cn.xzj.agent.ui.adapter

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import cn.xzj.agent.R
import cn.xzj.agent.entity.customerres.ResPurchaseRecordInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import com.channey.utils.FormatUtils


class ResPurchaseRecordsAdapter : QuickAdapter<ResPurchaseRecordInfo>(R.layout.item_res_purchase_record) {

    override fun convert(holder: BaseHolder, item: ResPurchaseRecordInfo, position: Int) {
        holder.setText(R.id.item_resPurchaseRecord_count_tv,"购买${item.phone.size}个用户")
        holder.setText(R.id.item_resPurchaseRecord_date_tv, FormatUtils.timeStamp2String(item.date.toLong(),"yyyy-MM-dd hh:mm"))
        holder.setText(R.id.item_resPurchaseRecord_cost_tv,"合计：${item.gold}金豆")
        var recyclerView = holder.getView<RecyclerView>(R.id.item_resPurchaseRecord_users_list)
        var adapter = SimpleStrAdapter()
        adapter.data = item.phone
        var layoutManager = GridLayoutManager(context!!,4)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        adapter.notifyDataSetChanged()
    }
}