package cn.xzj.agent.ui.adapter

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.entity.customer.QiandaoRecordInfo
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import com.channey.utils.FormatUtils
import com.channey.utils.ShapeUtil

class QiandaoRecordAdapter(var context: Context, var list: List<QiandaoRecordInfo>) : RecyclerView.Adapter<QiandaoRecordAdapter.Holder>() {
    private var listener: OnRecyclerViewItemClickListener? = null
    private var onUploadBtnClickListener: OnRecyclerViewItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QiandaoRecordAdapter.Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_qiandao_records, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: QiandaoRecordAdapter.Holder, position: Int) {
        val info = list[position]
        holder.nameTv.text = info.positionName
        holder.amountTv.text = "奖励金额${info.returnFee}元"
        var date = FormatUtils.timeStamp2String(info.signInTime.toLong(), EnumValue.DATE_FORMAT_1)
        holder.dateTv.text = "签到时间: $date"
        holder.locTv.text = "签到门店: ${info.storeName}"
//        if (position == list.size - 1) holder.splitLine.visibility = View.INVISIBLE else holder.splitLine.visibility = View.VISIBLE
        ShapeUtil.setShape(
                holder.uploadCardBtn,
                solidColor = context.resources.getColor(R.color.white),
                radius = context.resources.getDimension(R.dimen.dp_4),
                strokeColor = context.resources.getColor(R.color.green29AC3E)
        )
        holder.uploadCardBtn.setOnClickListener {
            if (onUploadBtnClickListener != null) onUploadBtnClickListener!!.onClick(holder.itemView, position)
        }
        holder.itemView.setOnClickListener {
            if (listener != null) listener!!.onClick(holder.itemView, position)
        }
        //岗位特性标签
        if (info.positionTypes != null) {
            holder.mRecyclerViewFlag.removeAllViews()
            for (tag in info.positionTypes!!){
                val tagView = LayoutInflater.from(context).inflate(R.layout.item_signin_feature_flag, null, false)
                tagView.findViewById<TextView>(R.id.tv_job_flag).text = tag
                holder.mRecyclerViewFlag.addView(tagView)
            }

        }


    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTv = itemView.findViewById<TextView>(R.id.qiandao_records_name_tv)
        var dateTv = itemView.findViewById<TextView>(R.id.qiandao_records_date_tv)
        var locTv = itemView.findViewById<TextView>(R.id.qiandao_records_loc_tv)
        var amountTv = itemView.findViewById<TextView>(R.id.qiandao_records_amount_tv)
        var splitLine = itemView.findViewById<TextView>(R.id.qiandao_records_split_line)
        var mRecyclerViewFlag = itemView.findViewById<LinearLayout>(R.id.recy_job_flag)
        var uploadCardBtn = itemView.findViewById<AppCompatTextView>(R.id.qiandao_records_uploadcard_btn)


    }

    fun setOnRecyclerViewItemClickListener(listener: OnRecyclerViewItemClickListener) {
        this.listener = listener
    }
    fun setOnUploadBtnClickListener(listener: OnRecyclerViewItemClickListener) {
        this.onUploadBtnClickListener = listener
    }
}