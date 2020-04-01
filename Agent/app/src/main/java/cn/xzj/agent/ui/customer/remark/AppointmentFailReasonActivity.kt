package cn.xzj.agent.ui.customer.remark

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.core.common.QuickActivity
import cn.xzj.agent.entity.common.KeyValue
import cn.xzj.agent.entity.customer.MarkJsonInfo
import cn.xzj.agent.util.Util
import com.alibaba.fastjson.JSON
import com.channey.utils.ShapeUtil
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.activity_appointment_fail_reason.*


class AppointmentFailReasonActivity:QuickActivity() {
    private var mReasons = ArrayList<KeyValue>()
    companion object {
        val key_selected_reason = "selected_reason"
        fun jumpForResult(activity: Activity, requestCode:Int = Code.RequestCode.AppointmentFailReason){
            var intent = Intent(activity, AppointmentFailReasonActivity::class.java)
            activity.startActivityForResult(intent,requestCode)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_appointment_fail_reason
    }

    override fun initParams() {
        super.initParams()
        val json = Util.getJsonFromAssets(this, "mark.json")
        val obj = JSON.parseObject(json, MarkJsonInfo::class.java)
        for (reason in obj.reason){
            mReasons.add(reason)
        }
    }

    override fun initViews() {
        setTitle("没约好原因")
        activity_appointmentfail_flowlayout.adapter = object : TagAdapter<KeyValue>(mReasons) {
            override fun getView(parent: FlowLayout, position: Int, o: KeyValue): View {
                val tv = LayoutInflater.from(this@AppointmentFailReasonActivity).inflate(R.layout.item_appointment_fail_reason,
                        activity_appointmentfail_flowlayout, false) as AppCompatTextView
                tv.text = o.value
                setUnSelectedStyle(tv)
                return tv
            }

            override fun onSelected(position: Int, view: View) {
                setSelectedStyle(view)
            }

            override fun unSelected(position: Int, view: View) {
                setUnSelectedStyle(view)
            }

            private fun setSelectedStyle(view: View){
                var tv = view as AppCompatTextView
                tv.setTextColor(resources.getColor(R.color.green29AC3E))
                var radius = resources.getDimension(R.dimen.dp_4)
                var strokeWidth = resources.getDimension(R.dimen.dp_1)
                var strokeColor = resources.getColor(R.color.green29AC3E)
                var solidColor = resources.getColor(R.color.greenF5FFF6)
                ShapeUtil.setShape(view,radius = radius,strokeWidth = strokeWidth.toInt(),strokeColor = strokeColor,solidColor = solidColor)
            }

            private fun setUnSelectedStyle(view: View){
                var tv = view as AppCompatTextView
                tv.setTextColor(resources.getColor(R.color.black333333))
                var radius = resources.getDimension(R.dimen.dp_4)
                var strokeWidth = resources.getDimension(R.dimen.dp_1)
                var strokeColor = resources.getColor(R.color.blackE0E0E0)
                var solidColor = resources.getColor(R.color.white)
                ShapeUtil.setShape(view,radius = radius,strokeWidth = strokeWidth.toInt(),strokeColor = strokeColor,solidColor = solidColor)
            }
        }
    }

    override fun setListeners() {
        activity_appointmentfail_submit_btn.setOnClickListener {
            var selectedList = activity_appointmentfail_flowlayout.selectedList
            if (selectedList.isEmpty()){
                showToast("请选择原因")
            }else{
                var l = ArrayList<Int>()
                for (i in selectedList){
                    l.add(i)
                }
                var intent = Intent()
                intent.putExtra(key_selected_reason,l)
                setResult(Code.ResultCode.OK,intent)
                finish()
            }
        }
    }
}