package cn.com.zzndb.wallpaper.presenter

import android.widget.ImageView
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
    fun cacheTodayPicInfo(sName: String, fName: String)
    fun getTodayPic(str: String) : String
}

interface DownloadListener {
    fun onProgress(progress: Int)
    fun onSuccess()
    fun onFailed()
}