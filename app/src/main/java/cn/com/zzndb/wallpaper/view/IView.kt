package cn.com.zzndb.wallpaper.view

import android.widget.ImageView
import cn.com.zzndb.wallpaper.presenter.DownloadService

/**
 * all view interface
 */
interface IView {
    fun showImage(str: String, view: ImageView, fView: ContentFragment)
    fun getHeight(): Int
    fun getWidth(): Int
    fun showMes(str: String)
    fun getDBinder(): DownloadService.DownloadBinder?
}

interface IFragmentView {
    fun hideProcessBar()
    fun showProcessBar()
    fun hideImageView()
    fun showImageVIew()
    fun height(): Int
    fun width(): Int
}