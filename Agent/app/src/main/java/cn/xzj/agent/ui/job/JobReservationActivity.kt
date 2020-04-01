package cn.xzj.agent.ui.job

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Html
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.QuickActivity
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.DefaultEventMessage
import cn.xzj.agent.entity.job.PickLocationVO
import cn.xzj.agent.entity.job.ReservationDTO
import cn.xzj.agent.iview.IReservationJobView
import cn.xzj.agent.presenter.ReservationJobPresenter
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.LogLevel
import cn.xzj.agent.widget.CommonDialog
import cn.xzj.agent.widget.SimpleToast
import com.aigestudio.wheelpicker.WheelPicker
import com.channey.utils.FormatUtils
import kotlinx.android.synthetic.main.activity_job_reservation.*
import org.greenrobot.eventbus.EventBus


/**
 * @Author yemao
 * @Email yrmao9893@163.com
 * @Date 2018/8/15
 * @Des 预约页面
 */
class JobReservationActivity : QuickActivity(), View.OnClickListener, IReservationJobView {
    private lateinit var reservationJobPresenter: ReservationJobPresenter
    private var mInterviewDateVOs: List<Long>? = null
    private var mPickLocationVOs: List<PickLocationVO>? = null
    private var mCurrentInterviewDateVO: Long? = null
    private var mCurrentPickLocationVO: PickLocationVO? = null
    private var mInterviewDateSelectedItemPosition: Int = 0
    private var mPickLocationSelectedItemPosition: Int = 0
    private lateinit var positionId: String
    private var recruitmentNeedsId: String? = null
    private var applyId: String? = null //处理申请id
    private var positionName: String? = null
    private var userId: String? = null
    private var userName: String? = null
    private var tollDescription: String? = null//收费描述，有可能没有需从岗位详情传入
    private var isNeedPreReserve: Boolean = false //是否需要预报名


    companion object {
        const val IS_NEED_PRORESERVE = "isNeedPreReserve"
        fun jump(context: Context, positionId: String, recruitmentNeedsId: String? = null
                 , applyId: String?, positionName: String?, userId: String?, userName: String? = null, tollDescription: String? = null, isNeedPreReserve: Boolean = false) {
            val mIntent = Intent()
            mIntent.setClass(context, JobReservationActivity::class.java)
            mIntent.putExtra(Keys.DATA_KEY_1, positionId)
            mIntent.putExtra(Keys.DATA_KEY_2, recruitmentNeedsId)
            mIntent.putExtra(Keys.DATA_KEY_3, applyId)
            mIntent.putExtra(Keys.DATA_KEY_4, positionName)
            mIntent.putExtra(Keys.DATA_KEY_5, userId)
            mIntent.putExtra(Keys.DATA_KEY_6, userName)
            mIntent.putExtra(Keys.DATA_KEY_7, tollDescription)
            mIntent.putExtra(IS_NEED_PRORESERVE, isNeedPreReserve)
            context.startActivity(mIntent)
        }
    }

    override fun getContext(): Context {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_job_reservation
    }

    override fun initParams() {
        positionId = intent.getStringExtra(Keys.DATA_KEY_1)
        recruitmentNeedsId = intent.getStringExtra(Keys.DATA_KEY_2)
        applyId = intent.getStringExtra(Keys.DATA_KEY_3)
        positionName = intent.getStringExtra(Keys.DATA_KEY_4)
        userId = intent.getStringExtra(Keys.DATA_KEY_5)
        userName = intent.getStringExtra(Keys.DATA_KEY_6)
        tollDescription = intent.getStringExtra(Keys.DATA_KEY_7)
        isNeedPreReserve = intent.getBooleanExtra(IS_NEED_PRORESERVE, false)
    }

    override fun initViews() {
        setTitle("预约岗位")
        setLifeBack()
        reservationJobPresenter = ReservationJobPresenter(this)
        if (isNeedPreReserve) {
            //需要预报名
            tv_activity_job_reservation_ready_apply_detail.visibility = View.VISIBLE
        } else {
            tv_activity_job_reservation_ready_apply_detail.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        reservationJobPresenter.getReservationLocations(positionId)
        CommonUtils.statistics(this, Constants.STATISTICS_RESERVE_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }


    override fun setListeners() {
        super.setListeners()
        btn_commit.setOnClickListener(this)
        btn_date.setOnClickListener(this)
        btn_address.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btn_commit -> {

                if (mCurrentPickLocationVO == null) {
                    SimpleToast.showShort(tv_address.hint.toString())
                    return
                }
                if (mCurrentInterviewDateVO == null) {
                    SimpleToast.showShort(tv_date.hint.toString())
                    return
                }
                showCommitInfoDialog()

            }
            R.id.btn_date -> {
                if (mCurrentPickLocationVO == null) {
                    SimpleToast.showShort(tv_address.hint.toString())
                    return
                }
                if (mInterviewDateVOs == null) {
                    reservationJobPresenter.getInterviewDate(positionId, mCurrentPickLocationVO!!.address)
                } else {
                    showDatePicker()
                }
            }
            R.id.btn_address -> {
                if (mPickLocationVOs == null) {
                    reservationJobPresenter.getReservationLocations(positionId)
                } else {
                    showReservationLocationPicker()
                }

            }
        }
    }

    override fun getReservationDate(data: BaseResponseInfo<List<Long>>) {
        mInterviewDateVOs = data.content
        if (mInterviewDateVOs != null) {
            showDatePicker()
        } else {
            SimpleToast.showShort("此岗位暂未配置面试时间")
        }
    }

    override fun getReservationLocations(data: BaseResponseInfo<List<PickLocationVO>>) {
        mPickLocationVOs = data.content
        if (mPickLocationVOs != null) {
            if (mPickLocationVOs!!.isEmpty())
                SimpleToast.showShort("此岗位暂未配置接站地址")
        }
    }

    override fun jobReservationSuccess(data: BaseResponseInfo<Any>) {
        SimpleToast.showNormal("预约成功")
        EventBus.getDefault().post(DefaultEventMessage())
        finish()
    }

    override fun jobReservationError(e: Throwable?, isNetWorkError: Boolean) {
    }


    @SuppressLint("InflateParams")
    private fun showDatePicker() {
        var mView = LayoutInflater.from(this).inflate(R.layout.view_select_service_center, null)
        var wheel = mView.findViewById<WheelPicker>(R.id.wheel)
        val mWheelData = ArrayList<String>()
        for (item in mInterviewDateVOs!!) {
            mWheelData.add(FormatUtils.timeStamp2String(item, EnumValue.DATA_FORMAT_5))
        }
        wheel.data = mWheelData
        wheel.selectedItemPosition = mInterviewDateSelectedItemPosition
        wheel.setOnItemSelectedListener { picker, data, position ->
            mInterviewDateSelectedItemPosition = position
        }
        CommonDialog.newBuilder(this)
                .setTitle("选择时间")
                .setView(mView)
                .setNegativeButton(resources.getString(R.string.cancel)) {
                    mView = null
                    wheel = null
                }
                .setPositiveButton(resources.getString(R.string.confirm)) { dialog ->
                    dialog.cancel()
                    if (mInterviewDateVOs!!.isEmpty()) {
                        return@setPositiveButton
                    }
                    LogLevel.w("选择的日期", "$mInterviewDateSelectedItemPosition")
                    mCurrentInterviewDateVO = mInterviewDateVOs!![mInterviewDateSelectedItemPosition]
                    tv_date.text = FormatUtils.timeStamp2String(mCurrentInterviewDateVO!!, EnumValue.DATA_FORMAT_5)
                    mView = null
                    wheel = null
                }
                .setCancelable(true)
                .create()
                .showCenter()
                .setBottomShow()

    }

    @SuppressLint("InflateParams")
    private fun showReservationLocationPicker() {
        var mView = LayoutInflater.from(this).inflate(R.layout.view_select_service_center, null)
        var wheel = mView.findViewById<WheelPicker>(R.id.wheel)
        val mWheelData = ArrayList<String>()
        for (item in mPickLocationVOs!!) {
            mWheelData.add(item.location)
        }
        wheel.data = mWheelData
        wheel.selectedItemPosition = mPickLocationSelectedItemPosition
        wheel.setOnItemSelectedListener { picker, data, position ->
            mPickLocationSelectedItemPosition = position
        }
        CommonDialog.newBuilder(this)
                .setTitle("选择地点")
                .setView(mView)
                .setNegativeButton(resources.getString(R.string.cancel)) {
                    mView = null
                    wheel = null
                }
                .setPositiveButton(resources.getString(R.string.confirm)) { dialog ->
                    dialog.cancel()
                    if (mPickLocationVOs!!.isEmpty()) {
                        return@setPositiveButton
                    }
                    LogLevel.w("选择的地点", "$mPickLocationSelectedItemPosition")
                    //地点改变时时间需要清空
                    if (tv_address.text.toString() != mPickLocationVOs!![mPickLocationSelectedItemPosition].location) {
                        mCurrentInterviewDateVO = null
                        mInterviewDateVOs = null
                        tv_date.text = ""
                        mInterviewDateSelectedItemPosition = 0
                    }

                    mCurrentPickLocationVO = mPickLocationVOs!![mPickLocationSelectedItemPosition]
                    tv_address.text = mCurrentPickLocationVO!!.location
                    tv_detail_address.text = mCurrentPickLocationVO!!.address
                    if (tv_detail_address.visibility == View.GONE) {
                        tv_detail_address.visibility = View.VISIBLE
                    }
                    mView = null
                    wheel = null

                }
                .setCancelable(true)
                .create()
                .showCenter()
                .setBottomShow()

    }

    private fun showCommitInfoDialog() {
        val msg = StringBuilder()
        msg.append("预约 ")
        msg.append(userName)
        msg.append(" ")
        msg.append(tv_date.text.toString())
        msg.append(" 到达 ")
        msg.append(mCurrentPickLocationVO!!.location)
        msg.append(",详细地址: ")
        msg.append(mCurrentPickLocationVO!!.address)
        if (!TextUtils.isEmpty(tollDescription)) {
            msg.append("<br/><font color=\"#FF0000\">$tollDescription</font>")

        }
        CommonDialog.newBuilder(this)
                .setMessage(Html.fromHtml(msg.toString()))
                .setContentGravity(Gravity.LEFT)
                .setCancelable(false)
                .setPositiveButton(resources.getString(R.string.confirm)) { dialog: CommonDialog ->
                    dialog.cancel()
                    val reservationDTO = ReservationDTO()
                    reservationDTO.applyId = applyId
                    reservationDTO.latitude = mCurrentPickLocationVO!!.latitude
                    reservationDTO.longitude = mCurrentPickLocationVO!!.longitude
                    reservationDTO.pickUpLocation = mCurrentPickLocationVO!!.location
                    reservationDTO.pickUpAddress = mCurrentPickLocationVO!!.address
                    reservationDTO.pickUpTime = mCurrentInterviewDateVO.toString()
                    reservationDTO.positionId = positionId
                    reservationDTO.positionName = positionName
                    reservationDTO.source = Constants.SOURCE
                    reservationDTO.userId = userId
                    reservationJobPresenter.commitJobReservation(reservationDTO)
                    CommonUtils.statistics(this, Constants.STATISTICS_RESERVE_SUBMIT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)

                }.setNegativeButton(resources.getString(R.string.cancel), null)
                .create()
                .show()
    }

    override fun onDefaultEvent(event: DefaultEventMessage) {
    }

    override fun onDestroy() {
        super.onDestroy()
        reservationJobPresenter.detachView()
    }

}
