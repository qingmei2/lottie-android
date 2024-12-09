package com.airbnb.lottie.ext.entity;

import java.util.List;

public class LottiePlayerExtLayer {

  private int type;
  private String desc;
  private List<String> keyPath;
  private int imgShape;
  private int imgRadius;
  private String imgId;
  private LottiePlayerLayerTransform transform;

  public void setType(int type) {
    this.type = type;
  }

  /**
   * 获取图层类型.
   * <p/>
   * {@Link com.airbnb.lottie.ext.LottiePlayerConstants.LayerType}
   */
  public int getType() {
    return type;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }

  public void setKeyPath(List<String> keyPath) {
    this.keyPath = keyPath;
  }

  public List<String> getKeyPath() {
    return keyPath;
  }

  public void setImgShape(int imgShape) {
    this.imgShape = imgShape;
  }

  public int getImgShape() {
    return imgShape;
  }

  public String getImgId() {
    return imgId;
  }

  public void setImgId(String imgId) {
    this.imgId = imgId;
  }

  public int getImgRadius() {
    return imgRadius;
  }

  public void setImgRadius(int imgRadius) {
    this.imgRadius = imgRadius;
  }

  public LottiePlayerLayerTransform getTransform() {
    return transform;
  }

  public void setTransform(LottiePlayerLayerTransform transform) {
    this.transform = transform;
  }
}
