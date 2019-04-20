package cn.com.zzndb.wallpaper.view

import android.content.Context
import android.content.SharedPreferences
import android.widget.ImageView
import androidx.fragment.app.Fragment
import cn.com.zzndb.wallpaper.domain.model.ImageCard
import cn.com.zzndb.wallpaper.presenter.DownloadService
import cn.com.zzndb.wallpaper.presenter.PresenterImpl

/**
 * all view interface
 */
interface IView {
    fun showImage(str: String, view: ImageView, fView: ContentFragment, force: Boolean)
    fun getHeight(): Int
    fun getWidth(): Int
    fun showMes(str: String, length: Int)
    fun getDBinder(): DownloadService.DownloadBinder?
    fun reloadFragment()
    fun getPresenter(): PresenterImpl
    fun getCurrentFrag(): Fragment
    fun getContext(): Context
    fun requestWFPermission()
    fun checkWFPermission(): Boolean
    fun getSharedPreference(): SharedPreferences
    fun getWBinder(): WallpaperChange.WallBinder?
    fun checkNetConnection() : Boolean
}

interface IContentFragmentView {
    fun hideProcessBar()
    fun showProcessBar()
    fun hideImageView()
    fun showImageVIew()
    fun gettStr(): String
    fun getImageView(): ImageView
    fun imagePreView()
}

interface IMineFragmentView {
    fun imagePreView(uris: List<String>, tStr: String, image: ImageView, date: String)
    fun getList() : List<ImageCard>
    fun updateMineFragment()
    fun deleteImage(uri: String, postion: Int)
}
