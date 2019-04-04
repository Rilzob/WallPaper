package cn.com.zzndb.wallpaper.view

import android.widget.ImageView

/**
 * all view interface
 */
interface IView {
    fun showImage(str: String, view: ImageView, fView: ContentFragment)
    fun getHeight(): Int
    fun getWidth(): Int
    fun showMes(str: String)
}

interface IFragmentView {
    fun hideProcessBar()
    fun showProcessBar()
    fun hideImageView()
    fun showImageVIew()
    fun height(): Int
    fun width(): Int
}