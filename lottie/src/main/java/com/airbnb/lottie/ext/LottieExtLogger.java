package com.airbnb.lottie.ext;

import android.util.Log;
import androidx.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class LottieExtLogger {

  private static final String TAG = "LottieLogger";

  public static final boolean enabled = true;

  public static void e(String prefix, String message) {
    Log.e(TAG, prefix + ": " + message);
  }
}
