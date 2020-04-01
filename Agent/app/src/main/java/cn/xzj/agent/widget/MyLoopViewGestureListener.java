package cn.xzj.agent.widget;

import android.view.MotionEvent;

public class MyLoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener  {
    private final MyWheelView wheelView;


    public MyLoopViewGestureListener(MyWheelView wheelView) {
        this.wheelView = wheelView;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        wheelView.scrollBy(velocityY);
        return true;
    }
}
