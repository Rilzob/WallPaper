package cn.com.zzndb.wallpaper.presenter

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import cn.com.zzndb.wallpaper.domain.model.ImageCard
import cn.com.zzndb.wallpaper.view.ContentFragment

/**
 * all interface here
 */
interface IPresenter {
    fun getImageUrl(str: String) : String
    fun loadImage(uri: String, image: ImageView, fView: ContentFragment)
    fun downImage(str: String, image: ImageView, fView: ContentFragment)
    fun downImage(str: String, image: ImageView, fView: ContentFragment, force: Boolean)
    fun getIName(url: String) : String
    fun cacheTodayPicInfo(sName: String, fName: String, url: String)
    fun getTodayPic(str: String) : String
    fun getCurrentPic(str: String) : String
    fun sendShareIntent(uri: Uri)
    fun setWallpaper(image: Bitmap)
    fun saveImage(uri: String)
    fun checkWFPermission() : Boolean
    fun getImageCards(): List<ImageCard>
    fun dbgettStr(fName: String): String
    fun dbgetDate(fName: String): String
}

interface DownloadListener {
    fun onProgress(progress: Int)
    fun onSuccess()
    fun onFailed()
}