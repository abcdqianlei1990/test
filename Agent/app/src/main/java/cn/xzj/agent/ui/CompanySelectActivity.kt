package cn.xzj.agent.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.customer.WorkExperienceCompanyInfo
import cn.xzj.agent.presenter.CompanySelectPresenter
import cn.xzj.agent.ui.adapter.CompanySelectListAdapter
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import kotlinx.android.synthetic.main.activity_company_select.*

class CompanySelectActivity:MVPBaseActivity<CompanySelectPresenter>() {
    private var list = ArrayList<WorkExperienceCompanyInfo>()
    companion object{
        const val key_companies = "companies"
        const val PARAM_KEY_POSITIONNAME = "position_name"
        const val PARAM_KEY_APPLYID = "apply_id"
        fun jump(activity: Activity, companies:ArrayList<WorkExperienceCompanyInfo>,requestCode:Int = Code.RequestCode.CompanySelectActivity){
            var intent = Intent(activity, CompanySelectActivity::class.java)
            intent.putExtra(key_companies,companies)
            activity.startActivityForResult(intent,requestCode)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_company_select
    }

    override fun context(): Context {
        return applicationContext
    }

    override fun initParams() {
        super.initParams()
        list = intent.getParcelableArrayListExtra<WorkExperienceCompanyInfo>(key_companies)
    }

    override fun initViews() {
        initRecyclerView()
    }

    private fun initRecyclerView(){
        var adapter = CompanySelectListAdapter()
        adapter.data = list
        adapter.mOnItemClickListener = object : QuickAdapter.OnItemClickListener<WorkExperienceCompanyInfo> {
            override fun onItemClick(view: View, itemData: WorkExperienceCompanyInfo, i: Int) {

            }

        }
        activity_commpanylist_recyclerview.adapter = adapter
        var layoutManager = LinearLayoutManager(this)
        activity_commpanylist_recyclerview.layoutManager = layoutManager
        adapter.notifyDataSetChanged()
    }
}