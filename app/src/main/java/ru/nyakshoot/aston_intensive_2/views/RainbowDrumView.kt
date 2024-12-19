package ru.nyakshoot.aston_intensive_2.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import kotlin.math.abs
import kotlin.math.min

class RainbowDrumView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val rainbowColors = listOf(
        Color.rgb(255, 0, 0) to "Text",
        Color.rgb(255, 165, 0) to "Image",
        Color.rgb(255, 255, 0) to "Text",
        Color.rgb(0, 255, 0) to "Image",
        Color.rgb(0, 255, 255) to "Text",
        Color.rgb(0, 0, 255) to "Image",
        Color.rgb(138, 43, 226) to "Text"
    )

    private val drumBounds = RectF()
    private val drumPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var _isRotating = false
    val isRotating: Boolean
        get() = _isRotating

    private var rotation = 0f
    private var rotationAnimator: ObjectAnimator? = null
    private var scaleFactor = 0.5f

    private val sweepAngle = 360f / rainbowColors.size

    fun setScale(scale: Float) {
        scaleFactor = 0.25f + (scale / 100f)
        updateDrumBounds()
        invalidate()
    }

    private fun updateDrumBounds() {
        val w = width
        val h = height
        val diameter = min(w, h).toFloat()
        val scaledDiameter = diameter * scaleFactor

        val centerX = w / 2f
        val centerY = h / 2f

        val left = centerX - (scaledDiameter / 2f)
        val top = centerY - (scaledDiameter / 2f)
        val right = centerX + (scaledDiameter / 2f)
        val bottom = centerY + (scaledDiameter / 2f)

        drumBounds.set(left, top, right, bottom)
    }

    fun startRotation(onFinish: (String) -> Unit) {
        if (_isRotating) return

        rotationAnimator?.cancel()
        _isRotating = true

        val randomRotation = 360 * (1 until 5).random() + (0 until 360).random()

        rotationAnimator = ObjectAnimator.ofFloat(
            this,
            "rotation",
            rotation,
            rotation + randomRotation
        )

        rotationAnimator?.apply {
            duration = 2000
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    rotation = (rotation + randomRotation) % 360f
                    _isRotating = false
                    onFinish(getCurrentColorName())
                }
            })
            start()
        }
    }

    private fun getCurrentColorName(): String {
        val colorIndex = abs((270 - (rotation % 360)) / sweepAngle).toInt()
        return rainbowColors[colorIndex].second
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateDrumBounds()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        rainbowColors.forEachIndexed { index, color ->
            drumPaint.color = color.first
            canvas.drawArc(
                drumBounds,
                index * sweepAngle,
                sweepAngle,
                true,
                drumPaint
            )
        }
    }
}