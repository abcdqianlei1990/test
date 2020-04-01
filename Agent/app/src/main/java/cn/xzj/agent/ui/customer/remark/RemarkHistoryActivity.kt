package cn.xzj.agent.ui.customer.remark

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.MarkJsonInfo
import cn.xzj.agent.entity.customer.RemarkHistoryInfo
import cn.xzj.agent.iview.IRemarkHistoryView
import cn.xzj.agent.presenter.RemarkHistoryPresenter
import cn.xzj.agent.ui.adapter.RemarkHistoryAdapter
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.StatusBarUtil
import cn.xzj.agent.util.Util
import com.alibaba.fastjson.JSON
import kotlinx.android.synthetic.main.activity_remark_history.*

/**
 * 历史备注
 */
class RemarkHistoryActivity : MVPBaseActivity<RemarkHistoryPresenter>(), IRemarkHistoryView {

    private lateinit var mAdapter: RemarkHistoryAdapter
    private var mList = ArrayList<RemarkHistoryInfo>()
    private var mCurrentPage = 1;
    private var mTotalCount = 0
    private var mUserId: String? = null
    private var mSituationMap = HashMap<String,String>()

    companion object {
        fun jump(context: Context, userId: String) {
            val intent = Intent(context, RemarkHistoryActivity::class.java)
            intent.putExtra(Keys.USER_ID, userId)
            context.startActivity(intent)
        }
    }

    override fun context(): Context {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_remark_history
    }

    override fun initParams() {
//        StatusBarUtil.darkMode(this)
//        StatusBarUtil.setPadding(this, rl_parent)
        mUserId = intent.getStringExtra(Keys.USER_ID)
    }

    override fun initViews() {
        setLifeBack()
        setTitle("历史注记")
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_historyNote_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    override fun initData() {
        val json = Util.getJsonFromAssets(this, "mark.json")
        var mMarkJsonInfo = JSON.parseObject(json, MarkJsonInfo::class.java)
        for (o in mMarkJsonInfo.situations){
            mSituationMap[o.key] = o.value
        }
        for (o in mMarkJsonInfo.reason){
            mSituationMap[o.key] = o.value
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        smartRefreshLayout.autoRefresh()
    }


    private fun initRecyclerView() {
        mAdapter = RemarkHistoryAdapter(this, mList)
        mAdapter.setSituationMap(mSituationMap)
        activity_remark_history_recyclerview.adapter = mAdapter
        activity_remark_history_recyclerview.layoutManager = LinearLayoutManager(this)
        smartRefreshLayout.isEnableLoadMore = true
        smartRefreshLayout.setOnRefreshListener {
            mCurrentPage = 1
            getHistories()
        }
        smartRefreshLayout.setOnLoadMoreListener {
            mCurrentPage++
            getHistories()
        }
    }

    private fun refreshList() {
        if (mList.size == 0) {
            statusLayoutManager.showEmptyData()
        } else {
            statusLayoutManager.showContent()
        }
        loadingComplete()
        mAdapter.notifyDataSetChanged()
    }

    private fun loadingComplete() {
        if (mCurrentPage == 1) {
            smartRefreshLayout.finishRefresh()
        } else {
            smartRefreshLayout.finishLoadMore()
        }
        smartRefreshLayout.setNoMoreData(!(mTotalCount > mList.size))
    }

    private fun getHistories() {
        mPresenter.getHistories(mUserId!!, mCurrentPage)
    }

    override fun onHistoryGetSuccess(info: CommonListBody<RemarkHistoryInfo>) {
        mTotalCount = info.totalCount
        if (mCurrentPage == 1) {
            mList.clear()
        }
        mList.addAll(info.items)
        refreshList()
    }

    override fun onHistoryGetFailure() {

    }

}