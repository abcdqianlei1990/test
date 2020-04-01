package cn.xzj.agent.ui.customer.remark

import android.view.View


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AlertDialog
import android.support.v7.widget.*
import android.text.InputType
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.RegionJsonPickerBean
import cn.xzj.agent.entity.customer.City
import cn.xzj.agent.entity.customer.RemarkVO
import cn.xzj.agent.i.MultiSelectListener
import cn.xzj.agent.iview.IUserInfoRemarkView
import cn.xzj.agent.presenter.UserInfoRemarkPresenter
import cn.xzj.agent.ui.adapter.MultiSelectorAdapter
import cn.xzj.agent.ui.location.SelectExpectCityActivity
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.widget.CommonDialog
import cn.xzj.agent.widget.SimpleBottomSheetDialog
import cn.xzj.agent.widget.SimpleLineItemView
import cn.xzj.agent.widget.SimpleToast
import com.aigestudio.wheelpicker.WheelPicker
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.fragment_userinfo_remark_01.*
import java.util.logging.Handler

class RemarkFragment : MVPBaseFragment<UserInfoRemarkPresenter>(), IUserInfoRemarkView {
    private lateinit var mRemarkVO: RemarkVO
    private var mCityList: ArrayList<City>? = null
    private var mLocationRegion: RegionJsonPickerBean? = null
    override fun context(): Context {
        return context!!
    }

    override fun showContent() {
    }

    override fun showLoading() {
    }

    override fun initLayout(): Int {
        return R.layout.fragment_userinfo_remark_01
    }

    override fun initParams() {
        mRemarkVO = RemarkVO()
    }

    override fun initViews() {
        btn_commit.setOnClickListener {
            commitRemark()
        }
    }

    override fun showEmpty() {
    }

    override fun showError() {
    }

    override fun showNetworkError() {
    }

    override fun initData() {
        mPresenter.getRemarkInfo((activity as RemarkActivity).getUserId()!!)
    }

    override fun getRegionsSuccess(cityList: ArrayList<City>) {
        this.mCityList = cityList
        selectProvinceCity()
    }

    override fun getRegionsFail() {
    }

    override fun onRemarkFailure() {
    }

    override fun onRemarkSuccess() {
        SimpleToast.showNormal("用户信息备注成功")
    }

    override fun onRemarkGetFailure() {
    }

    override fun onRemarkGetSuccess(info: RemarkVO) {
        setData(info)
    }

    override fun getLocationRegionSuccess(region: RegionJsonPickerBean) {
        mLocationRegion = region
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Code.RequestCode.PROVINCE_SELECT -> {
                if (resultCode == Code.ResultCode.OK) {
                    if (data != null) {
                        val city = data.getSerializableExtra(Keys.CITY) as City
                        mRemarkVO.expectCity = city.value
                        (rlParent.getChildAt(8) as SimpleLineItemView).editText.setText(city.name)
                    }
                }
            }
        }
    }

    private fun setData(info: RemarkVO) {
        this.mRemarkVO = info
        rlParent.removeAllViews()
        rlParent.addView(SimpleLineItemView.Builder(context)
                .content("常用称呼")
                .editHint("请输入常用称呼")
                .value(if (info.nickname == null) "" else info.nickname)
                .showDividerBottom()
                .onlyMark("常用称呼")
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .content("微信号")
                .editHint("请填写微信号")
                .value(if (info.wechat == null) "" else info.wechat)
                .showDividerBottom()
                .onlyMark("微信号")
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("性别")
                .content("性别")
                .value(info.strSex)
                .editHint("请选择性别")
                .editEnable(false)
                .setOnRootClickListener({
                    SimpleBottomSheetDialog.newBuilder(context)
                            .setData("男", "女")
                            .setItemClicklistener { v, s, position ->
                                (rlParent.getChildAt(2) as SimpleLineItemView).editText.setText(s)
                                mRemarkVO.sex = (position + 1).toString()
                            }
                            .show()

                }, "性别")
                .showRightArrow()
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("年龄")
                .content("年龄")
                .editHint("请输入用户年龄")
                .inputType(InputType.TYPE_CLASS_PHONE)
                .value(if (info.age == null) "" else info.age)
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("学历")
                .content("学历")
                .value(if (info.education == null) "" else info.education)
                .editHint("请选择学历")
                .setOnRootClickListener({
                    SimpleBottomSheetDialog.newBuilder(context)
                            .setData(context!!.resources.getStringArray(R.array.education).toList())
                            .setItemClicklistener { v, s, position ->
                                (rlParent.getChildAt(4) as SimpleLineItemView).editText.setText(s)
                                mRemarkVO.education = s
                            }
                            .show()
                }, "学历"
                )
                .editEnable(false)
                .showRightArrow()
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("婚姻状况")
                .content("婚姻状况")
                .value(info.strMarital)
                .editHint("请选择婚姻状况")
                .setOnRootClickListener({
                    SimpleBottomSheetDialog.newBuilder(context)
                            .setData("已婚", "未婚", "订婚", "有男女朋友", "单身")
                            .setItemClicklistener { v, s, position ->
                                (rlParent.getChildAt(5) as SimpleLineItemView).editText.setText(s)
                                when (s) {
                                    "已婚" ->
                                        mRemarkVO.marital = "MARRIED"
                                    "未婚" ->
                                        mRemarkVO.marital = "UNMARRIED"
                                    "订婚" ->
                                        mRemarkVO.marital = "GOT_ENGAGED"
                                    "有男女朋友" ->
                                        mRemarkVO.marital = "HAVING_BG_FRIEND"
                                    "单身" ->
                                        mRemarkVO.marital = "SINGLE"

                                }
                            }
                            .show()
                }, "婚姻状况"
                )
                .editEnable(false)
                .showRightArrow()
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("当前所在地")
                .content("当前所在地")
                .value(if (info.currentProvince == null) "" else "${info.currentProvince} ${info.currentCity} ${info.currentArea}")
                .editHint("请选择当前所在地")
                .setOnRootClickListener({
                    if (mLocationRegion != null) {
                        showCurrentPickerView()
                    } else {
                        mPresenter.getLocationRegions()
                    }

                }, "当前所在地"
                )
                .editEnable(false)
                .showRightArrow()
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("求职方向")
                .content("求职方向")
                .value(if (info.expectPosition == null) "" else info.expectPosition)
                .editHint("请选择求职方向")
                .setOnRootClickListener({
                    SimpleBottomSheetDialog.newBuilder(context)
                            .setData("工厂普工", "服务业", "司机家政", "不限")
                            .setItemClicklistener { v, s, position ->
                                (rlParent.getChildAt(7) as SimpleLineItemView).editText.setText(s)
                                mRemarkVO.expectPosition = s
                            }
                            .show()
                }, "求职方向"
                )
                .editEnable(false)
                .showRightArrow()
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("期望工作地")
                .content("期望工作地")
                .value(if (info.expectCityName == null) "" else info.expectCityName)
                .editHint("请选择期望工作地")
                .setOnRootClickListener({
                    if (mCityList == null) {
                        mPresenter.getRegions()
                    } else {
                        selectProvinceCity()
                    }
                }, "期望工作地"
                )
                .editEnable(false)
                .showRightArrow()
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("求职紧迫度")
                .content("求职紧迫度")
                .value(if (mRemarkVO.expectOnboardingDate == null) "" else mRemarkVO.expectOnboardingDate)
                .editHint("请选择求职紧迫度")
                .setOnRootClickListener({
                    SimpleBottomSheetDialog.newBuilder(context)
                            .setData("三天到岗", "一周到岗", "两周到岗", "一个月到岗")
                            .setItemClicklistener { v, s, position ->
                                (rlParent.getChildAt(9) as SimpleLineItemView).editText.setText(s)
                                mRemarkVO.expectOnboardingDate = s
                            }
                            .show()
                }, "求职紧迫度"
                )
                .editEnable(false)
                .showRightArrow()
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("籍贯老家")
                .content("籍贯老家")
                .value(if (info.birthplaceProvince == null) "" else "${info.birthplaceProvince} ${info.birthplaceCity}")
                .editHint("请选择籍贯老家")
                .setOnRootClickListener({
                    if (mLocationRegion != null) {
                        showBirthplacePickerView()
                    } else {
                        mPresenter.getLocationRegions()
                    }
                }, "籍贯老家"
                )
                .editEnable(false)
                .showRightArrow()
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("出门多久")
                .content("出门多久")
                .editHint("请填写出门多久")
                .value(if (info.goOut == null) "" else info.goOut)
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("找工作多久")
                .content("找工作多久")
                .editHint("请填写出门多久")
                .value(if (info.findJob == null) "" else info.findJob)
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("特殊标记")
                .content("特殊标记")
                .value(if (info.strTag == null) "" else info.strTag.toString().replace("[", "").replace("]", ""))
                .editHint("请选择特殊标记")
                .setOnRootClickListener({
                    showMultiSelectPopWindow("请选择特殊标记", mRemarkVO.strTag, context!!.resources.getStringArray(R.array.special_character).toList(), object : MultiSelectListener {
                        override fun onSelect(selection: List<String>) {
                            if (mMultiSelectorPopupWindow != null) mMultiSelectorPopupWindow!!.dismiss()
                            if (mRemarkVO.tags == null)
                                mRemarkVO.tags = ArrayList()
                            mRemarkVO.tags.clear()
                            for (tag in selection) {
                                mRemarkVO.tags.add(mRemarkVO.getTagId(tag))
                            }
                            (rlParent.getChildAt(13) as SimpleLineItemView).editText.setText(mRemarkVO.strTag.toString().replace("[", "").replace("]", ""))
                        }

                    })
                }, "特殊标记"
                )
                .editEnable(false)
                .showRightArrow()
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("抱团/单人")
                .content("抱团/单人")
                .value(if (info.peoples == null) "" else info.peoples)
                .editHint("请选择抱团/单人")
                .setOnRootClickListener({
                    //                    SimpleBottomSheetDialog.newBuilder(context)
//                            .setData("1人", "2人", "3人", "多人")
//                            .setItemClicklistener { v, s, position ->
//                                (rlParent.getChildAt(14) as SimpleLineItemView).editText.setText(s)
//                                mRemarkVO.peoples = s
//                            }
//                            .show()
                    mPeopleCommonDialog!!.setBottomShow()
                }, "抱团/单人"
                )
                .editEnable(false)
                .showRightArrow()
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("技能")
                .content("技能")
                .value(info.strSkillTag)
                .editHint("请选择技能")
                .setOnRootClickListener({
                    SimpleBottomSheetDialog.newBuilder(context)
                            .setData(context!!.resources.getStringArray(R.array.skill_flag).toList())
                            .setItemClicklistener { v, s, position ->
                                (rlParent.getChildAt(15) as SimpleLineItemView).editText.setText(s)

                            }
                            .show()
                }, "技能"
                )
                .editEnable(false)
                .showRightArrow()
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("当前月薪")
                .content("当前月薪")
                .value(if (info.currentSalary == null) "" else info.currentSalary)
                .editHint("请选择当前月薪")
                .setOnRootClickListener({
                    SimpleBottomSheetDialog.newBuilder(context)
                            .setData("3000-4000", "4000-5000", "5000-6000", "6000以上")
                            .setItemClicklistener { v, s, position ->
                                (rlParent.getChildAt(16) as SimpleLineItemView).editText.setText(s)
                                mRemarkVO.currentSalary = s
                            }
                            .show()
                }, "当前月薪"
                )
                .editEnable(false)
                .showRightArrow()
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("在离职状态")
                .content("在离职状态")
                .value(info.strWorkingStatus)
                .editHint("请选择在离职状态")
                .setOnRootClickListener({
                    SimpleBottomSheetDialog.newBuilder(context)
                            .setData(context!!.resources.getStringArray(R.array.work_status).toList())
                            .setItemClicklistener { v, s, position ->
                                (rlParent.getChildAt(17) as SimpleLineItemView).editText.setText(s)
                                when (s) {
                                    "在职" ->
                                        mRemarkVO.onboarding = 1
                                    "离职" ->
                                        mRemarkVO.onboarding = 2
                                }
                            }
                            .show()
                }, "在离职状态"
                )
                .editEnable(false)
                .showRightArrow()
                .showDividerBottom()
                .build())
        rlParent.addView(SimpleLineItemView.Builder(context)
                .onlyMark("痛点")
                .content("痛点")
                .editHint("请输入痛点")
                .value(if (info.painPoints == null) "" else info.painPoints)
                .showDividerBottom()
                .build())
        //无效用户标记
        var view = LayoutInflater.from(context).inflate(R.layout.view_remark_invalid,null)
        mReasonTv = view.findViewById<AppCompatTextView>(R.id.view_remark_invalid_reason)
        var line = view.findViewById<View>(R.id.view_remark_invalid_divider)
        var arrow = view.findViewById<AppCompatImageView>(R.id.view_remark_invalid_arrow)
        mOthersEd = view.findViewById<AppCompatEditText>(R.id.view_remark_invalid_others)
        if (info.invalidReason != null && info.invalidOtherReasons(info.invalidReason)){
            mReasonTv.text = "其他原因"
            mOthersEd.visibility = View.VISIBLE
            mOthersEd.setText(info.invalidReason)
        }else{
            mReasonTv.text = info.invalidReason
            mOthersEd.visibility = View.GONE
        }
        //审核通过后不可选择和提交
        if (info.invalidRequestPassed()){
            arrow.visibility = View.INVISIBLE
        }else{
            arrow.visibility = View.VISIBLE
            var clickListener = View.OnClickListener {
                SimpleBottomSheetDialog.newBuilder(context)
                        .setData(context!!.resources.getStringArray(R.array.invalid_reason).toList())
                        .setItemClicklistener { v, s, position ->
                            mInvalidReason = s
                            mReasonTv.text = s
                            if (s == "其他原因") {
                                mOthersEd.visibility = View.VISIBLE
                                line.visibility = View.VISIBLE
                                android.os.Handler().postDelayed({fragment_userinfo_remark_scrollview.fullScroll(ScrollView.FOCUS_DOWN)},500)
                            }else{
                                mOthersEd.visibility = View.GONE
                                line.visibility = View.GONE
                            }
                        }
                        .show()
            }
            mReasonTv.setOnClickListener(clickListener)
            arrow.setOnClickListener(clickListener)
        }
        rlParent.addView(view)

        initPeoplePicker()
    }

    private lateinit var mReasonTv:AppCompatTextView
    private lateinit var mOthersEd:AppCompatEditText
    private var mInvalidReason:String? = null
    private var mInvalidReasonOthers:String? = null //其他原因
    private fun selectProvinceCity() {
        startActivityForResult(SelectExpectCityActivity.newInstance(context!!, mCityList!!), Code.RequestCode.PROVINCE_SELECT)
    }

    private fun showCurrentPickerView() {// 弹出选择器
        val pvOptions = OptionsPickerBuilder(context, OnOptionsSelectListener { options1, options2, options3, v ->
            //返回的分别是三个级别的选中位置
            val region = "${mLocationRegion!!.options1Items[options1]} ${mLocationRegion!!.options2Items[options1][options2]} ${mLocationRegion!!.options3Items[options1][options2][options3]}"
            (rlParent.getChildAt(6) as SimpleLineItemView).editText.setText(region)
            mRemarkVO.currentProvince = mLocationRegion!!.options1Items[options1]
            mRemarkVO.currentCity = mLocationRegion!!.options2Items[options1][options2]
            mRemarkVO.currentArea = mLocationRegion!!.options3Items[options1][options2][options3]

        }).setTitleText("选择当前城市")
                .setDividerColor(context!!.resources.getColor(R.color.green29AC3E))
                .setTextColorCenter(context!!.resources.getColor(R.color.green29AC3E)) //设置选中项文字颜色
                .setSubmitColor(context!!.resources.getColor(R.color.green29AC3E))
                .setCancelColor(context!!.resources.getColor(R.color.green29AC3E))
                .setTitleColor(context!!.resources.getColor(R.color.green29AC3E))
                .setContentTextSize(20)
                .build<Any>()

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(mLocationRegion!!.options1Items as List<Any>?, mLocationRegion!!.options2Items as List<MutableList<Any>>?, mLocationRegion!!.options3Items as List<MutableList<MutableList<Any>>>?)//三级选择器
        pvOptions.show()
    }

    private fun showBirthplacePickerView() {// 弹出选择器
        val pvOptions = OptionsPickerBuilder(context, OnOptionsSelectListener { options1, options2, options3, v ->
            //返回的分别是三个级别的选中位置
            val region = "${mLocationRegion!!.options1Items[options1]} ${mLocationRegion!!.options2Items[options1][options2]}"
            (rlParent.getChildAt(10) as SimpleLineItemView).editText.setText(region)
            mRemarkVO.birthplaceProvince = mLocationRegion!!.options1Items[options1]
            mRemarkVO.birthplaceCity = mLocationRegion!!.options2Items[options1][options2]
//            mRemarkVO.birthplaceArea = mLocationRegion!!.options3Items[options1][options2][options3]

        }).setTitleText("选择籍贯")
                .setDividerColor(context!!.resources.getColor(R.color.green29AC3E))
                .setTextColorCenter(context!!.resources.getColor(R.color.green29AC3E)) //设置选中项文字颜色
                .setSubmitColor(context!!.resources.getColor(R.color.green29AC3E))
                .setCancelColor(context!!.resources.getColor(R.color.green29AC3E))
                .setTitleColor(context!!.resources.getColor(R.color.green29AC3E))
                .setContentTextSize(20)
                .build<Any>()

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(mLocationRegion!!.options1Items as List<Any>?, mLocationRegion!!.options2Items as List<MutableList<Any>>?)//三级选择器
        pvOptions.show()
    }

    private fun commitRemark() {
        mRemarkVO.userId = (activity as RemarkActivity).getUserId()!!
        var i = 0
        //有一个不是SimpleLineItemView
        while (i < rlParent.childCount-1) {
            val mItemView = rlParent.getChildAt(i) as SimpleLineItemView
            when (mItemView.onlyMark.toString()) {
                "常用称呼" -> {
                    mRemarkVO.nickname = mItemView.editText.text.toString().trim()
                }
                "微信号" -> {
                    mRemarkVO.wechat = mItemView.editText.text.toString().trim()
                }
                "性别" -> {

                }
                "年龄" -> {
                    mRemarkVO.age = mItemView.editText.text.toString().trim()
                }
                "学历" -> {

                }
                "婚姻状况" -> {

                }
                "当前所在地" -> {

                }
                "求职方向" -> {

                }
                "期望工作地" -> {

                }
                "求职紧迫度" -> {

                }
                "籍贯老家" -> {

                }
                "出门多久" -> {
                    mRemarkVO.goOut = mItemView.editText.text.toString().trim()

                }
                "找工作多久" -> {
                    mRemarkVO.findJob = mItemView.editText.text.toString().trim()

                }
                "特殊标记" -> {

                }
                "抱团/单人" -> {

                }
                "技能" -> {

                }
                "当前月薪" -> {

                }
                "在离职状态" -> {

                }
                "痛点" -> {
                    mRemarkVO.painPoints = mItemView.editText.text.toString().trim()
                }
            }

            i++
        }
        //检测必填项 年龄、性别、求职意向、求职紧迫度
//        if (TextUtils.isEmpty(mRemarkVO.age)) {
//            SimpleToast.showNormal("年龄必填")
//            return
//        }
//        if (TextUtils.isEmpty(mRemarkVO.sex)) {
//            SimpleToast.showNormal("性别必填")
//            return
//        }
//        if (TextUtils.isEmpty(mRemarkVO.expectPosition)) {
//            SimpleToast.showNormal("求职方向必填")
//            return
//        }
//        if (TextUtils.isEmpty(mRemarkVO.expectOnboa rdingDate)) {
//            SimpleToast.showNormal("求职紧迫度")
//            return
//        }
        if (mReasonTv.text == "其他原因"){
            var others = mOthersEd.text.toString()
            if (StringUtils.isEmpty(others)){
                showToast("请输入其他原因")
                return
            }else{
                mRemarkVO.invalidReason = others
                mRemarkVO.invalidStatus = invalidStatus_request
            }
        }else if (mInvalidReason != null) {
            mRemarkVO.invalidReason = mInvalidReason
            mRemarkVO.invalidStatus = invalidStatus_request
        }
        mPresenter.remark(mRemarkVO)
        CommonUtils.statistics(context, Constants.STATISTICS_remarkAndNote_REMARK_SUBMIT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)

    }

    private var mMultiSelectorPopupWindow: AlertDialog? = null
    private val invalidStatus_request = 0
    private val invalidStatus_passed = 1
    private val invalidStatus_reject = 2

    /**
     * 显示单选框
     * @param title 弹框标题
     * @param defaultSelection 默认选中项
     * @param list 数据源
     * @param listener  确认选择后回调
     */
    private fun showMultiSelectPopWindow(title: String, defaultSelection: List<String>?, list: List<String>, listener: MultiSelectListener) {
        mMultiSelectorPopupWindow = AlertDialog.Builder(context!!).create()
        mMultiSelectorPopupWindow!!.show()
        //设置显示位置和动画
        val window = mMultiSelectorPopupWindow!!.window
        val attributes = window.attributes
        attributes.width = window.windowManager.defaultDisplay.width
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.setGravity(Gravity.BOTTOM)
        window.setWindowAnimations(R.style.popWindowAnim)

        mMultiSelectorPopupWindow!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(context!!).inflate(R.layout.pop_selector, null)
        mMultiSelectorPopupWindow!!.window!!.setContentView(view)
        mMultiSelectorPopupWindow!!.window!!.setBackgroundDrawable(ColorDrawable())

        val closeBtn = view.findViewById<TextView>(R.id.pop_single_selector_close_btn)
        val confirmBtn = view.findViewById<TextView>(R.id.pop_single_selector_confirm_btn)
        val titleTv = view.findViewById<TextView>(R.id.pop_single_selector_title_tv)
        val recyclerView = view.findViewById<RecyclerView>(R.id.pop_single_selector_list)

        titleTv.text = title
        closeBtn.setOnClickListener { mMultiSelectorPopupWindow!!.dismiss() }
        val adapter = MultiSelectorAdapter(context!!, list)
        adapter.setDefaultSelections(if (defaultSelection == null) ArrayList<String>() else defaultSelection)
        val layoutManager = LinearLayoutManager(context!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        adapter.notifyDataSetChanged()
        confirmBtn.setOnClickListener {
            val selection = adapter.getSelection()
            listener.onSelect(selection)
        }
        mMultiSelectorPopupWindow!!.setOnDismissListener {

        }
    }

    private var mPeopleCommonDialog: CommonDialog? = null
    @SuppressLint("InflateParams")
    private fun initPeoplePicker() {
        val mView = LayoutInflater.from(context).inflate(R.layout.view_customer_remark_people_dialog, null)
        val mEditText: EditText = mView.findViewById(R.id.et_note)
        if (mRemarkVO.peoples != null) {
            try {
                mEditText.setText(mRemarkVO.peoples.substring(2, mRemarkVO.peoples.length))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        val wheel = mView.findViewById<WheelPicker>(R.id.wheel)
        val mWheelData = ArrayList<String>()
        var selectPosition = 0
        mWheelData.add("1人")
        mWheelData.add("2人")
        mWheelData.add("3人")
        mWheelData.add("多人")
        wheel.data = mWheelData
        wheel.setOnItemSelectedListener { picker, data, position ->
            selectPosition = position
        }
        mPeopleCommonDialog = CommonDialog.newBuilder(context)
                .setTitle("抱团/单人")
                .setView(mView)
                .setNegativeButton(resources.getString(R.string.cancel)) {
                }
                .setPositiveButton(resources.getString(R.string.confirm)) { dialog ->

                    val mValue = mWheelData[selectPosition] + mEditText.text.toString().trim()
                    (rlParent.getChildAt(14) as SimpleLineItemView).editText.setText(mValue)
                    mRemarkVO.peoples = mValue
                    dialog.dismiss()

                }
                .setCancelable(true)
                .create()
    }


}