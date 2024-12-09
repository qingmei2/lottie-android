package com.airbnb.lottie.ext.entity;

public final class LottiePlayerLayerTransform {

  private int type;
  private String desc;
  private int dur;
  private int reverse;

  public void setType(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }

  public void setDur(int dur) {
    this.dur = dur;
  }

  public int getDur() {
    return dur;
  }

  public void setReverse(int reverse) {
    this.reverse = reverse;
  }

  public boolean isReverse() {
    return reverse == 1;
  }

  @Override public String toString() {
    return "transform {" +
        "type=" + type +
        ", desc='" + desc + '\'' +
        ", dur=" + dur +
        ", reverse=" + reverse +
        '}';
  }
}
