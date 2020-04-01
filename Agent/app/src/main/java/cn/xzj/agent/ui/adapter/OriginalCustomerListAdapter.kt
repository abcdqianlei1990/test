package cn.xzj.agent.ui.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.entity.customer.OriginalCustomerInfo
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import com.channey.utils.FormatUtils
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils

class OriginalCustomerListAdapter(var context: Context, var list: List<OriginalCustomerInfo>) : RecyclerView.Adapter<OriginalCustomerListAdapter.Holder>() {
    private var listener: OnRecyclerViewItemClickListener? = null
    private var phonecallClicklistener: OnRecyclerViewItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OriginalCustomerListAdapter.Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_customer_list01, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OriginalCustomerListAdapter.Holder, position: Int) {
        val info = list[position]
        var nickname = ""
        if (!StringUtils.isEmpty(info.nickname)) {
            nickname = info.nickname
        }
        var userName = ""
        if (!StringUtils.isEmpty(info!!.userName)) {
            userName = "(${info!!.userName})"
        }
        var str = String.format(context.resources.getString(R.string.user_base_info), info.phone, nickname, userName)
        val spanned = Html.fromHtml(str)
        holder.baseInfoTv.text = spanned
        //意向
        var solidColor = context.resources.getColor(R.color.redF83232)
        var radius = context.resources.getDimension(R.dimen.dp_12)
        holder.intentLvTv.visibility = View.VISIBLE
        var intentStr = ""
        when(info.wish){
            OriginalCustomerInfo.INTENT_EXTREMELY_HIGH -> {
                solidColor = context.resources.getColor(R.color.redF83232)
                intentStr = "意向：极高"
            }
            OriginalCustomerInfo.INTENT_HIGH -> {
                solidColor = context.resources.getColor(R.color.yellowFF5800)
                intentStr = "意向：高"
            }
            OriginalCustomerInfo.INTENT_MEDIUM -> {
                solidColor = context.resources.getColor(R.color.yellowFF8500)
                intentStr = "意向：中"
            }
            OriginalCustomerInfo.INTENT_LOW -> {
                solidColor = context.resources.getColor(R.color.yellowFFB600)
                intentStr = "意向：低"
            }
            OriginalCustomerInfo.INTENT_NONE -> {
                solidColor = context.resources.getColor(R.color.green90D600)
                intentStr = "意向：无"
            }
            else -> holder.intentLvTv.visibility = View.GONE
        }
        ShapeUtil.setShape(holder.intentLvTv,solidColor = solidColor,radius = radius)
        holder.intentLvTv.text = intentStr

        //客户等级
        if (StringUtils.isEmpty(info.rank)) {
            holder.levelImg.visibility = View.GONE
        } else {
            holder.levelImg.visibility = View.VISIBLE
            when (info.rank) {
                EnumValue.CUSTOMER_LEVEL_A -> holder.levelImg.setImageResource(R.mipmap.ic_customer_level_a)
                EnumValue.CUSTOMER_LEVEL_B -> holder.levelImg.setImageResource(R.mipmap.ic_customer_level_b)
                else -> holder.levelImg.visibility = View.GONE
            }
        }
        //分配时间
        if (!TextUtils.isEmpty(info.matchTime)) {
            try {
                holder.distributionTime.text = FormatUtils.timeStamp2String(info.matchTime!!.toLong(), EnumValue.DATE_FORMAT_1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        holder.registinfoTv.text = FormatUtils.timeStamp2String(info.registerTime.toLong(), EnumValue.DATE_FORMAT_1) + " ${info.sourceName}(${info.source})"
        if (StringUtils.isEmpty(info.lastContactComment)) {
            holder.noticeTitleTv.visibility = View.GONE
            holder.noticeTv.visibility = View.GONE
        } else {
            holder.noticeTitleTv.visibility = View.VISIBLE
            holder.noticeTv.visibility = View.VISIBLE
            try {
                holder.noticeTv.text = FormatUtils.timeStamp2String(info.lastContactTime.toLong(), EnumValue.DATE_FORMAT_1) + " " + info.lastContactComment

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //微信号
        if (StringUtils.isEmpty(info.weixin)) {
            holder.wechatTitleTv.visibility = View.GONE
            holder.wechatTv.visibility = View.GONE
        } else {
            holder.wechatTitleTv.visibility = View.VISIBLE
            holder.wechatTv.visibility = View.VISIBLE
            holder.wechatTv.text = info.weixin
        }

        holder.phonecallBtn.setOnClickListener {
            if (phonecallClicklistener != null) phonecallClicklistener!!.onClick(holder.itemView, position)
        }
        holder.itemView.setOnClickListener {
            if (listener != null) listener!!.onClick(holder.itemView, position)
        }
    }


    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var baseInfoTv = itemView.findViewById<TextView>(R.id.item_customer_baseinfo_tv)
        var intentLvTv = itemView.findViewById<TextView>(R.id.item_customer_intent_tv)
        var levelImg = itemView.findViewById<AppCompatImageView>(R.id.item_customer_level_img)
        var phonecallBtn = itemView.findViewById<TextView>(R.id.item_customer_phonecall_btn)
        var registinfoTv = itemView.findViewById<TextView>(R.id.item_customer_registinfo_tv)
        var noticeTitleTv = itemView.findViewById<TextView>(R.id.item_customer_notice_title_tv)
        var noticeTv = itemView.findViewById<TextView>(R.id.item_customer_notice_tv)
        var wechatTitleTv = itemView.findViewById<TextView>(R.id.item_customer_wechat_title_tv)
        var wechatTv = itemView.findViewById<TextView>(R.id.item_customer_wechat_tv)
        var distributionTime = itemView.findViewById<TextView>(R.id.item_customer_distribution_tv)
    }

    fun setOnRecyclerViewItemClickListener(listener: OnRecyclerViewItemClickListener) {
        this.listener = listener
    }

    fun setOnPhoneCallClickListener(listener: OnRecyclerViewItemClickListener) {
        this.phonecallClicklistener = listener
    }
}