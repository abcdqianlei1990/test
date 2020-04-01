package cn.xzj.agent.core.common

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.core.statusviewmanager.StatusLayoutManager
import cn.xzj.agent.entity.DefaultEventMessage
import cn.xzj.agent.i.DateSelectedListener
import cn.xzj.agent.util.SharedPreferencesUtil
import cn.xzj.agent.util.StatusBarUtil
import com.aigestudio.wheelpicker.WheelPicker
import com.channey.utils.StringUtils
import com.umeng.message.PushAgent
import kotlinx.android.synthetic.main.activity_quick.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("Registered")
abstract class QuickActivity : BaseActivity() {
    lateinit var statusLayoutManager: StatusLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initParams()
        initViews()
        setListeners()
        initData()
        PushAgent.getInstance(this).onAppStart()
        EventBus.getDefault().register(this)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(R.layout.activity_quick)
//        StatusBarUtil.darkMode(this)
//        StatusBarUtil.setPadding(this, ll_quick_top_parent)
        val mView = LayoutInflater.from(this).inflate(layoutResID, null)
        val contentView = findViewById<FrameLayout>(R.id.ll_content)
        statusLayoutManager = StatusLayoutManager.newBuilder(this)
                .contentView(mView)
                .loadingView(R.layout.view_loading)
                .emptyDataView(R.layout.view_empty)
                .netWorkErrorView(R.layout.view_network_error)
                .errorView(R.layout.view_error)
                .errorRetryViewId(R.id.tv_try)
                .onRetryListener {
                    onRetry()
                }
                .build()
        statusLayoutManager.showContent()
        contentView.addView(statusLayoutManager.rootLayout)
        setLifeBack()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    fun setLifeBack() {
        iv_back.setOnClickListener {
            finish()
        }
    }

    fun setOnBackClickListener(listener: View.OnClickListener){
        iv_back.setOnClickListener(listener)
    }

    fun setTitle(title: String) {
        tv_title.text = title
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

    open fun hideLifeImage() {
        if (iv_back.visibility != View.GONE) {
            iv_back.visibility = View.GONE
        }
    }

    abstract fun getLayoutId(): Int
    open fun initParams() {

    }

    abstract fun initViews()
    open fun setListeners() {}
    open fun initData() {

    }

    /**
     * 加载错误点击重试按钮触发的方法
     */
    open fun onRetry() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onDefaultEvent(event: DefaultEventMessage) {
    }

    /**
     * 设置添加屏幕的背景透明度

     * @param bgAlpha
     * *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    fun setBackgroundAlpha(bgAlpha: Float) {
        val lp = window.attributes
        lp.alpha = bgAlpha
        window.attributes = lp
    }

    fun hasLogin(): Boolean {
        val token = SharedPreferencesUtil.getToken(this)
        return !StringUtils.isEmpty(token)
    }

    //============================= 时间选择弹框 =============================
    private var mDatePickerPopupWindow: AlertDialog? = null
    fun showDatePickerPopWindow(showHour:Boolean=false,showMin:Boolean=false,listener: DateSelectedListener?) {
        var selectYear = ""
        var selectMonth = ""
        var selectDay = ""
        var selectHour = ""
        var selectMin = ""
        mDatePickerPopupWindow = AlertDialog.Builder(this).create()
        mDatePickerPopupWindow!!.show()
        //设置显示位置和动画
        val window = mDatePickerPopupWindow!!.window
        val attributes = window.attributes
        attributes.width = window.getWindowManager().getDefaultDisplay().getWidth()
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.setGravity(Gravity.BOTTOM)
        window.setWindowAnimations(R.style.popWindowAnim)

        mDatePickerPopupWindow!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(this).inflate(R.layout.view_date_select, null)
        mDatePickerPopupWindow!!.getWindow()!!.setContentView(view)
        mDatePickerPopupWindow!!.getWindow()!!.setBackgroundDrawable(ColorDrawable())

        val negBtn = view.findViewById<TextView>(R.id.date_select_neg_btn)
        val posBtn = view.findViewById<TextView>(R.id.date_select_pos_btn)
        val yearWheel = view.findViewById<WheelPicker>(R.id.WheelView1)
        val monthWheel = view.findViewById<WheelPicker>(R.id.WheelView2)
        val dayWheel = view.findViewById<WheelPicker>(R.id.WheelView3)
        val hourWheel = view.findViewById<WheelPicker>(R.id.WheelView4)
        val minWheel = view.findViewById<WheelPicker>(R.id.WheelView5)

        //设置年份数据，从今年开始,从今年开始往后推10年
//            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        var c = Calendar.getInstance()
        c.timeZone = java.util.TimeZone.getTimeZone("GMT+8")
        var currentYear = c.get(Calendar.YEAR); // 获取当前年份
        var currentMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        var currentDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        var currentHour = c.get(Calendar.HOUR_OF_DAY);//时
        var currentMin = c.get(Calendar.MINUTE);//分
        selectYear = "${currentYear}年"
        selectMonth = if (currentMonth > 9) "${currentMonth}月" else "0${currentMonth}月"
        selectDay = if (currentDay > 9) "${currentDay}日" else "0${currentDay}日"
        selectHour= if (currentHour > 9) "${currentHour}时" else "0${currentHour}时"
        selectMin = if (currentMin > 9) "${currentMin}分" else "0${currentMin}分"
        var yeas:ArrayList<String> = ArrayList<String>()
        for (i in currentYear..currentYear+3){
            yeas.add("${i}年")
        }
        yearWheel.data = yeas
        //默认第一项选中
        yearWheel.selectedItemPosition = 0
        selectYear = yeas[0]
        yearWheel.setOnItemSelectedListener(object : WheelPicker.OnItemSelectedListener {
            override fun onItemSelected(picker: WheelPicker?, data: Any?, position: Int) {
//                    Log("当前年份选择为 $data")
                selectYear = yeas[position]
            }
        })
        //设置月份
        var months:ArrayList<String> = ArrayList<String>()
        var selectedMonthPos = 0
        for (i in 1..31){
            var temp = if(i > 9) "${i}月" else "0${i}月"
            months.add("$temp")
            if (i == currentMonth) selectedMonthPos = i-1
        }
        monthWheel.data = months
        monthWheel.selectedItemPosition = selectedMonthPos
        selectMonth = months[selectedMonthPos]
        monthWheel.setOnItemSelectedListener(object : WheelPicker.OnItemSelectedListener {
            override fun onItemSelected(picker: WheelPicker?, data: Any?, position: Int) {
//                    Log("当前月份选择为 $data")
                selectMonth = months[position]
            }
        })
        //设置天
        var days:ArrayList<String> = ArrayList<String>()
        var selectedDayPos = 0
        for (i in 1..31){
            var temp = if(i > 9) "${i}日" else "0${i}日"
            days.add("$temp")
            if (i == currentDay) selectedDayPos = i-1
        }
        dayWheel.data = days
        dayWheel.selectedItemPosition = selectedDayPos
        selectDay = days[selectedDayPos]
        dayWheel.setOnItemSelectedListener(object : WheelPicker.OnItemSelectedListener {
            override fun onItemSelected(picker: WheelPicker?, data: Any?, position: Int) {
//                    Log("当前日期选择为 $data")
                selectDay = days[position]
            }
        })
        //设置小时
        if (showHour){
            hourWheel.visibility = View.VISIBLE
            var hours:ArrayList<String> = ArrayList<String>()
            var selectedHourPos = 2
            for (i in 0..23){
                var temp = if(i > 9) "${i}时" else "0${i}时"
                hours.add("$temp")
                if (i == currentHour) selectedHourPos = i
            }
            hourWheel.data = hours
            hourWheel.selectedItemPosition = selectedHourPos
            selectHour = hours[selectedHourPos]
            hourWheel.setOnItemSelectedListener(object : WheelPicker.OnItemSelectedListener {
                override fun onItemSelected(picker: WheelPicker?, data: Any?, position: Int) {
//                    Log("当前小时选择为 $data")
                    selectHour = hours[position]
                }
            })
        }else{
            hourWheel.visibility = View.GONE
        }
        //设置分
        if (showMin){
            minWheel.visibility = View.VISIBLE
            var mins:ArrayList<String> = ArrayList<String>()
            var selectedMinPos = 2
            for (i in 0..59){
                var temp = if(i > 9) "${i}分" else "0${i}分"
                mins.add("$temp")
                if (i == currentMin) selectedMinPos = i
            }
            minWheel.data = mins
            minWheel.selectedItemPosition = selectedMinPos
            selectMin = mins[selectedMinPos]
            minWheel.setOnItemSelectedListener(object : WheelPicker.OnItemSelectedListener {
                override fun onItemSelected(picker: WheelPicker?, data: Any?, position: Int) {
//                    Log("当前分选择为 $data")
                    selectMin = mins[position]
                }
            })
        }else{
            minWheel.visibility = View.GONE
        }


        negBtn.setOnClickListener { mDatePickerPopupWindow!!.dismiss() }
        posBtn.setOnClickListener {
            mDatePickerPopupWindow!!.dismiss()
//                if (listener != null) listener.onSelected("$selectYear$selectMonth$selectDay$selectHour$selectMin")
            if (listener != null) listener.onSelected(selectYear,selectMonth,selectDay,selectHour,selectMin)
        }

        mDatePickerPopupWindow!!.setOnDismissListener {

        }
//        if (mDatePickerPopupWindow == null) {
//
//        }else{
//            val window = mDatePickerPopupWindow!!.window
//            window.setGravity(Gravity.BOTTOM)
//            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            window.setWindowAnimations(R.style.popWindowAnim)
//            mDatePickerPopupWindow!!.show()
//        }
    }

    fun dismissDatePickerDialog(){
        if (mDatePickerPopupWindow != null) mDatePickerPopupWindow!!.dismiss()
    }

}
