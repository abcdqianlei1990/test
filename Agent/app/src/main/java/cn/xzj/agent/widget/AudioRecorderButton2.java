package cn.xzj.agent.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;

import cn.xzj.agent.R;
import cn.xzj.agent.constants.Config;
import cn.xzj.agent.ui.audio.AudioManager;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/19
 * @Des 录音按钮
 */
public class AudioRecorderButton2 extends android.support.v7.widget.AppCompatButton implements AudioManager.AudioStateListener {

    //手指滑动 距离
    private static final int DISTANCE_Y_CANCEL = 50;
    //状态
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;
    //当前状态
    private int mCurState = STATE_NORMAL;
    //已经开始录音
    private boolean isRecording = false;

    private AudioDialogManager mDialogManager;

    private OnTouchEventStatusListener mOnTouchEventStatusListener;

    public AudioRecorderButton2(Context context) {
        this(context, null);
    }

    public AudioRecorderButton2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mDialogManager = new AudioDialogManager(getContext());
    }

    /**
     * 录音完成后的回调
     */
    public interface AudioFinishRecorderListener {
        //时长  和 文件
        void onFinish(float seconds, String filePath);
    }

    private AudioFinishRecorderListener mListener;

    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener) {
        mListener = listener;
    }

    private static final int MSG_AUDIO_PREPARED = 0X110;
    private static final int MSG_VOICE_CHANGED = 0X111;
    private static final int MSG_DIALOG_DISMISS = 0X112;


    @Override
    public void wellPrepared() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDialogManager.showRecordingDialog();
                changeState(STATE_RECORDING);
                if (mOnTouchEventStatusListener != null)
                    mOnTouchEventStatusListener.onAudioRecordDown();
                break;
            case MotionEvent.ACTION_MOVE:

                //根据想x,y的坐标，判断是否想要取消
                if (wantToCancel(x, y)) {
                    changeState(STATE_WANT_TO_CANCEL);
                } else {
                    changeState(STATE_RECORDING);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (wantToCancel(x, y)) {
                    if (mOnTouchEventStatusListener != null)
                        mOnTouchEventStatusListener.onAudioRecordCancel();
                } else {
                    if (mOnTouchEventStatusListener != null)
                        mOnTouchEventStatusListener.onAudioRecordSuccess();
                }
                mDialogManager.dismissDialog();
                reset();

                break;

        }
        return super.onTouchEvent(event);
    }

    /**
     * 恢复状态 标志位
     */
    private void reset() {
        isRecording = false;
        changeState(STATE_NORMAL);
    }

    private boolean wantToCancel(int x, int y) {
        //如果左右滑出 button
        if (x < 0 || x > getWidth()) {
            return true;
        }
        //如果上下滑出 button  加上我们自定义的距离
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }
        return false;
    }

    //改变状态
    private void changeState(int state) {
        if (mCurState != state) {
            mCurState = state;
            switch (state) {
                case STATE_NORMAL:
                    setText(R.string.voice_btn_state_normal);
                    break;
                case STATE_RECORDING:
                    setText(R.string.voice_btn_state_recording);

                    if (isRecording) {
                        mDialogManager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setText(R.string.voice_btn_state_cancel);
                    mDialogManager.wantToCancel();
                    break;
            }
        }
    }

    public void setmOnTouchEventStatusListener(OnTouchEventStatusListener mOnTouchEventStatusListener) {
        this.mOnTouchEventStatusListener = mOnTouchEventStatusListener;
    }

    public interface OnTouchEventStatusListener {
        void onAudioRecordDown();

        void onAudioRecordCancel();

        void onAudioRecordSuccess();
    }
}