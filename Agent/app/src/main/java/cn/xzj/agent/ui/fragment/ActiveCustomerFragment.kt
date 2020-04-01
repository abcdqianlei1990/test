package cn.xzj.agent.ui.fragment

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.active_customer.ActiveCustomerInfo
import cn.xzj.agent.entity.active_customer.CustomerActiveEventInfo
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.OriginalCustomerInfo
import cn.xzj.agent.entity.event.ActiveCustomerEventMessage
import cn.xzj.agent.iview.IActiveCustomerView
import cn.xzj.agent.presenter.ActiveCustomerPresenter
import cn.xzj.agent.ui.adapter.DropDownSelectMenuAdapter
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.ui.adapter.common.decoration.SimpleItemDecoration
import cn.xzj.agent.ui.customer.CustomerDetailActivity
import cn.xzj.agent.ui.customer.CustomerSearchActivity
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.widget.CommonDialog
import cn.xzj.agent.widget.DropDownSelectMenu
import com.channey.utils.FormatUtils
import com.channey.utils.ShapeUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_customer_active.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/2/26
 * @Des 活跃客户
 */
class ActiveCustomerFragment : MVPBaseFragment<ActiveCustomerPresenter>(), IActiveCustomerView {
    private var mCustomerSortConditionAdapter: DropDownSelectMenuAdapter? = null
    private var mDropDownSelectMenuSort: DropDownSelectMenu? = null
    private var mCustomerIntentLvConditionAdapter: DropDownSelectMenuAdapter? = null
    private var mDropDownSelectMenuIntentLv: DropDownSelectMenu? = null
    private var mCustomerConditionAdapter: DropDownSelectMenuAdapter? = null
    private var mDropDownSelectMenuCondition: DropDownSelectMenu? = null
    private var mCurrentPage = 1
    private var mCustomerActiveEventDialog: CommonDialog? = null
    private var mKeyWord: String? = null
    private var mDetails = HashMap<String, CustomerDetailInfo>()
    private var mAdapter = object : QuickAdapter<ActiveCustomerInfo>(R.layout.item_active_customer) {
        override fun convert(holder: BaseHolder, item: ActiveCustomerInfo, position: Int) {
            //昵称手机号
            var nickname = ""
            if (item.nickname != null)
                nickname = item.nickname
            var userName = ""
            if (item.userName != null)
                userName = item.userName
            holder.setText(R.id.tvActiveCustomerItemPhoneAndName, "${item.phone} $nickname($userName)")
            //意向
            var solidColor = resources.getColor(R.color.redF83232)
            var radius = resources.getDimension(R.dimen.dp_12)
            var intentLvTv = holder.getView<AppCompatTextView>(R.id.item_active_customer_intent_tv)
            intentLvTv.visibility = View.VISIBLE
            var intentStr = ""
            when(item.wish){
                OriginalCustomerInfo.INTENT_EXTREMELY_HIGH -> {
                    solidColor = resources.getColor(R.color.redF83232)
                    intentStr = "意向：极高"
                }
                OriginalCustomerInfo.INTENT_HIGH -> {
                    solidColor = resources.getColor(R.color.yellowFF5800)
                    intentStr = "意向：高"
                }
                OriginalCustomerInfo.INTENT_MEDIUM -> {
                    solidColor = resources.getColor(R.color.yellowFF8500)
                    intentStr = "意向：中"
                }
                OriginalCustomerInfo.INTENT_LOW -> {
                    solidColor = resources.getColor(R.color.yellowFFB600)
                    intentStr = "意向：低"
                }
                OriginalCustomerInfo.INTENT_NONE -> {
                    solidColor = resources.getColor(R.color.green90D600)
                    intentStr = "意向：无"
                }
                else -> intentLvTv.visibility = View.GONE
            }
            ShapeUtil.setShape(intentLvTv,solidColor = solidColor,radius = radius)
            intentLvTv.text = intentStr
            //活跃情况
            val mTvActiveCustomerItemActiveDetail = holder.getView<TextView>(R.id.tvActiveCustomerItemActiveDetail)
            mTvActiveCustomerItemActiveDetail.text = item.lastActiveEvents
            //添加下划线
            mTvActiveCustomerItemActiveDetail.paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
            mTvActiveCustomerItemActiveDetail.paint.isAntiAlias = true //抗锯齿
            mTvActiveCustomerItemActiveDetail.setOnClickListener {
                mPresenter.getCustomerAvtiveEvents(item.userId)
                CommonUtils.statistics(context, Constants.STATISTICS_activeCustomer_activeEvents_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            //最近注记
            if (item.lastContactComment != null) {
                val lastContactCommentTime = FormatUtils.timeStamp2String(item.lastContactTime.toLong(), EnumValue.DATE_FORMAT_1)
                holder.setText(R.id.tvActiveCustomerItemLastMark, lastContactCommentTime + " " + item.lastContactComment)
            } else {
                holder.setText(R.id.tvActiveCustomerItemLastMark, "无")
            }
            //累计活跃
            holder.setText(R.id.tvActiveCustomerItemActiveNumber, "累计活跃${item.activeTimes}次")
        }
    }
    private val mCustomerActiveEventAdapter = object : QuickAdapter<CustomerActiveEventInfo>(R.layout.item_customer_active_event) {
        override fun convert(holder: BaseHolder, item: CustomerActiveEventInfo, position: Int) {
            holder.setText(R.id.tvCustomerActiveEventContent, item.content)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun context(): Context {
        return context!!
    }

    override fun initLayout(): Int {
        return R.layout.fragment_customer_active
    }

    override fun initParams() {
    }

    override fun initViews() {
        recyclerViewActiveFragmentCustomer.layoutManager = LinearLayoutManager(context)
        recyclerViewActiveFragmentCustomer.adapter = mAdapter
        recyclerViewActiveFragmentCustomer.addItemDecoration(SimpleItemDecoration.builder(context).isDrawTopFirstLine(false).build())
        initTabLayout()
    }

    override fun setListeners() {
        super.setListeners()
        statusLayout.setOnRetryListener {
            statusLayout.showLoading()
            getCustomerData()
        }
        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mCurrentPage++
                getCustomerData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mCurrentPage = 1
                getCustomerData()
            }

        })
        mAdapter.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<ActiveCustomerInfo> {
            override fun onItemClick(view: View, itemData: ActiveCustomerInfo, i: Int) {
                val mDetailInfo = mDetails[itemData.userId]
                if (mDetailInfo == null) {
                    mPresenter.getCustomerDetailInfo(itemData.userId)
                } else {
                    jumpCustomerDetail(mDetailInfo)
                }
            }
        })
        rlActiveFragmentCustomerSearch.setOnClickListener {
            CustomerSearchActivity.jump(context!!, CustomerSearchActivity.TYPE_CORE)
            CommonUtils.statistics(context, Constants.STATISTICS_activeCustomer_SEARCH_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
        }
        ivActiveFragmentCustomerSearchDelete.setOnClickListener {
            mKeyWord = null
            tvActiveFragmentCustomerSearch.text = ""
            rlActiveFragmentCustomerSearch.visibility = View.GONE
            getCustomerData()
        }
    }

    override fun initData() {
        getCustomerData()
    }

    private fun initTabLayout() {
        dropDownTabLayoutActiveFragmentCustomer.setSumItemSize(3)
        dropDownTabLayoutActiveFragmentCustomer.addItem("排序")
        dropDownTabLayoutActiveFragmentCustomer.addItem(getString(R.string.sort_type_intent_level))
        dropDownTabLayoutActiveFragmentCustomer.addItem("筛选客户")
        dropDownTabLayoutActiveFragmentCustomer.setOnItemOnClickListener { view, position ->
            when (position) {
                0 -> {
                    showSortCondition()
                }
                1 -> {
                    showIntentLvCondition()
                }
                2 -> {
                    showCustomerCondition()
                }
            }
        }
    }

    private fun showSortCondition() {
        if (mCustomerSortConditionAdapter == null) {
            mCustomerSortConditionAdapter = DropDownSelectMenuAdapter()
            mCustomerSortConditionAdapter!!.addItem("按最近活跃时间")
            mCustomerSortConditionAdapter!!.addItem("按累计活跃次数 从大到小排序")
            mCustomerSortConditionAdapter!!.addItem("按累计活跃次数 从小到大排序")
        }
        if (mDropDownSelectMenuSort == null) {
            mDropDownSelectMenuSort = DropDownSelectMenu(activity, mCustomerSortConditionAdapter)
            mCustomerSortConditionAdapter!!.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<String> {
                override fun onItemClick(view: View, itemData: String, i: Int) {
                    mDropDownSelectMenuSort!!.dismiss()
                    mCustomerSortConditionAdapter!!.selectPosition(i)
                    dropDownTabLayoutActiveFragmentCustomer.itemTitle = itemData
                    statusLayout.showLoading()
                    smartRefreshLayout.autoRefresh()
                    getCustomerData()
                    CommonUtils.statistics(context, Constants.STATISTICS_activeCustomer_SORT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
            })
            mDropDownSelectMenuSort!!.popupWindow.setOnDismissListener {
                dropDownTabLayoutActiveFragmentCustomer.closeMenu()
            }
        }
        mDropDownSelectMenuSort!!.show(dropDownTabLayoutActiveFragmentCustomer)
    }

    private fun showIntentLvCondition() {
        if (mCustomerIntentLvConditionAdapter == null) {
            mCustomerIntentLvConditionAdapter = DropDownSelectMenuAdapter()
            mCustomerIntentLvConditionAdapter!!.addItem("不限")
            mCustomerIntentLvConditionAdapter!!.addItem("极高")
            mCustomerIntentLvConditionAdapter!!.addItem("高")
            mCustomerIntentLvConditionAdapter!!.addItem("中")
            mCustomerIntentLvConditionAdapter!!.addItem("低")
            mCustomerIntentLvConditionAdapter!!.addItem("无")
        }
        if (mDropDownSelectMenuIntentLv == null) {
            mDropDownSelectMenuIntentLv = DropDownSelectMenu(activity, mCustomerIntentLvConditionAdapter)
            mCustomerIntentLvConditionAdapter!!.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<String> {
                override fun onItemClick(view: View, itemData: String, i: Int) {
                    mDropDownSelectMenuIntentLv!!.dismiss()
                    mCustomerIntentLvConditionAdapter!!.selectPosition(i)
                    dropDownTabLayoutActiveFragmentCustomer.itemTitle = itemData
                    statusLayout.showLoading()
                    smartRefreshLayout.autoRefresh()
                    getCustomerData()
                    CommonUtils.statistics(context, Constants.STATISTICS_activeCustomer_SORT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
            })
            mDropDownSelectMenuIntentLv!!.popupWindow.setOnDismissListener {
                dropDownTabLayoutActiveFragmentCustomer.closeMenu()
            }
        }
        mDropDownSelectMenuIntentLv!!.show(dropDownTabLayoutActiveFragmentCustomer)
    }

    private fun showCustomerCondition() {
        if (mCustomerConditionAdapter == null) {
            mCustomerConditionAdapter = DropDownSelectMenuAdapter()

            mCustomerConditionAdapter!!.addItem("不限")
            mCustomerConditionAdapter!!.addItem("7日内未联系")
            mCustomerConditionAdapter!!.addItem("8-15日未联系")
            mCustomerConditionAdapter!!.addItem("16-30日未联系")
            mCustomerConditionAdapter!!.addItem("30日以上未联系")
            mCustomerConditionAdapter!!.selectPosition(0)
        }
        if (mDropDownSelectMenuCondition == null) {
            mDropDownSelectMenuCondition = DropDownSelectMenu(activity, mCustomerConditionAdapter)
            mCustomerConditionAdapter!!.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<String> {
                override fun onItemClick(view: View, itemData: String, i: Int) {
                    mDropDownSelectMenuCondition!!.dismiss()
                    mCustomerConditionAdapter!!.selectPosition(i)
                    dropDownTabLayoutActiveFragmentCustomer.itemTitle = itemData
                    smartRefreshLayout.autoRefresh()
                    getCustomerData()
                    CommonUtils.statistics(context, Constants.STATISTICS_activeCustomer_FILTER_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
            })
            mDropDownSelectMenuCondition!!.popupWindow.setOnDismissListener {
                dropDownTabLayoutActiveFragmentCustomer.closeMenu()
            }
        }
        mDropDownSelectMenuCondition!!.show(dropDownTabLayoutActiveFragmentCustomer)
    }

    override fun getActiveCustomersSuccess(data: CommonListBody<ActiveCustomerInfo>) {
        finishSmartRefreshLayout()
        if (data.totalCount == 0) {
            statusLayout.showEmpty()
        } else {
            statusLayout.showContent()
        }
        if (mCurrentPage == 1) {
            mAdapter.setNewData(data.items)
        } else {
            mAdapter.addAll(data.items)
            mAdapter.notifyDataSetChanged()
        }
        smartRefreshLayout.setNoMoreData(data.totalCount <= mAdapter.data!!.size)
    }

    override fun getActiveCustomersFailure(isNetWorkError: Boolean) {
        if (isNetWorkError)
            statusLayout.showNetworkError()
        else
            statusLayout.showLoadError()
        finishSmartRefreshLayout()
    }

    private fun finishSmartRefreshLayout() {
        if (mCurrentPage == 1) {
            smartRefreshLayout.finishRefresh()
        } else {
            smartRefreshLayout.finishLoadMore()
        }
    }

    override fun getCustomerActiveEventsSuccess(data: List<CustomerActiveEventInfo>) {
        showCustomerActiveEventView(data)
    }

    private fun showCustomerActiveEventView(data: List<CustomerActiveEventInfo>) {
        mCustomerActiveEventAdapter.setNewData(data)
        if (mCustomerActiveEventDialog == null) {
            mCustomerActiveEventDialog = CommonDialog.newBuilder(context)
                    .setCancelable(true)
                    .setView(R.layout.dialog_customer_active_event)
                    .create()
            val recyclerViewDialogCustomerActiveEvent = mCustomerActiveEventDialog!!.getView<RecyclerView>(R.id.recyclerViewDialogCustomerActiveEvent)
            recyclerViewDialogCustomerActiveEvent.layoutManager = LinearLayoutManager(context)
            recyclerViewDialogCustomerActiveEvent.adapter = mCustomerActiveEventAdapter
            mCustomerActiveEventDialog!!.getView<View>(R.id.btnDialogCustomerActiveEventClose)
                    .setOnClickListener {
                        mCustomerActiveEventDialog!!.dismiss()
                    }
        }
        mCustomerActiveEventDialog!!.setBottomShow()
    }

    fun getCustomerData() {
        var sort: String? = null
        var order: String? = null
        /**
         * 排序字段: ACTIVE_TIME - 活跃时间, ACTIVE_TIMES - 活跃次数
         */
        if (mCustomerSortConditionAdapter != null) {
            //排序
            val selectPosition = mCustomerSortConditionAdapter!!.getSelectPosition()
            if (selectPosition >= 0) {
                when (selectPosition) {
                    0 -> {
                        sort = "ACTIVE_TIME"
                    }
                    1 -> {
                        sort = "ACTIVE_TIMES"
                        order = "1"
                    }
                    2 -> {
                        sort = "ACTIVE_TIMES"
                        order = "0"
                    }
                }
            }
        }
        var wish: String? = null
        if (mCustomerIntentLvConditionAdapter != null) {
            if (mCustomerIntentLvConditionAdapter!!.getSelectPosition() >= 0) {
                when (mCustomerIntentLvConditionAdapter!!.getSelectPosition()) {
                    0 -> wish = null
                    1 -> wish = OriginalCustomerInfo.INTENT_EXTREMELY_HIGH
                    2 -> wish = OriginalCustomerInfo.INTENT_HIGH
                    3 -> wish = OriginalCustomerInfo.INTENT_MEDIUM
                    4 -> wish = OriginalCustomerInfo.INTENT_LOW
                    5 -> wish = OriginalCustomerInfo.INTENT_NONE
                }
            }
        }
        var notContacted: Int? = null
        if (mCustomerConditionAdapter != null) {
            val selectPosition = mCustomerConditionAdapter!!.getSelectPosition()
            if (selectPosition >= 0) {
                when (selectPosition) {
                    1 -> {
                        notContacted = selectPosition
                    }
                    2 -> {
                        notContacted = selectPosition
                    }
                    3 -> {
                        notContacted = selectPosition
                    }
                    4 -> {
                        notContacted = selectPosition
                    }
                }
            }
        }
        mPresenter.getActiveCustomers(mCurrentPage, sort = sort, order = order, wish = wish,notContacted = notContacted, term = mKeyWord)
    }

    override fun onCustomerDetailInfoGetSuccess(content: CustomerDetailInfo, userId: String) {
        mDetails[userId] = content
        jumpCustomerDetail(content)
    }

    private fun jumpCustomerDetail(mDetailInfo: CustomerDetailInfo) {
        CustomerDetailActivity.jump(context!!, mDetailInfo)
        CommonUtils.statistics(context, Constants.STATISTICS_activeCustomer_customerDetail_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getNormalCustomerEventMessage(eventData: ActiveCustomerEventMessage) {
        mKeyWord = eventData.content
        rlActiveFragmentCustomerSearch.visibility = View.VISIBLE
        tvActiveFragmentCustomerSearch.text = mKeyWord
        smartRefreshLayout.autoRefresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(context, Constants.STATISTICS_activeCustomer_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }
}
