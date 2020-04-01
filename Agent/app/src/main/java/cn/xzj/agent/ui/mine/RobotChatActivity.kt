package cn.xzj.agent.ui.mine

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.RobotChatInfo
import cn.xzj.agent.iview.IChatView
import cn.xzj.agent.presenter.ChatPresenter
import cn.xzj.agent.ui.adapter.RobotChatAdapter
import cn.xzj.agent.util.*
import cn.xzj.agent.widget.AudioRecorderButton2
import cn.xzj.agent.widget.SimpleToast
import com.alibaba.fastjson.JSON
import com.iflytek.cloud.*
import kotlinx.android.synthetic.main.activity_robot_chat.*
import org.json.JSONObject
import java.util.*

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/18
 * @Des 机器人聊天
 */
class RobotChatActivity : MVPBaseActivity<ChatPresenter>(), IChatView, AudioRecorderButton2.AudioFinishRecorderListener, AudioRecorderButton2.OnTouchEventStatusListener {
    private lateinit var robotChatData: ArrayList<RobotChatInfo>
    private lateinit var mAdapter: RobotChatAdapter
    private var mIat: SpeechRecognizer? = null
    private var LOG: String = RobotChatActivity::class.java.simpleName
    private var engineType = SpeechConstant.TYPE_CLOUD
    private lateinit var userId: String
    private val OBJECT_TYPE_SELF = 0 //自己
    private val OBJECT_TYPE_ROBOT = 1//机器人
    private val CONTENT_TYPE_TEXT = "text"//文字
    private val CONTENT_TYPE_VOICE = "voice_to_text"//语音转文字
    private var sendMessageType = 0 //0发送文字1发送语音
    private var ret = 0 // 函数调用返回值
    private var voiceText = StringBuffer()
    // 用HashMap存储听写结果
    private var mIatResults = HashMap<String, String>()
    private var isNeedSend = true

    override fun context(): Context {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_robot_chat
    }

    override fun initParams() {
        super.initParams()
        robotChatData = ArrayList()
        userId = SharedPreferencesUtil.getCurrentAgentInfo(this).agentId
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_problemReply_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    override fun initData() {
        super.initData()
        //显示缓存数据
        val cacheChatData = SharedPreferencesUtil.getRobotChatDataCache(this, SharedPreferencesUtil.getCurrentAgentInfo(this).agentId)
        if (cacheChatData != null) {
            robotChatData.clear()
            robotChatData.addAll(cacheChatData)
            mAdapter.notifyDataSetChanged()
            recyclerViewRobotChat.scrollToPosition(robotChatData.size - 1)
        }
    }

    override fun initViews() {
        setLifeBack()
        setTitle("问题解答")
        mAdapter = RobotChatAdapter(this, robotChatData)
        recyclerViewRobotChat.layoutManager = LinearLayoutManager(this)
        recyclerViewRobotChat.adapter = mAdapter
        recyclerViewRobotChat.setOnTouchListener { view, motionEvent ->
            CommonUtils.closeKeyBoard(this)//触摸关闭软键盘
            return@setOnTouchListener false
        }
        btnRobotChatSend.setOnClickListener {
            val content = editRobotChatSendContent.text.trim().toString()
            if (content.isEmpty()) {
                SimpleToast.showNormal(editRobotChatSendContent.hint.toString())
                return@setOnClickListener
            }
            addChatInfo(content, OBJECT_TYPE_SELF, CONTENT_TYPE_TEXT)
            reset()
        }
        ivVoice.setOnClickListener {
            setSendMessageType()
            reset()
        }
        btnRobotChatSendContent.setAudioFinishRecorderListener(this)
        btnRobotChatSendContent.setmOnTouchEventStatusListener(this)
        mIat = SpeechRecognizer.createRecognizer(this) { code -> LogLevel.w(LOG, "onInit code=$code") }

    }

    override fun onDestroy() {
        super.onDestroy()
        //保存聊天数据
        SharedPreferencesUtil.setRobotChatDataCache(this, robotChatData, SharedPreferencesUtil.getCurrentAgentInfo(this).agentId)
    }

    private fun reset() {
        voiceText = StringBuffer()
        editRobotChatSendContent.setText("")
        mIatResults.clear()
        CommonUtils.closeKeyBoard(this)

    }

    private fun setSendMessageType() {
        if (sendMessageType == 0) {
            sendMessageType = 1
            btnRobotChatSendContent.visibility = View.VISIBLE
            btnRobotChatSend.visibility = View.GONE
            editRobotChatSendContent.visibility = View.GONE
            ivVoice.setImageResource(R.mipmap.ic_chat_change_keyboard)

        } else {
            sendMessageType = 0
            btnRobotChatSendContent.visibility = View.GONE
            btnRobotChatSend.visibility = View.VISIBLE
            editRobotChatSendContent.visibility = View.VISIBLE
            ivVoice.setImageResource(R.mipmap.ic_chat_change_voice)
        }
        CommonUtils.statistics(this, Constants.STATISTICS_problemReply_SHEND_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
    }

    override fun onGetProblemResultSuccess(robotChatInfo: RobotChatInfo) {
        //组装消息对象
        robotChatInfo.objectType = OBJECT_TYPE_ROBOT
        robotChatInfo.contentType = CONTENT_TYPE_TEXT
        robotChatInfo.createTime = System.currentTimeMillis()
        robotChatInfo.content = robotChatInfo.messages[0].trim()
        robotChatData.add(robotChatInfo)
        mAdapter.notifyDataSetChanged()
        recyclerViewRobotChat.scrollToPosition(robotChatData.size - 1)
    }

    override fun onGetProblemResultFail() {
    }

    override fun onFinish(seconds: Float, filePath: String?) {
        LogLevel.w(LOG, filePath)
        executeStream(filePath!!)
//        executeStream("/storage/emulated/0/xzjAgent/recorder_audios/iat.wav")
    }

    override fun onAudioRecordDown() {
        if (mIat == null) {//创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化
            SimpleToast.showNormal("初始化lib失败，语音功能暂无法使用")
            return
        }
        iatSetParameter()
        ret = mIat!!.startListening(mRecognizerListener)
        if (ret != ErrorCode.SUCCESS) {
            SimpleToast.showNormal("识别失败,错误码：$ret")
        }
    }

    override fun onAudioRecordCancel() {
        isNeedSend = false
        mIat!!.cancel()
    }

    override fun onAudioRecordSuccess() {
        isNeedSend = true
        if (mIat != null)
            mIat!!.stopListening()
    }

    private fun iatSetParameter() {
        // 清空参数
        mIat!!.setParameter(SpeechConstant.PARAMS, null)

        // 设置听写引擎
        mIat!!.setParameter(SpeechConstant.ENGINE_TYPE, engineType)
        // 设置返回结果格式
        mIat!!.setParameter(SpeechConstant.RESULT_TYPE, "json")

        // 设置语言
        mIat!!.setParameter(SpeechConstant.LANGUAGE, "zh_cn")
        // 设置语言区域
//        mIat.setParameter(SpeechConstant.ACCENT, lag);

        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat!!.setParameter(SpeechConstant.VAD_BOS, "4000000")

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat!!.setParameter(SpeechConstant.VAD_EOS, "1000000")

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat!!.setParameter(SpeechConstant.ASR_PTT, "1")

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
//        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");

    }

    //执行音频流识别操作
    private fun executeStream(voiceFilePath: String) {
        // 设置参数
        iatSetParameter()
        // 设置音频来源为外部文件
        mIat!!.setParameter(SpeechConstant.AUDIO_SOURCE, "-1")
        // 也可以像以下这样直接设置音频文件路径识别（要求设置文件在sdcard上的全路径）：
//        mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-2")
//        mIat.setParameter(SpeechConstant.ASR_SOURCE_PATH, voiceFilePath)
//        mIat.setParameter(SpeechConstant.SAMPLE_RATE, "8000")
        ret = mIat!!.startListening(mRecognizerListener)
        if (ret != ErrorCode.SUCCESS) {
            SimpleToast.showNormal("识别失败,错误码：$ret")
        } else {
            val audioData = FucUtil.readAudioFile1(voiceFilePath)
            if (null != audioData) {
                // 一次（也可以分多次）写入音频文件数据，数据格式必须是采样率为8KHz或16KHz（本地识别只支持16K采样率，云端都支持），
                // 位长16bit，单声道的wav或者pcm
                // 写入8KHz采样的音频时，必须先调用setParameter(SpeechConstant.SAMPLE_RATE, "8000")设置正确的采样率
                // 注：当音频过长，静音部分时长超过VAD_EOS将导致静音后面部分不能识别。
                // 音频切分方法：FucUtil.splitBuffer(byte[] buffer,int length,int spsize);
                mIat!!.writeAudio(audioData, 0, audioData.size)
                mIat!!.stopListening()
            } else {
                mIat!!.cancel()
            }
        }
    }

    private var mRecognizerListener = object : RecognizerListener {
        override fun onVolumeChanged(p0: Int, p1: ByteArray?) {
            LogLevel.w(LOG, "正在说话，音量大小:$p0")
        }

        override fun onResult(p0: RecognizerResult?, isLast: Boolean) {
            printResult(p0!!)
            if (isLast && isNeedSend) {
                addChatInfo(voiceText.toString(), OBJECT_TYPE_SELF, CONTENT_TYPE_VOICE)
                reset()
            }
        }

        override fun onBeginOfSpeech() {
            LogLevel.w(LOG, "开始说话")
        }

        override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
        }

        override fun onEndOfSpeech() {
            LogLevel.w(LOG, "结束说话")
        }

        override fun onError(p0: SpeechError?) {
            LogLevel.w(LOG, "onError:${JSON.toJSONString(p0.toString())}")
            SimpleToast.showNormal(JSON.toJSONString(p0.toString()))
        }
    }

    /**
     * 打印翻译结果
     */
    private fun printResult(results: RecognizerResult) {
        voiceText = StringBuffer()
        val text = JsonParser.parseIatResult(results.resultString)
        var sn = ""
        try {
            val resultJson = JSONObject(results.resultString)
            sn = resultJson.optString("sn")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mIatResults[sn] = text
        for (key in mIatResults.keys) {
            voiceText.append(mIatResults[key])
        }
        LogLevel.w(LOG, "RecognizerResult${voiceText.toString()}")

    }

    /**
     * 添加聊天数据到界面
     */
    private fun addChatInfo(content: String, objectType: Int, contentType: String) {
        val chatInfo = RobotChatInfo()
        chatInfo.content = content
        chatInfo.objectType = objectType
        chatInfo.contentType = contentType
        chatInfo.createTime = System.currentTimeMillis()
        robotChatData.add(chatInfo)
        mAdapter.notifyDataSetChanged()
        recyclerViewRobotChat.scrollToPosition(robotChatData.size - 1)
        mPresenter.sendProblem(userId, content)
    }

}