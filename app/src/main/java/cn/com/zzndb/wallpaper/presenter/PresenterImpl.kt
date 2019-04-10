package cn.com.zzndb.wallpaper.presenter

import android.widget.ImageView
import cn.com.zzndb.wallpaper.domain.commands.getBingUrl
import cn.com.zzndb.wallpaper.domain.commands.getNASAUrl
import cn.com.zzndb.wallpaper.domain.commands.getNGChinaUrl
import cn.com.zzndb.wallpaper.view.ContentFragment
import cn.com.zzndb.wallpaper.view.IView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception

/**
 * the impliment of IPresenter
 * connect model and view
 */
class PresenterImpl(val mView: IView) : IPresenter {

    override fun getImageUrl(str: String) : String {
        return when(str) {
            "Bing" -> getBingUrl(mView.getWidth(), mView.getHeight(), mView).url
            "Ng"   -> getNGChinaUrl().url
            "Nasa" -> getNASAUrl().url
            else -> {
                "https://zzndb.com.cn/3.png"
            }
        }
    }

    // TODO save pic info in database, no need internet access after loaded
    override fun downImage(url: String, image: ImageView, fView: ContentFragment) {
        mView.getDBinder()!!.startDownload(url, this, image, fView)
    }

    override fun loadImage(uri: String, image: ImageView, fView: ContentFragment) {
        mView.showMes("try loading image locally")
        doAsync {
            uiThread {
                Picasso.get()
                    .load("file://$uri")
                    // there may cause random bug: java.lang.IllegalArgumentException: At least one dimension has to be positive number.
                    .resize(fView.width(), fView.height())
                    .centerCrop()
                    .into(image, object : Callback {
                        override fun onError(e: Exception?) {
                            fView.hideProcessBar()
                            mView.showMes("Picasso loading image failed!")
                        }

                        override fun onSuccess() {
                            fView.showImageVIew()
                            fView.hideProcessBar()
                            mView.showMes(fView.width().toString() + "+" + fView.height().toString())
                        }

                    })
            }
        }
    }

    override fun getIName(url: String): String {
        // parse url to get file name or just Name the file cache
        val regex = "[0-9a-zA-Z_?-]*.(jpg|jpeg|png)".toRegex()
        val result = regex.find(url)?.value
        if (result != null) {
            return result.toString()
        }
        else {
            mView.showMes("parse url get file name error, save file directly")
            return url
        }
    }
}
