package cn.com.zzndb.wallpaper.presenter

import android.widget.ImageView
import cn.com.zzndb.wallpaper.view.ContentFragment

/**
 * all interface here
 */
interface IPresenter {
    fun getImageUrl(str: String) : String
    fun loadImage(url: String, image: ImageView, fView: ContentFragment)
}