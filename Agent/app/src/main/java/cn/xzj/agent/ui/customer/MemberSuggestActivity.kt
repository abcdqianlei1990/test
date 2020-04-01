package cn.xzj.agent.ui.customer

import android.content.Context
import android.content.Intent
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.iview.IMemberSuggestView
import cn.xzj.agent.presenter.MemberSuggestPresenter
import cn.xzj.agent.util.DialogUtil
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.activity_member_suggest.*

class MemberSuggestActivity: MVPBaseActivity<MemberSuggestPresenter>(), View.OnClickListener, IMemberSuggestView {

    private var mCustomerDetailInfo:CustomerDetailInfo? = null
    companion object {
        val key_customerInfo = "customerInfo"
        fun jump(context: Context, customerInfo: CustomerDetailInfo) {
            val intent = Intent(context, MemberSuggestActivity::class.java)
            intent.putExtra(key_customerInfo, customerInfo)
            context.startActivity(intent)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_member_suggest
    }

    override fun context(): Context {
        return this
    }

    override fun initParams() {
        mCustomerDetailInfo = intent.getParcelableExtra(key_customerInfo)
    }
    override fun initViews() {
        activity_member_suggest_name_tv.text = mCustomerDetailInfo!!.userName
        setTitle("推荐关系")
    }

    override fun setListeners() {
        activity_member_suggest_btn.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.activity_member_suggest_btn -> {
                    if (mCustomerDetailInfo != null){
                        var name = activity_member_suggest_inviteeName_tv.text.toString()
                        if (StringUtils.isEmpty(name)){
                            showToast("请输入被邀请人姓名")
                            return
                        }
                        var phone = activity_member_suggest_inviteePhone_tv.text.toString()
                        if (StringUtils.isEmpty(phone)){
                            showToast("请输入被邀请人手机号")
                            return
                        }
                        DialogUtil.showNoticeDialogWithCancelConfirm(this,title = "确定绑定邀请关系？"
                                ,confirmListener = View.OnClickListener {
                            DialogUtil.dismissCancelConfirmDialog()
                            mPresenter.memberSuggest(name,phone,mCustomerDetailInfo!!.userId)
                        })
                    }
                }
            }
        }
    }

    override fun onSuggestFailure(msg: String) {
        showToast(msg)
    }

    override fun onSuggestSuccess(success: Boolean) {
        if (success){
            showToast("邀请成功")
            finish()
        }else{
            showToast("邀请失败")
        }
    }
}