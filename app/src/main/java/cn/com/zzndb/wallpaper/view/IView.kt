package cn.com.zzndb.wallpaper.view

import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import cn.com.zzndb.wallpaper.presenter.DownloadService
import cn.com.zzndb.wallpaper.presenter.PresenterImpl

/**
 * all view interface
 */
interface IView {
    fun showImage(str: String, view: ImageView, fView: ContentFragment)
    fun showImage(str: String, view: ImageView, fView: ContentFragment, force: Boolean)
    fun getHeight(): Int
    fun getWidth(): Int
    fun showMes(str: String)
    fun getDBinder(): DownloadService.DownloadBinder?
    fun forceLoadImage()
    fun getPresenter(): PresenterImpl
    fun getCurrentFrag(): Fragment
    fun getContext(): Context
    fun requestWFPermission()
    fun checkWFPermission(): Boolean
}

interface IFragmentView {
    fun hideProcessBar()
    fun showProcessBar()
    fun hideImageView()
    fun showImageVIew()
    fun height(): Int
    fun width(): Int
    fun gettStr(): String
    fun getImageView(): ImageView
    fun imagePreView()
}