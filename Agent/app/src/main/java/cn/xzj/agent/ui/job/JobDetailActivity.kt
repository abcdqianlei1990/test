package cn.xzj.agent.ui.job

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.DefaultEventMessage
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.entity.job.RecruitmentNeedsInfo
import cn.xzj.agent.iview.IJobDetailView
import cn.xzj.agent.presenter.JobDetailPresenter
import cn.xzj.agent.ui.adapter.PositionRequirementAdapter
import cn.xzj.agent.ui.adapter.common.CommonListAdapter
import cn.xzj.agent.ui.adapter.common.CommonViewHolder
import cn.xzj.agent.ui.job.source.JobSourceActivity
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.ImageLoader
import cn.xzj.agent.util.SpanTagHandler
import cn.xzj.agent.util.StatusBarUtil
import cn.xzj.agent.widget.SimpleToast
import com.channey.utils.StringUtils
import com.youth.banner.BannerConfig
import com.youth.banner.listener.OnBannerListener
import kotlinx.android.synthetic.main.activity_job_detail.*


/**
 * Created by channey on 2018/3/22.
 * 岗位详情
 */
class JobDetailActivity : MVPBaseActivity<JobDetailPresenter>(), View.OnClickListener, IJobDetailView {
    override fun getLayoutId(): Int {
        return R.layout.activity_job_detail
    }

    override fun context(): Context {
        return this
    }

    private var mJobId: String? = null
    private var mUserId: String? = null
    private lateinit var mJobDetail: JobInfo
    private val mBannerImgs = ArrayList<String>()
    private lateinit var mJobFlagAdapter: CommonListAdapter<String>
    private var userName: String? = null
    private var tollDescription: String? = null//收费描述,传入岗位预约


    companion object {
        /**
         * @param context
         * @param jobId wechatId
         */
        fun jumpForResult(activity: Activity, jobId: String, userId: String?, requestCode: Int, userName: String? = null) {
            var intent = Intent(activity, JobDetailActivity::class.java)
            intent.putExtra(Keys.POSITION_ID, jobId)
            intent.putExtra(Keys.USER_ID, userId)
            intent.putExtra(Keys.DATA_KEY_1, userName)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun initParams() {
        mJobId = intent.getStringExtra(Keys.POSITION_ID)
        mUserId = intent.getStringExtra(Keys.USER_ID)
        userName = intent.getStringExtra(Keys.DATA_KEY_1)
    }

    override fun initViews() {
        hideToolbar()
        initBanner()
        initFlagRecyclerView()
        StatusBarUtil.setPadding(this, activity_job_detail_backArrow_tv)
        if (mUserId == null) {
            //企业岗位过来的没有预约按钮
            activity_job_detail_submit_btn.visibility = View.GONE
        }
//        activity_job_detail_image_count_btn.visibility=View.GONE
//        activity_job_detail_video_count_btn.visibility=View.GONE
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_positionDetail_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    override fun initData() {
        mPresenter.getPositionDetails(mJobId!!, mUserId)
    }

    override fun setListeners() {
        activity_job_detail_backArrow_tv.setOnClickListener(this)
        activity_job_detail_submit_btn.setOnClickListener(this)
        activity_job_detail_image_count_btn.setOnClickListener(this)
        activity_job_detail_video_count_btn.setOnClickListener(this)
    }

    private fun initBanner() {
        //设置图片加载器
        activity_job_detail_banner.setImageLoader(ImageLoader())
        //设置图片集合
        activity_job_detail_banner.setImages(mBannerImgs)
        //设置指示器位置（当banner模式中有指示器时）
        activity_job_detail_banner.setIndicatorGravity(BannerConfig.CENTER)
        activity_job_detail_banner.setOnBannerListener(object : OnBannerListener {
            override fun OnBannerClick(position: Int) {

            }
        })
    }

    private fun invalidateBanner() {
        activity_job_detail_banner.setImages(mBannerImgs)
        activity_job_detail_banner.start()
    }

    private fun initFlagRecyclerView() {
        mJobFlagAdapter = object : CommonListAdapter<String>(this, R.layout.item_job_flag) {
            override fun convert(viewHolder: CommonViewHolder?, item: String?, position: Int) {
                viewHolder!!.setText(R.id.item_job_flag_title, item!!)
            }
        }
        activity_job_detail_flag_list.adapter = mJobFlagAdapter
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.activity_job_detail_backArrow_tv -> finish()
            R.id.activity_job_detail_submit_btn -> {
                if (mJobDetail.applied) {
                    SimpleToast.showShort("该用户已申请该职位")
                } else {
                    CommonUtils.statistics(this, Constants.STATISTICS_positionDetail_RESERVE_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                    JobReservationActivity.jump(this, mJobDetail.positionId, recruitmentNeedsId = null
                            , applyId = null, positionName = mJobDetail.positionName, userId = mUserId, userName = userName, tollDescription = tollDescription, isNeedPreReserve = mJobDetail.preReserve)
                }
            }
            R.id.activity_job_detail_image_count_btn -> {
                JobSourceActivity.jump(this, JobSourceActivity.SHOW_SOURCE_IMAGE, mJobDetail)
            }
            R.id.activity_job_detail_video_count_btn -> {
                JobSourceActivity.jump(this, JobSourceActivity.SHOW_SOURCE_VIDEO, mJobDetail)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun injectViews(info: JobInfo) {
        if (info.images.isNotEmpty()) {
            mBannerImgs.clear()
            mBannerImgs.addAll(info.images)
        } else {
            mBannerImgs.add("default url 1")
            mBannerImgs.add("default url 2")
        }
        invalidateBanner()
        activity_job_detail_title_tv.text = info.positionName
        //企业简称
        activity_job_detail_companyShortName_tv.text = info.companyShortName
        activity_job_detail_applyCount_tv.text = info.applyCount.toString()
        activity_job_detail_yuyueCount_tv.text = "男:${info.reservedMaleQuantity} 女:${info.reservedFemaleQuantity}"
        activity_job_detail_entryCount_tv.text = info.entryCount.toString()
        //显示标签
        if (info.positionTypes.isNotEmpty()) {
            mJobFlagAdapter.clear()
            mJobFlagAdapter.addAllItem(info.positionTypes)
            mJobFlagAdapter.notifyDataSetChanged()
        }
        //企业名称
        activity_job_detail_companyName2_tv.text = "${info.companyName} ${info.staffSize}人"
        //工作地区
        activity_job_detail_workLoc_tv.text = "${info.workingProvince} ${info.workingCity} ${info.workingArea}"
        //工作地址
        activity_job_detail_workAddress_tv.text = info.workingAddress
        //岗位特征
        var strFeature = ""
        if (info.positionFeatures != null) {
            for (featureItem in info.positionFeatures!!) {
                strFeature += if (TextUtils.isEmpty(strFeature))
                    featureItem
                else
                    ",$featureItem"
            }
        }
        activity_job_detail_features_tv.text = strFeature
        //招聘需求
        initRequirementList(info.recruitmentNeeds)
        //面试要求
        activity_job_detail_requirement_tv.text = convertHtml(info.interviewRequirements, activity_job_detail_requirement_tv)
        //综合工资
        var sb = StringBuilder();
        sb.append(info.minSalary)
        sb.append("-")
        sb.append(info.maxSalary)
        sb.append("元")
        activity_job_detail_salary_tv.text = sb.toString()
        //工资说明
        if (StringUtils.isEmpty(mJobDetail.salaryDescription)) {
            activity_job_detail_salaryDesctitle_tv.visibility = View.GONE
            activity_job_detail_salaryDesc_tv.visibility = View.GONE
        } else {
            activity_job_detail_salaryDesctitle_tv.visibility = View.VISIBLE
            activity_job_detail_salaryDesc_tv.visibility = View.VISIBLE
            activity_job_detail_salaryDesc_tv.text = convertHtml(mJobDetail.salaryDescription, activity_job_detail_salaryDesc_tv)//富文本显示
        }
        //衣食住行
        if (StringUtils.isEmpty(info.living)) {
            activity_job_detail_shisu_title.visibility = View.GONE
            activity_job_detail_shisu_tv.visibility = View.GONE
        } else {
            activity_job_detail_shisu_title.visibility = View.VISIBLE
            activity_job_detail_shisu_tv.visibility = View.VISIBLE
            activity_job_detail_shisu_tv.text = convertHtml(info.living, activity_job_detail_shisu_tv)//富文本显示
        }
        //隐私描述
        if (StringUtils.isEmpty(info.privateInfo)) {
            activity_job_detail_private_title.visibility = View.GONE
            activity_job_detail_private.visibility = View.GONE
        } else {
            activity_job_detail_private_title.visibility = View.VISIBLE
            activity_job_detail_private.visibility = View.VISIBLE
            activity_job_detail_private.text = convertHtml(info.privateInfo, activity_job_detail_private)//富文本显示
        }
        //任职要求
        activity_job_detail_conditions_tv.text = convertHtml(info.requirement, activity_job_detail_conditions_tv)//富文本显示
        //工作福利
        var sb1 = StringBuilder();
        for (str in info.welfare) {
            sb1.append("$str ")
        }
        activity_job_detail_welfare_tv.text = sb1
        //工作内容
        activity_job_detail_content_tv.text = convertHtml(info.content, activity_job_detail_content_tv)//富文本
        //工作时间
        activity_job_detail_time_tv.text = convertHtml(info.workTime, activity_job_detail_time_tv)//富文本
        //注意事项
        if (StringUtils.isEmpty(info.notice)) {
            activity_job_detail_noticeTitle_tv.visibility = View.GONE
            activity_job_detail_notice_tv.visibility = View.GONE
        } else {
            activity_job_detail_noticeTitle_tv.visibility = View.VISIBLE
            activity_job_detail_notice_tv.visibility = View.VISIBLE
            activity_job_detail_notice_tv.text = convertHtml(info.notice, activity_job_detail_notice_tv)//富文本显示
        }
        //简介
        activity_job_detail_intro_tv.text = info.companyProfile
        //是否停招
        if (info.status == EnumValue.JOB_STATUS_STOP) {
            tv_status.text = "已停招"
            tv_status.visibility = View.VISIBLE
        } else {
            tv_status.text = ""
            tv_status.visibility = View.GONE
        }
        //图片数量
        var count = info.images.size
        if (info.companyInnerImages != null) {
            //内部图片
            count += info.companyInnerImages!!.size
        }
        activity_job_detail_image_count_tv.text = "$count"
        //视频数量
        if (info.companyInnerVideos != null) {
            activity_job_detail_video_count_tv.text = "${info.companyInnerVideos!!.size}"
        }

        //是否预报名
        if (info.preReserve) {
            tv_activity_job_detail_is_need_preReserve.visibility = View.VISIBLE
        } else {
            tv_activity_job_detail_is_need_preReserve.visibility = View.GONE
        }

    }

    private fun initRequirementList(list: List<RecruitmentNeedsInfo>) {
        val adapter = PositionRequirementAdapter(this, list)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        activity_job_detail_requirement_list.adapter = adapter
        activity_job_detail_requirement_list.layoutManager = layoutManager
        adapter.notifyDataSetChanged()
        for (info in list) {
            if (info.userMaleToll > 0 || info.userFemaleToll > 0)
                tollDescription = "温馨提示：该岗位需支付费用，男${info.userMaleToll}元，女${info.userFemaleToll}元，请提醒用户在APP上付费。"
        }
    }

    override fun onDetailGetSuccess(info: JobInfo?) {
        mJobDetail = info!!
        mJobId = info.positionId
        injectViews(info)
    }

    override fun onDetailGetFailure(msg: String?) {
        SimpleToast.showNormal(msg!!)
    }

    override fun onDefaultEvent(event: DefaultEventMessage) {
        finish()
    }


    private fun convertHtml(str: String, textView: TextView): Spanned {
        if (str.contains("<span") || str.contains("</span>") || str.contains("style=") || str.contains("font-size")) {
            return Html.fromHtml(
                    str.replace("span", "spann"),
                    null,
                    SpanTagHandler(this, textView.textColors)
            )
        } else {
            return Html.fromHtml(str)
        }
    }
}