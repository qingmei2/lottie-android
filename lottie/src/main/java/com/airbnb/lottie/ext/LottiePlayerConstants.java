package com.airbnb.lottie.ext;

public interface LottiePlayerConstants {

  public interface LayerType {
    /**
     * 上层图片资源.
     * <p/>
     * 资源替换的标记，展示当前歌曲封面
     */
    int TYPE_IMAGE_RES_TOP = 1;

    /**
     * 下层图片资源.
     * <p/>
     * 资源替换的标记，展示下一首歌曲封面
     */
    int TYPE_IMAGE_RES_BOTTOM = 2;

    /**
     * 上层封面.
     * <p/>
     * 上层容器，有出入场动画，动画结束，回到初始位置
     */
    int TYPE_IMAGE_LAYER_CONTAINER_TOP = 3;

    /**
     * 下层封面
     * <p/>
     * 下层容器，有出入场动画，动画结束隐藏
     */
    int TYPE_IMAGE_LAYER_CONTAINER_BOTTOM = 4;

    /**
     * 上/下层封面图
     * <p/>
     * 封面旋转动画
     */
    int TYPE_IMAGE_LAYER_ROTATE_COVER = 5;
  }

  public interface ImageAttr {
    /**
     * 圆形
     */
    int IMG_SHAPE_CIRCLE = 1;

    /**
     * 圆角矩形
     */
    int IMG_SHAPE_ROUND_RECT = 2;

    /**
     * 正方形（默认）
     */
    int IMG_SHAPE_RECT = 0;
  }

  public interface LayerTransform {

    /**
     * 位移动画
     */
    int TRANSFORM_POSITION = 1;

    /**
     * 缩放动画
     */
    int TRANSFORM_SCALE = 2;

    /**
     * 旋转动画
     */
    int TRANSFORM_ROTATE = 3;
  }
}
