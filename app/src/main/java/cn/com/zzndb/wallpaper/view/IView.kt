package cn.com.zzndb.wallpaper.view

import android.widget.ImageView

/**
 * all view interface
 */
interface IView {
    fun showImage(view: ImageView)
    fun getHeight(): Int
    fun getWidth(): Int
}