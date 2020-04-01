package cn.xzj.agent.ui.adapter

import android.support.v7.widget.AppCompatTextView
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.entity.goldenbeans.GoldenBeansChangeRecordInfo
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import com.channey.utils.FormatUtils

class GoldenBeansChangeRecordAdapter : QuickAdapter<GoldenBeansChangeRecordInfo>(R.layout.item_goldenbeans_bills) {
    override fun convert(holder: BaseHolder, item: GoldenBeansChangeRecordInfo, position: Int) {
        holder.setText(R.id.item_beans_bill_title,item.comment)
        holder.setText(R.id.item_beans_bill_date,FormatUtils.timeStamp2String(item.updateTime.toLong(),"yyyy-MM-dd hh:mm"))
        var amountTv = holder.getView<AppCompatTextView>(R.id.item_beans_bill_amount)
        if (item.gold > 0 ) {
            amountTv.text = "+${item.gold}"
            amountTv.setTextColor(context!!.resources.getColor(R.color.redFF7731))
        } else {
            amountTv.text = "${item.gold}"
            amountTv.setTextColor(context!!.resources.getColor(R.color.green29AC3E))
        }
    }
}