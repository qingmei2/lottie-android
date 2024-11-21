package com.airbnb.lottie.samples.custom

import android.animation.ValueAnimator
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.TextDelegate
import com.airbnb.lottie.samples.R
import com.airbnb.lottie.samples.databinding.ActivityPlayerThemeBinding
import com.airbnb.lottie.samples.utils.viewBinding

class CustomPlayerActivity : AppCompatActivity() {
    private val binding: ActivityPlayerThemeBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.playerView.setOnClickListener { _ -> Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show() }
        binding.playerView.setRepeatCount(ValueAnimator.INFINITE)

        binding.btnPlay.setOnClickListener { _ ->
//            binding.playerView.setAnimation(R.raw.player)
//            binding.playerView.setTextDelegate(object : TextDelegate(binding.playerView) {
//                init {
//                    setText("L", "8")
//                    setText("C", "8")
//                    setText("R", "8")
//                }
//
//                override fun getText(input: String?): String {
//                    return super.getText(input)
//                }
//            })

            binding.playerView.setImageAssetDelegate(
                LottieAssetDelegate(
                    this@CustomPlayerActivity, "",
                    BitmapFactory.decodeResource(resources, R.drawable.song_cover),
                    "",
                ),
            )
            binding.playerView.setAnimation(R.raw.player2)
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
