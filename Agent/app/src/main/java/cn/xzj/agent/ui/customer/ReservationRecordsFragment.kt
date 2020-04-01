package cn.xzj.agent.ui.customer

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.DefaultEventMessage
import cn.xzj.agent.entity.RefreshCustomerRecordesMessage
import cn.xzj.agent.entity.agentinfo.YuyueCancelBody
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.YuyueRecordInfo
import cn.xzj.agent.iview.IYuyueRecordsView
import cn.xzj.agent.presenter.ReservationRecordsPresenter
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.util.CommonUtils
import com.channey.utils.FormatUtils
import kotlinx.android.synthetic.main.fragment_yuyue_records.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 预约记录
 */
class ReservationRecordsFragment : MVPBaseFragment<ReservationRecordsPresenter>(), IYuyueRecordsView {


    private var mCurrentPage = 1
    private var mTotalCount = 0
    private lateinit var mDetailInfo: CustomerDetailInfo
    override fun context(): Context {
        return context!!
    }

    override fun initLayout(): Int {
        return R.layout.fragment_yuyue_records
    }

    override fun initParams() {
        mDetailInfo = (activity as CustomerDetailActivity).getDetailInfoForFragment()
    }

    override fun initViews() {
        initRecyclerView()
    }

    override fun initData() {
        getRecords()
    }

    override fun loadData() {
        super.loadData()
        getRecords()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDefaultEvent(event: RefreshCustomerRecordesMessage) {
        loadData()
    }

    override fun onRecordsGetSuccess(info: CommonListBody<YuyueRecordInfo>) {
        mTotalCount = info.totalCount
        if (mCurrentPage == 1) {
            mAdapter.setNewData(info.items)
        } else {
            mAdapter.addAll(info.items)
            mAdapter.notifyDataSetChanged()
        }

        loadingComplete()
    }

    override fun onRecordsGetFailure(msg: String) {
        loadingComplete()
    }


    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter=mAdapter
        smartRefreshLayout.isEnableLoadMore = true
        smartRefreshLayout.setOnRefreshListener {
            mCurrentPage = 1
            getRecords()
        }
        smartRefreshLayout.setOnLoadMoreListener {
            mCurrentPage++
            getRecords()
        }
    }


    private fun loadingComplete() {
        if (mCurrentPage <= 1) {
            smartRefreshLayout.finishRefresh()
        } else {
            smartRefreshLayout.finishLoadMore()
        }
        smartRefreshLayout.setNoMoreData(mTotalCount <= mAdapter.data!!.size)
    }


    private fun getRecords() {
        mPresenter.getCustomerYuyueRecords(mDetailInfo.userId, mCurrentPage)
    }

    private fun requestCancel(info: YuyueRecordInfo) {
        val body = YuyueCancelBody(info.reserveId)
        mPresenter.yuyueCancel(body)
    }

    override fun cancelSuccess(info: Boolean) {
        if (info) {
            getRecords()
        }
    }

    override fun cancelFailure(msg: String) {
        showToast(msg)
    }

    var mAdapter = object : QuickAdapter<YuyueRecordInfo>(R.layout.item_yuyue_records) {
        override fun convert(holder: BaseHolder, item: YuyueRecordInfo, position: Int) {

            val date = FormatUtils.timeStamp2String(item.reserveDate.toLong(), EnumValue.DATE_FORMAT_1)
            holder.setText(R.id.yuyue_records_mendian_tv, "${item.signInLocation} $date")
            holder.setText(R.id.yuyue_records_name_tv, item.positionName)


            holder.getView<TextView>(R.id.yuyue_records_cancel_btn).setOnClickListener {
                requestCancel(item)
                CommonUtils.statistics(context!!, Constants.STATISTICS_customerDetail_customerDetail_reserveRecord_cancelReserve_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)

            }
            //支付状态
            when(item.paymentStatus){
                EnumValue.YUYUE_PAY_STATUS_NO_PAY->{
                    holder.setText(R.id.yuyue_records_pay_status_tv,"支付状态: 未支付")
                }
                EnumValue.YUYUE_PAY_STATUS_PAYING->{
                    holder.setText(R.id.yuyue_records_pay_status_tv,"支付状态: 支付中")
                }
                EnumValue.YUYUE_PAY_STATUS_PAYED->{
                    holder.setText(R.id.yuyue_records_pay_status_tv,"支付状态: 已支付")
                }
                EnumValue.YUYUE_PAY_STATUS_CANCEL->{
                    holder.setText(R.id.yuyue_records_pay_status_tv,"支付状态: 已取消")
                }
                null->{
                    holder.setText(R.id.yuyue_records_pay_status_tv,"支付状态: --")
                }
            }

            //退款状态
            when(item.refundStatus){
                EnumValue.YUYUE_REFUND_STATUS_NO_REFUND->{
                    holder.setText(R.id.yuyue_records_tuikuan_tv,"退款状态: 未退款")
                }
                EnumValue.YUYUE_REFUND_STATUS_REFUNDING->{
                    holder.setText(R.id.yuyue_records_tuikuan_tv,"退款状态: 退款中")
                }
                EnumValue.YUYUE_REFUND_STATUS_REFUNDED->{
                    holder.setText(R.id.yuyue_records_tuikuan_tv,"退款状态: 已退款")
                }
                null->{
                    holder.setText(R.id.yuyue_records_tuikuan_tv,"退款状态: --")
                }
            }
            //预约状态
            when(item.status){
                EnumValue.YUYUE_STATUS_CANCEL, EnumValue.YUYUE_STATUS_CANCEL_10,EnumValue.YUYUE_STATUS_CANCEL_20,
                        EnumValue.YUYUE_STATUS_CANCEL_30->{
                    holder.setText(R.id.tv_yuyue_status,"已取消")
                    holder.getView<TextView>(R.id.yuyue_records_cancel_btn).visibility=View.INVISIBLE
                }
                EnumValue.YUYUE_STATUS_SINGNED->{
                    holder.setText(R.id.tv_yuyue_status,"已签到")
                    holder.getView<TextView>(R.id.yuyue_records_cancel_btn).visibility=View.VISIBLE

                }
                EnumValue.YUYUE_STATUS_UNSIGNIN->{
                    holder.setText(R.id.tv_yuyue_status,"未签到")
                    holder.getView<TextView>(R.id.yuyue_records_cancel_btn).visibility=View.VISIBLE
                }
            }


            if (item.operatorType == 1) {
                holder.itemView.setBackgroundResource(R.color.greenC4E8CA)
                holder.setText(R.id.yuyue_records_remark_tv, "预约人: 小职姐")
                //设置背景色，由小职姐预报名来的(即无入职奖励)有颜色标识
                //设置背景色，标识可预报名 无返费代表可预报名
                try {
                    if (!item.configuredReturnFee) {
                        holder.itemView.setBackgroundResource(R.color.greenC4E8CA)
                    } else {
                        holder.itemView.setBackgroundResource(R.color.white)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                holder.itemView.setBackgroundResource(R.color.white)
                holder.setText(R.id.yuyue_records_remark_tv, "预约人: 用户")
            }

        }

    }

}