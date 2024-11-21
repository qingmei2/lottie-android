package com.airbnb.lottie.samples.custom

import android.animation.ValueAnimator
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
import com.airbnb.lottie.samples.R
import com.airbnb.lottie.samples.databinding.ActivityPlayerThemeBinding
import com.airbnb.lottie.samples.utils.viewBinding


class CustomPlayerActivity : AppCompatActivity() {
    private val binding: ActivityPlayerThemeBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.playerView.setOnClickListener { _ -> Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show() }
        binding.playerView.setRepeatCount(ValueAnimator.INFINITE)


        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.song_cover)
        val scale = 240f / bitmap.width     // 将图片宽度缩放为100px
        val matrix = Matrix().apply { postScale(scale, scale) }
        val scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        // 创建一个新的Bitmap对象，用于存放裁剪后的圆形Bitmap
        val circularBitmap = Bitmap.createBitmap(
            scaledBitmap.width, scaledBitmap.height,
            Bitmap.Config.ARGB_8888
        )

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val canvas = Canvas(circularBitmap)
        canvas.drawColor(Color.TRANSPARENT)
        val centerX = scaledBitmap.width / 2
        val centerY = scaledBitmap.height / 2
        val radius = Math.min(centerX, centerY).toFloat()
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius, paint)
        val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        paint.xfermode = xfermode
        canvas.drawBitmap(scaledBitmap, 0f, 0f, paint)

            scaledBitmap.recycle() // 回收原Bitmap

        binding.playerView.setImageAssetDelegate(
            LottieAssetDelegate(
                this@CustomPlayerActivity, "song_cover.webp",
//                scaledBitmap,
                    circularBitmap,
                "",
            ),
        )
        binding.playerView.setAnimation(R.raw.player2)

        binding.btnPlay.setOnClickListener { _ ->
            binding.playerView.playAnimation()
        }
        binding.btnPause.setOnClickListener { _ -> binding.playerView.pauseAnimation() }

        binding.btnPre.setOnClickListener { _ ->
            Toast.makeText(this, "上一曲", Toast.LENGTH_SHORT).show()
        }
        binding.btnNext.setOnClickListener { _ ->
            Toast.makeText(this, "下一曲", Toast.LENGTH_SHORT).show()
        }
    }
}
