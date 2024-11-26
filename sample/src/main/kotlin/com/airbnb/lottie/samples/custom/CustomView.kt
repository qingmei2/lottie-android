package com.airbnb.lottie.samples.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class CustomView : View {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var like: View? = null

    fun setLike(view: View?) {
        like = view
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (like != null) {
            like!!.draw(canvas)
        }
    }
}
