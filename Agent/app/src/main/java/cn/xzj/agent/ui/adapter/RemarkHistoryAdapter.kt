package cn.xzj.agent.ui.adapter

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.entity.customer.RemarkHistoryInfo
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import com.channey.utils.FormatUtils
import com.channey.utils.StringUtils
import java.lang.StringBuilder

class RemarkHistoryAdapter(var context:Context, var list:List<RemarkHistoryInfo>): RecyclerView.Adapter<RemarkHistoryAdapter.Holder>() {
    private var listener:OnRecyclerViewItemClickListener ?= null
    private var situationMap = HashMap<String,String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemarkHistoryAdapter.Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_remark_history,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RemarkHistoryAdapter.Holder, position: Int) {
        val info = list[position]
        holder.nameTv.text = info.operatorName
        holder.communicateTv.text = "沟通方式-${info.getCommunicate()}"
        if (StringUtils.isEmpty(info.comment)){
            holder.contentTv.visibility = View.GONE
            holder.contentTitleTv.visibility = View.GONE
        }else{
            holder.contentTv.visibility = View.VISIBLE
            holder.contentTitleTv.visibility = View.VISIBLE
            holder.contentTv.text = info.comment
        }
        holder.dateTv.text = FormatUtils.timeStamp2String(info.updateTime.toLong(),EnumValue.DATE_FORMAT_1)
        var sb = StringBuilder()
        if (info.communicateSituation == null || info.communicateSituation.isEmpty()){
            holder.situationTv.visibility = View.GONE
            holder.situationTitleTv.visibility = View.GONE
        }else{
            holder.situationTv.visibility = View.VISIBLE
            holder.situationTitleTv.visibility = View.VISIBLE
            if (info.isAppointmentSuccess()){
                sb.append("约好了")
            }else{
                sb.append("没约好，")
                for (i in 0 until info.communicateSituation.size){
                    var s = info.communicateSituation[i]
                    sb.append(situationMap[s])
                    if (i != info.communicateSituation.size - 1){
                        sb.append("/")
                    }
                }
            }
        }
        holder.situationTv.text = sb
        holder.itemView.setOnClickListener {
            if (listener != null) listener!!.onClick(holder.itemView,position)
        }
    }

    class Holder(itemView:View):RecyclerView.ViewHolder(itemView){
        var circle = itemView.findViewById<AppCompatImageView>(R.id.item_remark_history_circle)
        var line = itemView.findViewById<TextView>(R.id.item_remark_history_vertical_line)
        var nameTv = itemView.findViewById<TextView>(R.id.item_remark_history_name_tv)
        var communicateTv = itemView.findViewById<TextView>(R.id.item_remark_history_communicate_tv)
        var contentTv = itemView.findViewById<TextView>(R.id.item_remark_history_content_tv)
        var contentTitleTv = itemView.findViewById<TextView>(R.id.item_remark_history_contentTitle_tv)
        var dateTv = itemView.findViewById<TextView>(R.id.item_remark_history_date_tv)
        var situationTv = itemView.findViewById<TextView>(R.id.item_remark_history_situation_tv)
        var situationTitleTv = itemView.findViewById<TextView>(R.id.item_remark_history_situation_title_tv)
    }

    fun setOnRecyclerViewItemClickListener(listener: OnRecyclerViewItemClickListener){
        this.listener = listener
    }

    fun setSituationMap(situationMap:HashMap<String,String>){
        this.situationMap = situationMap
    }
}