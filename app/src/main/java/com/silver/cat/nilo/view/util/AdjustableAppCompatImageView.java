package com.silver.cat.nilo.view.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by liq on 2017/7/25.
 * <p>
 * https://inthecheesefactory.com/blog/correct-imageview-adjustviewbounds-with-adjustable-imageview/en
 */

public class AdjustableAppCompatImageView extends AppCompatImageView {
  boolean mAdjustViewBounds;

  public AdjustableAppCompatImageView(Context context) {
    super(context);
  }

  public AdjustableAppCompatImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AdjustableAppCompatImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public void setAdjustViewBounds(boolean adjustViewBounds) {
    mAdjustViewBounds = adjustViewBounds;
    super.setAdjustViewBounds(adjustViewBounds);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    Drawable mDrawable = getDrawable();
    if (mDrawable == null) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      return;
    }

    if (mAdjustViewBounds) {
      int mDrawableWidth = mDrawable.getIntrinsicWidth();
      int mDrawableHeight = mDrawable.getIntrinsicHeight();
      int heightSize = MeasureSpec.getSize(heightMeasureSpec);
      int widthSize = MeasureSpec.getSize(widthMeasureSpec);
      int heightMode = MeasureSpec.getMode(heightMeasureSpec);
      int widthMode = MeasureSpec.getMode(widthMeasureSpec);

      if (heightMode == MeasureSpec.EXACTLY && widthMode != MeasureSpec.EXACTLY) {
        // Fixed Height & Adjustable Width
        int height = heightSize;
        int width = height * mDrawableWidth / mDrawableHeight;
        if (isInScrollingContainer()) {
          setMeasuredDimension(width, height);
        } else {
          setMeasuredDimension(Math.min(width, widthSize), Math.min(height, heightSize));
        }
      } else if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
        // Fixed Width & Adjustable Height
        int width = widthSize;
        int height = width * mDrawableHeight / mDrawableWidth;
        if (isInScrollingContainer()) {
          setMeasuredDimension(width, height);
        } else {
          setMeasuredDimension(Math.min(width, widthSize), Math.min(height, heightSize));
        }
      } else {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      }
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
  }

  private boolean isInScrollingContainer() {
    ViewParent p = getParent();
    while (p != null && p instanceof ViewGroup) {
      if (((ViewGroup) p).shouldDelayChildPressedState()) {
        return true;
      }
      p = p.getParent();
    }
    return false;
  }
}
