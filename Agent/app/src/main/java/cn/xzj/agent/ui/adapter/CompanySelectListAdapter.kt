package cn.xzj.agent.ui.adapter

import cn.xzj.agent.R
import cn.xzj.agent.entity.customer.WorkExperienceCompanyInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter

class CompanySelectListAdapter : QuickAdapter<WorkExperienceCompanyInfo>(R.layout.item_companyselect) {
    override fun convert(holder: BaseHolder, item: WorkExperienceCompanyInfo, position: Int) {
        holder.setText(R.id.item_companylist_name_tv, item.positionName)
    }
}