package cn.xzj.agent.ui.customer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.agentinfo.NoticeSettingBody
import cn.xzj.agent.iview.INoticeSettingView
import cn.xzj.agent.presenter.NoticeSettingPresenter
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.widget.CommonDialog
import cn.xzj.agent.widget.SimpleToast
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.channey.utils.FormatUtils
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.activity_notice_setting.*
import java.util.*

/**
 * 设置跟踪提醒
 */
class NoticeSettingActivity : MVPBaseActivity<NoticeSettingPresenter>(), View.OnClickListener, INoticeSettingView {
    override fun context(): Context {
        return this
    }

    private var mUserId: String? = null
    private lateinit var pvTime: TimePickerView

    companion object {
        fun jumpForResult(activity: Activity, userId: String, requestCode: Int) {
            val intent = Intent(activity, NoticeSettingActivity::class.java)
            intent.putExtra(Keys.USER_ID, userId)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_notice_setting
    }

    override fun initParams() {
        mUserId = intent.getStringExtra(Keys.USER_ID)
    }

    override fun initViews() {
        setTitle("设置跟踪提醒")
        setLifeBack()
        initDatePickerPopWindow()

    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_returnVisit_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    override fun setListeners() {
        activity_notice_setting_date_tv.setOnClickListener(this)
        activity_notice_setting_right_arrow.setOnClickListener(this)
        activity_notice_setting_submit_btn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.activity_notice_setting_date_tv, R.id.activity_notice_setting_right_arrow -> {
                CommonUtils.closeKeyBoard(this)
                pvTime.show(v)
            }
            R.id.activity_notice_setting_submit_btn -> {
                val date = activity_notice_setting_date_tv.text.toString()
                if (StringUtils.isEmpty(date)) {
                    SimpleToast.showNormal("请选择跟踪时间")
                    return
                }
                val notice = activity_notice_setting_remark_ed.text.toString()
                if (StringUtils.isEmpty(notice)) {
                    SimpleToast.showNormal("请设置提醒内容")
                    return
                }
                showNoticeDialog()
            }
        }
    }


    private fun addNotice() {
        if (mUserId != null) {
            val date = activity_notice_setting_date_tv.text.toString()
            val notice = activity_notice_setting_remark_ed.text.toString()
            val stamp = FormatUtils.string2TimeStamp(EnumValue.DATE_FORMAT_DATE, date)
            val body = NoticeSettingBody(notice, stamp.toString(), mUserId!!)
            mPresenter.addNotice(body)
            CommonUtils.statistics(this, Constants.STATISTICS_returnVisit_SUBMIT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
        } else {
            SimpleToast.showNormal("未获取到用户id，请重新进入该页面")
        }
    }

    override fun onSettingSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            SimpleToast.showNormal("提醒设置成功")
            val notice = activity_notice_setting_remark_ed.text.toString()
        } else {

        }
    }

    override fun onRemarkSuccess(isSuccess: Boolean) {
//        if (isSuccess) {
//            SimpleToast.showNormal("注记成功")
        setResult(Code.ResultCode.OK)
        finish()
//        }
    }

    override fun onRemarkFailure(msg: String) {
        setResult(Code.ResultCode.OK)
        finish()
    }

    override fun onSettingFailure() {
    }

    private fun showNoticeDialog() {
        CommonDialog.newBuilder(this)
                .setMessage("确认设置跟踪提醒？")
                .setPositiveButton("确定") { dialog ->
                    dialog!!.cancel()
                    addNotice()
                }
                .setNegativeButton("取消", null)
                .create()
                .show()

    }

    private fun initDatePickerPopWindow() {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        endDate.set(2050, 12, 31)

        pvTime = TimePickerBuilder(this) { date, _ ->
            if (date.time <= System.currentTimeMillis()) {
                SimpleToast.showNormal("提醒时间设置错误")
            } else {
                activity_notice_setting_date_tv.text = FormatUtils.timeStamp2String(date.time, EnumValue.DATE_FORMAT_DATE)
            }

        }.setLabel("年", "月", "日", "时", "分", "秒")
                .setType(booleanArrayOf(true, true, true, true, true, false))
                .setRangDate(startDate, endDate)
                .setDividerColor(this.resources.getColor(R.color.green29AC3E))
                .setTextColorCenter(this.resources.getColor(R.color.green29AC3E)) //设置选中项文字颜色
                .setSubmitColor(this.resources.getColor(R.color.green29AC3E))
                .setCancelColor(this.resources.getColor(R.color.green29AC3E))
                .setTitleColor(this.resources.getColor(R.color.green29AC3E))
                .build()
    }
}