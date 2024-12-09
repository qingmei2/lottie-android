package com.airbnb.lottie.samples.custom

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.ext.entity.LottiePlayerExtModel
import com.airbnb.lottie.model.layer.BaseLayer
import com.airbnb.lottie.samples.R
import com.airbnb.lottie.samples.databinding.ActivityPlayerTheme3Binding
import com.airbnb.lottie.samples.utils.viewBinding
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import kotlin.math.abs


/**
 * 第二种方案: 将占位贴图替换成原生的布局控件.
 */
class CustomPlayerActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, CustomPlayerActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val binding: ActivityPlayerTheme3Binding by viewBinding()

    private var images: Array<Bitmap> = arrayOf()
    private var curIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.playerView.setOnClickListener { _ -> Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show() }
        binding.playerView.setRepeatCount(ValueAnimator.INFINITE)

        // 1.拿到歌曲原始图片
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.song_cover)
        val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.song_cover2)
        val bitmap3 = BitmapFactory.decodeResource(resources, R.drawable.song_cover3)

        // 图片背景
//        BaseLayer.layerView = binding.ivAvatar
        images = arrayOf(bitmap, bitmap2, bitmap3)

        val json = readFromAssets(this, "ext/player_config.json")
        val extModel = Gson().fromJson(json, LottiePlayerExtModel::class.java)

        binding.playerView.setAnimation(R.raw.player4, extModel)
        binding.playerView.updateCovers(images[curIndex], images[curIndex + 1])

        binding.btnPlay.setOnClickListener { _ ->
            binding.playerView.playAnimation()
            onSongPlaying()
        }
        binding.btnPause.setOnClickListener { _ ->
            onSongStop()
        }

        binding.btnPre.setOnClickListener { _ ->
            Toast.makeText(this, "上一曲", Toast.LENGTH_SHORT).show()
//            animateImageView(binding.lavForeground)
            onPrev()
        }
        binding.btnNext.setOnClickListener { _ ->
            Toast.makeText(this, "下一曲", Toast.LENGTH_SHORT).show()
            onNext()
        }
    }

    private fun onPrev() {
        val curSongBitmap = images[abs(curIndex - 1) % images.size]
        val nextSongBitmap = images[abs(curIndex) % images.size]
        binding.playerView.onPlayPrev(curSongBitmap, nextSongBitmap)
        curIndex--
    }

    private fun onNext() {
        val curSongBitmap = images[abs(curIndex + 1) % images.size]
        val nextSongBitmap = images[abs(curIndex + 2) % images.size]
        binding.playerView.onPlayNext(curSongBitmap, nextSongBitmap)
        curIndex++
    }

    private fun onSongPlaying() {
        binding.playerView.onPlayerPlaying()
    }

    private fun onSongStop() {
        binding.playerView.onPlayerStop()
    }

    fun readFromAssets(context: Context, fileName: String?): String? {
        try {
            val assetManager = context.assets
            val inputStream = assetManager.open(fileName!!)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))

            val stringBuilder = StringBuilder()
            var line: String?
            while ((bufferedReader.readLine().also { line = it }) != null) {
                stringBuilder.append(line)
            }

            bufferedReader.close()
            inputStream.close()

            return stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}
