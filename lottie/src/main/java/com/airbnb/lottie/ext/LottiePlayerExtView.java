package com.airbnb.lottie.ext;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.ext.entity.LottiePlayerExtLayer;
import com.airbnb.lottie.ext.entity.LottiePlayerExtModel;
import com.airbnb.lottie.ext.entity.LottiePlayerLayerTransform;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.ScaleXY;

import java.util.List;

public final class LottiePlayerExtView extends LottieAnimationView {

  /**
   * 上层封面动画，有两个阶段，取值范围 [0f, 1f]，分别对应动画的开始和结束.
   */
  @FloatRange(from = 0, to = 1) private float mTopCoverValue = 0f;
  /**
   * 上层封面动画属性.
   */
  @Nullable private LottiePlayerLayerTransform mTopCoverTransform = null;
  /**
   * 上层封面动画.
   */
  @Nullable private ValueAnimator mTopCoverAnimator = null;

  /**
   * 下层封面动画，有两个阶段，取值范围[0f, 1f]，分别对应动画的开始和结束.
   */
  @FloatRange(from = 0, to = 1) private float mBottomCoverValue = 0f;
  /**
   * 下层封面动画属性.
   */
  @Nullable private LottiePlayerLayerTransform mBottomCoverTransform = null;
  /**
   * 下层封面动画.
   */
  @Nullable private ValueAnimator mBottomCoverAnimator = null;

  /**
   * 封面旋转动画，通常是 0 - 360 度循环播放的.
   */
  @FloatRange(from = 0, to = 360) private float mRotateAngel = 0;
  /**
   * 封面旋转动画.
   */
  @Nullable private ValueAnimator mRotateAnimator = null;

  @Nullable
  private LottiePlayerExtAssetDelegate mAssetDelegate = null;

  public LottiePlayerExtView(Context context) {
    super(context);
  }

  public LottiePlayerExtView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public LottiePlayerExtView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setAnimation(@NonNull String name, @NonNull LottiePlayerExtModel extModel) {
    setAnimation(name);
    setExtValueCallbacks(extModel);
  }

  public void onPlayNext(@Nullable Bitmap curBitmap, @Nullable Bitmap nextBitmap) {
    startTopCoverAnimation(true, curBitmap, nextBitmap);
    startBottomCoverAnimation(true);
  }

  public void onPlayPrev(@Nullable Bitmap curBitmap, @Nullable Bitmap nextBitmap) {
    startTopCoverAnimation(false, curBitmap, nextBitmap);
    startBottomCoverAnimation(false);
  }

  @SuppressLint("NewApi") public void onPlayerPlaying() {
    if (mRotateAnimator != null) {
      mRotateAnimator.resume();
    }
  }

  @SuppressLint("NewApi") public void onPlayerStop() {
    if (mRotateAnimator != null) {
      mRotateAnimator.pause();
    }
  }

  private void setExtValueCallbacks(@Nullable LottiePlayerExtModel extModel) {
    if (extModel == null || extModel.getLayers() == null) {
      return;
    }

    String topImgId = null;
    String topImgName = null;
    String bottomImgId = null;
    String bottomImgName = null;

    boolean coverRotate = false;

    @Nullable LottiePlayerLayerTransform topCoverTransform = null;
    @Nullable LottiePlayerLayerTransform bottomCoverTransform = null;

    for (LottiePlayerExtLayer layer : extModel.getLayers()) {
      final int layerType = layer.getType();
      @Nullable final List<String> keyPaths = layer.getKeyPath();
      @Nullable final String desc = layer.getDesc();

      switch (layerType) {
        case LottiePlayerConstants.LayerType.TYPE_IMAGE_RES_TOP:              // 上层图片资源
          topImgId = layer.getImgId();
          if (keyPaths != null && !keyPaths.isEmpty()) {
            topImgName = keyPaths.get(0);
          }
          break;
        case LottiePlayerConstants.LayerType.TYPE_IMAGE_RES_BOTTOM:           // 下层图片资源
          bottomImgId = layer.getImgId();
          if (keyPaths != null && !keyPaths.isEmpty()) {
            bottomImgName = keyPaths.get(0);
          }
          break;
        case LottiePlayerConstants.LayerType.TYPE_IMAGE_LAYER_CONTAINER_TOP:  // 上层封面
          final KeyPath topKeyPath = constructKeyPath(keyPaths);
          if (topKeyPath != null) {
            addValueCallback(topKeyPath, LottieProperty.TRANSFORM_POSITION, new TopCoverPositionTransformCallback());
          }
          topCoverTransform = layer.getTransform();
          break;
        case LottiePlayerConstants.LayerType.TYPE_IMAGE_LAYER_CONTAINER_BOTTOM:  // 下层封面
          final KeyPath bottomKeyPath = constructKeyPath(keyPaths);
          if (bottomKeyPath != null) {
            addValueCallback(bottomKeyPath, LottieProperty.TRANSFORM_SCALE, new BottomCoverScaleTransformCallback());
          }
          bottomCoverTransform = layer.getTransform();
          break;
        case LottiePlayerConstants.LayerType.TYPE_IMAGE_LAYER_ROTATE_COVER:   // 上/下层封面图
          final KeyPath coverKeyPath = constructKeyPath(keyPaths);
          if (coverKeyPath != null) {
            addValueCallback(coverKeyPath, LottieProperty.TRANSFORM_ROTATION, new CoverRotateTransformCallback(desc));
            coverRotate = true;
          }
          break;
      }

      if (topImgId != null && topImgName != null) {
        this.setImageAssetsFolder("images/");
        this.mAssetDelegate = new LottiePlayerExtAssetDelegate(this, topImgName, topImgId, bottomImgName, bottomImgId, null, null);
        this.setImageAssetDelegate(mAssetDelegate);
      }

      this.mTopCoverTransform = topCoverTransform;
      this.mBottomCoverTransform = bottomCoverTransform;

      if (coverRotate) {
        startCoverRotateAnimation();
      } else {
        stopCoverRotateAnimation();
      }
    }
  }

  private void startCoverRotateAnimation() {
    this.stopCoverRotateAnimation();

    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 360f);
    valueAnimator.setDuration(5000L);
    valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
    valueAnimator.setRepeatMode(ValueAnimator.RESTART);
    valueAnimator.setInterpolator(new LinearInterpolator());
    valueAnimator.addUpdateListener(animation -> {
      mRotateAngel = (Float) animation.getAnimatedValue();
    });
    // 启动动画
    valueAnimator.start();
    mRotateAnimator = valueAnimator;
  }

  private void stopCoverRotateAnimation() {
    if (mRotateAnimator != null) {
      mRotateAnimator.cancel();
      mRotateAnimator = null;
      mRotateAngel = 0f;
    }
  }

  private void startTopCoverAnimation(boolean playNext, @Nullable Bitmap curBitmap, @Nullable Bitmap nextBitmap) {
    this.stopTopCoverAnimation();

    if (mTopCoverTransform == null) {
      return;
    }

    if (playNext) {
      mTopCoverAnimator = ValueAnimator.ofFloat(0f, 1f);
    } else if (mTopCoverTransform.isReverse()) {
      mTopCoverAnimator = ValueAnimator.ofFloat(1f, 0f);
    }

    if (mTopCoverAnimator != null) {
      mTopCoverAnimator.setDuration(mTopCoverTransform.getDur());
      mTopCoverAnimator.addUpdateListener(valueAnimator -> {
        this.mTopCoverValue = (float) valueAnimator.getAnimatedValue();
      });
      mTopCoverAnimator.addListener(new AnimatorListenerAdapter() {
        @Override public void onAnimationEnd(Animator animation) {
          mTopCoverValue = 0f;

          if (mAssetDelegate != null) {
            mAssetDelegate.updateCovers(playNext, curBitmap, nextBitmap);
          }
        }
      });
      mTopCoverAnimator.start();
    }
  }

  private void stopTopCoverAnimation() {
    if (mTopCoverAnimator != null) {
      mTopCoverAnimator.cancel();
      mTopCoverAnimator = null;
      mTopCoverValue = 0f;
    }
  }

  private void startBottomCoverAnimation(boolean playNext) {
    this.stopBottomCoverAnimation();

    if (mBottomCoverTransform == null) {
      return;
    }

    if (playNext) {
      mBottomCoverAnimator = ValueAnimator.ofFloat(0f, 1f);
    } else if (mBottomCoverTransform.isReverse()) {
      mBottomCoverAnimator = ValueAnimator.ofFloat(1f, 0f);
    }

    if (mBottomCoverAnimator != null) {
      mBottomCoverAnimator.setDuration(mBottomCoverTransform.getDur());
      mBottomCoverAnimator.addUpdateListener(valueAnimator -> {
        this.mBottomCoverValue = (float) valueAnimator.getAnimatedValue();
      });
    }
  }

  private void stopBottomCoverAnimation() {
    if (mBottomCoverAnimator != null) {
      mBottomCoverAnimator.cancel();
      mBottomCoverAnimator = null;
      mBottomCoverValue = 0f;
    }
  }

  @Nullable private KeyPath constructKeyPath(@Nullable List<String> keyPaths) {
    if (keyPaths == null || keyPaths.isEmpty()) {
      return null;
    }

    if (keyPaths.size() == 1) {
      return new KeyPath(keyPaths.get(0));
    }
    if (keyPaths.size() == 2) {
      return new KeyPath(keyPaths.get(0), keyPaths.get(1));
    }
    if (keyPaths.size() == 3) {
      return new KeyPath(keyPaths.get(0), keyPaths.get(1), keyPaths.get(2));
    }
    return null;
  }

  private class TopCoverPositionTransformCallback extends LottieValueCallback<PointF> {

    public TopCoverPositionTransformCallback() {
    }

    @Nullable @Override public PointF getValue(LottieFrameInfo<PointF> frameInfo) {
      float startX = (frameInfo.getStartValue() != null ? frameInfo.getStartValue().x : 0f);
      float startY = (frameInfo.getStartValue() != null ? frameInfo.getStartValue().y : 0f);
      float endX = (frameInfo.getEndValue() != null ? frameInfo.getEndValue().x : 0f);
      float endY = (frameInfo.getEndValue() != null ? frameInfo.getEndValue().y : 0f);

      PointF result;
      if (mTopCoverValue <= 0f) {
        result = new PointF(startX, startY);
      } else if (mTopCoverValue >= 1f) {
        result = new PointF(endX, endY);
      } else {
        result = new PointF(startX + (endX - startX) * mTopCoverValue, startY + (endY - startY) * mTopCoverValue);
      }

      LottieExtLogger.e("topPosition", result + ", musicPos = " + mTopCoverValue);
      return result;
    }
  }

  private class BottomCoverScaleTransformCallback extends LottieValueCallback<ScaleXY> {

    public BottomCoverScaleTransformCallback() {
    }

    @Nullable @Override public ScaleXY getValue(LottieFrameInfo<ScaleXY> frameInfo) {
      float startX = (frameInfo.getStartValue() != null ? frameInfo.getStartValue().getScaleX() : 0f);
      float startY = (frameInfo.getStartValue() != null ? frameInfo.getStartValue().getScaleY() : 0f);
      float endX = (frameInfo.getEndValue() != null ? frameInfo.getEndValue().getScaleX() : 0f);
      float endY = (frameInfo.getEndValue() != null ? frameInfo.getEndValue().getScaleY() : 0f);

      ScaleXY result;
      if (mBottomCoverValue <= 0f) {
        result = new ScaleXY(0f, 0f);
      } else if (mBottomCoverValue > 1f) {
        result = new ScaleXY(0f, 0f);
      } else {
        result = new ScaleXY(startX + (endX - startX) * mBottomCoverValue, startY + (endY - startY) * mBottomCoverValue);
      }

      LottieExtLogger.e("bottomScale", result + "，pos = " + mBottomCoverValue);
      return result;
    }
  }

  private class CoverRotateTransformCallback extends LottieValueCallback<Float> {

    @Nullable private String desc;

    public CoverRotateTransformCallback(@Nullable String desc) {
      this.desc = desc;
    }

    @Nullable @Override public Float getValue(LottieFrameInfo<Float> frameInfo) {
      LottieExtLogger.e("coverRotate", desc + ", angle value = " + mBottomCoverValue);
      return mBottomCoverValue;
    }
  }
}
