package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var robotoFont = "roboto"
    private var label = "Download"
    // May need @Volile we will see.
    private var progress: Double = 0.0
    private var textColor: Int = Color.BLACK // default color



    // TODO: This should be used to animate the action of the download button
    private val valueAnimator = ValueAnimator()

    // Unsure what this does specifically.
    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
        color = Color.WHITE
        typeface = Typeface.create(robotoFont, Typeface.NORMAL)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Sets the initial background color for the button
        canvas.drawRGB(7, 194, 170)
        // Sets the initial text for the button
        canvas.drawText(label,475f, 70f, paint)

        // If the Button State is Clicked
        // If the Button State is Loading

        // If the Button State is Completed


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

    // Used to start animations
    private fun startAnimation() {
        valueAnimator.start()
    }

    // Used to stop animations
    private fun stopAnimation() {
        valueAnimator.end()
    }

}