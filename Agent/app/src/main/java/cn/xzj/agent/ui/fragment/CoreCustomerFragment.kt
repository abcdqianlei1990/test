package cn.xzj.agent.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.core_customer.CoreCustomerInfo
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.OriginalCustomerInfo
import cn.xzj.agent.entity.event.CoreCustomerEventMessage
import cn.xzj.agent.iview.ICoreCustomerView
import cn.xzj.agent.presenter.CoreCustomerPresenter
import cn.xzj.agent.ui.adapter.DropDownSelectMenuAdapter
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.ui.adapter.common.decoration.SimpleItemDecoration
import cn.xzj.agent.ui.customer.CustomerDetailActivity
import cn.xzj.agent.ui.customer.CustomerSearchActivity
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.widget.DropDownSelectMenu
import com.channey.utils.FormatUtils
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_core_customer.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/2/26
 * @Des 核心客户
 */
class CoreCustomerFragment : MVPBaseFragment<CoreCustomerPresenter>(), ICoreCustomerView {
    private var mCustomerSortConditionAdapter: DropDownSelectMenuAdapter? = null
    private var mDropDownSelectMenuSort: DropDownSelectMenu? = null
    private var mCustomerIntentLvConditionAdapter: DropDownSelectMenuAdapter? = null
    private var mCustomerCurrentStatusConditionAdapter: DropDownSelectMenuAdapter? = null
    private var mDropDownSelectMenuCurrentStatus: DropDownSelectMenu? = null
    private var mDropDownSelectMenuIntentLv: DropDownSelectMenu? = null
    private var mCustomerConditionAdapter: DropDownSelectMenuAdapter? = null
    private var mDropDownSelectMenuCondition: DropDownSelectMenu? = null
    private var mCurrentPage = 1
    private val CURRENT_STATUS_ENTER = 1
    private val CURRENT_STATUS_QUIT = 2
    private val CURRENT_STATUS_SIGN = 0
    private var mDetails = HashMap<String, CustomerDetailInfo>()
    private var mKeyWord: String? = null
    private var mAdapter = object : QuickAdapter<CoreCustomerInfo>(R.layout.item_core_customer) {
        override fun convert(holder: BaseHolder, item: CoreCustomerInfo, position: Int) {
            //名称
            var nickname = ""
            if (item.nickname != null)
                nickname = item.nickname
            var userName = ""
            if (item.userName != null)
                userName = item.userName
            holder.setText(R.id.tvCoreCustomerItemPhoneAndName, "${item.phone} $nickname($userName)")
            //岗位名称
            holder.setText(R.id.tvCoreCustomerItemPositionName, item.positionName)
            //意向
            var solidColor = resources.getColor(R.color.redF83232)
            var radius = resources.getDimension(R.dimen.dp_12)
            var intentLvTv = holder.getView<AppCompatTextView>(R.id.item_core_customer_intent_tv)
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
            //签到时间
            val signInTime = FormatUtils.timeStamp2String(item.signInTime, EnumValue.DATE_FORMAT_1)
            holder.setText(R.id.tvCoreCustomerItemSignInTime, signInTime)
            //入职时间
            val onboardingTime = FormatUtils.timeStamp2String(item.onboardingTime, EnumValue.DATE_FORMAT_6)
            holder.setText(R.id.tvCoreCustomerItemPositionInTime, onboardingTime)
            //离职时间
            val quitTime = FormatUtils.timeStamp2String(item.quitTime, EnumValue.DATE_FORMAT_6)
            holder.setText(R.id.tvCoreCustomerItemLeavePositionTime, quitTime)
            //当前状态
            val mTvCurrentStatus = holder.getView<TextView>(R.id.tvCoreCustomerItemCurrentStatus)
            val mLlCoreCustomerItemLeavePositionTime = holder.getView<View>(R.id.llCoreCustomerItemLeavePositionTime)
            val mLlCoreCustomerItemPositionInTime = holder.getView<View>(R.id.llCoreCustomerItemPositionInTime)
            when (item.currentState) {
                CURRENT_STATUS_SIGN -> {
                    mTvCurrentStatus.text = "已签到"
                    mLlCoreCustomerItemLeavePositionTime.visibility = View.GONE
                    mLlCoreCustomerItemPositionInTime.visibility = View.GONE
                    mTvCurrentStatus.setBackgroundColor(resources.getColor(R.color.blueDFEAFF))
                }
                CURRENT_STATUS_ENTER -> {
                    mTvCurrentStatus.text = "已入职"
                    mLlCoreCustomerItemLeavePositionTime.visibility = View.GONE
                    mLlCoreCustomerItemPositionInTime.visibility = View.VISIBLE
                    mTvCurrentStatus.setBackgroundColor(resources.getColor(R.color.redFFFFE9DD))
                }
                CURRENT_STATUS_QUIT -> {
                    mTvCurrentStatus.text = "已离职"
                    mLlCoreCustomerItemLeavePositionTime.visibility = View.VISIBLE
                    mLlCoreCustomerItemPositionInTime.visibility = View.VISIBLE
                    mTvCurrentStatus.setBackgroundColor(resources.getColor(R.color.redFFF0EDE5))
                }
            }
            //最近注记
            if (item.lastContactComment != null) {
                val lastContactCommentTime = FormatUtils.timeStamp2String(item.lastContactTime, EnumValue.DATE_FORMAT_1)
                holder.setText(R.id.tvCoreCustomerItemLastMark, lastContactCommentTime + " " + item.lastContactComment)
            }else{
                holder.setText(R.id.tvCoreCustomerItemLastMark, "无")
            }
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
        return R.layout.fragment_core_customer
    }

    override fun initParams() {
    }

    override fun initViews() {
        statusLayout.showLoading()
        initTabLayout()
        recyclerViewCoreFragmentCustomer.layoutManager = LinearLayoutManager(context)
        recyclerViewCoreFragmentCustomer.adapter = mAdapter
        recyclerViewCoreFragmentCustomer.addItemDecoration(SimpleItemDecoration.builder(context)
                .isDrawTopFirstLine(false)
                .build())
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
        mAdapter.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<CoreCustomerInfo> {
            override fun onItemClick(view: View, itemData: CoreCustomerInfo, i: Int) {
                val mDetailInfo = mDetails[itemData.userId]
                if (mDetailInfo == null) {
                    mPresenter.getCustomerDetailInfo(itemData.userId)
                } else {
                    jumpCustomerDetail(mDetailInfo)
                }
            }

        })
        rlCoreFragmentCustomerSearch.setOnClickListener {
            CustomerSearchActivity.jump(context!!, CustomerSearchActivity.TYPE_CORE)
            CommonUtils.statistics(context, Constants.STATISTICS_coreCustomer_SEARCH_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
        }
        ivCoreFragmentCustomerSearchDelete.setOnClickListener {
            mKeyWord = null
            tvCoreFragmentCustomerSearch.text = ""
            rlCoreFragmentCustomerSearch.visibility = View.GONE
            getCustomerData()
        }
    }

    private fun jumpCustomerDetail(mDetailInfo: CustomerDetailInfo) {
        CustomerDetailActivity.jump(context!!, mDetailInfo)
        CommonUtils.statistics(context, Constants.STATISTICS_coreCustomer_customerDetail_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
    }

    override fun initData() {
        getCustomerData()
    }

    private fun initTabLayout() {
        dropDownTabLayoutCoreFragmentCustomer.setSumItemSize(4)
        dropDownTabLayoutCoreFragmentCustomer.addItem("排序")
        dropDownTabLayoutCoreFragmentCustomer.addItem("状态")
        dropDownTabLayoutCoreFragmentCustomer.addItem(getString(R.string.sort_type_intent_level))
        dropDownTabLayoutCoreFragmentCustomer.addItem("筛选客户")
        dropDownTabLayoutCoreFragmentCustomer.setOnItemOnClickListener { view, position ->
            when (position) {
                0 -> {
                    showSortCondition()
                }
                1 -> {
                    showCurrentStatusCondition()
                }
                2 -> {
                    showIntentLvCondition()
                }
                3 -> showCustomerCondition()
            }
        }
    }

    private fun showSortCondition() {
        if (mCustomerSortConditionAdapter == null) {
            mCustomerSortConditionAdapter = DropDownSelectMenuAdapter()
            mCustomerSortConditionAdapter!!.addItem("按签到时间")
            mCustomerSortConditionAdapter!!.addItem("按入职时间")
            mCustomerSortConditionAdapter!!.addItem("按离职时间")
            mCustomerSortConditionAdapter!!.addItem("按注册时间")
            mCustomerSortConditionAdapter!!.addItem("按注记时间")
        }
        if (mDropDownSelectMenuSort == null) {
            mDropDownSelectMenuSort = DropDownSelectMenu(activity, mCustomerSortConditionAdapter)
            mCustomerSortConditionAdapter!!.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<String> {
                override fun onItemClick(view: View, itemData: String, i: Int) {
                    mDropDownSelectMenuSort!!.dismiss()
                    mCustomerSortConditionAdapter!!.selectPosition(i)
                    dropDownTabLayoutCoreFragmentCustomer.itemTitle = itemData
                    smartRefreshLayout.autoRefresh()
                    getCustomerData()
                    CommonUtils.statistics(context, Constants.STATISTICS_coreCustomer_SORT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
            })
            mDropDownSelectMenuSort!!.popupWindow.setOnDismissListener {
                dropDownTabLayoutCoreFragmentCustomer.closeMenu()
            }
        }
        mDropDownSelectMenuSort!!.show(dropDownTabLayoutCoreFragmentCustomer)
    }

    private fun showCurrentStatusCondition() {
        if (mCustomerCurrentStatusConditionAdapter == null) {
            mCustomerCurrentStatusConditionAdapter = DropDownSelectMenuAdapter()
            mCustomerCurrentStatusConditionAdapter!!.addItem("不限")
            mCustomerCurrentStatusConditionAdapter!!.addItem("已签到")
            mCustomerCurrentStatusConditionAdapter!!.addItem("已入职")
            mCustomerCurrentStatusConditionAdapter!!.addItem("已离职")
            mCustomerCurrentStatusConditionAdapter!!.selectPosition(0)
        }
        if (mDropDownSelectMenuCurrentStatus == null) {
            mDropDownSelectMenuCurrentStatus = DropDownSelectMenu(activity, mCustomerCurrentStatusConditionAdapter)
            mCustomerCurrentStatusConditionAdapter!!.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<String> {
                override fun onItemClick(view: View, itemData: String, i: Int) {
                    mDropDownSelectMenuCurrentStatus!!.dismiss()
                    mCustomerCurrentStatusConditionAdapter!!.selectPosition(i)
                    dropDownTabLayoutCoreFragmentCustomer.itemTitle = itemData
                    smartRefreshLayout.autoRefresh()
                    getCustomerData()
                    CommonUtils.statistics(context, Constants.STATISTICS_coreCustomer_filterCurrentStatus_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
            })
            mDropDownSelectMenuCurrentStatus!!.popupWindow.setOnDismissListener {
                dropDownTabLayoutCoreFragmentCustomer.closeMenu()
            }
        }
        mDropDownSelectMenuCurrentStatus!!.show(dropDownTabLayoutCoreFragmentCustomer)
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
        if (mDropDownSelectMenuIntentLv == null) {
            mDropDownSelectMenuIntentLv = DropDownSelectMenu(activity, mCustomerIntentLvConditionAdapter)
            mCustomerIntentLvConditionAdapter!!.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<String> {
                override fun onItemClick(view: View, itemData: String, i: Int) {
                    mDropDownSelectMenuIntentLv!!.dismiss()
                    mCustomerIntentLvConditionAdapter!!.selectPosition(i)
                    dropDownTabLayoutCoreFragmentCustomer.itemTitle = itemData
                    smartRefreshLayout.autoRefresh()
                    getCustomerData()
                    CommonUtils.statistics(context, Constants.STATISTICS_coreCustomer_filterCurrentStatus_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
            })
            mDropDownSelectMenuIntentLv!!.popupWindow.setOnDismissListener {
                dropDownTabLayoutCoreFragmentCustomer.closeMenu()
            }
        }
        mDropDownSelectMenuIntentLv!!.show(dropDownTabLayoutCoreFragmentCustomer)
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
                    dropDownTabLayoutCoreFragmentCustomer.itemTitle = itemData
                    smartRefreshLayout.autoRefresh()
                    getCustomerData()
                    CommonUtils.statistics(context, Constants.STATISTICS_activeCustomer_FILTER_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
            })
            mDropDownSelectMenuCondition!!.popupWindow.setOnDismissListener {
                dropDownTabLayoutCoreFragmentCustomer.closeMenu()
            }
        }
        mDropDownSelectMenuCondition!!.show(dropDownTabLayoutCoreFragmentCustomer)
    }

    override fun getCoreCustomersSuccess(data: CommonListBody<CoreCustomerInfo>) {
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

    override fun getCoreCustomersFailure(isNetWorkError: Boolean) {
        if (isNetWorkError)
            statusLayout.showNetworkError()
        else
            statusLayout.showLoadError()
        finishSmartRefreshLayout()
    }

    override fun onCustomerDetailInfoGetSuccess(content: CustomerDetailInfo, userId: String) {
        mDetails[userId] = content
        jumpCustomerDetail(content)
    }

    private fun finishSmartRefreshLayout() {
        if (mCurrentPage == 1) {
            smartRefreshLayout.finishRefresh()
        } else {
            smartRefreshLayout.finishLoadMore()
        }
    }

    fun getCustomerData() {
        var sort: String? = null
        if (mCustomerSortConditionAdapter != null) {
            //有筛选排序
            val selectPosition = mCustomerSortConditionAdapter!!.getSelectPosition()
            if (selectPosition >= 0) {
                when (selectPosition) {
                    0 -> sort = "SIGN_DATE"
                    1 -> sort = "ENTER_TIME"
                    2 -> sort = "QUIT_TIME"
                    3 -> sort = "REGISTER_TIME"
                    4 -> sort = "CONTACT_TIME"
                }
            }
        }
        var status: Int? = null
        if (mCustomerCurrentStatusConditionAdapter != null) {
            //有状态筛选
            val selectPosition = mCustomerCurrentStatusConditionAdapter!!.getSelectPosition()
            if (selectPosition > 0) {
                when (selectPosition) {
                    1 -> status = CURRENT_STATUS_SIGN
                    2 -> status = CURRENT_STATUS_ENTER
                    3 -> status = CURRENT_STATUS_QUIT
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
            //筛选联系时间
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

        mPresenter.getCoreCustomers(pageNo = mCurrentPage, sort = sort, status = status,wish = wish, notContacted = notContacted, term = mKeyWord)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getNormalCustomerEventMessage(eventData: CoreCustomerEventMessage) {
        mKeyWord = eventData.content
        rlCoreFragmentCustomerSearch.visibility = View.VISIBLE
        tvCoreFragmentCustomerSearch.text = mKeyWord
        smartRefreshLayout.autoRefresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(context, Constants.STATISTICS_coreCustomer_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)

    }

}