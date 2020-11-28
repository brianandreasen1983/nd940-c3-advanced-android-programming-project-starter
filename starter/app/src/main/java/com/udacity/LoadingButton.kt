package com.udacity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.content_main.view.*
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var robotoFont = "roboto"
    private var label = "Download"


    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { property, oldValue, newValue ->
        when(newValue) {
            ButtonState.Loading -> {
                // Animate this value?
                loadingButton.setBackgroundColor(Color.rgb(0, 67, 73))
                label = "Downloading..."
                disableLoadingButton()
//                startAnimation()
            }

            ButtonState.Completed -> {
                // Animate this?
                loadingButton.setBackgroundColor(Color.rgb(7, 194, 170))
                label = "Download"
                enableLoadingButton()
//                stopAnimation()
            }

            ButtonState.Clicked -> {
                // Do nothing just here so the when statement is exhaustive :<
            }
        }
        // Redraw the button
        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
        color = Color.WHITE
        typeface = Typeface.create(robotoFont, Typeface.NORMAL)
    }

    private val rect = RectF(
            740f,
            50f,
            810f,
            110f
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText(label,475f, 70f, paint)
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

    private fun disableLoadingButton() {
        loadingButton.isEnabled = false
    }

    private fun enableLoadingButton() {
        loadingButton.isEnabled = true
    }

    // Used to provide a way to change the button state from the main activity
    fun setLoadingButtonState(state: ButtonState) {
        buttonState = state
    }
}