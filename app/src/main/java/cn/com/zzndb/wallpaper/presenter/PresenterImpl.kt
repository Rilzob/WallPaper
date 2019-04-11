package cn.com.zzndb.wallpaper.presenter

import android.annotation.SuppressLint
import android.widget.ImageView
import cn.com.zzndb.wallpaper.domain.commands.getBingUrl
import cn.com.zzndb.wallpaper.domain.commands.getNASAUrl
import cn.com.zzndb.wallpaper.domain.commands.getNGChinaUrl
import cn.com.zzndb.wallpaper.domain.db.PicDb
import cn.com.zzndb.wallpaper.view.ContentFragment
import cn.com.zzndb.wallpaper.view.IView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * the implement of IPresenter
 * connect model and view
 */
class PresenterImpl(val mView: IView, val picDb: PicDb) : IPresenter {

    // return image url
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

    // download image or load from external storage
    // TODO fix bing rotate load fit screen image
    override fun downImage(str: String, image: ImageView, fView: ContentFragment) {
        // get today pic uri if exist
        val uri = getTodayPic(str)
        if (uri != str) {
            loadImage(uri, image, fView)
        }
        else {
            mView.getDBinder()!!.startDownload(uri, this, image, fView)
        }
    }

    // show image
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

    // return image name from url
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

    // save cache info only once a day a image
    @SuppressLint("SimpleDateFormat")
    override fun cacheTodayPicInfo(sName: String, fName: String) {
        val date = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        // save once
        if (!picDb.checkExist(fName)) {
            picDb.saveDailyPicInfo(date, sName, fName)
        }
    }

    // return str or uri
    @SuppressLint("SimpleDateFormat")
    override fun getTodayPic(str: String) : String {
        val date = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        return picDb.checkTodayPicInfo(date, str)
    }
}
