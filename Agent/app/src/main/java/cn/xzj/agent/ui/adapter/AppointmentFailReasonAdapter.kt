package cn.xzj.agent.ui.adapter

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.entity.common.KeyValue
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter

class AppointmentFailReasonAdapter : QuickAdapter<KeyValue>(R.layout.item_appointment_fail_reason2) {
    private var select:String? = null
    override fun convert(holder: BaseHolder, item: KeyValue, position: Int) {
        var contentTv = holder.getView<AppCompatTextView>(R.id.item_appointment_fail_tv)
        var delBtn = holder.getView<AppCompatImageView>(R.id.item_appointment_fail_del)
        contentTv.text = item.value
    }
}