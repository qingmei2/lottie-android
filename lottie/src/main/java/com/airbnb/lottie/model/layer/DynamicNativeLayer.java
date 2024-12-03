package com.airbnb.lottie.model.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.utils.DropShadow;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;

public class DynamicNativeLayer extends BaseLayer {

  @Nullable private final LottieImageAsset lottieImageAsset;
  @Nullable private BaseKeyframeAnimation<ColorFilter, ColorFilter> colorFilterAnimation;
  @Nullable private BaseKeyframeAnimation<Bitmap, Bitmap> imageAnimation;
  private final View dynamicView;

  private final Rect src = new Rect();
  private final Rect dst = new Rect();

  DynamicNativeLayer(LottieDrawable lottieDrawable, Layer layerModel, @NonNull View dynamicView) {
    super(lottieDrawable, layerModel);
    lottieImageAsset = lottieDrawable.getLottieImageAssetForId(layerModel.getRefId());
    this.dynamicView = dynamicView;
  }

  /**
   * 将 View 转换为 Bitmap
   */
  private Bitmap captureView(View view) {
    int width = view.getWidth();
    int height = view.getHeight();
    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    view.draw(canvas);

    // 缩放bitmap为指定宽高，这个宽高需要和 json 中的宽高一致.
    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 413, 413, true);
    return scaledBitmap;
  }

  @Override public void getBounds(RectF outBounds, Matrix parentMatrix, boolean applyParents) {
    super.getBounds(outBounds, parentMatrix, applyParents);

  }

  @Override void drawLayer(Canvas canvas, Matrix parentMatrix, int parentAlpha, @Nullable DropShadow shadowToApply) {
    final Bitmap bitmap = captureView(dynamicView);
    if (bitmap == null) {
      return;
    }
    canvas.save();
    canvas.concat(parentMatrix);

    int viewWidth = bitmap.getWidth();
    int viewHeight = bitmap.getHeight();

    float density = Utils.dpScale();
    int targetWidth = (int) (viewWidth * density);
    int targetHeight = (int) (viewHeight * density);

    // 设置源矩形和目标矩形
    src.set(0, 0, viewWidth, viewHeight);
    dst.set(0, 0, targetWidth, targetHeight);

    // 绘制 dynamicView 到 Canvas
    canvas.drawBitmap(bitmap, src, dst, null);

    canvas.restore();
  }

  @SuppressWarnings("SingleStatementInBlock")
  @Override
  public <T> void addValueCallback(T property, @Nullable LottieValueCallback<T> callback) {
    super.addValueCallback(property, callback);
    if (property == LottieProperty.COLOR_FILTER) {
      if (callback == null) {
        colorFilterAnimation = null;
      } else {
        //noinspection unchecked
        colorFilterAnimation =
            new ValueCallbackKeyframeAnimation<>((LottieValueCallback<ColorFilter>) callback);
      }
    } else if (property == LottieProperty.IMAGE) {
      if (callback == null) {
        imageAnimation = null;
      } else {
        //noinspection unchecked
        imageAnimation =
            new ValueCallbackKeyframeAnimation<>((LottieValueCallback<Bitmap>) callback);
      }
    }
  }
}
