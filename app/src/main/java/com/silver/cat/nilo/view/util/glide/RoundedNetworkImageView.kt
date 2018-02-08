package com.silver.cat.nilo.view.util.glide

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.widget.ImageView
import com.silver.cat.nilo.R
import com.silver.cat.nilo.view.util.toPx

open class RoundedNetworkImageView @JvmOverloads constructor(context: Context,
                                                             attrs: AttributeSet? = null,
                                                             defStyle: Int = 0)
  : NetworkImageView(context, attrs, defStyle) {

  var radius: Float = 0.0f
  var leftTopRadius: Boolean = true
  var leftBottomRadius: Boolean = true
  var rightTopRadius: Boolean = true
  var rightBottomRadius: Boolean = true
  var borderWidth: Float = 0.0f
    set(value) {
      field = value
      borderPaint.strokeWidth = value
    }
  var borderColor: Int = Color.TRANSPARENT
    set(value) {
      field = value
      borderPaint.color = value
    }
  var isCircular: Boolean = false

  private val dstRect: Rect = Rect()
  private val dstRectF: RectF = RectF()
  private val dstBorderRectF: RectF = RectF()
  private val borderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val maskPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

  init {
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    borderPaint.style = Paint.Style.STROKE

    if (attrs != null) {
      val a = context.theme
          .obtainStyledAttributes(attrs, R.styleable.RoundedNetworkImageView, 0, 0)
      val defaultImageResId: Int
      try {
        defaultImageResId = a.getResourceId(R.styleable.RoundedNetworkImageView_defaultImage, 0)
        radius = a.getDimensionPixelSize(R.styleable.RoundedNetworkImageView_roundedRadius,
            0).toFloat()
        leftTopRadius = a.getBoolean(R.styleable.RoundedNetworkImageView_leftTopRoundedRadius, true)
        leftBottomRadius = a.getBoolean(R.styleable.RoundedNetworkImageView_leftBottomRoundedRadius,
            true)
        rightTopRadius = a.getBoolean(R.styleable.RoundedNetworkImageView_rightTopRoundedRadius,
            true)
        rightBottomRadius = a.getBoolean(R.styleable.RoundedNetworkImageView_rightBottomRoundedRadius,
            true)
        borderColor = a.getColor(R.styleable.RoundedNetworkImageView_borderColor,
            Color.TRANSPARENT)
        borderWidth = a.getDimensionPixelSize(R.styleable.RoundedNetworkImageView_roundedBorderWidth,
            0).toFloat()
      } finally {
        a.recycle()
      }
      if (defaultImageResId != 0) {
        setDefaultImageResId(defaultImageResId, ImageView.ScaleType.CENTER_CROP)
      }
    }
  }

  override fun onDraw(canvas: Canvas) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      saveLayerGreater21(canvas)
    } else {
      saveLayerUnder21(canvas)
    }
    super.onDraw(canvas)
  }

  @SuppressWarnings("deprecation")
  private fun saveLayerUnder21(canvas: Canvas) {
    canvas.saveLayer(0.0f,
        0.0f,
        canvas.width.toFloat(),
        canvas.width.toFloat(),
        null,
        Canvas.ALL_SAVE_FLAG)
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private fun saveLayerGreater21(canvas: Canvas) {
    canvas.saveLayer(0.0f, 0.0f, canvas.width.toFloat(), canvas.width.toFloat(), null)
  }

  private fun createMask(): Bitmap {
    val maskWidth = dstRect.width()
    val maskHeight = dstRect.height()
    val mask = Bitmap.createBitmap(maskWidth, maskHeight, Bitmap.Config.ALPHA_8)
    val maskCanvas = Canvas(mask)
    if (leftTopRadius && leftBottomRadius && rightTopRadius && rightBottomRadius) {
      maskCanvas.drawRoundRect(dstRectF, radius, radius, maskPaint)
    } else {
      maskCanvas.drawPath(composeRoundedRectPath(dstRectF), maskPaint)
    }
    return mask
  }

  override fun dispatchDraw(canvas: Canvas) {
    if (drawable == null) {
      return
    }

    if (drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) {
      return
    }

    dstRect.set(0, 0, width, height)

    dstRectF.set(dstRect)
    dstBorderRectF.set(dstRect)
    if (borderWidth > 0.05f) {
      dstRectF.inset(borderWidth, borderWidth)
      dstBorderRectF.inset(borderWidth, borderWidth)
    }

    if (isCircular) {
      val minDrawDimen = Math.min(dstRect.width(), dstRect.height())
      val insetX = Math.max(0, (dstRect.width() - minDrawDimen) / 2)
      val insetY = Math.max(0, (dstRect.height() - minDrawDimen) / 2)
      dstRect.inset(insetX, insetY)
      radius = 0.5f * minDrawDimen
    }

    val mask = createMask()
    canvas.drawBitmap(mask, 0.0f, 0.0f, paint)
    mask.recycle()

    if (borderWidth > 0.05f) {
      if (leftTopRadius && leftBottomRadius && rightTopRadius && rightBottomRadius) {
        canvas.drawRoundRect(dstBorderRectF, radius, radius, borderPaint)
      } else {
        canvas.drawPath(composeRoundedRectPath(dstBorderRectF), borderPaint)
      }
    }
  }

  private fun composeRoundedRectPath(rect: RectF): Path {
    val path = Path()
    val leftTopDiameter = if (leftTopRadius) radius else 0.0f
    val leftBottomDiameter = if (leftBottomRadius) radius else 0.0f
    val rightTopDiameter = if (rightTopRadius) radius else 0.0f
    val rightBottomDiameter = if (rightBottomRadius) radius else 0.0f

    path.moveTo(rect.left + leftTopDiameter / 2, rect.top)
    path.lineTo(rect.right - rightTopDiameter / 2, rect.top)
    path.quadTo(rect.right, rect.top, rect.right, rect.top + rightTopDiameter / 2)
    path.lineTo(rect.right, rect.bottom - rightBottomDiameter / 2)
    path.quadTo(rect.right, rect.bottom, rect.right - rightBottomDiameter / 2, rect.bottom)
    path.lineTo(rect.left + leftBottomDiameter / 2, rect.bottom)
    path.quadTo(rect.left, rect.bottom, rect.left, rect.bottom - leftBottomDiameter / 2)
    path.lineTo(rect.left, rect.top + leftTopDiameter / 2)
    path.quadTo(rect.left, rect.top, rect.left + leftTopDiameter / 2, rect.top)
    path.close()

    return path
  }

  /**
   * @param radius
   * *     in dp
   */
  fun setRadiusInDp(radius: Int) {
    this.radius = radius.toPx.toFloat()
  }

  fun setBorderWidthInDp(borderWidth: Float) {
    this.borderWidth = borderWidth.toPx
  }

}
