package com.airbnb.lottie.ext;

import android.content.Context;
import android.util.AttributeSet;
import com.airbnb.lottie.LottieAnimationView;

public class LottieAnimExtView extends LottieAnimationView {

  public LottieAnimExtView(Context context) {
    super(context);
  }

  public LottieAnimExtView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public LottieAnimExtView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setAnimation(String name, String ext) {
    setAnimation(name + "." + ext);
  }
}
