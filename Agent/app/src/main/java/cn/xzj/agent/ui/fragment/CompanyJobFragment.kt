package cn.xzj.agent.ui.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.PopupWindow
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.CityInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.PositionRequestPermissionInfo
import cn.xzj.agent.entity.job.JobFeature
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.entity.job.RewardConditionInfo
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import cn.xzj.agent.iview.IJobListView
import cn.xzj.agent.presenter.CompanyJobPresenter
import cn.xzj.agent.ui.adapter.CityFilterConditionAdapter
import cn.xzj.agent.ui.adapter.CompanyJobListAdapter
import cn.xzj.agent.ui.adapter.JobFeatureAdapter
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.ui.company.CompanyJobSearchActivity
import cn.xzj.agent.ui.job.JobDetailActivity
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.DialogUtil
import cn.xzj.agent.util.StatusBarUtil
import com.channey.utils.StringUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_company_job.*

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/27
 * @Des 企业列表
 */
class CompanyJobFragment : MVPBaseFragment<CompanyJobPresenter>(), IJobListView {

    private var mDataAdapter = CompanyJobListAdapter()
    private var mCurrentPage = 1
    private lateinit var mFeatureAdapter: JobFeatureAdapter
    private var mCityList = ArrayList<CityInfo>()
    private var mSelectedCity: CityInfo? = null
    private lateinit var mFeaturePopupWindow: PopupWindow//岗位特征popupWindow
    private var mJobFeatures = ArrayList<JobFeature>()//岗位特征数据
    private var mSelectedFeatures: ArrayList<String>? = null
    private var mTotalCount = 0
    override fun initLayout(): Int {
        return R.layout.fragment_company_job
    }

    override fun initParams() {
    }

    override fun initViews() {
        statusLayoutCompanyJob.showLoading()
        dropDownTabLayoutFragmentJob.setSumItemSize(2)
        dropDownTabLayoutFragmentJob.addItem("工作城市")
        dropDownTabLayoutFragmentJob.addItem("岗位特征")
        initFeaturePopupWindow()
        //RecyclerView
        recyclerViewFragmentJob.layoutManager = LinearLayoutManager(context)
        recyclerViewFragmentJob.adapter = mDataAdapter
        mDataAdapter.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<JobInfo> {
            override fun onItemClick(view: View, itemData: JobInfo, i: Int) {
                gotoPositionDetail(i)
            }
        })
        mDataAdapter.setOnRewardConditionBtnClickListener(object : QuickAdapter.OnItemClickListener<JobInfo> {
            override fun onItemClick(view: View, itemData: JobInfo, i: Int) {
                var info = mJobList[i]
                if (info.recruitmentNeeds.isEmpty()){
                    DialogUtil.showNoticeDialog(context(),title = "无招聘需求",listener = View.OnClickListener {
                        DialogUtil.dismissNoticeDialog()
                    })
                }else{
                    mPresenter.getRewardCondition(info.recruitmentNeeds[0].id)
                }
            }
        })
    }

    override fun setListeners() {
        super.setListeners()
        dropDownTabLayoutFragmentJob.setOnItemOnClickListener { view, position ->
            when (position) {
                0 -> {
                    if (mCityList.isNotEmpty()) {
                        showCityFilterCondition(mCityList)
                    } else {
                        mPresenter.getCities()
                    }
                }
                1 -> {
                    //弹出岗位特征ui
                    if (mJobFeatures == null || mJobFeatures.isEmpty()) {
                        mPresenter.getJobFeatures()
                    } else {
                        mFeaturePopupWindow.showAtLocation(dropDownTabLayoutFragmentJob, Gravity.CENTER, 0, 0)
                    }

                }
            }
        }
        //刷新控件
        smartRefreshLayoutFragmentJob.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mCurrentPage++
                getJobs()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mCurrentPage = 1
                getJobs()
            }
        })
        statusLayoutCompanyJob.setOnRetryListener {
            getJobs()
        }
        iv_company_job_search.setOnClickListener {
            CompanyJobSearchActivity.jump(context!!)
            CommonUtils.statistics(context, Constants.STATISTICS_companyPosition_thinkSearch_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
        }
    }

    override fun initData() {
        getJobs()
    }

    override fun loadData() {
        super.loadData()
        getJobs()
    }

    override fun context(): Context {
        return context!!
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(context, Constants.STATISTICS_companyPosition_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    override fun onCityListGetSuccess(list: List<CityInfo>) {
        mCityList.clear()
        mCityList.addAll(list)
        mCityList.add(0, CityInfo("-1", "不限"))
        showCityFilterCondition(mCityList)
    }

    private var mJobList = ArrayList<JobInfo>()
    override fun onJobListGetSuccess(info: CommonListBody<JobInfo>) {
        mTotalCount = info.totalCount
        if (mCurrentPage == 1) mJobList.clear()
        mJobList.addAll(info.items)
        if (mCurrentPage == 1) {
            mDataAdapter.setNewData(info.items)
        } else {
            mDataAdapter.addAll(info.items)
            mDataAdapter.notifyDataSetChanged()
        }
        if (mTotalCount != 0) {
            statusLayoutCompanyJob.showContent()
        } else {
            statusLayoutCompanyJob.showEmpty()
        }
        if (mCurrentPage == 1) {
            smartRefreshLayoutFragmentJob.finishRefresh()
        } else {
            smartRefreshLayoutFragmentJob.finishLoadMore()
        }
        smartRefreshLayoutFragmentJob.setNoMoreData(mTotalCount <= mDataAdapter.data!!.size)
        mDataAdapter.notifyDataSetChanged()
    }

    override fun onJobListGetFailure() {
        if (mDataAdapter.data!!.size == 0) {
            statusLayoutCompanyJob.showLoadError()
        }
        if (mCurrentPage == 1) {
            smartRefreshLayoutFragmentJob.finishRefresh()
        } else {
            smartRefreshLayoutFragmentJob.finishLoadMore()
        }
    }

    override fun onPermissionGetSuccess(info: PositionRequestPermissionInfo, position: Int) {
    }

    override fun onGetJobFeaturesSuceess(jobFeatures: List<JobFeature>) {
        mJobFeatures.clear()
        mJobFeatures.addAll(jobFeatures)
        mFeatureAdapter.notifyDataSetChanged()
        mFeaturePopupWindow.showAtLocation(dropDownTabLayoutFragmentJob, Gravity.CENTER, 0, 0)
    }

    override fun onGetJobFeaturesFailure() {
    }

    private fun getJobs() {
        val cityId: String?
        if (mSelectedCity == null || mSelectedCity!!.name.equals("不限")) {
            cityId = null
        } else {
            cityId = mSelectedCity!!.id
        }
        mPresenter.getJobList(cityId = cityId, pageNo = mCurrentPage,  term = null, features = mSelectedFeatures)
    }

    private var mCityPopupWindow: PopupWindow? = null
    private fun showCityFilterCondition(conditions: List<CityInfo>) {
        if (mCityPopupWindow == null) {
            val popupView = LayoutInflater.from(context).inflate(R.layout.pop_filter_condition, null)
            val mRecyclerView = popupView.findViewById<RecyclerView>(R.id.pop_filter_condition_list)
            val adapter = CityFilterConditionAdapter(context!!, conditions, mSelectedCity)
            adapter.setOnRecyclerViewItemClickListener(object : OnRecyclerViewItemClickListener {
                override fun onClick(view: View, position: Int) {
                    mCityPopupWindow!!.dismiss()
                    mSelectedCity = conditions[position]
                    dropDownTabLayoutFragmentJob.itemTitle = mSelectedCity!!.name
                    getJobs()
                }
            })
            val layoutManager = LinearLayoutManager(context)
            mRecyclerView.adapter = adapter
            mRecyclerView.layoutManager = layoutManager
            mCityPopupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            mCityPopupWindow!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
            mCityPopupWindow!!.isFocusable = true
            mCityPopupWindow!!.isOutsideTouchable = true
            mCityPopupWindow!!.setOnDismissListener {
                dropDownTabLayoutFragmentJob.closeMenu()
            }
            mCityPopupWindow!!.update()
        }
        mCityPopupWindow!!.showAsDropDown(viewTabLayoutFragmentJob, 0, 0)
        CommonUtils.statistics(context, Constants.STATISTICS_companyPosition_FILTER_CITY_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
    }

    private fun initFeaturePopupWindow() {
        val mJobFeatureView = LayoutInflater.from(context).inflate(R.layout.popupwindow_job_feature, null, false)
        val mLvJobFeature = mJobFeatureView.findViewById<ExpandableListView>(R.id.lv_job_feature)
        mLvJobFeature.setGroupIndicator(null)
        mFeatureAdapter = JobFeatureAdapter(context, mJobFeatures)
        mLvJobFeature.setAdapter(mFeatureAdapter)
        mFeatureAdapter.setOnSelectedFeatureListener {
            mSelectedFeatures = it
            getJobs()
            CommonUtils.statistics(context, Constants.STATISTICS_companyPosition_FILTER_FEATURE_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
        }

        mFeaturePopupWindow = PopupWindow(mJobFeatureView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mFeaturePopupWindow.isOutsideTouchable = true
        mFeaturePopupWindow.isFocusable = true
        mFeaturePopupWindow.animationStyle = R.style.popupWindowRightInRightOut
        mFeaturePopupWindow.setOnDismissListener {
            dropDownTabLayoutFragmentJob.closeMenu()
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

    private fun gotoPositionDetail(position: Int) {
        JobDetailActivity.jumpForResult(activity!!,
                jobId = mDataAdapter.data!![position].positionId,
                userId = null,
                requestCode = Code.RequestCode.POSITION_DETAIL, userName = null)
    }

    override fun onRewardConditionGetSuccess(condition: List<RewardConditionInfo>?) {
        if (condition == null || condition.isEmpty()){
            DialogUtil.showNoticeDialog(context(),title = "无提成条件",listener = View.OnClickListener {
                DialogUtil.dismissNoticeDialog()
            })
        }else{
            var sb = StringBuilder()
            for (i in 0 until condition.size){
                var info = condition[i]
                if (!(sb.contains("正常返费") || sb.contains("小时工"))){
                    var value = StringBuilder()
                    var commissionValueMaleStr = StringUtils.doubleFormat(info.commissionValueMale)
                    var commissionValueFemaleStr = StringUtils.doubleFormat(info.commissionValueFemale)
                    if (StringUtils.isNotEmpty(commissionValueMaleStr)){
                        value.append("男$commissionValueMaleStr ")
                    }
                    if (StringUtils.isNotEmpty(commissionValueFemaleStr)){
                        value.append("女$commissionValueFemaleStr")
                    }
                    when(info.commissionType){
                        "REWARD" -> {
                            sb.append("正常返费 ")
                            when(info.commissionKey){
                                "REWARD_DAYS_PUNCH" -> sb.append("出勤打卡满${info.condition}天 $value")
                                "REWARD_DAYS_INSERVICE" -> sb.append("在职满${info.condition}天 $value")
                                "REWARD_DAYS_WORK" -> sb.append("工作满${info.condition}天 $value")
                                "REWARD_HOURS_WORK" -> sb.append("出勤满${info.condition}小时 $value")
                            }
                        }
                        "HW" -> {
                            sb.append("小时工 ")
                            when(info.commissionKey){
                                "HW_SERVICE" -> sb.append("${StringUtils.doubleFormat(info.commissionValueMale)}元/小时")
                            }
                        }
                    }
                }
                if (i < condition.size-1) sb.append(";")
            }
            DialogUtil.showNoticeDialog(context(),title = "提成条件",content = sb.toString(),listener = View.OnClickListener {
                DialogUtil.dismissNoticeDialog()
            })
        }
    }

    override fun onRewardConditionGetFailure() {

    }
}