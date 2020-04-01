package cn.xzj.agent.ui.mine

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.BaseActivity
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.customer.RequestRecordInfo
import cn.xzj.agent.iview.IMyTeamView
import cn.xzj.agent.presenter.MyTeamPresenter
import cn.xzj.agent.ui.adapter.LowerAgentListAdapter
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.ui.adapter.common.decoration.LineSplitDecoration
import cn.xzj.agent.ui.adapter.common.decoration.SimpleItemDecoration
import cn.xzj.agent.util.CommonUtils
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.activity_my_team.*

class MyTeamActivity:MVPBaseActivity<MyTeamPresenter>(),IMyTeamView {

    private var mAgentId:String ?= null
    private var mSupperAgentId:String ?= null
    private var mCurrentPage = 1
    private var mTotalCount = 0
    private lateinit var mAdapter: LowerAgentListAdapter
    private var mLowerAgents = ArrayList<AgentInfo>()
    companion object{
        val key_agentId = "agentId"
        val key_supperAgentId = "supperAgentId"
        fun jump(context:BaseActivity,agentId:String,supperAgentId:String?,requestCode:Int = Code.RequestCode.AREA_SELECT){
            var intent = Intent(context,MyTeamActivity::class.java)
            intent.putExtra(key_agentId,agentId)
            if (supperAgentId != null) intent.putExtra(key_supperAgentId,supperAgentId)
            context.startActivityForResult(intent,requestCode)
        }
    }

    override fun context(): Context {
        return this
    }

    override fun initParams() {
        super.initParams()
        mAgentId = intent.getStringExtra(key_agentId)
        mSupperAgentId = intent.getStringExtra(key_supperAgentId)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_my_team
    }

    override fun initViews() {
        setTitle("我的团队")
        initRecyclerView()
        if (StringUtils.isEmpty(mSupperAgentId)){
            activity_my_team_supper_title.visibility = View.GONE
            activity_my_team_supper_group.visibility = View.GONE
        }else{
            activity_my_team_supper_title.visibility = View.VISIBLE
            activity_my_team_supper_group.visibility = View.VISIBLE
        }
    }

    private fun initRecyclerView() {
        mAdapter = LowerAgentListAdapter()
        mAdapter.data = mLowerAgents
        activity_my_team_member_list.layoutManager = LinearLayoutManager(this)
        activity_my_team_member_list.adapter = mAdapter
        activity_my_team_member_smartRefreshLayout.isEnableLoadMore = true
        var decoration = LineSplitDecoration(this,splitSize = resources.getDimension(R.dimen.dp_10).toInt())
        activity_my_team_member_list.addItemDecoration(decoration)
        activity_my_team_member_smartRefreshLayout.setOnRefreshListener {
            mCurrentPage = 1
            getLowerAgents()
        }
        activity_my_team_member_smartRefreshLayout.setOnLoadMoreListener {
            mCurrentPage++
            getLowerAgents()
        }
    }

    private fun loadingComplete() {
        if (mCurrentPage <= 1) {
            activity_my_team_member_smartRefreshLayout.finishRefresh()
        } else {
            activity_my_team_member_smartRefreshLayout.finishLoadMore()
        }

        activity_my_team_member_smartRefreshLayout.setNoMoreData(mTotalCount <= mAdapter.data!!.size)
        if (mLowerAgents.isEmpty()){
            showLowerAgentGroup(false)
        }else{
            showLowerAgentGroup(true)
        }
        //是否显示空页面
        if (StringUtils.isEmpty(mSupperAgentId) && mLowerAgents.isEmpty()){
            showEmptyView(true)
        }else{
            showEmptyView(false)
        }
    }

    private fun getLowerAgents(){
        mPresenter.getLowerAgents(mAgentId!!,mCurrentPage)
    }

    override fun initData() {
        super.initData()
        if (StringUtils.isNotEmpty(mSupperAgentId)) mPresenter.getAgentInfo(mSupperAgentId!!)
        mPresenter.getLowerAgents(mAgentId!!,mCurrentPage)
    }

    override fun onAgentInfoGetSuccess(agentInfo: AgentInfo) {
        injectSupperAgentView(agentInfo)
    }

    override fun onAgentInfoGetFailure() {
    }

    private fun injectSupperAgentView(info:AgentInfo){
        activity_my_team_supperAgentName_tv.text = "姓名：${info.name}"
        activity_my_team_supperAgentPhone_tv.text = StringUtils.phoneNumberParseWithStars(info.phone)
        activity_my_team_supperAgentNickName_tv.text = "昵称：${info.nick}"
        ShapeUtil.setShape(activity_my_team_supper_group,radius = resources.getDimension(R.dimen.dp_4),solidColor = resources.getColor(R.color.white))
    }

    override fun onLowerAgentsGetSuccess(body: CommonListBody<AgentInfo>) {
        mTotalCount = body.totalCount
        if (mCurrentPage == 1) mLowerAgents.clear()
        mLowerAgents.addAll(body.items)
        mAdapter.notifyDataSetChanged()
        activity_my_team_member_lowerAgentCount_tv.text = "你的团队目前共有${mLowerAgents.size}个成员"
        loadingComplete()
    }

    private fun showLowerAgentGroup(shown:Boolean){
        if (shown){
            activity_my_team_member_lowerAgentCount_group.visibility = View.VISIBLE
            activity_my_team_member_smartRefreshLayout.visibility = View.VISIBLE
        }else{
            activity_my_team_member_lowerAgentCount_group.visibility = View.GONE
            activity_my_team_member_smartRefreshLayout.visibility = View.GONE
        }
    }

    private fun showEmptyView(shown:Boolean){
        if (shown){
            activity_my_team_member_emptyView_img.visibility = View.VISIBLE
            activity_my_team_member_emptyView_tv.visibility = View.VISIBLE
        }else{
            activity_my_team_member_emptyView_img.visibility = View.GONE
            activity_my_team_member_emptyView_tv.visibility = View.GONE
        }
    }
    override fun onLowerAgentsGetFailure() {
        loadingComplete()
    }
}