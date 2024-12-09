package com.airbnb.lottie.samples.custom

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.annotation.FloatRange
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.model.layer.BaseLayer
import com.airbnb.lottie.samples.R
import com.airbnb.lottie.samples.databinding.ActivityPlayerTheme3Binding
import com.airbnb.lottie.samples.utils.viewBinding
import com.airbnb.lottie.value.LottieFrameInfo
import com.airbnb.lottie.value.LottieValueCallback
import com.airbnb.lottie.value.ScaleXY

/**
 * 第二种方案: 将占位贴图替换成原生的布局控件.
 */
class CustomPlayerActivity2 : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, CustomPlayerActivity2::class.java)
            context.startActivity(intent)
        }
    }

    private val binding: ActivityPlayerTheme3Binding by viewBinding()

    /**
     * 上层封面位移动画，有两个阶段，取值范围 [0f, 1f]，分别对应动画的开始和结束.
     */
    private var musicCoverPos: Float = 0f

    /**
     * 下层封面缩放动画，有两个阶段，取值范围[0f, 1f]，分别对应动画的开始和结束.
     */
    private var musicCoverScale: Float = 0f

    private var musicBottomCoverScale: ScaleXY = ScaleXY(0f, 0f)

    @FloatRange(from = 0.0, to = 360.0)
    private var rotateAngel: Float = 0f
    private var rotateAnimator: ValueAnimator? = null

    private var musicPointerValue: Float? = null

    private var delegate: LottieAssetDelegate? = null

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

        delegate = LottieAssetDelegate(
            this@CustomPlayerActivity2,
            "topCover",
            "bottomCover",
            arrayOf(circularBitmap1, circularBitmap2, circularBitmap3),
        )

        binding.playerView.imageAssetsFolder = "images/"
        binding.playerView.setImageAssetDelegate(delegate)

        // 图片背景
//        BaseLayer.layerView = binding.ivAvatar

        binding.playerView.setAnimation(R.raw.player4)

        // 唱针-位移动画
//        val musicPointer = KeyPath("腰杆")
//        binding.playerView.addValueCallback(
//            musicPointer, LottieProperty.TRANSFORM_ROTATION,
//            object : LottieValueCallback<Float>() {
//                override fun getValue(frameInfo: LottieFrameInfo<Float?>): Float? {
//                    if (musicPointerValue == null) {
//                        return super.getValue(frameInfo)
//                    }
////                    Log.e("meiqing" , "value = " + musicPointerValue)
//                    return musicPointerValue
//                }
//            },
//        )

        // 上层封面-出场动画
        val cdBackground1 = KeyPath("上层")
        binding.playerView.addValueCallback(
            cdBackground1, LottieProperty.TRANSFORM_POSITION,
            object : LottieValueCallback<PointF>() {
                override fun getValue(frameInfo: LottieFrameInfo<PointF>): PointF? {
                    val startX = frameInfo.startValue?.x ?: 0f
                    val startY = frameInfo.startValue?.y ?: 0f
                    val endX = frameInfo.endValue?.x ?: 0f
                    val endY = frameInfo.endValue?.y ?: 0f

                    val result = if (musicCoverPos <= 0f) {
                        PointF(startX, startY)
                    } else if (musicCoverPos >= 1f) {
                        PointF(endX, endY)
                    } else {
                        PointF(
                            startX + (endX - startX) * musicCoverPos,
                            startY + (endY - startY) * musicCoverPos,
                        )
                    }
//                    Log.e("meiqing", "碟01 => (${result.x},${result.y}), musicPos = " + musicCoverPos)
                    return result
                }
            },
        )

        // 上层封面-旋转动画
        val cdRotate1 = KeyPath("上层", "上层封面图")
        binding.playerView.addValueCallback(
            cdRotate1, LottieProperty.TRANSFORM_ROTATION,
            object : LottieValueCallback<Float>() {
                override fun getValue(frameInfo: LottieFrameInfo<Float>): Float {
//                    Log.e("meiqing" , "value = " + rotateAngel)
                    return rotateAngel
                }
            },
        )

        // 下层封面-出场动画
        val cdBackground2 = KeyPath("下层")
        binding.playerView.addValueCallback(
            cdBackground2, LottieProperty.TRANSFORM_SCALE,
            object : LottieValueCallback<ScaleXY>() {
                override fun getValue(frameInfo: LottieFrameInfo<ScaleXY>): ScaleXY? {
                    val startX = frameInfo.startValue?.scaleX ?: 0f     // 0.85f
                    val startY = frameInfo.startValue?.scaleY ?: 0f
                    val endX = frameInfo.endValue?.scaleX ?: 0f         // 1.0f
                    val endY = frameInfo.endValue?.scaleY ?: 0f

                    val result = if (musicCoverScale <= 0f) {
                        ScaleXY(0f, 0f)
                    } else if (musicCoverScale > 1f) {
                        ScaleXY(0f, 0f)
                    } else {
                        ScaleXY(
                            startX + (endX - startX) * musicCoverScale,
                            startY + (endY - startY) * musicCoverScale,
                        )
                    }
                    Log.e("meiqing", "碟02 =>scaleXY = (${result.scaleX},${result.scaleY})，pos = " + musicCoverScale)
                    return result
                }
            },
        )

        // 上层封面-旋转动画
        val cdRotate2 = KeyPath("下层", "下层封面图")
        binding.playerView.addValueCallback(
            cdRotate2, LottieProperty.TRANSFORM_ROTATION,
            object : LottieValueCallback<Float>() {
                override fun getValue(frameInfo: LottieFrameInfo<Float>): Float {
//                    Log.e("meiqing" , "value = " + rotateAngel)
                    return rotateAngel
                }
            },
        )

        binding.btnPlay.setOnClickListener { _ ->
            binding.playerView.playAnimation()
            onSongPlaying()

            refreshRotateAnim()
        }
        binding.btnPause.setOnClickListener { _ ->
            onSongStop()
        }

        binding.btnPre.setOnClickListener { _ ->
            Toast.makeText(this, "上一曲", Toast.LENGTH_SHORT).show()
//            animateImageView(binding.lavForeground)
            onPreOrNext(false)
        }
        binding.btnNext.setOnClickListener { _ ->
            Toast.makeText(this, "下一曲", Toast.LENGTH_SHORT).show()
            onPreOrNext(true)
        }
    }

    private fun refreshRotateAnim() {
        if (rotateAnimator != null) {
            rotateAnimator?.cancel()
            rotateAnimator = null
            rotateAngel = 0f
        }

        // 创建ValueAnimator，设置动画的起始值和结束值为0和360
        val valueAnimator = ValueAnimator.ofFloat(0f, 360f)
        // 设置动画的持续时间为5000毫秒（5秒）
        valueAnimator.duration = 5000L
        // 设置重复模式为无限重复
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        // 设置重复行为为正常（即旋转回来）
        valueAnimator.repeatMode = ValueAnimator.RESTART
        // 设置动画的插值器为线性，确保旋转速度均匀
        valueAnimator.interpolator = LinearInterpolator()
        // 添加UpdateListener来更新ImageView的旋转角度
        valueAnimator.addUpdateListener {
            rotateAngel = it.animatedValue as Float
        }
        // 启动动画
        valueAnimator.start()

        rotateAnimator = valueAnimator
    }

    private fun onPreOrNext(next: Boolean) {
        // 切歌：
        // 1.歌曲指针波动一个来回
//        ValueAnimator.ofFloat(musicPointerPos[0][1], musicPointerPos[0][0], musicPointerPos[0][1]).apply {
//            duration = 1200 // 动画持续时间，单位为毫秒
//            addUpdateListener { animation ->
//                musicPointerValue = animation.animatedValue as Float
//            }
//            addListener(object : Animator.AnimatorListener {
//                override fun onAnimationStart(p0: Animator) {
//                }
//
//                override fun onAnimationEnd(p0: Animator) {
//                }
//
//                override fun onAnimationCancel(p0: Animator) {
//                }
//
//                override fun onAnimationRepeat(p0: Animator) {
//                }
//
//            })
//            start() // 开始动画
//        }

        // 2.歌曲封面图动画
        val valueAnimator = when (next) {
            true -> ValueAnimator.ofFloat(0f, 1f)
            false -> ValueAnimator.ofFloat(1f, 0f)
        }

        valueAnimator.apply {
            duration = 1200 // 动画持续时间，单位为毫秒
            addUpdateListener { animation ->
                musicCoverPos = animation.animatedValue as Float
                musicCoverScale = if (next) {
                    musicCoverPos
                } else {
                    1f
                }
            }
            addListener(
                object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {
                    }

                    override fun onAnimationEnd(p0: Animator) {
                        musicCoverPos = 0f
                        musicCoverScale = 1f

                        delegate?.notifySongChanged(next, binding.ivAvatar, binding.playerView)

                        refreshRotateAnim()
                    }

                    override fun onAnimationCancel(p0: Animator) {
                    }

                    override fun onAnimationRepeat(p0: Animator) {
                    }

                },
            )
            start() // 开始动画
        }
    }

    private fun onSongPlaying() {
        if (rotateAnimator != null) {
            rotateAnimator!!.resume()
        }

//        ValueAnimator.ofFloat(musicPointerPos[0][0], musicPointerPos[0][1]).apply {
//            duration = 600L // 动画持续时间，单位为毫秒
//            addUpdateListener { animation ->
//                musicPointerValue = animation.animatedValue as Float
//            }
//            addListener(object : Animator.AnimatorListener {
//                override fun onAnimationStart(p0: Animator) {
//                }
//
//                override fun onAnimationEnd(p0: Animator) {
//                }
//
//                override fun onAnimationCancel(p0: Animator) {
//                }
//
//                override fun onAnimationRepeat(p0: Animator) {
//                }
//
//            })
//            start() // 开始动画
//        }
    }

    private fun onSongStop() {
        if (rotateAnimator != null) {
            rotateAnimator!!.pause()
        }
//        ValueAnimator.ofFloat(musicPointerPos[1][0], musicPointerPos[1][1]).apply {
//            duration = 600L // 动画持续时间，单位为毫秒
//            addUpdateListener { animation ->
//                musicPointerValue = animation.animatedValue as Float
//            }
//            addListener(object : Animator.AnimatorListener {
//                override fun onAnimationStart(p0: Animator) {
//                }
//
//                override fun onAnimationEnd(p0: Animator) {
//                }
//
//                override fun onAnimationCancel(p0: Animator) {
//                }
//
//                override fun onAnimationRepeat(p0: Animator) {
//                }
//
//            })
//            start() // 开始动画
//        }
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
        val scale = 480f / bitmap.width
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
