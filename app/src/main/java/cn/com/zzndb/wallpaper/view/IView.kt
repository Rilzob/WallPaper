package cn.com.zzndb.wallpaper.view

import android.widget.ImageView

/**
 * all view interface
 */
interface IView {
    fun showImage(str: String, view: ImageView)
    fun getHeight(): Int
    fun getWidth(): Int
    fun showMes(str: String)
}