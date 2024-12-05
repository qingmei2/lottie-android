package com.airbnb.lottie.samples.custom

import android.content.Context
import android.graphics.Bitmap
import com.airbnb.lottie.ImageAssetDelegate
import com.airbnb.lottie.LottieImageAsset

class LottieAssetDelegate(
    private val context: Context,
    private val topCoverName: String,
    private val bottomCoverName: String,
    private val images: Array<Bitmap>,
) : ImageAssetDelegate {

    private var curIndex: Int = 0

    private var curSongBitmap: Bitmap? = images[curIndex % images.size]
    private var nextSongBitmap: Bitmap? = images[curIndex + 1 % images.size]

    override fun fetchBitmap(asset: LottieImageAsset): Bitmap? {
        if (topCoverName == asset.fileName) {
            return curSongBitmap
        } else if (bottomCoverName == asset.fileName) {
            return nextSongBitmap
        }
        return null
    }

    /**
     * 切歌后，上层封面回到起始位置，并切换封面到最新歌曲；下层封面更新为下一首歌封面图.
     */
    fun notifySongChanged() {
        curSongBitmap = images[curIndex + 1 % images.size]
        nextSongBitmap = images[curIndex + 2 % images.size]
        curIndex++
    }
}


