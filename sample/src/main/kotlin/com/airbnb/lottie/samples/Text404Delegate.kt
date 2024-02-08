package com.airbnb.lottie.samples

import android.util.Log
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.TextDelegate

class Text404Delegate : TextDelegate {

    constructor(animationView: LottieAnimationView?) : super(animationView)

    constructor(drawable: LottieDrawable?) : super(drawable)

    init {
        setText("L", "4")
        setText("C", "0")
        setText("R", "4")
    }

    override fun getText(layerName: String, input: String): String {
        val result = super.getText(layerName, input)
        Log.e("meiqing", "layerName = $layerName, input = $input, result = $result")
        return result
    }
}
