package cn.xzj.agent.ui.customer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.RefreshCustomerRecordesMessage
import cn.xzj.agent.entity.agentinfo.RequestCancelBody
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.RequestRecordInfo
import cn.xzj.agent.entity.customer.RequestRecordsBodySimple
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.iview.IRequestRecordsView
import cn.xzj.agent.presenter.RequestRecordsPresenter
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.ui.job.JobDetailActivity
import cn.xzj.agent.ui.job.JobReservationActivity
import cn.xzj.agent.util.CommonUtils
import com.channey.utils.FormatUtils
import kotlinx.android.synthetic.main.fragment_request_records.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 岗位申请
 */
class RequestRecordsFragment : MVPBaseFragment<RequestRecordsPresenter>(), IRequestRecordsView {
    private var mCurrentPage = 1
    private var mTotalCount = 0
    private lateinit var mDetailInfo: CustomerDetailInfo
    override fun context(): Context {
        return context!!
    }

    override fun initLayout(): Int {
        return R.layout.fragment_request_records
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

    override fun onRecordsGetSuccess(info: CommonListBody<RequestRecordInfo>) {
        mTotalCount = info.totalCount
        if (mCurrentPage == 1) {
            mAdapter.clearData()
        }
        mAdapter.addAll(info.items)
        mAdapter.notifyDataSetChanged()
        loadingComplete()
    }

    override fun onRecordsGetFailure(msg: String) {
        loadingComplete()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<RequestRecordInfo> {
            override fun onItemClick(view: View, itemData: RequestRecordInfo, i: Int) {
                gotoPositionDetail(itemData.positionId)
                CommonUtils.statistics(context!!, Constants.STATISTICS_customerDetail_positionApply_positionDetail_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }

        })
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
        val body = RequestRecordsBodySimple(EnumValue.APPLY_STATUS_REQUEST_ALREADY_1, mCurrentPage, EnumValue.PAGE_SIZE.toInt(), mDetailInfo.userId)
        mPresenter.getCustomerRequestRecords(body)
    }

    private fun requestCancel(info: RequestRecordInfo) {
        val body = RequestCancelBody(info.applyId)
        mPresenter.cancelRequest(body)
        CommonUtils.statistics(context!!, Constants.STATISTICS_customerDetail_positionApply_cancelApply_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
    }

    override fun cancelSuccess(info: Boolean) {
        if (info) {
            getRecords()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Code.RequestCode.YUYUE -> {
                getRecords()
            }
        }
    }

    private fun gotoPositionDetail(positionId: String) {
        val intent = Intent(context, JobDetailActivity::class.java)
        intent.putExtra(Keys.POSITION_ID, positionId)
        intent.putExtra(Keys.USER_ID, mDetailInfo.userId)
        startActivityForResult(intent, Code.RequestCode.POSITION_DETAIL)
    }

    override fun onJobDetailGetSuccess(info: JobInfo, position: Int) {
        //获取岗位详情成功去预约
        val item=mAdapter.data!![position]
        JobReservationActivity.jump(context!!, item.positionId, applyId = item.applyId
                , positionName = item.positionName, userId = mDetailInfo.userId, userName = mDetailInfo.userName,isNeedPreReserve = info.preReserve)
        CommonUtils.statistics(context!!, Constants.STATISTICS_customerDetail_positionApply_RESERVE_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
    }

    private var mAdapter = object : QuickAdapter<RequestRecordInfo>(R.layout.item_request_records) {
        override fun convert(holder: BaseHolder, item: RequestRecordInfo, position: Int) {

            var status = ""
            when (item.applyStatus) {
                EnumValue.APPLY_STATUS_REQUEST_ALREADY_1 -> status = "已申请"
                EnumValue.APPLY_STATUS_REQUEST_CANCEL_2 -> status = "取消申请"
                EnumValue.APPLY_STATUS_YUYUE_ALREADYL_10 -> status = "已预约"
                EnumValue.APPLY_STATUS_YUYUE_CANCEL_11 -> status = "取消预约"
                EnumValue.APPLY_STATUS_QIANDAO_ALREADY_20 -> status = "已签到"
                EnumValue.APPLY_STATUS_LUQU_ALREADY_40 -> status = "已录取"
                EnumValue.APPLY_STATUS_LUQU_NOTYET_41 -> status = "未录取"
                EnumValue.APPLY_STATUS_RUZHI_ALREADY_50 -> status = "已入职"
                EnumValue.APPLY_STATUS_RUZHI_NOTYET_51 -> status = "未入职"
                EnumValue.APPLY_STATUS_JIESUAN_ALREADY_60 -> status = "已结算返费"
                EnumValue.APPLY_STATUS_WAIPAI_ALREADY_97 -> status = "已外派"
                EnumValue.APPLY_STATUS_ZHUANZHENG_ALREADY_98 -> status = "已转正"
                EnumValue.APPLY_STATUS_LIZHI_ALREADY_99 -> status = "已离职"
            }
            holder.setText(R.id.item_request_records_flag2, status)

            //岗位特性标签
            if (item.positionTypes != null || item.positionTypes.isNotEmpty()) {
                var mFeature = ""
                for (tagFeature in item.positionTypes) {
                    mFeature += if (TextUtils.isEmpty(mFeature)) tagFeature
                    else
                        ",$tagFeature"
                }
                if (TextUtils.isEmpty(mFeature)) {
                    holder.getView<TextView>(R.id.item_request_records_flag1).visibility = View.GONE
                } else {
                    holder.getView<TextView>(R.id.item_request_records_flag1).visibility = View.VISIBLE
                }
                holder.getView<TextView>(R.id.item_request_records_flag1).text = mFeature
            } else {
                holder.getView<TextView>(R.id.item_request_records_flag1).visibility = View.GONE
            }

            holder.getView<TextView>(R.id.item_request_records_yuyue_btn).setOnClickListener {
                mPresenter.getPositionDetails(item.positionId,mDetailInfo.userId,position)

            }
            holder.getView<TextView>(R.id.item_request_records_cancel_btn).setOnClickListener {
                requestCancel(item)
            }
            val date = FormatUtils.timeStamp2String(item.applyTime.toLong(), EnumValue.DATE_FORMAT_1)

            holder.setText(R.id.item_request_records_name_tv, item.positionName)

            holder.setText(R.id.item_request_records_date_tv, "申请时间: $date")
        }

    }
}