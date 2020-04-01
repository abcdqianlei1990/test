package cn.xzj.agent.ui.adapter

import android.support.v7.widget.AppCompatTextView
import cn.xzj.agent.R
import cn.xzj.agent.entity.customer.WorkingRecordInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import com.channey.utils.FormatUtils

class WorkingRecordListAdapter : QuickAdapter<WorkingRecordInfo>(R.layout.item_workingrecord_list) {
    override fun convert(holder: BaseHolder, item: WorkingRecordInfo, position: Int) {
        holder.setText(R.id.item_workingrecord_positionName_tv, item.positionName)
        holder.setText(R.id.item_workingrecord_entryDate_tv, FormatUtils.timeStamp2String(item.onboardingTime,"yyyy年MM月dd日"))
        var quitTimeStr:String
        if (item.quitTime == null || item.quitTime <= 0){
            quitTimeStr = "--"
        }else{
            quitTimeStr = FormatUtils.timeStamp2String(item.quitTime,"yyyy年MM月dd日")
        }
        holder.setText(R.id.item_workingrecord_quitDate_tv, quitTimeStr)
        var deleteBtn = holder.getView<AppCompatTextView>(R.id.item_workingrecord_del_btn)
        deleteBtn.setOnClickListener { v -> listener.onItemClick(v!!,item,position) }
    }

    private lateinit var listener: OnItemClickListener<WorkingRecordInfo>
    fun setDeleteBtnOnClickListener(listener: OnItemClickListener<WorkingRecordInfo>){
        this.listener = listener
    }
}