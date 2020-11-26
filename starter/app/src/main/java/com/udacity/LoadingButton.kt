package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

// TODO: Animate properties of the button once it is clicked.
// TODO: Width of the button should be animated from left to right.
// TODO: Text is changed dynamically based on different states of the button.
// TODO: Circle gets to be animated from 0 to 360 degrees.
// TODO: Have a background color to the button...this should be specified in attrs.xml
class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    // TODO: This may be able to be specified in either styles.xml or attrs.xml
    private var textFamilyName = "roboto"
    private var label = "Testing"

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        // TODO: Would be best provided by attrs.xml
        textSize = 50.0f
        // TODO: maybe attrs.xml?
        typeface = Typeface.create(textFamilyName, Typeface.NORMAL)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // TODO: Used as a concept this should be rendered dynamically based on the state of the button....
        canvas.drawRGB(7, 194, 170)
        canvas.drawText(label,475f, 100f, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}