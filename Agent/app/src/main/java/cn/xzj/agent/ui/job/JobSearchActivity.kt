package cn.xzj.agent.ui.job

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.DefaultEventMessage
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.job.JobSearchSuggestion
import cn.xzj.agent.iview.IJobSearchView
import cn.xzj.agent.presenter.JobSearchPresenter
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.CommonListAdapter
import cn.xzj.agent.ui.adapter.common.CommonViewHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.ui.adapter.common.decoration.SimpleItemDecoration
import cn.xzj.agent.util.*
import cn.xzj.agent.widget.SimpleToast
import com.alibaba.fastjson.JSON
import kotlinx.android.synthetic.main.activity_job_search.*
import org.greenrobot.eventbus.EventBus

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/10
 * @ Des 岗位搜索页面
 */
class JobSearchActivity : MVPBaseActivity<JobSearchPresenter>(), IJobSearchView {

    private lateinit var mACache: ACache
    private var mCustomerDetailInfo: CustomerDetailInfo? = null
    private var term:String?=null
    private var mHistoryAdapter = object : CommonListAdapter<String>(this, R.layout.item_job_search_history) {
        override fun convert(viewHolder: CommonViewHolder, item: String?, position: Int) {
            viewHolder.setText(R.id.tv_value, item)
        }
    }
    private var mQuickResultAdapter = object : QuickAdapter<JobSearchSuggestion>(R.layout.item_job_search_quick_result) {
        override fun convert(holder: BaseHolder, item: JobSearchSuggestion, position: Int) {
            holder.setText(R.id.tv_value, item.positionName)
        }

    }

    companion object {
        fun jump(context: Context, customerDetailInfo: CustomerDetailInfo?,term:String?) {
            val intent = Intent(context, JobSearchActivity::class.java)
            intent.putExtra(Keys.CUSTOMER_DETAIL_INFO, customerDetailInfo)
            intent.putExtra(Keys.DATA_KEY_1,term)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_job_search
    }

    override fun initParams() {
        super.initParams()
        mCustomerDetailInfo = intent.getParcelableExtra(Keys.CUSTOMER_DETAIL_INFO)
        term=intent.getStringExtra(Keys.DATA_KEY_1)
    }

    override fun initViews() {
        hideToolbar()
        StatusBarUtil.setPadding(this, rl_top_parent)
        tcl_history.setAdapter(mHistoryAdapter)
        rv_quick_result.layoutManager = LinearLayoutManager(this)
        rv_quick_result.adapter = mQuickResultAdapter
        rv_quick_result.addItemDecoration(SimpleItemDecoration.builder(this)
                .build())
        if (term!=null){
            et_search.setText(term)
        }
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_thinkSearch_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    override fun initData() {
        super.initData()
        val mHistoryData: List<String>
        mACache = ACache.get(this)
        val jobSearchHistory = mACache.getAsString(Keys.JOB_SEARCH_HISTORY)
        if (!TextUtils.isEmpty(jobSearchHistory)) {
            mHistoryData = JSON.parseArray(jobSearchHistory, String::class.java)
            mHistoryAdapter.clear()
            mHistoryAdapter.addAllItem(mHistoryData)
        } else {
            mHistoryData = ArrayList<String>()
            mHistoryAdapter.addAllItem(mHistoryData)
        }
    }

    override fun setListeners() {
        super.setListeners()
        tv_search_cancel.setOnClickListener {
            EventBus.getDefault().post(DefaultEventMessage(2, "搜索数据", null))
            finish()
        }
        ic_search_delete.setOnClickListener {
            et_search.setText("")
        }
        ic_search_history_delete.setOnClickListener {
            mHistoryAdapter.clear()
            mHistoryAdapter.notifyDataSetChanged()
        }
        tcl_history.setItemClickListener {
            EventBus.getDefault().post(DefaultEventMessage(1, "搜索数据", mHistoryAdapter.data[it]))
            finish()
        }
        mQuickResultAdapter.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<JobSearchSuggestion> {
            override fun onItemClick(view: View, itemData: JobSearchSuggestion, i: Int) {
                //跳转到详情
                if (mCustomerDetailInfo!=null) {
                    JobDetailActivity.jumpForResult(view.context as Activity,
                            jobId = itemData.positionId,
                            userId = mCustomerDetailInfo!!.userId,
                            requestCode = Code.RequestCode.POSITION_DETAIL,
                            userName = mCustomerDetailInfo!!.userName)
                }else{
                    JobDetailActivity.jumpForResult(view.context as Activity,
                            jobId = itemData.positionId,
                            userId = null,
                            requestCode = Code.RequestCode.POSITION_DETAIL,
                            userName = null)
                }
                mHistoryAdapter.addItem(itemData.positionName)
                mHistoryAdapter.notifyDataSetChanged()
                CommonUtils.statistics(this@JobSearchActivity, Constants.STATISTICS_thinkSearch_positionDetail_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
        })
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.isEmpty()) {
                    ic_search_delete.visibility = View.GONE
                    rl_search_history.visibility = View.VISIBLE
                    rv_quick_result.visibility = View.GONE
                    mQuickResultAdapter.clearData()
                } else {
                    ic_search_delete.visibility = View.VISIBLE
                    rl_search_history.visibility = View.GONE
                    rv_quick_result.visibility = View.VISIBLE
                    mPresenter.getSearchSuggestions(p0.toString())
                    CommonUtils.statistics(this@JobSearchActivity, Constants.STATISTICS_thinkSearch_INPUT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
            }
        })
        et_search.setOnEditorActionListener(TextView.OnEditorActionListener { p0, p1, p2 ->
            if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                //监听搜索动作
                if (TextUtils.isEmpty(p0.text.toString().trim())) {
                    SimpleToast.showShort(p0.hint.toString())
                    return@OnEditorActionListener true
                }
                KeyBoardUtil.close(this)
                if (mHistoryAdapter.data.size == 10) {
                    mHistoryAdapter.removeItem(9)
                    mHistoryAdapter.addItem(0, p0.text.toString().trim())
                } else {
                    mHistoryAdapter.addItem(0, p0.text.toString().trim())
                }
                mHistoryAdapter.notifyDataSetChanged()
                CommonUtils.statistics(this@JobSearchActivity, Constants.STATISTICS_thinkSearch_BTN_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                EventBus.getDefault().post(DefaultEventMessage(1, "搜索数据", p0.text.toString().trim()))
                finish()

                return@OnEditorActionListener true
            }
            false
        })
        statusLayoutManager.rootLayout.setOnTouchListener { view, motionEvent ->
            KeyBoardUtil.close(this)
            return@setOnTouchListener false
        }
    }

    override fun onPause() {
        super.onPause()
        //关闭软键盘
        KeyBoardUtil.close(this)
        try {
            //保存岗位搜索缓存
            mACache.put(Keys.JOB_SEARCH_HISTORY, JSON.toJSONString(mHistoryAdapter.data))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getSearchSuggestionsSuccess(data: List<JobSearchSuggestion>, term: String) {
        LogLevel.w("getSearchSuggestionsSuccess", ""+et_search.text.toString()+ "mSearchIndex $term")
        if (et_search.text.toString() == term) {
            //最新一次索引
            mQuickResultAdapter.setNewData(data)

        }
    }

    override fun context(): Context {
        return this
    }

    override fun showContent() {
    }

    override fun showLoading() {
    }

    override fun showEmpty() {
    }

    override fun showError() {
    }

    override fun showNetworkError() {
    }

    override fun onDefaultEvent(event: DefaultEventMessage) {
        super.onDefaultEvent(event)
        if (event.code == 1) {
            //初始状态页面
            et_search.setText("")
        }
    }
}
