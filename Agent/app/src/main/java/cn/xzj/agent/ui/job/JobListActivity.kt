package cn.xzj.agent.ui.job

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.PopupWindow
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.CityInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.DefaultEventMessage
import cn.xzj.agent.entity.RefreshCustomerRecordesMessage
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.PositionRequestPermissionInfo
import cn.xzj.agent.entity.job.JobFeature
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.entity.job.JobTypeFilterInfo
import cn.xzj.agent.entity.job.RewardConditionInfo
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import cn.xzj.agent.iview.IJobListView
import cn.xzj.agent.presenter.JobListPresenter
import cn.xzj.agent.ui.adapter.CityFilterConditionAdapter
import cn.xzj.agent.ui.adapter.FilterConditionAdapter
import cn.xzj.agent.ui.adapter.JobFeatureAdapter
import cn.xzj.agent.ui.adapter.JobListAdapter
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.StatusBarUtil
import cn.xzj.agent.widget.SimpleToast
import com.channey.utils.FormatUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_job_list.*
import org.greenrobot.eventbus.EventBus

/**
 *  岗位列表
 */
class JobListActivity : MVPBaseActivity<JobListPresenter>(), View.OnClickListener, IJobListView {
    private var mCurrentPage = 1
    private var mTotalCount = 0
    private var mJobFilterTypeList = ArrayList<JobTypeFilterInfo>()
    private var mSelectedPositionListType: String = EnumValue.JOB_FILTER_TYPE_SHIPPING
    private var mCustomerDetailInfo: CustomerDetailInfo? = null
    private var mCityList = ArrayList<CityInfo>()
    private var mSelectedCity: CityInfo? = null
    private lateinit var mFeaturePopupWindow: PopupWindow//岗位特征popupWindow
    private var mJobFeatures = ArrayList<JobFeature>()//岗位特征数据
    private var mSelectedFeatures: ArrayList<String>? = null
    private var term: String? = null

    companion object {
        fun jump(context: Context, customerDetailInfo: CustomerDetailInfo) {
            val intent = Intent(context, JobListActivity::class.java)
            intent.putExtra(Keys.CUSTOMER_DETAIL_INFO, customerDetailInfo)
            context.startActivity(intent)
        }
    }

    override fun context(): Context {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_job_list
    }

    override fun initParams() {
        initFilterTypeData()
        mCustomerDetailInfo = intent.getParcelableExtra(Keys.CUSTOMER_DETAIL_INFO)
    }

    override fun initViews() {
        setLifeBack()
        setTitle("推荐岗位")
        dropDownTabLayout.setSumItemSize(3)
        dropDownTabLayout.addItem("企业急招")
        dropDownTabLayout.addItem("工作城市")
        dropDownTabLayout.addItem("岗位特征")
        initRecyclerView()
        initFeaturePopupWindow()
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_recommendPosition_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    override fun initData() {
        smartRefreshLayout.autoRefresh()
    }

    override fun onDefaultEvent(event: DefaultEventMessage) {
        super.onDefaultEvent(event)
        if (event.code == 0) {
            EventBus.getDefault().post(RefreshCustomerRecordesMessage())
            finish()
        } else if (event.code == 1) {
            //搜索返回
            term = event.data.toString()
            ll_search_history.visibility = View.VISIBLE
            rl_search.visibility = View.GONE
            tv_search.text = term
            hideToolbar()
            getJobs()
        } else if (event.code == 2) {
            resetSearch()
            getJobs()
        }
    }

    private fun resetSearch() {
        showToolbar()
        ll_search_history.visibility = View.GONE
        rl_search.visibility = View.VISIBLE
        tv_search.text = null
        term = null
    }

    override fun setListeners() {
        activity_job_list_emptyview.setOnClickListener(this)
        rl_search.setOnClickListener(this)
        dropDownTabLayout.setOnItemOnClickListener { view, position ->
            when (position) {
                0 -> {
                    showFilterCondition(mJobFilterTypeList)
                    CommonUtils.statistics(this, Constants.STATISTICS_recommendPosition_FILTER_SHIPPING_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
                1 -> {
                    if (mCityList.isNotEmpty()) {
                        showCityFilterCondition(mCityList)
                    } else {
                        mPresenter.getCities()
                    }
                }
                2 -> {
                    //弹出岗位特征ui
                    if (mJobFeatures == null || mJobFeatures.isEmpty()) {
                        mPresenter.getJobFeatures()
                    } else {
                        mFeaturePopupWindow.showAtLocation(popup_window_host, Gravity.CENTER,0, 0)
                        CommonUtils.statistics(this, Constants.STATISTICS_recommendPosition_FILTER_CITY_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                    }

                }
            }
        }

        StatusBarUtil.setPadding(this, ll_search_history)
        //点击头部搜索结果
        ll_search_history.setOnClickListener {
            //跳转到搜索页面
            JobSearchActivity.jump(this, mCustomerDetailInfo!!, term)
            CommonUtils.statistics(this, Constants.STATISTICS_recommendPosition_thinkSearch_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
        }
        //点击取消搜索
        ic_search_delete.setOnClickListener {
            resetSearch()
            getJobs()
        }
        //返回
        iv_job_back.setOnClickListener {
            finish()
        }


    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.activity_job_list_emptyview -> {
            }
            R.id.rl_search -> {
                JobSearchActivity.jump(this, mCustomerDetailInfo!!, term)
                CommonUtils.statistics(this, Constants.STATISTICS_recommendPosition_thinkSearch_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
        }
    }

    private fun initFilterTypeData() {
        mJobFilterTypeList.add(JobTypeFilterInfo(EnumValue.JOB_FILTER_TYPE_INCENTIVE, EnumValue.JOB_FILTER_TYPE_INCENTIVE_DESC))
        mJobFilterTypeList.add(JobTypeFilterInfo(EnumValue.JOB_FILTER_TYPE_HOURWORK, EnumValue.JOB_FILTER_TYPE_HOURWORK_DESC))
        mJobFilterTypeList.add(JobTypeFilterInfo(EnumValue.JOB_FILTER_TYPE_SHIPPING, EnumValue.JOB_FILTER_TYPE_SHIPPING_DESC))
        mJobFilterTypeList.add(JobTypeFilterInfo(EnumValue.JOB_FILTER_TYPE_ZX, EnumValue.JOB_FILTER_TYPE_ZX_DESC))
    }

    private fun initRecyclerView() {
        activity_job_list_recyclerview.layoutManager = LinearLayoutManager(this)
        activity_job_list_recyclerview.adapter = mDataAdapter
        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mCurrentPage++
                getJobs()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mCurrentPage = 1
                getJobs()
            }
        })
        mDataAdapter.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<JobInfo> {
            override fun onItemClick(view: View, itemData: JobInfo, i: Int) {
                //周薪职位需要签署协议
                if (itemData.applied) {
                    SimpleToast.showShort("该用户已申请该职位")
                }else {
                    if (itemData.positionTypes != null && itemData.positionTypes.contains("周薪")) {
                        mPresenter.positionRequestPermissionCheck(mCustomerDetailInfo!!.userId, i)
                    } else {
                        gotoPositionDetail(i)
                    }
                }
            }
        })
    }


    private fun showEmptyView(shown: Boolean) {
        if (shown) {
            activity_job_list_emptyview.visibility = View.VISIBLE
            activity_job_list_recyclerview.visibility = View.GONE
        } else {
            activity_job_list_emptyview.visibility = View.GONE
            activity_job_list_recyclerview.visibility = View.VISIBLE
        }
    }

    private fun getJobs() {
        val cityId: String?
        if (mSelectedCity == null || mSelectedCity!!.name.equals("不限")) {
            cityId = null
        } else {
            cityId = mSelectedCity!!.id
        }
        mPresenter.getJobList(cityId = cityId, pageNo = mCurrentPage, positionListType = mSelectedPositionListType, userId = mCustomerDetailInfo!!.userId, term = term, features = mSelectedFeatures)
    }


    private var mPopupWindow: PopupWindow? = null
    private fun showFilterCondition(conditions: List<JobTypeFilterInfo>) {
        val popupView = LayoutInflater.from(this).inflate(R.layout.pop_filter_condition, null)
        val mRecyclerView = popupView.findViewById<RecyclerView>(R.id.pop_filter_condition_list)
        val adapter = FilterConditionAdapter(this, conditions, mSelectedPositionListType)
        adapter.setOnRecyclerViewItemClickListener(object : OnRecyclerViewItemClickListener {
            override fun onClick(view: View, position: Int) {
                dismissFilterCondition()
                dropDownTabLayout.itemTitle = mJobFilterTypeList[position].desc
                mSelectedPositionListType = mJobFilterTypeList[position].type
                getJobs()
            }
        })
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = layoutManager


        mPopupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mPopupWindow!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        mPopupWindow!!.isFocusable = true
        mPopupWindow!!.isOutsideTouchable = true
        mPopupWindow!!.setOnDismissListener {
            activity_job_list_cover_view.visibility = View.GONE
            dropDownTabLayout.closeMenu()

        }
        activity_job_list_cover_view.visibility = View.VISIBLE
        mPopupWindow!!.update()
        mPopupWindow!!.showAsDropDown(popup_window_host, 0, 0)
    }

    private var mCityPopupWindow: PopupWindow? = null
    private fun showCityFilterCondition(conditions: List<CityInfo>) {
        val popupView = LayoutInflater.from(this).inflate(R.layout.pop_filter_condition, null)
        val mRecyclerView = popupView.findViewById<RecyclerView>(R.id.pop_filter_condition_list)
        val adapter = CityFilterConditionAdapter(this, conditions, mSelectedCity)
        adapter.setOnRecyclerViewItemClickListener(object : OnRecyclerViewItemClickListener {
            override fun onClick(view: View, position: Int) {
                mCityPopupWindow!!.dismiss()
                mSelectedCity = conditions[position]
                dropDownTabLayout.itemTitle = mSelectedCity!!.name
                getJobs()
            }
        })
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = layoutManager


        mCityPopupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mCityPopupWindow!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        mCityPopupWindow!!.isFocusable = true
        mCityPopupWindow!!.isOutsideTouchable = true
        mCityPopupWindow!!.setOnDismissListener {
            activity_job_list_cover_view.visibility = View.GONE
            dropDownTabLayout.closeMenu()
        }
        activity_job_list_cover_view.visibility = View.VISIBLE
        mCityPopupWindow!!.update()
        mCityPopupWindow!!.showAsDropDown(popup_window_host, 0, 0)
    }

    private fun initFeaturePopupWindow() {
        val mJobFeatureView = LayoutInflater.from(this).inflate(R.layout.popupwindow_job_feature, null, false)
        val mLvJobFeature = mJobFeatureView.findViewById<ExpandableListView>(R.id.lv_job_feature)
        mLvJobFeature.setGroupIndicator(null)
        mFeatureAdapter = JobFeatureAdapter(this, mJobFeatures)
        mLvJobFeature.setAdapter(mFeatureAdapter)
        mFeatureAdapter.setOnSelectedFeatureListener {
            mSelectedFeatures = it
            getJobs()
            CommonUtils.statistics(this, Constants.STATISTICS_recommendPosition_FILTER_FEATURE_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
        }

        mFeaturePopupWindow = PopupWindow(mJobFeatureView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mFeaturePopupWindow.isOutsideTouchable = true
        mFeaturePopupWindow.isFocusable = true
        mFeaturePopupWindow.animationStyle=R.style.popupWindowRightInRightOut
        mFeaturePopupWindow.setOnDismissListener {
            dropDownTabLayout.closeMenu()
        }
        mFeaturePopupWindow.update()
        mJobFeatureView.setOnClickListener {
            mFeaturePopupWindow.dismiss()
        }
        //重置按钮
        mJobFeatureView.findViewById<Button>(R.id.btn_reset).setOnClickListener {
            mFeatureAdapter.removeSelectedFeature()
        }
        //关闭按钮
        mJobFeatureView.findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            mFeaturePopupWindow.dismiss()
        }
    }

    fun dismissFilterCondition() {
        if (mPopupWindow != null) {
            mPopupWindow!!.dismiss()

        }
    }

    override fun onJobListGetSuccess(info: CommonListBody<JobInfo>) {
        mTotalCount = info.totalCount
        if (mCurrentPage == 1) {
            mDataAdapter.setNewData(info.items)
        } else {
            mDataAdapter.addAll(info.items)
        }
        if (mDataAdapter.data!!.size == 0) {
            showEmptyView(true)
        } else {
            showEmptyView(false)
        }
        if (mCurrentPage == 1) {
            smartRefreshLayout.finishRefresh()
        } else {
            smartRefreshLayout.finishLoadMore()
        }
        smartRefreshLayout.setNoMoreData(mTotalCount <= mDataAdapter.data!!.size)
        mDataAdapter.notifyDataSetChanged()
    }

    override fun onCityListGetSuccess(list: List<CityInfo>) {
        mCityList.clear()
        mCityList.addAll(list)
        mCityList.add(0, CityInfo("-1", "不限"))
        showCityFilterCondition(mCityList)
    }

    override fun onPermissionGetSuccess(info: PositionRequestPermissionInfo, position: Int) {
        //因为已申请过的不能点击，所以此处只判断周薪且未签署协议的情况
        if (info.contractAgreed) {
            gotoPositionDetail(position)
        } else {
            SimpleToast.showShort(getString(R.string.zx_agreement_notice))
        }
    }

    private fun gotoPositionDetail(position: Int) {
        JobDetailActivity.jumpForResult(this,
                jobId = mDataAdapter.data!![position].positionId,
                userId = mCustomerDetailInfo!!.userId,
                requestCode = Code.RequestCode.POSITION_DETAIL, userName = mCustomerDetailInfo!!.userName)
    }

    override fun onJobListGetFailure() {
        if (mCurrentPage == 1) {
            smartRefreshLayout.finishRefresh()
        } else {
            smartRefreshLayout.finishLoadMore()
        }
    }

    override fun onGetJobFeaturesSuceess(jobFeatures: List<JobFeature>) {
        mJobFeatures.clear()
        mJobFeatures.addAll(jobFeatures)
        mFeatureAdapter.notifyDataSetChanged()
        mFeaturePopupWindow.showAtLocation(popup_window_host, Gravity.CENTER,0, 0)
    }

    override fun onGetJobFeaturesFailure() {
    }


    private var mDataAdapter = JobListAdapter()

    private lateinit var mFeatureAdapter: JobFeatureAdapter

    override fun onRewardConditionGetSuccess(condition: List<RewardConditionInfo>?) {

    }

    override fun onRewardConditionGetFailure() {

    }

}