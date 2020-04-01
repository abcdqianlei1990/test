package cn.xzj.agent.ui

import android.content.Context
import android.content.Intent
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.entity.common.SignUpPostBody
import cn.xzj.agent.entity.store.StoreInfo
import cn.xzj.agent.i.PickerSelectedListener
import cn.xzj.agent.presenter.UserRegisterPresenter
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.Util
import com.channey.timerbutton.TimerButton
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.activity_user_register.*

class UserRegisterActivity():BaseActivity(),View.OnClickListener {
    private val TITLE = "代用户注册"
    private var mPhoneNumber:String ?= null
    private var mCaptcha:String ?= null
    private var mName:String ?= null
    private var mSex:String ?= null
    private var mCardNumber:String ?= null
    private lateinit var mPresenter: UserRegisterPresenter
    private val male = "1"
    private val female = "2"
    private var mStoresList = ArrayList<StoreInfo>()
    private var mStoreSelectedPosition = -1

    companion object {
        fun jump(context:Context){
            var intent = Intent(context,UserRegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
    
    override fun initLayout(): Int {
       return R.layout.activity_user_register
    }

    override fun initParams() {
        mPresenter = UserRegisterPresenter(this)
    }

    override fun initViews() {
        setTitle(TITLE)
        initTimerButton()
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_REGISTER_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    override fun initData() {

    }

    override fun setListeners() {
        activity_user_register_submit_btn.setOnClickListener(this)
        activity_user_register_storename_group.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.activity_user_register_storename_group -> {
                if(mStoresList.isNotEmpty()){
                    showStoresPicker()
                }else{
                    mPresenter.getStores()
                }
            }
            R.id.activity_user_register_submit_btn -> {
                val inputCheckOk = inputCheck()
                if (inputCheckOk){
                    showNoticeDialogWithCancelConfirm(title = "确定给此用户注册",
                            content = "",
                            confirmText = "确定",
                            cancelListener = View.OnClickListener {
                                dismissCancelConfirmDialog()
                            },
                            confirmListener = View.OnClickListener {
                                dismissCancelConfirmDialog()
                                var storeId:String ?= null
                                var storeName:String ?= null
                                if(mStoreSelectedPosition != -1){
                                    var i = mStoresList[mStoreSelectedPosition]
                                    storeId = i.storeId
                                    storeName = i.storeName
                                }
                                var body = SignUpPostBody(storeId,storeName,mCaptcha!!,mCardNumber,mName!!,mPhoneNumber!!,mSex!!)
                                mPresenter.signUp(body)
                                CommonUtils.statistics(this, Constants.STATISTICS_REGISTER_SUBMIT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                            })
                }
            }
        }
    }

    private fun initTimerButton(){
        activity_user_register_timerbtn.setOnClickListener {
            var checkOk = cellphoneNumberCheck()
            if (checkOk){
                activity_user_register_timerbtn.start()
                mPresenter.getCaptcha(mPhoneNumber!!)
            }
        }
        activity_user_register_timerbtn.setOnCountingListener(object : TimerButton.CountingListener {
            override fun onCounting(l: Long) {
//                setTimerBtnBg(false)
                val sp = setCustomerColor((l / 1000).toString() + "s后重发")
                activity_user_register_timerbtn.text = sp
            }

            override fun onCountingFinish() {
//                setTimerBtnBg(true)
                activity_user_register_timerbtn.text = getString(R.string.btn_get_captcha)
            }
        })
    }

    private fun inputCheck():Boolean{
        cellphoneNumberCheck()
        mCaptcha = activity_user_register_captcha_ed.text.toString()
        if (StringUtils.isEmpty(mCaptcha)){
            toast(msg = "验证码不能为空")
            return false
        }
        mName = activity_user_register_name_ed.text.toString()
        if (StringUtils.isEmpty(mName)){
            toast(msg = "姓名不能为空")
            return false
        }
        if (!Util.isChinese(mName!!)){
            toast(msg = "姓名只能是中文")
            return false
        }

        if (!activity_user_register_sex_male.isChecked && !activity_user_register_sex_female.isChecked){
            toast(msg = "性别不能为空")
            return false
        }else{
            mSex = if (activity_user_register_sex_male.isChecked) male else female
        }
        mCardNumber = activity_user_register_idnum_ed.text.toString()
        return true
    }

    private fun cellphoneNumberCheck():Boolean{
        mPhoneNumber = activity_user_register_phonenumber_ed.text.toString()
        if (StringUtils.isEmpty(mPhoneNumber)){
            toast(msg = "手机号不能为空")
            return false
        }
        if (mPhoneNumber!!.length < 11){
            toast("请输入正确的手机号")
            return false
        }
        return true
    }

    fun onCaptchaGetFailure(msg:String){
        toast(msg)
    }

    fun onSignUpSuccess(){
        toast("注册成功")
        finish()
    }

    fun onSignUpFailure(msg:String){
        toast(msg)
    }

    fun onStoresGetSuccess(list:List<StoreInfo>){
        mStoresList.clear()
        mStoresList.addAll(list)
        showStoresPicker()
    }

    fun onStoresGetFailure(msg:String){toast(msg)}

    private fun showStoresPicker(){
        var data = ArrayList<String>()
        for (i in mStoresList){
            data.add(i.storeName)
        }
        showPickerPopWindow("门店",data,activity_user_register_storename_tv.text.toString(),object : PickerSelectedListener {
            override fun onSelected(position: Int, selection: String) {
                mStoreSelectedPosition = position
                activity_user_register_storename_tv.text = selection
            }
        })
    }
}