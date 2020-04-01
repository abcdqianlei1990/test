package cn.xzj.agent.core.common

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import cn.xzj.agent.R
import cn.xzj.agent.core.statusviewmanager.StatusLayoutManager
import cn.xzj.agent.util.SharedPreferencesUtil
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.activity_quick.*
import kotlinx.android.synthetic.main.activity_quick.title_line
import kotlinx.android.synthetic.main.view_title.*

/**
 * Created by channey on 2018/3/21.
 */
abstract class BaseFragment : Fragment() {
    //Fragment的View加载完毕的标记
    private var isViewCreated: Boolean = false
    //Fragment对用户可见的标记
    private var isUIVisible: Boolean = false
    //是否为第一次加载
    private var isFirst = true
    open var mStatusLayoutManager: StatusLayoutManager? = null
    private var mContentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutId = initLayout()
        if (layoutId <= 0) throw Exception("invalid layout id")
        if (mContentView == null)
            mContentView = LayoutInflater.from(activity).inflate(layoutId, null)
        if (mStatusLayoutManager == null) {
            mStatusLayoutManager = StatusLayoutManager.newBuilder(context)
                    .contentView(mContentView)
                    .loadingView(R.layout.view_loading)
                    .emptyDataView(R.layout.view_empty)
                    .netWorkErrorView(R.layout.view_network_error)
                    .errorView(R.layout.view_loading)
                    .retryViewId(R.id.tv_try)
                    .onRetryListener {
                        mStatusLayoutManager!!.showLoading()
                        onReTry()
                    }
                    .build()
            mStatusLayoutManager!!.showContent()
        }
        return mStatusLayoutManager!!.rootLayout
    }

    open fun onReTry() {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewCreated = true
        if (isFirst) {
            initParams()
            initViews()
            setListeners()
            initData()
            isFirst = false
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isUIVisible = isVisibleToUser
        lazyLoad()
    }

    /**
     * 懒加载
     */
    private fun lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            try {
                loadData()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false
            isUIVisible = false
        }
    }


    abstract fun initLayout(): Int
    abstract fun initParams()
    abstract fun initViews()
    open fun loadData() {}
    /**
     * 加载数据
     * 每次Fragment处于显示状态都会调用
     */
    abstract fun initData()

    open fun setListeners() {}

    fun showToast(msg: String) {
        Toast.makeText(this.activity, msg, Toast.LENGTH_LONG).show()
    }

    fun log(msg: String) {
        android.util.Log.d("qian", msg)
    }

    fun hasLogin(): Boolean {
        val token = SharedPreferencesUtil.getToken(context)
        return !StringUtils.isEmpty(token)
    }

    fun fillEdit(ed: EditText, str: String?) {
        ed.setText(str, TextView.BufferType.EDITABLE)
        if (str != null) ed.setSelection(str.length)
    }

    fun getStatusLayoutManager(): StatusLayoutManager {
        return mStatusLayoutManager!!
    }

    /**
     * 设置添加屏幕的背景透明度

     * @param bgAlpha
     * *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    fun setBackgroundAlpha(bgAlpha: Float) {
        val lp = activity!!.window.attributes
        lp.alpha = bgAlpha
        activity!!.window.attributes = lp
    }

    fun setTitle(title: String) {
        title_label.text = title
    }
    fun hideTitleLeftBtn(){
        title_back.visibility = View.INVISIBLE
    }
    open fun setRightBtn(text:CharSequence,listener: View.OnClickListener){
        tv_right_text.text = text
        tv_right_text.setOnClickListener(listener)
    }

    open fun setRightBtn(res:Int,listener: View.OnClickListener){
        tv_right_text.setBackgroundResource(res)
        tv_right_text.setOnClickListener(listener)
    }

    open fun hideToolbar() {
        if (ll_quick_top_parent.visibility != View.GONE) {
            ll_quick_top_parent.visibility = View.GONE
        }
    }
    open fun showToolbar(){
        if (ll_quick_top_parent.visibility != View.VISIBLE) {
            ll_quick_top_parent.visibility = View.VISIBLE
        }
    }


    fun setTitleBgWhite() {
        title_container.setBackgroundColor(resources.getColor(R.color.white))
        iv_back.setImageResource(R.mipmap.nav_back)
        tv_title.setTextColor(resources.getColor(R.color.black))
        tv_right_text.setTextColor(resources.getColor(R.color.black333333))
        title_line.setBackgroundColor(resources.getColor(R.color.commonLineColor))
    }

    fun setTitleBgGreen() {
        title_container.setBackgroundColor(resources.getColor(R.color.green29AC3E))
        iv_back.setImageResource(R.drawable.ic_nav_back_white)
        tv_title.setTextColor(resources.getColor(R.color.white))
        tv_right_text.setTextColor(resources.getColor(R.color.white))
        title_line.setBackgroundColor(resources.getColor(R.color.green29AC3E))
    }
}