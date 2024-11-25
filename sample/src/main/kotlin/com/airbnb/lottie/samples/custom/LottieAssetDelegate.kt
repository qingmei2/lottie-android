package com.airbnb.lottie.samples.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import com.airbnb.lottie.ImageAssetDelegate
import com.airbnb.lottie.LottieImageAsset
import java.io.IOException
import java.io.InputStream

class LottieAssetDelegate(
    private val context: Context, private val replaceImgName: String, private val replaceBitmap: Bitmap,
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
        if (replaceImgName == asset.fileName) {
            return replaceBitmap
        }
        return null
    }
}


