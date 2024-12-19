package ru.nyakshoot.aston_intensive_2.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        textSize = 60f
        textAlign = Paint.Align.CENTER
    }

    private var text: String = ""

    fun setText(newText: String) {
        text = newText
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val xPos = width / 2f
        val yPos = height / 2f - (paint.descent() + paint.ascent()) / 2
        canvas.drawText(text, xPos, yPos, paint)
    }
}