package com.n.library.util;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 水印工具
 * 全局设置需要在BaseActivity基类中定义
 */
public class WaterMarkUtil {
    /**
     * 水印文本
     */
    private String mText;
    /**
     * 字体颜色，十六进制形式，例如：0xF2F2F2
     * 这里不需要设置透明度，抛出 mTextAlpha字段，动态设置透明度
     */
    private int mTextColor;
    /**
     * 字体透明度 范围0~255  比如不透明度6% 等于255x6%=15
     * 默认=15
     */
    private int mTextAlpha;
    /**
     * 字体大小，单位为sp
     */
    private float mTextSize;
    /**
     * 旋转角度
     */
    private float mRotation;
    /**
     * 顶部间距
     */
    private int mTopMargin;
    /**
     * 顶部间距
     */
    private int mBottomMargin;

    private static WaterMarkUtil sInstance;

    //默认属性：文本、字体颜色、大小、旋转角度
    private WaterMarkUtil() {
        mText = "";
        mTextColor = 0xFF222222;//0xF0F2F2F2
        mTextAlpha = 15;//240
        mTextSize = 13;
        mRotation = -18;
        mTopMargin = 0;
        mBottomMargin = 0;
    }

    public static WaterMarkUtil getInstance() {
        if (sInstance == null) {
            synchronized (WaterMarkUtil.class) {
                sInstance = new WaterMarkUtil();
            }
        }
        return sInstance;
    }

    /**
     * 设置水印文本
     *
     * @param text 文本
     * @return Watermark实例
     */
    public WaterMarkUtil setText(String text) {
        mText = text;
        return sInstance;
    }

    /**
     * 设置字体颜色
     *
     * @param color 颜色，十六进制形式，例如：0xAEAEAEAE
     * @return Watermark实例
     */
    public WaterMarkUtil setTextColor(int color) {
        mTextColor = color;
        return sInstance;
    }

    /**
     * 设置水印字体透明度
     *
     * @param alpha 默认15 即6%透明度
     */
    public WaterMarkUtil setTextAlpha(int alpha) {
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 255) {
            alpha = 255;
        }
        this.mTextAlpha = alpha;
        return sInstance;
    }

    /**
     * 设置字体大小
     *
     * @param size 大小，单位为sp
     * @return Watermark实例
     */
    public WaterMarkUtil setTextSize(float size) {
        mTextSize = size;
        return sInstance;
    }

    /**
     * 设置旋转角度
     *
     * @param degrees 度数
     * @return Watermark实例
     */
    public WaterMarkUtil setRotation(float degrees) {
        mRotation = degrees;
        return sInstance;
    }

    /**
     * 设置距离顶部边距
     *
     * @param mTopMargin
     */
    public WaterMarkUtil setTopMargin(int mTopMargin) {
        this.mTopMargin = mTopMargin;
        return sInstance;
    }

    /**
     * 设置距离底部边距
     *
     * @param mBottomMargin
     */
    public WaterMarkUtil setBottomMargin(int mBottomMargin) {
        this.mBottomMargin = mBottomMargin;
        return sInstance;
    }

    /**
     * 显示水印，铺满整个页面
     *
     * @param activity 活动
     */
    public void show(Activity activity) {
        show(activity, mText);
    }

    /**
     * 显示水印，铺满整个页面
     *
     * @param activity 活动
     * @param text     水印
     */
    public void show(Activity activity, String text) {
        WatermarkDrawable drawable = new WatermarkDrawable();
        drawable.mText = text;
        drawable.mTextColor = mTextColor;
        drawable.mTextAlpha = mTextAlpha;
        drawable.mTextSize = mTextSize;
        drawable.mRotation = mRotation;

        ViewGroup rootView = activity.findViewById(android.R.id.content);
        FrameLayout layout = new FrameLayout(activity);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = mTopMargin;
        layoutParams.bottomMargin = mBottomMargin;
        layout.setLayoutParams(layoutParams);
        layout.setBackground(drawable);
        rootView.addView(layout);
    }

    private class WatermarkDrawable extends Drawable {
        private Paint mPaint;
        /**
         * 水印文本
         */
        private String mText;
        /**
         * 字体颜色，十六进制形式，例如：0xF2F2F2
         */
        private int mTextColor;
        /**
         * 字体透明度 0~255  比如不透明度6% 等于255x6%=15
         * 默认=15
         */
        private int mTextAlpha;
        /**
         * 字体大小，单位为sp
         */
        private float mTextSize;
        /**
         * 旋转角度
         */
        private float mRotation;

        private WatermarkDrawable() {
            mPaint = new Paint();
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            int width = getBounds().right;
            int height = getBounds().bottom;
            int diagonal = (int) Math.sqrt(width * width + height * height); // 对角线的长度

            mPaint.setColor(mTextColor);
            mPaint.setAlpha(mTextAlpha);//设置alpha不透明度，范围为0~255
            mPaint.setTextSize(NDisplayUtil.sp2px(mTextSize)); // ConvertUtils.spToPx()这个方法是将sp转换成px，ConvertUtils这个工具类在我提供的demo里面有
            mPaint.setAntiAlias(true);//是否抗锯齿
            float textWidth = mPaint.measureText(mText);

            canvas.drawColor(0x00000000);
            canvas.rotate(mRotation);

            int index = 0;
            float fromX;
            // 以对角线的长度来做高度，这样可以保证竖屏和横屏整个屏幕都能布满水印
            for (int positionY = diagonal / 10; positionY <= diagonal; positionY += diagonal / 10) {
                fromX = -width + (index++ % 2) * textWidth; // 上下两行的X轴起始点不一样，错开显示
                for (float positionX = fromX; positionX < width; positionX += textWidth * 2) {
                    canvas.drawText(mText, positionX, positionY, mPaint);
                }
            }

            canvas.save();
            canvas.restore();
        }

        @Override
        public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

    }
}
