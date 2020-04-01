package cn.xzj.agent.ui.customer

import android.content.Context
import android.content.Intent
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.RealNameVerificationBody
import cn.xzj.agent.iview.IIDCardCertificationView
import cn.xzj.agent.presenter.IDCardCertificationPresenter
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.widget.CommonDialog
import cn.xzj.agent.widget.SimpleToast
import kotlinx.android.synthetic.main.activity_id_card_certification.*

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/17
 * @Des  实名认证页面
 */
class IDCardCertificationActivity : MVPBaseActivity<IDCardCertificationPresenter>(), IIDCardCertificationView {
    private lateinit var mCustomerDetailInfo: CustomerDetailInfo

    companion object {
        fun jump(context: Context, mCustomerDetailInfo: CustomerDetailInfo) {
            val mIntent = Intent()
            mIntent.setClass(context, IDCardCertificationActivity::class.java)
            mIntent.putExtra(Keys.DATA_KEY_1, mCustomerDetailInfo)
            context.startActivity(mIntent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_id_card_certification
    }

    override fun context(): Context {
        return this
    }

    override fun initParams() {
        super.initParams()
        mCustomerDetailInfo = intent.getParcelableExtra(Keys.DATA_KEY_1)
    }

    override fun initViews() {
        setTitle(getString(R.string.activity_id_card_certification_title))
        setLifeBack()
        //设置用户手机号
        et_id_card_certification_phone.setText(mCustomerDetailInfo.phone)
        et_id_card_certification_phone.setSelection(et_id_card_certification_phone.text.length)
        //手机号不能更改
        et_id_card_certification_phone.isEnabled = false
    }

    override fun setListeners() {
        super.setListeners()
        //提交按钮
        btn_et_id_card_certification_commit.setOnClickListener {
            val phone = et_id_card_certification_phone.text.toString().trim()
            val idCardNumber = et_id_card_certification_id_card_number.text.toString().trim()
            val name = et_id_card_certification_name.text.toString().trim()
            //验证非空
            if (phone.isEmpty()) {
                SimpleToast.showNormal(et_id_card_certification_phone.hint.trim().toString())
                return@setOnClickListener
            }
            if (name.isEmpty()) {
                SimpleToast.showNormal(et_id_card_certification_name.hint.toString())
                return@setOnClickListener
            }
            if (idCardNumber.isEmpty()) {
                SimpleToast.showNormal(et_id_card_certification_id_card_number.hint.toString())
                return@setOnClickListener
            }
            val realNameVerificationBody = RealNameVerificationBody(idCardNumber, name, mCustomerDetailInfo.userId)
            //提交
            mPresenter.commit(realNameVerificationBody)
            CommonUtils.statistics(this, Constants.STATISTICS_realNameAuthentication_SUBMIT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)

        }
    }

    override fun onIDCardCertificationSuccess() {
        CommonDialog.newBuilder(this)
                .setMessage(getString(R.string.activity_id_card_certification_certification_success))
                .setNegativeButton(getString(R.string.activity_id_card_certification_i_know)) {
                    it.cancel()
                    finish()
                }
                .create()
                .show()
    }

    override fun onIDCardCertificationFail(content: String) {
        SimpleToast.showNormal(content)
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_realNameAuthentication_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)

    }
}