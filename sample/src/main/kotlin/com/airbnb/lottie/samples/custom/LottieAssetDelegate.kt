package com.airbnb.lottie.samples.custom

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import com.airbnb.lottie.ImageAssetDelegate
import com.airbnb.lottie.LottieImageAsset

class LottieAssetDelegate(
    private val context: Context,
    private val replaceImgName1: String, private val replaceBitmap1: Bitmap,
    private val replaceImgName2: String, private val replaceBitmap2: Bitmap,
    private val replaceImgName3: String, private val replaceBitmap3: Bitmap,
    imagesFolder: String
) : ImageAssetDelegate {
    private var imagesFolder: String? = null

    init {
        if (!TextUtils.isEmpty(imagesFolder) && imagesFolder[imagesFolder.length - 1] != '/') {
            this.imagesFolder = "$imagesFolder/"
        } else {
            this.imagesFolder = imagesFolder
        }
    }

    override fun fetchBitmap(asset: LottieImageAsset): Bitmap? {
        if (replaceImgName1 == asset.fileName) {
            return replaceBitmap1
        } else if (replaceImgName2 == asset.fileName) {
            return replaceBitmap2
        } else if (replaceImgName3 == asset.fileName) {
            return replaceBitmap3
        }
        return null
    }
}


