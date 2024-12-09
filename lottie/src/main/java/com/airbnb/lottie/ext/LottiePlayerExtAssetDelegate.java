package com.airbnb.lottie.ext;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieImageAsset;

public final class LottiePlayerExtAssetDelegate implements ImageAssetDelegate {

  @NonNull private final String topCoverName;
  @NonNull private final String topCoverId;

  @Nullable private final String bottomCoverName;
  @Nullable private final String bottomCoverId;

  @Nullable private Bitmap topCover;
  @Nullable private Bitmap bottomCover;
  private LottieAnimationView imageView;

  public LottiePlayerExtAssetDelegate(@NonNull LottieAnimationView imageView, @NonNull String topCoverName, @NonNull String topCoverId,
      @Nullable String bottomCoverName, @Nullable String bottomCoverId, @Nullable Bitmap topCover, @Nullable Bitmap bottomCover) {
    this.imageView = imageView;

    this.topCoverName = topCoverName;
    this.topCoverId = topCoverId;

    this.bottomCoverName = bottomCoverName;
    this.bottomCoverId = bottomCoverId;

    this.topCover = topCover;
    this.bottomCover = bottomCover;
  }

  @Override public Bitmap fetchBitmap(LottieImageAsset asset) {
    if (TextUtils.equals(topCoverName, asset.getFileName())) {
      return topCover;
    } else if (TextUtils.equals(bottomCoverName, asset.getFileName())) {
      return bottomCover;
    }
    return null;
  }

  /**
   * 切歌后，上层封面回到起始位置，并切换封面到最新歌曲；下层封面更新为下一首歌封面图.
   */
  public void updateCovers(boolean next, @Nullable Bitmap curBitmap, @Nullable Bitmap nextBitmap) {
    topCover = curBitmap;
    bottomCover = nextBitmap;
    try {
      if (curBitmap != null) {
        imageView.updateBitmap(topCoverId, curBitmap);
      }
      if (!TextUtils.isEmpty(bottomCoverId) && nextBitmap != null) {
        imageView.updateBitmap(bottomCoverId, nextBitmap);
      }
    } catch (Throwable e) {
    }
  }
}
