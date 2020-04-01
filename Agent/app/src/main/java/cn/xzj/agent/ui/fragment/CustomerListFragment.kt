package cn.xzj.agent.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.OriginalCustomerInfo
import cn.xzj.agent.entity.event.NormalCustomerEventMessage
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import cn.xzj.agent.iview.IOriginalCustomerListView
import cn.xzj.agent.presenter.OriginalCustomerListPresenter
import cn.xzj.agent.ui.adapter.DropDownSelectMenuAdapter
import cn.xzj.agent.ui.adapter.OriginalCustomerListAdapter
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.ui.adapter.common.decoration.SimpleItemDecoration
import cn.xzj.agent.ui.customer.CustomerDetailActivity
import cn.xzj.agent.ui.customer.CustomerSearchActivity
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.widget.DropDownSelectMenu
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.activity_customer_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 客户列表
 * 修改人：MarkYe
 */
class CustomerListFragment : MVPBaseFragment<OriginalCustomerListPresenter>(), OnRecyclerViewItemClickListener, View.OnClickListener, IOriginalCustomerListView {
    private lateinit var mAdapter: OriginalCustomerListAdapter
    private var mList = ArrayList<OriginalCustomerInfo>()
    private var mCurrentPage = 1
    private var mTotalCount = 0
    private var mCustomerDetailInfo: CustomerDetailInfo? = null
    private val ACTION_PHONECALL = 0
    private val ACTION_GOTO_DETAIL = 1
    private var mKeyWord: String? = null    //搜索关键字
    private val SORT_COMMENT_TIME = "COMMENT_TIME"  //注记时间
    private val SORT_REGISTER_TIME = "REGISTER_TIME"    //注册时间
    private val SORT_DISTRIBUTION_TIME = "MATCH_TIME"  //分配时间
    /**
     * 1 - 七日未联系, 2 - 十五日未联系, 3 - 三十日未联系
     */
    private var mDetails = HashMap<String, CustomerDetailInfo>()
    private var mCustomerSortConditionAdapter: DropDownSelectMenuAdapter? = null
    private var mCustomerIntentLvConditionAdapter: DropDownSelectMenuAdapter? = null
    private var mCustomerConditionAdapter: DropDownSelectMenuAdapter? = null
    private var mDropDownSelectMenuSort: DropDownSelectMenu? = null
    private var mDropDownSelectMenuIntent: DropDownSelectMenu? = null
    private var mDropDownSelectMenuCustomer: DropDownSelectMenu? = null

    companion object {
        val INTENT_EXTREMELY_HIGH = "EXTREMELY_HIGH"   //极高
        val INTENT_HIGH = "HIGH"   //高
        val INTENT_MEDIUM = "MEDIUM"   //中
        val INTENT_LOW = "LOW" //低
        val INTENT_NONE = "NONE"   //无法判断
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun context(): Context {
        return context!!
    }

    override fun initLayout(): Int {
        return R.layout.activity_customer_list
    }

    override fun initParams() {
    }

    override fun initViews() {
        getStatusLayoutManager().showLoading()
        initRecyclerView()
        initTabLayout()
    }

    override fun initData() {
    }

    override fun onResume() {
        super.onResume()
        getOriginalCustomersSort()
        CommonUtils.statistics(context, Constants.STATISTICS_CUSTOMER_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    override fun setListeners() {
        ivFragmentCustomerSearch.setOnClickListener(this)
        activity_customer_list_search_tv.setOnClickListener(this)
        activity_customer_list_delete_tv.setOnClickListener(this)
        statusLayout.setOnRetryListener {
            getOriginalCustomersSort()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.activity_customer_list_search_tv -> {
                gotoCustomerSearch()
                CommonUtils.statistics(context, Constants.STATISTICS_CUSTOMER_SEARCH_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            R.id.activity_customer_list_delete_tv -> {
                mKeyWord = null
                activity_customer_list_delete_tv.visibility = View.GONE
                activity_customer_list_search_tv.text = mKeyWord
                getOriginalCustomersSort()
            }
            R.id.ivFragmentCustomerSearch -> {
                CustomerSearchActivity.jump(context!!, CustomerSearchActivity.TYPE_NOMARL)
                CommonUtils.statistics(context, Constants.STATISTICS_CUSTOMER_SEARCH_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
        }
    }

    private fun initRecyclerView() {
        mAdapter = OriginalCustomerListAdapter(context!!, mList)
        mAdapter.setOnPhoneCallClickListener(object : OnRecyclerViewItemClickListener {
            override fun onClick(view: View, position: Int) {
                getCustomerDetailInfo(position, ACTION_PHONECALL)
                CommonUtils.statistics(context, Constants.STATISTICS_CUSTOMER_CALL_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
        })
        mAdapter.setOnRecyclerViewItemClickListener(this)
        activity_customer_list_recyclerView.adapter = mAdapter
        activity_customer_list_recyclerView.layoutManager = LinearLayoutManager(context)
        activity_customer_list_recyclerView.addItemDecoration(SimpleItemDecoration.builder(context)
                .isDrawTopFirstLine(false)
                .build())
        smartRefreshLayout.isEnableLoadMore = true
        smartRefreshLayout.setOnRefreshListener {
            mCurrentPage = 1
            getOriginalCustomersSort()
        }
        smartRefreshLayout.setOnLoadMoreListener {
            mCurrentPage++
            getOriginalCustomersSort()
        }
    }

    private fun initTabLayout() {
        dropDownTabLayoutFragmentCustomer.setSumItemSize(3)
        dropDownTabLayoutFragmentCustomer.addItem("排序")
        dropDownTabLayoutFragmentCustomer.addItem(getString(R.string.sort_type_intent_level))
        dropDownTabLayoutFragmentCustomer.addItem("筛选客户")
        dropDownTabLayoutFragmentCustomer.setOnItemOnClickListener { view, position ->
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

    private fun getCustomerDetailInfo(position: Int, action: Int) {
        val customer = mList[position]
        val map = HashMap<String, String>()
        map["user_id"] = customer.userId
        mPresenter.getCustomerDetailInfo(map, customer.phone, action, position)
    }


    private fun loadingComplete() {
        mAdapter.notifyDataSetChanged()
        if (mCurrentPage == 1) {
            smartRefreshLayout.finishRefresh()
        } else {
            smartRefreshLayout.finishLoadMore()
        }
        smartRefreshLayout.setNoMoreData(!(mTotalCount > mList.size))
    }

    fun getOriginalCustomersSort() {
        val map = HashMap<String, String>()
        map["page_no"] = mCurrentPage.toString()
        map["order"] = "1"   //顺序: 0 - 升序, 1 - 降序
        if (!StringUtils.isEmpty(mKeyWord)) {
            map["term"] = mKeyWord!!
            activity_customer_list_search_tv.visibility = View.VISIBLE
        } else {
            activity_customer_list_search_tv.visibility = View.GONE
        }
        if (mCustomerSortConditionAdapter != null) {
            if (mCustomerSortConditionAdapter!!.getSelectPosition() >= 0) {
                var sort = SORT_COMMENT_TIME
                when (mCustomerSortConditionAdapter!!.getSelectPosition()) {
                    0 -> {
                        sort = SORT_COMMENT_TIME
                    }
                    1 -> {
                        sort = SORT_REGISTER_TIME
                    }
                    2 -> {
                        sort = SORT_DISTRIBUTION_TIME
                    }
                }
                map["sort"] = sort
            }
        }
        if (mCustomerIntentLvConditionAdapter != null) {
            if (mCustomerIntentLvConditionAdapter!!.getSelectPosition() >= 0) {
                var wish = INTENT_EXTREMELY_HIGH
                when (mCustomerIntentLvConditionAdapter!!.getSelectPosition()) {
                    0 -> wish = ""
                    1 -> wish = INTENT_EXTREMELY_HIGH
                    2 -> wish = INTENT_HIGH
                    3 -> wish = INTENT_MEDIUM
                    4 -> wish = INTENT_LOW
                    5 -> wish = INTENT_NONE
                }
                if (StringUtils.isNotEmpty(wish)) map["wish"] = wish
            }
        }
        if (mCustomerConditionAdapter != null) {
            val mSelectPosition = mCustomerConditionAdapter!!.getSelectPosition()

            if (mSelectPosition >= 0) {
                map["not_contacted"] = "$mSelectPosition"
            }
        }
        mPresenter.getOriginalCustomers(map)
    }

    override fun onCustomersGetSuccess(info: CommonListBody<OriginalCustomerInfo>) {
        getStatusLayoutManager().showContent()

        mTotalCount = info.totalCount
        if (mCurrentPage == 1) {
            mList.clear()
        }
        mList.addAll(info.items)
        if (info.totalCount == 0) {
            statusLayout.showEmpty()
        } else {
            statusLayout.showContent()
        }
        loadingComplete()
    }

    override fun onCustomersGetFailure() {
        statusLayout.showLoadError()
        getStatusLayoutManager().showContent()
        loadingComplete()
    }

    override fun onClick(view: View, position: Int) {
        val info = mDetails[mList[position].userId]
        if (info == null) {
            getCustomerDetailInfo(position, ACTION_GOTO_DETAIL)
        } else {
            CustomerDetailActivity.jump(context!!, info)
            if (mKeyWord == null) {
                CommonUtils.statistics(context, Constants.STATISTICS_CUSTOMER_customerDetail_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            } else {
                CommonUtils.statistics(context, Constants.STATISTICS_customerSearch_customerDetail_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
        }
    }

    private fun showSortCondition() {
        if (mCustomerSortConditionAdapter == null) {
            mCustomerSortConditionAdapter = DropDownSelectMenuAdapter()
            mCustomerSortConditionAdapter!!.addItem("注记时间")
            mCustomerSortConditionAdapter!!.addItem("注册时间")
            mCustomerSortConditionAdapter!!.addItem("分配时间")
            mCustomerSortConditionAdapter!!.selectPosition(0)
        }
        if (mDropDownSelectMenuSort == null) {
            mDropDownSelectMenuSort = DropDownSelectMenu(activity, mCustomerSortConditionAdapter)
            mCustomerSortConditionAdapter!!.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<String> {
                override fun onItemClick(view: View, itemData: String, i: Int) {
                    mDropDownSelectMenuSort!!.dismiss()
                    mCustomerSortConditionAdapter!!.selectPosition(i)
                    dropDownTabLayoutFragmentCustomer.itemTitle = itemData

                    smartRefreshLayout.autoRefresh()
                    getOriginalCustomersSort()
                    CommonUtils.statistics(context, Constants.STATISTICS_CUSTOMER_SORT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
            })
            mDropDownSelectMenuSort!!.popupWindow.setOnDismissListener {
                dropDownTabLayoutFragmentCustomer.closeMenu()
            }
        }
        mDropDownSelectMenuSort!!.show(dropDownTabLayoutFragmentCustomer)
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
            mCustomerIntentLvConditionAdapter!!.selectPosition(0)
        }
        if (mDropDownSelectMenuIntent == null) {
            mDropDownSelectMenuIntent = DropDownSelectMenu(activity, mCustomerIntentLvConditionAdapter)
            mCustomerIntentLvConditionAdapter!!.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<String> {
                override fun onItemClick(view: View, itemData: String, i: Int) {
                    mDropDownSelectMenuIntent!!.dismiss()
                    mCustomerIntentLvConditionAdapter!!.selectPosition(i)
                    dropDownTabLayoutFragmentCustomer.itemTitle = itemData
                    smartRefreshLayout.autoRefresh()
                    getOriginalCustomersSort()
                    CommonUtils.statistics(context, Constants.STATISTICS_CUSTOMER_filterContactTime_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
            })
            mDropDownSelectMenuIntent!!.popupWindow.setOnDismissListener {
                dropDownTabLayoutFragmentCustomer.closeMenu()
            }
        }
        mDropDownSelectMenuIntent!!.show(dropDownTabLayoutFragmentCustomer)
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
        if (mDropDownSelectMenuCustomer == null) {
            mDropDownSelectMenuCustomer = DropDownSelectMenu(activity, mCustomerConditionAdapter)
            mCustomerConditionAdapter!!.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<String> {
                override fun onItemClick(view: View, itemData: String, i: Int) {
                    mDropDownSelectMenuCustomer!!.dismiss()
                    mCustomerConditionAdapter!!.selectPosition(i)
                    dropDownTabLayoutFragmentCustomer.itemTitle = itemData
                    smartRefreshLayout.autoRefresh()
                    getOriginalCustomersSort()
                    CommonUtils.statistics(context, Constants.STATISTICS_CUSTOMER_filterContactTime_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
            })
            mDropDownSelectMenuCustomer!!.popupWindow.setOnDismissListener {
                dropDownTabLayoutFragmentCustomer.closeMenu()
            }
        }
        mDropDownSelectMenuCustomer!!.show(dropDownTabLayoutFragmentCustomer)
    }


    override fun onCustomerDetailInfoGetSuccess(info: CustomerDetailInfo, number: String, action: Int, position: Int) {
        mCustomerDetailInfo = info
        mCustomerDetailInfo!!.nickname = mList[position].nickname
        mDetails[info.userId] = info
        when (action) {
            ACTION_PHONECALL -> CommonUtils.makePhoneCall(activity, number, numberFormat(info.phone))
            ACTION_GOTO_DETAIL -> {
                CustomerDetailActivity.jump(context!!, info)
                if (mKeyWord == null) {
                    CommonUtils.statistics(context, Constants.STATISTICS_CUSTOMER_customerDetail_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                } else {
                    CommonUtils.statistics(context, Constants.STATISTICS_customerSearch_customerDetail_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
            }

        }
    }

    override fun onCustomerDetailInfoGetFailure() {
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


    private fun gotoCustomerSearch() {
        val intent = Intent(context, CustomerSearchActivity::class.java)
        startActivity(intent)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getNormalCustomerEventMessage(eventData: NormalCustomerEventMessage) {
        mKeyWord = eventData.content
        activity_customer_list_delete_tv.visibility = View.VISIBLE
        activity_customer_list_search_tv.text = mKeyWord
        smartRefreshLayout.autoRefresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}