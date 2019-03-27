package cn.com.zzndb.wallpaper.presenter

import android.widget.ImageView

/**
 * all interface here
 */
interface IPresenter {
    fun getImageUrl() : String
    fun loadImage(url: String, image: ImageView)
}