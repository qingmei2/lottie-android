package com.airbnb.lottie.ext;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY) final class ExtUtils {

  public static Bitmap getLottieBitmap(@Nullable final Bitmap bitmap, final int imgWidth, final int imgType, final int imgRadius) {
    if (bitmap == null || bitmap.isRecycled() || bitmap.getWidth() == 0) {
      return null;
    }
    float scale = (float) imgWidth / bitmap.getWidth();
    Matrix matrix = new Matrix();
    matrix.postScale(scale, scale);
    Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    if (imgType == LottiePlayerConstants.ImageAttr.IMG_SHAPE_CIRCLE) {
      Bitmap circularBitmap = Bitmap.createBitmap(scaledBitmap.getWidth(), scaledBitmap.getHeight(), Bitmap.Config.ARGB_8888);
      Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
      Canvas canvas = new Canvas(circularBitmap);
      canvas.drawColor(Color.TRANSPARENT);

      int centerX = scaledBitmap.getWidth() / 2;
      int centerY = scaledBitmap.getHeight() / 2;
      int radius = Math.min(centerX, centerY);
      canvas.drawCircle(centerX, centerY, radius, paint);

      PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
      paint.setXfermode(xfermode);
      canvas.drawBitmap(scaledBitmap, 0f, 0f, paint);

      scaledBitmap.recycle();

      return circularBitmap;
    } else if (imgType == LottiePlayerConstants.ImageAttr.IMG_SHAPE_ROUND_RECT) {
      // TODO 实现圆角矩形
      return null;
    } else {
      return scaledBitmap;
    }
  }
}
