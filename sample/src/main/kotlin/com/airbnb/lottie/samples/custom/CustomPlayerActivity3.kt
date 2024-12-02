package com.airbnb.lottie.samples.custom

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.model.layer.BaseLayer
import com.airbnb.lottie.samples.R
import com.airbnb.lottie.samples.databinding.ActivityPlayerTheme3Binding
import com.airbnb.lottie.samples.utils.viewBinding
import com.airbnb.lottie.utils.MiscUtils
import com.airbnb.lottie.value.LottieFrameInfo
import com.airbnb.lottie.value.LottieValueCallback

/**
 * 第二种方案: 将占位贴图替换成原生的布局控件.
 */
class CustomPlayerActivity3 : AppCompatActivity() {
    private val binding: ActivityPlayerTheme3Binding by viewBinding()

    private val musicPointerPos = arrayOf(
        arrayOf(-17f, 0f),  // 指针开始动画
        arrayOf(0f, -17f),  // 指针结束动画
    )

    /**
     * 封面动画，有两个阶段，取值范围 [0f, 1f]，分别对应动画的开始和结束.
     */
    private var musicCoverPos: Float = 0f

    private var musicPointerValue: Float? = null

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
                "images/",
            ),
        )

        // 图片背景
        BaseLayer.layerView = binding.ivAvatar

        binding.playerView.setAnimation(R.raw.player3)

        // 音乐指针
        val musicPointer = KeyPath("腰杆")
        binding.playerView.addValueCallback(
            musicPointer, LottieProperty.TRANSFORM_ROTATION,
            object : LottieValueCallback<Float>() {
                override fun getValue(frameInfo: LottieFrameInfo<Float?>): Float? {
                    if (musicPointerValue == null) {
                        return super.getValue(frameInfo)
                    }
//                    Log.e("meiqing" , "value = " + musicPointerValue)
                    return musicPointerValue
                }
            },
        )

        val cdBackground1 = KeyPath("胶片_1")
        binding.playerView.addValueCallback(
            cdBackground1, LottieProperty.TRANSFORM_POSITION,
            object : LottieValueCallback<PointF>() {
                override fun getValue(frameInfo: LottieFrameInfo<PointF>): PointF? {
                    val startX = frameInfo.startValue?.x ?: 0f
                    val startY = frameInfo.startValue?.y ?: 0f
                    val endX = frameInfo.endValue?.x ?: 0f
                    val endY = frameInfo.endValue?.y ?: 0f

                    return if (musicCoverPos <= 0f) {
                        PointF(startX, startY)
                    } else if (musicCoverPos >= 1f) {
                        PointF(endX, endY)
                    } else {
                        PointF(
                            startX + (endX - startX) * musicCoverPos,
                            startY + (endY - startY) * musicCoverPos,
                        )
                    }
                }
            },
        )

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
            onPreOrNext()
        }
        binding.btnNext.setOnClickListener { _ ->
            Toast.makeText(this, "下一曲", Toast.LENGTH_SHORT).show()
            onPreOrNext()
        }
    }

    private fun onPreOrNext() {
        // 切歌：
        // 1.歌曲指针波动一个来回
        ValueAnimator.ofFloat(musicPointerPos[0][1], musicPointerPos[0][0], musicPointerPos[0][1]).apply {
            duration = 1200 // 动画持续时间，单位为毫秒
            addUpdateListener { animation ->
                musicPointerValue = animation.animatedValue as Float
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }

            })
            start() // 开始动画
        }

        // 2.歌曲封面图动画
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1200 // 动画持续时间，单位为毫秒
            addUpdateListener { animation ->
                musicCoverPos = animation.animatedValue as Float
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {
                    musicCoverPos = 1f
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }

            })
            start() // 开始动画
        }
    }

    private fun onSongPlaying() {
        ValueAnimator.ofFloat(musicPointerPos[0][0], musicPointerPos[0][1]).apply {
            duration = 600L // 动画持续时间，单位为毫秒
            addUpdateListener { animation ->
                musicPointerValue = animation.animatedValue as Float
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }

            })
            start() // 开始动画
        }
    }

    private fun onSongStop() {
        ValueAnimator.ofFloat(musicPointerPos[1][0], musicPointerPos[1][1]).apply {
            duration = 600L // 动画持续时间，单位为毫秒
            addUpdateListener { animation ->
                musicPointerValue = animation.animatedValue as Float
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }

            })
            start() // 开始动画
        }
    }

//    fun animateImageView(view: View) {
//        // 获取视图的初始位置
//        val startX = view.translationX
//
//        // 设置动画目标位置，这里设置为屏幕宽度，视图将完全移出屏幕
//        val screenWidth = view.resources.displayMetrics.widthPixels
//        val endX = screenWidth.toFloat()
//
//        // 创建平移动画
//        val animator = ObjectAnimator.ofFloat(view, "translationX", startX, endX)
//        animator.duration = 500 // 设置动画持续时间为1秒
//        animator.interpolator = LinearInterpolator() // 设置线性插值器，动画速度均匀
//
//        // 启动动画
//        animator.start()
//    }

    private fun getCircleBitmap(bitmap: Bitmap): Bitmap {
        val scale = 413f / bitmap.width
        val matrix = Matrix().apply { postScale(scale, scale) }
        val scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        val circularBitmap = Bitmap.createBitmap(
            scaledBitmap.width, scaledBitmap.height,
            Bitmap.Config.ARGB_8888,
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
