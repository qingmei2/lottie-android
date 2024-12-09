package com.airbnb.lottie.ext;

public interface LottiePlayerEventCallback {

  void onPlayPrepared();

  void onPlayStarted();

  void onPlayPause();

  void onPlayResumed();

  void onPlayEnd();
}
