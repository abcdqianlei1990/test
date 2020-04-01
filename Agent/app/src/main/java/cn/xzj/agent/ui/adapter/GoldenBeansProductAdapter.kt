package cn.xzj.agent.ui.adapter

import android.support.v7.widget.AppCompatTextView
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.entity.goldenbeans.PurchasableItemInfo
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.CommonListAdapter
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import com.channey.utils.FormatUtils
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils


class GoldenBeansProductAdapter : QuickAdapter<PurchasableItemInfo>(R.layout.item_purchasable_beans) {
    private var listener:QuickAdapter.OnItemClickListener<PurchasableItemInfo>? = null

    override fun convert(holder: BaseHolder, item: PurchasableItemInfo, position: Int) {
        ShapeUtil.setShape(
                holder.itemView,
                radius = context!!.resources.getDimension(R.dimen.dp3),
                solidColor = context!!.resources.getColor(R.color.white)
        )
        holder.setText(R.id.item_beans_productName_tv,"${item.gold} 金豆")
        holder.setText(R.id.item_beans_amount_tv,"￥${StringUtils.doubleFormat(item.price)}")
        var btn = holder.getView<AppCompatTextView>(R.id.item_beans_purchase_btn)
        ShapeUtil.setShape(
                btn,
                radius = context!!.resources.getDimension(R.dimen.dp3),
                solidColor = context!!.resources.getColor(R.color.redFF7731)
        )
        btn.setOnClickListener {
            if (listener!=null) listener!!.onItemClick(holder.itemView,item,position)
        }
    }

    fun setOnPurchaseBtnClickListener(listener:QuickAdapter.OnItemClickListener<PurchasableItemInfo>){
        this.listener = listener
    }
}