package cn.xzj.agent.ui.job.source

import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.ImageView
import cn.xzj.agent.R
import cn.xzj.agent.core.common.BaseFragment
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.util.DisplayUtil
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_job_image_source.*

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/7
 * @Des
 */
class ImageSourceFragment : BaseFragment(), JobSourceActivity.OnDownloadButtonListener {
    lateinit var mOnDownloadButtonListener: JobSourceActivity.OnDownloadButtonListener
    private var downloadBtnIsShowing = false
    private var sourceData: ArrayList<String>? = null
    private var selectImageHashMap = HashMap<Int, String>()
    private var mJobInfo: JobInfo? = null
    private var visibleLastIndex = 0 //用户可见的最后一个下标

    private var mAdapter = object : QuickAdapter<String>(R.layout.item_job_source) {
        override fun convert(holder: BaseHolder, item: String, position: Int) {
            holder.getView<View>(R.id.ic_job_source_play_btn).visibility = View.GONE
            if (position < visibleLastIndex) {
                holder.getView<View>(R.id.tv_job_source_user_is_look).visibility = View.VISIBLE
            } else {
                holder.getView<View>(R.id.tv_job_source_user_is_look).visibility = View.GONE
            }
            if (downloadBtnIsShowing) {
                holder.getView<View>(R.id.job_source_download_select).visibility = View.VISIBLE
            } else {
                holder.getView<View>(R.id.job_source_download_select).visibility = View.GONE
            }
            val mIvJobSourceDownloadSelect = holder.getView<ImageView>(R.id.job_source_download_select)
            mIvJobSourceDownloadSelect.setOnClickListener {
                mIvJobSourceDownloadSelect.isSelected = !mIvJobSourceDownloadSelect.isSelected
                if (mIvJobSourceDownloadSelect.isSelected) {
                    mIvJobSourceDownloadSelect.setImageResource(R.mipmap.ic_selected)
                } else {
                    mIvJobSourceDownloadSelect.setImageResource(R.mipmap.ic_select)
                }
                if (mIvJobSourceDownloadSelect.isSelected) {
                    selectImageHashMap[position] = item
                } else {
                    selectImageHashMap.remove(position)
                }
            }

            //显示图片
            val mIvJobSource = holder.getView<ImageView>(R.id.iv_job_source)
//            /设置图片圆角角度
            val mRoundedCorners = RoundedCornersTransformation(context, DisplayUtil.dip2px(4), 1)
            Glide.with(holder.itemView.context)
                    .load(item)
                    .transform(mRoundedCorners)
                    .placeholder(R.mipmap.ic_default_logo)
                    .into(mIvJobSource)
        }
    }

    override fun initLayout(): Int {
        return R.layout.fragment_job_image_source
    }

    override fun initParams() {
        mJobInfo = arguments!!.getSerializable(JobSourceActivity.SOURCE_DATA) as JobInfo?
        if (mJobInfo != null) {
            try {
                sourceData = ArrayList()
                //可见图片+不可见图片
                sourceData!!.addAll(mJobInfo!!.images)
                visibleLastIndex = mJobInfo!!.images.size
                if (mJobInfo!!.companyInnerImages != null)
                    sourceData!!.addAll(mJobInfo!!.companyInnerImages!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun initViews() {
        recyclerViewJobSource.layoutManager = GridLayoutManager(context, 2)
        recyclerViewJobSource.adapter = mAdapter
        if (sourceData != null) {
            mAdapter.setNewData(sourceData!!)
        }
        mAdapter.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<String> {
            override fun onItemClick(view: View, itemData: String, i: Int) {
                JobSourceDetailActivity.jump(context!!, itemData, JobSourceActivity.SHOW_SOURCE_IMAGE)
            }
        })
        mOnDownloadButtonListener = this
        if (mAdapter.data!!.isEmpty()) {
            statusLayoutJobImageSource.showEmpty()
        } else {
            statusLayoutJobImageSource.showContent()
        }
    }

    override fun initData() {
    }

    override fun onDownloadClick(downloadBtnIsShowing: Boolean) {
        this.downloadBtnIsShowing = downloadBtnIsShowing
        mAdapter.notifyDataSetChanged()
    }

    fun getSelectImageUrls(): ArrayList<String> {
        val mImageUrls = ArrayList<String>()
        for (item in selectImageHashMap.values) {
            mImageUrls.add(item)
        }
        return mImageUrls
    }
}