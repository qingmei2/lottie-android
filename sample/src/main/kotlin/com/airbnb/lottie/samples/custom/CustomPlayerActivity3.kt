package com.airbnb.lottie.samples.custom

import android.animation.ObjectAnimator
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
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.model.layer.BaseLayer
import com.airbnb.lottie.samples.R
import com.airbnb.lottie.samples.databinding.ActivityPlayerTheme2Binding
import com.airbnb.lottie.samples.databinding.ActivityPlayerTheme3Binding
import com.airbnb.lottie.samples.databinding.ActivityPlayerThemeBinding
import com.airbnb.lottie.samples.utils.viewBinding

/**
 * 第二种方案: 将占位贴图替换成原生的布局控件.
 */
class CustomPlayerActivity3 : AppCompatActivity() {
    private val binding: ActivityPlayerTheme3Binding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.playerView.setOnClickListener { _ -> Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show() }
        binding.playerView.setRepeatCount(ValueAnimator.INFINITE)

        // 1.拿到歌曲原始图片
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.song_cover)
        val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.song_cover2)
        val bitmap3 = BitmapFactory.decodeResource(resources, R.drawable.song_cover3)

        // 2.裁剪图片宽高，应与lottie动画的占位图宽高一致，然后弄成圆形
        val circularBitmap1 = getCircleBitmap(bitmap)
        val circularBitmap2 = getCircleBitmap(bitmap2)
        val circularBitmap3 = getCircleBitmap(bitmap3)

        binding.playerView.imageAssetsFolder = "images/"
        binding.playerView.setImageAssetDelegate(
            LottieAssetDelegate3(
                this@CustomPlayerActivity3,
                "song_cover.webp", circularBitmap1,
                "song_cover2.webp", circularBitmap2,
                "song_cover3.webp", circularBitmap3,
                "images/"
            ),
        )

        binding.playerView.setAnimation(R.raw.player3)

        binding.btnPlay.setOnClickListener { _ ->
            binding.playerView.playAnimation()
        }
        binding.btnPause.setOnClickListener { _ -> binding.playerView.pauseAnimation() }

        binding.btnPre.setOnClickListener { _ ->
            Toast.makeText(this, "上一曲", Toast.LENGTH_SHORT).show()

            animateImageView(binding.lavForeground)
        }
        binding.btnNext.setOnClickListener { _ ->
            Toast.makeText(this, "下一曲", Toast.LENGTH_SHORT).show()
        }
    }

    fun animateImageView(view: View) {
        // 获取视图的初始位置
        val startX = view.translationX

        // 设置动画目标位置，这里设置为屏幕宽度，视图将完全移出屏幕
        val screenWidth = view.resources.displayMetrics.widthPixels
        val endX = screenWidth.toFloat()

        // 创建平移动画
        val animator = ObjectAnimator.ofFloat(view, "translationX", startX, endX)
        animator.duration = 500 // 设置动画持续时间为1秒
        animator.interpolator = LinearInterpolator() // 设置线性插值器，动画速度均匀

        // 启动动画
        animator.start()
    }

    private fun getCircleBitmap(bitmap: Bitmap): Bitmap {
        val scale = 413f / bitmap.width
        val matrix = Matrix().apply { postScale(scale, scale) }
        val scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

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

        bitmap.recycle()
        scaledBitmap.recycle()

        return circularBitmap
    }
}
