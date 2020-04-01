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
import cn.xzj.agent.entity.agentinfo.JiezhanCancelBody
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.JiezhanRecordInfo
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import cn.xzj.agent.iview.IMeetStationRecordsView
import cn.xzj.agent.presenter.JiezhanRecordsPresenter
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.util.CommonUtils
import com.channey.utils.FormatUtils
import kotlinx.android.synthetic.main.fragment_jiezhan_records.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 接站记录
 */
class MeetStationRecordsFragment : MVPBaseFragment<JiezhanRecordsPresenter>(), IMeetStationRecordsView {

    private var mCurrentPage = 1
    private var mTotalCount = 0
    private lateinit var mDetailInfo: CustomerDetailInfo
    override fun context(): Context {
        return context!!
    }

    override fun initLayout(): Int {
        return R.layout.fragment_jiezhan_records
    }

    override fun initParams() {
        mDetailInfo = (activity as CustomerDetailActivity).getDetailInfoForFragment()
    }

    override fun initViews() {
        initRecyclerView()
    }

    override fun initData() {
        loadData()
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

    override fun onRecordsGetSuccess(info: CommonListBody<JiezhanRecordInfo>) {
        mTotalCount = info.totalCount
        if (mCurrentPage == 1) {
            mAdapter.clearData()
        }
        mAdapter.addAll(info.items)
        refreshList()
    }

    override fun onRecordsGetFailure(msg: String) {
        refreshList()
    }

    private fun refreshList() {
        if (mCurrentPage == 1) {
            smartRefreshLayout.finishRefresh()
        } else {
            smartRefreshLayout.finishLoadMore()
        }
        smartRefreshLayout.setNoMoreData(!(mTotalCount > mAdapter.data!!.size))
        mAdapter.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        jiezhan_records_recyclerView_list.adapter = mAdapter
        val layoutManager = LinearLayoutManager(context)
        jiezhan_records_recyclerView_list.layoutManager = layoutManager
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


    private fun getRecords() {
        mPresenter.getCustomerJiezhanRecords(mDetailInfo.userId, mCurrentPage)
    }

    private fun requestCancel(info: JiezhanRecordInfo) {
        val body = JiezhanCancelBody(info.pickUpId)
        mPresenter.jiezhanCancel(body)
    }

    override fun cancelSuccess(info: Boolean) {
        if (info) {
            getRecords()
        }
    }

    override fun cancelFailure(msg: String) {
        showToast(msg)
    }

    override fun loadData() {
        super.loadData()
        getRecords()
    }

    override fun onResume() {
        super.onResume()
        smartRefreshLayout.autoRefresh()
    }

    private var mAdapter = object : QuickAdapter<JiezhanRecordInfo>(R.layout.item_jiezhan_records) {
        override fun convert(holder: BaseHolder, item: JiezhanRecordInfo, position: Int) {
            holder.setText(R.id.jiezhan_records_name_tv, item.positionName)
            val date = FormatUtils.timeStamp2String(item.pickUpTime.toLong(), EnumValue.DATE_FORMAT_1)

            holder.setText(R.id.jiezhan_records_date_tv, "接站时间: $date")
            holder.setText(R.id.jiezhan_records_loc_tv, "接站地点: ${item.pickUpLocation}")
            holder.getView<TextView>(R.id.jiezhan_records_cancel_btn).setOnClickListener {
                requestCancel(item)
                CommonUtils.statistics(context!!, Constants.STATISTICS_customerDetail_pickupRecord_cancelPickup_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            //设置背景色，由小职姐预报名来的(即无入职奖励)有颜色标识
            if (item.operatorType == 1) {
//            holder.itemView.setBackgroundResource(R.color.greenC4E8CA)
                holder.setText(R.id.jiezhan_records_user_type, "预约人: 小职姐")
            } else {
                holder.itemView.setBackgroundResource(R.color.white)
                holder.setText(R.id.jiezhan_records_user_type, "预约人: 用户")
            }
        }

    }

}