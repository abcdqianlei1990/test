package cn.xzj.agent.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import cn.xzj.agent.R;


/**
 * @Author yemao
 * @Email yrmao9893@163.com
 * @Date 2017/6/5
 * @Des null!
 */

public class TagCloudConfiguration {
    private static final int DEFAULT_LINE_SPACING = 5;
    private static final int DEFAULT_TAG_SPACING = 10;

    private int lineSpacing;
    private int tagSpacing;

    public TagCloudConfiguration(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagCloudLayout);
        try {
            lineSpacing = a.getDimensionPixelSize(R.styleable.TagCloudLayout_lineSpacing, DEFAULT_LINE_SPACING);
            tagSpacing = a.getDimensionPixelSize(R.styleable.TagCloudLayout_tagSpacing, DEFAULT_TAG_SPACING);
        } finally {
            a.recycle();
        }
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public int getTagSpacing() {
        return tagSpacing;
    }

    public void setTagSpacing(int tagSpacing) {
        this.tagSpacing = tagSpacing;
    }
}
