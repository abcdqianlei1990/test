package cn.xzj.agent.ui.fragment

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.DefaultEventMessage
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.newyearreservation.NewYearReservation
import cn.xzj.agent.iview.INewYearReserveView
import cn.xzj.agent.presenter.NewYearReservePresenter
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.ui.adapter.common.decoration.SimpleItemDecoration
import cn.xzj.agent.ui.customer.CustomerDetailActivity
import cn.xzj.agent.ui.newyear.NewYearReservationSearchActivity
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.SharedPreferencesUtil
import cn.xzj.agent.util.StatusBarUtil
import com.channey.utils.FormatUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_new_year_job.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/27
 * @Des 年后预约
 */
class NewYearReserveFragment : MVPBaseFragment<NewYearReservePresenter>(), INewYearReserveView {
    private var mCurrentPageNo = 1
    private var agentId: String? = null
    private val ACTION_PHONECALL = 0
    private val ACTION_GOTO_DETAIL = 1
    private var mDetails = HashMap<String, CustomerDetailInfo>()
    private var term: String? = null
    override fun context(): Context {
        return context!!
    }
    override fun initLayout(): Int {
        return R.layout.fragment_new_year_job
    }
    override fun initParams() {
        agentId = SharedPreferencesUtil.getCurrentAgentInfo(context).agentId
    }
    override fun onResume() {
        super.onResume()
        getNewYearJob()
        CommonUtils.statistics(context, Constants.STATISTICS_chineseNewYearReserveCustomer_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }
    override fun initViews() {
        EventBus.getDefault().register(this)
        statusLayoutFragmentNewYear.showLoading()
        StatusBarUtil.setPadding(context, layout_fragment_newyearjob_top_parent)
        recyclerViewFragmentNewYearJob.layoutManager = LinearLayoutManager(context)
        recyclerViewFragmentNewYearJob.adapter = mAdapter
        recyclerViewFragmentNewYearJob.addItemDecoration(SimpleItemDecoration.builder(context)
                .isDrawTopFirstLine(false)
                .build())
    }
    override fun setListeners() {
        super.setListeners()
        smartRefreshLayoutFragmentNewYearJob.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mCurrentPageNo++
                getNewYearJob()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mCurrentPageNo = 1
                getNewYearJob()
            }
        })
        mAdapter.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<NewYearReservation> {
            override fun onItemClick(view: View, itemData: NewYearReservation, i: Int) {
                val info = mDetails[itemData.userId]
                if (info == null) {
                    getCustomerDetailInfo(i, ACTION_GOTO_DETAIL)
                } else {
                    CustomerDetailActivity.jump(context!!, info)
                    CommonUtils.statistics(context, Constants.STATISTICS_chineseNewYearReserveCustomer_customerDetail_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
            }
        })
        tv_fragment_new_year_search_btn.setOnClickListener {
            startActivity(Intent(context, NewYearReservationSearchActivity::class.java))
            CommonUtils.statistics(context, Constants.STATISTICS_chineseNewYearReserveCustomer_SEARCH_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
        }
        ic_new_year_search_delete.setOnClickListener {
            //清空搜索
            term = null
            getNewYearJob()
            ic_new_year_search_delete.visibility = View.GONE
            tv_fragment_new_year_search_btn.text = ""
        }
        statusLayoutFragmentNewYear.setOnRetryListener {
            getNewYearJob()
        }
    }
    override fun initData() {
    }
    override fun onGetNewYearReservationSuccess(data: CommonListBody<NewYearReservation>) {
        if (mCurrentPageNo == 1) {
            smartRefreshLayoutFragmentNewYearJob.finishRefresh()
            mAdapter.setNewData(data.items)
        } else {
            mAdapter.addAll(data.items)
            mAdapter.notifyDataSetChanged()
            smartRefreshLayoutFragmentNewYearJob.finishLoadMore()
        }
        smartRefreshLayoutFragmentNewYearJob.setNoMoreData(data.totalCount <= mAdapter.data!!.size)
        if (mAdapter.data!!.size==0) {
            statusLayoutFragmentNewYear.showEmpty()
        } else {
            statusLayoutFragmentNewYear.showContent()
        }
    }
    override fun onGetNewYearReservationFail() {
        if (mCurrentPageNo == 1) {
            smartRefreshLayoutFragmentNewYearJob.finishRefresh()
        } else {
            smartRefreshLayoutFragmentNewYearJob.finishLoadMore()
        }
        if (mAdapter.data!!.isEmpty()) {
            statusLayoutFragmentNewYear.showLoadError()
        }
    }
    override fun onCustomerDetailInfoGetSuccess(info: CustomerDetailInfo, number: String, action: Int, position: Int) {
        mDetails[info.userId] = info
        when (action) {
            ACTION_PHONECALL -> CommonUtils.makePhoneCall(activity, number, numberFormat(info.phone))
            ACTION_GOTO_DETAIL -> {
                CustomerDetailActivity.jump(context!!, info)
                CommonUtils.statistics(context, Constants.STATISTICS_chineseNewYearReserveCustomer_customerDetail_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
        }
    }


    private fun getNewYearJob() {
        mPresenter.getNewYearReservation(mCurrentPageNo, agentId!!, term)
    }

    private fun numberFormat(number: String): String {
        if (number.contains("-")) {
            val strings = number.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val sb = StringBuilder()
            for (s in strings) {
                sb.append(s)
            }
            return sb.toString()
        } else
            return number
    }

    private fun getCustomerDetailInfo(position: Int, action: Int) {
        val customer = mAdapter.data!![position]
        val map = HashMap<String, String>()
        map.put("user_id", customer.userId)
        mPresenter.getCustomerDetailInfo(map, customer.userPhone, action, position)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun eventMessage(info: DefaultEventMessage) {
        if (info.code == NewYearReservationSearchActivity.SEARCH_REQUEST) {
            term = info.data.toString()
            if (term != null) {
                if (ic_new_year_search_delete.visibility != View.VISIBLE)
                    ic_new_year_search_delete.visibility = View.VISIBLE
                tv_fragment_new_year_search_btn.text = term
            }
            getNewYearJob()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }


    private var mAdapter = object : QuickAdapter<NewYearReservation>(R.layout.item_new_year_reservation) {
        override fun convert(holder: BaseHolder, item: NewYearReservation, position: Int) {
            holder.setText(R.id.tv_newyear_baseinfo, "${item.userPhone} ${item.userName}(${item.agentNickname})")
            //报名时间
            holder.setText(R.id.tv_newyear_reservation_time, FormatUtils.timeStamp2String(item.applyTime, EnumValue.DATE_FORMAT_1))
            //岗位名称
            holder.setText(R.id.tv_newyear_job_name, item.positionName)
            //最近注记
            if (item.lastContactComment != null) {
                holder.setText(R.id.tv_newyear_job_notice, item.lastContactComment)
            }
            holder.getView<View>(R.id.tv_newyear_phonecall_btn).setOnClickListener {
                getCustomerDetailInfo(position, ACTION_PHONECALL)
            }
        }
    }

}