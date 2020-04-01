package cn.xzj.agent.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.customer.WorkingRecordPostBody
import cn.xzj.agent.i.DateSelectedListener
import cn.xzj.agent.iview.IAddWorkingRecordView
import cn.xzj.agent.presenter.AddWorkingRecordPresenter
import com.channey.utils.FormatUtils
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.activity_add_workingrecord.*

class AddWorkingRecordActivity:MVPBaseActivity<AddWorkingRecordPresenter>(), View.OnClickListener,IAddWorkingRecordView {

    private var mUserId:String? = null
    companion object {
        val key_userId = "user_id"
        fun jump(context: Activity,userId:String) {
            val intent = Intent(context, AddWorkingRecordActivity::class.java)
            intent.putExtra(key_userId,userId)
            context.startActivityForResult(intent,Code.RequestCode.AddWorkingRecordActivity)
        }
    }

    override fun initParams() {
        mUserId = intent.getStringExtra(key_userId)
    }
    
    override fun getLayoutId(): Int {
        return R.layout.activity_add_workingrecord
    }

    override fun context(): Context {
        return this
    }

    override fun initViews() {
        setTitle("添加工作记录")
    }

    override fun setListeners() {
        activity_add_workingrecord_entryDate_arrow.setOnClickListener(this)
        activity_add_workingrecord_quitDate_arrow.setOnClickListener(this)
        activity_add_workingrecord_btn.setOnClickListener(this)
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.activity_add_workingrecord_entryDate_arrow -> {
                showDatePickerPopWindow(listener = object : DateSelectedListener {
                    override fun onSelected(year: String, month: String, day: String, hour: String, min: String) {
                        activity_add_workingrecord_entryDate_ed.text = "$year$month$day"
                    }
                })
            }
            R.id.activity_add_workingrecord_quitDate_arrow -> {
                showDatePickerPopWindow(listener = object : DateSelectedListener {
                    override fun onSelected(year: String, month: String, day: String, hour: String, min: String) {
                        activity_add_workingrecord_quitDate_ed.text = "$year$month$day"
                    }
                })
            }
            R.id.activity_add_workingrecord_btn -> {
                var company = activity_add_workingrecord_company_ed.text.toString()
                if (StringUtils.isEmpty(company)){
                    showToast("请输入入职企业岗位")
                    return
                }
                var entryDate = activity_add_workingrecord_entryDate_ed.text.toString()
                if (StringUtils.isEmpty(entryDate)){
                    showToast("请选择入职时间")
                    return
                }
                var quitDate:String? = activity_add_workingrecord_quitDate_ed.text.toString()
                if (StringUtils.isEmpty(quitDate)){
                    quitDate = null
                }else{
                    quitDate = FormatUtils.string2TimeStamp("yyyy年MM月dd日",quitDate!!).toString()
                }
                if (StringUtils.isNotEmpty(mUserId)){
                    var body = WorkingRecordPostBody(
                            userId = mUserId!!,
                            positionName = company,
                            onboardingTime = FormatUtils.string2TimeStamp("yyyy年MM月dd日",entryDate).toString(),
                            quitTime = quitDate
                    )   
                    mPresenter.addWorkingRecord(body)
                }
            }
        }
    }

    override fun onAddWorkingRecordSuccess() {
        showToast("添加成功")
        activity_add_workingrecord_company_ed.text = null
        activity_add_workingrecord_entryDate_ed.text = null
        activity_add_workingrecord_quitDate_ed.text = null
    }

    override fun onAddWorkingRecordFailure() {

    }
}