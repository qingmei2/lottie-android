package com.airbnb.lottie.ext.entity;

import java.util.List;

public final class LottiePlayerExtModel {

  private List<LottiePlayerExtLayer> layers;

  public void setLayers(List<LottiePlayerExtLayer> layers) {
    this.layers = layers;
  }

  public List<LottiePlayerExtLayer> getLayers() {
    return layers;
  }

  @Override public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("LottiePlayerExtModel{");
    sb.append("layers=");
    if (layers != null) {
      sb.append('[');
      for (int i = 0; i < layers.size(); i++) {
        sb.append(layers.get(i).toString());
        if (i < layers.size() - 1) {
          sb.append(", ");
        }
      }
      sb.append(']');
    } else {
      sb.append("null");
    }
    sb.append('}');
    return sb.toString();
  }
}
